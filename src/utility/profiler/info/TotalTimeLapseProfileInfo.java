package utility.profiler.info;

import java.util.concurrent.TimeUnit;

public class TotalTimeLapseProfileInfo extends AbstractProfileInfo {

	public TotalTimeLapseProfileInfo(String name) {
		super(name);
		infoValue = -1l;
	}

	@Override
	protected void start() {
	}

	@Override
	protected void stop() {
		infoValue = (Long)infoValue + (Long)TimeUnit.NANOSECONDS.toMillis(stopTime - startTime);
	}

}
