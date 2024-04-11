package it.thera.thippers;

import com.thera.thermfw.persist.*;
import java.sql.*;
import java.util.*;
import it.odl.thip.YCondCtrVen;
import com.thera.thermfw.common.*;
import it.thera.thip.base.azienda.Azienda;
import com.thera.thermfw.security.*;

public abstract class YordVenRigPO extends PersistentObject implements BusinessObject, Authorizable, Deletable, Conflictable {

	private static YordVenRig cInstance;

	protected String iAnnoOrdine;

	protected String iNumeroOrdine;

	protected Integer iRigaOrdine;

	protected Integer iID_DET_RIGA_ORD = new Integer("0");

	protected java.sql.Date iDataprvspe;

	protected java.sql.Date iData2;

	protected boolean iSpedsosp = false;

	protected boolean iExpContratto = true;

	protected java.sql.Timestamp iTimestamp;

	protected Proxy iRelcontratto = new Proxy(it.odl.thip.YCondCtrVen.class);

	@SuppressWarnings("rawtypes")
	public static Vector retrieveList(String where, String orderBy, boolean optimistic) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (cInstance == null)
			cInstance = (YordVenRig)Factory.createObject(YordVenRig.class);
		return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
	}

	public static YordVenRig elementWithKey(String key, int lockType) throws SQLException {
		return (YordVenRig)PersistentObject.elementWithKey(YordVenRig.class, key, lockType);
	}

	public YordVenRigPO() {
		setID_DET_RIGA_ORD(new Integer(0));
		setSpedsosp(false);
		setExpContratto(true);
		setIdAzienda(Azienda.getAziendaCorrente());
	}

	public void setAnnoOrdine(String annoOrdine) {
		this.iAnnoOrdine = annoOrdine;
		setDirty();
		setOnDB(false);
	}

	public String getAnnoOrdine() {
		return iAnnoOrdine;
	}

	public void setNumeroOrdine(String numeroOrdine) {
		this.iNumeroOrdine = numeroOrdine;
		setDirty();
		setOnDB(false);
	}

	public String getNumeroOrdine() {
		return iNumeroOrdine;
	}

	public void setRigaOrdine(Integer rigaOrdine) {
		this.iRigaOrdine = rigaOrdine;
		setDirty();
		setOnDB(false);
	}

	public Integer getRigaOrdine() {
		return iRigaOrdine;
	}

	public void setID_DET_RIGA_ORD(Integer iD_DET_RIGA_ORD) {
		this.iID_DET_RIGA_ORD = iD_DET_RIGA_ORD;
		setDirty();
	}

	public Integer getID_DET_RIGA_ORD() {
		return iID_DET_RIGA_ORD;
	}

	public void setDataprvspe(java.sql.Date dataprvspe) {
		this.iDataprvspe = dataprvspe;
		setDirty();
	}

	public java.sql.Date getDataprvspe() {
		return iDataprvspe;
	}

	public void setData2(java.sql.Date data2) {
		this.iData2 = data2;
		setDirty();
	}

	public java.sql.Date getData2() {
		return iData2;
	}

	public void setSpedsosp(boolean spedsosp) {
		this.iSpedsosp = spedsosp;
		setDirty();
	}

	public boolean getSpedsosp() {
		return iSpedsosp;
	}

	public void setExpContratto(boolean expContratto) {
		this.iExpContratto = expContratto;
		setDirty();
	}

	public boolean getExpContratto() {
		return iExpContratto;
	}

	public void setTimestamp(java.sql.Timestamp timestamp) {
		this.iTimestamp = timestamp;

	}

	public java.sql.Timestamp getTimestamp() {
		return iTimestamp;
	}

	public void setRelcontratto(YCondCtrVen relcontratto) {
		String oldObjectKey = getKey();
		this.iRelcontratto.setObject(relcontratto);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public YCondCtrVen getRelcontratto() {
		return (YCondCtrVen)iRelcontratto.getObject();
	}

	public void setRelcontrattoKey(String key) {
		String oldObjectKey = getKey();
		iRelcontratto.setKey(key);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
		}
	}

	public String getRelcontrattoKey() {
		return iRelcontratto.getKey();
	}

	public void setIdAzienda(String idAzienda) {
		String key = iRelcontratto.getKey();
		iRelcontratto.setKey(KeyHelper.replaceTokenObjectKey(key , 1, idAzienda));
		setDirty();
		setOnDB(false);
	}

	public String getIdAzienda() {
		String key = iRelcontratto.getKey();
		String objIdAzienda = KeyHelper.getTokenObjectKey(key,1);
		return objIdAzienda;

	}

	public void setIdContrato(Integer idContrato) {
		String key = iRelcontratto.getKey();
		iRelcontratto.setKey(KeyHelper.replaceTokenObjectKey(key , 2, idContrato));
		setDirty();
	}

	public Integer getIdContrato() {
		String key = iRelcontratto.getKey();
		String objIdContrato = KeyHelper.getTokenObjectKey(key,2);
		return KeyHelper.stringToIntegerObj(objIdContrato);
	}

	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
		YordVenRigPO yordVenRigPO = (YordVenRigPO)obj;
		if (yordVenRigPO.iDataprvspe != null)
			iDataprvspe = (java.sql.Date)yordVenRigPO.iDataprvspe.clone();
		if (yordVenRigPO.iData2 != null)
			iData2 = (java.sql.Date)yordVenRigPO.iData2.clone();
		if (yordVenRigPO.iTimestamp != null)
			iTimestamp = (java.sql.Timestamp)yordVenRigPO.iTimestamp.clone();
		iRelcontratto.setEqual(yordVenRigPO.iRelcontratto);
	}

	@SuppressWarnings("rawtypes")
	public Vector checkAll(BaseComponentsCollection components) {
		Vector errors = new Vector();
		components.runAllChecks(errors);
		return errors;
	}

	public void setKey(String key) {
		setIdAzienda(KeyHelper.getTokenObjectKey(key, 1));
		setAnnoOrdine(KeyHelper.getTokenObjectKey(key, 2));
		setNumeroOrdine(KeyHelper.getTokenObjectKey(key, 3));
		setRigaOrdine(KeyHelper.stringToIntegerObj(KeyHelper.getTokenObjectKey(key, 4)));
	}

	public String getKey() {
		String idAzienda = getIdAzienda();
		String annoOrdine = getAnnoOrdine();
		String numeroOrdine = getNumeroOrdine();
		Integer rigaOrdine = getRigaOrdine();
		Object[] keyParts = {idAzienda, annoOrdine, numeroOrdine, rigaOrdine};
		return KeyHelper.buildObjectKey(keyParts);
	}

	public boolean isDeletable() {
		return checkDelete() == null;
	}

	public String toString() {
		return getClass().getName() + " [" + KeyHelper.formatKeyString(getKey()) + "]";
	}

	protected TableManager getTableManager() throws SQLException {
		return YordVenRigTM.getInstance();
	}

}

