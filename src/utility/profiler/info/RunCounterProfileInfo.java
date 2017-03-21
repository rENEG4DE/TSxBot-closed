package utility.profiler.info;

public class RunCounterProfileInfo extends AbstractProfileInfo {

	public RunCounterProfileInfo(String name) {
		super(name);
		infoValue = -1l;
	}

	@Override
	protected void start() {

	}

	@Override
	protected void stop() {
		infoValue = (long)infoValue + 1;
	}

}
