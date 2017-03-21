package api.util;

import api.data.TsEntityObject;

@FunctionalInterface
public interface EntityPredicate<T extends TsEntityObject> {
	boolean eval (T entity);
}
