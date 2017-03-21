package system.persistence.gateway;

import tsxdk.entity.TsEntity;

public interface AbstractEntityGateway {

	int insertEntity(TsEntity entity);

	void updateEntity(TsEntity entity);

	void removeEntity(TsEntity entity);
	
	TsEntity selectEntity(int entityId);
	
	TsEntity[] getAll();

	void removeEntity(int entityId);

}