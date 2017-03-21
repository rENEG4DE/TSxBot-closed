package system.persistence.gateway;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import system.persistence.DBIO;
import tsxdk.entity.TsClient;
import tsxdk.entity.TsEntity;
import utility.misc.BatchSetter;
import utility.misc.StringMan;

public final class TsClientGateway implements AbstractEntityGateway {
	private DBIO dbIO;
	private PreparedStatement preparedInsert;
	private PreparedStatement preparedUpdate;
	private PreparedStatement preparedDelete;
	private PreparedStatement preparedSelect;
	private PreparedStatement preparedGetAll;

	TsClientGateway() {
		dbIO = DBIO.getSharedInstance();
		prepareStatements();
	}
	
	private void prepareStatements() {
		preparedInsert = dbIO.prepareStatement("INSERT_TSCLIENT" , 
				"INSERT INTO TsClient"
						+ "(clid,cid ,client_database_id,client_nickname ,client_input_muted ,client_away ,client_flag_talking,"
						+ "client_servergroups,client_type,client_unique_identifier,client_output_muted ,client_is_priority_speaker,"
						+ "client_is_recording ,client_is_channel_commander ,client_output_hardware ,client_talk_power ,"
						+ "client_channel_group_id ,client_channel_group_inherited_channel_id ,client_created ,client_lastconnected ,"
						+ "client_is_talker ,client_input_hardware ,client_idle_time ,client_country,client_away_message, client_meta_data ,"
						+ "client_flag_avatar ,client_description ,client_unread_messages ,client_nickname_phonetic  ,client_needed_serverquery_view_power ,client_icon_id ,client_version ,"
						+ "client_platform ,client_default_channel ,client_totalconnections ,connection_client_ip)"
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		preparedUpdate = dbIO.prepareStatement("UPDATE_TSCLIENT",
				"UPDATE TsClient SET " 
						+ "clid = ?, cid = ?, client_database_id = ?, client_nickname = ?, client_input_muted = ?, client_away = ?, client_flag_talking = ?, "
						+ "client_servergroups = ?, client_type = ?, client_unique_identifier = ?, client_output_muted = ?, client_is_priority_speaker = ?, "
						+ "client_is_recording = ?, client_is_channel_commander = ?, client_output_hardware = ?, client_talk_power = ?, "
						+ "client_channel_group_id = ?, client_channel_group_inherited_channel_id = ?, client_created = ?, client_lastconnected = ?, "
						+ "client_is_talker = ?, client_input_hardware = ?, client_idle_time = ?, client_country = ?, client_away_message = ?,  client_meta_data = ?, "
						+ "client_flag_avatar = ?, client_description = ?, client_unread_messages = ?, client_nickname_phonetic = ?, client_needed_serverquery_view_power = ?, client_icon_id = ?, client_version = ?, "
						+ "client_platform = ?, client_default_channel = ?, client_totalconnections = ?, connection_client_ip = ?"
						+ "WHERE Id = ?");

		preparedDelete = dbIO.prepareStatement("REMOVE_TSCLIENT", 
				"DELETE FROM TsClient WHERE Id = ?");
		
		preparedSelect = dbIO.prepareStatement("SELECT_SINGLE_TSCLIENT",
				"SELECT * FROM TsClient WHERE Id = ?");

		preparedGetAll = dbIO.prepareStatement("SELECT_ALL_TSCLIENT",
				"SELECT * FROM TsClient");
	}

	@Override
	public int insertEntity(TsEntity client) {
		if (! (client instanceof TsClient))
			throw new IllegalArgumentException();
		
		TsClient entity = (TsClient) client;
		BatchSetter setter = new BatchSetter(preparedInsert);
		
		entity.prepareForInsert(setter);
			
		dbIO.executeStatement(preparedInsert);

		int databaseId = 0;
		{// Retrieve the Database and Glue ID
			ResultSet genKey;
			try {
				genKey = preparedInsert.getGeneratedKeys();
				genKey.next();
				databaseId = genKey.getInt(1);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		entity.setTSXDBID(databaseId);
		entity.setGlueID(GatewayManager.getSharedInstance().getAuxGateway().getGlueId(entity));
		return databaseId;
	}

	@Override
	public void updateEntity(TsEntity client) {
		if (! (client instanceof TsClient))
			throw new IllegalArgumentException();
		
		TsClient entity = (TsClient) client;
		BatchSetter setter = new BatchSetter(preparedUpdate);
		
		entity.prepareForUpdate(setter);

		dbIO.executeStatement(preparedUpdate);
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

	@Override
	public TsEntity selectEntity(int entityId) {
		TsClient entity = null;
		
		try {
			preparedSelect.setInt(1, entityId);
			ResultSet rs = dbIO.executeQuery(preparedSelect);	
			{//retrieve data
				rs.next();
				entity = new TsClient();

				entity.setTSXDBID(rs.getInt(1));
				entity.setClid(rs.getInt(2));
				entity.setCid(rs.getInt(3));
				entity.setClient_Database_id(rs.getInt(4));
				entity.setClient_Nickname(rs.getString(5));
				entity.setClient_input_muted(rs.getInt(6));
				entity.setClient_Away(rs.getInt(7));
				entity.setTalking(rs.getInt(8));
				entity.setServergroups(StringMan.getArrayFromString(rs.getString(9)));
				entity.setClient_type(rs.getInt(10));
				entity.setClient_unique_identifier(rs.getString(11));
				entity.setClient_output_muted(rs.getInt(12));
				entity.setClient_is_priority_speaker(rs.getInt(13));
				entity.setClient_is_recording(rs.getInt(14));
				entity.setClient_is_channel_commander(rs.getInt(15));
				entity.setClient_output_hardware(rs.getInt(16));
				entity.setClient_talk_power(rs.getInt(17));
				entity.setClient_channel_group_id(rs.getInt(18));
				entity.setClient_channel_group_inherited_channel_id (rs.getInt(19));
				entity.setClient_created (rs.getInt(20));
				entity.setClient_lastconnected(rs.getInt(21));
				entity.setClient_is_talker(rs.getInt(22));
				entity.setClient_input_hardware(rs.getInt(23));
				entity.setIdle_time(rs.getInt(24));
				entity.setClient_country(rs.getString(25));
				entity.setClient_Away_Message(rs.getString(26));
				entity.setClient_meta_data(rs.getString(27));
				entity.setClient_flag_avatar(rs.getString(28));
				entity.setClient_description(rs.getString(29));
				entity.setClient_unread_messages(rs.getInt(30));
				entity.setClient_nickname_phonetic (rs.getString(31));
				entity.setClient_needed_serverquery_view_power (rs.getInt(32));
				entity.setClient_icon_id (rs.getInt(33));
				entity.setClient_version(rs.getString(34));
				entity.setClient_platform (rs.getString(35));
				entity.setClient_default_channel (rs.getString(36));
				entity.setClient_totalconnections (rs.getInt(37));
				entity.setConnection_client_ip(rs.getString(38));

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
				TsClient entity = new TsClient();

				entity.setTSXDBID(rs.getInt(1));
				entity.setClid(rs.getInt(2));
				entity.setCid(rs.getInt(3));
				entity.setClient_Database_id(rs.getInt(4));
				entity.setClient_Nickname(rs.getString(5));
				entity.setClient_input_muted(rs.getInt(6));
				entity.setClient_Away(rs.getInt(7));
				entity.setTalking(rs.getInt(8));
				entity.setServergroups(StringMan.getArrayFromString(rs.getString(9)));
				entity.setClient_type(rs.getInt(10));
				entity.setClient_unique_identifier(rs.getString(11));
				entity.setClient_output_muted(rs.getInt(12));
				entity.setClient_is_priority_speaker(rs.getInt(13));
				entity.setClient_is_recording(rs.getInt(14));
				entity.setClient_is_channel_commander(rs.getInt(15));
				entity.setClient_output_hardware(rs.getInt(16));
				entity.setClient_talk_power(rs.getInt(17));
				entity.setClient_channel_group_id(rs.getInt(18));
				entity.setClient_channel_group_inherited_channel_id (rs.getInt(19));
				entity.setClient_created (rs.getInt(20));
				entity.setClient_lastconnected(rs.getInt(21));
				entity.setClient_is_talker(rs.getInt(22));
				entity.setClient_input_hardware(rs.getInt(23));
				entity.setIdle_time(rs.getInt(24));
				entity.setClient_country(rs.getString(25));
				entity.setClient_Away_Message(rs.getString(26));
				entity.setClient_meta_data(rs.getString(27));
				entity.setClient_flag_avatar(rs.getString(28));
				entity.setClient_description(rs.getString(29));
				entity.setClient_unread_messages(rs.getInt(30));
				entity.setClient_nickname_phonetic (rs.getString(31));
				entity.setClient_needed_serverquery_view_power (rs.getInt(32));
				entity.setClient_icon_id (rs.getInt(33));
				entity.setClient_version(rs.getString(34));
				entity.setClient_platform (rs.getString(35));
				entity.setClient_default_channel (rs.getString(36));
				entity.setClient_totalconnections (rs.getInt(37));
				entity.setConnection_client_ip(rs.getString(38));
				
				coll.add(entity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return coll.toArray(new TsEntity[coll.size()]);
	}

}