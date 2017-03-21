package utility.bulletin;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents a Global "bulletin-board", 
 * flat List with String entries for general purpose notifications 
 * (file not found, Can not find parser etc..)
 * @deprecated
 */
public class GlobalBulletin {
	private static final class SingletonHolder {
		private static final GlobalBulletin INSTANCE = new GlobalBulletin();
	}
	
	public static final GlobalBulletin getSharedInstance () {
		return SingletonHolder.INSTANCE;
	}
	
	private static final ConcurrentLinkedQueue<String> bullets = new ConcurrentLinkedQueue<>();
	
	private static final float LOADFACTOR = 0.75f;		//if count of bullets reaches MaxSize,
														//	MaxSize * ( 1 - loadFactor ) bullets are removed from stack 
	private static final int MAXSIZE = 100;

	public  Switch DEBUG = Switch.Disabled;			//Please keep output here low, 
															//use for test-purposes and when you're done DELETE or choose other switch
	public  Switch VERBOSE = Switch.Disabled;			//Lots of output!
	public  Switch INFO = Switch.Enabled;
	public  Switch ERROR = Switch.Enabled;	
	public  Switch EXCEPTION = Switch.Disabled;
	
	public  Switch QUERYLISTENERS = Switch.Disabled;		//TsQuery interaction
	public  Switch QUERYSTATE = Switch.Disabled;		//TsQuery interaction
	public  Switch QUERYENGINE = Switch.Enabled;
	public  Switch QUERY = Switch.Enabled;		//TsQuery interaction
	public  Switch QUERYDEPLOY = Switch.Disabled; //Query deployment
	public  Switch QUERYRETURN = Switch.Disabled; //Query deployment
	public 	Switch QUERYEXPECTATION = Switch.Disabled;
	
	public  Switch RECLAIMCYCLE = Switch.Disabled; //Reclaimcycle 	//lots of output !
	public  Switch RECLAIMCYCLE_VERBOSE = Switch.Disabled; //Reclaimcycle 	//lots of output !
	public  Switch ENTITYSTATES = Switch.Disabled; 	//Entity-states
	
	public  Switch USRCMD = Switch.Disabled;		//User-command-notifications
	public  Switch STATE = Switch.Disabled;		//Entity-states
	public  Switch PIPE_IN =Switch.Disabled;		//Low-Level TS INPUT
	public  Switch PIPE_OUT = Switch.Disabled;		//Low-Level TS OUTPUT
	public  Switch ACTION = Switch.Disabled;	//Action execute or expire
	public  Switch EVENT = Switch.Disabled;		//Event info
	public  Switch PROFILER = Switch.Disabled;	//Profiler data
	public  Switch DATABASE = Switch.Disabled; //Database - currently only used for uncaught exception 
	
	private GlobalBulletin () {
	}
	
	public void clear () {
		bullets.clear();
	}
	
	public void enableAllChannels () {
		DEBUG = VERBOSE = INFO = ERROR = EXCEPTION = QUERYLISTENERS = QUERYSTATE = QUERY = RECLAIMCYCLE = 
				ENTITYSTATES = USRCMD = STATE = PIPE_IN = PIPE_OUT = ACTION = EVENT = PROFILER = DATABASE = Switch.Enabled;
	}
	
	public void disableAllChannels () {
		DEBUG = VERBOSE = INFO = ERROR = EXCEPTION = QUERYLISTENERS = QUERYSTATE = QUERY = RECLAIMCYCLE = 
				ENTITYSTATES = USRCMD = STATE = PIPE_IN = PIPE_OUT = ACTION = EVENT = PROFILER = DATABASE = Switch.Disabled;
	}
	
	public void restoreChannelDefaults () {
		DEBUG = INFO = ERROR = EXCEPTION = Switch.Enabled;
		
		ACTION = PROFILER = Switch.Enabled;
		
		QUERYLISTENERS = QUERYSTATE = QUERY = RECLAIMCYCLE = ENTITYSTATES = Switch.Enabled;
		
		USRCMD = STATE = PIPE_IN = PIPE_OUT = EVENT = DATABASE = Switch.Disabled;
	}

	public static final String createBullet (String bullet, String[] posDesc, Object[] values) {
		StringBuilder builder = new StringBuilder(bullet);
		builder.append('{');
		Object current;
		for (int i = 0; i < values.length; i++) {
			current = values[i];
			if (current != null) 
				builder.append(current.toString());
			else
				builder.append("[null]");
			if (i != values.length - 1)
				builder.append(",");
		}
		
		builder.append('}');
		
		return createBullet(builder.toString(), posDesc);
	}
	
	public static final String createBullet (String bullet, String[] posDesc) {
		String ret = bullet;
		if (posDesc != null) 
			ret = (Arrays.deepToString(posDesc).replace(", ","][") + bullet);

		return ret;
	}
	
	
	/**
	 * For unit-testing
	 */
	public void printBoard() {
		for (String current : bullets) {
			System.out.println(current);
		}
	}
	
	public int getStackSize () {
		return bullets.size();
	}
	
	public int getMaxStackSize () {
		return MAXSIZE;
	}
}
