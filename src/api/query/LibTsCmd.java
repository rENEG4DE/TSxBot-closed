package api.query;

public enum LibTsCmd {
	MESSAGE ("sendtextmessage"),
	CLIENTMOVE("clientmove"),
	CLIENTKICK("clientkick"),
	CLIENTPOKE("clientpoke"),
	CLIENTBAN("banclient"),
	ADDCLIENT2GROUP("servergroupaddclient"),
	DELCLIENTFROMGROUP("servergroupdelclient"),
	CHANNELMOVE("channeledit"),
	CHANNELCREATE("channelcreate"),
	CHANNELDELETE("channeldelete"),
	CHANNELRENAME("channeledit"), 
	SENDTEXTMESSAGE("sendtextmessage"), 
	CLIENTLIST ("clientlist"),
	CHANNELLIST ("channellist"), 
	COMPLAINLIST ("complainlist"),
	SERVERNOTIFYREGISTER ("servernotifyregister"), 
	CLIENTUPDATE ("clientupdate");
	
	private final String command;
	
	private LibTsCmd (String command) {
		this.command = command;
	}
	
	public String getValue() {
		return command;
	}
}