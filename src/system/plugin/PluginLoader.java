package system.plugin;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import system.config.PropertyFileParser;
import system.core.Context;
import system.core.Default;
import utility.exclusionlist.ExclusionListManager;
import utility.misc.Debuggable;
import utility.systemInfo.SystemInfo;
import utility.systemInfo.SystemInfoElement;
import utility.systemInfo.SystemObject;
import api.plugin.EventClient;
import api.plugin.Plugin;
import bot.BotException;
import bot.EventServer;

/*
 * Classes for redesign:
 * 
 * PluginIncarnator		Initialisert ein Plugin und lädt die Config
 * PluginFinder			Sucht Plugins und erzeugt Container
 * PluginContainer		Enthält alle namen und pfade eines Plugins, zusätzlich die Referenz auf die Instanz und die Klasse
 * PluginContainerManager	Führt eine Map von Plugins und sorgt dafür as jedes Plugin nur einmal existiert
 * PluginMounter		Lädt die Plugins in den EventServer mit einem gegebenen Manager
 */

/*
 * Anforderungen an das System
 * 
 * Plugins in gegebenen Verzeichnissen suchen
 * Plugins in den Eventserver einhängen
 * Zur laufzeit neue Plugins einhängen können 
 */

public class PluginLoader implements Debuggable, SystemObject {
	private final static class SingletonHolder {
		private final static PluginLoader	INSTANCE	= new PluginLoader();
	}

	private static final String						PLUGINCFGNAME	= "plugin.cfg";
	// private static final int PLUGINVERSION = 1;
	// private JDBCWrapper jdbcWrapper = null;
	private URLClassLoader							loader;
	private final static Logger						log				= Context.getSharedInstance().getLog();
	private String									pluginPath;
	private final List<PluginContainer>				plugins			= new ArrayList<>();
	private final List<URL>							urlLst			= new ArrayList<>();
	private final PrivilegedAction<URLClassLoader>	urlCol			= () -> new URLClassLoader(urlLst.toArray(new URL[urlLst.size()]));

	public PluginLoader () {
		pluginPath = (String) Default.path_plugins.getValue();
		SystemInfo.getSharedInstance().registerSystemObject(this);
	}
	
	public final static PluginLoader getSharedInstance () {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * Validates a given path and adds it to urls
	 * maybe this will be moved to searchPlugins
	 * @param path the root-plugin-path (plugins/ExamplePlugin/ExamplePlugin.jar)
	 * @param plugin the plugin's name
	 */
	public void loadPlugin(String path,String plugin) {
		String fName = path + File.separator + plugin + ".jar";
		String entryMethod = plugin.substring(0, 1).toLowerCase() + plugin.substring(1) + "." + plugin;
		
		if (!containerExists(path, entryMethod)) {

			log.finer("Creating plugin-container for " + fName);

			log.finest("Entry-Method = " + entryMethod);

			// System.out.println("EntryMethod = " + entryMethod);

			try {
				this.urlLst.add(((new File(fName)).toURI().toURL()));
			} catch (MalformedURLException e) {
				e.printStackTrace();
				throw new BotException("PluginService.loadPlugin", "e1",
						"Malformed URL! " + plugin, e);
			}

			this.plugins.add(new PluginContainer(path, entryMethod));
		} else
			log.finer("Already have a container for " + entryMethod + ", omitted!");
	}

	private boolean containerExists(String path, String entryMethod) {
		for (final PluginContainer container : this.plugins) {
			if (container.getPath().equals(path) && container.getEntryMethod().equals(entryMethod))
				return true;
		}
		return false;
	}

	/**
	 * Mounts all unmounted Plugins
	 */
	public void mountPlugins() {
		log.finer("Mounting plugins...");
		this.loader = AccessController.doPrivileged(urlCol);
		for (PluginContainer container : this.plugins) {
			if (!container.isActive()) {
				container.loadPluginClass(this.loader);
				// Create instance of this Plugin
				final Plugin plugin;
				try {
					log.finest("Create instance of plugin-class " + container.getEntryMethod());
					plugin = container.incarnatePlugin();
//					Try to load config
					{
						PropertyFileParser parser = new PropertyFileParser(container.getPath() + File.separator + PLUGINCFGNAME);
						parser.parseModuleConfigFile(plugin.getPluginName());
					}
				} catch (InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e) {
					e.printStackTrace();
					throw new BotException("mountPlugins", "e1",
							"Failed to mount Plugin, " + e.toString(), e);
				}

				for (EventClient client : plugin.getEventClients()) {
					System.out.println (
							"Container {\n"
							+ "\t path: " + container.getPath() + "\n"
							+ "\t entryMethod: " + container.getEntryMethod() + "\n"
							+ "\t class: " + container.getPluginClass() + "\n"
							+ "\t instance: " + container.getPluginObject() + "\n"
							+ "}"
							);
					
					log.fine("Starting plugin *" + plugin.getPluginName() + "*");
					container.setActive(true);
					EventServer.getSharedInstance().addClient(plugin.getPluginName(),client);
				}
			}
		}
	}
	
	public void searchPlugins() {
		log.finest("Searching for plugins in " + this.pluginPath);
		File[] files = new File(this.pluginPath).listFiles();
		
		  if (files != null) { // Erforderliche Berechtigungen etc. sind vorhanden
			for (final File current : files) {
				final String directory = current.getPath();
				final String name = current.getName();
				if (ExclusionListManager.getSharedInstance().getList("PLUGIN").isExcluded(name)) {
					log.fine("Plugin " + name + " omitted");
					continue;
				}
				final String jarPath = directory + File.separator + name + ".jar";
				if (current.isDirectory()) {
//					log.finest("Folder found: " + directory);

					File f = new File(jarPath);
					if (f.exists()) {

						loadPlugin(directory, name);
					} else {
						log.finest("Folder " + directory + " does not contain \" "+ name + ".jar\"");
					}
				} else {
//					log.finest("File " + name + " found, but it's not in a folder so we will not load it. :-P");
				}
			}
		}
	}


	@Override
	public String getStateView() {
		StringBuilder builder = new StringBuilder("(plugin, active): {\n");
		
		for (PluginContainer plugin : plugins) {
			builder.append(plugin.getPluginObject().getPluginName());
			builder.append(", ");
			builder.append(plugin.isActive());
			builder.append("\n");
		}
		
		builder.append("}");
		
		return builder.toString();
	}

	@Override
	public SystemInfoElement getSystemInfo() {
		StringBuilder builder = new StringBuilder();
		
		for (PluginContainer plugin : plugins)  {
			builder.append("\t");
			builder.append(plugin.getEntryMethod());
			builder.append(" : active: ");
			builder.append(plugin.isActive());
			builder.append("\n");
		}
		
		return new SystemInfoElement("PluginLoader", "state", 
				builder.toString());
	}
}
