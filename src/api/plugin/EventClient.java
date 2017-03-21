package api.plugin;

import api.data.EventDTI;


public interface EventClient {
	void onClientJoined (EventDTI data);
	void onClientLeft (EventDTI data);
	void onComplainExpired (EventDTI data);
	void onComplainNew (EventDTI data);
	void onTextMessage (EventDTI data);
	void onBrainBeat (EventDTI data);
}