package system.plugin.serviceprovider;

import java.util.Arrays;

import api.action.ActionContainer;
import api.query.AbstractQuery;
import api.util.ActionFactoryUtil;
import bot.action.instance.ActionBatch;
import bot.action.instance.QueryAction;
import bot.action.instance.SoundAction;
import bot.action.instance.TTSAction;

public class ActionFactoryUtilProvider implements ActionFactoryUtil{

	@Override
	public ActionContainer createTTSAction(long delay, long expire, String text) {
		return new TTSAction (delay, expire, text); 
	}

	@Override
	public ActionContainer createMP3Action(long delay, long expire, String file) {
		return new SoundAction (delay, expire, file);
	}

	@Override
	public ActionContainer createQueryAction(long delay, long expire, AbstractQuery query) {
		return new QueryAction(delay, expire, query);
	}

	@Override
	public ActionContainer createActionBatch(long delay, long expire, ActionContainer... actions) {
		ActionBatch batch = new ActionBatch (delay, expire, "");	//last argument is internally rreplaced...
		
		Arrays.stream(actions).forEach(batch::addAction);
		
		return batch;
	}

}
