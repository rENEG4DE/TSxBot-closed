package utility.reclaimable;

import java.util.Collection;
import java.util.Map;

public interface ReclaimableCollection<K> {

	//what DO we need ? :
	
	/*
	 * --If an Object is reclaimable it must have been "deleted" before somehow
	 * --If an Object is reclaimable it must not be made accessible from outside by implementations of this class
	 * --Get all entries as Collection (or Array ?)
	 * --Get absolute count of entries
	 * --Get count of non-reclaimables
	 * --"acquire"  :  Create new one if none exists OR return existing -- return something in all cases!
	 * 		STEPS TO ACQUIRE:
	 * 		-- look if there is an Object already present with that slot - if yes return that
	 * 		-- look if we have a reclaimable in our contents, set non reclaimable -- and return that
	 * 		-- create new instance, lock to slot, set non reclaimable -- return
	 * 
	 * --"find"  :  same as acquire, just without the object-creation
	 */
	
	
	<T extends Reclaimable> T[] getContent();
	<T extends Reclaimable> Collection<? extends T> getContentAsCollection ();
	
	Map<K,Reclaimable> getMappedContent();		//->? extends Reclaimable2   necessary?
	
	int getCapacity ();

	Reclaimable acquire(K slot);
	Reclaimable find (K slot);
	void remove(Reclaimable entry);
	void remove (K slot);
	void clearCache ();
}