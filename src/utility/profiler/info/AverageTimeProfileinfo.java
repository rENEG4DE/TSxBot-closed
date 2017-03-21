package utility.profiler.info;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;


public class AverageTimeProfileinfo extends AbstractProfileInfo {
	
	private int activationCtr;
	private BigInteger summedUp = BigInteger.valueOf(0l);
	
	public AverageTimeProfileinfo(String name) {
		super(name);
		infoValue = -1.0;
	}

	@Override
	protected void start() {
		activationCtr++;
	}
	
	@Override
	protected void stop() {
		long timeLapsed = stopTime - startTime;
		summedUp = summedUp.add(BigInteger.valueOf(TimeUnit.NANOSECONDS.toMicros(timeLapsed)));
		infoValue = summedUp.divide(BigInteger.valueOf(activationCtr * 1000L)).doubleValue();
	}
}
