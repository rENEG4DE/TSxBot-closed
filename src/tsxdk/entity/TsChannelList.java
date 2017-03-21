package tsxdk.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tsxdk.entity.meta.EntityList;
import tsxdk.entity.meta.LibEntityState;
import tsxdk.entity.meta.StatefulEntity;
import tsxdk.parser.TsFieldStack;
import utility.bulletin.AdvancedGlobalBulletin;
import utility.reclaimable.ProdigalPool;
import utility.reclaimable.Reclaimable;
import utility.reclaimable.ReclaimableCollection;
import api.data.TsEntityType;

public class TsChannelList implements EntityList<TsChannel, String>, TsEntity  {

	private int propHash = 0;
	private ReclaimableCollection<String> channels;

	TsChannelList() {
		channels = new ProdigalPool<>(TsChannel.class);
//		channels = new ReclaimablePool<Integer>((Reclaimable<Integer>)new TsChannel(),true);
	}
	
	
	public void collectStates () {
		int removedCount = 0;
		int updatedCount = 0;
		int touchedCount = 0;
		int createdCount = 0;
		int pendingCount = 0;
		
		for (TsChannel channel : (TsChannel[]) channels.getContent()) {
			switch (channel.getState()) {
			case INITIAL: {
				removedCount++;
			} break;
			case UPDATED: {
				updatedCount++;
			} break;
			case TOUCHED: {
				touchedCount++;
			} break;
			case CREATED: {
				createdCount++;
			} break;
			case UNUSED: {
				pendingCount++;
			}
			}
		}
		
		
		AdvancedGlobalBulletin.getSharedInstance().ENTITYSTATES.Verbose.push("removed, pending, touched, updated, created", 
				new String[]{"TsChannelList","collectStates"},
				new Integer[]{removedCount,pendingCount,touchedCount,updatedCount,createdCount});
	}
	
	@Override
	public void cleanUp() {
		List<Reclaimable> toRemove = new ArrayList<>();
		for (Reclaimable entity : channels.getContent()) {
			if (((StatefulEntity)entity).getState() == LibEntityState.UNUSED) {
//				System.out.println("Removing: " + ((TsChannelDTI) entity).getChannel_name());
				toRemove.add(entity);
			}
		}
		for(Reclaimable entity : toRemove) {
			channels.remove(entity);
		}
	}
	
	@Override
	public int getEntityCount() {
		return channels.getCapacity();
	}
	
	@Override
	public TsChannel acquire(String channel) {
		TsChannel ret = (TsChannel) channels.acquire(channel);
	
		if (ret.getState() == LibEntityState.UNUSED) {
			ret.setCreated();
		} else {
			ret.setTouched();
		}
		return ret;
	}

	@Override
	public void setInitial() {
		for (TsChannel channel : (TsChannel[]) channels.getContent()) {
			channel.setInitial();
		}
	}
	
	@Override
	public void setTouched() {
		for (TsChannel channel : (TsChannel[]) channels.getContent()) {
			channel.setTouched();
		}
	}

	@Override
	public TsEntityType getType() {
		return TsEntityType.TSCHANNELLIST;
	}

	@Override
	public int getTsPropHash() {
		return propHash;
	}

	@Override
	public void setTsPropHash(String str) {
		propHash = str.hashCode();
	}

	@Override
	public void update(TsFieldStack stack) {
	}

	@Override
	public TsChannel[] getIterable() {
		return (TsChannel[]) channels.getContent();
	}

	@Override
	public Collection<? super TsChannel> getContentList() {
		return (Collection<? super TsChannel>) channels.getContentAsCollection();
	}
		
	@Override
	public void setTSXDBID(int id) {
		throw new UnsupportedOperationException();
	}
	@Override
	public int getTSXDBID() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int getGlueID() {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setGlueID(int id) {
		throw new UnsupportedOperationException();
	}


	@Override
	public void clearCache() {
		cleanUp();
		channels.clearCache();		
	}

}
