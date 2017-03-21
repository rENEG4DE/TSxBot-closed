package bot;

import java.util.HashMap;
import java.util.Map;

import utility.bulletin.AdvancedGlobalBulletin;
import utility.systemInfo.SystemInfo;
import utility.systemInfo.SystemInfoElement;
import utility.systemInfo.SystemObject;
import api.data.EventDTI;
import api.plugin.EventClient;
import api.plugin.Plugin;
import bot.tsxcli.CLIService;

public final class EventServer implements SystemObject{

	private final Map<String,EventClient> clients = new HashMap<>();
	private final Map<String,EventClient> suspendedClients = new HashMap<>();
	
	private final static AdvancedGlobalBulletin bulletin = AdvancedGlobalBulletin
			.getSharedInstance();

	private static class SingletonHolder {
		private static final EventServer INSTANCE = new EventServer();
	}

	private int brainBeatCtr = 0;
	private int clientJoinCtr = 0;
	private int clientLeaveCtr = 0;
	private int textMessageCtr = 0;
	private int complainCommitCtr = 0;
	private int complainExpireCtr = 0;

	@Override
	public SystemInfoElement getSystemInfo() {
		return new SystemInfoElement("EventServer", "eventCounter", 
				"Brain-beats: " + brainBeatCtr,
				"Text-messages: " + textMessageCtr,
				"Clients-joined: " + clientJoinCtr,
				"Clients-left: " + clientLeaveCtr,
				"Complain-new: " + complainCommitCtr,
				"Complain-old: " + complainExpireCtr);
	}

	
	private EventServer() {
		SystemInfo.getSharedInstance().registerSystemObject(this);
	}

	public static EventServer getSharedInstance() {
		return SingletonHolder.INSTANCE;
	}

	public void addClient(String name, EventClient client) {
		clients.put(name, client);
	}

//	public void removeClient(String name) {
//		clients.remove(name);
//	}

	public boolean suspendClient(String name) {		
		for (EventClient current : clients.values()) {
			if (((Plugin)current).getPluginName().toUpperCase().equals(name.toUpperCase())) {
				final Plugin choice = (Plugin) current;
				suspendedClients.put(choice.getPluginName(), clients.remove(choice.getPluginName()));
				return true;
			}
		}
		return false;
	}
	


	public boolean isRegisteredPlugin(String pluginName) {
		for (EventClient current : clients.values()) {
			if (((Plugin)current).getPluginName().toUpperCase().equals(pluginName.toUpperCase())) {
				return true;
			}
		}
		for (EventClient current : suspendedClients.values()) {
			if (((Plugin)current).getPluginName().toUpperCase().equals(pluginName.toUpperCase())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean restartClient(String name) {		
		for (EventClient current : suspendedClients.values()) {
			if (((Plugin)current).getPluginName().toUpperCase().equals(name.toUpperCase())) {
				final Plugin choice = (Plugin) current;
				clients.put(choice.getPluginName(), suspendedClients.remove(choice.getPluginName()));
				return true;
			}
		}
		return false;
	}
	
	public void startSuspendedClients() {
		for (EventClient current : suspendedClients.values()) {
			final Plugin choice = (Plugin) current;
			clients.put(choice.getPluginName(), suspendedClients.remove(choice.getPluginName()));
		}
	}
	
	public void triggerBrainBeat(EventDTI data) {
		brainBeatCtr++;
		try {
			for (EventClient client : clients.values()) {
				client.onBrainBeat(data);
			}
		} catch (Exception e) {		//Bug: Exception is caught when Exception is not thrown
			e.printStackTrace();
			throw new BotException("EventServer.triggerBrainBeat", "e1",
					"An exception was thrown by an event-client");
		}
	}

	private static final String[] BULLETIN_POS_DESC = new String[] {
			"EventServer", "triggerEvent" };

	public void triggerClientJoined(EventDTI data) {
		bulletin.EVENT.Info.push("A Client has joined", BULLETIN_POS_DESC, new String[]{data.getClient().getClient_Nickname()});
		clientJoinCtr++;
		try {
			for (EventClient client : clients.values()) {
				client.onClientJoined(data);
			}
		} catch (Exception e) {		//Bug: Exception is caught when Exception is not thrown
			e.printStackTrace();
			throw new BotException("EventServer.triggerClientJoined", "e1",
					"An exception was thrown by an event-client");
		}
	}

	public void triggerClientLeft(EventDTI data) {
		clientLeaveCtr++;
		bulletin.EVENT.Info.push("A Client has left", BULLETIN_POS_DESC, new String[]{data.getClient().getClient_Nickname()});
		try {
			for (EventClient client : clients.values()) {
				client.onClientLeft(data);
			}
			
		} catch (Exception e) {	//Bug: Exception is caught when Exception is not thrown
			e.printStackTrace();
			throw new BotException("EventServer.triggerClientLeft", "e1",
					"An exception was thrown by an event-client");
		}
	}

	public void triggerComplainNew(EventDTI data) {
		complainCommitCtr++;
		bulletin.EVENT.Info.push("A Complain was committed", BULLETIN_POS_DESC);
		try {
			for (EventClient client : clients.values()) {
				client.onComplainNew(data);
			}
		} catch (Exception e) {	//Bug: Exception is caught when Exception is not thrown
			e.printStackTrace();
			throw new BotException("EventServer.triggerComplainNew", "e1",
					"An exception was thrown by an event-client");
		}
	}

	public void triggerComplainExpired(EventDTI data) {
		complainExpireCtr++;
		bulletin.EVENT.Info.push("A Complain has expired", BULLETIN_POS_DESC);
		try {
			for (EventClient client : clients.values()) {
				client.onComplainExpired(data);
			}
		} catch (Exception e) {	//Bug: Exception is caught when Exception is not thrown
			e.printStackTrace();
			throw new BotException("EventServer.triggerComplainExpired", "e1",
					"An exception was thrown by an event-client");
		}
	}

	public void triggerTextMessage(EventDTI data) {
		textMessageCtr++;
		bulletin.EVENT.Info.push("A Text-message was posted", BULLETIN_POS_DESC);
		try {
			CLIService.getSharedInstance().parseTextMessageEvent();
			for (EventClient client : clients.values()) {
				client.onTextMessage(data);
			}
		} catch (Exception e) {	//Bug: Exception is caught when Exception is not thrown
			e.printStackTrace();
			throw new BotException("EventServer.triggerTextMessage", "e1",
					"An exception was thrown by an event-client");
		}
	}
}
