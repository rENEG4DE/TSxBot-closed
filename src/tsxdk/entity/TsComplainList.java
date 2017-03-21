package tsxdk.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import system.core.Context;
import tsxdk.entity.meta.EntityList;
import tsxdk.entity.meta.LibEntityState;
import tsxdk.entity.meta.StatefulEntity;
import tsxdk.parser.TsFieldStack;
import utility.bulletin.AdvancedGlobalBulletin;
import utility.reclaimable.ProdigalPool;
import utility.reclaimable.Reclaimable;
import utility.reclaimable.ReclaimableCollection;
import api.data.TsEntityType;
import bot.EventServer;

public class TsComplainList implements EntityList<TsComplain, String>, TsEntity  {

	private int propHash = 0;
	private ReclaimableCollection<String> complains;

	TsComplainList() {
		complains = new ProdigalPool<String>(TsComplain.class);
//		complains = new ReclaimablePool<Integer>((Reclaimable<Integer>)new TsComplain(), true);
	}

	public void triggerEvents() {
		for (TsComplain complain : (TsComplain[]) complains.getContent()) {
			switch (complain.getState()) {
			case INITIAL: {
				LibTsEvent.COMPLAINEXPIRED.setComplain(complain);
				LibTsEvent.COMPLAINEXPIRED.setTimeStamp(Context.getSharedInstance().getRtMillis());
				EventServer.getSharedInstance().triggerComplainExpired(LibTsEvent.COMPLAINEXPIRED);
			} break;
			case CREATED: {
				LibTsEvent.COMPLAINCOMMITTED.setComplain(complain);
				LibTsEvent.COMPLAINCOMMITTED.setTimeStamp(Context.getSharedInstance().getRtMillis());
				EventServer.getSharedInstance().triggerComplainNew(LibTsEvent.COMPLAINCOMMITTED);
			} break;
			default: {
			} break;
			}
		}
	}
	
	public void collectStates () {
		int removedCount = 0;
		int updatedCount = 0;
		int touchedCount = 0;
		int createdCount = 0;
		int pendingCount = 0;
		
		for (TsComplain complain : (TsComplain[]) complains.getContent()) {
			switch (complain.getState()) {
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
				new String[]{"TsComplainList","collectStates"},
				new Integer[]{removedCount,pendingCount,touchedCount,updatedCount,createdCount});
	}
	
	@Override
	public void cleanUp() {
		List<Reclaimable> toRemove = new ArrayList<>();
		for (Reclaimable entity : complains.getContent()) {
			if (((StatefulEntity)entity).getState() == LibEntityState.UNUSED) {
				toRemove.add(entity);
			}
		}
		for(Reclaimable entity : toRemove) {
			complains.remove(entity);
		}
	}
	
	@Override
	public int getEntityCount() {
		return complains.getCapacity();
	}
	
	@Override
	public TsComplain acquire(String complain) {
		TsComplain ret = (TsComplain) complains.acquire(complain);
		
		if (ret.getState() == LibEntityState.UNUSED) {
			ret.setCreated();
		} else {
			ret.setTouched();
		}
		return ret;
	}


	public Collection<TsComplain> getContentList() {
		return (Collection<TsComplain>) complains.getContentAsCollection();
	}
	
	@Override
	public void setInitial() {
		for (TsComplain complain : (TsComplain[]) complains.getContent()) {
			complain.setInitial();
		}
	}
	
	@Override
	public void setTouched() {
		for (TsComplain complain : (TsComplain[]) complains.getContent()) {
			complain.setTouched();
		}
	}
	
	@Override
	public TsEntityType getType() {
		return TsEntityType.TSCOMPLAINLIST;
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
	public TsComplain[] getIterable() {
		return (TsComplain[]) complains.getContent(); 
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
		complains.clearCache();		
	}

}
