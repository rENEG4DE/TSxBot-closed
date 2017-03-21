package api.util;

import java.util.List;

import api.data.TsClientDTI;
import api.query.AbstractQuery;
import api.service.UtilityProvider;

public interface QueryProvider extends UtilityProvider {

	AbstractQuery sendPrivateMessageToClient(int clid, String msg);

	AbstractQuery kickClient(int clid, String msg);

	AbstractQuery pokeClient(int clid, String msg);

	AbstractQuery banClient(String uid, String msg);

	AbstractQuery addClientToGroup(int dbid, int sgid);

	AbstractQuery removeClientFromGroup(int dbid, int sgid);

	AbstractQuery createSubChannel(int parent, int cdc_quality, String name, String topic);

	AbstractQuery createChannel(int cdc_quality, String name, String topic);

	AbstractQuery deleteChannel(int cid, int force);

	AbstractQuery moveChannelTo(int channel, int order);

	AbstractQuery moveClientTo(int channel, int member_cid);

	AbstractQuery moveClientsTo(int channel, List<TsClientDTI> members);

	AbstractQuery renameChannel(int channel, String name);

	AbstractQuery requestClientList();

	AbstractQuery requestChannelList();

	AbstractQuery requestComplainList();

	AbstractQuery registerEventTextServer();

	AbstractQuery registerEventTextChannel();

	AbstractQuery registerEventTextPrivate();

	AbstractQuery registerEventServer();

	AbstractQuery sendGlobalTextMessage(int server_id, String msg);

	AbstractQuery setMyNickname(String nickname);

	void login(String client_login_name, String client_login_password, int virtual_server_id);
	
	void pushCustomQuery(String query);

}