package it.odl.thip;

import com.thera.thermfw.persist.*;
import java.sql.*;
import java.util.*;
import it.thera.thip.base.cliente.ClienteVendita;
import it.thera.thip.base.articolo.Articolo;
import java.math.*;
import com.thera.thermfw.cbs.*;
import it.thera.thip.cs.*;
import com.thera.thermfw.common.*;
import it.thera.thip.base.azienda.Azienda;
import com.thera.thermfw.security.*;

public abstract class YCondCtrVenPO extends EntitaAzienda implements Commentable, BusinessObject, Authorizable, Deletable, Conflictable {


	/**
	 *  instance
	 */
	private static YCondCtrVen cInstance;

	/**
	 * Attributo iDatainizio
	 */
	protected java.sql.Date iDatainizio;

	/**
	 * Attributo iDatafine
	 */
	protected java.sql.Date iDatafine;

	/**
	 * Attributo iQtaprevista
	 */
	protected BigDecimal iQtaprevista;

	/**
	 * Attributo iQtaordinata
	 */
	protected BigDecimal iQtaordinata;

	/**
	 * Attributo iQtaconsegnata
	 */
	protected BigDecimal iQtaconsegnata;

	/**
	 * Attributo iSogliacontrollo
	 */
	protected BigDecimal iSogliacontrollo;

	/**
	 * Attributo iPrezzo
	 */
	protected BigDecimal iPrezzo;

	/**
	 * Attributo iSconto
	 */
	protected BigDecimal iSconto;

	/**
	 * Attributo iPerccompletam
	 */
	protected BigDecimal iPerccompletam;

	/**
	 * Attributo iTipoRegistrazione
	 */
	protected char iTipoRegistrazione = '0';

	/**
	 * Attributo iPerccompletamy
	 */
	protected BigDecimal iPerccompletamy;

	/**
	 * Attributo iSaldoManuale
	 */
	protected boolean iSaldoManuale = false;

	/**
	 * Attributo iFlagAcq
	 */
	protected boolean iFlagAcq = false;

	/**
	 * Attributo iRendiCorrente
	 */
	protected boolean iRendiCorrente = false;

	/**
	 * Attributo iIdReg
	 */
	protected Integer iIdReg;

	/**
	 * Attributo iRifContratto
	 */
	protected String iRifContratto;

	/**
	 * Attributo iDataContratto
	 */
	protected java.sql.Date iDataContratto;

	/**
	 * Attributo iCliente
	 */
	protected Proxy iCliente = new Proxy(it.thera.thip.base.cliente.ClienteVendita.class);

	/**
	 * Attributo iArticolo
	 */
	protected Proxy iArticolo = new Proxy(it.thera.thip.base.articolo.Articolo.class);

	/**
	 * Attributo iPianoIpotesiProduzione
	 */
	protected OneToMany iPianoIpotesiProduzione = new OneToMany(it.odl.thip.YCondCtrVenPrd.class, this, 3, true);

	/**
	 * Attributo iCommentHandlerManager
	 */
	protected CommentHandlerManager iCommentHandlerManager;


	/**
	 *  retrieveList
	 * @param where
	 * @param orderBy
	 * @param optimistic
	 * @return Vector
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    CodeGen     Codice generato da CodeGenerator
	 *
	 */
	@SuppressWarnings("rawtypes")
	public static Vector retrieveList(String where, String orderBy, boolean optimistic) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (cInstance == null)
			cInstance = (YCondCtrVen)Factory.createObject(YCondCtrVen.class);
		return PersistentObject.retrieveList(cInstance, where, orderBy, optimistic);
	}

	/**
	 *  elementWithKey
	 * @param key
	 * @param lockType
	 * @return YCondCtrVen
	 * @throws SQLException
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    CodeGen     Codice generato da CodeGenerator
	 *
	 */
	public static YCondCtrVen elementWithKey(String key, int lockType) throws SQLException {
		return (YCondCtrVen)PersistentObject.elementWithKey(YCondCtrVen.class, key, lockType);
	}

	/**
	 * YCondCtrVenPO
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public YCondCtrVenPO() {
		iCommentHandlerManager = (CommentHandlerManager) Factory.createObject(CommentHandlerManager.class);
		iCommentHandlerManager.setOwner(this);

		setTipoRegistrazione('0');
		setSaldoManuale(false);
		setFlagAcq(false);
		setRendiCorrente(false);
		setIdReg(new Integer(0));
		setIdAzienda(Azienda.getAziendaCorrente());
	}

	/**
	 * Valorizza l'attributo. 
	 * @param datainizio
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setDatainizio(java.sql.Date datainizio) {
		this.iDatainizio = datainizio;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return java.sql.Date
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public java.sql.Date getDatainizio() {
		return iDatainizio;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param datafine
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setDatafine(java.sql.Date datafine) {
		this.iDatafine = datafine;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return java.sql.Date
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public java.sql.Date getDatafine() {
		return iDatafine;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param qtaprevista
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setQtaprevista(BigDecimal qtaprevista) {
		this.iQtaprevista = qtaprevista;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return BigDecimal
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public BigDecimal getQtaprevista() {
		return iQtaprevista;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param qtaordinata
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setQtaordinata(BigDecimal qtaordinata) {
		this.iQtaordinata = qtaordinata;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return BigDecimal
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public BigDecimal getQtaordinata() {
		return iQtaordinata;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param qtaconsegnata
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setQtaconsegnata(BigDecimal qtaconsegnata) {
		this.iQtaconsegnata = qtaconsegnata;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return BigDecimal
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public BigDecimal getQtaconsegnata() {
		return iQtaconsegnata;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param sogliacontrollo
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setSogliacontrollo(BigDecimal sogliacontrollo) {
		this.iSogliacontrollo = sogliacontrollo;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return BigDecimal
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public BigDecimal getSogliacontrollo() {
		return iSogliacontrollo;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param prezzo
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setPrezzo(BigDecimal prezzo) {
		this.iPrezzo = prezzo;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return BigDecimal
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public BigDecimal getPrezzo() {
		return iPrezzo;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param sconto
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setSconto(BigDecimal sconto) {
		this.iSconto = sconto;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return BigDecimal
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public BigDecimal getSconto() {
		return iSconto;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param perccompletam
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setPerccompletam(BigDecimal perccompletam) {
		this.iPerccompletam = perccompletam;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return BigDecimal
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public BigDecimal getPerccompletam() {
		return iPerccompletam;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param tipoRegistrazione
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setTipoRegistrazione(char tipoRegistrazione) {
		this.iTipoRegistrazione = tipoRegistrazione;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return char
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public char getTipoRegistrazione() {
		return iTipoRegistrazione;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param perccompletamy
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setPerccompletamy(BigDecimal perccompletamy) {
		this.iPerccompletamy = perccompletamy;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return BigDecimal
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public BigDecimal getPerccompletamy() {
		return iPerccompletamy;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param saldoManuale
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setSaldoManuale(boolean saldoManuale) {
		this.iSaldoManuale = saldoManuale;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return boolean
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public boolean getSaldoManuale() {
		return iSaldoManuale;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param flagAcq
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setFlagAcq(boolean flagAcq) {
		this.iFlagAcq = flagAcq;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return boolean
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public boolean getFlagAcq() {
		return iFlagAcq;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param rendiCorrente
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setRendiCorrente(boolean rendiCorrente) {
		this.iRendiCorrente = rendiCorrente;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return boolean
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public boolean getRendiCorrente() {
		return iRendiCorrente;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param idReg
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setIdReg(Integer idReg) {
		this.iIdReg = idReg;
		setDirty();
		setOnDB(false);
		iPianoIpotesiProduzione.setFatherKeyChanged();
		iCommentHandlerManager.setOwnerKeyChanged();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return Integer
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public Integer getIdReg() {
		return iIdReg;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param rifContratto
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setRifContratto(String rifContratto) {
		this.iRifContratto = rifContratto;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return String
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public String getRifContratto() {
		return iRifContratto;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param dataContratto
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setDataContratto(java.sql.Date dataContratto) {
		this.iDataContratto = dataContratto;
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return java.sql.Date
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public java.sql.Date getDataContratto() {
		return iDataContratto;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param cliente
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setCliente(ClienteVendita cliente) {
		String oldObjectKey = getKey();
		String idAzienda = getIdAzienda();
		if (cliente != null) {
			idAzienda = KeyHelper.getTokenObjectKey(cliente.getKey(), 1);
		}
		setIdAziendaInternal(idAzienda);
		this.iCliente.setObject(cliente);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
			iPianoIpotesiProduzione.setFatherKeyChanged();
			iCommentHandlerManager.setOwnerKeyChanged();
		}
	}

	/**
	 * Restituisce l'attributo. 
	 * @return ClienteVendita
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public ClienteVendita getCliente() {
		return (ClienteVendita)iCliente.getObject();
	}

	/**
	 * setClienteKey
	 * @param key
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setClienteKey(String key) {
		String oldObjectKey = getKey();
		iCliente.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setIdAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
			iPianoIpotesiProduzione.setFatherKeyChanged();
			iCommentHandlerManager.setOwnerKeyChanged();
		}
	}

	/**
	 * getClienteKey
	 * @return String
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public String getClienteKey() {
		return iCliente.getKey();
	}

	/**
	 * Valorizza l'attributo. 
	 * @param idcliente
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setIdcliente(String idcliente) {
		String key = iCliente.getKey();
		iCliente.setKey(KeyHelper.replaceTokenObjectKey(key , 2, idcliente));
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return String
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public String getIdcliente() {
		String key = iCliente.getKey();
		String objIdcliente = KeyHelper.getTokenObjectKey(key,2);
		return objIdcliente;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param articolo
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setArticolo(Articolo articolo) {
		String oldObjectKey = getKey();
		String idAzienda = getIdAzienda();
		if (articolo != null) {
			idAzienda = KeyHelper.getTokenObjectKey(articolo.getKey(), 1);
		}
		setIdAziendaInternal(idAzienda);
		this.iArticolo.setObject(articolo);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
			iPianoIpotesiProduzione.setFatherKeyChanged();
			iCommentHandlerManager.setOwnerKeyChanged();
		}
	}

	/**
	 * Restituisce l'attributo. 
	 * @return Articolo
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public Articolo getArticolo() {
		return (Articolo)iArticolo.getObject();
	}

	/**
	 * setArticoloKey
	 * @param key
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setArticoloKey(String key) {
		String oldObjectKey = getKey();
		iArticolo.setKey(key);
		String idAzienda = KeyHelper.getTokenObjectKey(key, 1);
		setIdAziendaInternal(idAzienda);
		setDirty();
		if (!KeyHelper.areEqual(oldObjectKey, getKey())) {
			setOnDB(false);
			iPianoIpotesiProduzione.setFatherKeyChanged();
			iCommentHandlerManager.setOwnerKeyChanged();
		}
	}

	/**
	 * getArticoloKey
	 * @return String
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public String getArticoloKey() {
		return iArticolo.getKey();
	}

	/**
	 * Valorizza l'attributo. 
	 * @param idAzienda
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setIdAzienda(String idAzienda) {
		setIdAziendaInternal(idAzienda);
		setDirty();
		setOnDB(false);
		iPianoIpotesiProduzione.setFatherKeyChanged();
		iCommentHandlerManager.setOwnerKeyChanged();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return String
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public String getIdAzienda() {
		String key = iAzienda.getKey();
		return key;
	}

	/**
	 * Valorizza l'attributo. 
	 * @param idarticolo
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setIdarticolo(String idarticolo) {
		String key = iArticolo.getKey();
		iArticolo.setKey(KeyHelper.replaceTokenObjectKey(key , 2, idarticolo));
		setDirty();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return String
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public String getIdarticolo() {
		String key = iArticolo.getKey();
		String objIdarticolo = KeyHelper.getTokenObjectKey(key,2);
		return objIdarticolo;
	}

	/**
	 * getPianoIpotesiProduzione
	 * @return List
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	@SuppressWarnings("rawtypes")
	public List getPianoIpotesiProduzione() {
		return getPianoIpotesiProduzioneInternal();
	}

	/**
	 * Restituisce l'attributo. 
	 * @return CommentHandlerManager
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public CommentHandlerManager getCommentHandlerManager() {
		return iCommentHandlerManager;
	}

	/**
	 * getCommentHandler
	 * @return CommentHandler
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public CommentHandler getCommentHandler() {
		return iCommentHandlerManager.getObject();
	}

	/**
	 * save
	 * @return int
	 * @throws SQLException
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public int save() throws SQLException {
		int rcCommentHandler = iCommentHandlerManager.save();
		if (rcCommentHandler >= ErrorCodes.NO_ROWS_UPDATED)  {
			int rc = super.save();
			if (rc >= ErrorCodes.NO_ROWS_UPDATED)
				rcCommentHandler = rcCommentHandler + rc;
			else
				rcCommentHandler = rc;
		}
		return rcCommentHandler;
	}

	/**
	 * delete
	 * @return int
	 * @throws SQLException
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public int delete() throws SQLException {
		return iCommentHandlerManager.delete(super.delete());
	}

	/**
	 * setEqual
	 * @param obj
	 * @throws CopyException
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setEqual(Copyable obj) throws CopyException {
		super.setEqual(obj);
		YCondCtrVenPO yCondCtrVenPO = (YCondCtrVenPO)obj;
		if (yCondCtrVenPO.iDatainizio != null)
			iDatainizio = (java.sql.Date)yCondCtrVenPO.iDatainizio.clone();
		if (yCondCtrVenPO.iDatafine != null)
			iDatafine = (java.sql.Date)yCondCtrVenPO.iDatafine.clone();
		if (yCondCtrVenPO.iDataContratto != null)
			iDataContratto = (java.sql.Date)yCondCtrVenPO.iDataContratto.clone();
		iCliente.setEqual(yCondCtrVenPO.iCliente);
		iArticolo.setEqual(yCondCtrVenPO.iArticolo);
		iPianoIpotesiProduzione.setEqual(yCondCtrVenPO.iPianoIpotesiProduzione);
		iCommentHandlerManager.setEqual(yCondCtrVenPO.iCommentHandlerManager);
	}

	/**
	 * checkAll
	 * @param components
	 * @return Vector
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	@SuppressWarnings("rawtypes")
	public Vector checkAll(BaseComponentsCollection components) {
		Vector errors = new Vector();
		if (!isOnDB()) {
			setIdReg(new Integer(0));
		}
		components.runAllChecks(errors);
		return errors;
	}

	/**
	 *  setKey
	 * @param key
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public void setKey(String key) {
		setIdAzienda(KeyHelper.getTokenObjectKey(key, 1));
		setIdReg(KeyHelper.stringToIntegerObj(KeyHelper.getTokenObjectKey(key, 2)));
	}

	/**
	 *  getKey
	 * @return String
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public String getKey() {
		String idAzienda = getIdAzienda();
		Integer idReg = getIdReg();
		Object[] keyParts = {idAzienda, idReg};
		return KeyHelper.buildObjectKey(keyParts);
	}

	/**
	 * isDeletable
	 * @return boolean
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public boolean isDeletable() {
		return checkDelete() == null;
	}

	/**
	 * saveOwnedObjects
	 * @param rc
	 * @return int
	 * @throws SQLException
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public int saveOwnedObjects(int rc) throws SQLException {
		rc = iPianoIpotesiProduzione.save(rc);
		return rc;
	}

	/**
	 * deleteOwnedObjects
	 * @return int
	 * @throws SQLException
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public int deleteOwnedObjects() throws SQLException {
		return getPianoIpotesiProduzioneInternal().delete();
	}

	/**
	 * initializeOwnedObjects
	 * @param result
	 * @return boolean
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public boolean initializeOwnedObjects(boolean result) {
		result = iPianoIpotesiProduzione.initialize(result);
		result = iCommentHandlerManager.initialize(result);
		return result;
	}

	/**
	 * toString
	 * @return String
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	public String toString() {
		return getClass().getName() + " [" + KeyHelper.formatKeyString(getKey()) + "]";
	}

	/**
	 *  getTableManager
	 * @return TableManager
	 * @throws SQLException
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    CodeGen     Codice generato da CodeGenerator
	 *
	 */
	protected TableManager getTableManager() throws SQLException {
		return YCondCtrVenTM.getInstance();
	}

	/**
	 * getPianoIpotesiProduzioneInternal
	 * @return OneToMany
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	protected OneToMany getPianoIpotesiProduzioneInternal() {
		if (iPianoIpotesiProduzione.isNew())
			iPianoIpotesiProduzione.retrieve();
		return iPianoIpotesiProduzione;
	}

	/**
	 * setIdAziendaInternal
	 * @param idAzienda
	 */
	/*
	 * Revisions:
	 * Date          Owner      Description
	 * 23/09/2021    Wizard     Codice generato da Wizard
	 *
	 */
	protected void setIdAziendaInternal(String idAzienda) {
		iAzienda.setKey(idAzienda);
		String key2 = iCliente.getKey();
		iCliente.setKey(KeyHelper.replaceTokenObjectKey(key2, 1, idAzienda));
		String key3 = iArticolo.getKey();
		iArticolo.setKey(KeyHelper.replaceTokenObjectKey(key3, 1, idAzienda));
	}

}

