package api.query;

public enum LibTsParm {
	TARGETMODE	("targetmode"),
	TARGET ("target"),
	MSG ("msg"),
	CLID ("clid"),
	CID ("cid"),
	REASONID ("reasonid"),
	REASONMSG ("reasonmsg"),
	UID ("uid"),
	AWAY ("away"),
	VOICE ("voice"),
	GROUPS ("groups"),
	TIMES ("times"),
	COUNTRY ("country"),
	BANREASON ("banreason"),
	SGID ("sgid"),
	CLDBID ("cldbid"),
	CHANNEL_ORDER ("channel_order"),
	CHANNEL_NAME ("channel_name"),
	CHANNEL_TOPIC ("channel_topic"),
	CPID ("cpid"),
	CHANNEL_FLAG_PERMANENT("channel_flag_permanent"),
	CHANNEL_FLAG_MAXFAMILYCLIENTS_INHERITED("channel_flag_maxfamilyclients_inherited"),
	CHANNEL_CODEC_QUALITY("channel_codec_quality"),
	FORCE ("force"), 
	TOPIC ("topic"), 
	FLAGS ("flags"), 
	LIMITS ("limits"),
	EVENT ("event"), 
	CLIENT_NICKNAME ("client_nickname");
	
	private final String parm;
	
	private LibTsParm (String parm) {
		this.parm = parm;
	}
	
	public String getValue () {
		return parm;
	}
}