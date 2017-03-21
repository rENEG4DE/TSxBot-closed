package system.persistence;


/**
 * Takes care about creating and validating the underlying Database
 *
 */
public class DBBuilder {
	private static DBIO dbConn;
	static {
		dbConn = DBIO.getSharedInstance();
	}
	
	public DBBuilder() {} 
	
	public void buildDB() {
		cleanup();
		initializeEntityTables();
		initializeMetaTables();
		initializeOptDataTable();
		initializeTriggers();
	}

	private static void cleanup() {
		dbConn.executeStatement("DROP TABLE IF EXISTS TsxOptData;");
		dbConn.executeStatement("DROP TABLE IF EXISTS EntityGlue;"); 
		dbConn.executeStatement("DROP TABLE IF EXISTS TypeMeta;");
		dbConn.executeStatement("DROP TABLE IF EXISTS EntityMeta;");
		dbConn.executeStatement("DROP TABLE IF EXISTS EventMeta;");
		dbConn.executeStatement("DROP TABLE IF EXISTS TsComplain;");
		dbConn.executeStatement("DROP TABLE IF EXISTS TsEvent;");
		dbConn.executeStatement("DROP TABLE IF EXISTS TsChannel;");
		dbConn.executeStatement("DROP TABLE IF EXISTS TsClient");
	}

	private static void initializeOptDataTable() {
		dbConn.executeStatement("CREATE TABLE TsxOptData "+
				"(" +
				"Descriptor VARCHAR(256)," +
				"TypeId INTEGER NOT NULL," + 
				"EntityGlueId INTEGER NOT NULL," +
				"NumericData NUMERIC," +
				"StringData VARCHAR(256)," +
				"CONSTRAINT pk_OptData PRIMARY KEY(EntityGlueId,Descriptor)" +
				")" +
				";");
	}

	private static void initializeMetaTables() {
		initializeEntityMetaTable();
		initializeTypeMetaTable();
		initializeEntityGlueTable();
		initializeEventMetaTable();
	}

	private static void initializeTriggers() {
		//TsEvent
		//"FOREIGN KEY(EntityId) REFERENCES EntityGlue(Id) NOT NULL"
		//Trying to Insert to TsEvent with an unknown EntityId causes it to abort
//		dbConn.executeStatement("CREATE TRIGGER itrigger_EVENT_ENTITY_ID " 
//				+ "BEFORE INSERT ON TsEvent "
//				+ "BEGIN "
//				+ "SELECT CASE "
//				+ "WHEN ((new.EntityId IS NOT NULL) "
//				+ "AND ((SELECT Id FROM EntityGlue WHERE Id = new.EntityId) IS NULL))"
//				+ "THEN RAISE(ABORT, \"No EntityGlue with this ID was found while inserting to TsEvent || constraint trigger_EVENT_ENTITY_ID\")"
//				+ "END;" + "END;");
		//Trying to Update to TsEvent with an unknown EntityId causes it to abort
//		dbConn.executeStatement("CREATE TRIGGER utrigger_EVENT_ENTITY_ID " 
//				+ "BEFORE UPDATE ON TsEvent "
//				+ "BEGIN "
//				+ "SELECT CASE "
//				+ "WHEN ((SELECT Id FROM EntityGlue WHERE Id = new.EntityId) IS NULL)"
//				+ "THEN RAISE(ABORT, \"No EntityGlue with this ID was found while updating to TsEvent || constraint trigger_EVENT_ENTITY_ID\")"
//				+ "END;" + "END;");
		
		//EntityGlue:
		//These triggers have been deprecated, for what is checked by them is guaranteed to be correct. 
		//"FOREIGN KEY(MetaId) REFERENCES EntityMeta(Id) NOT NULL" +
		//The given MetaID has to be present
//		dbConn.executeStatement("CREATE TRIGGER itrigger_ENTITY_META_ID " 
//				+ "BEFORE INSERT ON EntityGlue "
//				+ "BEGIN "
//				+ "SELECT CASE "
//				+ "WHEN ((new.MetaId IS NOT NULL) "
//				+ "AND ((SELECT Id FROM EntityMeta WHERE Id = new.MetaId) IS NULL))"
//				+ "THEN RAISE(ABORT, \"Unknown EntityMetaId given while inserting to EntityGlue || constraint itrigger_ENTITY_META_ID\")"
//				+ "END;" + "END;");
		//Trying to Update to an unknown MetaId
//		dbConn.executeStatement("CREATE TRIGGER utrigger_ENTITY_META_ID " 
//				+ "BEFORE UPDATE ON EntityGlue "
//				+ "BEGIN "
//				+ "SELECT CASE "
//				+ "WHEN ((SELECT Id FROM EntityMeta WHERE Id = new.MetaId) IS NULL)"
//				+ "THEN RAISE(ABORT, \"Unknown EntityMetaId given while updating to EntityGlue || constraint utrigger_ENTITY_META_ID\")"
//				+ "END;" + "END;");
		
		//"FOREIGN KEY(TypeId) REFERENCES TypeMeta(Id) NOT NULL," +
		//The given TypeID has to be present
		dbConn.executeStatement("CREATE TRIGGER itrigger_OPT_TYPE_ID " 
				+ "BEFORE INSERT ON TsxOptData "
				+ "BEGIN "
				+ "SELECT CASE "
				+ "WHEN ((new.TypeId IS NOT NULL) "
				+ "AND ((SELECT Id FROM TypeMeta WHERE Id = new.TypeId) IS NULL))"
				+ "THEN RAISE(ABORT, \"Unknown TypeId given while inserting to TsxOptData || constraint itrigger_OPT_TYPE_ID\")"
				+ "END;" + "END;");
		//Trying to Update to an unknown TypeId
		dbConn.executeStatement("CREATE TRIGGER utrigger_OPT_TYPE_ID " 
				+ "BEFORE UPDATE ON TsxOptData "
				+ "BEGIN "
				+ "SELECT CASE "
				+ "WHEN ((SELECT Id FROM TypeMeta WHERE Id = new.TypeId) IS NULL)"
				+ "THEN RAISE(ABORT, \"Unknown TypeId given while updating to TsxOptData || constraint utrigger_OPT_TYPE_ID\")"
				+ "END;" + "END;");
		
		//"FOREIGN KEY(EntityId) REFERENCES EntityGlue(Id) NOT NULL," +
		//The given EntityID has to be present inside EntityGlue
		dbConn.executeStatement("CREATE TRIGGER itrigger_OPT_ENTITY_ID " 
				+ "BEFORE INSERT ON TsxOptData "
				+ "BEGIN "
				+ "SELECT CASE "
				+ "WHEN ((new.EntityGlueId IS NOT NULL) "
				+ "AND ((SELECT Id FROM EntityGlue WHERE Id = new.EntityGlueId) IS NULL))"
				+ "THEN RAISE(ABORT, \"Unknown EntityId given while inserting to TsxOptData || constraint itrigger_OPT_ENTITY_ID\")"
				+ "END;" + "END;");
		//Trying to Update to an unknown TypeId
		dbConn.executeStatement("CREATE TRIGGER utrigger_OPT_ENTITY_ID " 
				+ "BEFORE UPDATE ON TsxOptData "
				+ "BEGIN "
				+ "SELECT CASE "
				+ "WHEN ((SELECT Id FROM EntityGlue WHERE Id = new.EntityGlueId) IS NULL)"
				+ "THEN RAISE(ABORT, \"Unknown EntityId given while updating to TsxOptData || constraint utrigger_OPT_ENTITY_ID\")"
				+ "END;" + "END;");
		
		//TsEntities
		//Automagically generate glue-records after insert
		//TsEvent
		dbConn.executeStatement("CREATE TRIGGER itrigger_TSEVENT_CREATE_GLUE " 
				+ "AFTER INSERT ON TsEvent BEGIN "
				+ "INSERT INTO EntityGlue (EntityId, MetaId) VALUES (new.Id, (SELECT Id FROM EntityMeta WHERE Identifier LIKE \"TsEvent\")); "
				+ "END;");
		//TsComplain
		dbConn.executeStatement("CREATE TRIGGER itrigger_TSCOMPLAIN_CREATE_GLUE " 
				+ "AFTER INSERT ON TsComplain BEGIN "
				+ "INSERT INTO EntityGlue (EntityId, MetaId) VALUES (new.Id, (SELECT Id FROM EntityMeta WHERE Identifier LIKE \"TsComplain\")); "
				+ "END;");
		//TsChannel
		dbConn.executeStatement("CREATE TRIGGER itrigger_TSCHANNEL_CREATE_GLUE " 
				+ "AFTER INSERT ON TsChannel BEGIN "
				+ "INSERT INTO EntityGlue (EntityId, MetaId) VALUES (new.Id, (SELECT Id FROM EntityMeta WHERE Identifier LIKE \"TsChannel\")); "
				+ "END;");
		//TsEvent
		dbConn.executeStatement("CREATE TRIGGER itrigger_TSCLIENT_CREATE_GLUE " 
				+ "AFTER INSERT ON TsClient BEGIN "
				+ "INSERT INTO EntityGlue (EntityId, MetaId) VALUES (new.Id, (SELECT Id FROM EntityMeta WHERE Identifier LIKE \"TsClient\")); "
				+ "END;");
		
		//Referential integrity-triggers
		//Delete Opt-data and EntityGlue on deleting an Entity
		dbConn.executeStatement("CREATE TRIGGER dtrigger_DELETE_TSCLIENT_ASSOCIATED " 
				+ "AFTER DELETE ON TsClient BEGIN "
				+ "DELETE FROM TsxOptData WHERE EntityGlueId = (SELECT Id FROM EntityGlue WHERE EntityId = Old.Id AND MetaId = (SELECT Id FROM EntityMeta WHERE Identifier LIKE \"TsClient\")); "
				+ "DELETE FROM EntityGlue WHERE EntityId = Old.Id AND MetaId = (SELECT Id FROM EntityMeta WHERE Identifier LIKE \"TsClient\"); "
				+ "END;");
		
		dbConn.executeStatement("CREATE TRIGGER dtrigger_DELETE_TSCHANNEL_ASSOCIATED " 
				+ "AFTER DELETE ON TsChannel BEGIN "
				+ "DELETE FROM TsxOptData WHERE EntityGlueId = (SELECT Id FROM EntityGlue WHERE EntityId = Old.Id AND MetaId = (SELECT Id FROM EntityMeta WHERE Identifier LIKE \"TsChannel\")); "
				+ "DELETE FROM EntityGlue WHERE EntityId = Old.Id AND MetaId = (SELECT Id FROM EntityMeta WHERE Identifier LIKE \"TsChannel\"); "
				+ "END;");
		
		dbConn.executeStatement("CREATE TRIGGER dtrigger_DELETE_TSEVENT_ASSOCIATED " 
				+ "AFTER DELETE ON TsEvent BEGIN "
				+ "DELETE FROM TsxOptData WHERE EntityGlueId = (SELECT Id FROM EntityGlue WHERE EntityId = Old.Id AND MetaId = (SELECT Id FROM EntityMeta WHERE Identifier LIKE \"TsEvent\")); "
				+ "DELETE FROM EntityGlue WHERE EntityId = Old.Id AND MetaId = (SELECT Id FROM EntityMeta WHERE Identifier LIKE \"TsEvent\"); "
				+ "END;");
		
		dbConn.executeStatement("CREATE TRIGGER dtrigger_DELETE_TSCOMPLAIN_ASSOCIATED " 
				+ "AFTER DELETE ON TsComplain BEGIN "
				+ "DELETE FROM TsxOptData WHERE EntityGlueId = (SELECT Id FROM EntityGlue WHERE EntityId = Old.Id AND MetaId = (SELECT Id FROM EntityMeta WHERE Identifier LIKE \"TsComplain\")); "
				+ "DELETE FROM EntityGlue WHERE EntityId = Old.Id AND MetaId = (SELECT Id FROM EntityMeta WHERE Identifier LIKE \"TsComplain\"); "
				+ "END;");
		
		//Event-ID assignment-trigger
		dbConn.executeStatement("CREATE TRIGGER itrigger_EVENT_ID " 
				+ "AFTER INSERT ON TsEvent BEGIN "
				+ "UPDATE TsEvent "
				+ "SET EventId = (SELECT Id FROM EventMeta WHERE Identifier = new.Descriptor) "
				+ "WHERE Id = new.Id; "
				+ "END;" );
	}

	private static void initializeEntityGlueTable() {
		dbConn.executeStatement("CREATE TABLE EntityGlue "+
				"(" +
				"Id INTEGER PRIMARY KEY," +
				"EntityId INTEGER NOT NULL," +	
				"MetaId INTEGER NOT NULL" +	
				")" +
				";" +
				"CREATE INDEX GlueIdx1 ON EntityGlue(EntityId,MetaId)");
	}

	private static void initializeTypeMetaTable() {
		dbConn.executeStatement("CREATE TABLE TypeMeta " +
				"(" +
				"Id INTEGER PRIMARY KEY," +
				"Identifier VARCHAR(7) NOT NULL" +
				")" +
				";");
		
		dbConn.executeStatement("INSERT INTO TypeMeta (Identifier) VALUES (\"String\");\n");
		dbConn.executeStatement("INSERT INTO TypeMeta (Identifier) VALUES (\"Long\");\n" );
		dbConn.executeStatement("INSERT INTO TypeMeta (Identifier) VALUES (\"Double\");\n");
		dbConn.executeStatement("INSERT INTO TypeMeta (Identifier) VALUES (\"Integer\");\n"); 
		dbConn.executeStatement("INSERT INTO TypeMeta (Identifier) VALUES (\"Boolean\");\n");
	}

	private static void initializeEntityMetaTable() {
		dbConn.executeStatement("CREATE TABLE EntityMeta " +
				"(" +
				"Id INTEGER PRIMARY KEY NOT NULL," +
				"Identifier VARCHAR(10) NOT NULL" +
				")" +
				";");
		
		dbConn.executeStatement("INSERT INTO EntityMeta (Identifier) VALUES (\"TsClient\");");
		dbConn.executeStatement("INSERT INTO EntityMeta (Identifier) VALUES (\"TsChannel\");"); 
		dbConn.executeStatement("INSERT INTO EntityMeta (Identifier) VALUES (\"TsComplain\");");
		dbConn.executeStatement("INSERT INTO EntityMeta (Identifier) VALUES (\"TsEvent\");");
	}
	
	private static void initializeEventMetaTable() {
		dbConn.executeStatement("CREATE TABLE EventMeta " +
				"(" +
				"Id INTEGER PRIMARY KEY," +
				"Identifier VARCHAR(48) NOT NULL" +
				")" +
				";");
		
		dbConn.executeStatement("INSERT INTO EventMeta (Identifier) VALUES (\"CLIENTJOINED\");");
		dbConn.executeStatement("INSERT INTO EventMeta (Identifier) VALUES (\"CLIENTLEFT\");"); 
		dbConn.executeStatement("INSERT INTO EventMeta (Identifier) VALUES (\"COMPLAINCOMMITTED\");");
		dbConn.executeStatement("INSERT INTO EventMeta (Identifier) VALUES (\"COMPLAINEXPIRED\");");
		dbConn.executeStatement("INSERT INTO EventMeta (Identifier) VALUES (\"TEXTMESSAGE\");");
		dbConn.executeStatement("INSERT INTO EventMeta (Identifier) VALUES (\"BRAINBEAT\");");
	}

	private static void initializeEntityTables() {
		initializeClientTable();
		initializeChannelTable();
		initializeComplainTable();
		initializeEventTable();
	}

	private static void initializeEventTable() {
		dbConn.executeStatement("CREATE TABLE TsEvent " +
				"(" +
				"Id INTEGER PRIMARY KEY," +
				"Descriptor VARCHAR(48) NOT NULL," +
				"Timestamp LONG NOT NULL," +
				"EventId INTEGER DEFAULT NULL," + 
				"clid INTEGER DEFAULT NULL," +
				"cfid INTEGER DEFAULT NULL," + 
				"ctid INTEGER DEFAULT NULL," + 
				"reasonid INTEGER DEFAULT NULL," + 
				"client_outputonly_muted INTEGER DEFAULT NULL," +
				"client_talk_request INTEGER DEFAULT NULL," +
				"client_talk_request_msg TEXT DEFAULT NULL," +
				"reasonmsg TEXT DEFAULT NULL," +
				"targetmode INTEGER DEFAULT NULL," +
				"target INTEGER DEFAULT NULL," +
				"invokerid INTEGER DEFAULT NULL," +
				"invokername TEXT DEFAULT NULL," +
				"invokeruid  TEXT DEFAULT NULL," +
				"msg TEXT DEFAULT NULL" +
				")" +
				";");
	}

	private static void initializeComplainTable() {
		dbConn.executeStatement("CREATE TABLE TsComplain " +
				"(" +
				"Id INTEGER PRIMARY KEY," +
				"tcldbid INTEGER NOT NULL," +
				"tname TEXT NOT NULL," +
				"fcldbid INTEGER NOT NULL," +
				"fname TEXT NOT NULL," + 
				"message TEXT NOT NULL," +
				"timestamp LONG NOT NULL" +
				")" +
				";");
	}

	private static void initializeChannelTable() {
		dbConn.executeStatement("CREATE TABLE TsChannel " +
				"(" +
				"Id INTEGER PRIMARY KEY," +
				"cid INTEGER NOT NULL," +
				"pid INTEGER NOT NULL," +
				"channel_order INTEGER NOT NULL," +
				"channel_name VARCHAR(254) NOT NULL," +
				"channel_topic VARCHAR(254) NOT NULL," +
				"channel_flag_default INTEGER NOT NULL," +
				"channel_flag_password INTEGER NOT NULL," +
				"channel_flag_permanent INTEGER NOT NULL," +
				"channel_flag_semi_permanent INTEGER NOT NULL," +
				"channel_codec INTEGER NOT NULL," +
				"channel_codec_quality INTEGER NOT NULL," +
				"channel_needed_talk_power INTEGER NOT NULL," +
				"total_clients_family INTEGER NOT NULL," +
				"channel_maxclients INTEGER NOT NULL," +
				"channel_maxfamilyclients INTEGER NOT NULL," +
				"total_clients INTEGER NOT NULL," +
				"channel_needed_subscribe_power INTEGER NOT NULL);"
				);
	}

	private static void initializeClientTable() {
		dbConn.executeStatement("CREATE TABLE TsClient " +
				"(" +
				"Id INTEGER PRIMARY KEY," +
				"clid INTEGER NOT NULL," +
				"cid INTEGER DEFAULT NULL," +
				"client_database_id INTEGER DEFAULT NULL," +
				"client_nickname TEXT DEFAULT NULL," +
	     		"client_input_muted INTEGER DEFAULT NULL," +
	     		"client_away INTEGER DEFAULT NULL," +
	     		"client_flag_talking INTEGER DEFAULT NULL," +
	     		"client_servergroups TEXT DEFAULT NULL," +
	     		"client_type INTEGER DEFAULT NULL," +
	     		"client_unique_identifier TEXT DEFAULT NULL," +
	     		"client_output_muted INTEGER DEFAULT NULL," +
	     		"client_is_priority_speaker INTEGER DEFAULT NULL," +
	     		"client_is_recording INTEGER DEFAULT NULL," +
	     		"client_is_channel_commander INTEGER DEFAULT NULL," +
	     		"client_output_hardware INTEGER DEFAULT NULL," +
	     		"client_talk_power INTEGER DEFAULT NULL," +
	     		"client_channel_group_id INTEGER DEFAULT NULL," +
	     		"client_channel_group_inherited_channel_id INTEGER DEFAULT NULL," +
	     		"client_created INTEGER DEFAULT NULL," +
	     		"client_lastconnected INTEGER DEFAULT NULL," +
	     		"client_is_talker INTEGER DEFAULT NULL," +
	     		"client_input_hardware INTEGER DEFAULT NULL," +
	     		"client_idle_time INTEGER DEFAULT NULL," +
	     		"client_country TEXT DEFAULT NULL," +
	     		"client_away_message TEXT DEFAULT NULL," +
	    //-- data once revived on join ------
	      		"client_meta_data TEXT DEFAULT NULL," +
	      		"client_flag_avatar TEXT DEFAULT NULL," +
	      		"client_description TEXT DEFAULT NULL," +
	      		"client_unread_messages INTEGER DEFAULT NULL," +
	      		"client_nickname_phonetic  TEXT DEFAULT NULL," +
	      		"client_needed_serverquery_view_power INTEGER DEFAULT NULL," +
	      		"client_icon_id INTEGER DEFAULT NULL," +
	    //-- data on request added -----
	      		"client_version TEXT DEFAULT NULL," +
	      		"client_platform TEXT DEFAULT NULL," +
	      		"client_default_channel TEXT DEFAULT NULL," +
	      		"client_totalconnections INTEGER DEFAULT NULL," +
	      		"connection_client_ip VARCHAR(19) DEFAULT NULL);");
//	     "client_month_bytes_uploaded LONG NOT NULL," +
//	     "client_month_bytes_downloaded LONG NOT NULL," +
//	     "client_total_bytes_uploaded LONG NOT NULL," +
//	     "client_total_bytes_downloaded LONG NOT NULL," +
//	     "connection_filetransfer_bandwidth_sent LONG NOT NULL," +
//	     "connection_filetransfer_bandwidth_received LONG NOT NULL," +
//	     "connection_packets_sent_total LONG NOT NULL," +
//	     "connection_bytes_sent_total LONG NOT NULL," +
//	     "connection_packets_received_total LONG NOT NULL," +
//	     "connection_bytes_received_total LONG NOT NULL," +
//	     "connection_bandwidth_sent_last_second_total LONG NOT NULL," +
//	     "connection_bandwidth_sent_last_minute_total LONG NOT NULL," +
//	     "connection_bandwidth_received_last_second_total LONG NOT NULL," +
//	     "connection_bandwidth_received_last_minute_total LONG NOT NULL," +

	}
}
