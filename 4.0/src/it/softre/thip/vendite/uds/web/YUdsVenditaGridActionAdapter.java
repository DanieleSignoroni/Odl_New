package it.softre.thip.vendite.uds.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;

import com.thera.thermfw.ad.ClassADCollection;
import com.thera.thermfw.base.Trace;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.WebElement;
import com.thera.thermfw.web.WebToolBar;
import com.thera.thermfw.web.WebToolBarButton;
import com.thera.thermfw.web.servlet.GridActionAdapter;

import it.softre.thip.vendite.uds.YUdsVendita;
import it.thera.thip.base.documenti.web.DocumentoDataCollector;
import it.thera.thip.crm.anagrMarketing.web.SegmentazioneMktgHelper;
/**
 * 
 * @author daniele.signoroni
 *	70687	DSSOF3	06/05/2022	Aggiunti bottoni UDS_SECONDO_LIVELLO e SPOSTAMENTO_UDS nella grid delle Uds di Vendita
 */

public class YUdsVenditaGridActionAdapter extends GridActionAdapter{

	private static final long serialVersionUID = 1L;

	public static final String REFRESH_GRID = "REFRESH_GRID";

	protected static final String UDS_SECONDO_LIVELLO = "UDS_SECONDO_LIVELLO";
	protected static String UDS_SECONDO_LIVELLO_IMG = "it/thera/thip/vendite/pickingPacking/images/GestionePacking.gif";
	protected static String UDS_SECONDO_LIVELLO_RES = "it/softre/thip/vendite/uds/resources/YUdsVendita";

	protected static final String SPOSTAMENTO_UDS = "SPOSTAMENTO_UDS";
	protected static String SPOSTAMENTO_UDS_IMG = "it/thera/thip/vendite/pickingPacking/images/PrelevaTutto.gif";
	protected static String SPOSTAMENTO_UDS_RES= "it/softre/thip/vendite/uds/resources/YUdsVendita";

	protected static final String EVADI_UDS = "EVADI_UDS";
	protected static String EVADI_UDS_IMG = "it/thera/thip/vendite/ordineVE/images/EvaOrdVenDir.gif";
	protected static String EVADI_UDS_RES= "it/softre/thip/vendite/uds/resources/YUdsVendita";

	protected static final String STAMPA_ETICHETTA_UDS = "STAMPA_ETICHETTA_UDS";
	protected static String STAMPA_ETICHETTA_UDS_IMG = "it/softre/thip/vendite/uds/img/StampaEtic.gif";
	protected static String STAMPA_ETICHETTA_UDS_RES = "it/softre/thip/vendite/uds/resources/YUdsVendita";

	protected static final String UDS_VENDITA_SEMPLIFICATA = "UDS_VENDITA_SEMPLIFICATA";
	protected static String UDS_VENDITA_SEMPLIFICATA_IMG = "it/softre/thip/vendite/uds/img/possible.gif";
	protected static String UDS_VENDITA_SEMPLIFICATA_RES = "it/softre/thip/vendite/uds/resources/YUdsVendita";

	protected static int i = 0;

	@Override
	public void modifyToolBar(WebToolBar toolBar) {
		super.modifyToolBar(toolBar);
		WebToolBarButton udsSecondoLvl = new WebToolBarButton(UDS_SECONDO_LIVELLO, "action_submit", "infoArea"
				, "no", UDS_SECONDO_LIVELLO_RES, UDS_SECONDO_LIVELLO, UDS_SECONDO_LIVELLO_IMG, UDS_SECONDO_LIVELLO, "multiple_action", false);
		toolBar.addButton("tbbtTicklerPullDown", udsSecondoLvl);

		WebToolBarButton spostamentoUds = new WebToolBarButton(SPOSTAMENTO_UDS, "action_submit", "new"
				, "no", SPOSTAMENTO_UDS_RES, SPOSTAMENTO_UDS, SPOSTAMENTO_UDS_IMG, SPOSTAMENTO_UDS, "", false);
		toolBar.addButton(UDS_SECONDO_LIVELLO, spostamentoUds);

		WebToolBarButton evasioneUds = new WebToolBarButton(EVADI_UDS, "action_submit", "infoArea"
				, "no", EVADI_UDS_RES, EVADI_UDS, EVADI_UDS_IMG, EVADI_UDS, "multiple_action", false);
		toolBar.addButton(SPOSTAMENTO_UDS, evasioneUds);

		WebToolBarButton stampaEtichettaUds = new WebToolBarButton(STAMPA_ETICHETTA_UDS, "action_submit", "new"
				, "no", STAMPA_ETICHETTA_UDS_RES, STAMPA_ETICHETTA_UDS, STAMPA_ETICHETTA_UDS_IMG, STAMPA_ETICHETTA_UDS, "multiple_action", false);
		toolBar.addButton(EVADI_UDS, stampaEtichettaUds);

		WebToolBarButton udsAcquistoSemplificata = new WebToolBarButton(UDS_VENDITA_SEMPLIFICATA, "action_submit", "new"
				, "no", UDS_VENDITA_SEMPLIFICATA_RES, UDS_VENDITA_SEMPLIFICATA, UDS_VENDITA_SEMPLIFICATA_IMG, UDS_VENDITA_SEMPLIFICATA, "", false);
		toolBar.addButton(UDS_VENDITA_SEMPLIFICATA, udsAcquistoSemplificata);
	}

	@Override
	public void processAction(ServletEnvironment se) throws ServletException, IOException {
		String action = se.getRequest().getParameter(ACTION) != null ? se.getRequest().getParameter(ACTION) : "";
		if(action.equals("COPY")) {
			String key = se.getRequest().getParameter(OBJECT_KEY);
			se.sendRequest(getServletContext(), "it/softre/thip/vendite/uds/YCopiaUdsVendita.jsp?Key="+key, false);
		}else {
			super.processAction(se);
		}
	}

	@Override
	protected void otherActions(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {
		String action = se.getRequest().getParameter(ACTION) != null ? se.getRequest().getParameter(ACTION) : "";
		if(action.equals(UDS_SECONDO_LIVELLO)) {
			udsSecondoLivello(se,cadc);
		}
		if(action.equals(SPOSTAMENTO_UDS)) {
			String url = "it/softre/thip/vendite/uds/YSpostamentoUdsVendita.jsp";
			se.sendRequest(getServletContext(), url, true);
		}
		if(action.equals(EVADI_UDS)) {
			lanciaEvasioneUds(se,cadc);
		}
		if(action.equals(UDS_VENDITA_SEMPLIFICATA)) {
			String url = "it/softre/thip/vendite/uds/YUdsVenditaSemplificata.jsp?";
			se.sendRequest(getServletContext(), url, false);
		}
		if(action.equals(STAMPA_ETICHETTA_UDS)) {
			stampaEtichetteUds(cadc, se);
		}
		super.otherActions(cadc, se);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void stampaEtichetteUds(ClassADCollection cadc, ServletEnvironment se) throws ServletException, IOException {
		List lstChiaviSelected = SegmentazioneMktgHelper.getChiaviSelected(cadc, se);
		if(lstChiaviSelected.size() == 0) {
			ErrorMessage em = new ErrorMessage("YSOFTRE001","Selezionare almeno un UDS per la stampa!");
			manageErrorMessage(em, se.getResponse().getOutputStream());
			return;
		}
		if(lstChiaviSelected.size() > 1) {
			for(String key : (List<String>)lstChiaviSelected) {
				String[] arr = KeyHelper.unpackObjectKey(key);
				if((arr.length > 0 && arr[1] != null)
						&& arr[1].startsWith("PA")) {
					ErrorMessage em = new ErrorMessage("YSOFTRE001","I pallet vanno stampati uno alla volta!");
					manageErrorMessage(em, se.getResponse().getOutputStream());
					return;
				}
			}
		}
		try {
			se.getRequest().setAttribute("lstChiaviSelected", lstChiaviSelected);
			se.sendRequest(getServletContext(),"it/softre/thip/uds/vendite/YEtichettUdsVenBatch.jsp", true);
		}
		catch (Exception ex) {
			ex.printStackTrace(Trace.excStream);
		}
	}
	
	/**
	 * @author Daniele Signoroni
	 * <p>
	 * Tramite il seguente metodo viene mostrato un errore nella infoArea nella GUI di una grid.<br>
	 * Nel caso in cui il nostro bottone non abbia come target "infoArea" dobbiamo gestire gli errori in maniera differente.<br>
	 * </p>
	 * @param em
	 * @param out
	 * @throws IOException
	 */
	protected void manageErrorMessage(ErrorMessage em, ServletOutputStream out) throws IOException {
		out.println("<script language='JavaScript1.2'>");
		out.println("parent.window.close()");
		out.println("var errorsArray = new Array();");
		out.println("var errViewObj = parent.opener.eval(parent.opener.errorsViewName);");
		boolean foundErrors = false;
		boolean foundErrorsForz = false;
		String errId = em.getId();
		String errShortText = WebElement.formatStringForHTML(em.getText());
		String errLongText = WebElement.formatStringForHTML(em.getLongText());
		String errLabel = WebElement.formatStringForHTML(em.getAttOrGroupLabel());
		String errGrpName = WebElement.formatStringForHTML(em.getAttOrGroupName());
		String errSeverity = String.valueOf(em.getSeverity());
		boolean isForceable = em.getForceable();
		if(errLabel != null && !errLabel.equals(""))
			errShortText = errLabel + " - " + errShortText;
		if(em.getSeverity()==ErrorMessage.ERROR) {
			foundErrors=true;
		}
		 out.println("var idCompInError = new Array();");
		out.println("numErr = " + 1  + ";");  			
		out.println("foundErrors = " + foundErrors + ";");  			
		out.println("foundErrorsForzabili = " + foundErrorsForz + ";");  			
		out.println("var singleError = new Array('" + errId + "', '" + errShortText + "', idCompInError, '" + errSeverity + "', '" + errLongText + "', " + isForceable + ", '" + errGrpName + "', '" + errLabel + "');");
		out.println("errorsArray[errorsArray.length] = singleError;");

		out.println("		errViewObj.addErrorsAsArray(errorsArray, parent.opener.document.forms[0].elements);");	
		out.println("</script>");
	}

	protected void udsSecondoLivello(ServletEnvironment se, ClassADCollection cadc) throws ServletException, IOException {
		String[] chiaviSel = (String[]) (se.getRequest().getParameterValues(OBJECT_KEY));
		if(chiaviSel != null) {
			ErrorMessage em = YUdsVendita.controllaUdsSecondoLivello(chiaviSel);
			if(em == null) {
				se.getSession().setAttribute("chiaviYUdsVendita", chiaviSel);
				String url = "it/softre/thip/vendite/uds/YUdsSecondoLivello.jsp";
				executeJSOpenAction(se, url, null);
			}else {
				se.addErrorMessage(em);
				se.sendRequest(getServletContext(), "com/thera/thermfw/common/InfoAreaHandler.jsp", false);
			}
		}else {
			ErrorMessage em = new ErrorMessage("YSOFTRE001","Non e' stata selezionata nessuna UDS");
			se.addErrorMessage(em);
			se.sendRequest(getServletContext(), "com/thera/thermfw/common/InfoAreaHandler.jsp", false);
		}
	}

	public void lanciaEvasioneUds(ServletEnvironment se, ClassADCollection cadc) throws ServletException, IOException {
		String[] chiaviSel = se.getRequest().getParameterValues(OBJECT_KEY);
		se.getRequest().setAttribute("ChiaviSelEvasioneUdsVendita", chiaviSel);
		String url = "it.softre.thip.vendite.uds.web.YEvasioneUdsVendita?thAction="+EVADI_UDS;
		url += "&thClassName=YEvasioneUdsVendita";
		se.getRequest().setAttribute("DaEstratto", false);
		se.sendRequest(getServletContext(), se.getServletPath() + url, false);
	}

	public void executeJSOpenAction(ServletEnvironment se, String url, DocumentoDataCollector docBODC) {
		try {
			PrintWriter out = se.getResponse().getWriter();
			out.println("  <script language=\'JavaScript1.2\'>");
			String initialActionAdapter = getStringParameter(se.getRequest(), "thInitialActionAdapter");
			if(initialActionAdapter != null) {
				out.println("    var errViewObj = window.parent.eval(window.parent.errorsViewName);");
				out.println("    errViewObj.setMessage(null);");
				out.println("    parent.enableFormActions();");
			}
			else {
				out.println("window.parent.ErVwinfoarea.clearDisplay();");
				out.println("window.parent.enableGridActions();");
			}
			if (url.startsWith("/"))
				url = url.substring(1);
			out.println("    var url = '" + se.getWebApplicationPath() + url + "'");
			out.println("    var winFeature = 'width=800, height=700, resizable=yes';");
			out.println("    var winName = '" + String.valueOf(System.currentTimeMillis()) + "';");
			out.println("    var winrUrl = window.open(url, winName, winFeature);");
			if(( (Boolean) se.getRequest().getAttribute("DaEstratto") != null &&
					((Boolean) se.getRequest().getAttribute("DaEstratto")).booleanValue() == false) && se.isErrorListEmpity()) {
				out.println("parent.runAction('" + REFRESH_GRID + "','none','same','no');");
			}
			out.println("  </script>");
		}
		catch (Exception ex) {
			ex.printStackTrace(Trace.excStream);
		}
	}
}
