package system.persistence.gateway;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import system.persistence.DBIO;
import tsxdk.entity.TsEntity;
import tsxdk.entity.LibTsEvent;
import utility.misc.BatchSetter;

/**
 *	Offers Insert,update,delete methods for the TsEvent Entity
 *	OMITTED - Not sure how to safe events ?!
 * 	Event-System has to be cleansed and redone before implementing this.
 */

public final class TsEventGateway {
	private DBIO dbIO;
	private PreparedStatement preparedInsert;
	private PreparedStatement preparedDelete;
//	private PreparedStatement preparedSelect;
//	private PreparedStatement preparedGetAll;
		
	public TsEventGateway() {
		dbIO = DBIO.getSharedInstance();
		prepareStatements();
	}

	private void prepareStatements() {
			preparedInsert = dbIO.prepareStatement("INSERT_TSEVENT",
					"INSERT INTO TsEvent " +
					"(Descriptor,Timestamp,clid,cfid,ctid,reasonid,client_outputonly_muted," +
					"client_talk_request,client_talk_request_msg,reasonmsg,targetmode,target,invokerid," +
					"invokername,invokeruid,msg)" +
					"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			
			preparedDelete = dbIO.prepareStatement("DELETE_TSEVENT",
					"DELETE FROM TsEvent " +
					"WHERE Descriptor = ? AND Timestamp = ?");
			
//			preparedSelect = dbIO.prepareStatement("SELECT_COUNT_OF_TSEVENT",
//					"SELECT COUNT(*) FROM TsEvent");
//			
//			preparedGetAll = dbIO.prepareStatement("SELECT_ALL_TSEVENT",
//					"SELECT * FROM TsEvent");
	}

	public int insertEntity(TsEntity event) {
		if (! (event instanceof LibTsEvent))
			throw new IllegalArgumentException();
		
		LibTsEvent entity = (LibTsEvent) event;
		
		try {
			BatchSetter setter = new BatchSetter(preparedInsert);
			entity.prepareForInsert(setter);
		
			dbIO.executeStatement(preparedInsert);
			
			int databaseId = 0;
			{//Retrieve the Database ID
				ResultSet genKey = preparedInsert.getGeneratedKeys();
				genKey.next();
				databaseId = genKey.getInt(1);
			}
			entity.setTSXDBID(databaseId);
			return databaseId;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void removeEntity(TsEntity entity) {
		if (! (entity instanceof LibTsEvent))
			throw new IllegalArgumentException();
		
		LibTsEvent event = (LibTsEvent) entity;
		removeEntity(event.getEventDescriptor(), event.getTimeStamp());
	}

	private void removeEntity(String eventDescriptor, long timeStamp) {
		try {
			preparedDelete.setString(1, eventDescriptor);
			preparedDelete.setLong(2, timeStamp);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbIO.executeStatement(preparedDelete);	
	}
}
