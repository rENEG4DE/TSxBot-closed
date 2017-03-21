package system.plugin.serviceprovider;

import api.data.TsEntityObject;
import api.util.EntityPredicate;

public class EntityPredicateCompanion {

	public static <T extends TsEntityObject> boolean checkAgainst (T entity, EntityPredicate<T>[] predicate) {		
		for (EntityPredicate<T> pred : predicate) {
			if (pred.eval(entity) == false) return false;
		}
		return true;
	}

}
