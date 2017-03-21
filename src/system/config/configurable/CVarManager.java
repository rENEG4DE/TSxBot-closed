package system.config.configurable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.logging.Level;

import utility.log.Log;
import utility.log.Log.Target;
import utility.misc.Debuggable;
import utility.systemInfo.SystemInfo;
import utility.systemInfo.SystemInfoElement;
import utility.systemInfo.SystemObject;
import api.data.CVarDTI;

/**
 * Our manager for all externally configurable Variables
 * 
 * @author kornholio
 * 
 */
public class CVarManager implements Debuggable, SystemObject {
	private static class SingletonHolder {
		private static final CVarManager INSTANCE = new CVarManager();
	}

	private Map<String, CVarDTI> idToCVar = new HashMap<String, CVarDTI>();

	private CVarManager() {
		SystemInfo.getSharedInstance().registerSystemObject(this);
	}
	
	@Override
	public SystemInfoElement getSystemInfo() {
		return new SystemInfoElement("CVarManager", "state", 
				"cvar-count: " + idToCVar.size());
	}

	public static CVarManager getSharedInstance() {
		return SingletonHolder.INSTANCE;
	}

	/**
	 * Validates whether a CVar is registered or not.
	 * 
	 * @param identifier
	 * @return idToCVar.containsKey(identifier)
	 */
	public boolean validateCVar(String identifier) {
		return idToCVar.containsKey(identifier);
	}

	/**
	 * Announce a given CVar
	 * 
	 * @param configurable
	 */
	public void registerCVar(CVarDTI configurable) {
		idToCVar.put(configurable.getIdentifer(), configurable);
	}

	/**
	 * Remove a CVar with name identifier
	 * 
	 * @param identifier
	 */
	public void removeCVar(String identifier) {
		idToCVar.remove(identifier);
	}

	/**
	 * Return a CVar with name Identifier (if existent)
	 * 
	 * @param identifier
	 * @return CVar or null if not found
	 */
	public CVarDTI getCVar(String identifier) {
		return idToCVar.get(identifier);
	}

	/**
	 * List all CVars to a Log-File
	 */
	public void dumpCVar() {
		Log dump = new Log("CVar", Level.FINEST, Target.FILE);
		dump.setUseBasicFormatter();
		dump.info(getStateView());
	}

	public void dumpCVarTo(String fileName) {
		File cfg = new File(fileName);

		if (cfg.exists()) {
			cfg.delete();
		}

		try (final FileWriter fstream = new FileWriter(cfg); 
			final BufferedWriter out = new BufferedWriter(fstream);) {
			out.write("##CVar dump\n");
			out.write(getStateView());
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public String getStateView() {
		StringBuilder builder = new StringBuilder();
		for (String key : new TreeSet<String>(idToCVar.keySet())) {
			builder.append(key);
			builder.append("=");
			builder.append(idToCVar.get(key).toString());
			builder.append("\n");
		}
		return builder.toString();
	}
}
