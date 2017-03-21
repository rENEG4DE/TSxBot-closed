package api.util;

import api.action.ActionContainer;
import api.query.AbstractQuery;
import api.service.UtilityProvider;

public interface ActionFactoryUtil extends UtilityProvider {
	ActionContainer createTTSAction (long delay, long expire, String text);
	ActionContainer createMP3Action (long delay, long expire, String file);
	ActionContainer createQueryAction (long delay, long expire, AbstractQuery query);
	ActionContainer createActionBatch(long delay, long expire, ActionContainer... actions);
}
