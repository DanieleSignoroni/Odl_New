package it.thera.thippers;

import com.thera.thermfw.persist.*;
import java.sql.*;
import com.thera.thermfw.base.*;

public class YordVenRigTM extends TableManager {

	public static final String ID_AZIENDA = "ID_AZIENDA";

	public static final String ID_ANNO_ORD = "ID_ANNO_ORD";

	public static final String ID_NUMERO_ORD = "ID_NUMERO_ORD";

	public static final String ID_RIGA_ORD = "ID_RIGA_ORD";

	public static final String ID_DET_RIGA_ORD = "ID_DET_RIGA_ORD";

	public static final String DATAPRVSPE = "DATAPRVSPE";

	public static final String DATA2 = "DATA2";

	public static final String SPEDSOSP = "SPEDSOSP";

	public static final String ID_CONTRATO = "ID_CONTRATO";

	public static final String EXP_CONTRATTO_DDT = "EXP_CONTRATTO_DDT";

	public static final String TIMESTAMP = "TIMESTAMP";

	public static final String TABLE_NAME = SystemParam.getSchema("THIPPERS") + "YORD_VEN_RIG";

	private static TableManager cInstance;

	private static final String CLASS_NAME = it.thera.thippers.YordVenRig.class.getName();

	public synchronized static TableManager getInstance() throws SQLException {
		if (cInstance == null) {
			cInstance = (TableManager)Factory.createObject(YordVenRigTM.class);
		}
		return cInstance;
	}

	public YordVenRigTM() throws SQLException {
		super();
	}

	protected void initialize() throws SQLException {
		setTableName(TABLE_NAME);
		setObjClassName(CLASS_NAME);
		init();
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();
		addAttribute("AnnoOrdine", ID_ANNO_ORD);
		addAttribute("NumeroOrdine", ID_NUMERO_ORD);
		addAttribute("RigaOrdine", ID_RIGA_ORD, "getIntegerObject");
		addAttribute("ID_DET_RIGA_ORD", ID_DET_RIGA_ORD, "getIntegerObject");
		addAttribute("Dataprvspe", DATAPRVSPE);
		addAttribute("Data2", DATA2);
		addAttribute("Spedsosp", SPEDSOSP);
		addAttribute("ExpContratto", EXP_CONTRATTO_DDT);
		addAttribute("IdAzienda", ID_AZIENDA);
		addAttribute("IdContrato", ID_CONTRATO, "getIntegerObject");
		addTimestampAttribute("Timestamp" , TIMESTAMP);
		setKeys(ID_AZIENDA + "," + ID_ANNO_ORD + "," + ID_NUMERO_ORD + "," + ID_RIGA_ORD);
	}

	private void init() throws SQLException {
		configure();
	}

}

