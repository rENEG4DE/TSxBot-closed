package utility.profiler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import utility.profiler.info.AbstractProfileInfo;
import utility.systemInfo.SystemInfo;
import utility.systemInfo.SystemInfoElement;
import utility.systemInfo.SystemObject;

public class ProfileSet implements SystemObject{
	private final Map<String,AbstractProfileInfo> infoMap = new LinkedHashMap<>();
	private final String name;
	
	public ProfileSet (String name) {
		SystemInfo.getSharedInstance().registerSystemObject(this);
		this.name = name;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Profile " + name + " {\n");
		
		for (Entry<String,AbstractProfileInfo> info : infoMap.entrySet()) {
			builder.append("\t");
			builder.append(info.getValue().getStringRepresentation());
			builder.append("\n");
		}
		
		builder.append("}");
		return builder.toString();
	}
	
	public void addInfo(AbstractProfileInfo info) {
		infoMap.put(info.getName(), info);
	}
	
	public String getName() {
		return name;
	}
	
	public void start () {
		final long time = System.nanoTime();
		for (AbstractProfileInfo info : infoMap.values()) {
			info.start(time);
		}
	}
	
	public void stop () {
		final long time = System.nanoTime();
		for (AbstractProfileInfo info : infoMap.values()) {
			info.stop(time);
		}
	}

	@Override
	public SystemInfoElement getSystemInfo() {
		return new SystemInfoElement("Profile", name, "left out");
//		return new SystemInfoElement("Profile", name, toString());
	}
}
