package tsxdk.parser;

/**
 * Every symbol-id we ever get from ts
 * @author kornholio
 *
 */

//Thanks to gvim and the command :%s/.*/& (\"\L&\E\".hashCode()), :D

public enum LibTsSym implements AbstractSymbol{
	//New Symbols - thx if-ts-admin...
	
	CLIENT_BADGES ("client_badges".hashCode()),
	
	//please keep alphabetic order when adding new symbols
	
	CFID ("cfid".hashCode()),
	CHANNEL_CODEC ("channel_codec".hashCode()),
	CHANNEL_CODEC_QUALITY ("channel_codec_quality".hashCode()),
	CHANNEL_FLAG_DEFAULT ("channel_flag_default".hashCode()),
	CHANNEL_FLAG_PASSWORD ("channel_flag_password".hashCode()),
	CHANNEL_FLAG_PERMANENT ("channel_flag_permanent".hashCode()),
	CHANNEL_FLAG_SEMI_PERMANENT ("channel_flag_semi_permanent".hashCode()),
	CHANNEL_MAXCLIENTS ("channel_maxclients".hashCode()),
	CHANNEL_MAXFAMILYCLIENTS ("channel_maxfamilyclients".hashCode()),
	CHANNEL_NAME ("channel_name".hashCode()),
	CHANNEL_NEEDED_SUBSCRIBE_POWER ("channel_needed_subscribe_power".hashCode()),
	CHANNEL_NEEDED_TALK_POWER ("channel_needed_talk_power".hashCode()),
	CHANNEL_ORDER ("channel_order".hashCode()),
	CHANNEL_TOPIC ("channel_topic".hashCode()),
	CID ("cid".hashCode()),
	CLID ("clid".hashCode()),
	CLID_ADVANCED ("clid_advanced".hashCode()),
	CLIENT_AWAY ("client_away".hashCode()),
	CLIENT_AWAY_MESSAGE ("client_away_message".hashCode()),
	CLIENT_CHANNEL_GROUP_ID ("client_channel_group_id".hashCode()),
	CLIENT_CHANNEL_GROUP_INHERITED_CHANNEL_ID ("client_channel_group_inherited_channel_id".hashCode()),
	CLIENT_COUNTRY ("client_country".hashCode()),
	CLIENT_CREATED ("client_created".hashCode()),
	CLIENT_DATABASE_ID ("client_database_id".hashCode()),
	CLIENT_DEFAULT_CHANNEL ("client_default_channel".hashCode()),
	CLIENT_DESCRIPTION ("client_description".hashCode()),
	CLIENT_FLAG_AVATAR ("client_flag_avatar".hashCode()),
	CLIENT_FLAG_TALKING ("client_flag_talking".hashCode()),
	CLIENT_ICON_ID ("client_icon_id".hashCode()),
	CLIENT_IDLE_TIME ("client_idle_time".hashCode()),
	CLIENT_INPUT_HARDWARE ("client_input_hardware".hashCode()),
	CLIENT_INPUT_MUTED ("client_input_muted".hashCode()),
	CLIENT_IS_CHANNEL_COMMANDER ("client_is_channel_commander".hashCode()),
	CLIENT_IS_PRIORITY_SPEAKER ("client_is_priority_speaker".hashCode()),
	CLIENT_IS_RECORDING ("client_is_recording".hashCode()),
	CLIENT_IS_TALKER ("client_is_talker".hashCode()),
	CLIENT_LASTCONNECTED ("client_lastconnected".hashCode()),
	CLIENT_META_DATA ("client_meta_data".hashCode()),
	CLIENT_MONTH_BYTES_DOWNLOADED ("client_month_bytes_downloaded".hashCode()),
	CLIENT_MONTH_BYTES_UPLOADED ("client_month_bytes_uploaded".hashCode()),
	CLIENT_NEEDED_SERVERQUERY_VIEW_POWER ("client_needed_serverquery_view_power".hashCode()),
	CLIENT_NICKNAME ("client_nickname".hashCode()),
	CLIENT_NICKNAME_PHONETIC ("client_nickname_phonetic".hashCode()),
	CLIENT_OUTPUTONLY_MUTED ("client_outputonly_muted".hashCode()),
	CLIENT_OUTPUT_HARDWARE ("client_output_hardware".hashCode()),
	CLIENT_OUTPUT_MUTED ("client_output_muted".hashCode()),
	CLIENT_PLATFORM ("client_platform".hashCode()),
	CLIENT_SERVERGROUPS ("client_servergroups".hashCode()),
	CLIENT_TALK_POWER ("client_talk_power".hashCode()),
	CLIENT_TALK_REQUEST ("client_talk_request".hashCode()),
	CLIENT_TALK_REQUEST_MSG ("client_talk_request_msg".hashCode()),
	CLIENT_TOTALCONNECTIONS ("client_totalconnections".hashCode()),
	CLIENT_TOTAL_BYTES_DOWNLOADED ("client_total_bytes_downloaded".hashCode()),
	CLIENT_TOTAL_BYTES_UPLOADED ("client_total_bytes_uploaded".hashCode()),
	CLIENT_TYPE ("client_type".hashCode()),
	CLIENT_UNIQUE_IDENTIFIER ("client_unique_identifier".hashCode()),
	CLIENT_UNREAD_MESSAGES ("client_unread_messages".hashCode()),
	CLIENT_VERSION ("client_version".hashCode()),
	CONNECTION_BANDWIDTH_RECEIVED_LAST_MINUTE_TOTAL ("connection_bandwidth_received_last_minute_total".hashCode()),
	CONNECTION_BANDWIDTH_RECEIVED_LAST_SECOND_TOTAL ("connection_bandwidth_received_last_second_total".hashCode()),
	CONNECTION_BANDWIDTH_SENT_LAST_MINUTE_TOTAL ("connection_bandwidth_sent_last_minute_total".hashCode()),
	CONNECTION_BANDWIDTH_SENT_LAST_SECOND_TOTAL ("connection_bandwidth_sent_last_second_total".hashCode()),
	CONNECTION_BYTES_RECEIVED_TOTAL ("connection_bytes_received_total".hashCode()),
	CONNECTION_BYTES_SENT_TOTAL ("connection_bytes_sent_total".hashCode()),
	CONNECTION_CLIENT_IP ("connection_client_ip".hashCode()),
	CONNECTION_CONNECTED_TIME ("connection_connected_time".hashCode()),
	CONNECTION_FILETRANSFER_BANDWIDTH_RECEIVED ("connection_filetransfer_bandwidth_received".hashCode()),
	CONNECTION_FILETRANSFER_BANDWIDTH_SENT ("connection_filetransfer_bandwidth_sent".hashCode()),
	CONNECTION_PACKETS_RECEIVED_TOTAL ("connection_packets_received_total".hashCode()),
	CONNECTION_PACKETS_SENT_TOTAL ("connection_packets_sent_total".hashCode()),
	CTID ("ctid".hashCode()),
	ERROR ("error".hashCode()),
	FCLDBID ("fcldbid".hashCode()),
	FNAME ("fname".hashCode()),
	HASH ("hash".hashCode()),
	ID ("id".hashCode()),
	INVOKERID ("invokerid".hashCode()),
	INVOKERNAME ("invokername".hashCode()),
	INVOKERUID ("invokeruid".hashCode()),
	MESSAGE ("message".hashCode()),
	MSG ("msg".hashCode()),
	NOTIFYCLIENTENTERVIEW ("notifycliententerview".hashCode()),
	NOTIFYCLIENTLEFTVIEW ("notifyclientleftview".hashCode()),
	NOTIFYTEXTMESSAGE ("notifytextmessage".hashCode()),
	PID ("pid".hashCode()),
	REASONID ("reasonid".hashCode()),
	REASONMSG ("reasonmsg".hashCode()),
	TARGET ("target".hashCode()),
	TARGETMODE ("targetmode".hashCode()),
	TCLDBID ("tcldbid".hashCode()),
	TIMESTAMP ("timestamp".hashCode()),
	TNAME ("tname".hashCode()),
	TOTAL_CLIENTS ("total_clients".hashCode()),
	TOTAL_CLIENTS_FAMILY ("total_clients_family".hashCode());
	
	LibTsSym (int val) {
		this.value = val;
	}
	
	@Override
	public final int getValue() {
		return value;
	}
	
	private final int value;
		
	public static final LibTsSym parseSymbol (String symbol) throws IllegalArgumentException {
		try {
			return valueOf (symbol.toUpperCase());
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			throw e;
		}
	}

}