package it.odl.thip.logisticaLight;

import java.math.BigDecimal;

import com.thera.thermfw.common.BusinessObjectAdapter;
import com.thera.thermfw.persist.KeyHelper;
import com.thera.thermfw.persist.Proxy;

import it.softre.thip.base.uds.YTipoUds;
import it.thera.thip.base.azienda.Azienda;

public class YCreazionePallet extends BusinessObjectAdapter {

	protected String iIdAzienda;

	protected Proxy iTipoPallet = new Proxy(it.softre.thip.base.uds.YTipoUds.class);

	protected String iBarcodeScatola;
	
	protected BigDecimal iAltezza;

	public YCreazionePallet() {
		setIdAzienda(Azienda.getAziendaCorrente());
	}

	public String getIdAzienda() {
		return iIdAzienda;
	}

	public void setIdAzienda(String iIdAzienda) {
		this.iIdAzienda = iIdAzienda;
		setAziendaInternal(iIdAzienda);
	}

	protected void setAziendaInternal(String idAzienda) {
		String key1 = iTipoPallet.getKey();
		iTipoPallet.setKey(KeyHelper.replaceTokenObjectKey(key1, 1, idAzienda));

	}

	public String getBarcodeScatola() {
		return iBarcodeScatola;
	}

	public void setBarcodeScatola(String iBarcodeScatola) {
		this.iBarcodeScatola = iBarcodeScatola;
	}

	public void setTipoPallet(YTipoUds reltipouds) {
		String idAzienda = getIdAzienda();
		if (reltipouds != null) {
			idAzienda = KeyHelper.getTokenObjectKey(reltipouds.getKey(), 1);
		}
		setAziendaInternal(idAzienda);
		this.iTipoPallet.setObject(reltipouds);
	}

	public YTipoUds getTipoPallet() {
		return (YTipoUds)iTipoPallet.getObject();
	}

	public void setTipoPalletKey(String key) {
		iTipoPallet.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setAziendaInternal(idAzienda);
	}

	public String getTipoPalletKey() {
		return iTipoPallet.getKey();
	}

	public void setIdPallet(String rTipoUds) {
		String key = iTipoPallet.getKey();
		iTipoPallet.setKey(KeyHelper.replaceTokenObjectKey(key , 2, rTipoUds));
	}

	public String getIdPallet() {
		String key = iTipoPallet.getKey();
		String objRTipoUds = KeyHelper.getTokenObjectKey(key,2);
		return objRTipoUds;
	}

	public BigDecimal getAltezza() {
		return iAltezza;
	}

	public void setAltezza(BigDecimal iAltezza) {
		this.iAltezza = iAltezza;
	}
	

}
