package utility.bulletin;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Represents a Global "bulletin-board", 
 * flat List with String entries for general purpose notifications 
 * (file not found, Can not find parser etc..)
 */
public class AdvancedGlobalBulletin {
	private static final class SingletonHolder {
		private static final AdvancedGlobalBulletin INSTANCE = new AdvancedGlobalBulletin();
	}
	
	public static final AdvancedGlobalBulletin getSharedInstance () {
		return SingletonHolder.INSTANCE;
	}
	
	static final ConcurrentLinkedQueue<String> bullets = new ConcurrentLinkedQueue<>();
	
	static final float LOADFACTOR = 0.75f;		//if count of bullets reaches MaxSize,
														//	MaxSize * ( 1 - loadFactor ) bullets are removed from stack 
	static final int MAXSIZE = 100;

	public BulletinChannel						SYSTEM;
	public BulletinChannel						PARSER;
	public BulletinChannel						BULLETIN;
	public BulletinChannel						QUERYLISTENERS;								// TsQuery interaction
	public BulletinChannel						QUERYSTATE;									// TsQuery interaction
	public BulletinChannel						QUERYENGINE;
	public BulletinChannel						QUERY;											// TsQuery interaction
	public BulletinChannel						QUERYDEPLOY;									// Query deployment
	public BulletinChannel						QUERYRETURN;									// Query deployment
	public BulletinChannel						QUERYEXPECTATION;

	public BulletinChannel						RECLAIMCYCLE;									// Reclaimcycle //lots of output !
	public BulletinChannel						ENTITYSTATES;									// Entity-states

	public BulletinChannel						USRCMD;										// User-command-notifications
	public BulletinChannel						STATE;											// Entity-states
	public BulletinChannel						PIPE_IN;										// Low-Level TS INPUT
	public BulletinChannel						PIPE_OUT;										// Low-Level TS OUTPUT
	public BulletinChannel						ACTION;										// Action execute or expire
	public BulletinChannel						EVENT;											// Event info
	public BulletinChannel						PROFILER;										// Profiler data
	public BulletinChannel						DATABASE;										// Database - currently only used for uncaught exception

	private final BulletinChannel[]				channels;

	Switch										Debug		= Switch.Enabled;
	Switch										Info		= Switch.Enabled;
	Switch										Verbose		= Switch.Enabled;
	Switch										Warn		= Switch.Enabled;
	Switch										Error		= Switch.Enabled;

	private AdvancedGlobalBulletin() {
		SYSTEM = new BulletinChannel(this);
		PARSER = new BulletinChannel(this);
		BULLETIN = new BulletinChannel(this);
		QUERYLISTENERS = new BulletinChannel(this); // TsQuery interaction
		QUERYSTATE = new BulletinChannel(this); // TsQuery interaction
		QUERYENGINE = new BulletinChannel(this);
		QUERY = new BulletinChannel(this); // TsQuery interaction
		QUERYDEPLOY = new BulletinChannel(this); // Query deployment
		QUERYRETURN = new BulletinChannel(this); // Query deployment
		QUERYEXPECTATION = new BulletinChannel(this);

		RECLAIMCYCLE = new BulletinChannel(this); // Reclaimcycle //lots of output !
		ENTITYSTATES = new BulletinChannel(this); // Entity-states

		USRCMD = new BulletinChannel(this); // User-command-notifications
		STATE = new BulletinChannel(this); // Entity-states
		PIPE_IN = new BulletinChannel(this); // Low-Level TS INPUT
		PIPE_OUT = new BulletinChannel(this); // Low-Level TS OUTPUT
		ACTION = new BulletinChannel(this); // Action execute or expire
		EVENT = new BulletinChannel(this); // Event info
		PROFILER = new BulletinChannel(this); // Profiler data
		DATABASE = new BulletinChannel(this); // Database - currently only used for uncaught exception

		
//		System.out.println("Initialized BulletinChannels!");
		
		channels = new BulletinChannel[]
				{
					SYSTEM,
					PARSER,
					BULLETIN,
					QUERYLISTENERS,QUERYSTATE,QUERYENGINE,QUERY,QUERYDEPLOY,QUERYRETURN,QUERYEXPECTATION,
					RECLAIMCYCLE,
					ENTITYSTATES,
					USRCMD,
					STATE,
					PIPE_IN,PIPE_OUT,
					ACTION,
					EVENT,
					PROFILER,
					DATABASE
				};
	}
	

	public void disableAllChannels() {
		for (BulletinChannel channel : channels) {
			channel.disableAll();
		}
	}
	
	public void enableAllChannels() {
		for (BulletinChannel channel : channels) {
			channel.enableAll();
		}
	}
	
	public void clear () {
		bullets.clear();
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
