package system.runner;

import java.util.Arrays;

public class Start {
	public static void main(String[] args) {
		if (args.length == 0) {
			printUsage ();
			return;
		}
		
		String startArg = args[0];
		
		if (args.length == 1) {
			args = new String [] {""};
		} else {
			args = Arrays.copyOfRange(args, 1, args.length);
		}
		
		switch (startArg) {
		case "-install": {
			system.runner.Install.main(args);
			break;
			}
		case "-run": {
			system.runner.Execute.main(args);
			break;
			}
		case "-shell": {
			if (args.length <= 1) {
				args = new String [] {"",""};
			}
			system.runner.Shell.main(args);
			break;
		}
		default: {
			System.out.println("Unknown argument: " + args[0]);
			printUsage();
		}
		}
	}

	private static void printUsage() {
		System.out.println("Usage:\n"
				+ "\t-install\t\t:Set up environment and database\n"
				+ "\t-run\t\t\t:Execute the bot\n"
				+ "\t-shell\t\t:Execute the TSxShell\n"
				+ "\t-var {VarName}={Value}\t:Execute with CVar [VarName] set to [Value] (Not yet implemented)\n");
	}
}
