package bot.action.instance;

import java.util.LinkedList;
import java.util.List;

import api.action.ActionContainer;

public class ActionBatch extends AbstractAction {

	final List<ActionContainer> actionBatch; 
	
	public ActionBatch(Long delay, Long expire, String whatToDo) {
		super(delay, expire, "ActionBatch");
		actionBatch = new LinkedList<>();
	}

	@Override
	public void execute() {
		for (ActionContainer action : actionBatch) {
			((AbstractAction)action).execute();
		}
	}

	public void addAction (ActionContainer action) {
		actionBatch.add(action);
	}
}
