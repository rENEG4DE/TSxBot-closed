package system.core;

//** this part is auto generated dont touch this

// Revision: $Revision: 631 $
// Last modified: $Date: 2014-01-13 21:18:08 +0100 (Mo, 13 Jan 2014) $
// Last modified by: $Author: KoRnHolio $

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import system.config.configurable.CVarManager;
import system.plugin.PluginService;
import tsxdk.io.query.engine.QueryAgent;
import tsxdk.io.query.engine.QueryEngine;
import utility.exclusionlist.ExclusionListManager;
import utility.log.Log;
import api.data.CVarDTI;

/**
 * Im Context werden Objekte und Methoden hinterlegt die global verf�gbar sein
 * sollen.
 * 
 * @author $Author: KoRnHolio $
 */

public final class Context {

	private static class SingletonHolder {
		private static final Context INSTANCE = new Context();
	}
	
	private final Long bootMillis = System.currentTimeMillis();

	private final CVarManager cVarMgr = CVarManager.getSharedInstance();
	private final static String REVISION = "$Revision: 0.7 $";
	private final static String VERSION_STRING = "Arcturus -- " + REVISION + " -- 201501150044" ;
	private Core core;
	private int majorRev = 1;
	private int minorRev = 0;
	
	//Logger - Attribute
	private Log log;
	private Log tsLog;
	
	//Pluginservice
	PluginService plgService;
	
	public String getVersionString() {
		return VERSION_STRING;
	}

	public PluginService getPlgService() {
		return plgService;
	}

	public void setPlgService(PluginService plgService) {
		this.plgService = plgService;
	}

	/**
	 * Privater Konstruktor, der Context wird mit der Methode {@link getSharedInstance()} besorgt!
	 */
	private Context () {
		log = new Log ("system",Level.INFO, Log.Target.SHARED);
		tsLog = new Log ("teamspeak",Level.FINE, Log.Target.FILE);
		
		
		{	//Filter the minor and major-rev
    		Matcher m = Pattern.compile("\\d+").matcher(REVISION);
    		m.find();
    		int version = Integer.parseInt(m.group());
    		majorRev = (version - (version % 100)) / 100;
        	minorRev = version - majorRev * 100;
		}
	}
	
	/**
	 * Methode zum zugreifen auf den Kontext
	 * @return Die Einzelst�ck-Instanz des Kontext
	 */
	public static Context getSharedInstance () {
		return SingletonHolder.INSTANCE;
	}
	
	public static QueryAgent getQueryAgent() {
		return QueryEngine.getSharedInstance();
	}

	public Core getCore() {
		return core;
	}

	public void setCore(Core core) {
		this.core = core;
	}
	
	/**
	 * Returns the time since Context was initialized 
	 * that's the best we can get at determining boot-up time of the bot
	 * @return The time in ms since Bot was started as Long
	 */
	public Long getRtMillis () {
		return System.currentTimeMillis() - bootMillis; 
	}
	
	public Long getBootMillis () {
		return bootMillis;
	}

//	public <T> CVar<T> getCVar (String identifier) {
//		return (CVar<T>) cVarMgr.getCVar(identifier);
//	}
	
	public void registerCVar (CVarDTI configurable) {
		cVarMgr.registerCVar(configurable);
	}

	public Log getLog() {
		return this.log;
	}

	public Log getTsLog() {
		return this.tsLog;
	}

	public int getMajorRev() {
		return majorRev;
	}

	public int getMinorRev() {
		return minorRev;
	}
	
	public String getRevString () {
		return majorRev + "." + minorRev;
	}

	/**
	 * Initialize Config-Variables
	 */
	public void initConfig () {
		log.printSection("Initializing Config-variables");
		log.printSeperator();
	}
	
	/**
	 * Sets up Exclusion-lists
	 */
	void initELists () {
		log.printSection("Initializing Exclusion-lists");
		
		ExclusionListManager mgr = ExclusionListManager.getSharedInstance();
		
		mgr.addBlackList("PLUGIN", (String)Default.file_plugin_blacklist.getValue());
		
		mgr.addVirtualBlackList("CVAR_VIEW", 
									"client_login_name", 
									"client_login_password");
		
//		System.out.println(mgr.getStateView());
		
		log.printSeperator();
	}

//	public void initQueryCommandRegistry() {
//		final String[] posDesc = new String[]{"Context","initQueryCommandRegistry"};
//		final String command_list_filename = (String) Default.file_query_command_list.getValue();
//		final QueryCommandRegistry qcr = QueryCommandRegistry.getSharedInstance();
//		final AdvancedGlobalBulletin bulletin = AdvancedGlobalBulletin.getSharedInstance();
//		
//		int cmdCount = 0;
//
//		if ((cmdCount = qcr.parseFile(command_list_filename)) == 0) {
//			bulletin.SYSTEM.Error.push("No commands found in query_command_list",
//					posDesc,
//					new Object[]{command_list_filename});
//		} else {
//			bulletin.SYSTEM.Info.push("Commands in query_command_list", 
//					posDesc,
//					new Object[] { command_list_filename, cmdCount });
//		}
//	}
}
