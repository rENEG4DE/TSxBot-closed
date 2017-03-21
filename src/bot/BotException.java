package bot;


import java.util.ArrayList;
import java.util.List;

import system.core.Default;
import utility.bulletin.AdvancedGlobalBulletin;

/**
 * TSX-Exception,
 * copied from <a href="http://tutorials.jenkov.com/java-exception-handling/exception-enrichment.html">http://tutorials.jenkov.com/java-exception-handling/exception-enrichment.html</a>
 * SUBJECT TO BE CHANGED!!!
 */
public class BotException extends RuntimeException {
	public static final long serialVersionUID = -1;
//	public static final Log exceptionLog = new Log("exception", Level.FINEST, Target.FILE);
	
	protected final List<InfoItem> infoItems = new ArrayList<InfoItem>();

	protected static class InfoItem {
		public String errorContext = null;
		public String errorCode = null;
		public String errorText = null;
//		public Long timestamp = 0L;

		public InfoItem(String contextCode, String errorCode, String errorText) {
			this.errorContext = contextCode;
			this.errorCode = errorCode;
			this.errorText = errorText;
//			this.timestamp = Context.getSharedInstance().getRtMillis();
		}
	}
	
//	private void reportException () {
		//Create remark in shared-log
//		Long lastInfoTStamp = infoItems.get(infoItems.size() -1 ).timestamp;
//		if (((BoolVar)CVarManager.getSharedInstance().getCVar("create_exception_log")).getValue() == true) {
//			Context.getSharedInstance()
//					.getLog()
//					.warning(
//							"[BotException][occured][@" + lastInfoTStamp
//									+ "]See exception.txt");
//			exceptionLog.printSection("Exception at " + lastInfoTStamp);
//			exceptionLog.fine(toString());
//		}
//	}

	public BotException(String errorContext, String errorCode,
			String errorMessage) {
		this(errorContext, errorCode, errorMessage,null);
	}

	public BotException(String errorContext, String errorCode,
			String errorMessage, Throwable cause) {
//		super(cause);
		addInfo(errorContext, errorCode, errorMessage);
		if ((boolean)Default.auto_report_exception.getValue() == true) {
//			reportException();
			AdvancedGlobalBulletin.getSharedInstance().SYSTEM.Error.push("", new String[]{"!!!Exception!!!"}, new Object[]{this});
		}
	}

	public BotException addInfo(String errorContext, String errorCode,
			String errorText) {
		this.infoItems.add(new InfoItem(errorContext, errorCode, errorText));
		return this;
	}

	public String getCode() {
		StringBuilder builder = new StringBuilder();

		for (int i = this.infoItems.size() - 1; i >= 0; i--) {
			InfoItem info = this.infoItems.get(i);
			builder.append('[');
			builder.append(info.errorContext);
			builder.append(':');
			builder.append(info.errorCode);
			builder.append(']');
		}

		return builder.toString();
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();

//		builder.append(getCode());
//		builder.append('\n');

		// append additional context information.
		for (int i = this.infoItems.size() - 1; i >= 0; i--) {
			InfoItem info = this.infoItems.get(i);
			builder.append('[');
			builder.append(info.errorContext);
			builder.append(':');
			builder.append(info.errorCode);
			builder.append(']');
			builder.append(info.errorText);
			if (i > 0)
				builder.append('\n');
		}

		// append root causes and text from this exception first.
		if (getMessage() != null) {
			builder.append('\n');
			if (getCause() == null) {
				builder.append(getMessage());
			} else if (!getMessage().equals(getCause().toString())) {
				builder.append(getMessage());
			}
		}
		appendException(builder, getCause());

		return builder.toString();
	}

	private void appendException(StringBuilder builder, Throwable throwable) {
		if (throwable == null)
			return;
		appendException(builder, throwable.getCause());
		builder.append(throwable.toString());
		builder.append('\n');
	}
}
