package tsxdk.entity;

import utility.systemInfo.SystemInfoElement;
import utility.systemInfo.SystemObject;
import api.data.TsClientDTI;

//import tsxdk.entity.meta.EntityList;

//An entity is everything we get from teamspeak except Returns - even unknown stuff is stored as entity!

public class EntityManager implements SystemObject {
	private final static class SingletonHolder {
		private final static EntityManager INSTANCE = new EntityManager ();
	}
	
	public final static EntityManager getSharedInstance () {
		return SingletonHolder.INSTANCE;
	}
	

	@Override
	public SystemInfoElement getSystemInfo() {
		return new SystemInfoElement("EntityManager", "state",
				"Client-count: " + clients.getIterable().length,
				"Channel-count: " + channels.getIterable().length,
				"Complain-count: " + complains.getIterable().length);
	}
	
	private TsClientList clients;
	private TsChannelList channels;
	private TsComplainList complains;
	
	private TsClient botClient;

	private EntityManager () {
		channels = new TsChannelList();
		clients = new TsClientList();
		complains = new TsComplainList();
	}
	
	public TsChannelList getChannelList () {
		return channels;
	}

	public TsClientList getClientList() {
		return clients;
	}

	public TsComplainList getComplainList() {
		return complains;
	}
	
	void setBotClient(TsClient client) {
		this.botClient = client; 
	}
	
	public TsClientDTI getBotClient() {
		return botClient;
	}

	public void clearEntityCache() {
		clients.clearCache();
		channels.clearCache();
		complains.clearCache();
	}
}
