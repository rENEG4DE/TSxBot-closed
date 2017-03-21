package utility.profiler.info;

import java.util.concurrent.TimeUnit;


public class TimeLapseProfileInfo extends AbstractProfileInfo {
	
	public TimeLapseProfileInfo(String name) {
		super(name);
		infoValue = -1l;
	}
	
	@Override
	protected void start() {}

	@Override
	protected void stop() {
		infoValue = Long.valueOf(TimeUnit.NANOSECONDS.toMillis(stopTime - startTime));
	}
}
