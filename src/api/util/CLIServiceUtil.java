package api.util;

import api.service.AbstractCLICommand;
import api.service.UtilityProvider;

public interface CLIServiceUtil extends UtilityProvider {

	void registerCommand(AbstractCLICommand cmd, String... expectedSymbols);

	/**
	 * Outputs to the client that caused the last Textmessage
	 * @param text
	 */
	void echo(String text);

}