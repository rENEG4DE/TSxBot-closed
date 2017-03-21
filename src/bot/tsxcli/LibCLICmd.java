package bot.tsxcli;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import system.config.configurable.CVar;
import system.config.configurable.CVarManager;
import system.core.Context;
import system.core.CoreModule;
import system.core.CoreModuleExecutor;
import system.plugin.PluginLoader;
import tsxdk.entity.EntityManager;
import tsxdk.entity.LibTsEvent;
import utility.misc.StringMan;
import api.data.TsChannelDTI;
import api.data.TsClientDTI;
import api.data.TsComplainDTI;
import api.service.AbstractCLICommand;
import bot.EventServer;
import bot.permission.PermissionManager;

public enum LibCLICmd implements AbstractCLICommand {
	/*
	 * Commands to implement
	 * whois
	 * whoami
	 * top => outputs debug information right to the console
	 * botinfo => information about the bot (with nice ascii art)
	 * hash
	 * plugin start / stop / restart
	 */

	SET {
		@Override
		public void execute(Map<String, Object> fields) {
			final CLIService cmdSrv = CLIService.getSharedInstance();
			final TsClientDTI emitter = LibTsEvent.TEXTMESSAGE.getClient(); // emitter for permission-check
			final PermissionManager permMgr = PermissionManager.getSharedInstance();
			
			if (!permMgr.checkHasPermission(emitter.getClid(),"USE_CLI_SET_CMD")) {
				cmdSrv.echo("-- insufficient permissions\n-- missing permission: " + permMgr.getLastMissingPermission());
				return;
			}
			
			final CLIService cmdService = CLIService.getSharedInstance();
			String cvar = (String) fields.get("cvar");
			String val = (String) fields.get("value");
			CVar var = (CVar) CVarManager.getSharedInstance().getCVar(cvar);
			
			// Check if parms set
			if (var == null) {
				cmdService.reportCommandError("Unknown CVar:", new String[] { "PreparedUserCommand", "Set" }, new Object[] { cvar });
				return;
			}

			if (val == null) {
				cmdService.reportCommandError("Parameter not set! This is fatal and may not have happened!", new String[] { "PreparedUserCommand", "Set" },
						new Object[] { cvar, "null" });
				return;
			}

			// Set the value!
			try {
				var.parse(val);
			} catch (Exception e) {
				e.printStackTrace();
				cmdService.reportCommandError("An exception occured:", new String[] { "PreparedUserCommand", "Set" }, new Object[] { e.getMessage() });
			}
		}

		@Override
		public String getInfoString() {
			return "Sets a specified CVar";
		}
	},

	GET {
		@Override
		public void execute(Map<String, Object> fields) {
			final CLIService cmdService = CLIService.getSharedInstance();
			String cvar = (String) fields.get("cvar");
			CVar var = (CVar) CVarManager.getSharedInstance().getCVar(cvar);

			// Check if parms set
			if (var == null) {
				cmdService.reportCommandError("Unknown CVar:",
						new String[] { "PreparedUserCommand", "Get" },
						new Object[] { cvar });
				return;
			}

			// Set the value
			cmdService.echo(cvar + " := " + var.getValue());
		}

		@Override
		public String getInfoString() {
			return "Returns the value of a specified CVar";
		}
	},

	LIST {
		@Override
		public void execute(Map<String, Object> fields) {
			String list = (String) fields.get("list");
			final CLIService cmdService = CLIService.getSharedInstance();

			switch (list) {
				case "cmd": {
					StringBuilder builder = new StringBuilder(list + ": {\n");
					for (CLICommandObject cmd : cmdService.getCommands()) {
						builder.append("\t");
						builder.append(cmd.getCommand().getId());
						builder.append(" ");
						builder.append(cmdService.getFieldsFor(cmd.getCommand().getId()));
						builder.append("\n");
					}
					builder.append("}");
					cmdService.echo(builder.toString());
				}
					break;
				case "cvar": {
					StringBuilder builder = new StringBuilder(list + ": {\n");
					builder.append("\t");
					builder.append(CVarManager.getSharedInstance().getStateView().replace("\n", "\n\t"));
					builder.append("}");
					cmdService.echo(builder.toString());
				}
					break;
				case "query": {
					StringBuilder builder = new StringBuilder(list + ": {\n");
					builder.append("\t");
					builder.append(Context.getQueryAgent().getStateView().replace("\n", "\n\t"));
					builder.append("}");
					cmdService.echo(builder.toString());
				}
					break;
				case "client": {
					StringBuilder builder = new StringBuilder(list + "(id,type,dbid,nickname): {\n");
					for (TsClientDTI client : EntityManager.getSharedInstance().getClientList().getIterable()) {
						builder.append("\t");
						builder.append(String.format("%-4d", client.getClid()));
						builder.append(", ");
						builder.append(String.format("%-2d", client.getClient_type()));
						builder.append(", ");
						builder.append(String.format("%-4d", client.getClient_Database_id()));
						builder.append(", ");
						builder.append(StringMan.makeFixedLen(client.getClient_Nickname(), 50));
						builder.append("\n");
					}
					builder.append("}");
					cmdService.echo(builder.toString());
				}
					break;
				case "channel": {
					StringBuilder builder = new StringBuilder(list + "(id,name): {\n");
					for (TsChannelDTI channel : EntityManager.getSharedInstance().getChannelList().getIterable()) {
						builder.append("\t");
						builder.append(String.format("%-4d", channel.getCid()));
						builder.append(", ");
						builder.append(channel.getChannel_name());
						builder.append("\n");
					}
					builder.append("}");
					cmdService.echo(builder.toString());
				}
					break;
				case "complain": {
					StringBuilder builder = new StringBuilder(list + "(sub,obj,text): {\n");
					for (TsComplainDTI client : EntityManager.getSharedInstance().getComplainList().getIterable()) {
						builder.append("\t");
						// Kann Erstellungsdatum von complains nicht auslesen ?!
						DateFormat format = new SimpleDateFormat("DD-MM-YYYY hh:mm:ss");
						format.setTimeZone(TimeZone.getTimeZone("GMT+0"));
						builder.append(format.format(client.getTimestamp()));
						builder.append(", ");
						builder.append(client.getFname());
						builder.append(", ");
						builder.append(client.getTname());
						builder.append(", ");
						builder.append(client.getMessage());
						builder.append("\n");
					}
					builder.append("}");
					cmdService.echo(builder.toString());
				}
					break;
				case "plugin": {
					cmdService.echo(list + PluginLoader.getSharedInstance().getStateView().replace("\n", "\n\t"));
				}
					break;
				default: {
					cmdService.reportCommandError("Unknown list",
							new String[] { "PreparedUserCommand", "Set" },
							new Object[] { list });
					cmdService.echo(getInfoString());
				}
			}
		}

		@Override
		public String getInfoString() {
			return "Possible arguments: channel, client, cmd, complain, cvar, plugin, query";
		}
	},

	INFO {
		@Override
		public void execute(Map<String, Object> fields) {
			String cmdLbl = ((String) fields.get("cmd")).toUpperCase();
			CLIService cmdSrv = CLIService.getSharedInstance();

			if (cmdSrv.getCommand(cmdLbl) == null) {
				cmdSrv.reportCommandError("Unknown command", new String[] { "PreparedUserCommand", "Info" }, new String[] { cmdLbl });
				return;
			}

			AbstractCLICommand cmd = cmdSrv.getCommand(cmdLbl).getCommand();
			cmdSrv.echo(cmd.getId() + ": " + cmd.getInfoString());
		}

		@Override
		public String getInfoString() {
			return "Additional information on specified command";
		}
	},

	UPTIME {
		@Override
		public void execute(Map<String, Object> fields) {
			final CLIService cmdSrv = CLIService.getSharedInstance();
			final SimpleDateFormat fmtUptime = new SimpleDateFormat("DD, HH:mm:ss");
			final SimpleDateFormat fmtStartTime = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
			
			fmtUptime.setTimeZone(TimeZone.getTimeZone("GMT+0"));
			fmtStartTime.setTimeZone(TimeZone.getDefault());
			
			cmdSrv.echo("uptime: day# " + fmtUptime.format(new Date(Context.getSharedInstance().getRtMillis())) + ", started: "
					+ fmtStartTime.format(Context.getSharedInstance().getBootMillis()));
		}

		@Override
		public String getInfoString() {
			return "Displays how long and since when TSxBot has been up";
		}
	},

	WHOAMI {
		@Override
		public void execute(Map<String, Object> fields) {
			TsClientDTI emitter = LibTsEvent.TEXTMESSAGE.getClient();
			CLIService cmdSrv = CLIService.getSharedInstance();

			String lastConnect = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date(emitter.getClient_lastconnected() * 1000L));

			String firstConnect = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
					.format(new Date(emitter.getClient_created() * 1000L));

			cmdSrv.echo("You are {"
					+ "\n\t" + "name = " + emitter.getClient_Nickname()
					+ "\n\t" + "Clid = " + emitter.getClid()
					+ "\n\t" + "Cid = " + emitter.getCid()
					+ "\n\t" + "DBID = " + emitter.getClient_Database_id()
					+ "\n\t" + "Last Connected = " + lastConnect
					+ "\n\t" + "Total Connections = " + emitter.getClient_totalconnections()
					+ "\n\t" + "First Connection = " + firstConnect
					+ "\n\t" + "Connection connected time = " + emitter.getConnection_connected_time()
					+ "\n}");
		}

		@Override
		public String getInfoString() {
			return "Displays some information about yourself";
		}
	},

	THREAD {
		@Override
		public void execute(Map<String, Object> arguments) {
			final CLIService cmdSrv = CLIService.getSharedInstance();
			final TsClientDTI emitter = LibTsEvent.TEXTMESSAGE.getClient(); // emitter for permission-check
			final PermissionManager permMgr = PermissionManager.getSharedInstance();
			
			if (!permMgr.checkHasPermission(emitter.getClid(),"USE_CLI_THREAD_CMD")) {
				cmdSrv.echo("-- insufficient permissions\n-- missing permission: " + permMgr.getLastMissingPermission());
				return;
			}
			
			final String mode = arguments.get("mode").toString().toUpperCase();
			final String threadLbl = arguments.get("thread").toString().toUpperCase();
			
			if (!isValidMode(mode)) {
				cmdSrv.echo("-- invalid mode: " + mode);
				return;
			}

			final StringBuilder builder = new StringBuilder("You selected: " + threadLbl);

			if (!isCoreThread(threadLbl) && !threadLbl.equals("ALL")) {
				builder.append("\n-- unknown thread!");
				builder.append("\nAvailable threads are: {");
				for (CoreModule mdl : CoreModule.values()) {
					if (!mdl.getFutureRef().isDone())
						builder.append("\n\t" + mdl.name());
				}
			} else {
				if (!threadLbl.equals("ALL")) {
					CoreModule mdl = CoreModule.valueOf(threadLbl);
					switch (mode) {
						case "START": {
							if (!mdl.isAttached()) {
								builder.append("-- starting " + threadLbl + "!");
								CoreModuleExecutor.getSharedInstance().attach(threadLbl);
							} else {
								builder.append("-- " + threadLbl + " is already running!");
							}
						}
							break;
						case "STOP": {
							if (mdl.isAttached()) {
								builder.append("-- stopping " + threadLbl + "!");
								CoreModuleExecutor.getSharedInstance().detach(threadLbl);
							} else {
								builder.append("-- " + threadLbl + " is not running, can't stop!");
							}
						}
							break;
						case "RESTART": {
							builder.append("-- restarting " + threadLbl + "!");
							if (mdl.isAttached()) {
								CoreModuleExecutor.getSharedInstance().detach(threadLbl);
							}
							CoreModuleExecutor.getSharedInstance().attach(threadLbl);
						}
							break;
					}
				} else {
					switch (mode) {
						case "START": {
							for (CoreModule mdl : CoreModule.values()) {
								if (!mdl.isAttached()) {
									builder.append("\n-- starting " + mdl.name() + "!");
									CoreModuleExecutor.getSharedInstance().attach(mdl.name());
								} else {
									builder.append("\n-- " + mdl.name() + " is already running!");
								}
							}
						}
							break;
						case "STOP": {
							for (CoreModule mdl : CoreModule.values()) {
								if (mdl.isAttached()) {
									builder.append("\n-- stopping " + mdl.name() + "!");
									CoreModuleExecutor.getSharedInstance().detach(mdl.name());
								} else {
									builder.append("\n-- " + mdl.name() + " is not running, can't stop!");
								}
							}
						}
							break;
						case "RESTART": {
							for (CoreModule mdl : CoreModule.values()) {
								builder.append("\n-- restarting " + mdl.name() + "!");
								if (mdl.isAttached()) {
									CoreModuleExecutor.getSharedInstance().detach(mdl.name());
								}
								CoreModuleExecutor.getSharedInstance().attach(mdl.name());
							}
						}
							break;
					}
				}
			}

			cmdSrv.echo(builder.toString());
		}

		private boolean isCoreThread(String thread) {
			for (CoreModule mdl : CoreModule.values()) {
				if (mdl.name().equals(thread))
					return true;
			}
			return false;
		}

		private boolean isValidMode(String mode) {
			mode = mode.toUpperCase();
			List<String> list = Arrays.asList("START", "STOP", "RESTART");
			return list.contains(mode);
		}

		@Override
		public String getInfoString() {
			return "Control the core-threads - start, stop or restart. Think before you act!";
		}
	},

	VERSION {

		@Override
		public void execute(Map<String, Object> arguments) {
			CLIService.getSharedInstance().echo(Context.getSharedInstance().getVersionString());
		}

		@Override
		public String getInfoString() {
			return "Prints the currently running version of TSxBot -- warning: may not be up-to-date!";
		}

	},

	PLUGIN {

		@Override
		public void execute(Map<String, Object> arguments) {
			final CLIService cmdSrv = CLIService.getSharedInstance();
			final TsClientDTI emitter = LibTsEvent.TEXTMESSAGE.getClient(); // emitter for permission-check
			final PermissionManager permMgr = PermissionManager.getSharedInstance();
			
			if (!permMgr.checkHasPermission(emitter.getClid(),"USE_CLI_THREAD_CMD")) {
				cmdSrv.echo("-- insufficient permissions\n-- missing permission: " + permMgr.getLastMissingPermission());
				return;
			}
			
			// plugin=%s" , "action=%s {start|stop}
			final String pluginName = arguments.get("plugin").toString().toUpperCase();
			final String action = arguments.get("action").toString().toUpperCase();

			EventServer eventServer = EventServer.getSharedInstance();

			if (!pluginName.equals("ALL")) {

				if (!eventServer.isRegisteredPlugin(pluginName)) {
					cmdSrv.echo(" -- unknown Plugin: \"" + pluginName + "\"");
					return;
				}

				switch (action) {
					case "START": {
						if (eventServer.restartClient(pluginName)) {
							cmdSrv.echo(" -- started plugin : \"" + pluginName + "\"");
						} else {
							cmdSrv.echo(" -- unable to start plugin : \"" + pluginName + "\"");
						}
					}
						break;
					case "STOP": {
						if (eventServer.suspendClient(pluginName)) {
							cmdSrv.echo(" -- stopped plugin : \"" + pluginName + "\"");
						} else {
							cmdSrv.echo(" -- unable to stop plugin : \"" + pluginName + "\"");
						}
					}
						break;
					default: {
						cmdSrv.echo(" -- invalid action: \"" + action + "\"");
					}
				}
			}
		}

		@Override
		public String getInfoString() {
			return "Control plugins - start or stop -- list available plugins with !list plugin";
		}

	};

	public String getId() {
		return name();
	}
}
