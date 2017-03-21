package api.data;

import java.math.BigInteger;

public interface TsClientDTI extends TsEntityObject{

	/**
	 * Gets the clid (Client Id).
	 *  
	 * @return the clid as String
	 */
	int getClid();

	/**
	 * Gets the cid (Channel Id).
	 *
	 * @return the cid
	 */
	int getCid();

	/**
	 * Gets the db_id (Unique Database Id).
	 *
	 * @return the db_id
	 */
	int getClient_Database_id();

	/**
	 * Gets the client nickname.
	 *
	 * @return the nickname
	 */
	String getClient_Nickname();

	/**
	 * Gets the mike_muted status of this client.
	 *
	 * @return the mike_muted status 1 or 0
	 */
	int getClient_input_muted();

	/**
	 * Gets the away status of this client.
	 *
	 * @return the away 1 or 0
	 */
	int getClient_Away();

	/**
	 * Gets the talking status of this client.
	 *
	 * @return the talking 1 or 0
	 */
	int getTalking();

	/**
	 * Gets the server groups of this client separated by ",".
	 *
	 * @return the servergroups
	 */
	Integer[] getServergroups();

	/**
	 * Gets the client type 0 for query 1 for real client.
	 *
	 * @return the type 1 or 0.
	 */
	int getClient_type();

	/**
	 * Gets the idle_time.
	 *
	 * @return the idle_time
	 */
	long getIdle_time();

	/**
	 * Gets the talk_time.
	 *
	 * @return the talk_time
	 */
	Integer getTalk_time();

	String getClient_Away_Message();

	int getClient_output_hardware();

	int getClient_talk_power();

	int getClient_is_talker();

	int getClient_is_priority_speaker();

	int getClient_is_recording();

	int getClient_is_channel_commander();

	String getClient_unique_identifier();

	int getClient_channel_group_id();

	int getClient_channel_group_inherited_channel_id();

	int getClient_created();

	int getClient_lastconnected();

	String getClient_meta_data();

	String getClient_flag_avatar();

	String getClient_description();

	int getClient_unread_messages();

	String getClient_nickname_phonetic();

	int getClient_needed_serverquery_view_power();

	int getClient_icon_id();

	int getClient_output_muted();

	int getClient_input_hardware();

	String getClient_country();

	String getClient_version();

	String getClient_platform();

	String getClient_default_channel();

	int getClient_totalconnections();

	long getClient_month_bytes_uploaded();

	long getClient_month_bytes_downloaded();

	long getClient_total_bytes_uploaded();

	long getClient_total_bytes_downloaded();

	long getConnection_filetransfer_bandwidth_sent();

	long getConnection_filetransfer_bandwidth_received();

	long getConnection_packets_sent_total();

	long getConnection_bytes_sent_total();

	long getConnection_packets_received_total();

	long getConnection_bytes_received_total();

	long getConnection_bandwidth_sent_last_second_total();

	long getConnection_bandwidth_received_last_second_total();

	long getConnection_bandwidth_sent_last_minute_total();

	long getConnection_bandwidth_received_last_minute_total();

	BigInteger getConnection_connected_time();

	String getConnection_client_ip();

}