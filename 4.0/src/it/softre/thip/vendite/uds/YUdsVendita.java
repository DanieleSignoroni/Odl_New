package it.softre.thip.vendite.uds;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.DB2Database;
import com.thera.thermfw.persist.Database;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.persist.SQLServerJTDSNoUnicodeDatabase;

import it.softre.thip.base.uds.YTipoUds;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.produzione.ordese.OrdineEsecutivo;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 21/03/2024
 * <br><br>
 * <b>70687	DSSOF3 04/10/2022</b>    
 * <p>Prima stesura.</p>
 * <b>71469	DSSOF3	11/03/2024</b>  
 * <p>
 * Aggiungere {@link #ricalcoloPesi()}.<br>
 * Aggiungere {@link #getNewProgressivoForBancale()},{@link #getNewProgressivoForScatola()}.<br>
 * Aggiungere {@link #getRigheUDSVenditaDaFigli()}.<br>
 * Aggiungere {@link #controllaUdsSecondoLivello(String[])}.<br>
 * </p>
 */

public class YUdsVendita extends YUdsVenditaPO {

	public static final char IN_CORSO = '0';
	public static final char COMPLETATO = '1';
	public static final char VERSATO_A_MAGAZZINO = '2';
	public static final char GENERATO_DOCUMENTO = '3';

	public static final char ST_ETI_NON_STAMPATA = '0'; //71469
	public static final char ST_ETI_STAMPATA = '1';	//71469

	protected static final String STMT_UDS_PADRE_CLG = "SELECT * "
			+ "FROM "+YUdsVenditaTM.TABLE_NAME+" U "
			+ "WHERE U."+YUdsVenditaTM.ID_AZIENDA+" = ? "
			+ "AND U."+YUdsVenditaTM.R_ID_UDS_PADRE+" = ? "; //71469

	public static CachedStatement cUdsPadreCollegate = new CachedStatement(STMT_UDS_PADRE_CLG); //71469

	protected static final String STMT_ESIST_UDS_FIGLIE_CON_RIGHE = "SELECT * FROM SOFTRE.YUDS_VEN_RIG R  "
			+ "INNER JOIN SOFTRE.YUDS_VEN_TES T "
			+ "ON R.ID_AZIENDA = T.ID_AZIENDA  "
			+ "AND R.ID_UDS = T.ID_UDS  "
			+ "WHERE T.ID_AZIENDA = ? AND T.R_ID_UDS_PADRE = ? "
			+ "AND R.ID_UDS IS NOT NULL"; //71469

	public static CachedStatement cEsistonoUdsFiglieConRows = new CachedStatement(STMT_ESIST_UDS_FIGLIE_CON_RIGHE); //71469

	protected boolean iCopia;

	protected boolean assegnaNumeratoreAutomatico = true; //71469

	protected boolean isRicalcoloPesi = false; //71469

	public boolean isRicalcoloPesi() {
		return isRicalcoloPesi;
	}

	public void setRicalcoloPesi(boolean isRicalcoloPesi) {
		this.isRicalcoloPesi = isRicalcoloPesi;
	}

	public boolean isAssegnaNumeratoreAutomatico() {
		return assegnaNumeratoreAutomatico;
	}

	public void setAssegnaNumeratoreAutomatico(boolean assegnaNumeratoreAutomatico) {
		this.assegnaNumeratoreAutomatico = assegnaNumeratoreAutomatico;
	}

	public boolean isCopia() {
		return iCopia;
	}

	public void setCopia(boolean iCopia) {
		this.iCopia = iCopia;
	}

	public YUdsVendita() {
		setIdUds("0");
		this.setDataUds(TimeUtils.getCurrentDate());
	}

	public YUdsVendita(OrdineEsecutivo ordEsec, YTipoUds tipoUds) {
		this.setRTipoUds(tipoUds.getIdTipoUds());
		this.setDataUds(TimeUtils.getCurrentDate());
		this.setRCliente(ordEsec.getIdCliente());
		this.setPesoUds(tipoUds.getPeso());
		this.setVolume(tipoUds.getVolume());
		this.setAltezza(tipoUds.getAltezza());
		this.setLunghezza(tipoUds.getLunghezza());
		this.setLarghezza(tipoUds.getLarghezza());
	}

	@SuppressWarnings("rawtypes")
	@Override
	public int save() throws SQLException {
		Iterator iterRighe = this.getRigheUDSVendita().iterator();
		while (iterRighe.hasNext()) {
			YUdsVenRig udsVenRig = (YUdsVenRig) iterRighe.next();
			udsVenRig.setUdsPadre(this.getRIdUdsPadre());
			//udsVenRig.save();
		}
		if (!isOnDB()) {
			if(isAssegnaNumeratoreAutomatico())
				this.setIdUds(getNewProgressivo());
			compilaMisureNuovo();
			setRicalcoloPesi(true);
		}
		if(isRicalcoloPesi()) {
			ricalcoloPesi();
		}
		return super.save();
	}

	protected void ricalcoloPesi() throws SQLException {
		if(getIdUds().startsWith("PA")) {
			//i pesi derivano da altre uds che hanno come uds padre la questa uds
			//quindi li devo prendere da database
			BigDecimal[] pesi = getPesiImballiFigli();
			setPesoLordo(pesi[0]);
			setPesoNetto(pesi[1]);
			setPesoUds(pesi[2]);
		}else {
			BigDecimal[] pesi = getPesiTotali();
			setPesoLordo(pesi[0]);
			setPesoNetto(pesi[1]);
			setPesoUds(pesi[2]);
		}
	}

	protected BigDecimal[] getPesiImballiFigli() {
		YTipoUds tipoUds = null;
		BigDecimal pesoLordo = BigDecimal.ZERO;
		BigDecimal pesoNetto = BigDecimal.ZERO;
		BigDecimal pesoUds = BigDecimal.ZERO;
		try {
			tipoUds = (YTipoUds) YTipoUds.elementWithKey(YTipoUds.class,KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), this.getRTipoUds() }), 0);
			for(YUdsVenRig udsVenRig : getRigheUDSVenditaDaFigli()) {
				pesoLordo = pesoLordo.add(udsVenRig.getPesoLordo() != null ? udsVenRig.getPesoLordo() : BigDecimal.ZERO);
				pesoNetto = pesoNetto.add(udsVenRig.getPesoNetto() != null ? udsVenRig.getPesoLordo() : BigDecimal.ZERO);
			}
			if(tipoUds != null && tipoUds.getPeso() != null) {
				pesoLordo = pesoLordo.add(tipoUds.getPeso() != null ? tipoUds.getPeso() : BigDecimal.ZERO);
				pesoUds = pesoUds.add(tipoUds.getPeso() != null ? tipoUds.getPeso() : BigDecimal.ZERO);
			}
		}catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return new BigDecimal[] {pesoLordo,pesoNetto,pesoUds};
	}

	@SuppressWarnings("rawtypes")
	protected BigDecimal[] getPesiTotali() throws SQLException {
		YTipoUds tipoUds = (YTipoUds) YTipoUds.elementWithKey(YTipoUds.class,KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), this.getRTipoUds() }), 0);
		BigDecimal pesoLordo = BigDecimal.ZERO;
		BigDecimal pesoNetto = BigDecimal.ZERO;
		BigDecimal pesoUds = BigDecimal.ZERO;
		Iterator iterRighe = this.getRigheUDSVendita().iterator();
		while (iterRighe.hasNext()) {
			YUdsVenRig udsVenRig = (YUdsVenRig) iterRighe.next();
			pesoLordo = pesoLordo.add(udsVenRig.getPesoLordo() != null ? udsVenRig.getPesoLordo() : BigDecimal.ZERO);
			pesoNetto = pesoNetto.add(udsVenRig.getPesoNetto() != null ? udsVenRig.getPesoLordo() : BigDecimal.ZERO);
		}
		if(tipoUds.getPeso() != null) {
			pesoLordo = pesoLordo.add(tipoUds.getPeso() != null ? tipoUds.getPeso() : BigDecimal.ZERO);
			pesoUds = pesoUds.add(tipoUds.getPeso() != null ? tipoUds.getPeso() : BigDecimal.ZERO);
		}
		return new BigDecimal[] {pesoLordo,pesoNetto,pesoUds};
	}

	public void compilaMisureNuovo() throws SQLException {
		YTipoUds tipoUds = (YTipoUds) YTipoUds.elementWithKey(YTipoUds.class,
				KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), this.getRTipoUds() }), 0);
		if (tipoUds != null) {
			if (this.getPesoUds() == null)
				this.setPesoUds(tipoUds.getPeso() != null ? tipoUds.getPeso() : null);

			if (this.getAltezza() == null)
				this.setAltezza(tipoUds.getAltezza() != null ? tipoUds.getAltezza() : null);

			if (this.getVolume() == null)
				this.setVolume(tipoUds.getVolume() != null ? tipoUds.getVolume() : null);

			if (this.getLarghezza() == null)
				this.setLarghezza(tipoUds.getLarghezza() != null ? tipoUds.getLarghezza() : null);

			if (this.getLunghezza() == null)
				this.setLunghezza(tipoUds.getLunghezza() != null ? tipoUds.getLunghezza() : null);
		}
	}

	@Override
	public int delete() throws SQLException {
		int rc = super.delete();
		return rc;
	}

	public ErrorMessage checkStatoBeforeDelete() {
		ErrorMessage em = null;
		if (this.getStatoEvasione() == '1') {
			em = new ErrorMessage("YSOFTRE001", "Impossibile eliminare un UDS in stato 'COMPLETATO'");
		} else if (this.getStatoEvasione() == '2') {
			em = new ErrorMessage("YSOFTRE001", "Impossibile eliminare un UDS versata a magazzino");
		} else if (this.getStatoEvasione() == '3') {
			em = new ErrorMessage("YSOFTRE001", "Impossibile eliminare, esiste un documento collegato");
		}
		return em;
	}

	@Override
	public ErrorMessage checkDelete() {
		ErrorMessage em = checkStatoBeforeDelete();
		if(em == null) {
			//em = checkUdsPadreCollegato();
		}
		return em;
	}

	public ErrorMessage checkUdsPadreCollegato() {
		ResultSet rs = null;
		try {
			PreparedStatement ps = cUdsPadreCollegate.getStatement();
			Database db = ConnectionManager.getCurrentDatabase();
			db.setString(ps, 1, this.getIdAzienda());                  
			db.setString(ps, 2, this.getIdUds());
			rs = ps.executeQuery();
			if (rs.next())
				return new ErrorMessage("YSOFTRE001","Non e' possibile cancellare l'uds, e' un uds padre!");
		}catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace(Trace.excStream);
				}
			}
		}
		return null;
	}

	protected static String getNewProgressivo() {
		String newProg = "1";
		try {
			Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);
			String prefAnno = String.valueOf(year).substring(2);
			String select = "";
			if (ConnectionManager.getCurrentDatabase() instanceof SQLServerJTDSNoUnicodeDatabase) {
				select = "SELECT COALESCE(MAX(SUBSTRING(ID_UDS,3,9))+1,1) " + "FROM SOFTRE.YUDS_VEN_TES "
						+ "WHERE SUBSTRING(ID_UDS,1,2) = '" + prefAnno + "' ";
			} else if (ConnectionManager.getCurrentDatabase() instanceof DB2Database) {
				select = "SELECT COALESCE(MAX(SUBSTRING(ID_UDS,3,9,CODEUNITS32))+1,1) " + "FROM SOFTRE.YUDS_VEN_TES "
						+ "WHERE SUBSTRING(ID_UDS,1,2,CODEUNITS32) = '" + prefAnno + "' ";
			}
			CachedStatement cs = new CachedStatement(select);
			ResultSet rs = cs.executeQuery();
			if (rs.next()) {
				newProg = rs.getString(1).trim();
			}
			while (newProg.length() < 7) {
				newProg = 0 + newProg;
			}
			newProg = prefAnno + newProg;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return newProg;
	}

	public static String getNewProgressivoForBancale() {
		String newProg = "1";
		try {
			String select = "";
			if (ConnectionManager.getCurrentDatabase() instanceof SQLServerJTDSNoUnicodeDatabase) {
				select = "SELECT COALESCE(MAX(SUBSTRING(ID_UDS,3,9))+1,1) " + "FROM SOFTRE.YUDS_VEN_TES "
						+ "WHERE SUBSTRING(ID_UDS,1,2) = 'PA' ";
			} else if (ConnectionManager.getCurrentDatabase() instanceof DB2Database) {
				select = "SELECT COALESCE(MAX(SUBSTRING(ID_UDS,3,9,CODEUNITS32))+1,1) " + "FROM SOFTRE.YUDS_VEN_TES "
						+ "WHERE SUBSTRING(ID_UDS,1,2,CODEUNITS32) = 'PA' ";
			}
			CachedStatement cs = new CachedStatement(select);
			ResultSet rs = cs.executeQuery();
			if (rs.next()) {
				newProg = rs.getString(1).trim();
			}
			while (newProg.length() < 7) {
				newProg = 0 + newProg;
			}
			newProg = "PA" + newProg;
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return newProg;
	}

	public static String getNewProgressivoForScatola() {
		String newProg = "1";
		try {
			String select = "";
			if (ConnectionManager.getCurrentDatabase() instanceof SQLServerJTDSNoUnicodeDatabase) {
				select = "SELECT COALESCE(MAX(SUBSTRING(ID_UDS,2,9))+1,1) " + "FROM SOFTRE.YUDS_VEN_TES "
						+ "WHERE SUBSTRING(ID_UDS,1,1) = 'S' ";
			} else if (ConnectionManager.getCurrentDatabase() instanceof DB2Database) {
				select = "SELECT COALESCE(MAX(SUBSTRING(ID_UDS,2,9,CODEUNITS32))+1,1) " + "FROM SOFTRE.YUDS_VEN_TES "
						+ "WHERE SUBSTRING(ID_UDS,1,1,CODEUNITS32) = 'S' ";
			}
			CachedStatement cs = new CachedStatement(select);
			ResultSet rs = cs.executeQuery();
			if (rs.next()) {
				newProg = rs.getString(1).trim();
			}
			while (newProg.length() < 8) {
				newProg = 0 + newProg;
			}
			newProg = "S" + newProg;
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return newProg;
	}

	public void convalidaUdsVendita(String note) {
		this.setNote(note);
		this.setStatoEvasione('1');
	}

	public void regressioneUdsVendita() {
		this.setStatoEvasione('0');
	}

	public void rendiDefinitivaUdsVendita() {
		this.setStatoEvasione('3');
	}

	public boolean esistonoUdsFiglieCollegate() {
		ResultSet rs = null;
		try {
			PreparedStatement ps = cEsistonoUdsFiglieConRows.getStatement();
			Database db = ConnectionManager.getCurrentDatabase();
			db.setString(ps, 1, this.getIdAzienda());                  
			db.setString(ps, 2, this.getIdUds());
			rs = ps.executeQuery();
			if (rs.next())
				return true;
		}catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace(Trace.excStream);
				}
			}
		}
		return false;
	}

	public List<YUdsVenRig> getRigheUDSVenditaDaFigli() throws SQLException {
		List<YUdsVenRig> list = new ArrayList<YUdsVenRig>();
		List<String> keys = new ArrayList<String>();
		ResultSet rs = null;
		try {
			PreparedStatement ps = cEsistonoUdsFiglieConRows.getStatement();
			Database db = ConnectionManager.getCurrentDatabase();
			db.setString(ps, 1, this.getIdAzienda());                  
			db.setString(ps, 2, this.getIdUds());
			rs = ps.executeQuery();
			while (rs.next())
				keys.add(KeyHelper.buildObjectKey(new String[] {
						rs.getString(YUdsVenRigTM.ID_AZIENDA),
						rs.getString(YUdsVenRigTM.ID_UDS),
						rs.getString(YUdsVenRigTM.ID_RIGA_UDS)
				}));
		}catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			if(rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace(Trace.excStream);
				}
			}
		}
		for(String c : keys) {
			list.add((YUdsVenRig) YUdsVenRig.elementWithKey(YUdsVenRig.class,c, PersistentObject.NO_LOCK));
		}
		return list;
	}

	public static ErrorMessage controllaUdsSecondoLivello(String[] chiaviSel) {
		for(String key : chiaviSel) {
			try {
				YUdsVendita uds = (YUdsVendita) YUdsVendita.elementWithKey(YUdsVendita.class, key, PersistentObject.NO_LOCK);
				if(uds.getRIdUdsPadre() != null) {
					return new ErrorMessage("YSOFTRE001","L'UDS : "+uds.getKey()+" e' gia' parte del pallet: "+uds.getRIdUdsPadre());
				}
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		return null;
	}

}