package system.core;

import java.util.logging.Level;

import system.config.configurable.CVarFactory;
import system.config.configurable.CVarManager;
import system.config.type.StringVar;
import api.data.CVarDTI;

public final class Default {
	public static final CVarDTI	file_system_config;
	public static final CVarDTI	file_plugin_blacklist;
	public static final CVarDTI	file_sysinfo;
	public static final CVarDTI	query_nickname;
	public static final CVarDTI	host;
	public static final CVarDTI	path_plugins;
	public static final CVarDTI	path_log;
	public static final CVarDTI	port;
	public static final CVarDTI	brain_beat;
	public static final CVarDTI	auto_report_exception;		// Rethink this
	public static final CVarDTI	trap_unused_clients;
	public static final CVarDTI	resend_faulty_query;
	public static final CVarDTI	path_ts3server;
	public static final CVarDTI	client_login_name;
	public static final CVarDTI	client_login_password;
	public static final CVarDTI	bot_client_db_id;
	public static final CVarDTI	virtual_server_id;

	/**
	 * Sets up system-config CVars
	 */
	static {

		final CVarManager cVarMgr = CVarManager.getSharedInstance();

		// defaulted system-cfgs
		cVarMgr.registerCVar(new StringVar("log_level", "finest", value -> {
			Level level = Level.parse(((String) value).toUpperCase());
			Context.getSharedInstance().getLog().setLevel(level);
		}));

		/** The query_nickname. */
		cVarMgr.registerCVar(file_system_config = CVarFactory.createStringVar("file_system_config", "./conf/systemconfig.txt"));
		cVarMgr.registerCVar(file_plugin_blacklist = CVarFactory.createStringVar("file_plugin_blacklist", "./conf/PluginEList.txt"));
		cVarMgr.registerCVar(file_sysinfo = CVarFactory.createStringVar("file_sysinfo", "./log/sysinfo.txt"));
		cVarMgr.registerCVar(query_nickname = CVarFactory.createStringVar("query_nickname", "TSXBOT"));
		cVarMgr.registerCVar(host = CVarFactory.createStringVar("host", "127.0.0.1"));
		cVarMgr.registerCVar(path_plugins = CVarFactory.createStringVar("path_plugins", "./plugins/"));
		cVarMgr.registerCVar(path_log = CVarFactory.createStringVar("path_log", "./log/"));
		cVarMgr.registerCVar(port = CVarFactory.createIntegerVar("port", 10011));
		cVarMgr.registerCVar(brain_beat = CVarFactory.createIntegerVar("brain_beat", 1000));
		cVarMgr.registerCVar(auto_report_exception = CVarFactory.createBoolVar("auto_report_exception", true));
		cVarMgr.registerCVar(trap_unused_clients = CVarFactory.createBoolVar("trap_unused_clients", true));
		cVarMgr.registerCVar(resend_faulty_query = CVarFactory.createBoolVar("resend_faulty_query", true));
		// cVarMgr.registerCVar(CVarFactory.createBoolVar("redirStdOut", false));
		// non-defaulted system-cfgs
		cVarMgr.registerCVar(path_ts3server = CVarFactory.createStringVar("path_ts3server", "null"));
		cVarMgr.registerCVar(client_login_name = CVarFactory.createStringVar("client_login_name", "null"));
		cVarMgr.registerCVar(client_login_password = CVarFactory.createStringVar("client_login_password", "null"));
		cVarMgr.registerCVar(bot_client_db_id = CVarFactory.createIntegerVar("id_bot_client_db", 0));
		cVarMgr.registerCVar(virtual_server_id = CVarFactory.createIntegerVar("id_virtual_server", 0));
	}
}
