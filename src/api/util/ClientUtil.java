package api.util;

import java.util.Collection;
import java.util.List;

import api.data.TsChannelDTI;
import api.data.TsClientDTI;
import api.service.UtilityProvider;

public interface ClientUtil extends UtilityProvider{

	List<TsClientDTI> collectChannelMembers(Collection<TsClientDTI> clientsArray, TsChannelDTI channel);

	Collection<TsClientDTI> getClientIterable ();

}
