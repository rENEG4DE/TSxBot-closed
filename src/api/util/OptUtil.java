package api.util;

import api.data.TsEntityObject;
import api.service.UtilityProvider;

public interface OptUtil extends UtilityProvider {

	/*
	 * Persistence
	 */
	
	<T> void saveOptVar (TsEntityObject entity, String descriptor, T value);

	/*
	 * Persistence
	 */
	
	<T> T getOptVar (TsEntityObject entity, String descriptor);

	/*
	 * Persistence
	 */
	
	void deleteOptVar (TsEntityObject entity, String descriptor);

}
