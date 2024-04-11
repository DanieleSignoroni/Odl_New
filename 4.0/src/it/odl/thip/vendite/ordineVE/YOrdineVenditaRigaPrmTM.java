package it.odl.thip.vendite.ordineVE;

import java.sql.SQLException;

import com.thera.thermfw.base.SystemParam;

import it.thera.thip.vendite.ordineVE.OrdineVenditaRigaPrmTM;

public class YOrdineVenditaRigaPrmTM extends OrdineVenditaRigaPrmTM{

	public static final String DATAPRVSPE = "DATAPRVSPE";

	public static final String DATA2 = "DATA2";

	public static final String SPEDSOSP = "SPEDSOSP";

	public static final String ID_CONTRATO = "ID_CONTRATO";

	public static final String EXP_CONTRATTO_DDT = "EXP_CONTRATTO_DDT";
	
	public static final String TABLE_NAME_EXT_1 = SystemParam.getSchema("THIPPERS") + "YORD_VEN_RIG";

	private static final String CLASS_NAME = it.odl.thip.vendite.ordineVE.YOrdineVenditaRigaPrm.class.getName();


	public YOrdineVenditaRigaPrmTM() throws SQLException {
		super();
	}

	protected void initialize() throws SQLException {
		super.initialize();
		setObjClassName(CLASS_NAME);
	}

	protected void initializeRelation() throws SQLException {
		super.initializeRelation();
		linkTable(TABLE_NAME_EXT_1);
		addAttributeOnTable("Dataprvspe", DATAPRVSPE, TABLE_NAME_EXT_1);
		addAttributeOnTable("Data2", DATA2, TABLE_NAME_EXT_1);
		addAttributeOnTable("Spedsosp", SPEDSOSP, TABLE_NAME_EXT_1);
		addAttributeOnTable("ExpContratto", EXP_CONTRATTO_DDT, TABLE_NAME_EXT_1);
		addAttributeOnTable("IdContrato", ID_CONTRATO, TABLE_NAME_EXT_1);

		getColumn(TABLE_NAME_EXT_1, ID_DET_RIGA_ORD).excludeFromUpdate();
		getColumn(TABLE_NAME_EXT_1, ID_DET_RIGA_ORD).excludeFromInsert();
	}
	
}
