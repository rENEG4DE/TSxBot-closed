package api.service;


import api.action.ActionContainer;
import api.data.TsClientDTI;
import api.plugin.Plugin;

public interface BotServices {
	
	/*
	 * Bot system
	 */
	
	void handshake(int version);
	
	void init (Plugin abstractPlugin);
	
	TsClientDTI getBotClient();
	
	String encodeTS3String (String str);
	
	String decodeTS3String (String str);
	
	/*
	 * Actions
	 */

	//Flat action commands
	void addAction(ActionContainer action);
	
	/*
	 * Providers
	 */
	
	<T extends UtilityProvider> T getProvider (Class<? extends UtilityProvider> clazz);
}
