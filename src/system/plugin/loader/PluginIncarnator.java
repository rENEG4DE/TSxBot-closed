package system.plugin.loader;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.List;

import system.core.Context;

import api.plugin.Plugin;
import api.service.BotServices;
import bot.BotException;

public class PluginIncarnator {
	private final PluginManager	mgr;

	public PluginIncarnator (PluginManager manager) { 
			this.mgr = manager;
	}
	
	public void incarnateAll () {
		final PrivilegedAction<URLClassLoader>	classLoaderCreate = () -> {
			List<URL> list = mgr.getUrlList();
			return new URLClassLoader(list.toArray(new URL[list.size()]));
		};
		
		final URLClassLoader loader = AccessController.doPrivileged(classLoaderCreate);
		
		for (PluginContainer current : mgr.getContainers()) {
			incarnatePlugin (current, loader);
		}
	}
	
	private void incarnatePlugin(final PluginContainer container, final URLClassLoader loader) {
		try {
			if (!container.isFullyIncarnated ()) {
				@SuppressWarnings("unchecked")
				final Class<? extends Plugin> pluginClass = (Class<? extends Plugin>) loader.loadClass(container.getEntryMethod());
				
				container.setPluginClass(pluginClass);
				container.setPluginObject(pluginClass.getDeclaredConstructor(BotServices.class).newInstance(Context.getSharedInstance().getPlgService()));
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			throw new BotException("PluginContainer.loadPluginClass", "e1",
					"Could not load Plugin-class " + container.getEntryMethod(), e);
		}
	}
	
}
