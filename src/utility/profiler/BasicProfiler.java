package utility.profiler;

import java.math.BigInteger;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import utility.reclaimable.RecyclingPool;
import utility.reclaimable.Reclaimable;
import utility.reclaimable.ReclaimableCollection;


public class BasicProfiler {
	private static final class SingletonHolder {
		private static final BasicProfiler INSTANCE = new BasicProfiler();
	}
	
	public final static BasicProfiler getSharedInstance() {
		return SingletonHolder.INSTANCE;
	}
	
//	private final static GlobalBulletin bulletin = GlobalBulletin.getSharedInstance();
	
//	private Map<String, ProfJob> jobs;
	private ReclaimableCollection<Integer> jobs;
	private Stack<Integer> lastStarted;
	
	private BasicProfiler() {
//		jobs = new HashMap<>();
		jobs = new RecyclingPool<>(ProfJob.class);
		((RecyclingPool<?>) jobs).configureLimitReserve(false);
//		jobs = new SingletonPool<>(new ProfJob("prototye"));
		lastStarted = new Stack<> ();
	}
	
	public void startProfile (String profJobName) {
//		System.out.println("Starting profile " + profJobName);

//		jobs.put(profJobName, new ProfJob(lastStarted.peek()));
		ProfJob job = (ProfJob) jobs.find(profJobName.hashCode()); 
		if (job == null) {
			job = (ProfJob) jobs.acquire(profJobName.hashCode());
			job.setup(profJobName);
		} else {
			job.restart();
		}
		lastStarted.push(profJobName.hashCode());
	}
	
	public ProfileJobResult endProfile () {
//		System.out.println("Ending profile " + lastStarted);
		return endProfile0(lastStarted.pop());
	}
	
	public ProfileJobResult endProfile0 (Integer profJob) {
		return ((ProfJob)jobs.find(profJob)).endJob();
	}
	
	public final static class ProfileJobResult {
		public final String name;
		public final Long timeLapsedNs;
		public final Double average;
		
		public ProfileJobResult(final String name, final long timeLapsed, final double average) {
			this.name = name;
			this.timeLapsedNs = timeLapsed;
			this.average = average;
		}
	}
		
	public final static class ProfJob implements Reclaimable{
		private Long beginTime;
		private Long endTime;
		private Long timeLapsed;
		private BigInteger summedUp;
		private Double average;
		private int activationCtr;
		
		private String name;
		
		public ProfJob () {
			activationCtr = 0;
			summedUp = BigInteger.valueOf(0);
			restart ();
		}
		
		@Override
		public void clearForReuse() {
			beginTime = endTime = timeLapsed = 0L;
			summedUp = BigInteger.valueOf(0l);
			average = 0.0;
			activationCtr = 0;
			name = null;
		}
		
		void setup (String name) {
			this.name = name;
		}
		
		private ProfileJobResult endJob () {
			endTime = System.nanoTime();
			timeLapsed = endTime - beginTime;
			summedUp = summedUp.add(BigInteger.valueOf(TimeUnit.NANOSECONDS.toMicros(timeLapsed)));
			activationCtr++;
			average = summedUp.divide(BigInteger.valueOf(activationCtr)).doubleValue();
			//wenn das Mikrosekunden-zeichen (µ) seltsam aussieht stimmt etwas mit der Zeichenkodierung nicht ! (UTF-8!)
//			bulletin.PROFILER.push("took (µs),(average)", 
//					new String[] {"Profile", name}, 
//					new Object[] {TimeUnit.NANOSECONDS.toMicros(timeLapsed), average});
			
			return new ProfileJobResult(name, timeLapsed, average);
		}

		public void restart () {
			this.endTime = 0L;
			this.timeLapsed = 0L;
			this.beginTime = System.nanoTime();
		}

		@Override
		public Object getSlotID() {
			return name;
		}

		@Override
		public void updateSlotID(Object k) {
			name = (String) k;
		}
		}	
}
