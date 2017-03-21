package tsxdk.io;

import system.core.Context;
import system.plugin.serviceprovider.QueryProviderObject;
import utility.misc.StringMan;
import api.data.TsClientDTI;

/**
 * Instantaneous delivery of Text-messages of 
 * any desired length to a given client
 * @author kornholio
 */
public class BufferedTsPrompt {
	
	private final static class SingletonHolder {
		private final static BufferedTsPrompt INSTANCE = new BufferedTsPrompt();
	};
	
	public final static BufferedTsPrompt getSharedInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private final static int MAX_TS_TEXT_LEN = 200;
//	private final static AsynchronousQueryAgent agent = AsynchronousQueryAgent.getSharedInstance();
	
	public void prompt (TsClientDTI client, String text) {
		if (text.length() <= MAX_TS_TEXT_LEN && !text.contains("\n")) {
			echo(client, text);
		} else {
			//Try to split for newline
			String lines[] = text.split("\n");
			if (lines.length > 1) {
				for (String line : lines) {
					prompt(client,line);
				}
			} else {//Split string into subparts and echo them seperately
				int begin = 0;
				int end = MAX_TS_TEXT_LEN;
			
				while (begin < text.length()) {
					echo(client, text.substring(begin,end));
				
					begin += MAX_TS_TEXT_LEN;
					end = (end + MAX_TS_TEXT_LEN >= text.length()) ? text.length() - 1 : end + MAX_TS_TEXT_LEN;
				}
			}
		}
	}
	
	private void echo (TsClientDTI client, String text) {
		Context.getQueryAgent().pushQuery(new QueryProviderObject().sendPrivateMessageToClient(client.getClid(), StringMan.encodeTS3String(text)));
	}

}
