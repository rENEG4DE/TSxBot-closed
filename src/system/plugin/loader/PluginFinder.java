package system.plugin.loader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
//public void searchPlugins() {
//	log.finest("Searching for plugins in " + this.pluginPath);
//	File[] files = new File(this.pluginPath).listFiles();
//	
//	  if (files != null) { // Erforderliche Berechtigungen etc. sind vorhanden
//		for (final File current : files) {
//			final String directory = current.getPath();
//			final String name = current.getName();
//			if (ExclusionListManager.getSharedInstance().getList("PLUGIN").isExcluded(name)) {
//				log.fine("Plugin " + name + " omitted");
//				continue;
//			}
//			final String jarPath = directory + File.separator + name + ".jar";
//			if (current.isDirectory()) {
////				log.finest("Folder found: " + directory);
//
//				File f = new File(jarPath);
//				if (f.exists()) {
//
//					loadPlugin(directory, name);
//				} else {
//					log.finest("Folder " + directory + " does not contain \" "+ name + ".jar\"");
//				}
//			} else {
////				log.finest("File " + name + " found, but it's not in a folder so we will not load it. :-P");
//			}
//		}
//	}
//}
public class PluginFinder {

	private final List<String> paths = new ArrayList<>();
	
	public void addPath (String path) {
		paths.add(path);
	}
	
	public List<PluginContainer> findPlugins () {
		List<PluginContainer> result = new ArrayList<>();
		
		for (String path : paths) {
			for (File current : new File(path).listFiles()) {
				if (current.isDirectory()) {
					final String directory = current.getPath();
					final String name = current.getName();
					final String jarPath = directory + File.separator + name + ".jar";
					final File file = new File(jarPath);
					if (file.exists()) {
						final String entryMethod = name.substring(0, 1).toLowerCase() + name.substring(1) + "." + name;
						try {
							final URL url = file.toURI().toURL();
							final PluginContainer container = new PluginContainer (name, directory, entryMethod,url);
							result.add(container);
						} catch (MalformedURLException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		return result;
	}
}
