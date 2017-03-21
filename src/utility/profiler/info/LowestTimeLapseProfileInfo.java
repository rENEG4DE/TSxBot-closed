package utility.profiler.info;

import java.util.concurrent.TimeUnit;

public class LowestTimeLapseProfileInfo extends AbstractProfileInfo {

	public LowestTimeLapseProfileInfo(String name) {
		super(name);
		infoValue = Long.valueOf(-1l);
	}

	@Override
	protected void start() {
	}

	@Override
	protected void stop() {
		final Long timeLapse = TimeUnit.NANOSECONDS.toMillis(stopTime - startTime);
		if ((Long)infoValue == -1l || timeLapse < (Long)infoValue) {
			infoValue = timeLapse;
		}
	}
}
