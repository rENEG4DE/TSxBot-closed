package bot.tsxcli;

import java.util.HashMap;
import java.util.Map;

import tsxdk.entity.LibTsEvent;
import tsxdk.io.BufferedTsPrompt;
import utility.bulletin.AdvancedGlobalBulletin;
import api.service.AbstractCLICommand;

public class CLIService {
	private final static class SingletonHolder {
		public final static CLIService INSTANCE = new CLIService ();
	}

	private Map<String,CLICommandObject> cmdList;
	
	private TextMessageModel context;
	
	public TextMessageModel getContext() {
		return context;
	}

	private CLIService () {
		//System.out.println("UserCommandService()");
		cmdList = new HashMap<>();
		registerPreparedCommands();
	}
	
	private void registerPreparedCommands() {		
		registerCommand(LibCLICmd.SET, "cvar=%s {any cvar - for a full list type !list cvar}", "value=%s");
		registerCommand(LibCLICmd.GET, "cvar=%s {any cvar - for a full list type !list cvar}");
		registerCommand(LibCLICmd.LIST, "list=%s {channel|client|cmd|complain|cvar|plugin|query} ");
		registerCommand(LibCLICmd.INFO, "cmd=%s {any command}");
		registerCommand(LibCLICmd.UPTIME);
		registerCommand(LibCLICmd.WHOAMI);
		registerCommand(LibCLICmd.VERSION);
		registerCommand(LibCLICmd.PLUGIN, "action=%s {start|stop}", "plugin=%s" );
		registerCommand(LibCLICmd.THREAD, "mode=%s {start|stop|restart}", "thread=%s {all|threadname}");
		
	}

	public final static CLIService getSharedInstance() {
		//System.out.println("getSharedInstance()");
		return SingletonHolder.INSTANCE;
	}

	public void parseTextMessageEvent() {
		//System.out.println("parseTextMessageEvent()");
		TextMessageModel context = new TextMessageModel(LibTsEvent.TEXTMESSAGE); 
		this.context = context;
		String message = context.getMsg();
		
		AdvancedGlobalBulletin.getSharedInstance().USRCMD.Info.push(
				"parsing Textmessage target, invokerId, targetMode, invokerName, invokerUid, message", 
				new String[]{"UserCommandService","parseTextMessageEvent"}, 
				new Object[]{context.getTarget(), context.getInvokerid(), context.getTargetmode(), context.getInvokername(), context.getInvokeruid(), message});
		
		//Recognize Command
		
		if (message.startsWith("!")) {			
			String parts[] = message.split(" ", 2);
			String cmd = parts[0].substring(1).toUpperCase();
			String arg = (parts.length == 2 ? parts[1] : null);
//			System.out.println("cmd, arg:" + cmd + "," + arg);
			executeCommand(cmd, arg);
		}
	}
	
	private void executeCommand (String cmd, String arg) {
		//System.out.println("executeCommand(" + cmd + "," + arg + ")");
		if (!cmdList.containsKey(cmd)) {
			reportCommandError("Unknown command", 
					new String[] {"UserCommandService", "executeCommand"},
					new Object[] {cmd});
		} else {
			((CLICommandObject) cmdList.get(cmd)).execute(arg);
		}
	}

	public void registerCommand (AbstractCLICommand cmd , String... expectedSymbols) {
		cmdList.put(cmd.getId().toUpperCase(), new CLICommandObject(cmd, expectedSymbols));
	}

	public void reportCommandError(String description, String[] posDesc,Object[] values) {
		String text = AdvancedGlobalBulletin.createBullet(description, new String[]{"ERROR"} , values);
		echo (text);
	}

	/**
	 * Outputs to the client that caused the last Textmessage
	 * @param text
	 */
	public void echo (String text) {
		BufferedTsPrompt.getSharedInstance().prompt(
				LibTsEvent.TEXTMESSAGE.getClient(), 	//<<prüfen! könnte sein das in der zwischenzeit ein anderer ne Textmessage geschrieben hat
				">>\n" + text + "\n<<"
			);
	}

	public Iterable<CLICommandObject> getCommands() {
		return cmdList.values();
	}

	public Map<String, String> getFieldsFor(String cmd) {
		return cmdList.get(cmd).fields;
	}

	public CLICommandObject getCommand(String cmd) {
		return cmdList.get(cmd);
	}
}
