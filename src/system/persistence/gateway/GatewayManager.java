package system.persistence.gateway;

public final class GatewayManager {
	private static class SingletonHolder {
		private static final GatewayManager INSTANCE = new GatewayManager();
	}
	
	public static GatewayManager getSharedInstance () {
		return SingletonHolder.INSTANCE;
	}
	
	private AuxiliaryGateway auxiliaryGW;
	private TsClientGateway clientGW;
	private TsChannelGateway channelGW;
	private TsComplainGateway complainGW;
	private TsxOptDataGateway optDataGW;
	private TsEventGateway eventGW;
	private TsEntityGateway entityGW;
	
	public AuxiliaryGateway getAuxGateway () {
		if (auxiliaryGW == null)
			auxiliaryGW = new AuxiliaryGateway();
		
		return auxiliaryGW;
	}
		
	public TsChannelGateway getChannelGateway () {
		if (channelGW == null)
			channelGW = new TsChannelGateway();
		
		return channelGW;
	}
	
	public TsComplainGateway getComplainGateway () {
		if (complainGW == null)
			complainGW = new TsComplainGateway();
		
		return complainGW;
	}
	
	public TsxOptDataGateway getOptDataGateway () {
		if (optDataGW == null)
			optDataGW = new TsxOptDataGateway();
		
		return optDataGW;
	}

	public TsClientGateway getClientGateway() {
		if (clientGW == null)
			clientGW = new TsClientGateway();
		
		return clientGW;
	}

	public TsEventGateway getEventGateway() {
		if (eventGW == null)
			eventGW = new TsEventGateway();
		return eventGW;
	}
	
	public TsEntityGateway getEntityGateway() {
		if (entityGW == null)
			entityGW  = new TsEntityGateway();
		return entityGW ;
	}
}
