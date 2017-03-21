package system.plugin.loader;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PluginManager {
	final Map<String,PluginContainer> pluginMap = new HashMap<>();	
	
	public void printContents () {
		for (PluginContainer current : pluginMap.values()) {
			System.out.println (
					"Container {\n"
					+ "\t name: "  + current.getName() + "\n" 
					+ "\t path: " + current.getPath() + "\n"
					+ "\t url: " + current.getUrl() + "\n"
					+ "\t entryMethod: " + current.getEntryMethod() + "\n"
					+ "\t configPath: " + current.getConfigPath() + "\n"
					+ "\t class: " + current.getPluginClass() + "\n"
					+ "\t instance: " + current.getPluginObject() + "\n"
					+ "}"
					);
		}
	}
	
	public void addAll (List<PluginContainer> ctrLst) {
		for (final PluginContainer current : ctrLst) {
			addContainer(current);
		}
	}

	public boolean addContainer (PluginContainer ctr) {
		if (pluginMap.get(ctr.getName()) != null) {
			return false;
		}
		
		pluginMap.put(ctr.getName(), ctr);
		return true;
	}

	public Collection<PluginContainer> getContainers () {
		return pluginMap.values();
	}
	
	public List<URL> getUrlList () {
//		final List<URL> result = new ArrayList<>(pluginMap.size());
//
//		for (final PluginContainer current : pluginMap.values()) {
//			result.add(current.getUrl());
//		}
//		
//		return result;

		return pluginMap.values().stream()
				.map(p -> p.getUrl())
				.collect(Collectors.toList());
	}

	public Collection<PluginContainer> getUnmountedContainers() {
//		Collection<PluginContainer> result = new ArrayList<>();
//		
//		for (final PluginContainer current : pluginMap.values()) {
//			if (!current.getMounted()) {
//				result.add(current);
//			}
//		}
//		
//		return result;
		
		return pluginMap.values().stream()
				.filter(p -> !p.getMounted())
				.collect(Collectors.toList());
	}
}
