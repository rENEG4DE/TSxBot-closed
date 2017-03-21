package system.plugin.serviceprovider;

import java.util.List;

import system.core.Context;
import tsxdk.io.query.BuildQuery;
import api.data.TsClientDTI;
import api.data.TsEntityType;
import api.query.AbstractPreparableQuery;
import api.query.AbstractQuery;
import api.query.LibTsCmd;
import api.query.LibTsParm;
import api.util.QueryProvider;

public class QueryProviderObject implements QueryProvider {

	private static final AbstractPreparableQuery	sendPrivateMessageToClient;
	private static final AbstractPreparableQuery	kickClient;
	private static final AbstractPreparableQuery	pokeClient;
	private static final AbstractPreparableQuery	banClient;
	private static final AbstractPreparableQuery	addClientToGroup;
	private static final AbstractPreparableQuery	removeClientFromGroup;
	private static final AbstractPreparableQuery	createSubChannel;
	private static final AbstractPreparableQuery	moveChannelTo;
	private static final AbstractPreparableQuery	moveClientTo;
	private static final AbstractPreparableQuery	renameChannel;
	private static final AbstractPreparableQuery	requestClientList;
	private static final AbstractPreparableQuery	requestChannelList;
	private static final AbstractPreparableQuery	requestComplainList;
	private static final AbstractPreparableQuery	registerEventTextServer;
	private static final AbstractPreparableQuery	registerEventTextChannel;
	private static final AbstractPreparableQuery	registerEventTextPrivate;
	private static final AbstractPreparableQuery	registerEventServer;
	private static final AbstractPreparableQuery	sendGlobalTextMessage;
	private static final AbstractPreparableQuery	setMyNickname;
	private static final AbstractPreparableQuery	deleteChannel;

	static {
		sendPrivateMessageToClient = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.MESSAGE)
				.With(LibTsParm.TARGETMODE, 1)
				.With(LibTsParm.TARGET, 0)
				.With(LibTsParm.MSG, "")
				.Expecting(TsEntityType.TSRETURN)
				.Preparable()
				.Publish();

		kickClient = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.CLIENTKICK)
				.With(LibTsParm.CLID, 0)
				.With(LibTsParm.REASONID, 4)
				.With(LibTsParm.REASONMSG, "")
				.Expecting(TsEntityType.TSRETURN)
				.Preparable()
				.Publish();

		pokeClient = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.CLIENTPOKE)
				.With(LibTsParm.CLID, 0)
				.With(LibTsParm.MSG, "")
				.Expecting(TsEntityType.TSRETURN)
				.Preparable()
				.Publish();

		banClient = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.CLIENTBAN)
				.With(LibTsParm.UID, 0)
				.With(LibTsParm.BANREASON, "")
				.Expecting(TsEntityType.TSRETURN)
				.Preparable()
				.Publish();

		addClientToGroup = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.ADDCLIENT2GROUP)
				.With(LibTsParm.CLDBID, 0)
				.With(LibTsParm.SGID, 0)
				.Expecting(TsEntityType.TSRETURN)
				.Preparable()
				.Publish();

		removeClientFromGroup = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.DELCLIENTFROMGROUP)
				.With(LibTsParm.CLDBID, 0)
				.With(LibTsParm.SGID, 0)
				.Expecting(TsEntityType.TSRETURN)
				.Preparable()
				.Publish();

		createSubChannel = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.CHANNELCREATE)
				.Expecting(TsEntityType.TSSIMPLERESULT)
				.With(LibTsParm.CPID, 0)
				.With(LibTsParm.CHANNEL_CODEC_QUALITY, 0)
				.With(LibTsParm.CHANNEL_FLAG_PERMANENT, 1)
				.With(LibTsParm.CHANNEL_FLAG_MAXFAMILYCLIENTS_INHERITED, 1)
				.With(LibTsParm.CHANNEL_NAME, "")
				.With(LibTsParm.CHANNEL_TOPIC, "\"" + "" + "\"")
				.Preparable()
				.Publish();

		moveChannelTo = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.CHANNELMOVE)
				.With(LibTsParm.CID, 0)
				.With(LibTsParm.CHANNEL_ORDER, 0)
				.Expecting(TsEntityType.TSRETURN)
				.Preparable()
				.Publish();

		moveClientTo = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.CLIENTMOVE)
				.With(LibTsParm.CLID, 0)
				.With(LibTsParm.CID, 0)
				.Preparable()
				.Publish();

		renameChannel = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.CHANNELRENAME)
				.With(LibTsParm.CID, 0)
				.With(LibTsParm.CHANNEL_NAME, "")
				.Preparable()
				.Publish();

		requestClientList = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.CLIENTLIST)
				.With(LibTsParm.AWAY)
				.With(LibTsParm.VOICE)
				.With(LibTsParm.GROUPS)
				.With(LibTsParm.UID)
				.With(LibTsParm.TIMES)
				.With(LibTsParm.COUNTRY)
				.Expecting(TsEntityType.TSCLIENTLIST)
				.Preparable()
				.Publish();

		requestChannelList = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.CHANNELLIST)
				.With(LibTsParm.TOPIC)
				.With(LibTsParm.FLAGS)
				.With(LibTsParm.VOICE)
				.With(LibTsParm.LIMITS)
				.With(LibTsParm.TIMES)
				.With(LibTsParm.COUNTRY)
				.Expecting(TsEntityType.TSCHANNELLIST)
				.Preparable()
				.Publish();

		requestComplainList = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.COMPLAINLIST)
				.Expecting(TsEntityType.TSCOMPLAINLIST)
				.Preparable()
				.Publish();

		registerEventTextServer = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.SERVERNOTIFYREGISTER)
				.With(LibTsParm.EVENT, "textserver")
				.Expecting(TsEntityType.TSRETURN)
				.Preparable()
				.Publish();

		registerEventTextChannel = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.SERVERNOTIFYREGISTER)
				.With(LibTsParm.EVENT, "textchannel")
				.Expecting(TsEntityType.TSRETURN)
				.Preparable()
				.Publish();

		registerEventTextPrivate = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.SERVERNOTIFYREGISTER)
				.With(LibTsParm.EVENT, "textprivate")
				.Expecting(TsEntityType.TSRETURN)
				.Preparable()
				.Publish();

		registerEventServer = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.SERVERNOTIFYREGISTER)
				.With(LibTsParm.EVENT, "server")
				.Expecting(TsEntityType.TSRETURN)
				.Preparable()
				.Publish();

		sendGlobalTextMessage = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.SENDTEXTMESSAGE)
				.With(LibTsParm.TARGETMODE, 3)
				.With(LibTsParm.TARGET, 0)
				.With(LibTsParm.MSG, "")
				.Expecting(TsEntityType.TSRETURN)
				.Preparable()
				.Publish();

		setMyNickname = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.CLIENTUPDATE)
				.With(LibTsParm.CLIENT_NICKNAME, "")
				.Expecting(TsEntityType.TSRETURN)
				.Preparable()
				.Publish();

		deleteChannel = (AbstractPreparableQuery) new BuildQuery(LibTsCmd.CHANNELDELETE)
				.With(LibTsParm.CID, 0)
				.With(LibTsParm.FORCE, 0)
				.Expecting(TsEntityType.TSRETURN)
				.Preparable()
				.Publish();
	}

	@Override
	public AbstractQuery sendPrivateMessageToClient(int clid, String msg) {
		sendPrivateMessageToClient.prepare(1, clid, msg);

		return sendPrivateMessageToClient.copy();
	}

	@Override
	public AbstractQuery kickClient(int clid, String msg) {

		kickClient.prepare(clid, 4, msg);

		return kickClient.copy();
	}

	@Override
	public AbstractQuery pokeClient(int clid, String msg) {
		pokeClient.prepare(clid, msg);

		return pokeClient.copy();
	}

	@Override
	public AbstractQuery banClient(String uid, String msg) {

		banClient.prepare(uid, msg);

		return banClient.copy();
	}

	@Override
	public AbstractQuery addClientToGroup(int dbid, int sgid) {

		addClientToGroup.prepare(dbid, sgid);
		return addClientToGroup.copy();
	}

	@Override
	public AbstractQuery removeClientFromGroup(int dbid, int sgid) {

		removeClientFromGroup.prepare(dbid, sgid);
		return removeClientFromGroup.copy();
	}

	@Override
	public AbstractQuery createSubChannel(int parent, int cdc_quality, String name, String topic) {
		createSubChannel.prepare(parent, cdc_quality, 1, 1, name, "\"" + topic + "\"");

		return createSubChannel.copy();
	}

	@Override
	public AbstractQuery createChannel(int cdc_quality, String name, String topic) {
		return createSubChannel(0, cdc_quality, name, topic);
	}

	@Override
	public AbstractQuery deleteChannel(int cid, int force) {
		deleteChannel.prepare(cid, force);

		return deleteChannel.copy();
	}

	@Override
	public AbstractQuery moveChannelTo(int channel, int order) {
		moveChannelTo.prepare(channel, order);

		return moveChannelTo.copy();
	}

	@Override
	public AbstractQuery moveClientTo(int channel, int member_cid) {
		moveClientTo.prepare(member_cid, channel);

		return moveClientTo.copy();
	}

	@Override
	public AbstractQuery moveClientsTo(int channel, List<TsClientDTI> members) {
		final Integer[] clidList = new Integer[members.size()];

		int i = 0;
		for (TsClientDTI client : members) {
			clidList[i++] = client.getClid();
		}

		return new BuildQuery(LibTsCmd.CLIENTMOVE)
				.WithChained(LibTsParm.CLID, clidList)
				.With(LibTsParm.CID, channel)
				.Publish();
	}

	@Override
	public AbstractQuery renameChannel(int channel, String name) {

		renameChannel.prepare(channel, name);
		return renameChannel.copy();
	}

	@Override
	public AbstractQuery requestClientList() {
		requestClientList.prepare();

		return requestClientList;
	}

	@Override
	public AbstractQuery requestChannelList() {
		requestChannelList.prepare();

		return requestChannelList;
	}

	@Override
	public AbstractQuery requestComplainList() {
		requestComplainList.prepare();

		return requestComplainList;
	}

	@Override
	public AbstractQuery registerEventTextServer() {
		registerEventTextServer.prepare("textserver");

		return registerEventTextServer.copy();
	}

	@Override
	public AbstractQuery registerEventTextChannel() {
		registerEventTextChannel.prepare("textchannel");

		return registerEventTextChannel.copy();
	}

	@Override
	public AbstractQuery registerEventTextPrivate() {
		registerEventTextPrivate.prepare("textprivate");

		return registerEventTextPrivate.copy();
	}

	@Override
	public AbstractQuery registerEventServer() {
		registerEventServer.prepare("server");

		return registerEventServer.copy();
	}

	@Override
	public AbstractQuery sendGlobalTextMessage(int server_id, String msg) {
		sendGlobalTextMessage.prepare(3, server_id, msg);

		return sendGlobalTextMessage.copy();
	}

	@Override
	public AbstractQuery setMyNickname(String nickname) {
		setMyNickname.prepare(nickname);

		return setMyNickname.copy();
	}

	@Override
	public void login(String client_login_name, String client_login_password, int virtual_server_id) {
		Context.getQueryAgent().pushNewQuery("use " + virtual_server_id);
		Context.getQueryAgent().pushNewQuery("login client_login_name=" + client_login_name + " client_login_password=" + client_login_password);
	}

	@Override
	public void pushCustomQuery(String query) {
		Context.getQueryAgent().pushNewQuery(query);
	}
	
	
}
