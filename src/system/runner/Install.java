package system.runner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import system.config.configurable.CVar;
import system.config.configurable.CVarManager;
import system.core.Default;
import system.persistence.DBBuilder;
import utility.bulletin.AdvancedGlobalBulletin;
import utility.misc.StringMan;
import utility.profiler.BasicProfiler;
import api.data.CVarDTI;

public class Install {
	private static final BasicProfiler		profiler			= BasicProfiler.getSharedInstance();
	private static final InputStreamReader	iStream				= new InputStreamReader(System.in);
	private static final BufferedReader		consoleInput		= new BufferedReader(iStream);

	private static final String				TS3_INI				= "ts3server.ini";
	private static final String				TS3_IP_WHITELIST	= "query_ip_whitelist.txt";
	private static String					ts3Path				= null;
	private static String					ts3IniPath			= null;
	private static String					ts3IPWLPath			= null;

	private static boolean					testMode			= false;

	// TODO: Add -quiet parameter

	public static void main(String[] args) {
		AdvancedGlobalBulletin.getSharedInstance().disableAllChannels();

		stateOutLn("Install", "Welcome to the guided TsXBot-installation-routine");
		stateOutLn("Install", "Course of installation:");

		if (args.length == 0 || optArg(args, "test")) {
			stateOutLn("Install", "Running in Testmode!");
			args = new String[] { "-all" };
			testMode = true;
		}

		if (optArg(args, "all")) {
			outLn("\t* Configure TS3-Server");
			outLn("\t* Build database");
			outLn("\t* Build config");
			locateTSServer();
			buildConfig();
			buildDatabase();
		} else if (optArg(args, "dbOnly")) {
			outLn("\t* Build database");
			buildDatabase();
		} else if (optArg(args, "cfgOnly")) {
			outLn("\t* Build config");
			buildConfig();
		}

		if (!testMode) {
			stateOutLn("Install", "Installation complete - have fun!");
		} else {
			stateOutLn("Install", "Mock-Installation complete - nothing changed!");
		}

		try {
			consoleInput.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void locateTSServer() {
		stateOutLn("Interconnecting Teamspeak-server");
		outLn("\t* Locate path of TS3-Server");
		outLn("\t* Validate " + TS3_IP_WHITELIST);
		outLn("\t* Read useful information from " + TS3_INI);

		outLn("Please specify the location of your TS3-Server: ");

		boolean pathValid = false;

		do {
			out("\tTS-Server path: ");
			try {
				String path = consoleInput.readLine();
				if (StringMan.isEmptyString(path) || !new File(path).isDirectory()) {
					outLn("The given String is not a valid path! retry");
					if (!testMode) {
						continue;
					}
				}

				stateOutLn("Examine", path);

				out("Trying to locate " + TS3_INI + "\t\t...");
				final File tsIni = new File(path + File.separator + TS3_INI);
				if (!tsIni.exists() && !testMode) {
					outLn("\tFAILED");
					outLn("Please retry");
					continue;
				} else if (testMode) {
					outLn("[MOCK]\tSUCCEEDED");
				} else {
					pathValid = true;
					ts3Path = path;
					ts3IniPath = ts3Path + File.separator + TS3_INI;
					((CVar) Default.path_ts3server).parse(path);
					outLn("\tSUCCEEDED");
				}

				out("Trying to locate " + TS3_IP_WHITELIST + " ...");
				final File tsIpWhiteList = new File(path + File.separator + TS3_IP_WHITELIST);
				if (!tsIpWhiteList.exists() && !testMode) {
					outLn("\tFAILED");
				} else if (testMode) {
					outLn("[MOCK]\tSUCCEEDED");
				} else {
					ts3IPWLPath = path + File.separator + TS3_IP_WHITELIST;
					outLn("\tSUCCEEDED");
				}
			} catch (Exception e) {
				if (!testMode) {
					e.printStackTrace();
				}
			}
		} while (!(pathValid || testMode));

		{
			try {
				stateOut("Validate", TS3_IP_WHITELIST + "\t...");
				String content;
				if (!testMode) {
					content = new String(Files.readAllBytes(new File(ts3IPWLPath).toPath()));
				} else {
					content = "127.0.0.1";
					out("[MOCK]");
				}
				if (content.contains("127.0.0.1")) {
					outLn("\tSUCCEEDED");
					((CVar) Default.host).parse("127.0.0.1");
				} else {
					outLn("\tFAILED");
					outLn("IP whitelist " + ts3IPWLPath + " does not contain localhost");
					outLn("This is mandatory due to massive query amount caused by TSxBot");
				}
			} catch (IOException e) {
				if (!testMode) {
					e.printStackTrace();
				}
			}
		}

		{
			stateOut("Extract", TS3_INI + "\t\t...");
			if (!testMode) {
				Properties tsIni = new Properties();
				try (final BufferedInputStream stream = new BufferedInputStream(new FileInputStream(ts3IniPath))) {
					tsIni.load(stream);
				} catch (IOException e) {
					if (!testMode) {
						e.printStackTrace();
					}
				}
				((CVar) Default.port).parse(tsIni.getProperty("query_port"));
				outLn("\tSUCCEEDED");
			} else {
				outLn("[MOCK]\tSUCCEEDED");
			}

		}
	}

	private static final void buildDatabase() {
		stateOutLn("Database", "Installing");
		File dbFile = new File("TSXBOT.db");

		if (dbFile.exists()) {
			stateOutLn("Database", "Preexisting database-file detected, deleting");
			if (!testMode) {
				if (dbFile.delete() == false) {
					stateOutLn("Database", "Could not delete database-file!");
				}
			} else {
				stateOutLn("Database", "Will not delete database-file since we are in testmode!");
			}
		}

		stateOutLn("Database", "Building");

		if (!testMode) {
			profiler.startProfile("build-database");

			DBBuilder builder = new DBBuilder();
			builder.buildDB();

			stateOutLn("Database", "Built in " +
					TimeUnit.NANOSECONDS.toMillis(profiler.endProfile().timeLapsedNs) +
					" ms");
			stateOutLn("Database", "DONE");
		} else {
			stateOutLn("Database", "[MOCK]DONE");
		}
	}

	private static final void buildConfig() {
		stateOutLn("Config", "Building");
		demandConfig();
		stateOutLn("Config", "All variables have been configured");
		final String sysCfgFilePath = Default.file_system_config.getValue();

		if (!testMode) {
			stateOutLn("Config", "Now writing config-file (" + sysCfgFilePath + ")");
			profiler.startProfile("write-syscfg");
			CVarManager.getSharedInstance().dumpCVar();
			stateOutLn("Config", "File was written in " +
					TimeUnit.NANOSECONDS.toMillis(profiler.endProfile().timeLapsedNs) +
					" ms");
			stateOutLn("Config", "DONE");
		} else {
			stateOutLn("Config", "Now I would try and write (" + sysCfgFilePath + ")");
			stateOutLn("Config", "Since we are in testmode, I will show you the configured CVars");

			out(CVarManager.getSharedInstance().getStateView());

			stateOutLn("Config", "[MOCK]DONE");
		}
	}

	private static void demandConfig() {
		stateOutLn("Config", "CVar configuration");
		stateOutLn("Config", "Enter new value or leave blank to approve default, press enter to confirm");
		stateOutLn("Config", "WARNING: No validation is done, if you enter something wrong your installation will be broken!");

		demandVariable(Default.query_nickname);
		demandVariable(Default.brain_beat);
		demandVariable(Default.client_login_name);
		demandVariable(Default.client_login_password);
	}

	private static void demandVariable(CVarDTI cVar) {
		CVar var = (CVar) cVar;
		stateOut("Config", "Configuring \"" + var.getIdentifer() + "\", default value: [" + var.getValue() + "]; New value: ");

		try {
			String content = consoleInput.readLine();
			if (!StringMan.isEmptyString(content)) {
				try {
					var.parse(content);
				} catch (Exception e) {
					stateOutLn("Error", "Wrong format, retry");
					demandVariable(cVar);
					return;
				}
				stateOutLn("Config", "Var: \"" + var.getIdentifer() + "\" set to: [" + var.getValue() + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final void outLn(String text) {
		System.out.println(text);
	}

	private static final void out(String text) {
		System.out.print(text);
	}

	private static final void stateOutLn(String task, String state) {
		outLn(AdvancedGlobalBulletin.createBullet(state, new String[] { "Task", task }));
	}

	private static final void stateOutLn(String state) {
		outLn(AdvancedGlobalBulletin.createBullet(state, new String[] { "Task" }));
	}

	private static final void stateOut(String task, String state) {
		out(AdvancedGlobalBulletin.createBullet(state, new String[] { "Task", task }));
	}

	private static final boolean optArg(String[] args, String arg) {
		for (String current : args) {
			if (current.equals("-" + arg))
				return true;
		}
		return false;
	}
}
