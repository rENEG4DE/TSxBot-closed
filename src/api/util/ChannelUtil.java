package api.util;

import java.util.Collection;
import java.util.List;

import api.data.TsChannelDTI;
import api.service.UtilityProvider;

public interface ChannelUtil extends UtilityProvider {

	int countChannelsWhere(Collection<TsChannelDTI> channelsArray, EntityPredicate... predicate);

	int countChannelMembers(Collection<TsChannelDTI> channelsArray, int cid);

	List<TsChannelDTI> selectChannelsWhere(Collection<TsChannelDTI> channelsArray, EntityPredicate... predicate);

	List<TsChannelDTI> getSubChannels(Collection<TsChannelDTI> channelsArray, int pid);

	List<TsChannelDTI> getDirectSubChannels(Collection<TsChannelDTI> channelsArray, int pid);
	
	List<TsChannelDTI> mirrorChannelOrder(Collection<TsChannelDTI> channelsArray);

	List<TsChannelDTI> sortChannelsUserCount(Collection<TsChannelDTI> channelList);

	List<TsChannelDTI> sortChannelsAlphaNumeric(Collection<TsChannelDTI> channelList);

	Collection<TsChannelDTI> getChannelIterable ();

	void applyChannelOrder(List<TsChannelDTI> toOrder);

}
