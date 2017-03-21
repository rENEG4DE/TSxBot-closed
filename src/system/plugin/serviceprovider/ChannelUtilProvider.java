package system.plugin.serviceprovider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import system.core.Context;
import tsxdk.entity.EntityManager;
import api.data.TsChannelDTI;
import api.util.ActionFactoryUtil;
import api.util.ChannelUtil;
import api.util.EntityPredicate;
import api.util.QueryProvider;

public final class ChannelUtilProvider implements ChannelUtil {

	@Override
	public Collection<TsChannelDTI> getChannelIterable() {
		return ((Collection<TsChannelDTI>) EntityManager.getSharedInstance().getChannelList().getContentList());
	}
	
	@Override
	public List<TsChannelDTI> selectChannelsWhere(Collection<TsChannelDTI> channelList, EntityPredicate... predicate) {
		if (channelList.size() == 0) return new LinkedList<>();
		List<TsChannelDTI> result = new ArrayList<>();

		for (TsChannelDTI channel : channelList) {
			if (EntityPredicateCompanion.checkAgainst(channel, predicate)) {
				result.add(channel);
			}
		}

		return result;
	}

	@Override
	public int countChannelsWhere(Collection<TsChannelDTI> channelList, EntityPredicate... predicate) {
		if (channelList.size() == 0) return 0;
		int result = 0;

		for (TsChannelDTI channel : channelList) {
			if (EntityPredicateCompanion.checkAgainst(channel, predicate)) {
				result++;
			}
		}

		return result;
	}

	@Override
	public int countChannelMembers(final Collection<TsChannelDTI> channelList, final int cid) {
		if (channelList.size() == 0) return 0;
		int result = 0;

		for (TsChannelDTI channel : getSubChannels(channelList, cid)) {
			result += channel.getTotal_clients();
		}

		for (TsChannelDTI channel : channelList) {
			if (channel.getCid() == cid)
				result += channel.getTotal_clients();
		}

		return result;
	}

	@Override
	public List<TsChannelDTI> getSubChannels(final Collection<TsChannelDTI> channelList, final int pid) {
		if (channelList.size() == 0) return new LinkedList<>();
		
		final List<TsChannelDTI> result = new ArrayList<>();
		
		result.addAll(getDirectSubChannels(channelList, pid));
		
		final List<TsChannelDTI> temp =  new ArrayList<>();
		
		for (TsChannelDTI channel : result) {
			temp.addAll(getSubChannels(channelList, channel.getCid()));
		}

		result.addAll(temp);
		
		return result;
	}
	
	@Override
	public List<TsChannelDTI> getDirectSubChannels(final Collection<TsChannelDTI> channelList, final int pid) {
		if (channelList.size() == 0) return new LinkedList<>();
		
		final List<TsChannelDTI> result = new ArrayList<>();
		
		for (TsChannelDTI channel : channelList) {
			if (channel.getPid() == pid) {
				result.add(channel);
			}
		}
		
		return result;
	}

	@Override
	public List<TsChannelDTI> sortChannelsAlphaNumeric(Collection<TsChannelDTI> channelList) {
		if (channelList.size() == 0) return new LinkedList<>();
		return sortChannelsWith(channelList, (o1, o2) -> o1.getChannel_name().compareTo(o2.getChannel_name()));
	}

	@Override
	public List<TsChannelDTI> sortChannelsUserCount(Collection<TsChannelDTI> channelList) {
		if (channelList.size() == 0) return new LinkedList<>();
		return sortChannelsWith(channelList, (o1, o2) -> {
			if (o1.getTotal_clients_family() > o2.getTotal_clients_family())
				return -1;
			else if (o1.getTotal_clients_family() < o2
					.getTotal_clients_family())
				return +1;
			else
				return 0;
		});
	}

	public List<TsChannelDTI> sortChannelsWith(Collection<TsChannelDTI> channelList, Comparator<TsChannelDTI> comparator) {
		if (channelList.size() == 0) return new LinkedList<>();
		final List<TsChannelDTI> result = new LinkedList<>(channelList);
		Collections.sort(result, comparator);
		return result;
	}

	@Override
	public List<TsChannelDTI> mirrorChannelOrder(Collection<TsChannelDTI> channelList) {
//		System.out.println("restoreChannelOrder");
		if (channelList.size() == 0) return new LinkedList<TsChannelDTI> ();
		
		final List<TsChannelDTI> ret = new ArrayList<>(channelList.size()); 
	
		//Find the level we're on
		int level = Integer.MAX_VALUE;
		
		for (TsChannelDTI current : channelList) {
			if (current.getPid() < level) {
				level =  current.getPid();
			}
		}
		
		int searched_order = 0;
		
		for (int i = 0; i < channelList.size(); ++i) {
			boolean found = false;
			for (TsChannelDTI current : channelList) {
				if (current.getPid() == level && current.getChannel_order() == searched_order) {
					found = true;
					ret.add(current);
					searched_order = current.getCid();
					List<TsChannelDTI> subChannels = null;
					if (!(subChannels = mirrorChannelOrder(getDirectSubChannels(channelList, current.getCid())/*, 1*/)).isEmpty()) {
						ret.addAll(subChannels);
					}
				}
			}
			if (!found) {	//We have an "in-betweener" - the order of the channels is corrupted and the next order is not available
				System.out.println("I could not find the Channel with order " + searched_order);
//				final List<TsChannelDTI> toSearch = new ArrayList<> (channelList);
//				toSearch.removeAll(ret);
//				TsChannelDTI next = toSearch.get(0);
//				//find the smallest order that is still above the searched one
//				for (TsChannelDTI current : toSearch) {
//					if (current.getChannel_order() > searched_order && 
//						current.getChannel_order() < next.getChannel_order()) {
//						next = current;
//					}
//				}
//				ret.add(next);
			}
			if (ret.size() == channelList.size()) break; // for some mathematical reason this makes no difference...
		}

		return ret;
	}

	@Override
	public void applyChannelOrder(final List<TsChannelDTI> toOrder) {
		final List<TsChannelDTI> oldOrder = mirrorChannelOrder(toOrder);

		int orderToGive = 0;

		final QueryProviderObject queryProviderObject = (QueryProviderObject) Context.getSharedInstance().getPlgService().getProvider(QueryProvider.class);
		final ActionFactoryUtilProvider actionUtil = (ActionFactoryUtilProvider) Context.getSharedInstance().getPlgService().getProvider(ActionFactoryUtil.class);
		
		
		for (int i = 0; i < toOrder.size(); ++i) {
			TsChannelDTI orderedChannel = toOrder.get(i);
			if (orderedChannel != oldOrder.get(i)) {
				
				Context.getSharedInstance()
						.getPlgService()
						.addAction(actionUtil.createQueryAction(0,0,
								queryProviderObject.moveChannelTo(
										orderedChannel.getCid(), orderToGive)));
			}
			orderToGive = orderedChannel.getCid();
		}
	}

	
//	private List<TsChannelDTI> restoreChannelOrder(Collection<TsChannelDTI> channelList, int depth) {
//		String indent = new String(new char[depth]).replace("\0", "\t");
//		System.out.println(indent + "restoreChannelOrder");
//		if (channelList.size() == 0)
//			return new LinkedList<TsChannelDTI>();
//
//		final List<TsChannelDTI> ret = new ArrayList<>(channelList.size());
//
//		// Find the level we're on
//		int level = Integer.MAX_VALUE;
//
//		for (TsChannelDTI current : channelList) {
//			if (current.getPid() < level) {
//				level = current.getPid();
//			}
//		}
//
//		System.out.println(indent + "level: " + level);
//
//		int searched_order = 0;
//
//		for (int i = 0; i < channelList.size(); ++i) {
//			for (TsChannelDTI current : channelList) {
//				if (current.getPid() == level && current.getChannel_order() == searched_order) {
//					ret.add(current);
//					System.out.println(indent + "current: " + current.getChannel_name());
//					searched_order = current.getCid();
//					System.out.println(indent + "searched order: " + searched_order);
//					List<TsChannelDTI> subChannels = null;
//					if (!(subChannels = restoreChannelOrder(getDirectSubChannels(channelList, current.getCid()), depth + 1)).isEmpty()) {
//						System.out.println(indent + "adding another " + subChannels.size() + " entries to the list");
//						ret.addAll(subChannels);
//					}
//				}
//			}
//			// if (ret.size() == channelList.size()) break; // for some mathematical reason this makes no difference...
//		}
//
//		return ret;
//	}
}
