package system.plugin.loader;

import java.io.File;
import java.net.URL;

import system.config.PropertyFileParser;

import api.plugin.Plugin;

public class PluginContainer {
	private final String name;
	private final String path;
	private final String entryMethod;
	private final URL url;
	private final String configPath;
	private final PropertyFileParser parser;

	private Class<? extends Plugin>	pluginClass;
	private Plugin					pluginObject;
	private boolean	mounted;
	
	PluginContainer (String name, String path, String entryMethod, URL url) {
		this.name = name;
		this.path = path;
		this.entryMethod = entryMethod;
		this.url = url;
		this.configPath = path + File.separator + "plugin.cfg";
		this.parser = new PropertyFileParser(configPath);
	}
	
	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public String getEntryMethod() {
		return entryMethod;
	}

	public URL getUrl() {
		return url;
	}
	
	public String getConfigPath() {
		return configPath;
	}

	public Class<? extends Plugin> getPluginClass() {
		return pluginClass;
	}

	public void setPluginClass(Class<? extends Plugin> pluginClass) {
		this.pluginClass = pluginClass;
	}

	public Plugin getPluginObject() {
		return pluginObject;
	}

	public void setPluginObject(Plugin pluginObject) {
		this.pluginObject = pluginObject;
	}

	public boolean isFullyIncarnated() {
		return pluginClass != null && pluginObject != null;
	}

	public PropertyFileParser getConfigParser() {
		return parser;
	}

	public boolean getMounted() {
		return mounted;
	}
	
	public void setMounted () {
		mounted = true;
	}
}
