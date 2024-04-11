package it.softre.thip.uds.vendite.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.jsp.JspWriter;

import com.thera.thermfw.web.WebFormModifier;

import it.softre.thip.vendite.uds.YEtichettUdsVenBatch;
import it.thera.thip.cs.ColonneFiltri;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 21/03/2024
 * <br><br>
 * <b>71469	DSSOF3	21/03/2024</b>
 * <p>
 * Prima stesura.<br>
 * Gestione chiavi selezionate per batch stampa etichette.<br>
 * </p>
 */

public class YEtichetteUdsVenBatchFormModifier extends WebFormModifier{

	@Override
	public void writeHeadElements(JspWriter out) throws IOException {

	}

	@Override
	public void writeBodyStartElements(JspWriter out) throws IOException {

	}

	@SuppressWarnings({ "rawtypes", "unlikely-arg-type" })
	@Override
	public void writeFormStartElements(JspWriter out) throws IOException {
		List lstChiaviSelected = (List) getServletEnvironment().getRequest().getAttribute("lstChiaviSelected");
		if(lstChiaviSelected != null && !lstChiaviSelected.equals("")) {
			out.println("<input type=\"hidden\" id=\"thMDVExtractor\" name=\"thMDVExtractor\" >");
			out.println("<script language=\"javascript1.2\">");
			out.println("document.getElementById('thMDVExtractor').value='it.thera.thip.cs.web.StampaSingolaScreenDataExtractor';");
			out.println("</script>");
			getBODataCollector().setOnBORecursive();
			java.util.Iterator lstChiaviSelectedIte = ( java.util.Iterator)lstChiaviSelected.iterator();
			String keysSel = "";
			YEtichettUdsVenBatch myBatch = (YEtichettUdsVenBatch)getBODataCollector().getBo();
			while (lstChiaviSelectedIte.hasNext()) {
				String nextKey = (String)lstChiaviSelectedIte.next();
				keysSel += nextKey;
				if(lstChiaviSelectedIte.hasNext())
					keysSel += ColonneFiltri.LISTA_SEP;
			}
			myBatch.setChiaviSelezionati(keysSel);
			getBODataCollector().loadAttValue();
		}

	}

	@Override
	public void writeFormEndElements(JspWriter out) throws IOException {

	}

	@Override
	public void writeBodyEndElements(JspWriter out) throws IOException {

	}

}
