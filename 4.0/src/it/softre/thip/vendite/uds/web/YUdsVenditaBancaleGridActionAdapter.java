package it.softre.thip.vendite.uds.web;

import com.thera.thermfw.base.IniFile;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.web.ServletEnvironment;
import com.thera.thermfw.web.servlet.BaseServlet;

import it.softre.thip.vendite.uds.YUdsVendita;

/**
 * 
 * @author DSSOF3	
 *	DSSOF3	70687	04/10/2022	Prima stesura.	
 */

public class YUdsVenditaBancaleGridActionAdapter extends BaseServlet{

	private static final long serialVersionUID = 1L;

	/**
	 * Alla pressione del bottone vengo reindirizzato in questa servlet dove creo la nuova Testata YUdsVendita, e setto il suo idUds come udsPadre delle testate 
	 * precedentemente selezionate
	 */
	@Override
	protected void processAction(ServletEnvironment se) throws Exception {
		String[] chiaviSel = (String[]) se.getSession().getAttribute("chiaviYUdsVendita");
		String tipoUds = se.getRequest().getParameter("TipoUds");
		se.getSession().removeAttribute("chiaviYUdsVendita");
		if(chiaviSel != null) {
			String keyBancale = "";
			boolean commit = false;
			keyBancale = creaBancale(chiaviSel,tipoUds);
			if(keyBancale != null) {
				commit = true;
			}
			String webAppPath = IniFile.getValue("thermfw.ini", "Web", "WebApplicationPath");
			String url = "it/softre/thip/vendite/uds/YApriUdsBancale.jsp?Commit="+commit+"&KeyBancale="+keyBancale+"&WebAppPath="+webAppPath;
			se.sendRequest(getServletContext(), url, false);
		}

	}

	public String creaBancale(String[] chiaviSel, String tipoUds) {
		boolean commit = false;
		YUdsVendita udsBancale = null;
		String idCliente = null;
		try {
			udsBancale = (YUdsVendita) Factory.createObject(YUdsVendita.class);
			udsBancale.setRTipoUds(tipoUds);
			udsBancale.setAssegnaNumeratoreAutomatico(false);
			udsBancale.setIdUds(YUdsVendita.getNewProgressivoForBancale());
			if(udsBancale.save() >= 0) {
				commit = true;
			}
			for(int i = 0 ; i < chiaviSel.length; i++) {
				YUdsVendita udsVE = (YUdsVendita) YUdsVendita.elementWithKey(YUdsVendita.class, chiaviSel[i], 0);
				if(udsVE != null) {
					udsVE.setRIdUdsPadre(udsBancale.getIdUds());
					udsVE.setDirty();
					if(udsVE.save() < 0) {
						commit = false;
					}
					idCliente = udsVE.getRCliente() != null ? udsVE.getRCliente() : null;
				}
			}
			if(idCliente != null) {
				udsBancale.setRCliente(idCliente);
				udsBancale.save();
			}
			if(commit) {
				ConnectionManager.commit();
			}else {
				ConnectionManager.rollback();
			}
			//Risalvo tutto settando il ricalcolo pesi
			//Necessario fare cosi dato che in realta' l'uds bancale non ha righe ma sono solo altre uds.
			//Quindi devono essere gia' presenti a database.
			if(commit) {
				udsBancale.setRicalcoloPesi(true);
				int rc = udsBancale.save();
				if(rc  >= 0) {
					ConnectionManager.commit();
				}else {
					ConnectionManager.rollback();
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		return udsBancale.getKey();

	}
}
