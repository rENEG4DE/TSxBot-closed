package utility.profiler.info;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.concurrent.TimeUnit;

public class TotalRuntimePercentageProfileInfo extends AbstractProfileInfo {

	long totalTime;
	
	public TotalRuntimePercentageProfileInfo(String name) {
		super(name);
		infoValue = -1l;
		totalTime = -1l;
	}

	@Override
	protected void start() {
	}

	@Override
	protected void stop() {
		RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
		
		totalTime = totalTime + (Long)TimeUnit.NANOSECONDS.toMillis(stopTime - startTime);
		final long runTime = System.currentTimeMillis() - mxBean.getUptime();
//		infoValue = (totalTime / (runTime * 0.01));
//		
//		//convert to 2 dec. places
		infoValue = ((int)((double)(totalTime / (runTime * 0.01)) * 100)) * 0.01;

	}

}
