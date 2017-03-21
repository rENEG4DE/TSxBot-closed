package animateChannelName;

import api.data.CVarDTI;
import api.data.EventDTI;
import api.data.TsChannelDTI;
import api.plugin.EventClient;
import api.plugin.Plugin;
import api.service.BotServices;
import api.util.QueryProvider;

public class AnimateChannelName extends Plugin implements EventClient  {

	CVarDTI channel_to_animate;
	CVarDTI movement_direction;
	
	private final QueryProvider			queryUtil;
	
	public AnimateChannelName (BotServices service) { 
		super ("AnimateChannelName", service);
		queryUtil = service.getProvider(QueryProvider.class);
	}
	
	@Override
	public EventClient[] getEventClients() {
		return new EventClient[]{this};
	}

	@Override
	protected void initConfig() {
		channel_to_animate = cVarUtil.createIntegerVar(createCVarID("channel_to_animate"), 0);
		movement_direction = cVarUtil.createEnumVar(createCVarID("movement_direction"), "left", "right");
		
		cVarUtil.registerCVar(channel_to_animate);
		cVarUtil.registerCVar(movement_direction);
	}

	public void onBrainBeat(EventDTI data) {
		TsChannelDTI channel_to_change = null;
		for (TsChannelDTI current : channelUtil.getChannelIterable()) {
			if (current.getCid() == (int)channel_to_animate.getValue())
				channel_to_change = current;
		}
		
		if (channel_to_change != null) {
			String channel_name = channel_to_change.getChannel_name();
			
			/**
			 * Channel_name = 123456
			 * right = 612345
			 * left = 234561
			 */
			
			
			if (movement_direction.getValue().equals("left")) {
				channel_name = channel_name.substring(1) + channel_name.substring(0, 1);
			} else if (movement_direction.getValue().equals("right")) {
				channel_name = channel_name.substring(channel_name.length() -1) + channel_name.substring(0, channel_name.length() -1);
			}

			service.addAction(actionUtil.createQueryAction(0, 0, queryUtil.renameChannel(channel_to_change.getCid(), service.encodeTS3String(channel_name))));
			
		}
	}
	
	
}
