package system.plugin;

import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.util.logging.Logger;

import system.core.Context;

import api.plugin.Plugin;
import api.service.BotServices;
import bot.BotException;

final class PluginContainer {
	private boolean					active;
	private final String			entryMethod;
	private final String			path;
	private Class<? extends Plugin>	pluginClass;
	private Plugin					pluginObject;
	private final String			name;
	final static Logger				log	= Context.getSharedInstance().getLog();

	protected PluginContainer(String path, String entryMethod) {
		// System.err.println("private PluginContainer() called");
		this.path = path;
		this.entryMethod = entryMethod;
		this.name = entryMethod.substring(entryMethod.indexOf('.') + 1);
		// System.out.println("Plugin-name: " + name);
		this.active = false;
	}

	// public PluginContainer(Class<? extends Plugin> plugin) {
	// this();
	// this.pluginClass = plugin;
	// }

	protected String getEntryMethod() {
		return this.entryMethod;
	}

	protected String getPath() {
		return this.path;
	}

	protected Class<? extends Plugin> getPluginClass() {
		return this.pluginClass;
	}

	public Plugin incarnatePlugin() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		Class<? extends Plugin> clazz = getPluginClass();

		if (clazz != null) {
			pluginObject = clazz.getDeclaredConstructor(BotServices.class).newInstance(Context.getSharedInstance().getPlgService());
		}

		return pluginObject;
	}

	protected boolean isActive() {
		return this.active;
	}

	@SuppressWarnings("unchecked")
	// TODO Checking type safety of the cast from Class<capture#3-of ?> to Class<? extends Plugin>
	//This should be inside PluginLoader
	protected void loadPluginClass(final URLClassLoader loader) {
		log.finest("Try to load " + this.entryMethod);
		try {
			this.pluginClass = ((Class<? extends Plugin>) loader.loadClass(this.entryMethod));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new BotException("PluginContainer.loadPluginClass", "e1",
					"Could not load Plugin-class " + this.entryMethod, e);
		}
	}

	protected void setActive(boolean bActive) {
		this.active = bActive;
	}

	public Plugin getPluginObject() {
		return pluginObject;
	}

	// @SuppressWarnings("unused")
	// private void setEntryMtd(String entryMethod) {
	// this.entryMethod = entryMethod;
	// }

	// @SuppressWarnings("unused")
	// private void setPath(String path) {
	// this.path = path;
	// }
}