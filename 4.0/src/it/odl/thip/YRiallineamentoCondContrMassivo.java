package it.odl.thip;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import com.thera.thermfw.base.Trace;
import com.thera.thermfw.batch.BatchRunnable;
import com.thera.thermfw.persist.ConnectionManager;
import com.thera.thermfw.security.Authorizable;

/**
 * <h1>Softre Solutions</h1>
 * <br>
 * @author Daniele Signoroni 04/04/2024
 * <br><br>
 * <b></b>
 * <p>
 * Prima stesura.<br>
 * Riallineamento condizioni contrattuali.
 * </p>
 */

public class YRiallineamentoCondContrMassivo extends BatchRunnable implements Authorizable{

	@Override
	protected boolean run() {
		boolean isOk = true;
		output.println("** INIZIATO RIALLINEAMENTO CONDIZIONI CONTRATTUALI MASSIVO **");
		isOk = aggiornaCondizioniContrattuali();
		output.println("** FINE RIALLINEAMENTO CONDIZIONI CONTRATTUALI MASSIVO **");
		return isOk;
	}

	protected boolean aggiornaCondizioniContrattuali() {
		boolean isOk = true;
		Vector<YCondCtrVen> contratti = YCondCtrVen.recuperaContrattiValidiOggi();
		output.println(" --> Ho trovato = "+contratti.size()+" contratti da aggiornare");
		int aggiornati = 0;
		for (Iterator<YCondCtrVen> iterator = contratti.iterator(); iterator.hasNext();) {
			YCondCtrVen contratto = (YCondCtrVen) iterator.next();
			contratto.aggiornaCondizioniContrattuali();
			try {
				int rc = contratto.save();
				if(rc < 0) {
					output.println(" --> Errore nel salvataggio del contratto {"+contratto.getKey()+"} rc : "+rc);
					ConnectionManager.rollback();
				}else {
					aggiornati = aggiornati + 1;
					output.println(" --> Aggiornato correttamente il contratto {"+contratto.getKey()+"} rc : "+rc);
					ConnectionManager.commit();
				}
			} catch (SQLException e) {
				isOk = false;
				output.println(" --> Errore nel salvataggio del contratto {"+contratto.getKey()+"} : "+e.getMessage());
				e.printStackTrace(Trace.excStream);
			}
		}
		if(contratti.size() > 0) {
			output.println("--> Aggiornati correttamente = "+aggiornati+" contratti su "+contratti.size()+" contratti.");
		}
		return isOk;
	}

	@Override
	protected String getClassAdCollectionName() {
		return "YRiallCondContr";
	}

}
