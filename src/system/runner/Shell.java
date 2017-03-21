package system.runner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import system.config.PropertyFileParser;
import system.core.Context;
import system.core.Default;
import system.plugin.PluginLoader;
import system.plugin.PluginService;
import tsxdk.io.TsPipe;
import utility.misc.StringMan;
import utility.profiler.BasicProfiler;

public class Shell {
	private static final Context context = Context.getSharedInstance();
	private static final BasicProfiler profiler = BasicProfiler.getSharedInstance();
	private static final InputStreamReader iStream = new InputStreamReader(System.in);
	private static final BufferedReader consoleInput = new BufferedReader(iStream);
	private static final TsPipe pipe = TsPipe.getSharedInstance();
	private static String lnOutput = "> ";
	private static String lnInput = "< ";
	
	public static void main(String[] args) {
		intro();
		initialize();
		shellLn("Type help to get started");
		mainRepl();
		shellLn("Good bye!");
	}

	private static void mainRepl() {
		String input = "";
		while (!(input = inputLn()).equals("exit")) {
			switch (input) {
			case "help": {help();}break;
			case "query": {queryRepl();}break;
			default : {shellLn("Unknown command: " + input);}break;
			}
		}
	}

	private static void initialize() {
//		AdvancedGlobalBulletin.getSharedInstance().disableAllChannels();
		
		profiler.startProfile("init-config");
		context.initConfig();
		new PropertyFileParser((String)Default.file_system_config.getValue()).parseConfigFile();
		shellLn("Loaded and Initialized config (" + TimeUnit.NANOSECONDS.toMillis(profiler.endProfile().timeLapsedNs) + " ms)");
	    
		PrintStream oldOut = System.out;
		
		System.setOut(new PrintStream(new OutputStream() {
            public void write(int b) {
                //DO NOTHING
            }
        }));
		
		profiler.startProfile("init-plugins");
	    {//Init plugins
	    	context.setPlgService(new PluginService());
			PluginLoader ldr = PluginLoader.getSharedInstance();
			ldr.searchPlugins();
			ldr.mountPlugins();
	    }
	    System.setOut(oldOut);
	    shellLn("Loaded and Initialized plugins (" + TimeUnit.NANOSECONDS.toMillis(profiler.endProfile().timeLapsedNs) + " ms) - not regarding PluginEList!");
	    
	    profiler.startProfile("init-pipe");
		TsPipe.getSharedInstance ().connect ((String)Default.host.getValue(), (int)Default.port.getValue());
		shellLn("Established connection to TS3-Server {host=" +  (String)Default.host.getValue() + ",port=" + (int)Default.port.getValue() + "} (" + TimeUnit.NANOSECONDS.toMillis(profiler.endProfile().timeLapsedNs) + " ms)");
		shellLn("TSxBot environment has been established");
	}
	
	private static void queryRepl() {
		String tsInput = "";
		shellLn("Everything you enter now will be sent to the connected TS3-Server");
		shellLn("The answer from the TS-Server is printed as soon as it comes in");
		shellLn("Type exit to break the query-r.e.p.l.");
//		for (int i = 0; i < 18; ++i) {
//			pipe.in();
//		}
		
		lnOutput = ">> ";
		lnInput = "<< ";
		while (!(tsInput = inputLn()).equals("exit")) {
			pipe.out(tsInput);
			String tsOutput = pipe.in();
			if (tsOutput != null && !tsOutput.equals("null") && !StringMan.isEmptyString(tsOutput))
				shellLn(tsOutput);
			else
				shellLn("[No output from TsPipe]");
		}
		
		lnOutput = "> ";
		lnInput = "< ";
		
		shellLn("Exiting query-r.e.p.l.");
	}
	
	private static void help() {
		helpLn("help", "Display this helptext");
		
		helpLn("query", "Start a query-r.e.p.l.");
		
		helpLn("exit", "End the shell");
	}

	private static void outLn(String ln) {
		System.out.println(ln);
	}
	
	private static void emptyLn() {
		System.out.println("");
	}

	private static void out(String ln) {
		System.out.print(ln);
	}

	private static void shellLn(String ln) {
		outLn(lnOutput + ln);
	}

	private static String inputLn() {
		out(lnInput);
		try {
			return consoleInput.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private static void helpLn(String cmd, String expl) {
		shellLn(cmd + "\t- " + expl);
	}

	private static void intro() {
		logo();
		emptyLn();
		shellLn("Welcome to TSxShell");
		shellLn("Use this to interact with your teamspeak-server or subsystems of TSxBot");
		emptyLn();
	}

	private static void logo() {
		outLn("   ___________      ____        __");
		outLn("  /_  __/ ___/_  __/ __ )____  / /_");
		outLn("   / /  \\__ \\| |/_/ __  / __ \\/ __/");
		outLn("  / /  ___/ />  </ /_/ / /_/ / /_");
		outLn(" /_/  /____/_/|_/_____/\\____/\\__/");
		emptyLn();
	}
}
