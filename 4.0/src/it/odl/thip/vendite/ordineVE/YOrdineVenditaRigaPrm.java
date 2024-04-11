package it.odl.thip.vendite.ordineVE;

import java.math.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Vector;

import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.common.BaseComponentsCollection;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.CachedStatement;
import com.thera.thermfw.persist.Column;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.Proxy;

import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.base.generale.ParametroPsn;
import it.thera.thip.cs.DatiComuniEstesi;
import it.thera.thip.vendite.ordineVE.*;
import it.odl.thip.YCondCtrVen;
import it.odl.thip.YCondCtrVenTM;
import it.odl.thip.vendite.generaleVE.*;

/**
 * <h1>Softre Solutions</h1>
 * @author Thomas Brescianini	06/06/2024
 * <br><br>
 * <b>22660 LP		10/12/2015</b> <p>Prima versione.</p>
 * <b>71120	TBSOF3	06/06/2023</b> <p>Gestione articoli vecchi 1°stesura.</p>
 * <b>71486	DSSOF3	28/03/2024</b> <p>Aggiungere logica {@link #giorniTolleranza}.</p>
 * <b>71XXX	DSSOF3	04/04/2024</b> <p>Aggiungere aggiornamento condizioni contrattuali.</p>
 */

public class YOrdineVenditaRigaPrm extends OrdineVenditaRigaPrm {

	protected int numeroGiorni = Integer.valueOf(ParametroPsn.getValoreParametroPsn("YControlloDate", "Giorni di differenza"));

	protected int giorniTolleranza = Integer.valueOf(ParametroPsn.getValoreParametroPsn("YControlloDate", "Giorni di tolleranza"));

	protected java.sql.Date iDataprvspe;

	protected java.sql.Date iData2;

	protected boolean iSpedsosp = false;

	protected boolean iExpContratto = true;

	protected Proxy iRelcontratto = new Proxy(it.odl.thip.YCondCtrVen.class);

	public YOrdineVenditaRigaPrm() {
		setSpedsosp(false);
		setExpContratto(true);
	}

	public void setDataprvspe(java.sql.Date dataprvspe) {
		this.iDataprvspe = dataprvspe;
		setDirty();
	}

	public java.sql.Date getDataprvspe() {
		return iDataprvspe;
	}

	public void setData2(java.sql.Date data2) {
		this.iData2 = data2;
		setDirty();
	}

	public java.sql.Date getData2() {
		return iData2;
	}

	public void setSpedsosp(boolean spedsosp) {
		this.iSpedsosp = spedsosp;
		setDirty();
	}

	public boolean getSpedsosp() {
		return iSpedsosp;
	}

	public void setExpContratto(boolean expContratto) {
		this.iExpContratto = expContratto;
		setDirty();
	}

	public boolean getExpContratto() {
		return iExpContratto;
	}

	public void setRelcontratto(YCondCtrVen relcontratto) {
		String oldObjectKey = getKey();
		this.iRelcontratto.setObject(relcontratto);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public YCondCtrVen getRelcontratto() {
		return (YCondCtrVen)iRelcontratto.getObject();
	}

	public void setRelcontrattoKey(String key) {
		String oldObjectKey = getKey();
		iRelcontratto.setKey(key);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getRelcontrattoKey() {
		return iRelcontratto.getKey();
	}

	public void setIdContrato(Integer idContrato) {
		String key = iRelcontratto.getKey();
		iRelcontratto.setKey(KeyHelper.replaceTokenObjectKey(key , 2, idContrato));
		setDirty();
	}

	public Integer getIdContrato() {
		String key = iRelcontratto.getKey();
		String objIdContrato = KeyHelper.getTokenObjectKey(key,2);
		return KeyHelper.stringToIntegerObj(objIdContrato);
	}

	@Override
	protected void setIdAziendaInternal(String idAzienda) {
		super.setIdAziendaInternal(idAzienda);
		iRelcontratto.setKey(KeyHelper.replaceTokenObjectKey(iRelcontratto.getKey(), 1, idAzienda));
	}

	protected void recuperaCondizioniVendita(OrdineVenditaPO testata) throws SQLException {
		super.recuperaCondizioniVendita(testata);
		if(condVen != null) {
			((YCondizioniDiVendita)condVen).setOldPrezzo(condVen.getPrezzo());
			if(condVen.getPrezzoAlNettoSconti() != null)
				condVen.setPrezzo(condVen.getPrezzoAlNettoSconti().setScale(2, BigDecimal.ROUND_HALF_UP));
			condVen.setScontoArticolo1(new BigDecimal("0"));
			condVen.setScontoArticolo2(new BigDecimal("0"));
		}
	}

	protected void calcolaDatiVendita(OrdineVenditaPO testata) throws SQLException {
		super.calcolaDatiVendita(testata);
		if (condVen != null) {
			setPrezzoListino(((YCondizioniDiVendita)condVen).getOldPrezzo());
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Vector checkAll(BaseComponentsCollection components) {
		Vector errori = super.checkAll(components);
		if(!this.isOnDB())
			errori = controlloDataArticolo(errori);
		return errori;
	}

	@Override
	public int save() throws SQLException {
		if(!isOnDB()) {
			gestisciCondizioniContrattualiSuNuovo();
		}else {
			gestisciCondizioniContrattualiSuEsistente();
		}
		int rc = super.save();
		return rc;
	}

	protected void gestisciCondizioniContrattualiSuEsistente() {
		YCondCtrVen condizioniContrattuali = cercaRigaCondizioniContrattuali();
		if(condizioniContrattuali != null) {
			condizioniContrattuali.aggiornaCondizioniContrattuali();
			try {
				condizioniContrattuali.save();
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void gestisciCondizioniContrattualiSuNuovo() {
		YCondCtrVen condizioniContrattuali = cercaRigaCondizioniContrattuali();
		if(condizioniContrattuali != null) {
			setRelcontratto(condizioniContrattuali);
			condizioniContrattuali.setQtaordinata(condizioniContrattuali.getQtaordinata().add(getQtaPropostaEvasione().getQuantitaInUMPrm()));

			BigDecimal percentualeComplet = condizioniContrattuali.getQtaordinata().divide(condizioniContrattuali.getQtaprevista(), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
			percentualeComplet = percentualeComplet.setScale(2, BigDecimal.ROUND_HALF_UP);
			condizioniContrattuali.setPerccompletam(percentualeComplet);

			try {
				ErrorMessage avviso = null;
				int rc = condizioniContrattuali.save();
				if(rc > 0) {
					avviso = new ErrorMessage("YODL000005", "Sono state applicate le condizioni contrattuali previste sul cliente");
					if(condizioniContrattuali.getTipoRegistrazione() == YCondCtrVen.CONTRATTUALIZZATO
							&& condizioniContrattuali.getPrezzoContratto() != null) {
						setPrezzo(condizioniContrattuali.getPrezzoContratto());
					}
				}else {
					avviso = new ErrorMessage("YODL000005", "Impossibile applicare le condizioni contrattuali previste sul cliente, eseguire MANUALMENTE!");
				}
				getWarningList().add(avviso);
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public YCondCtrVen cercaRigaCondizioniContrattuali() {
		SimpleDateFormat db2Fmt = new SimpleDateFormat("yyyy-MM-dd");
		String where = " "+YCondCtrVenTM.ID_AZIENDA+" = '"+this.getIdAzienda()+"' AND " + 
				" "+YCondCtrVenTM.IDARTICOLO+" = '"+this.getIdArticolo()+"' AND "+
				" "+YCondCtrVenTM.IDCLIENTE+" = '"+((OrdineVendita)getTestata()).getIdCliente()+"' AND " + 
				" ("+YCondCtrVenTM.QTAPREVISTA+" - "+YCondCtrVenTM.QTAORDINATA+" > 0) AND " + 
				" "+YCondCtrVenTM.STATO+" = '"+DatiComuniEstesi.VALIDO+"' AND " + 
				" "+YCondCtrVenTM.SALDO_MANUALE+" = '"+Column.FALSE_CHAR+"' ";
		if(getDataConsegnaConfermata() != null) {
			where += " AND '"+db2Fmt.format(getDataConsegnaConfermata())+"' ";
		}else {
			where += " AND '"+db2Fmt.format(getDataConsegnaRichiesta())+"' ";
		}
		where += " BETWEEN "+YCondCtrVenTM.DATAINIZIO+" AND "+YCondCtrVenTM.DATAFINE+" ";
		String orderBy = " "+YCondCtrVenTM.DATA_CONTRATTO+" ASC, "+YCondCtrVenTM.ID_REGISTRAZIONE+" ASC ";
		try {
			Vector<YCondCtrVen> contratti = YCondCtrVen.retrieveList(YCondCtrVen.class, where, orderBy, false);
			if(contratti.size() > 0) {
				return contratti.get(0);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace(Trace.excStream);
		} catch (InstantiationException e) {
			e.printStackTrace(Trace.excStream);
		} catch (IllegalAccessException e) {
			e.printStackTrace(Trace.excStream);
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return null;

	}

	@SuppressWarnings({"unchecked", "rawtypes" })
	protected Vector controlloDataArticolo(Vector errori) {
		int diff = TimeUtils.differenceInDays(TimeUtils.getCurrentDate(),getArticolo().getDatiComuniEstesi().getDataCrz());
		if(diff <= giorniTolleranza) {
			return errori;
		}
		CachedStatement cs = null;
		ResultSet rs = null;
		try {
			Date dataFattura = null;
			String select = "SELECT * FROM THIP.STAT_FATVEN " + 
					"WHERE DATA_FATTURA = (SELECT MAX(DATA_FATTURA) FROM THIP.STAT_FATVEN WHERE R_ARTICOLO = '" + this.getIdArticolo() + "' AND ID_AZIENDA = '" + Azienda.getAziendaCorrente() + "') " + 
					"AND R_ARTICOLO = '" + this.getIdArticolo() + "' AND ID_AZIENDA = '" + Azienda.getAziendaCorrente() + "'";
			cs = new CachedStatement(select);
			rs = cs.executeQuery();
			if(rs.next()) {
				dataFattura = rs.getDate("DATA_FATTURA");
			}
			rs = null;cs.free();
			if(dataFattura != null) {
				Date dataOdierna = new Date(System.currentTimeMillis());
				int differenza = TimeUtils.differenceInDays(dataOdierna, dataFattura);
				if(differenza <= numeroGiorni)
					return errori;
			}
			Date dataControllo = null;
			select = "SELECT * FROM THIPPERS.Y_ART_ULTIMO_CONTROLLO " + 
					"WHERE ID_ARTICOLO = '" + this.getIdArticolo() + "' AND ID_AZIENDA = '" + Azienda.getAziendaCorrente() + "'";
			cs = new CachedStatement(select);
			rs = cs.executeQuery();
			if(rs.next()) {
				dataControllo = rs.getDate("YDATACONTROLLO");
			}
			rs = null;cs.free();
			if(dataControllo != null) {
				Date dataOdierna = new Date(System.currentTimeMillis());
				int differenza = TimeUtils.differenceInDays(dataOdierna, dataControllo);
				if(differenza <= numeroGiorni)
					return errori;
			}
			ErrorMessage er = new ErrorMessage("YODL000004");
			errori.add(er);
		}catch(SQLException e) {
			e.printStackTrace(Trace.excStream);
		}finally {
			try {
				if(rs != null)
					rs = null;
				if(cs != null)
					cs.free();
			}catch(SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		return errori;
	}
}
