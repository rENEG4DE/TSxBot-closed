package utility.reclaimable;

/**
 * Reclaimable-Objects have to implement a simple set of rules. 
 */

public interface Reclaimable {
	Object getSlotID ();
	void updateSlotID (Object k);
	void clearForReuse ();
}
