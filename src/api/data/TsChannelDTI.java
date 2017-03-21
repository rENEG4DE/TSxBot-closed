package api.data;

public interface TsChannelDTI extends TsEntityObject{

	int getCid();

	int getPid();

	int getChannel_order();

	String getChannel_name();

	String getChannel_topic();

	int getChannel_flag_default();

	int getChannel_flag_password();

	int getChannel_flag_permanent();

	int getChannel_flag_semi_permanent();

	int getChannel_codec();

	int getChannel_codec_quality();

	int getChannel_needed_talk_power();

	int getTotal_clients_family();

	int getChannel_maxclients();

	int getChannel_maxfamilyclients();

	int getTotal_clients();

	int getChannel_needed_subscribe_power();

}