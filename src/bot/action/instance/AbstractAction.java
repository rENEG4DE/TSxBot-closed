package bot.action.instance;

import api.action.ActionContainer;

public abstract class AbstractAction implements ActionContainer {
	private Long delay;			//The delay before this method is executed
	private Long expire;		//The absolute time this ActionMethod expires
	protected String whatToDo; 
	
	@SuppressWarnings("unused")
	private AbstractAction () {}
	
	protected AbstractAction (Long delay, Long expire, String whatToDo) {
		this.delay = delay;
		this.expire = (expire == 0L ? 0 : System.currentTimeMillis() + expire);
		this.whatToDo = whatToDo;
	}
	
	protected AbstractAction (Long delay, String whatToDo) {
		this (delay,0L,whatToDo);
	}
			
	public Long getDelay() {
		return delay;
	}
	
	public abstract void execute();	
	
	public String toString () {
		return whatToDo;
	}
	
	public boolean isExpired() {
		//TODO this should be ">" and not "<"
		return (expire != 0L && expire < System.currentTimeMillis());
	}
	
	
}
