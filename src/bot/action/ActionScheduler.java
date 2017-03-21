package bot.action;

import java.util.concurrent.ConcurrentLinkedQueue;

import utility.bulletin.AdvancedGlobalBulletin;
import bot.BotException;
import bot.action.instance.AbstractAction;

public class ActionScheduler implements Runnable {
	private final static class SingletonHolder {
		private final static ActionScheduler INSTANCE = new ActionScheduler();
	}
	
	public final static ActionScheduler getSharedInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	private ConcurrentLinkedQueue<AbstractAction> workList = new ConcurrentLinkedQueue<>();
	private final static AdvancedGlobalBulletin bulletin = AdvancedGlobalBulletin.getSharedInstance();
//	private final static BasicProfiler profiler = BasicProfiler.getSharedInstance();
	
	@Override
	public void run() {
		if (!workList.isEmpty()) {
//			profiler.startProfile("scheduled-actions-executing");
			bulletin.ACTION.Info.push("Actions to be executed", new String[]{"ActionScheduler", "run", String.valueOf(workList.size())});
//			System.out.println("Got " + workList.size() + " methods to execute");
			for (AbstractAction method : workList) {
				try {
					if (!method.isExpired()) {
						bulletin.ACTION.Info.push(method.toString(), new String[]{"Action","Method","execute"});
//						System.out.println("[Action][Method][execute]" + method.toString());
						Thread.sleep(method.getDelay());
						method.execute();
					} else {
						bulletin.ACTION.Info.push(method.toString(), new String[]{"Action","Method","expire"});
//						System.out.println("[Action][Method][expire]" + method.toString());
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					throw new BotException("ActionScheduler.run()", "e1",
							"Something interrupted Threads' sleep!", e);
				}
				workList.remove();
//				profiler.endProfile0("scheduled-actions-executing".hashCode());
			}
		} //else {
////			System.out.println("Nothing to do!");
//		}
	};
	
	public void addAction(AbstractAction mtd) {
		workList.offer(mtd);
	}
}
