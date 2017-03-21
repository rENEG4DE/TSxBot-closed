package system.plugin.serviceprovider;

import system.persistence.gateway.GatewayManager;
import system.persistence.gateway.TsxOptDataGateway;
import tsxdk.entity.TsEntity;
import api.data.TsEntityObject;
import api.util.OptUtil;

public class OptUtilProvider implements OptUtil {
	private final TsxOptDataGateway optData;
	
	{
		optData = new TsxOptDataGateway();
	}
	
	@Override
	public <T> void saveOptVar(TsEntityObject entity, String descriptor, T value) {
		GatewayManager.getSharedInstance().getEntityGateway().insertEntity((TsEntity)entity);
		optData.safeValue((TsEntity) entity, descriptor, value);
	}

	@Override
	public <T> T getOptVar(TsEntityObject entity, String descriptor) {
		return optData.getValue((TsEntity)entity, descriptor);
	}

	@Override
	public void deleteOptVar(TsEntityObject entity, String descriptor) {
		optData.deleteValue((TsEntity)entity, descriptor);
	}
	
}
