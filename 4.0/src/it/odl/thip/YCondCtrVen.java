package it.odl.thip;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.thera.thermfw.common.*;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.Column;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Database;

import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.documenti.StatoAvanzamento;
import it.thera.thip.cs.DatiComuniEstesi;

import com.thera.thermfw.base.*;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 04/04/2024
 * <br><br>
 * <b></b>
 * <p>
 * Prima stesura.<br>
 * </p>
 */

public class YCondCtrVen extends YCondCtrVenPO {
	
	public static final char CONTRATTUALIZZATO = '1';
	public static final char PREVISIVO = '0';

	protected static final String STMT_SELECT_QTA_ORD = "SELECT "
			+ "    SUM( "
			+ "        CASE "
			+ "            WHEN R.SALDO_MANUALE = ? THEN (R.QTA_P_EVA_UM_PRM + R.QTA_A_EVA_UM_PRM + R.QTA_SPE_UM_PRM) "
			+ "            ELSE R.QTA_ORD_UM_PRM "
			+ "        END "
			+ "    ) AS QTA_ORD, "
			+ "    SUM(R.QTA_SPE_UM_PRM) AS QTA_SPE "
			+ "FROM "
			+ "    THIP.ORD_VEN_RIG R "
			+ "INNER JOIN THIPPERS.YORD_VEN_RIG E "
			+ "ON "
			+ "    R.ID_AZIENDA = E.ID_AZIENDA "
			+ "AND R.ID_ANNO_ORD = E.ID_ANNO_ORD "
			+ "AND R.ID_NUMERO_ORD = E.ID_NUMERO_ORD "
			+ "AND R.ID_RIGA_ORD = E.ID_RIGA_ORD "
			+ "WHERE R.ID_AZIENDA = ? "
			+ "AND E.ID_CONTRATO = ? "
			+ "AND R.STATO_AVANZAMENTO = ? "
			+ "AND R.STATO = ?";

	protected static CachedStatement csSelectQtaOrd = new CachedStatement(STMT_SELECT_QTA_ORD);

	protected BigDecimal iPrezzoContratto;

	public BigDecimal getPrezzoContratto() {
		return iPrezzoContratto;
	}

	public void setPrezzoContratto(BigDecimal iPrezzoContratto) {
		this.iPrezzoContratto = iPrezzoContratto;
	}

	public ErrorMessage checkDelete() {
		return null;
	}

	public int save() throws SQLException {
		if (!isOnDB()) {
			try {
				setIdReg(new Integer(Numerator.getNextInt("YCondCtrVen")));
			}
			catch(NumeratorException e) {e.printStackTrace(Trace.excStream);}
		}
		return super.save();
	}

	protected BigDecimal[] getQuantitaTotaliOrdinePerContratto(Integer idReg) {
		ResultSet rs = null;
		try{
			PreparedStatement ps = csSelectQtaOrd.getStatement();
			Database db = ConnectionManager.getCurrentDatabase();
			db.setString(ps, 1, String.valueOf(Column.TRUE_CHAR));
			db.setString(ps, 2, Azienda.getAziendaCorrente());
			db.setString(ps, 3, String.valueOf(idReg));
			db.setString(ps, 4, String.valueOf(StatoAvanzamento.DEFINITIVO));
			db.setString(ps, 5, String.valueOf(DatiComuniEstesi.VALIDO));
			rs = ps.executeQuery();
			if (rs.next()){
				return new BigDecimal[]{
						rs.getBigDecimal(1) != null ? rs.getBigDecimal(1) : BigDecimal.ZERO,
								rs.getBigDecimal(2) != null ? rs.getBigDecimal(2) : BigDecimal.ZERO
				};
			}
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally{
			try{
				if(rs != null)
					rs.close();
			}catch(SQLException e){
				e.printStackTrace(Trace.excStream);
			}
		}
		return null;

	}

	/**
	 * @author Daniele Signoroni
	 * <p>
	 * Si occupa di aggiornare le condizioni contrattuali di un contratto.<br></br>
	 * A partire dal contratto vengono recuperate le quantita tramite {@link #getQuantitaTotaliOrdinePerContratto(Integer)}.<br></br>
	 * In seguito vengono aggiornate:<br>
	 * {@link #setQtaconsegnata(BigDecimal)} <br>
	 * {@link #setQtaordinata(BigDecimal)} <br>
	 * {@link #setPerccompletam(BigDecimal)} <br>
	 * </p>
	 */
	public void aggiornaCondizioniContrattuali() {
		BigDecimal[] quantita = getQuantitaTotaliOrdinePerContratto(getIdReg());
		if(quantita != null) {
			BigDecimal quantitaOrdinata = quantita[0];
			BigDecimal quantitaSpedita = quantita[1];
			setQtaconsegnata(quantitaSpedita);
			setQtaordinata(quantitaOrdinata);

			BigDecimal percentualeComplet = getQtaordinata().divide(getQtaprevista(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			percentualeComplet = percentualeComplet.setScale(2, BigDecimal.ROUND_HALF_UP);
			setPerccompletam(percentualeComplet);
		}
	}

	/**
	 * @author Daniele Signoroni
	 * <p>
	 * Ritorna una lista di contratti valdi nella data odierna.<br>
	 * I contratti non sono saldi manuali, sono validi e hanno ancora della quantita' prevista.
	 * </p>
	 * @return un vettore di YCondCtrVen gia loaddati da DB
	 */
	@SuppressWarnings("unchecked")
	protected static Vector<YCondCtrVen> recuperaContrattiValidiOggi(){
		Vector<YCondCtrVen> contratti = new Vector<YCondCtrVen>();
		SimpleDateFormat db2Fmt = new SimpleDateFormat("yyyy-MM-dd");
		String where = " "+YCondCtrVenTM.ID_AZIENDA+" = '"+Azienda.getAziendaCorrente()+"' AND " + 
				" ("+YCondCtrVenTM.QTAPREVISTA+" - "+YCondCtrVenTM.QTAORDINATA+" > 0) AND " + 
				" "+YCondCtrVenTM.STATO+" = '"+DatiComuniEstesi.VALIDO+"' AND " + 
				" "+YCondCtrVenTM.SALDO_MANUALE+" = '"+Column.FALSE_CHAR+"' ";
		where += " AND '"+db2Fmt.format(TimeUtils.getCurrentDate())+"' ";
		where += " BETWEEN "+YCondCtrVenTM.DATAINIZIO+" AND "+YCondCtrVenTM.DATAFINE+" ";
		String orderBy = " "+YCondCtrVenTM.DATA_CONTRATTO+" ASC, "+YCondCtrVenTM.ID_REGISTRAZIONE+" ASC ";
		try {
			contratti = YCondCtrVen.retrieveList(YCondCtrVen.class, where, orderBy, false);
		} catch (ClassNotFoundException e) {
			e.printStackTrace(Trace.excStream);
		} catch (InstantiationException e) {
			e.printStackTrace(Trace.excStream);
		} catch (IllegalAccessException e) {
			e.printStackTrace(Trace.excStream);
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return contratti;
	}

}

