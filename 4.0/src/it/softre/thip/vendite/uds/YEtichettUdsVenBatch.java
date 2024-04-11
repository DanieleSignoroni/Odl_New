package it.softre.thip.vendite.uds;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.batch.AvailableReport;
import com.thera.thermfw.batch.CrystalReportsInterface;
import com.thera.thermfw.batch.ElaboratePrintRunnable;
import com.thera.thermfw.batch.PrintingToolInterface;
import com.thera.thermfw.common.ErrorMessage;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.persist.Factory;
import com.thera.thermfw.persist.PersistentObject;
import com.thera.thermfw.security.Authorizable;

import it.softre.thip.uds.YRptUdsVen;
import it.softre.thip.uds.YRptUdsVenTM;
import it.thera.thip.base.azienda.Azienda;
import it.thera.thip.cs.ColonneFiltri;
import it.thera.thip.vendite.proposteEvasione.CreaMessaggioErrore;

/**
 * 
 * @author DSSOF3	
 *	70687	DSSOF3	Classe rpt per stampare un etichetta uds vendita.
 */

public class YEtichettUdsVenBatch extends ElaboratePrintRunnable implements Authorizable{

	protected AvailableReport iAvailableRpt;

	public String iChiaviSelezionati;

	public String getChiaviSelezionati() {
		return iChiaviSelezionati;
	}

	public void setChiaviSelezionati(String iChiaviSelezionati) {
		this.iChiaviSelezionati = iChiaviSelezionati;
	}

	public AvailableReport getiAvailableRpt() {
		return iAvailableRpt;
	}

	public void setiAvailableRpt(AvailableReport iAvailableRpt) {
		this.iAvailableRpt = iAvailableRpt;
	}

	public void writeLog(String log) {
		System.out.println(log);
		getOutput().println(log);
	}

	protected boolean createAvailableReport() throws Exception{
		job.setReportCounter((short)0);
		iAvailableRpt = createNewReport(getReportId());
		if(iAvailableRpt == null)
			return false;

		try {
			setPrintToolInterface((PrintingToolInterface)Factory.createObject(CrystalReportsInterface.class));
			String s = printToolInterface.generateDefaultWhereCondition(iAvailableRpt, YRptUdsVenTM.getInstance());
			iAvailableRpt.setWhereCondition(s);
			int res = iAvailableRpt.save();
			if(res < 0) {
				System.out.println("Problema di salvataggio availableReport, errorCode = " + res);
				return false;
			}
		}
		catch(SQLException e) {
			e.printStackTrace(Trace.excStream);
			return false;
		}
		return true;
	}

	@Override
	public boolean createReport() {
		boolean isOk = true;
		try {
			isOk = createAvailableReport();
			if(isOk == false) {
				output.println("ERRORE: AvailableReport non disponibile!!!");
				writeLog("ERRORE: AvailableReport non disponibile!!!");
				return false;
			}
			isOk = popolaTabellaRpt();
			if(isOk) {
				ConnectionManager.commit();
			}else {
				ConnectionManager.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace(Trace.excStream);
		}
		return isOk;
	}

	protected boolean popolaTabellaRpt() throws SQLException {
		List<String> keyList = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(getChiaviSelezionati(), ColonneFiltri.LISTA_SEP);
		while (tokenizer.hasMoreTokens()) {
			String next = tokenizer.nextToken(ColonneFiltri.LISTA_SEP);
			keyList.add(next);
		}
		int rigaJobId = 0;
		for(String key : keyList) {
			YUdsVendita uds = (YUdsVendita)
					YUdsVendita.elementWithKey(YUdsVendita.class, 
							key, PersistentObject.NO_LOCK);
			uds.setStatoStEti(YUdsVendita.ST_ETI_STAMPATA); //La flaggo come stampata.
			if(uds != null) {
				YRptUdsVen rpt = (YRptUdsVen) Factory.createObject(YRptUdsVen.class);
				rpt.setIdAzienda(Azienda.getAziendaCorrente());
				rpt.setIdUds(uds.getIdUds());
				rpt.setBatchJobId(getBatchJob().getBatchJobId());
				rpt.setReportNr(1);
				rpt.setRigaJobId(rigaJobId);
				int rc = rpt.save();
				rc += uds.save();
				if(rc < 0) {
					ErrorMessage em = CreaMessaggioErrore.daRcAErrorMessage(rc, (SQLException) (rpt.getException() != null ? rpt.getException() : null));
					if(em != null) {
						output.println(" ** --> Impossibile salvare la riga RPT : "+em.getText());
					}else {
						output.println(" ** --> Impossibile salvare la riga RPT, rc = "+rc);
					}
					return false;
				}
				rigaJobId++;
			}
		}
		return true;
	}

	protected String getClassAdCollectionName(){
		return "YEtichettUdsVen";
	}

}
