package system.plugin.serviceprovider;

import bot.tsxcli.CLIService;
import api.service.AbstractCLICommand;
import api.util.CLIServiceUtil;

public class CLIServiceUtilProvider implements CLIServiceUtil {
	private final CLIService svc = CLIService.getSharedInstance();
	
	@Override
	public void registerCommand(AbstractCLICommand cmd, String... expectedSymbols) {
		svc.registerCommand(cmd, expectedSymbols);
	}

	@Override
	public void echo(String text) {
		svc.echo(text);
	}

}
