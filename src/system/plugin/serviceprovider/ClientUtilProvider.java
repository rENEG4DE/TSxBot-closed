package system.plugin.serviceprovider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tsxdk.entity.EntityManager;

import api.data.TsChannelDTI;
import api.data.TsClientDTI;
import api.util.ClientUtil;

public class ClientUtilProvider implements ClientUtil {
	
	@Override
	public Collection<TsClientDTI> getClientIterable() {
		return ((Collection<TsClientDTI>) EntityManager.getSharedInstance().getClientList().getContentList());
	}
	
	@Override
	public List<TsClientDTI> collectChannelMembers(Collection<TsClientDTI> clientsArray, TsChannelDTI channel) {
		final List<TsClientDTI> ret = new ArrayList<>();
		for (TsClientDTI client : clientsArray) {
			if (client.getCid() == channel.getCid()) {
				ret.add(client);
			}
		}
		return ret;
	}
}
