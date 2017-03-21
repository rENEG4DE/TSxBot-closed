package api.plugin;

import api.data.EventDTI;
import api.service.BotServices;
import api.util.ActionFactoryUtil;
import api.util.CLIServiceUtil;
import api.util.CVarUtil;
import api.util.ChannelUtil;
import api.util.ClientUtil;
import api.util.OptUtil;
import api.util.QueryProvider;


public abstract class Plugin {
//	private static int VERSION = 1; 
//	private Log log;
	private final String pluginName;
	protected final BotServices service;
	protected final ChannelUtil channelUtil;
	protected final ClientUtil clientUtil;
	protected final QueryProvider queryUtil;
	protected final CVarUtil cVarUtil;
	protected final OptUtil optUtil;
	protected final CLIServiceUtil cliUtil;
	protected final ActionFactoryUtil actionUtil;

	protected Plugin(String plugName, BotServices service) {
		this.pluginName = plugName;
		this.service =	service;
		service.init(this);
		
		//init subsystems
		channelUtil = service.getProvider(ChannelUtil.class);
		clientUtil = service.getProvider(ClientUtil.class);
		queryUtil = service.getProvider(QueryProvider.class);
		cVarUtil = service.getProvider(CVarUtil.class);
		optUtil = service.getProvider(OptUtil.class);
		cliUtil = service.getProvider(CLIServiceUtil.class);
		actionUtil = service.getProvider(ActionFactoryUtil.class);
		
		initConfig();
	}

	abstract public EventClient[] getEventClients();
	abstract protected void initConfig();
	
	protected final String createCVarID (String identifier) {
		return getPluginName() + "." + identifier;
	}

	protected final Object getModCVarVal (String identifier) {
		return cVarUtil.getCVar(pluginName,identifier).getValue();
	}

	public final String getPluginName() { 
		return pluginName;
	}

	public void onClientJoined(EventDTI data) {
		//	System.out.println("empty onClientJoined called");
	}

	public void onClientLeft(EventDTI data) {
		// System.out.println("empty onClientLeft called");
	}

	public void onComplainExpired(EventDTI data) {
		// System.out.println("empty onComplainExpired called");
	}

	public void onComplainNew(EventDTI data) {
		// System.out.println("empty onComplainNew called");
	}

	public void onTextMessage(EventDTI data) {
		// System.out.println("empty onTextMessage called");
	}

	public void onBrainBeat(EventDTI data) {
//		 System.out.println("empty onBrainBeat called");
	}

}