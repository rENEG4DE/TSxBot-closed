package system.persistence.gateway;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import system.persistence.DBIO;
import tsxdk.entity.TsComplain;
import tsxdk.entity.TsEntity;

/**
 *	Offers Insert,update,delete methods for the TsComplain Entity
 */
public final class TsComplainGateway implements AbstractEntityGateway {
	private DBIO dbIO;
	private PreparedStatement preparedInsert;
	private PreparedStatement preparedUpdate;
	private PreparedStatement preparedDelete;
	private PreparedStatement preparedSelect;
	private PreparedStatement preparedGetAll;
		
	TsComplainGateway() {
		dbIO = DBIO.getSharedInstance();
		prepareStatements();
	}
	
	private void prepareStatements() {
			preparedInsert = dbIO.prepareStatement("INSERT_TSCOMPLAIN",
					"INSERT INTO TsComplain" + 
					"(tcldbid,tname,fcldbid,fname,message,timestamp)"
					+"VALUES(?,?,?,?,?,?)");
			
			preparedUpdate = dbIO.prepareStatement("UPDATE_TSCOMPLAIN",
					"UPDATE TsComplain SET "
					+ "tcldbid = ?," 
					+ "tname = ?," 
					+ "fcldbid = ?," 
					+ "fname = ?,"
					+ "message = ?,"
					+ "timestamp = ? "
					+ "WHERE "
					+ "Id = ?");
			
			preparedDelete = dbIO.prepareStatement("REMOVE_TSCOMPLAIN",
					"DELETE FROM TsComplain WHERE Id = ?");
			
			preparedSelect = dbIO.prepareStatement("SELECT_SINGLE_TSCOMPLAIN",
					"SELECT * FROM TsComplain WHERE Id = ?");
			
			preparedGetAll = dbIO.prepareStatement("SELECT_ALL_TSCOMPLAIN",
					"SELECT * FROM TsComplain");
	}

	@Override
	public int insertEntity(TsEntity complain) {
		if (! (complain instanceof TsComplain))
			throw new IllegalArgumentException();
		
		TsComplain entity = (TsComplain) complain;
		
		try {
			preparedInsert.setInt(1, entity.getTcldbid());	
			preparedInsert.setString(2, entity.getTname());
			preparedInsert.setInt(3, entity.getFcldbid());
			preparedInsert.setString(4, entity.getFname());
			preparedInsert.setString(5, entity.getMessage());
			preparedInsert.setLong(6, entity.getTimestamp());
			
			dbIO.executeStatement(preparedInsert);
			
			int databaseId = 0;
			{//Retrieve the Database ID
				ResultSet genKey = preparedInsert.getGeneratedKeys();
				genKey.next();
				databaseId = genKey.getInt(1);
			}
			entity.setGlueID(GatewayManager.getSharedInstance().getAuxGateway().getGlueId(entity));
			entity.setTSXDBID(databaseId);
			return databaseId;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public void updateEntity(TsEntity complain) {
		if (! (complain instanceof TsComplain))
			throw new IllegalArgumentException();
		
		TsComplain entity = (TsComplain) complain;
		
		try {
			preparedUpdate.setInt(1, entity.getTcldbid());	
			preparedUpdate.setString(2, entity.getTname());
			preparedUpdate.setInt(3, entity.getFcldbid());
			preparedUpdate.setString(4, entity.getFname());
			preparedUpdate.setString(5, entity.getMessage());
			preparedUpdate.setLong(6, entity.getTimestamp());
			preparedUpdate.setInt(7, entity.getTSXDBID());
			
			dbIO.executeStatement(preparedUpdate);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public TsEntity selectEntity(int id) {
		TsComplain entity = null;
		
		try {
			preparedSelect.setInt(1, id);
			ResultSet rs = dbIO.executeQuery(preparedSelect);	
			{//retrieve data
				rs.next();
				entity = new TsComplain();
				entity.setTSXDBID(rs.getInt(1));
				entity.setTcldbid(rs.getInt(2));
				entity.setTname(rs.getString(3));
				entity.setFcldbid(rs.getInt(4));
				entity.setFname(rs.getString(5));
				entity.setMessage(rs.getString(6));
				entity.setTimestamp(rs.getLong(7));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return entity;
	}
	
	@Override
	public TsEntity[] getAll() {

		ResultSet rs = dbIO.executeQuery(preparedGetAll);
		
		Collection<TsEntity> coll = new ArrayList<>();
		
		try {
			while (rs.next()) {
				TsComplain entity = new TsComplain();
				entity.setTSXDBID(rs.getInt(1));
				entity.setTcldbid(rs.getInt(2));
				entity.setTname(rs.getString(3));
				entity.setFcldbid(rs.getInt(4));
				entity.setFname(rs.getString(5));
				entity.setMessage(rs.getString(6));
				entity.setTimestamp(rs.getLong(7));

				coll.add(entity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return coll.toArray(new TsEntity[coll.size()]);
	}
	
	@Override
	public void removeEntity(TsEntity entity) {
		removeEntity(entity.getTSXDBID());
	}
	
	@Override
	public void removeEntity(int entityId) {
		try {
			preparedDelete.setInt(1, entityId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbIO.executeStatement(preparedDelete);
	}
}
