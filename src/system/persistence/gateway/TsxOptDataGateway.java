package system.persistence.gateway;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import system.persistence.DBIO;
import tsxdk.entity.TsEntity;
import utility.misc.BatchSetter;

public final class TsxOptDataGateway {
	private DBIO dbIO;
	private PreparedStatement preparedInsertNumeric;
	private PreparedStatement preparedUpdateNumeric;
	private PreparedStatement preparedInsertString;
	private PreparedStatement preparedUpdateString;
	private PreparedStatement preparedSelect;
//	private PreparedStatement preparedSelectAll;
	private PreparedStatement preparedDelete;
	private PreparedStatement preparedDeleteAll;
		
	public TsxOptDataGateway() {
		dbIO = DBIO.getSharedInstance();
		prepareStatements();
	}

	private void prepareStatements() {
			preparedInsertNumeric = dbIO.prepareStatement("INSERT_NUMERIC_OPTDATA",
					"INSERT OR REPLACE INTO TsxOptData" + 
					"(Descriptor,TypeId,EntityGlueId,NumericData)"
					+"VALUES(?,?,?,?)");
			
			preparedUpdateNumeric = dbIO.prepareStatement("UPDATE_NUMERIC_OPTDATA",
					"UPDATE TsxOptData SET "
					+ "TypeId = ?,"
					+ "NumericData = ?,"
					+ "StringData = NULL " 
					+ "WHERE "
					+ "EntityGlueId = ? "
					+ "AND "
					+ "Descriptor = ?");
			
			preparedInsertString = dbIO.prepareStatement("INSERT_STRING_OPTDATA",
					"INSERT OR REPLACE INTO TsxOptData" + 
					"(Descriptor,TypeId,EntityGlueId,StringData)"
					+"VALUES(?,?,?,?)");
			
			preparedUpdateString = dbIO.prepareStatement("UPDATE_STRING_OPTDATA",
					"UPDATE TsxOptData SET "
					+ "TypeId = ?,"
					+ "NumericData = NULL,"
					+ "StringData = ? " 
					+ "WHERE "
					+ "EntityGlueId = ? "
					+ "AND " 
					+ "Descriptor = ?");
			
			preparedSelect = dbIO.prepareStatement("SELECT_SINGLE_OPTDATA",
					"SELECT TypeId, COALESCE(NumericData,StringData) FROM TsxOptData WHERE EntityGlueId = ? AND Descriptor = ?");
			
//			preparedSelectAll = dbIO.prepareStatement("SELECT_ALL_OPTDATA_FOR_ENTITY",
//					"SELECT TypeId, COALESCE(NumericData,StringData) FROM TsxOptData WHERE EntityGlueId = ?");
			
			preparedDelete = dbIO.prepareStatement("DELETE_SINGLE_OPTDATA", 
					"DELETE FROM TsxOptData WHERE EntityGlueId = ? AND Descriptor = ?");
			
			preparedDeleteAll = dbIO.prepareStatement("DELETE_ALL_OPTDATA_FOR_ENTITY", 
					"DELETE FROM TsxOptData WHERE EntityGlueId = ?");
	}
	
	public <T> void safeValue (TsEntity entity, String descriptor, T value) {
		AuxiliaryGateway glueGW = new AuxiliaryGateway();
		if (!glueGW.isTypeSupported(value)) {
			throw new IllegalArgumentException("Unsupported type for value " + value);
		}
		
		PreparedStatement toDispatch;
		if (value instanceof String) {
			toDispatch = preparedInsertString;
		} else {
			toDispatch = preparedInsertNumeric;
		}

		int glueId = glueGW.getGlueId(entity);
		int typeId = glueGW.getTypeId(value);
		
		new BatchSetter(toDispatch).injectArgs(descriptor,typeId,glueId,value);
		
		dbIO.executeStatement(toDispatch);
	}
	
	public <T> void updateValue (TsEntity entity, String descriptor, T value) {
		AuxiliaryGateway glueGW = new AuxiliaryGateway();
		if (!glueGW.isTypeSupported(value)) {
			throw new IllegalArgumentException("Unsupported type for value " + value);
		}
		
		PreparedStatement toDispatch;
		if (value instanceof String) {
			toDispatch = preparedUpdateString;
		} else {
			toDispatch = preparedUpdateNumeric;
		}
		
		int glueId = glueGW.getGlueId(entity);
		int typeId = glueGW.getTypeId(value);
		
		new BatchSetter(toDispatch).injectArgs(typeId,value,glueId,descriptor);
		
		dbIO.executeStatement(toDispatch);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue (TsEntity entity, String descriptor) {
		AuxiliaryGateway glueGW = GatewayManager.getSharedInstance()
				.getAuxGateway();
		int glueId = glueGW.getGlueId(entity);

		try {
			preparedSelect.setInt(1, glueId);
			preparedSelect.setString(2, descriptor);
			ResultSet rs = dbIO.executeQuery(preparedSelect);
			if (rs.next()) {
				int typeId = rs.getInt(1);
				switch (typeId) {
				case 1: {
					return (T) rs.getString(2);
				}
				case 2: {
					return (T) (Long) rs.getLong(2);
				}
				case 3: {
					return (T) (Double) rs.getDouble(2);
				}
				case 4: {
					return (T) (Integer) rs.getInt(2);
				}
				case 5: {
					return (T) (Boolean) rs.getBoolean(2);
				}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
//	public Collection<Object> getAllFor (TsEntity entity) {
//		AuxiliaryGateway glueGW = GatewayManager.getSharedInstance()
//				.getAuxGateway();
//		int glueId = glueGW.getGlueId(entity) - 1;
//		
//		System.out.println(glueId);
//		
//		Collection<Object> ret = new ArrayList<>();
//		try {
//			preparedSelectAll.setInt(1, glueId);
//			ResultSet rs = dbIO.executeQuery(preparedSelect);
//
//			Object current = null;
//			while (rs.next()) {
//				System.out.println("Result Row processing");
//				int typeId = rs.getInt(1);
//				switch (typeId) {
//				case 1: {
//					current = rs.getString(2); break;
//				}
//				case 2: {
//					current = (Long) rs.getLong(2); break;
//				}
//				case 3: {
//					current = (Double) rs.getDouble(2); break;
//				}
//				case 4: {
//					current = (Integer) rs.getInt(2); break;
//				}
//				case 5: {
//					current = (Boolean) rs.getBoolean(2); break;
//				}
//				}
//				
//				ret.add(current);
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return ret;
//	}
	
	public void deleteValue (TsEntity entity, String descriptor) {
		AuxiliaryGateway glueGW = GatewayManager.getSharedInstance().getAuxGateway();
		int glueId = glueGW.getGlueId(entity);

		try {	
			preparedDelete.setInt(1, glueId);
			preparedDelete.setString(2,descriptor);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbIO.executeStatement(preparedDelete);
	}

	public void deleteAllFor (TsEntity entity) {
		AuxiliaryGateway glueGW = GatewayManager.getSharedInstance().getAuxGateway();
		int glueId = glueGW.getGlueId(entity);

		deleteAllFor(glueId);
	}
	
	public void deleteAllFor (int entityGlueId) {
		try {	
			preparedDeleteAll.setInt(1, entityGlueId);			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbIO.executeStatement(preparedDelete);
	}
	
	public void safeIntValue (TsEntity entity, String descriptor, int value) {
		AuxiliaryGateway glueGW = new AuxiliaryGateway();
		int glueId = glueGW.getGlueId(entity);
		int typeId = glueGW.getTypeId(value);
		try {
			preparedInsertNumeric.setString(1, descriptor);
			preparedInsertNumeric.setInt(2, typeId);
			preparedInsertNumeric.setInt(3, glueId);
			preparedInsertNumeric.setInt(4,value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dbIO.executeStatement(preparedInsertNumeric);
	}
	
	public void updateIntValue (TsEntity entity, String descriptor, int value) {
		AuxiliaryGateway glueGW = new AuxiliaryGateway();
		int glueId = glueGW.getGlueId(entity);
		int typeId = glueGW.getTypeId(value);
		try {	
			preparedUpdateNumeric.setInt(1, typeId);
			preparedUpdateNumeric.setInt(2,value);
			preparedUpdateNumeric.setInt(3, glueId);
			preparedUpdateNumeric.setString(4, descriptor);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dbIO.executeStatement(preparedUpdateNumeric);
	}
	
	public void safeStringValue (TsEntity entity, String descriptor, String value) {
		AuxiliaryGateway glueGW = GatewayManager.getSharedInstance().getAuxGateway();
		int glueId = glueGW.getGlueId(entity);
		int typeId = glueGW.getTypeId(value);
		try {
			preparedInsertString.setString(1, descriptor);
			preparedInsertString.setInt(2, typeId);
			preparedInsertString.setInt(3, glueId);
			preparedInsertString.setString(4,value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dbIO.executeStatement(preparedInsertNumeric);
	}
	
	public void updateStringValue (TsEntity entity, String descriptor, String value) {
		AuxiliaryGateway glueGW = GatewayManager.getSharedInstance().getAuxGateway();
		int glueId = glueGW.getGlueId(entity);
		int typeId = glueGW.getTypeId(value);
		try {
			preparedUpdateString.setInt(1, typeId);
			preparedUpdateString.setString(2,value);
			preparedUpdateString.setInt(3, glueId);
			preparedUpdateString.setString(4, descriptor);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		dbIO.executeStatement(preparedUpdateString);
	}
}
