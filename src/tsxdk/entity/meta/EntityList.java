package tsxdk.entity.meta;

import java.util.Collection;

import tsxdk.entity.TsEntity;

/**
 * Generic Type for Entity-lists.
 * Used For Channels, Clients, Complains
 * @param <E> The entity stored in this list
 * @param <S> The Slot for this entity (unique ID)
 */
public interface EntityList<E extends TsEntity, S> {
	/**
	 * @param slot The identifier for this slot
	 * @return An Entity for given slot
	 */
	E acquire (S slot);
	
	/**
	 * Sets all registered entities to the initial state
	 */
	void setInitial ();

	/**
	 * Sets all registered entities to the touched state
	 */
	void setTouched();
	
	/**
	 * Clears cached entities
	 */
	void clearCache();
	
	/**
	 * Removes all unused Entities
	 */
	void cleanUp();

	/**
	 * @return the count of entities in this list
	 */
	int getEntityCount ();

	TsEntity[] getIterable();

	Collection<? super E> getContentList();
}
