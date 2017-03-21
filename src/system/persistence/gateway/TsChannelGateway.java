package system.persistence.gateway;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import system.persistence.DBIO;
import tsxdk.entity.TsChannel;
import tsxdk.entity.TsEntity;
import utility.misc.BatchSetter;

public final class TsChannelGateway implements AbstractEntityGateway {
	private final DBIO dbIO;
	private PreparedStatement preparedInsert;
	private PreparedStatement preparedUpdate;
	private PreparedStatement preparedDelete;
	private PreparedStatement preparedSelect;
	private PreparedStatement preparedGetAll;

	TsChannelGateway() {
		dbIO = DBIO.getSharedInstance();
		prepareStatements();
	}
	
	private void prepareStatements() {
		preparedInsert = dbIO.prepareStatement("INSERT_TSCHANNEL",
				"INSERT INTO TsChannel"
						+ "(cid,pid,channel_order,channel_name," 
						+ "channel_topic,channel_flag_default,channel_flag_password,channel_flag_permanent,"
						+ "channel_flag_semi_permanent,channel_codec,channel_codec_quality,channel_needed_talk_power," 
						+ "total_clients_family,channel_maxclients,channel_maxfamilyclients,total_clients,channel_needed_subscribe_power)"
						+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		preparedUpdate = dbIO.prepareStatement("UPDATE_TSCHANNEL",
				"UPDATE TsChannel SET " 
						+ "cid= ?, pid = ?, channel_order = ?, channel_name = ?, " 
						+ "channel_topic = ?, channel_flag_default = ?, channel_flag_password = ?, channel_flag_permanent = ?, "
						+ "channel_flag_semi_permanent = ?, channel_codec = ?, channel_codec_quality = ?, channel_needed_talk_power = ?, " 
						+ "total_clients_family = ?, channel_maxclients = ?, channel_maxfamilyclients = ?, total_clients = ?, channel_needed_subscribe_power = ? " + "WHERE Id = ?");

		preparedDelete = dbIO.prepareStatement("REMOVE_TSCHANNEL", 
				"DELETE FROM TsChannel WHERE Id = ?");
		
		preparedSelect = dbIO.prepareStatement("SELECT_SINGLE_TSCHANNEL",
				"SELECT * FROM TsChannel WHERE Id = ?");

		preparedGetAll = dbIO.prepareStatement("SELECT_ALL_TSCHANNEL",
				"SELECT * FROM TsChannel");
	}

	@Override
	public int insertEntity(TsEntity channel) {
		if (! (channel instanceof TsChannel))
			throw new IllegalArgumentException();
		
		TsChannel entity = (TsChannel) channel;
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
			} catch (final SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		entity.setTSXDBID(databaseId);
		entity.setGlueID(GatewayManager.getSharedInstance().getAuxGateway().getGlueId(entity));
		return databaseId;

	}

	@Override
	public void updateEntity(TsEntity channel) {
		if (! (channel instanceof TsChannel))
			throw new IllegalArgumentException();
		
		TsChannel entity = (TsChannel) channel;
		BatchSetter setter = new BatchSetter(preparedUpdate);
		
		entity.prepareForUpdate(setter);

		dbIO.executeStatement(preparedUpdate);

	}

	@Override
	public TsEntity selectEntity(int id) {
		TsChannel entity = null;
		
		try {
			preparedSelect.setInt(1, id);
			ResultSet rs = dbIO.executeQuery(preparedSelect);	
			{//retrieve data
				rs.next();
				entity = new TsChannel();
				entity.setTSXDBID(rs.getInt(1));
				entity.setCid(rs.getInt(2));
				entity.setPid(rs.getInt(3));
				entity.setChannel_order(rs.getInt(4));
				entity.setChannel_name(rs.getString(5));
				entity.setChannel_topic(rs.getString(6));
				entity.setChannel_flag_default(rs.getInt(7));
				entity.setChannel_flag_password(rs.getInt(8));
				entity.setChannel_flag_permanent(rs.getInt(9));
				entity.setChannel_codec(rs.getInt(10));
				entity.setChannel_codec_quality(rs.getInt(11));
				entity.setChannel_needed_talk_power(rs.getInt(12));
				entity.setTotal_clients_family(rs.getInt(13));
				entity.setChannel_maxclients(rs.getInt(14));
				entity.setChannel_maxfamilyclients(rs.getInt(15));
				entity.setTotal_clients(rs.getInt(16));
				entity.setChannel_needed_subscribe_power(rs.getInt(17));
			}
			
		} catch (final SQLException e) {
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
				TsChannel entity = new TsChannel();

				entity.setTSXDBID(rs.getInt(1));
				entity.setCid(rs.getInt(2));
				entity.setPid(rs.getInt(3));
				entity.setChannel_order(rs.getInt(4));
				entity.setChannel_name(rs.getString(5));
				entity.setChannel_topic(rs.getString(6));
				entity.setChannel_flag_default(rs.getInt(7));
				entity.setChannel_flag_password(rs.getInt(8));
				entity.setChannel_flag_permanent(rs.getInt(9));
				entity.setChannel_codec(rs.getInt(10));
				entity.setChannel_codec_quality(rs.getInt(11));
				entity.setChannel_needed_talk_power(rs.getInt(12));
				entity.setTotal_clients_family(rs.getInt(13));
				entity.setChannel_maxclients(rs.getInt(14));
				entity.setChannel_maxfamilyclients(rs.getInt(15));
				entity.setTotal_clients(rs.getInt(16));
				entity.setChannel_needed_subscribe_power(rs.getInt(17));
				
				coll.add(entity);
			}
		} catch (final SQLException e) {
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
		} catch (final SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbIO.executeStatement(preparedDelete);
	}

}
