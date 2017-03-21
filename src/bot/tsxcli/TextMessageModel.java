package bot.tsxcli;

import tsxdk.entity.LibTsEvent;

public class TextMessageModel {
	private final int targetmode;
	private final int target;
	private final int invokerid;
	
	private final String invokername;
	private final String invokeruid;
	private final String msg;
	
	private TextMessageModel () {
		targetmode = 0;
		target = 0;
		invokerid = 0;
		
		invokername = null;
		invokeruid = null;
		msg = null;
	}
	
	public TextMessageModel (LibTsEvent textMessage) {
		targetmode = textMessage.getTargetmode();
		target = textMessage.getTarget();
		invokerid = textMessage.getInvokerid();
		
		invokername = textMessage.getInvokername();
		invokeruid = textMessage.getInvokeruid();
		msg = textMessage.getMsg();
	}
	
	public int getTargetmode() {
		return targetmode;
	}

	public int getTarget() {
		return target;
	}

	public int getInvokerid() {
		return invokerid;
	}

	public String getInvokername() {
		return invokername;
	}

	public String getInvokeruid() {
		return invokeruid;
	}

	public String getMsg() {
		return msg;
	}
}
