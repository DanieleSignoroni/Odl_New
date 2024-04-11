package it.odl.thip.logisticaLight.web;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.thera.thermfw.base.TimeUtils;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.batch.BatchOptions;
import com.thera.thermfw.batch.BatchService;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.WebElement;
import com.thera.thermfw.web.servlet.BaseServlet;

import it.softre.thip.base.uds.YTipoUds;
import it.softre.thip.uds.YRptUdsVen;
import it.softre.thip.vendite.uds.YEtichettUdsVenBatch;
import it.softre.thip.vendite.uds.YUdsVendita;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.vendite.proposteEvasione.CreaMessaggioErrore;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 25/03/2024
 * <br><br>
 * <b>71XXX	DSSOF3	25/03/2024</b>
 * <p>Prima stesura.<br>
 * Manage di azioni di form 'ResponsiveYCreazionePallet'.<br>
 * </p>
 */

public class YCreazionePalletFormActionAdapter extends BaseServlet{

	private static final long serialVersionUID = 1L;

	private static final String CREA_PALLET = "CREA_PALLET";

	private static final String STAMPA_PALLET = "STAMPA_PALLET";
	
	private static final String CONFERMA_MISURE = "CONFERMA_MISURE";

	@Override
	protected void processAction(ServletEnvironment se) throws Exception {
		String action = getStringParameter(se.getRequest(), ACTION);
		if(action.equals(CREA_PALLET)) {
			String barcode = getStringParameter(se.getRequest(), "Barcodes");
			String idPallet = getStringParameter(se.getRequest(), "IdPallet");
			creazionePallet(idPallet,barcode,se.getResponse().getWriter());
		}else if(action.equals(STAMPA_PALLET)) {
			String idPallet = getStringParameter(se.getRequest(), "IdPallet");
			stampaPallet(idPallet,se.getResponse().getWriter());
		}else if(action.equals(CONFERMA_MISURE)) {
			String idPallet = getStringParameter(se.getRequest(), "IdPallet");
			BigDecimal altezza = new BigDecimal(getStringParameter(se.getRequest(), "Altezza"));
			confermaMisurePallet(idPallet,altezza,se.getResponse().getWriter());
		}
	}

	private void confermaMisurePallet(String idPallet, BigDecimal altezza, PrintWriter out) {
		out.println("<script>");
		try {
			YUdsVendita pallet = (YUdsVendita) YUdsVendita.elementWithKey(YUdsVendita.class, idPallet, PersistentObject.NO_LOCK);
			if(pallet != null) {
				pallet.setAltezza(altezza);
				int rc = pallet.save();
				if(rc < 0) {
					ConnectionManager.rollback();
				}else {
					ConnectionManager.commit();
					out.println("parent.onSuccessConfermaMisure();");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(Trace.excStream);
		}
		out.println("</script>");
		out.close();
		
	}

	private void stampaPallet(String idPallet, PrintWriter out) {
		boolean isOk = stampaEtichettaUdsVendita(idPallet);
		if(!isOk) {
			out.println("parent.window.alert('Etichetta stampata correttamente, andare alla stampante!');");
		}else {
			out.println("parent.window.alert('Ci sono stati errori nella stampa dell etichetta!');");
		}
	}

	private void creazionePallet(String idPallet, String barcode, PrintWriter out) {
		out.println("<script>");
		List<YUdsVendita> uds = listaUdsVenditaDaBarcode(barcode);
		if(uds.size() == 0) {
			//qualcosa e' andato storto nel passare dati al back-end
		}else {
			try {
				//				String idContenitore = ParametroPsn.getValoreParametroPsn("YUdsPalletTerm", "CodiceContenitore") 
				//				!= null ? ParametroPsn.getValoreParametroPsn("YUdsPalletTerm", "CodiceContenitore") : "";
				YTipoUds contenitore = (YTipoUds) YTipoUds.elementWithKey(YTipoUds.class,
						KeyHelper.buildObjectKey(new String[] {
								Azienda.getAziendaCorrente(),
								idPallet
						}), PersistentObject.NO_LOCK);
				if(contenitore == null) {
					out.println("parent.window.alert('Contenitore non definito nel parametro.pers, valorizzare prima il parametro!');");
				}else {
					int rc = 0;
					YUdsVendita pallet = (YUdsVendita) Factory.createObject(YUdsVendita.class);
					pallet.setIdAzienda(Azienda.getAziendaCorrente());
					pallet.setRTipoUds(contenitore.getIdTipoUds());
					pallet.setDataUds(TimeUtils.getCurrentDate());
					pallet.setAssegnaNumeratoreAutomatico(false);
					pallet.setIdUds(YUdsVendita.getNewProgressivoForBancale());
					rc = pallet.save();
					if(rc > 0) {
						//Setto i riferimenti del padre al figlio
						for(YUdsVendita figlio : uds) {
							figlio.setRIdUdsPadre(pallet.getIdUds());
							rc += figlio.save();
						}
						if(rc > 0) {
							ConnectionManager.commit();
							//ok quindi nella schermata puliamo sia barcode del pallet sia tabellina
							//e rendiamo disponibile la stampa valorizzando un input hidden con i'id del bancale
							out.println("parent.onSuccessPallet('"+WebElement.formatStringForHTML(pallet.getKey())+"');");
							out.println("parent.document.getElementById('Altezza').value = '"+WebElement.formatStringForHTML(pallet.getAltezza() != null ? pallet.getAltezza().toString() : BigDecimal.ZERO.toString())+"'; ");
						}else {
							ConnectionManager.rollback();
						}
					}else {
						String msg = null;
						ErrorMessage em = CreaMessaggioErrore.daRcAErrorMessage(rc, (SQLException) pallet.getException());
						if(em != null) {
							msg = "Impossibile creare il pallet : " +em.getText();
						}else {
							msg = "Impossibile creare il pallet, rc = "+rc;
						}
						out.println("parent.window.alert('"+WebElement.formatStringForHTML(msg)+"');");
					}
				}
			}catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		out.println("</script>");
		out.close();
	}

	public List<YUdsVendita> listaUdsVenditaDaBarcode(String barcode){
		List<YUdsVendita> lst = new ArrayList<YUdsVendita>();
		String[] barcodes = barcode.split(",");
		for(String b : barcodes) {
			try {
				lst.add((YUdsVendita) YUdsVendita.elementWithKey(YUdsVendita.class, 
						KeyHelper.buildObjectKey(new String[] {
								Azienda.getAziendaCorrente(),
								b
						}), PersistentObject.NO_LOCK));
			} catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
			}
		}
		return lst;

	}

	@SuppressWarnings("unused")
	public static boolean stampaEtichettaUdsVendita(String idUds) {
		boolean isOk = false;
		try {
			if(idUds != null) {
				YUdsVendita uds = (YUdsVendita)
						YUdsVendita.elementWithKey(YUdsVendita.class, 
								idUds, PersistentObject.NO_LOCK);
				if(uds != null) {
					YRptUdsVen rpt = (YRptUdsVen) Factory.createObject(YRptUdsVen.class);
					YEtichettUdsVenBatch batch = (YEtichettUdsVenBatch) Factory.createObject(YEtichettUdsVenBatch.class);	
					BatchOptions batchOptions = (BatchOptions) Factory.createObject(BatchOptions.class);
					boolean ok = batchOptions.initDefaultValues(YEtichettUdsVenBatch.class, "YEtichettUdsVen", "RUN");
					batch.setBatchJob(batchOptions.getBatchJob());
					batch.setScheduledJob(batchOptions.getScheduledJob());
					batch.setExecutePrint(true);
					batch.setReportId(""); //dobbiamo settare il report id
					if(batch.save() >= 0) {
						rpt.setIdAzienda(Azienda.getAziendaCorrente());
						rpt.setIdUds(uds.getIdUds());
						rpt.setBatchJobId(batchOptions.getBatchJob().getBatchJobId());
						rpt.setReportNr(1);
						rpt.setRigaJobId(0);
						if(rpt.save() >= 0) {
							isOk = true;
							BatchService.submitJob(batch.getBatchJob());
							ConnectionManager.commit();
						}else {
							isOk = false;
						}
					}else {
						isOk = false;
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
		return isOk;
	}

}
