package utility.profiler.info;

import utility.misc.StringMan;

public abstract class AbstractProfileInfo {
	private final String name;
	protected Object infoValue;
	protected long startTime;
	protected long stopTime;
	private boolean running;
	
	public AbstractProfileInfo(final String name) {
		this.name = name;
	}
	
	public String getStringRepresentation() {
		return StringMan.makeFixedLen(getName() + ":", 25) + getValue();
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return infoValue.toString();
	}
	
	public void start (long nanoStartTime) {
		this.startTime = nanoStartTime;
		running = true;
		start ();
	}
	
	public void stop (long nanoStopTime) {
		if (running) {
			this.stopTime = nanoStopTime;
			stop ();
			running = false;
		}
	}

	protected abstract void start();
	
	protected abstract void stop();
}
