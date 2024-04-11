package it.odl.thip.base.uds.importazione;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.batch.BatchRunnable;
import com.thera.thermfw.collector.BODataCollector;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.security.Authorizable;

import it.softre.thip.base.uds.YTipoUds;
import it.softre.thip.vendite.uds.YUdsVenRig;
import it.softre.thip.vendite.uds.YUdsVendita;
import it.softre.thip.vendite.uds.web.YUdsVenRigDataCollector;
import it.softre.thip.vendite.uds.web.YUdsVenditaDataCollector;
import it.thera.thip.base.articolo.Articolo;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.produzione.ordese.AttivitaEsecLottiPrd;
import it.thera.thip.produzione.ordese.OrdineEsecutivo;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 11/03/2024
 * <br><br>
 * <b>71469	DSSOF3	11/03/2024</b>    
 * <p>Importazione UDS da MES, prima stesura.</p>
 */

public class YImportazioneUdsMES extends BatchRunnable implements Authorizable {

	@Override
	protected boolean run() {
		boolean isOk = true;
		try {
			isOk = runImportazione();
		} catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
		return isOk;
	}

	@SuppressWarnings("unchecked")
	protected boolean runImportazione() throws Exception {
		List<YUdsMes> list = YUdsMes.listaUdsMesDaImportare();
		List<YUdsMes> daRiportareInStatoDaProcessare = new ArrayList<YUdsMes>();
		if(list.size() < 0) {
			output.println(" --> Non e' presente nessuna UDS da importare....termino! ");
			return false;
		}else {
			for(YUdsMes udsMES : list) {
				boolean isOk = YUdsMes.aggiornaSemaforino(YStatoImportazioneUdsMES.IN_CORSO, udsMES.getIdUds());
				if(!isOk) {
					output.println(" --> Fallita l'update del semaforo per l'uds :"+udsMES.getIdUds());
					ConnectionManager.rollback();
					continue;
				}else {
					ConnectionManager.commit();
				}
				try {
					OrdineEsecutivo ordEsec = recuperaOrdineEsecutivo(udsMES.getRAnnoOrdPrd(), udsMES.getRNumeroOrdPrd());
					if(ordEsec == null) {
						output.println(" --> L'ordine esecutivo {"+Azienda.getAziendaCorrente()+"/"+udsMES.getRAnnoOrdPrd()+"/"+udsMES.getRNumeroOrdPrd()+"} non esiste in Panthera..salto la riga");
						continue;
					}else {
						YUdsVenditaDataCollector boDCTes = (YUdsVenditaDataCollector) dataCollectorUdsVenditaTestata();
						boDCTes.setBo(creaUdsVendita(udsMES, ordEsec));
						int rc = boDCTes.check();
						if(rc == BODataCollector.ERROR) {
							output.println(" --> Impossibile creare l'uds testata della riga {"+udsMES.getKey()+"}, \n "+boDCTes.messages().toString());
						}else {
							YUdsVenRigDataCollector boDCRig = (YUdsVenRigDataCollector) dataCollectorUdsVenditaRiga();
							boDCRig.setBo(creaUdsVenditaRiga((YUdsVendita) boDCTes.getBo(), udsMES, ordEsec));
							if(boDCRig.check() == BODataCollector.ERROR) {
								output.println(" --> Impossibile creare l'uds riga della riga {"+udsMES.getKey()+"}, \n "+boDCRig.messages().toString());
							}else {
								YUdsVendita udsVen = (YUdsVendita) boDCTes.getBo();
								udsVen.getRigheUDSVendita().add(boDCRig.getBo());
								udsVen.setAssegnaNumeratoreAutomatico(false);
								boDCTes.setBo(udsVen);
								boDCTes.setAutoCommit(false);
								int rcSave = boDCTes.save();
								if(rcSave == BODataCollector.ERROR) {
									output.println(" --> Impossibile salvare l'Uds vendita, rc --> : "+rcSave+" , msg --> : \n "+boDCTes.messages().toString());
								}else {
									isOk = YUdsMes.aggiornaSemaforino(YStatoImportazioneUdsMES.PROCESSATO, udsMES.getIdUds());
									if(isOk && rcSave == BODataCollector.OK) {
										output.println(" --> Creata correttamente l'Uds : "+boDCTes.getBo().getKey());
										ConnectionManager.commit();
									}else {
										daRiportareInStatoDaProcessare.add(udsMES);
										ConnectionManager.rollback();
									}
								}
							}
						}
					}
				}catch (SQLException e) {
					e.printStackTrace(Trace.excStream);
				}
			}
		}
		//per i rimanenti eseguo l'update del flag a 'Da processare'
		for(YUdsMes udsMES : daRiportareInStatoDaProcessare) {
			boolean isOk = YUdsMes.aggiornaSemaforino(YStatoImportazioneUdsMES.DA_PROCESSARE, udsMES.getIdUds());
			if(isOk) {
				ConnectionManager.commit();
			}else {
				ConnectionManager.rollback();
			}
		}
		return true;
	}

	protected YUdsVendita creaUdsVendita(YUdsMes udsMES, OrdineEsecutivo ordEsec) {
		YUdsVendita udsVen = (YUdsVendita) Factory.createObject(YUdsVendita.class);
		udsVen.setIdUds(udsMES.getIdUds());
		udsVen.setIdAzienda(Azienda.getAziendaCorrente());
		udsVen.setDataUds(TimeUtils.getCurrentDate());
		udsVen.setRCliente(ordEsec.getIdCliente());
		YTipoUds tipoUds = recuperaTipoUds(udsMES.getRCodimballo());
		if(tipoUds == null) {
			tipoUds = recuperaTipoUds("CONTENITORE");
		}
		udsVen.setRTipoUds(tipoUds.getIdTipoUds());
		udsVen.setStatoStPacking(YUdsVendita.ST_ETI_STAMPATA); //Aggiungere	21/03/2024
		return udsVen;
	}

	protected YUdsVenRig creaUdsVenditaRiga(YUdsVendita udsVenTes, YUdsMes udsMES, OrdineEsecutivo ordEsec) throws SQLException {
		YUdsVenRig udsVenRig = (YUdsVenRig) Factory.createObject(YUdsVenRig.class);
		udsVenRig.setIdAzienda(Azienda.getAziendaCorrente());
		udsVenRig.setParent(udsVenTes);
		udsVenRig.setIdRigaUds(1);
		udsVenRig.setRArticolo(ordEsec.getIdArticolo());
		udsVenRig.setRVersione(ordEsec.getIdVersione());
		udsVenRig.setRConfig(ordEsec.getIdEsternoConfig());
		if(ordEsec.getRigaProdottoPrimario() != null) {
			if(ordEsec.getRigaProdottoPrimario().getLottiProdotti().size() > 0)
				udsVenRig.setRLotto(((AttivitaEsecLottiPrd)ordEsec.getRigaProdottoPrimario().getLottiProdotti().get(0)).getIdLotto());
		}
		udsVenRig.setQtaPrm(udsMES.getQtaPrm());
		udsVenRig.setRAnnoOrdPrd(ordEsec.getIdAnnoOrdine());
		udsVenRig.setRNumOrdPrd(ordEsec.getIdNumeroOrdine());
		udsVenRig.setRCliente(ordEsec.getIdCliente());
		udsVenRig.setRAnnoOrdVen(ordEsec.getAnnoOrdineCliente());
		udsVenRig.setRNumOrdVen(ordEsec.getNumeroOrdineCliente());
		udsVenRig.setRRigaOrdVen(ordEsec.getRigaOrdineCliente());
		//udsVenRig.setRRigaDetOrdVen(ordEsec.getDettaglioRigaOrdine());
		udsVenRig.setRCauOrdVenRig(ordEsec.getOrdineVenditaRiga() != null ? ordEsec.getOrdineVenditaRiga().getIdCauRig() : null);
		if(ordEsec.getArticolo().getPesoNetto() != null) {
			udsVenRig.setPesoNetto(ordEsec.getArticolo().getPesoNetto().multiply(udsMES.getQtaPrm()));
		}
		if(ordEsec.getArticolo().getPeso() != null) {
			udsVenRig.setPesoLordo(ordEsec.getArticolo().getPeso().multiply(udsMES.getQtaPrm()));
		}
		YTipoUds tipoUds = (YTipoUds) YTipoUds.elementWithKey(YTipoUds.class,
				KeyHelper.buildObjectKey(new String[] { Azienda.getAziendaCorrente(), udsVenTes.getRTipoUds() }), 0);
		if (tipoUds != null) {
			if (udsVenTes.getAltezza() == null)
				udsVenRig.setAltezza(tipoUds.getAltezza() != null ? tipoUds.getAltezza() : null);
			if (udsVenTes.getVolume() == null)
				udsVenRig.setVolume(tipoUds.getVolume() != null ? tipoUds.getVolume() : null);
			if (udsVenTes.getLarghezza() == null)
				udsVenRig.setLarghezza(tipoUds.getLarghezza() != null ? tipoUds.getLarghezza() : null);
			if (udsVenTes.getAltezza() == null)
				udsVenRig.setLunghezza(tipoUds.getLunghezza() != null ? tipoUds.getLunghezza() : null);
		}
		return udsVenRig;
	}

	protected static OrdineEsecutivo recuperaOrdineEsecutivo(String idAnnoOrd, String idNumeroOrd) {
		try {
			return (OrdineEsecutivo) OrdineEsecutivo.elementWithKey(OrdineEsecutivo.class,
					KeyHelper.buildObjectKey(new String[] {
							Azienda.getAziendaCorrente(),
							idAnnoOrd,
							idNumeroOrd
					}),PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return null;
	}

	protected static Articolo recuperaArticolo(String idArticolo) {
		try {
			return (Articolo) Articolo.elementWithKey(Articolo.class,
					KeyHelper.buildObjectKey(new String[] {
							Azienda.getAziendaCorrente(),
							idArticolo
					}),PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return null;
	}

	protected static YTipoUds recuperaTipoUds(String idTipoUds) {
		try {
			return (YTipoUds) Articolo.elementWithKey(YTipoUds.class,
					KeyHelper.buildObjectKey(new String[] {
							Azienda.getAziendaCorrente(),
							idTipoUds
					}),PersistentObject.NO_LOCK);
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		return null;
	}

	public static BODataCollector dataCollectorUdsVenditaTestata() {
		YUdsVenditaDataCollector boDC = (YUdsVenditaDataCollector) Factory.createObject(YUdsVenditaDataCollector.class);
		boDC.initialize("YUdsVendita", false, PersistentObject.NO_LOCK);
		return boDC;
	}

	public static BODataCollector dataCollectorUdsVenditaRiga() {
		YUdsVenRigDataCollector boDC = (YUdsVenRigDataCollector) Factory.createObject(YUdsVenRigDataCollector.class);
		boDC.initialize("YUdsVenRig", false, PersistentObject.NO_LOCK);
		return boDC;
	}

	@Override
	protected String getClassAdCollectionName() {
		return "YImpUdsMES";
	}

}
