package utility.systemInfo;

public class SystemInfoElement {
	private final String subSys;
	private final String about;
	private final String[] info;
	
	public SystemInfoElement(String sub, String about, String... info) {
		this.subSys = sub;
		this.about = about;
		this.info = info;
	}
	
	public String toString() {
		if (info.length == 1) {
			return "[" + subSys + "]" + "." + about + ":\t" + info[0];
		} else {
			StringBuilder builder = new StringBuilder();
			builder.append("[" + subSys + "]" + "." + about + ": {\n");
			for (String nfo : info) {
				builder.append("\t" + nfo + "\n");
			}
			builder.append("}");
			return builder.toString();
		}
	}
}
