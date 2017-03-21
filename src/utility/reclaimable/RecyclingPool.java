package utility.reclaimable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import utility.bulletin.AdvancedGlobalBulletin;
import utility.systemInfo.SystemInfo;
import utility.systemInfo.SystemInfoElement;
import utility.systemInfo.SystemObject;

public class RecyclingPool<K> implements ReclaimableCollection<K>, SystemObject {

	private final class ReclaimableContainer <T extends Reclaimable> {
		public T _object;
		public boolean _isReclaimable;

		public ReclaimableContainer(T obj) {
			this._object = obj;
			_isReclaimable = false;
		}
	}

	final Class<? extends Reclaimable> clazz;

	final Map<K, ReclaimableContainer<?>> slotMap;

	Reclaimable[] content;
	private Collection<? extends Reclaimable>	contentAsList;

	AdvancedGlobalBulletin bulletin;

	int reserves; // The count of reclaimable Objects
	int capacity; // The count of non-reclaimable (i.e. "existing") entries in
					// the slotmap
	boolean regardReserve = true; //if there is a limit to the reclaimable objects kept in the pool 

	private static final int MAX_RESERVES = 50;		//The maximum Count of Objects marked reclaimable,
													//Any additional Objects to reclaim are removed

	public RecyclingPool(Class<? extends Reclaimable> pooledClazz) {
		clazz = pooledClazz;
		slotMap = new ConcurrentHashMap<>();
		// reclList = new ArrayList<>();
		capacity = 0;
		reserves = MAX_RESERVES;
		bulletin = AdvancedGlobalBulletin.getSharedInstance();
		SystemInfo.getSharedInstance().registerSystemObject(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Reclaimable> T[] getContent() {
		// Reclaimable<?>[] ret = (Reclaimable<?>[])
		// Array.newInstance(prototype.getClass(), getTotalSize());

		if (content == null || capacity != content.length) {
			content = (Reclaimable[]) Array.newInstance(clazz, capacity);

			bulletin.RECLAIMCYCLE.Verbose.push("instantiated array",
					createPosDesc("getContent"),
					new Object[] { capacity });
		}

		int i = 0;

		for (ReclaimableContainer<?> cur : slotMap.values()) {
			if (!cur._isReclaimable) {
				content[i++] = (T) cur._object;
			}
		}

		return (T[]) content;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T extends Reclaimable> Collection<T> getContentAsCollection() {
		//Überdenken :
		/*
		 * Es könnte sein, das zwischen dem erzeugen von contentAsList ein eintrag entfernt und wieder hinzugefügt wird -> d.h
		 * es wird keine neue liste erzeugt und ein alter verweis steht drin o schlimmer ein Objekt das NULL ist wird referenziert
		 */

		if (contentAsList == null || capacity != contentAsList.size()) {
			contentAsList = new ArrayList<>(capacity);
			
			bulletin.RECLAIMCYCLE.Info.push("instantiated collection",
					createPosDesc("getContentAsList"),
					new Object[] { capacity });
		} else {
			contentAsList.clear();
		}
		
		for (ReclaimableContainer current : slotMap.values()) {
			if (!current._isReclaimable)
				((ArrayList)contentAsList).add(current._object);
		}
		
		return (Collection<T>) contentAsList;
	}

	@Override
	public Map<K, Reclaimable> getMappedContent() {
		Map<K, Reclaimable> ret = new HashMap<>(capacity);

		for (Entry<K, ReclaimableContainer<?>> entry : slotMap.entrySet()) {
			if (!entry.getValue()._isReclaimable) {
				ret.put(entry.getKey(), entry.getValue()._object);
			}
		}
		
		bulletin.RECLAIMCYCLE.Verbose.push("getMappedContent:class,mapsize", createPosDesc("getMappedContent"), new Object[]{clazz, ret.size()});
		
		return ret;
	}

	@Override
	public int getCapacity() {
		return capacity;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Reclaimable acquire(K slot) {
		ReclaimableContainer<?> ret = slotMap.get(slot); // Is there a slot given
														// already ?
		
		AdvancedGlobalBulletin bulletin = AdvancedGlobalBulletin.getSharedInstance();
		
		if (ret != null) {
			bulletin.RECLAIMCYCLE.Verbose.push("Object already exists, slot, ipath#", createPosDesc("acquire"), new Object[]{ret._object.getSlotID(),1});
			ret._isReclaimable = false;
			updateCapacities();
			return ret._object;
		}

		ret = findReclaimable();

		if (ret != null) {
			bulletin.RECLAIMCYCLE.Info.push("Recycling slot,for,ipath#", createPosDesc("acquire"), new Object[]{ret._object.getSlotID(),slot,2});
			slotMap.put(slot, slotMap.remove(ret._object.getSlotID()));
			ret._object.updateSlotID(slot);
			ret._isReclaimable = false;
			updateCapacities();
			return ret._object;
		}
		
		bulletin.RECLAIMCYCLE.Info.push("Object is being created, slot, ipath#", createPosDesc("acquire"), new Object[]{slot,3});
		Reclaimable temp = createClazzInstance();
		ret = new ReclaimableContainer(temp);
		slotMap.put(slot, ret);
		ret._isReclaimable = false;
		updateCapacities();
		return ret._object;
	}

	@Override
	public Reclaimable find(K slot) {
		ReclaimableContainer<?> ret = slotMap.get(slot);
		return (ret == null || ret._isReclaimable) ? null : ret._object;
	}

	@Override
	public void remove(K slot) {
		ReclaimableContainer<?> tmp = slotMap.get(slot);

		if (tmp != null) {
			if (reserves < MAX_RESERVES || !regardReserve) {
				tmp._isReclaimable = true;
				tmp._object.clearForReuse();
				bulletin.RECLAIMCYCLE.Info.push("Setting entry Reclaimable, capacity, reserves",
						createPosDesc("remove"),
						new Object[] { slot, capacity, reserves });
			} else {
				slotMap.remove(slot);
				bulletin.RECLAIMCYCLE.Info.push("Removing Reclaimable entry, capacity, reserves",
						createPosDesc("remove"),
						new Object[] { slot, capacity, reserves});
			}	
			updateCapacities();
		} else {
			bulletin.RECLAIMCYCLE.Info.push("Slot is not occupied, can not remove", 
					createPosDesc("remove"), 
					new Object[]{slot});
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void remove(Reclaimable entry) {
		remove((K) entry.getSlotID());
	}
	
	public void configureLimitReserve(boolean state) {
		regardReserve = state;
	}

	/**
	 * Finds the first best reclaimable in pool,
	 * 
	 * @return
	 */
	private final ReclaimableContainer<?> findReclaimable() {
		for (ReclaimableContainer<?> recl : slotMap.values()) {
			if (recl._isReclaimable)
				return recl;
		}
		return null;
	}

	private final Reclaimable createClazzInstance() {
		bulletin.RECLAIMCYCLE.Verbose.push("Instantiating", 
				createPosDesc("createClazzInstance"),
				new Object[] { clazz });
		Reclaimable ret = null;
		try {
			ret = clazz.newInstance();
		} catch (InstantiationException 
				| IllegalAccessException 
				| IllegalArgumentException 
				| SecurityException  e) {
			e.printStackTrace();
			bulletin.RECLAIMCYCLE.Error.push("Exception caught while trying to create instance of class, possibly it is missing an public empty-argument constructor", 
					createPosDesc ("createClazzInstance"),
					new Object[] {clazz.getSimpleName(),e.getMessage()});
		}
		return ret;
	}
	
			private final void updateCapacities () {
		int cap = 0;
		int res = 0;
		for (ReclaimableContainer<?> recl : slotMap.values()) {
			if (recl._isReclaimable)
				res++;
			else
				cap++;
		}
		capacity = cap;
		reserves = res;
	}

	@Override
	public SystemInfoElement getSystemInfo() {
//		final Class<? extends Reclaimable> clazz;
//
//		final Map<K, ReclaimableContainer> slotMap;
//
//		Reclaimable[] content;
//		
//		GlobalBulletin bulletin;
//
//		int reserves; // The count of reclaimable Objects
//		int capacity; // The count of non-reclaimable (i.e. "existing") entries in
//						// the slotmap
//		boolean regardReserve = true; //if there is a limit to the reclaimable objects kept in the pool 
//		private static final int MAX_RESERVES = 10;		//The maximum Count of Objects marked reclaimable,
//														//Any additional Objects to reclaim are removed
//		
		return new SystemInfoElement(
				"Pool", 
				"state", 
				"Pooled class: " + clazz.getName(),
				"SlotMap size: " + slotMap.size(),
				content != null ? "Content size: " + content.length : "content is null",
				"Reserves: " + reserves,
				"Capacity: " + capacity,
				"RegardReserve: " + regardReserve);
	}
	
	private String[] createPosDesc (String... position) {
		final String[] posBase = {"Reclaimable", "Pool"};
		
		final List<String> ret = new LinkedList<>(Arrays.asList(posBase));
		
		for (String current : position) {
			ret.add(current);
		}		

		return ret.toArray(new String[ret.size()]);
	}

	@Override
	public void clearCache() {
		content = null;
		if (contentAsList != null) {
			contentAsList.clear();
		}
		
		for (Entry<K, ReclaimableContainer<?>> entry : slotMap.entrySet()) {
			if (entry.getValue()._isReclaimable) {
				slotMap.remove(entry.getKey());
			}
		}
		
		updateCapacities();
	}
}
