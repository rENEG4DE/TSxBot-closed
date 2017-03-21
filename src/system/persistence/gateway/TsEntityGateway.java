package system.persistence.gateway;

import tsxdk.entity.TsEntity;

public final class TsEntityGateway implements AbstractEntityGateway {
	{
		clientGW = GatewayManager.getSharedInstance().getClientGateway();
		channelGW = GatewayManager.getSharedInstance().getChannelGateway();
		complainGW = GatewayManager.getSharedInstance().getComplainGateway();
		eventGW = GatewayManager.getSharedInstance().getEventGateway();
	}

	private TsClientGateway clientGW;
	private TsChannelGateway channelGW;
	private TsComplainGateway complainGW;
	private TsEventGateway eventGW;

	@Override
	public int insertEntity(TsEntity entity) {
		if (entity.getTSXDBID() == 0) {
			EntityClass entityClass = EntityClass.valueOf(entity.getClass().getSimpleName());

			AbstractEntityGateway gw = null;
			switch (entityClass) {
				case TsClient: {
					gw = clientGW;
				}
					break;
				case TsChannel: {
					gw = channelGW;
				}
					break;
				case TsComplain: {
					gw = complainGW;
				}
					break;
				case TsEvent: {
					return eventGW.insertEntity(entity);
				}
				default: {
					throw new IllegalArgumentException();
				}
			}
			return gw.insertEntity(entity);
		} else
			return entity.getTSXDBID();
	}

	@Override
	public void updateEntity(TsEntity entity) {
		if (entity.getTSXDBID() == 0) {
			EntityClass entityClass = EntityClass.valueOf(entity.getClass().getSimpleName());

			AbstractEntityGateway gw = null;
			switch (entityClass) {
				case TsClient: {
					gw = clientGW;
				}break;
				case TsChannel: {
					gw = channelGW;
				}break;
				case TsComplain: {
					gw = complainGW;
				}break;
			case TsEvent: {
				throw new UnsupportedOperationException();
			}
			default: {
				throw new IllegalArgumentException();
			}
			}
			
			if (gw != null) {
				gw.updateEntity(entity);
			}
		}
	}

	@Override
	public void removeEntity(TsEntity entity) {
		if (entity.getTSXDBID() == 0) {
			EntityClass entityClass = EntityClass.valueOf(entity.getClass().getSimpleName());

			AbstractEntityGateway gw = null;
			switch (entityClass) {
				case TsClient: {
					gw = clientGW;
				}break;
				case TsChannel: {
					gw = channelGW;
				}break;
				case TsComplain: {
					gw = complainGW;
				}break;
				case TsEvent: {
					eventGW.removeEntity(entity);
				}
					break;
				default: {
					throw new IllegalArgumentException();
				}
			}
			
			if (gw != null) {
				gw.removeEntity(entity);
			}
		
		}
	}

	@Override
	public TsEntity selectEntity(int entityId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public TsEntity[] getAll() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void removeEntity(int entityId) {
		throw new UnsupportedOperationException();
	}

}
