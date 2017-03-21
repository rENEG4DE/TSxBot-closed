package system.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import utility.systemInfo.SystemInfo;
import utility.systemInfo.SystemInfoElement;
import utility.systemInfo.SystemObject;

public class ResourceManager implements SystemObject {
	private final static class SingletonHolder {
		private final static ResourceManager INSTANCE = new ResourceManager();
	}
	
	public final static ResourceManager getSharedInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private ResourceManager () {
		resources = new HashMap<> ();
		SystemInfo.getSharedInstance().registerSystemObject(this);
	}
	
	
	Map<String, AbstractResource[]> resources;
	
	@Override
	public SystemInfoElement getSystemInfo() {
		StringBuilder builder = new StringBuilder();
		
		for (Entry<String, AbstractResource[]> entry : resources.entrySet()) {
			builder.append("\t");
			builder.append(entry.getKey());
			builder.append("has " + entry.getValue().length + " resources");
			builder.append("\n");
		}
		
		return new SystemInfoElement("ResourceManager", "state", 
				builder.toString());
	}


	public void addResource (String name, AbstractResource... sources) {
		if (sources.length == 0) {
			throw new IllegalArgumentException();
		}
		
		resources.put(name, sources);
	}
	
	public void addFileResource (String name, String... files) {
		if (files.length == 0) {
			throw new IllegalArgumentException();
		}
		
		AbstractResource res[] = new FileResource[files.length]; 
		int i = 0;
		
		for (String file : files) {
			res[i++] = new FileResource(file);
		}

		addResource(name, res);
	}
	
	/**
	 * @param name of the wanted resource
	 * @return the first reader that is available for this resource
	 */
	public AbstractResource getResource (String name) {
		for (AbstractResource current : resources.get(name)) {
			if (current.isAvailable()) {
				return current;
			}
		}
		return null;
	}

	/**
	 * purge all resources
	 */
	public void reset() {
		for (AbstractResource[] currentAry : resources.values()) {
			for (AbstractResource current : currentAry) {
				current.close();
			}
		}
		resources.clear();
	}
	
	//TODO: create-Methods for different Resources !
	
}
