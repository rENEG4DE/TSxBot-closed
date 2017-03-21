package tsxdk.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import system.core.Context;
import system.core.Default;
import system.plugin.serviceprovider.QueryProviderObject;
import tsxdk.io.query.engine.QueryAgent;
import utility.bulletin.AdvancedGlobalBulletin;
import bot.BotException;


/**
 * This class cares about Connecting and Low-Level I/O with teamspeak.
 *
 */


//Make this Package-public-only
public class TsPipe {
	
//	private static final int IBUFSIZ = 10;
	private Socket socket;
	private InputStreamReader iStream;
	
//	private static Long time = System.currentTimeMillis();
	private static final AdvancedGlobalBulletin bulletin = AdvancedGlobalBulletin.getSharedInstance();
	
	private BufferedReader iBuf;
//	BufferedWriter oBuf;
	private PrintWriter writer;
	private String host;
	private int port;
	
	private TsPipe () {

	}
	
	private static class SingletonHolder {
		private static TsPipe INSTANCE = new TsPipe();
	}
	
	public static TsPipe getSharedInstance () {
		return SingletonHolder.INSTANCE;
	}
	
	private void setupTsServerConnection() {

		final QueryProviderObject qpo = new QueryProviderObject();
		final QueryAgent agent = Context.getQueryAgent();

		qpo.login((String) Default.client_login_name.getValue(),
				(String) Default.client_login_password.getValue(), 
				(int) Default.virtual_server_id.getValue());

		agent.pushQuery(qpo.registerEventTextServer());
		agent.pushQuery(qpo.registerEventTextPrivate());
		agent.pushQuery(qpo.registerEventTextChannel());
		agent.pushQuery(qpo.registerEventServer());
		agent.pushQuery(qpo.setMyNickname((String) Default.query_nickname.getValue()));
	}
	
	public void connect (String host, int port) throws BotException {
		this.host = host;
		this.port = port;
		initSocket(); 	
		initInput();
		initOutput();
//		oBuf = new BufferedWriter(oStream);
		setupTsServerConnection();
	}

	private void initSocket() {
		try {
			socket = new Socket(host, port);
//			socket.setReceiveBufferSize(IBUFSIZ);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BotException("TsPipe.init", "e1", "Failed to init socket to host,port{" + host + "," + port + "}", e);
		}
	}

	private void initOutput() {
		//init output
		try {
			writer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
			throw new BotException("TsPipe.init", "e3", "Failed to init oStream to socket,host,port{" + socket.toString() + "," + host + "," + port + "}", e);
		}
	}

	private void initInput() {
		//init input
		try {
			iStream = new InputStreamReader (socket.getInputStream(), "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			throw new BotException("TsPipe.init", "e2", "Failed to init iStream to socket,host,port{" + socket.toString() + "," + host + "," + port + "}", e);
		}
		
		iBuf = new BufferedReader(iStream);
	}
		
	public String in () {
		if (socket.isClosed()) {
			AdvancedGlobalBulletin.getSharedInstance().PIPE_IN.Error.push("Socket has been closed", new String[]{"TsPipe", "in"});
		}
		String ret = null;
		try {
			if (iBuf.ready()) {				//If we do not do this, iBuf is Blocked on readLine! (thread does not continue UNTIL there's IO!)
				ret = iBuf.readLine();
				iBuf.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BotException("TsPipe.in", "e1", "Failed to get input from iBuf{" + iBuf.toString() +"}", e);
		}
		
		if (ret != null && !ret.equals("") ) {
//			System.out.println("<<<@" + System.currentTimeMillis() + ":\"" + ret + "\"");
			bulletin.PIPE_IN.Verbose.push("  <<< ", new String[]{"TsPipe","in"}, new String[]{ret});
			return ret;
		}

		return null;
	}
	
	
//	public boolean hasInput () {
//		try {
//			String ret = iBuf.readLine();
////			System.out.println("<<<@" + System.currentTimeMillis() + ":\"" + ret + "\"");
//			return !ret.isEmpty() && !ret.equals("null");
//		} catch (IOException e) {
//			e.printStackTrace();
//			throw new BotException("TsPipe.in", "e1", "Failed to get input from iBuf{" + iBuf.toString() +"}", e);
//		}
//	}
	
	//Subject to change: visibility to default! only TsQuery should be able to use pipe.out !
	
	public void out (String out) {
		bulletin.PIPE_OUT.Verbose.push(" >>> ", new String[]{"TsPipe","out"}, new String[]{out});
//		System.out.println(">>>@" + System.currentTimeMillis() + ":" + out);
		writer.println(out);
//		}
		if (writer.checkError()) {
			throw new BotException("TsPipe.out", "e1", "Failed to write output to writer {" + writer.toString() + "}");
		}
	}
}
