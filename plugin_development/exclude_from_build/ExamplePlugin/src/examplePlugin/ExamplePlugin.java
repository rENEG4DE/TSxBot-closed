package examplePlugin;

import api.data.EventDTI;
import api.plugin.EventClient;
import api.plugin.Plugin;
import api.service.BotServices;

public class ExamplePlugin extends Plugin implements EventClient {
	
	public ExamplePlugin(BotServices service) {
		super("ExamplePlugin", service);
	}

	protected void initConfig () {
		service.createStringVar(createCVarID("exampleCVar"), "IAMCVAR");
	}

	@Override
	public EventClient[] getEventClients() {
		return new EventClient[]{this};
	}

	@Override
	public void onBrainBeat(EventDTI data) {
		//System.out.println(data.getEventDescriptor() + "BrainBeat from Runtime-Plugin-module, exampleCVar{" + super.getModCVarVal("exampleCVar") + "}");
	}
}
