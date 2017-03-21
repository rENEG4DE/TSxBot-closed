package utility.profiler.info;

import java.util.concurrent.TimeUnit;

public class HighestTimeLapseProfileInfo extends AbstractProfileInfo {

	public HighestTimeLapseProfileInfo(String name) {
		super(name);
		infoValue = Long.valueOf(-1l);
	}

	@Override
	protected void start() {
		if ((Long)infoValue == -1l) {
			infoValue = 0l;		//We need some value to start...
		}
	}

	@Override
	protected void stop() {
		final Long timeLapse = TimeUnit.NANOSECONDS.toMillis(stopTime - startTime);
		if (timeLapse > (Long)infoValue) infoValue = timeLapse;
	}
}
