package system.core;

/*notes:
 * Search 	\n[^//.]*System\.out\. 		For any System.out. that is active
 */

import system.config.PropertyFileParser;
import system.plugin.PluginService;
import system.plugin.loader.PluginFinder;
import system.plugin.loader.PluginIncarnator;
import system.plugin.loader.PluginManager;
import system.plugin.loader.PluginMounter;
import tsxdk.io.TsPipe;
import utility.log.Log;
import bot.EventServer;

final public class Core {
	private final Context context= Context.getSharedInstance();
	
	private final Log log = context.getLog();
	
	public Core () {
		log.printSection("Initializing Core");
		context.setCore(this);

	    //Load and Parse system-config!
		context.initConfig();
		new PropertyFileParser((String)Default.file_system_config.getValue()).parseConfigFile();

//		context.initQueryCommandRegistry();
		context.initELists();

//	    {//Init plugins	OLD
//	    	context.setPlgService(new PluginService());
//			PluginLoader ldr = PluginLoader.getSharedInstance();
//			ldr.searchPlugins();
//			ldr.mountPlugins();
//	    }
		
		{//Init Plugins
			context.setPlgService(new PluginService());
			final PluginManager pluginMgr = new PluginManager();
			final PluginFinder pluginFndr = new PluginFinder ();
			final PluginIncarnator pluginInc = new PluginIncarnator(pluginMgr);
			
			pluginFndr.addPath((String)Default.path_plugins.getValue());
			pluginMgr.addAll(pluginFndr.findPlugins());
			pluginInc.incarnateAll();
			
			final PluginMounter pluginMnt = new PluginMounter (pluginMgr, EventServer.getSharedInstance());
			pluginMnt.mountPlugins();
		}

		TsPipe.getSharedInstance ().connect ((String)Default.host.getValue(), (int)Default.port.getValue());

		{//Init Bot-threads.
			CoreModuleExecutor mdlAttacher = CoreModuleExecutor.getSharedInstance();	//Just take any instance here
			mdlAttacher.attachAll();
		}

		log.printSeperator();
	}
}
