package system.plugin;

import system.plugin.serviceprovider.ActionFactoryUtilProvider;
import system.plugin.serviceprovider.CLIServiceUtilProvider;
import system.plugin.serviceprovider.CVarUtilProvider;
import system.plugin.serviceprovider.ChannelUtilProvider;
import system.plugin.serviceprovider.ClientUtilProvider;
import system.plugin.serviceprovider.OptUtilProvider;
import system.plugin.serviceprovider.QueryProviderObject;
import tsxdk.entity.EntityManager;
import utility.misc.StringMan;
import api.action.ActionContainer;
import api.data.TsClientDTI;
import api.plugin.Plugin;
import api.service.BotServices;
import api.service.UtilityProvider;
import api.util.ActionFactoryUtil;
import api.util.CLIServiceUtil;
import api.util.CVarUtil;
import api.util.ChannelUtil;
import api.util.ClientUtil;
import api.util.OptUtil;
import api.util.QueryProvider;
import bot.action.ActionScheduler;
import bot.action.instance.AbstractAction;

/**
 * Implementation of Bot<->Plugin interaction.
 */

final public class PluginService implements BotServices {
	
	@Override
	public void init(Plugin abstractPlugin) {
		// Load abstractPlugins config
	}
	
	@Override
	public String decodeTS3String(String str) {
		return StringMan.decodeTS3String(str); 
	}
	
	@Override
	public String encodeTS3String(String str) {
		return StringMan.encodeTS3String(str);
	}
	
	@Override
	public TsClientDTI getBotClient() {
		return EntityManager.getSharedInstance().getBotClient();
	}

	@Override
	public void handshake(int version) {
		// TODO Auto-generated method stub
	}

	final UtilityProvider	channelUP	= new ChannelUtilProvider();
	final UtilityProvider	queryUP		= new QueryProviderObject();
	final UtilityProvider	clientUP	= new ClientUtilProvider();
	final UtilityProvider	cvarUP		= new CVarUtilProvider();
	final UtilityProvider	optUP		= new OptUtilProvider();
	final UtilityProvider	cliUP		= new CLIServiceUtilProvider();
	final UtilityProvider	actionUP	= new ActionFactoryUtilProvider();
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends UtilityProvider> T getProvider(Class<? extends UtilityProvider> clazz) {
		if (clazz.getName().equals(ChannelUtil.class.getName())) {
			return (T) channelUP;
		} else if (clazz.getName().equals(QueryProvider.class.getName())) {
			return (T) queryUP;
		} else if (clazz.getName().equals(ClientUtil.class.getName())) {
			return (T) clientUP;
		} else if (clazz.getName().equals(CVarUtil.class.getName())) {
			return (T) cvarUP;
		} else if (clazz.getName().equals(OptUtil.class.getName())) {
			return (T) optUP;
		} else if (clazz.getName().equals(CLIServiceUtil.class.getName())) {
			return (T) cliUP;
		} else if (clazz.getName().equals(ActionFactoryUtil.class.getName())) {
			return (T) actionUP;
		}
		
		return null;
	}

	@Override
	public void addAction(ActionContainer action) {
		ActionScheduler.getSharedInstance().addAction((AbstractAction) action);
	}
}
