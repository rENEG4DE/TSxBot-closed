package moveBotBack;

import api.data.CVarDTI;
import api.data.EventDTI;
import api.data.TsClientDTI;
import api.plugin.Plugin;
import api.plugin.EventClient;
import api.service.BotServices;


public class MoveBotBack extends Plugin implements EventClient {
	
	private int not_in_slumber_channel_since = 0;
	
	CVarDTI move_slumber_channel_delay;
	CVarDTI slumber_channel_id;

	public MoveBotBack(BotServices service) {
		super ("MoveBotBack", service);
	}

	@Override
	protected void initConfig() {
		System.out.println("[" + getPluginName() + "][loading Cvars..]" );
		move_slumber_channel_delay = service.createIntegerVar(createCVarID("move_slumber_channel_delay"), 2);
		slumber_channel_id = service.createIntegerVar(createCVarID("slumber_channel_id"), 571);
		service.registerCVar(move_slumber_channel_delay);
		service.registerCVar(slumber_channel_id);
		//System.out.println("[" + getPluginName() + "]move_slumber_channel_delay="+getModCVarVal("move_slumber_channel_delay"));
		//System.out.println("[" + getPluginName() + "]slumber_channel_id="+getModCVarVal("slumber_channel_id"));
		
	}
	
	@Override
	public EventClient[] getEventClients() {
		// TODO Auto-generated method stub
		return new EventClient[] {this};
	}
	
	@Override
	public void onClientJoined(EventDTI data) {
		System.out.println(data.getEventDescriptor() + " " +data.getCurrent_client().getClient_Nickname());
	}

	@Override
	public void onClientLeft(EventDTI data) {
		// TODO Auto-generated method stub
		// System.out.println(data.getEventDescriptor() + " " +data.getCurrent_client().getClient_Nickname());
		// das kann nicht funktionieren on left wird das client object zerstört also kann man auch keine infos mehr abrufen!!!
	}

	@Override
	public void onComplainExpired(EventDTI data) {
		System.out.println(data.getEventDescriptor() + " " +data.getCurrent_complain().getFname()+" [complain about] "+data.getCurrent_complain().getTname()+ " [expired] ");
	}

	@Override
	public void onComplainNew(EventDTI data) {
		System.out.println(data.getEventDescriptor() + " " +data.getCurrent_complain().getFname()+" [complained about] "+data.getCurrent_complain().getTname());
	}

	@Override
	public void onTextMessage(EventDTI data) {
		System.out.println(data.getEventDescriptor() + " from "+ data.getCurrent_client().getClient_Nickname()+" [msg] "
				 + data.getMsg() );
	}
	
	@Override
	public void onBrainBeat(EventDTI data) {
		// TODO Auto-generated method stub
		
		try {
			final TsClientDTI botClient = service.getBotClient();
//			final Integer moveDelay = 2;//(Integer) getModCVarVal("move_slumber_channel_delay");
//			final int slumberChannel = 571; //(Integer) getModCVarVal("slumber_channel_id");
			final Integer moveDelay = (Integer)move_slumber_channel_delay.getValue();
			final Integer slumberChannel = (Integer)slumber_channel_id.getValue();
			
			//System.out.println(data.getEventDescriptor()+ "[Move Bot Back]");
			//service.addAction(service.createAction(0).clientMove(botClient, slumberChannel));
			if (botClient != null
					&& botClient.getIdle_time() > moveDelay
					&& botClient.getCid() != slumberChannel) {

				if (botClient.getTalking() != 1) {
			    	not_in_slumber_channel_since -= 1;
				}
				if (not_in_slumber_channel_since < -8) {
				    System.out.println("Idle_time()"+botClient.getIdle_time() +" > moveDelay " +moveDelay);
				    System.out.println("getCid()"+botClient.getCid() +" != slumberChannel " +slumberChannel);
					service.addAction(service.createAction(1).clientMove(botClient, slumberChannel));
				}

			} else {
				not_in_slumber_channel_since = 0;
			}
		} catch (Exception e) {
			System.out.println("[" + getPluginName() + "][Warning]" +e);
		}
	}



}
