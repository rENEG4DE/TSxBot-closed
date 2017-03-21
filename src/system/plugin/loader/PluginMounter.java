package system.plugin.loader;

import utility.exclusionlist.ExclusionListManager;
import api.plugin.EventClient;
import bot.EventServer;

public class PluginMounter {
	private final EventServer server;
	private final PluginManager mgr;
	
	public PluginMounter (PluginManager mgr, EventServer server) {
		this.mgr = mgr;
		this.server = server;
	}
	
	public void mountPlugins () {
		for (PluginContainer container : mgr.getUnmountedContainers()) {
			if (ExclusionListManager.getSharedInstance().getList("PLUGIN").isIncluded(container.getName())) {
				for (EventClient clients : container.getPluginObject().getEventClients()) {
					container.getConfigParser().parseModuleConfigFile(container.getName());
					server.addClient(container.getName(), clients);
					container.setMounted();
				}
			}
		}
	}
}
