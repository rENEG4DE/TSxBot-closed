package tsxdk.entity;

import java.util.Collection;

import tsxdk.entity.meta.EntityList;
import tsxdk.entity.meta.LibEntityState;
import tsxdk.parser.TsFieldStack;
import utility.bulletin.AdvancedGlobalBulletin;
import utility.reclaimable.ProdigalPool;
import utility.reclaimable.Reclaimable;
import utility.reclaimable.ReclaimableCollection;
import api.data.TsClientDTI;
import api.data.TsEntityType;

public class TsClientList implements EntityList<TsClient, String>, TsEntity  {

	private int propHash = 0;
	private ReclaimableCollection<String> clients;

	TsClientList() {
//		clients = new ReclaimablePool<Integer>((Reclaimable<Integer>)new TsClient(),true);
		clients = new ProdigalPool<String>(TsClient.class);
	}

	public void collectStates () {
		int removedCount = 0;
		int updatedCount = 0;
		int touchedCount = 0;
		int createdCount = 0;
		int pendingCount = 0;
		
		AdvancedGlobalBulletin bulletin = AdvancedGlobalBulletin.getSharedInstance();
		
		for (TsClient client : (TsClient[]) clients.getContent()) {
			switch (client.getState()) {
			case INITIAL: {
				removedCount++;
//				bulletin.STATE.push("", new String[]{"TsClientList", "collectStates"}, new Object[]{client.getClient_Nickname(), client.getState()});
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
				bulletin.STATE.Verbose.push("", new String[]{"TsClientList", "collectStates"}, new Object[]{client.getClient_Nickname(),client.getClient_unique_identifier(), client.getState()});
				pendingCount++;
			}
			}
		}
	
		AdvancedGlobalBulletin.getSharedInstance().ENTITYSTATES.Verbose.push("removed, pending, touched, updated, created", 
													new String[]{"TsClientList","collectStates"},
									new Integer[]{removedCount,pendingCount,touchedCount,updatedCount,createdCount});
	}
	
	@Override
	public void cleanUp() {
		throw new UnsupportedOperationException();	//Clients are removed by client-leave-events
	}
	
	public TsClient selectClientClidEquals (int clid) {
		for (TsClient client : getIterable())
			if (client.getClid() == clid)
				return client;
		
		return null;
	}
		
	@Override
	public int getEntityCount() {
		return clients.getCapacity();
	}
	
	@Override
	public TsClient acquire(String client) {
		TsClient ret = (TsClient) clients.acquire(client);
		if (ret.getState() == LibEntityState.UNUSED) {
			ret.setCreated();
		} else {
			ret.setTouched();
		}
		return ret;
	}

	@Override
	public void setInitial() {
		for (TsClient client : (TsClient[]) clients.getContent()) {
			if (client != null) {
				client.setInitial();
			}
		}
	}
	
	@Override
	public void setTouched() {
		for (TsClient client : (TsClient[]) clients.getContent()) {
			client.setTouched();
		}
	}
	
	@Override
	public TsEntityType getType() {
		return TsEntityType.TSCLIENTLIST;
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
	public TsClient[] getIterable() {
		return (TsClient[]) clients.getContent();
	}
	
	@Override
	public Collection<? super TsClient> getContentList() {
		return (Collection<? super TsClient>) clients.getContentAsCollection();
	}
	
	public void remove(TsClientDTI client) {
		clients.remove((Reclaimable) client);
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
		clients.clearCache();		
	}


}
