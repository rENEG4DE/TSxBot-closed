package utility.reclaimable;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import utility.bulletin.AdvancedGlobalBulletin;
import utility.systemInfo.SystemInfo;
import utility.systemInfo.SystemInfoElement;
import utility.systemInfo.SystemObject;

/**
 * We rebuild our reclaimable-collection-type as a non reclaiming version,
 * maybe it will save us from some debugging trouble
 * maybe it will even be more performant as reusing objects might only be efficient with high object-counts
 * @param <K>
 */
public class ProdigalPool<K> implements ReclaimableCollection<K>, SystemObject {
	final Class<? extends Reclaimable> clazz;

	final Map<K, Reclaimable> slotMap;

	AdvancedGlobalBulletin bulletin;

	public ProdigalPool(Class<? extends Reclaimable> pooledClazz) {
		clazz = pooledClazz;
		slotMap = new ConcurrentHashMap<>();
		bulletin = AdvancedGlobalBulletin.getSharedInstance();
		SystemInfo.getSharedInstance().registerSystemObject(this);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Reclaimable> T[] getContent() {
		return slotMap.values().toArray((T[]) Array.newInstance(clazz, slotMap.size()));
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Reclaimable> Collection<? extends T> getContentAsCollection() {
		return (Collection<? extends T>) slotMap.values();
	}

	@Override
	public Map<K, Reclaimable> getMappedContent() {
		return slotMap;
	}

	@Override
	public int getCapacity() {
		return slotMap.size();
	}

	@Override
	public Reclaimable acquire(K slot) {
		Reclaimable ret = slotMap.get(slot);
		if(ret == null) {
			ret = createClazzInstance();
			slotMap.put(slot, ret);
		}
		return ret;
	}

	@Override
	public Reclaimable find(K slot) {
		return slotMap.get(slot);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void remove(Reclaimable entry) {
		remove((K)entry.getSlotID());
	}

	@Override
	public void remove(K slot) {
		slotMap.remove(slot);
	}

	@Override
	public void clearCache() {	
		//Do nothing -- nothing cached !
	}
	
	@Override
	public SystemInfoElement getSystemInfo() {
		return new SystemInfoElement(
				"ProdigalPool", 
				"state", 
				"Pooled class: " + clazz.getName(),
				"SlotMap size: " + slotMap.size());				
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

	private String[] createPosDesc (String... position) {
		final String[] posBase = {"Reclaimable", "Pool"};
		
		final List<String> ret = new LinkedList<>(Arrays.asList(posBase));
		for (String current : position) {
			ret.add(current);
		}
		
		return ret.toArray(new String[ret.size()]);
	}
}
