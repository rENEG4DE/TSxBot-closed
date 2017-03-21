package system.persistence.gateway;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import system.persistence.DBIO;
import tsxdk.entity.TsEntity;

public final class AuxiliaryGateway {
	private final DBIO dbIO;

	private PreparedStatement preparedSelectGlueId;
//	private PreparedStatement preparedRemoveGlueId;

	AuxiliaryGateway() {
		dbIO = DBIO.getSharedInstance();
		prepareStatements();
	}

	private void prepareStatements() {		
		preparedSelectGlueId = dbIO.prepareStatement("SELECT_GLUE_ID",
				"SELECT "
				+ "Id "
				+ "FROM EntityGlue "
				+ "WHERE MetaId = ? AND EntityId = ?;");
		
//		preparedRemoveGlueId = dbIO.prepareStatement("REMOVE_GLUE_ID", 
//						"DELETE "
//						+ "FROM "
//						+ "EntityGlue "
//						+ "WHERE "						
//						+ "EntityId = ? " 
//						+ "AND " 
//						+ "MetaId = ?");
	}
	
	public int getGlueId(TsEntity entity) {
		if (entity.getGlueID() != 0)
			return entity.getGlueID();
		
		int metaId = getMetaId(entity);
		int entityId = entity.getTSXDBID();
		
		try {
			preparedSelectGlueId.setInt(1, metaId);
			preparedSelectGlueId.setInt(2, entityId);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
	
		ResultSet rs = dbIO.executeQuery(preparedSelectGlueId);
		
		try {
			if (rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
// entity-glue-ids are removed automatically, no need to do manually
//	public void removeGlueId (TsEntity entity) {
//		try {
//			preparedRemoveGlueId.setInt(1, entity.getTSXDBID());
//			preparedRemoveGlueId.setInt(2, getMetaId(entity));
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		
//		dbIO.executeStatement(preparedRemoveGlueId);
//	}
	
	public int getMetaId(TsEntity entity) {
		EntityClass entityClass = EntityClass.valueOf(entity.getClass().getSimpleName());

		switch (entityClass) {
		case TsClient: {
			return 1;
		}
		case TsChannel: {
			return 2;
		}
		case TsComplain: {
			return 3;
		}
		case TsEvent: {
			return 4;
		}
		default: {
			return -1;
		}
		}
	}
	
	public String getMetaIdentifier(TsEntity entity) {
		return entity.getClass().getSimpleName();
	}
	
	public <T> int getTypeId(T obj) {
		TypeClass typeClass = TypeClass.valueOf(obj.getClass().getSimpleName());

		switch (typeClass) {
		case String: {
			return 1;
		}
		case Long: {
			return 2;
		}
		case Double: {
			return 3;
		}
		case Integer: {
			return 4;
		}
		case Boolean: {
			return 5;
		}
		default: {
			return -1;
		}
		}
	}
	
	public <T> boolean isTypeSupported(T obj) {
		try {
			@SuppressWarnings("unused")
			TypeClass typeClass = TypeClass.valueOf(obj.getClass().getSimpleName());
		} catch (final IllegalArgumentException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public <T> String getTypeIdentifier(T obj) {
		return obj.getClass().getSimpleName();
	}
}
