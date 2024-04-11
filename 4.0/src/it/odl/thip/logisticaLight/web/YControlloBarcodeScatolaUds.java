package it.odl.thip.logisticaLight.web;

import java.io.PrintWriter;
import java.sql.SQLException;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.WebElement;
import com.thera.thermfw.web.servlet.BaseServlet;

import it.softre.thip.vendite.uds.YUdsVendita;
import it.thera.thip.base.azienda.Azienda;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 21/03/2024
 * <br><br>
 * <b>71469	DSSOF3	21/03/2024</b>
 * <p>
 * Prima stesura.<br>
 * Controlo barcode scatola sparata.<br>
 * Se ok --> la aggiungo alla tabellina.
 * </p>
 */

public class YControlloBarcodeScatolaUds extends BaseServlet{

	private static final long serialVersionUID = 1L;

	@Override
	protected void processAction(ServletEnvironment se) throws Exception {
		PrintWriter out = se.getResponse().getWriter();
		out.println("<script>");
		String barcodeScatola = getStringParameter(se.getRequest(), "BarcodeScatola");
		if(barcodeScatola != null && !barcodeScatola.isEmpty()) {
			YUdsVendita scatola = null;
			try {
				scatola = (YUdsVendita) YUdsVendita.elementWithKey(YUdsVendita.class,
						KeyHelper.buildObjectKey(new String[]{
								Azienda.getAziendaCorrente(),
								barcodeScatola
						}), PersistentObject.NO_LOCK);
			}catch (SQLException e) {
				e.printStackTrace(Trace.excStream);
				out.println("parent.window.alert('La scatola sparata non esiste, spararne una esistente!');");
				out.println("parent.document.getElementById('BarcodeScatola').value = '';");
			}
			if(scatola == null) {
				out.println("parent.window.alert('La scatola sparata non esiste, spararne una esistente!');");
				out.println("parent.document.getElementById('BarcodeScatola').value = '';");
			}else if(scatola.getRIdUdsPadre() != null){
				out.println("parent.window.alert('La scatola sparata appartiene gia a un pallet: "+WebElement.formatStringForHTML(scatola.getRIdUdsPadre())+" ');");
				out.println("parent.document.getElementById('BarcodeScatola').value = '';");
			}else if(scatola.getIdUds().startsWith("PA")) {
				out.println("parent.window.alert('Non e possibile sparare pallet!');");
				out.println("parent.document.getElementById('BarcodeScatola').value = '';");
			}else {
				out.println("parent.manageSparataScatola('"+WebElement.formatStringForHTML(barcodeScatola)+"');");
				out.println("parent.document.getElementById('BarcodeScatola').value = '';");
			}
		}
		out.println("</script>");
		out.close();
	}

}
