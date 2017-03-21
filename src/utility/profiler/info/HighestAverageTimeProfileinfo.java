package utility.profiler.info;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;


public class HighestAverageTimeProfileinfo extends AbstractProfileInfo {
	
	private int activationCtr;
	private BigInteger summedUp = BigInteger.valueOf(0l);
	
	public HighestAverageTimeProfileinfo(String name) {
		super(name);
		infoValue = -1.0;		//We need some value to start...
	}

	@Override
	protected void stop() {
		activationCtr++;
		long timeLapsed = stopTime - startTime;
		summedUp = summedUp.add(BigInteger.valueOf(TimeUnit.NANOSECONDS.toMicros(timeLapsed)));
		final double average = summedUp.divide(BigInteger.valueOf(activationCtr * 1000L)).doubleValue();
		if (average > (Double)infoValue) infoValue = average;
	}

	@Override
	protected void start() {
		// TODO Auto-generated method stub
		
	}

}