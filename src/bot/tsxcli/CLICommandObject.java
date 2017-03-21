package bot.tsxcli;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import api.service.AbstractCLICommand;

import utility.bulletin.AdvancedGlobalBulletin;
import utility.misc.StringMan;

/**
 * The fully assembled, ready to execute Command-object
 * 
 */
class CLICommandObject {
	private static CLIService cmdService;		//note: wir können hier keine final nehmen, 
														//da die UserCommandObjects bereits im Konstruktor des Service benötigt werden 
														//und daher hier noch keine Fertige Instanz (=>null) verwendet wird
	AbstractCLICommand cmd;
	String fieldString;
	Map<String, String> fields;
	Map<String, Object> parms;

	// debug-vars
	String latestArgStr; // The last argument-string used to fill parms

	public AbstractCLICommand getCommand() {
		return cmd;
	}	
	
	private CLICommandObject() {
		fields = null;
	}

	/**
	 * A command without parameters
	 * 
	 * @param cmd
	 *            the command without entry-char (default: !)
	 */
	public CLICommandObject(AbstractCLICommand cmd) {
		this();
		this.cmd = cmd;
	}

	/**
	 * A command with parameters
	 * 
	 * @param cmd
	 *            the command without entry-char (default: "!")
	 * @param symbols
	 *            the parameters for this command in the form
	 *            ("cvar=%s","value=%d")
	 */
	// usage: new UserCommandObject(PreparedUserCommand.SET, "cvar=%s",
	// "value=%d")
	public CLICommandObject(AbstractCLICommand cmd, String... symbols) {
		this(cmd);
//		System.out.println("registering");
		if (symbols.length > 0) {
			fieldString = StringMan.join(symbols, " ");
			fields = new LinkedHashMap<>();
			parms = new HashMap<>();
//			System.out.println("UserCommandObject()_createFields: " + Arrays.deepToString(symbols));
			for (String field : symbols) {
				String subFields[] = field.split("=", 2);
				String sub2[] = subFields[1].split(" ");
				fields.put(subFields[0], sub2[0]);
			}
//			System.out.println("UserCommandObject()_createFields DONE!");
		}
//		System.out.println("registering done");
	}

	public void execute() {
//		System.out.println("execute()");
		if (fields == null || (parms != null && parms.size() == fields.size())) 
			cmd.execute(parms);
		else
			AdvancedGlobalBulletin.getSharedInstance().USRCMD.Info.push(
					"Denied execution of usercommand, args", 
					new String[] {"UC", "execute" }, 
					new Object[] { cmd.getId(),	latestArgStr });
		if (parms != null && !parms.isEmpty()) {
			parms.clear();
		}
	}

	public void execute(String arg) {
//		System.out.println("execute(arg)");
		parseFields(arg);
		execute();
	}

	private void parseFields(String argString) {
//		System.out.println("parseFields("+ argString + ")");
		cmdService = CLIService.getSharedInstance();
		// No fields given at all!
		if (fields == null) { 
			if (!StringMan.isEmptyString(argString)) {
				cmdService.reportCommandError(
							"Command does not have fields, ignoring arguments!; command, parms:",
							new String[] { "UserCommadService", "UserCommand", "parseFields" },
							new Object[] { cmd, argString });
			}
			return;
		}
		
		latestArgStr = argString;
		
		if (latestArgStr == null || ((latestArgStr = latestArgStr.trim()).equals(""))) {
			cmdService.reportCommandError(
					"Required arguments:",
					new String[] { "UC", "parseFields" }, 
					new Object[] { fieldString });

			return;
		}

//		cmdService.echo("latestArgStr: \"" + latestArgStr + "\"");
		
		latestArgStr = argString.trim();
		final String args[] = latestArgStr.split("\\s+", fields.size());

		if (latestArgStr.equals("") || args.length < fields.size()) {
			// Check if all parms set

			cmdService.reportCommandError(
					"Required arguments:",
					new String[] { "UC", "parseFields" }, 
					new Object[] { fieldString });

			return;
		}
		
		// Transfer args to parameter-map
		int i = 0;
		for (final Entry<String, String> entry : fields.entrySet()) {
			parms.put(entry.getKey(), String.format(entry.getValue(), args[i++]));
		}
	}
}