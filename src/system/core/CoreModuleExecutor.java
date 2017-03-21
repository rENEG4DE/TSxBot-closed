package system.core;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import utility.systemInfo.SystemInfo;
import utility.systemInfo.SystemInfoElement;
import utility.systemInfo.SystemObject;

public class CoreModuleExecutor implements SystemObject{
	
	private static final class SingletonHolder {
		private static final CoreModuleExecutor INSTANCE = new CoreModuleExecutor();
	}
	private final ScheduledThreadPoolExecutor executor;
	
	private CoreModuleExecutor() {
		executor = new ScheduledThreadPoolExecutor(CoreModule.values().length);
		SystemInfo.getSharedInstance().registerSystemObject(this);
	}
	
	public static CoreModuleExecutor getSharedInstance () {
		return SingletonHolder.INSTANCE;
	}

	public void attachAll () {
		for (CoreModule mdl : CoreModule.values()) {
			try {
			mdl.attachTo(executor);
			} catch (Exception e) {
				System.out.println("error attaching " + mdl.name());
			}
		}
	}
	
	public void detachAll () {
		for (CoreModule mdl : CoreModule.values()) {
			mdl.detachFrom(executor);
		}
	}
	
	public void attach (String thread) {
		CoreModule mdl = CoreModule.valueOf(thread);
		if (!mdl.isAttached()) {
			mdl.attachTo(executor);
		}
	}
	
	public void detach (String thread) {
		CoreModule mdl = CoreModule.valueOf(thread);
		if (mdl.isAttached()) {
			mdl.detachFrom(executor);
		}
	}

	@Override
	public SystemInfoElement getSystemInfo() {
		return new SystemInfoElement("CoreModuleAttacher", "state", 
						"TaskCount: " + executor.getTaskCount(),
						"ActiveCount: " + executor.getActiveCount(),
						"LargestPoolSize: " + executor.getLargestPoolSize(),
						"CorePoolSize: " + executor.getCorePoolSize(),
						"PoolSize: " + executor.getPoolSize());
	}
}
