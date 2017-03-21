    //** this part is auto generated dont touch this
    
	// Revision: $Revision $
	// Last modified: $Date:2012-05-13 16:09:54 +0000 (So, 13 Mai 2012) $
	// Last modified by: $Author:KoRnHolio $


package tsxdk.entity;

import static tsxdk.parser.LibTsSym.CID;
import static tsxdk.parser.LibTsSym.CLID;
import static tsxdk.parser.LibTsSym.CLIENT_AWAY;
import static tsxdk.parser.LibTsSym.CLIENT_AWAY_MESSAGE;
import static tsxdk.parser.LibTsSym.CLIENT_CHANNEL_GROUP_ID;
import static tsxdk.parser.LibTsSym.CLIENT_CHANNEL_GROUP_INHERITED_CHANNEL_ID;
import static tsxdk.parser.LibTsSym.CLIENT_COUNTRY;
import static tsxdk.parser.LibTsSym.CLIENT_CREATED;
import static tsxdk.parser.LibTsSym.CLIENT_DATABASE_ID;
import static tsxdk.parser.LibTsSym.CLIENT_DEFAULT_CHANNEL;
import static tsxdk.parser.LibTsSym.CLIENT_DESCRIPTION;
import static tsxdk.parser.LibTsSym.CLIENT_FLAG_AVATAR;
import static tsxdk.parser.LibTsSym.CLIENT_FLAG_TALKING;
import static tsxdk.parser.LibTsSym.CLIENT_ICON_ID;
import static tsxdk.parser.LibTsSym.CLIENT_IDLE_TIME;
import static tsxdk.parser.LibTsSym.CLIENT_INPUT_HARDWARE;
import static tsxdk.parser.LibTsSym.CLIENT_INPUT_MUTED;
import static tsxdk.parser.LibTsSym.CLIENT_IS_CHANNEL_COMMANDER;
import static tsxdk.parser.LibTsSym.CLIENT_IS_PRIORITY_SPEAKER;
import static tsxdk.parser.LibTsSym.CLIENT_IS_RECORDING;
import static tsxdk.parser.LibTsSym.CLIENT_IS_TALKER;
import static tsxdk.parser.LibTsSym.CLIENT_LASTCONNECTED;
import static tsxdk.parser.LibTsSym.CLIENT_META_DATA;
import static tsxdk.parser.LibTsSym.CLIENT_MONTH_BYTES_DOWNLOADED;
import static tsxdk.parser.LibTsSym.CLIENT_MONTH_BYTES_UPLOADED;
import static tsxdk.parser.LibTsSym.CLIENT_NEEDED_SERVERQUERY_VIEW_POWER;
import static tsxdk.parser.LibTsSym.CLIENT_NICKNAME_PHONETIC;
import static tsxdk.parser.LibTsSym.CLIENT_OUTPUT_HARDWARE;
import static tsxdk.parser.LibTsSym.CLIENT_OUTPUT_MUTED;
import static tsxdk.parser.LibTsSym.CLIENT_PLATFORM;
import static tsxdk.parser.LibTsSym.CLIENT_SERVERGROUPS;
import static tsxdk.parser.LibTsSym.CLIENT_TALK_POWER;
import static tsxdk.parser.LibTsSym.CLIENT_TOTALCONNECTIONS;
import static tsxdk.parser.LibTsSym.CLIENT_TOTAL_BYTES_DOWNLOADED;
import static tsxdk.parser.LibTsSym.CLIENT_TOTAL_BYTES_UPLOADED;
import static tsxdk.parser.LibTsSym.CLIENT_TYPE;
import static tsxdk.parser.LibTsSym.CLIENT_UNIQUE_IDENTIFIER;
import static tsxdk.parser.LibTsSym.CLIENT_VERSION;
import static tsxdk.parser.LibTsSym.CONNECTION_BANDWIDTH_RECEIVED_LAST_MINUTE_TOTAL;
import static tsxdk.parser.LibTsSym.CONNECTION_BANDWIDTH_RECEIVED_LAST_SECOND_TOTAL;
import static tsxdk.parser.LibTsSym.CONNECTION_BANDWIDTH_SENT_LAST_MINUTE_TOTAL;
import static tsxdk.parser.LibTsSym.CONNECTION_BANDWIDTH_SENT_LAST_SECOND_TOTAL;
import static tsxdk.parser.LibTsSym.CONNECTION_BYTES_RECEIVED_TOTAL;
import static tsxdk.parser.LibTsSym.CONNECTION_BYTES_SENT_TOTAL;
import static tsxdk.parser.LibTsSym.CONNECTION_CLIENT_IP;
import static tsxdk.parser.LibTsSym.CONNECTION_CONNECTED_TIME;
import static tsxdk.parser.LibTsSym.CONNECTION_FILETRANSFER_BANDWIDTH_RECEIVED;
import static tsxdk.parser.LibTsSym.CONNECTION_FILETRANSFER_BANDWIDTH_SENT;
import static tsxdk.parser.LibTsSym.CONNECTION_PACKETS_RECEIVED_TOTAL;
import static tsxdk.parser.LibTsSym.CONNECTION_PACKETS_SENT_TOTAL;

import java.math.BigInteger;
import java.util.Arrays;

import system.core.Default;
import tsxdk.entity.meta.LibEntityState;
import tsxdk.entity.meta.StatefulEntity;
import tsxdk.parser.LibTsSym;
import tsxdk.parser.TsFieldStack;
import utility.bulletin.AdvancedGlobalBulletin;
import utility.misc.BatchSetter;
import utility.misc.StringMan;
import utility.reclaimable.Reclaimable;
import api.data.TsClientDTI;
//import java.util.logging.Logger;
//import teamspeak.TsContext;
import api.data.TsEntityType;

//import system.Context;
//import api.data.AbstractTsClient;

/**
 * The Class Ts_Client.
 */
public class TsClient /*extends AbstractTsClient*/ implements TsClientDTI, Reclaimable, TsEntity, StatefulEntity {
	/** The log. */
//	private Logger log;
  
	private int clid = 0;
    private int cid = 0;
    private int client_database_id = 0;
	private String client_nickname = "NoNameYet";
    private int client_input_muted = 0;
    private int client_away = 0;
    private int client_flag_talking = 0;
    private Integer[] client_servergroups = null;
    private int client_type = 0;
    private String client_unique_identifier = "-1";
    private int client_output_muted = 0;
    private int client_is_priority_speaker = 0;
    private int client_is_recording = 0;
    private int client_is_channel_commander = 0;
    private int client_output_hardware = 0;
    private int client_talk_power = 0;
    private int client_channel_group_id = 0;
    private int client_channel_group_inherited_channel_id = 0;
    private int client_created = 0;
    private int client_lastconnected = 0;
    private int client_is_talker = 0;
    private int client_input_hardware = 0;
    private long client_idle_time = 0;
    private String client_country = "NoCountryYet";
    private String client_away_message = "";
    //-- data once revived on join ------
    private String client_meta_data="";
    private String client_flag_avatar="";
    private String client_description="";
    private int client_unread_messages=0;
    private String client_nickname_phonetic="";
    private int client_needed_serverquery_view_power=0;
    private int client_icon_id=0;
    //-- data on request added -----
    private String client_version="";
    private String client_platform="";
    private String client_default_channel="";
    private int client_totalconnections=0;
    private long client_month_bytes_uploaded=0;
    private long client_month_bytes_downloaded=0;
    private long client_total_bytes_uploaded=0;
    private long client_total_bytes_downloaded=0;
    private long connection_filetransfer_bandwidth_sent=0;
    private long connection_filetransfer_bandwidth_received=0;
    private long connection_packets_sent_total=0;
    private long connection_bytes_sent_total=0;
    private long connection_packets_received_total=0;
    private long connection_bytes_received_total=0;
    private long connection_bandwidth_sent_last_second_total=0;
    private long connection_bandwidth_sent_last_minute_total=0;
    private long connection_bandwidth_received_last_second_total=0;
    private long connection_bandwidth_received_last_minute_total=0;
    private BigInteger connection_connected_time=BigInteger.ZERO;
    private String connection_client_ip="0.0.0.0";
    
    
    //****** custom VAR not set by ts server ***
    /** The talk_time. */
    private int talk_time = 0;
    private Long lastTimeMeasureStamp = 0L;
    
    private LibEntityState state;
    private int propHash;
	private int tsxDbId;
	private int tsxGlueId;
    
    //constructor
    /**
     * Instantiates a new ts_ client.
     */
    public TsClient() {
    	state = LibEntityState.INITIAL; 
    }
    
    @Override
	public void clearForReuse () {
		//do nothing, we rely on update () 
	}
    
    //*************************************************************************
    //******* Setter and getter ***********************************************
    //*************************************************************************

    /* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClid()
	 */
    @Override
	public int getClid() {
		return clid;
	}

	/**
	 * Sets the clid (Client Id).
	 *
	 * @param clid the new clid
	 */
	public void setClid(int clid) {
		this.clid = clid;
	}
	
	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getCid()
	 */
	@Override
	public int getCid() {
		return cid;
	}
	
	/**
	 * Sets the cid (Channel Id).
	 *
	 * @param cid the cid to set
	 */
	public void setCid(int cid) {
		this.cid = cid;
	}
	
	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_Database_id()
	 */
	@Override
	public int getClient_Database_id() {
		return client_database_id;
	}
	
	/**
	 * Sets the db_id(Unique Database Id).
	 *
	 * @param client_database_id the db_id to set
	 */
	public void setClient_Database_id(int client_database_id) {
		this.client_database_id = client_database_id;
	}
	
    /* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_Nickname()
	 */
    @Override
	public String getClient_Nickname() {
		return client_nickname;
	}
	
	/**
	 * Sets the client nickname.
	 *
	 * @param client_nickname the new nickname
	 */
	public void setClient_Nickname(String client_nickname) {
		this.client_nickname = client_nickname;
	}
	
	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_input_muted()
	 */
	@Override
	public int getClient_input_muted() {
		return client_input_muted;
	}
	
	/**
	 * Sets the mike_muted status of this client.
	 *
	 * @param i the new mike_muted status of this client.
	 */
	public void setClient_input_muted(int i) {
		this.client_input_muted = i;
	}
	
	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_Away()
	 */
	@Override
	public int getClient_Away() {
		return client_away;
	}
	
	/**
	 * Sets the away status of this client.
	 *
	 * @param client_away is away?
	 */
	public void setClient_Away(int client_away) {
		this.client_away = client_away;
	}
	
	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getTalking()
	 */
	@Override
	public int getTalking() {
		return client_flag_talking;
	}
	
	/**
	 * Sets the talking status of this client.
	 *
	 * @param i the new talking status 1 or 0.
	 */
	public void setTalking(int i) {
		this.client_flag_talking = i;
	}
	
	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getServergroups()
	 */
	@Override
	public Integer[] getServergroups() {
		return Arrays.copyOf(client_servergroups,client_servergroups.length);
	}
	
	/**
	 * Sets the the server groups of this client must be separated by ",".
	 *
	 * @param servergroups the new servergroups
	 */
	public void setServergroups(Integer[] servergroups) {
		this.client_servergroups = Arrays.copyOf(servergroups,servergroups.length);
	}
	
	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_type()
	 */
	@Override
	public int getClient_type() {
		return client_type;
	}
	
	/**
	 * Sets the client type 0 for query 1 for real client.
	 *
	 * @param client_type the new type 1 or 0.
	 */
	public void setClient_type(int client_type) {
		this.client_type = client_type;
	}
	
	
	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getIdle_time()
	 */
	@Override
	public long getIdle_time() {
		return client_idle_time;
	}
	
	/**
	 * Sets the idle_time.
	 *
	 * @param idle_time the new idle_time
	 */
	public void setIdle_time(long idle_time) {
		this.client_idle_time = idle_time;
	}
	

	
	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getTalk_time()
	 */
	@Override
	public Integer getTalk_time() {
		return talk_time;
	}
	
	/**
	 * Sets the talk_time.
	 *
	 * @param talk_time the new talk_time
	 */
	public void setTalk_time(Integer talk_time) {
		this.talk_time = talk_time;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_Away_Message()
	 */
	@Override
	public String getClient_Away_Message() {
		return client_away_message;
	}


	public void setClient_Away_Message(String client_away_message) {
		this.client_away_message = client_away_message;
	}


	public void setClient_output_hardware( int client_Headset_Hardware_Muted) {	
		this.client_output_hardware = client_Headset_Hardware_Muted;	
	}
	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_output_hardware()
	 */
	@Override
	public int getClient_output_hardware() {	
		return client_output_hardware;	
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_talk_power()
	 */
	@Override
	public int getClient_talk_power() {
		return client_talk_power;
	}


	public void setClient_talk_power(int client_talk_power) {
		this.client_talk_power = client_talk_power;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_is_talker()
	 */
	@Override
	public int getClient_is_talker() {
		return client_is_talker;
	}


	public void setClient_is_talker(int client_is_talker) {
		this.client_is_talker = client_is_talker;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_is_priority_speaker()
	 */
	@Override
	public int getClient_is_priority_speaker() {
		return client_is_priority_speaker;
	}


	public void setClient_is_priority_speaker(int client_is_priority_speaker) {
		this.client_is_priority_speaker = client_is_priority_speaker;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_is_recording()
	 */
	@Override
	public int getClient_is_recording() {
		return client_is_recording;
	}


	public void setClient_is_recording(int client_is_recording) {
		this.client_is_recording = client_is_recording;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_is_channel_commander()
	 */
	@Override
	public int getClient_is_channel_commander() {
		return client_is_channel_commander;
	}


	public void setClient_is_channel_commander(int client_is_channel_commander) {
		this.client_is_channel_commander = client_is_channel_commander;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_unique_identifier()
	 */
	@Override
	public String getClient_unique_identifier() {
		return client_unique_identifier;
	}


	public void setClient_unique_identifier(String client_unique_identifier) {
		this.client_unique_identifier = client_unique_identifier;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_channel_group_id()
	 */
	@Override
	public int getClient_channel_group_id() {
		return client_channel_group_id;
	}


	public void setClient_channel_group_id(int client_channel_group_id) {
		this.client_channel_group_id = client_channel_group_id;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_channel_group_inherited_channel_id()
	 */
	@Override
	public int getClient_channel_group_inherited_channel_id() {
		return client_channel_group_inherited_channel_id;
	}


	public void setClient_channel_group_inherited_channel_id(
			int client_channel_group_inherited_channel_id) {
		this.client_channel_group_inherited_channel_id = client_channel_group_inherited_channel_id;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_created()
	 */
	@Override
	public int getClient_created() {
		return client_created;
	}


	public void setClient_created(int client_created) {
		this.client_created = client_created;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_lastconnected()
	 */
	@Override
	public int getClient_lastconnected() {
		return client_lastconnected;
	}


	public void setClient_lastconnected(int client_lastconnected) {
		this.client_lastconnected = client_lastconnected;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_meta_data()
	 */
	@Override
	public String getClient_meta_data() {
		return client_meta_data;
	}


	public void setClient_meta_data(String client_meta_data) {
		this.client_meta_data = client_meta_data;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_flag_avatar()
	 */
	@Override
	public String getClient_flag_avatar() {
		return client_flag_avatar;
	}


	public void setClient_flag_avatar(String client_flag_avatar) {
		this.client_flag_avatar = client_flag_avatar;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_description()
	 */
	@Override
	public String getClient_description() {
		return client_description;
	}


	public void setClient_description(String client_description) {
		this.client_description = client_description;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_unread_messages()
	 */
	@Override
	public int getClient_unread_messages() {
		return client_unread_messages;
	}


	public void setClient_unread_messages(int client_unread_messages) {
		this.client_unread_messages = client_unread_messages;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_nickname_phonetic()
	 */
	@Override
	public String getClient_nickname_phonetic() {
		return client_nickname_phonetic;
	}


	public void setClient_nickname_phonetic(String client_nickname_phonetic) {
		this.client_nickname_phonetic = client_nickname_phonetic;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_needed_serverquery_view_power()
	 */
	@Override
	public int getClient_needed_serverquery_view_power() {
		return client_needed_serverquery_view_power;
	}


	public void setClient_needed_serverquery_view_power(
			int client_needed_serverquery_view_power) {
		this.client_needed_serverquery_view_power = client_needed_serverquery_view_power;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_icon_id()
	 */
	@Override
	public int getClient_icon_id() {
		return client_icon_id;
	}


	public void setClient_icon_id(int client_icon_id) {
		this.client_icon_id = client_icon_id;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_output_muted()
	 */
	@Override
	public int getClient_output_muted() {
		return client_output_muted;
	}


	public void setClient_output_muted(int client_output_muted) {
		this.client_output_muted = client_output_muted;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_input_hardware()
	 */
	@Override
	public int getClient_input_hardware() {
		return client_input_hardware;
	}


	public void setClient_input_hardware(int client_input_hardware) {
		this.client_input_hardware = client_input_hardware;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_country()
	 */
	@Override
	public String getClient_country() {
		return client_country;
	}


	public void setClient_country(String client_country) {
		this.client_country = client_country;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_version()
	 */
	@Override
	public String getClient_version() {
		return client_version;
	}


	public void setClient_version(String client_version) {
		this.client_version = client_version;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_platform()
	 */
	@Override
	public String getClient_platform() {
		return client_platform;
	}


	public void setClient_platform(String client_platform) {
		this.client_platform = client_platform;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_default_channel()
	 */
	@Override
	public String getClient_default_channel() {
		return client_default_channel;
	}


	public void setClient_default_channel(String client_default_channel) {
		this.client_default_channel = client_default_channel;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_totalconnections()
	 */
	@Override
	public int getClient_totalconnections() {
		return client_totalconnections;
	}


	public void setClient_totalconnections(int client_totalconnections) {
		this.client_totalconnections = client_totalconnections;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_month_bytes_uploaded()
	 */
	@Override
	public long getClient_month_bytes_uploaded() {
		return client_month_bytes_uploaded;
	}


	public void setClient_month_bytes_uploaded(long client_month_bytes_uploaded) {
		this.client_month_bytes_uploaded = client_month_bytes_uploaded;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_month_bytes_downloaded()
	 */
	@Override
	public long getClient_month_bytes_downloaded() {
		return client_month_bytes_downloaded;
	}


	public void setClient_month_bytes_downloaded(
			long client_month_bytes_downloaded) {
		this.client_month_bytes_downloaded = client_month_bytes_downloaded;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_total_bytes_uploaded()
	 */
	@Override
	public long getClient_total_bytes_uploaded() {
		return client_total_bytes_uploaded;
	}


	public void setClient_total_bytes_uploaded(long client_total_bytes_uploaded) {
		this.client_total_bytes_uploaded = client_total_bytes_uploaded;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getClient_total_bytes_downloaded()
	 */
	@Override
	public long getClient_total_bytes_downloaded() {
		return client_total_bytes_downloaded;
	}


	public void setClient_total_bytes_downloaded(
			long client_total_bytes_downloaded) {
		this.client_total_bytes_downloaded = client_total_bytes_downloaded;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getConnection_filetransfer_bandwidth_sent()
	 */
	@Override
	public long getConnection_filetransfer_bandwidth_sent() {
		return connection_filetransfer_bandwidth_sent;
	}


	public void setConnection_filetransfer_bandwidth_sent(
			long connection_filetransfer_bandwidth_sent) {
		this.connection_filetransfer_bandwidth_sent = connection_filetransfer_bandwidth_sent;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getConnection_filetransfer_bandwidth_received()
	 */
	@Override
	public long getConnection_filetransfer_bandwidth_received() {
		return connection_filetransfer_bandwidth_received;
	}


	public void setConnection_filetransfer_bandwidth_received(
			long connection_filetransfer_bandwidth_received) {
		this.connection_filetransfer_bandwidth_received = connection_filetransfer_bandwidth_received;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getConnection_packets_sent_total()
	 */
	@Override
	public long getConnection_packets_sent_total() {
		return connection_packets_sent_total;
	}


	public void setConnection_packets_sent_total(
			long connection_packets_sent_total) {
		this.connection_packets_sent_total = connection_packets_sent_total;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getConnection_bytes_sent_total()
	 */
	@Override
	public long getConnection_bytes_sent_total() {
		return connection_bytes_sent_total;
	}


	public void setConnection_bytes_sent_total(long connection_bytes_sent_total) {
		this.connection_bytes_sent_total = connection_bytes_sent_total;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getConnection_packets_received_total()
	 */
	@Override
	public long getConnection_packets_received_total() {
		return connection_packets_received_total;
	}


	public void setConnection_packets_received_total(
			long connection_packets_received_total) {
		this.connection_packets_received_total = connection_packets_received_total;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getConnection_bytes_received_total()
	 */
	@Override
	public long getConnection_bytes_received_total() {
		return connection_bytes_received_total;
	}


	public void setConnection_bytes_received_total(
			long connection_bytes_received_total) {
		this.connection_bytes_received_total = connection_bytes_received_total;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getConnection_bandwidth_sent_last_second_total()
	 */
	@Override
	public long getConnection_bandwidth_sent_last_second_total() {
		return connection_bandwidth_sent_last_second_total;
	}


	public void setConnection_bandwidth_sent_last_second_total(
			long connection_bandwidth_sent_last_second_total) {
		this.connection_bandwidth_sent_last_second_total = connection_bandwidth_sent_last_second_total;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getConnection_bandwidth_received_last_second_total()
	 */
	@Override
	public long getConnection_bandwidth_received_last_second_total() {
		return connection_bandwidth_received_last_second_total;
	}


	public void setConnection_bandwidth_received_last_second_total(
			long connection_bandwidth_received_last_second_total) {
		this.connection_bandwidth_received_last_second_total = connection_bandwidth_received_last_second_total;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getConnection_bandwidth_sent_last_minute_total()
	 */
	@Override
	public long getConnection_bandwidth_sent_last_minute_total() {
		return connection_bandwidth_sent_last_minute_total;
	}


	public void setConnection_bandwidth_sent_last_minute_total(
			long connection_bandwidth_sent_last_minute_total) {
		this.connection_bandwidth_sent_last_minute_total = connection_bandwidth_sent_last_minute_total;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getConnection_bandwidth_received_last_minute_total()
	 */
	@Override
	public long getConnection_bandwidth_received_last_minute_total() {
		return connection_bandwidth_received_last_minute_total;
	}


	public void setConnection_bandwidth_received_last_minute_total(
			long connection_bandwidth_received_last_minute_total) {
		this.connection_bandwidth_received_last_minute_total = connection_bandwidth_received_last_minute_total;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getConnection_connected_time()
	 */
	@Override
	public BigInteger getConnection_connected_time() {
		return connection_connected_time;
	}


	public void setConnection_connected_time(BigInteger connection_connected_time) {
		this.connection_connected_time = connection_connected_time;
	}


	/* (non-Javadoc)
	 * @see teamspeak.Ts_ClientInterface#getConnection_client_ip()
	 */
	@Override
	public String getConnection_client_ip() {
		return connection_client_ip;
	}


	public void setConnection_client_ip(String connection_client_ip) {
		this.connection_client_ip = connection_client_ip;
	}


	@Override
	public void setUpdated() {
		state.setUpdatedState(this);
//		GlobalBulletin.getSharedInstance().STATE.push (" setting state to ", new String[]{"State", this.client_nickname}, new Object[]{state});
	}

	@Override
	public void setCreated() {
		state.setCreatedState(this);
//		GlobalBulletin.getSharedInstance().STATE.push (" setting state to ", new String[]{"State", this.client_nickname}, new Object[]{state});
	}

	@Override
	public void setTouched() {
		state.setTouchedState(this);
//		GlobalBulletin.getSharedInstance().STATE.push (" setting state to ", new String[]{"State", this.client_nickname}, new Object[]{state});
	}

	@Override
	public void setUnused() {
		state.setUnusedState(this);
//		GlobalBulletin.getSharedInstance().STATE.push (" setting state to ", new String[]{"State", this.client_nickname}, new Object[]{state});
	}
	
	@Override
	public void setInitial() {
		state.setInitialState(this);
//		GlobalBulletin.getSharedInstance().STATE.push (" setting state to ", new String[]{"State", this.client_nickname}, new Object[]{state});
	}

	@Override
	public LibEntityState getState() {
		return state;
	}


	@Override
	public void setState(LibEntityState state) {
		this.state = state;		
//		GlobalBulletin.getSharedInstance().STATE.push (" setting state to ", new String[]{"State", this.client_nickname}, new Object[]{state});
	}


	@Override
	public void update(TsFieldStack stack) {
		int tempOldHash = 0x123456;
		int tempNewHash = tempOldHash;
		int totalOldHash = tempOldHash;
		int totalNewHash = totalOldHash;
		
		tempOldHash ^= clid ^ (cid * 2) ^ client_database_id ^ (client_type * 4);
		
		clid = (Integer) stack.popFirst(CLID);
		cid = (Integer) stack.ifSet(cid,CID);
		client_database_id = (Integer) stack.popFirst(CLIENT_DATABASE_ID);
		client_type = (Integer) stack.popFirst(CLIENT_TYPE);
		
		//set the bot client
		if (client_database_id == (Integer)Default.bot_client_db_id.getValue())
			EntityManager.getSharedInstance().setBotClient(this);
		
		tempNewHash ^= clid ^ (cid * 2) ^ client_database_id ^ (client_type * 4);
		
		totalOldHash ^= tempOldHash;
		totalNewHash ^= tempNewHash;
		
		//nickname set by parser - is Slot-id !
		
//-country parm
		if(stack.hasField(CLIENT_COUNTRY)) {
			tempOldHash = tempNewHash = 0x123456;
			tempOldHash ^= client_country.hashCode();
			client_country = (String) stack.popFirst(CLIENT_COUNTRY);
			tempNewHash ^= client_country.hashCode();
			totalOldHash ^= tempOldHash;
			totalNewHash ^= tempNewHash;
		}
	
//-uid parm
		if (stack.hasField(CLIENT_UNIQUE_IDENTIFIER)) {
			tempOldHash = tempNewHash = 0x123456;
			tempOldHash ^= client_unique_identifier.hashCode();
			client_unique_identifier = (String) stack.popFirst(CLIENT_UNIQUE_IDENTIFIER);
			tempNewHash ^= client_unique_identifier.hashCode();
//			totalOldHash ^= tempOldHash;
//			totalNewHash ^= tempNewHash;
			//uid does not change !
		}
		totalOldHash ^= Arrays.deepToString(client_servergroups).hashCode();
		client_servergroups = (Integer[]) stack.ifSet(client_servergroups, CLIENT_SERVERGROUPS);
		totalNewHash ^= Arrays.deepToString(client_servergroups).hashCode();

//-groups parm
		if (stack.hasFields(new LibTsSym[] {CLIENT_CHANNEL_GROUP_ID, CLIENT_CHANNEL_GROUP_INHERITED_CHANNEL_ID})) {
			tempOldHash = tempNewHash = 0x123456;
			tempOldHash ^= client_channel_group_id ^ client_channel_group_inherited_channel_id;
			client_channel_group_id = (Integer) stack.popFirst(CLIENT_CHANNEL_GROUP_ID);
			client_channel_group_inherited_channel_id= (Integer) stack.popFirst(CLIENT_CHANNEL_GROUP_INHERITED_CHANNEL_ID);
			tempNewHash ^= client_channel_group_id ^ client_channel_group_inherited_channel_id;
			totalOldHash ^= tempOldHash;
			totalNewHash ^= tempNewHash;
		}

//-voice parm
		
			tempOldHash = tempNewHash = 0x123456;
			tempOldHash ^= client_flag_talking ^ (client_input_muted * 2) ^ 
					(client_input_hardware*4) ^ (client_output_muted * 8)  ^ 
					(client_output_hardware * 16) ^ (client_talk_power * 32) ^
					(client_is_talker * 64) ^ (client_is_priority_speaker * 128)^ 
					(client_is_recording * 256) ^ (client_is_channel_commander * 512);
			
			client_flag_talking = (Integer) stack.ifSet(client_flag_talking, CLIENT_FLAG_TALKING);
			client_input_muted = (Integer) stack.ifSet(client_input_muted, CLIENT_INPUT_MUTED);
			client_input_hardware = (Integer) stack.ifSet(client_input_hardware, CLIENT_INPUT_HARDWARE);
			client_output_muted = (Integer) stack.ifSet(client_output_muted, CLIENT_OUTPUT_MUTED);
			client_output_hardware = (Integer) stack.ifSet(client_output_hardware, CLIENT_OUTPUT_HARDWARE);
			client_talk_power = (Integer) stack.ifSet(client_talk_power, CLIENT_TALK_POWER);
			client_is_talker = (Integer) stack.ifSet(client_is_talker, CLIENT_IS_TALKER);
			client_is_priority_speaker = (Integer) stack.ifSet(client_is_priority_speaker, CLIENT_IS_PRIORITY_SPEAKER);
			client_is_recording = (Integer) stack.ifSet(client_is_recording, CLIENT_IS_RECORDING);
			client_is_channel_commander = (Integer) stack.ifSet(client_is_channel_commander, CLIENT_IS_CHANNEL_COMMANDER);
			
			tempNewHash ^= client_flag_talking ^ (client_input_muted * 2) ^ 
					(client_input_hardware*4) ^ (client_output_muted * 8)  ^ 
					(client_output_hardware * 16) ^ (client_talk_power * 32) ^
					(client_is_talker * 64) ^ (client_is_priority_speaker * 128)^ 
					(client_is_recording * 256) ^ (client_is_channel_commander * 512);
			
			totalOldHash ^= tempOldHash;
			totalNewHash ^= tempNewHash;
			
			if (client_flag_talking == 1) {
				if (talk_time > 0) {
					talk_time += System.currentTimeMillis() - lastTimeMeasureStamp;
				} else {
					talk_time = 1;
					lastTimeMeasureStamp = System.currentTimeMillis();
				}
			} else {
				talk_time = 0;
			}
		
		
//-away parm
		if (stack.hasField(CLIENT_AWAY))  {
			tempOldHash = tempNewHash = 0x123456;
			tempOldHash ^= client_away ^ client_away_message.hashCode();
			client_away = (Integer) stack.popFirst(CLIENT_AWAY);
			client_away_message = (String) stack.popFirst(CLIENT_AWAY_MESSAGE);
			tempNewHash ^= client_away ^ client_away_message.hashCode();
			totalOldHash ^= tempOldHash;
			totalNewHash ^= tempNewHash;
		}
		
//-idle parm		
		if (stack.hasFields(new LibTsSym[] {CLIENT_IDLE_TIME, CLIENT_CREATED, CLIENT_LASTCONNECTED})) {
			tempOldHash = tempNewHash = 0x123456;
			tempOldHash ^= client_idle_time ^ client_created ^ client_lastconnected;
			client_idle_time = (Long) stack.popFirst (CLIENT_IDLE_TIME);
			client_created = (Integer) stack.popFirst(CLIENT_CREATED);
			client_lastconnected = (Integer) stack.popFirst(CLIENT_LASTCONNECTED);
			tempNewHash ^= client_idle_time ^ client_created ^ client_lastconnected;
			//idle time updated always - no update-event !
//			totalOldHash ^= tempOldHash;
//			totalNewHash ^= tempNewHash;
		}
		
		
		//currently source unknown :
		client_version = (String) stack.ifSet(client_version, CLIENT_VERSION);
		client_version = (String) stack.ifSet(client_platform, CLIENT_PLATFORM);
		client_version = (String) stack.ifSet(client_default_channel, CLIENT_DEFAULT_CHANNEL);
		connection_client_ip = (String) stack.ifSet(connection_client_ip, CONNECTION_CLIENT_IP);
		
		client_totalconnections = (Integer) stack.ifSet(client_totalconnections, CLIENT_TOTALCONNECTIONS);
		
		client_month_bytes_uploaded = (Long) stack.ifSet(client_month_bytes_uploaded, CLIENT_MONTH_BYTES_UPLOADED);
		client_month_bytes_downloaded = (Long) stack.ifSet(client_month_bytes_downloaded, CLIENT_MONTH_BYTES_DOWNLOADED);
		client_total_bytes_uploaded = (Long) stack.ifSet(client_total_bytes_uploaded, CLIENT_TOTAL_BYTES_UPLOADED);
		client_total_bytes_downloaded = (Long) stack.ifSet(client_total_bytes_downloaded, CLIENT_TOTAL_BYTES_DOWNLOADED);
		
		connection_filetransfer_bandwidth_sent = (Long) stack.ifSet(connection_filetransfer_bandwidth_sent,CONNECTION_FILETRANSFER_BANDWIDTH_SENT);
		connection_filetransfer_bandwidth_received = (Long) stack.ifSet(connection_filetransfer_bandwidth_sent,CONNECTION_FILETRANSFER_BANDWIDTH_RECEIVED);
		connection_packets_sent_total = (Long) stack.ifSet(connection_packets_sent_total, CONNECTION_PACKETS_SENT_TOTAL);
		connection_packets_received_total = (Long) stack.ifSet(connection_packets_received_total , CONNECTION_PACKETS_RECEIVED_TOTAL);
		connection_bytes_sent_total = (Long) stack.ifSet(connection_bytes_sent_total, CONNECTION_BYTES_SENT_TOTAL);
		connection_bytes_received_total = (Long) stack.ifSet(connection_bytes_received_total, CONNECTION_BYTES_RECEIVED_TOTAL);

		connection_bandwidth_sent_last_second_total = (Long) stack.ifSet(connection_bandwidth_sent_last_second_total, CONNECTION_BANDWIDTH_SENT_LAST_SECOND_TOTAL);
		connection_bandwidth_received_last_second_total = (Long) stack.ifSet(connection_bandwidth_received_last_second_total ,CONNECTION_BANDWIDTH_RECEIVED_LAST_SECOND_TOTAL);
		connection_bandwidth_sent_last_minute_total = (Long) stack.ifSet(connection_bandwidth_sent_last_minute_total, CONNECTION_BANDWIDTH_SENT_LAST_MINUTE_TOTAL);
		connection_bandwidth_received_last_minute_total = (Long) stack.ifSet(connection_bandwidth_received_last_minute_total, CONNECTION_BANDWIDTH_RECEIVED_LAST_MINUTE_TOTAL);
		connection_connected_time = (BigInteger) stack.ifSet(connection_connected_time, CONNECTION_CONNECTED_TIME);

		client_meta_data = (String) stack.ifSet(client_meta_data, CLIENT_META_DATA);
		client_flag_avatar = (String) stack.ifSet(client_flag_avatar, CLIENT_FLAG_AVATAR);
		client_nickname_phonetic = (String) stack.ifSet(client_nickname_phonetic, CLIENT_NICKNAME_PHONETIC);
		client_needed_serverquery_view_power = (int) stack.ifSet(client_needed_serverquery_view_power, CLIENT_NEEDED_SERVERQUERY_VIEW_POWER);
		client_icon_id = (int) stack.ifSet(client_icon_id, CLIENT_ICON_ID);
		client_description = (String) stack.ifSet(client_description, CLIENT_DESCRIPTION);
		
		if(totalOldHash != totalNewHash)
			setUpdated();
	}


	@Override
	public TsEntityType getType() {
		return TsEntityType.TSCLIENT;
	}


	@Override
	public int getTsPropHash() {
		return propHash;
	}


	@Override
	public void setTsPropHash(String str) {
		this.propHash = str.hashCode();		
	}


	@Override
	public String getSlotID() {
		return client_nickname;
	}

	@Override
	public void updateSlotID(Object k) {
		AdvancedGlobalBulletin.getSharedInstance().ENTITYSTATES.Verbose.push("Updating Slot: ", new String[]{"TsClient", "updateSlotID"}, new Object[]{client_nickname,k});
		client_nickname = (String) k;
	}

	public String toString() {
		return client_nickname;
	}
	
	public String getDataString() {
		return "{clid,cid,client_database_id,client_nickname client_input_muted " +
				"client_away client_flag_talking client_servergroups " +
				"client_type client_unique_identifier client_output_muted" +
				"client_is_priority_speaker client_is_recording client_is_channel_commander client_output_hardware" +
				"client_talk_power client_channel_group_id client_channel_group_inherited_channel_id client_created" +
				"client_lastconnected client_is_talker client_input_hardware client_idle_time" +
				"client_country client_away_message client_meta_data client_flag_avatar " +
				"client_description client_unread_messages client_nickname_phonetic " +
				"client_needed_serverquery_view_power client_icon_id client_version client_platform" +
				"client_default_channel client_totalconnections client_month_bytes_uploaded client_month_bytes_downloaded" +
				"client_total_bytes_uploaded client_total_bytes_downloaded connection_filetransfer_bandwidth_sent " +
				"connection_filetransfer_bandwidth_received connection_packets_sent_total connection_bytes_sent_total" +
				"connection_packets_received_total connection_packets_received_total connection_bytes_received_total" +
				"connection_bandwidth_sent_last_second_total connection_bandwidth_sent_last_minute_total" +
				"connection_bandwidth_received_last_second_total connection_bandwidth_received_last_minute_total" +
				"connection_bandwidth_received_last_second_total connection_bandwidth_received_last_minute_total" +
				"connection_connected_time connection_client_ip :{" + clid+","+cid+","+client_database_id+","+client_nickname+","+client_input_muted+","+ 
					client_away+","+client_flag_talking+","+Arrays.deepToString(client_servergroups)+","+
					client_type+","+client_unique_identifier+","+client_output_muted +","+
					client_is_priority_speaker+","+client_is_recording+","+client_is_channel_commander+","+client_output_hardware +","+
					client_talk_power+","+client_channel_group_id+","+client_channel_group_inherited_channel_id+","+client_created +","+
					client_lastconnected+","+client_is_talker+","+client_input_hardware+","+client_idle_time +","+
					client_country+","+client_away_message+","+client_meta_data+","+client_flag_avatar +","+
					client_description+","+client_unread_messages+","+client_nickname_phonetic +","+
					client_needed_serverquery_view_power+","+client_icon_id+","+client_version+","+client_platform +","+
					client_default_channel+","+client_totalconnections+","+client_month_bytes_uploaded+","+client_month_bytes_downloaded +","+
					client_total_bytes_uploaded+","+client_total_bytes_downloaded+","+connection_filetransfer_bandwidth_sent  +","+
					connection_filetransfer_bandwidth_received+","+connection_packets_sent_total+","+connection_bytes_sent_total +","+
					connection_packets_received_total+","+connection_packets_received_total+","+connection_bytes_received_total +","+
					connection_bandwidth_sent_last_second_total+","+connection_bandwidth_sent_last_minute_total+","+
					connection_bandwidth_received_last_second_total+","+connection_bandwidth_received_last_minute_total+","+
					connection_bandwidth_received_last_second_total+","+connection_bandwidth_received_last_minute_total +","+
					connection_connected_time+","+connection_client_ip+"}}";
	}
	

	@Override
	public void setTSXDBID(int id) {
		this.tsxDbId = id;
	}
	@Override
	public int getTSXDBID() {
		return this.tsxDbId;
	}
	
	@Override
	public int getGlueID() {
		return tsxGlueId;
	}
	
	@Override
	public void setGlueID(int id) {
		this.tsxGlueId = id;
	}
	
	public void prepareForInsert(final BatchSetter setter) {
		setter.injectArgs(clid,cid ,client_database_id,client_nickname ,client_input_muted ,client_away ,client_flag_talking,
				StringMan.getStringFromArray(client_servergroups),client_type,client_unique_identifier,client_output_muted ,client_is_priority_speaker,
				client_is_recording ,client_is_channel_commander ,client_output_hardware ,client_talk_power ,
				client_channel_group_id ,client_channel_group_inherited_channel_id ,client_created ,client_lastconnected ,
				client_is_talker ,client_input_hardware ,client_idle_time ,client_country,client_away_message, client_meta_data ,
				client_flag_avatar ,client_description ,client_unread_messages ,client_nickname_phonetic  ,client_needed_serverquery_view_power ,client_icon_id ,client_version ,
				client_platform ,client_default_channel ,client_totalconnections ,connection_client_ip);
	}
	
	public void prepareForUpdate(final BatchSetter setter) {
		prepareForInsert(setter);
		setter.injectArgs(getTSXDBID());
	}
}