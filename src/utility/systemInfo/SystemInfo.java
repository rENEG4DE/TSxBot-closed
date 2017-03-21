package utility.systemInfo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import system.core.Default;

/**
 * Offers methods to inform about the internal state of the complete System
 *
 */
public class SystemInfo {
	private final static class SingletonHolder {
		private final static SystemInfo INSTANCE = new SystemInfo();
	}
	
	public final static SystemInfo getSharedInstance () {
		return SingletonHolder.INSTANCE;
	}
	
	private final List<SystemObject> sysObjects;
	
	private SystemInfo() {
		sysObjects = new CopyOnWriteArrayList<>();
	}
	
	public void registerSystemObject (SystemObject obj) {
		sysObjects.add(obj);
	}
	
	public void dumpInfos (String fileName) {
		try (final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)))) {
			for (SystemObject obj : sysObjects) {
				out.println(obj.getSystemInfo().toString());
			}
			printSeparator(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void printSeparator(final PrintWriter out) {
		out.println("********************************************************************************");
	}

	public void pushExceptionInfo(Map<String, Object> exceptionInfo) {
		try (final PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(Default.path_log.getValue() + "exception.txt", true)))) {
			out.println("Exception-info: " + exceptionInfo.get("About"));
			exceptionInfo.remove("About");
			out.println("{");
			for (Map.Entry<String, Object> entry : exceptionInfo.entrySet()) {
				out.println("\t" + entry.getKey() + " :\t" + entry.getValue());
			}
			printSeparator(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
