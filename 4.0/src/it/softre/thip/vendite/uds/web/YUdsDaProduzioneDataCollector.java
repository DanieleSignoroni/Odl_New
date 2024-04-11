package it.softre.thip.vendite.uds.web;

import java.math.BigDecimal;
import java.sql.SQLException;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.collector.BODataCollector;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;

import it.softre.thip.base.uds.YTipoUds;
import it.softre.thip.vendite.uds.YUdsDaProduzione;
import it.softre.thip.vendite.uds.YUdsVenRig;
import it.softre.thip.vendite.uds.YUdsVendita;
import it.thera.thip.base.generale.CategoriaUM;
import it.thera.thip.base.generale.CategoriaUMUM;
import it.thera.thip.cs.ThipDataCollector;
import it.thera.thip.produzione.ordese.AttivitaEsecLottiPrd;
import it.thera.thip.produzione.ordese.OrdineEsecutivo;
import it.thera.thip.vendite.proposteEvasione.CreaMessaggioErrore;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 14/03/2024
 * <br><br>
 * <b>71469	DSSOF3	14/03/2024</b>    
 * <p>Nuova stesura.<br>
 * </p>
 */

public class YUdsDaProduzioneDataCollector extends ThipDataCollector{

	@Override
	public int save() {
		int ret = check();
		if(ret == BODataCollector.OK) {
			creaUdsDaProduzione();
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	public void creaUdsDaProduzione() {
		YUdsDaProduzione bo = (YUdsDaProduzione) this.getBo();
		try {
			OrdineEsecutivo ordEsec = bo.getOrdineEsecutivo();
			YTipoUds tipoUds = bo.getTipoUds();
			if(ordEsec != null && tipoUds != null) {
				BigDecimal quantita = new BigDecimal(bo.getQuantita());
				BigDecimal quantitaPerConf = new BigDecimal(bo.getQuantitaConfezione());
				BigDecimal[] divisionResult = quantita.divideAndRemainder(quantitaPerConf);
				int fullBoxes = divisionResult[0].intValue(); 
				BigDecimal remainder = divisionResult[1];
				if(remainder.compareTo(BigDecimal.ZERO) > 0) {
					fullBoxes = fullBoxes+1;
				}
				for(int i = 0; i < fullBoxes; i++) {
					YUdsVendita udsVE = new YUdsVendita(ordEsec, tipoUds);
					udsVE.setAssegnaNumeratoreAutomatico(false);
					udsVE.setIdUds(YUdsVendita.getNewProgressivoForScatola());
					udsVE.setRCliente(ordEsec.getIdCliente() != null ? ordEsec.getIdCliente() : null);
					int rc = udsVE.save();
					if(rc < 0) {
						getErrorList().addError(CreaMessaggioErrore.daRcAErrorMessage(rc, (SQLException) udsVE.getException()));
					}
					if(i == fullBoxes-1) {
						if(remainder.compareTo(BigDecimal.ZERO) > 0)
							quantitaPerConf = remainder;
					}
					YUdsVenRig udsVenRig = new YUdsVenRig(ordEsec, quantitaPerConf);
					if(ordEsec.getOrdineVenditaRiga() != null) {
						udsVenRig.setRAnnoOrdVen(ordEsec.getAnnoOrdineCliente());
						udsVenRig.setRNumOrdVen(ordEsec.getNumeroOrdineCliente());
						udsVenRig.setRRigaOrdVen(ordEsec.getRigaOrdineCliente());
					}
					String idUmPeso = ordEsec.getArticolo().getIdUMPeso();
					BigDecimal pesoNetto = null;
					BigDecimal pesoLordo = null;
					if(idUmPeso != null && idUmPeso.equals("GR")) {
						CategoriaUM cat = (CategoriaUM) CategoriaUM.elementWithKey(CategoriaUM.class,
								KeyHelper.buildObjectKey(new String[] {
										bo.getIdAzienda(),
										"UMP"
								}), PersistentObject.NO_LOCK);
						if(cat != null) {
							BigDecimal fttCNV = null;
							char operCnv = ' ';
							for(Object umAssociata : cat.getUMAssociate()) {
								if(((CategoriaUMUM)umAssociata).getIdUnitaMisura().equals("GR")) {
									fttCNV = ((CategoriaUMUM)umAssociata).getFattoreConverUM();
									operCnv = ((CategoriaUMUM)umAssociata).getOperConverUM();
								}

							}
							if(fttCNV != null) {
								BigDecimal valorePesoLordo = ordEsec.getArticolo().getPeso().multiply(quantitaPerConf);
								BigDecimal valorePesoNetto = ordEsec.getArticolo().getPesoNetto().multiply(quantitaPerConf);
								switch (operCnv) {
								case 'D':
									pesoLordo = valorePesoLordo.divide(fttCNV, fttCNV.scale(), BigDecimal.ROUND_HALF_UP);
									pesoNetto = valorePesoNetto.divide(fttCNV, fttCNV.scale(), BigDecimal.ROUND_HALF_UP);
									break;
								case 'M':
									pesoLordo = valorePesoLordo.multiply(fttCNV);
									pesoNetto = valorePesoNetto.multiply(fttCNV);
								default:
									break;
								}

							}
						}
					}else {
						pesoLordo = ordEsec.getArticolo().getPeso().multiply(quantitaPerConf);
						pesoNetto = ordEsec.getArticolo().getPesoNetto().multiply(quantitaPerConf);
					}
					if(ordEsec.getArticolo().getPesoNetto() != null) {
						udsVenRig.setPesoNetto(pesoNetto);
					}
					if(ordEsec.getArticolo().getPeso() != null) {
						udsVenRig.setPesoLordo(pesoLordo);
					}
					if(ordEsec.getRigaProdottoPrimario() != null) {
						if(ordEsec.getRigaProdottoPrimario().getLottiProdotti().size() > 0)
							udsVenRig.setRLotto(((AttivitaEsecLottiPrd)ordEsec.getRigaProdottoPrimario().getLottiProdotti().get(0)).getIdLotto());
					}
					udsVenRig.setParent(udsVE);
					udsVE.getRigheUDSVendita().add(udsVenRig);
					udsVE.setRicalcoloPesi(true);
					if(udsVE.save() >= 0) {
						ConnectionManager.commit();
					}else {
						ConnectionManager.rollback();
					}
				}
			}else {
				if(ordEsec == null) {
					getErrorList().addError(new ErrorMessage("YSOFTRE001","Ordine esecutivo obbligatorio"));
				}else {
					getErrorList().addError(new ErrorMessage("YSOFTRE001","Tipo UDS obbligatorio"));
				}
			}
		}catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
	}
}
