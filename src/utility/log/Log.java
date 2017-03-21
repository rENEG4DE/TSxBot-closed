package utility.log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import utility.bulletin.AdvancedGlobalBulletin;
//import java.text.SimpleDateFormat;


/**
 * @author kornholio
 * 
 */
public class Log extends Logger {

	private static final int LOGLINELEN = 54;

	private static final String SHARED_TARGET_LOGNAME = "shared";

	private static final String FILE_EXT = ".txt";
	
//	private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("HH:mm:ss");

	private static final boolean ULTRA_DBG = false;
	
	private static final long startTime = System.currentTimeMillis();

	private static final Formatter FORMATTER = new Formatter() {
		@Override
		public String format(LogRecord record) {
			String aux = "";
			if (ULTRA_DBG) {
				aux = "[" + record.getSourceClassName() + "." + record.getSourceMethodName() + "]";
			}
			//return aux + "[" + DATEFORMAT.format(record.getMillis()) + "]" + formatMessage(record) + "\n";		//Use this for nice Date-output
			return aux + "[" + (record.getMillis() - startTime) + "]" + formatMessage(record) + "\n";				//Use this for millisecond-precision-logging
		}
	};
	
	private static final Formatter BASICFORMATTER = new Formatter() {
		@Override
		public String format(LogRecord record) {
			return super.formatMessage(record) + "\n";
		}
	};

	public static enum Target {
		STDOUT, STDERR, FILE, SHARED
	}
	
	private static PrintStream stdout;
	private static PrintStream stderr;
	
	private Handler handler;
	private Target target = Target.STDOUT;

	private void setHandler(Handler handler) {
		if (this.handler != null) {
			removeHandler(this.handler);
		}

		addHandler(handler);
	}
	
	public void setUseBasicFormatter () {
		handler.setFormatter(BASICFORMATTER);
	}
	
	private Handler createLogFileHandler (String name) {
		String fName = "log" + File.separator + name + FILE_EXT;
		try {
			return new FileHandler(fName, false);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
			throw new RuntimeException ("Could not create Filehandler for " + fName, e);
		}
	}

	public void setTarget(Target target) {
		if (this.target == target) {
			// System.out("Given Target already set");
			return;
		}

//		System.out.println("Setting target of " + getName() + " to " + target.toString());

		switch (target) {
		case FILE: {

			// FileHandler aufsetzen
			handler = createLogFileHandler(getName());
			handler.setFormatter(FORMATTER);

			final Log thiz = this;
			Runtime.getRuntime().addShutdownHook(new Thread() {
				private final Handler handle = handler;
				@Override
				public void run() {
					System.out.println("Closing " + thiz.getName() + " handle: " + handle);
					fine("Closing " + thiz.getName());
					handle.close();
				}
			});

			break;
		}
		case SHARED: {
			
			Logger logger = LogManager.getLogManager().getLogger(SHARED_TARGET_LOGNAME);
			if (logger == null) {

				handler = createLogFileHandler(SHARED_TARGET_LOGNAME);
				handler.setFormatter(FORMATTER);
				
			} else {
				handler = logger.getHandlers()[0];
			}

			break;
		}
		case STDOUT: {
			handler = new Handler() {
				@Override
				public void publish(LogRecord record) {
					System.out.print(getFormatter().format(record));
					System.out.flush();
				}

				@Override
				public void flush() {
				}

				@Override
				public void close() throws SecurityException {
					flush();
				}
			};
			break;
		}
		case STDERR: {
			handler = new Handler() {
				@Override
				public void publish(LogRecord record) {
					System.err.print(getFormatter().format(record));
				}

				@Override
				public void flush() {
				}

				@Override
				public void close() throws SecurityException {
					flush();
				}
			};
			break;
		}
		}

		// Formatter einstellen

		setHandler(handler);
	}

	public Log(String name, Level level, Target target) {
		super(name, null);
		setUseParentHandlers(false);
		setLevel(level);
		
		if (!LogManager.getLogManager().addLogger(this)) {
//			throw new RuntimeException("Log already exists {name:" + name + "}");
			AdvancedGlobalBulletin.getSharedInstance().SYSTEM.Info.push(" tried to double-initialize", new String[]{"Log","ctor"}, new String[]{name});
		} 
		
		if (target == Target.SHARED && LogManager.getLogManager().getLogger(SHARED_TARGET_LOGNAME) == null) {
			new Log(SHARED_TARGET_LOGNAME, Level.FINEST, Target.FILE);
		}
		
		setTarget(target);
	}
	

	
	/*
	 * Achtung: wird einer der Streams stdout oder stderr umgeleitet, 
	 * reseted und anschlie�end wieder umgeleitet, 
	 * so gehen die vorher in die Die entsprechenden Dateien geschriebenen Informationen VERLOREN!
	 */
	
	/**
	 * Static Method to redirect stdout to a file
	 */
	public synchronized void redirectStdOut2File () {
		if (stdout == null) {
			try {
				stdout = new PrintStream(new File("stdout" + FILE_EXT));
				System.setOut(stdout);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				// System.out("Could not open file");
			}
		}
	}
	
	/**
	 * Static Method to redirect stderr to a file
	 */
	public synchronized void redirectStdErr2File () {
		if (stderr == null) {
			try {
				stderr = new PrintStream(new File("stderr" + FILE_EXT));
				System.setErr(stderr);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				// System.out("Could not open file");
			}
		}
	}
	
	/**
	 * Reset a selected streamtarget to its default
	 * @param target the stream to reset
	 */
	public static void resetStream(Target target) {
		switch(target){
		case STDOUT:{
			Log.stdout=null;
			System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.out), 128), true));
			break;
		}
		case STDERR:{
			Log.stderr=null;
			System.setErr(new PrintStream(new BufferedOutputStream(new FileOutputStream(FileDescriptor.err), 128), true));
			break;
		}
		default: {} break;
		}	
	}
	/*********
	 * Convenience Methods start here
	 ********/
	
	/**
	 * Prints an seperator-line to the log-stream
	 */
	public void printSeperator () {
		//Neat trick :)
		info(new String(new char[LOGLINELEN]).replace("\0", "*"));
	}
	
	public void printSeperatorLv (Level lv) {
		if (getLevel().intValue() >= lv.intValue())
			printSeperator();
	}
	
	/**
	 * Prints a nice-ascii-formated Block for better overview in Logfile
	 */
	public void printBlock (String blockName) {
		printSeperator();
		printSection(blockName);
		printSeperator();
	}
	
	/**
	 * Prints a nice-ascii-formated section-identifier
	 * @param sector
	 */
	public void printSection (String sector) {
		if (sector.length() >= LOGLINELEN)
		{
			printSeperator();
			info (sector);	
		}
		else {
			int len = (LOGLINELEN - (sector.length() + 2)) / 2;
			
			StringBuilder builder = new StringBuilder(LOGLINELEN);
			builder.append(new String(new char[len]).replace("\0", "*"));
			builder.append(" ");
			builder.append(sector);
			builder.append(" ");
			builder.append(new String(new char[len]).replace("\0", "*"));
			if (builder.length() < LOGLINELEN) 
				builder.append(new String(new char[LOGLINELEN - builder.length()]).replace("\0", "*"));
			
			info (builder.toString());
		}
	}
	
	/**
	 * Print section only if Log has given Level set
	 * @param sector
	 * @param lv
	 */
	public void printSectionLv (String sector, Level lv) {
		if (getLevel().intValue() >= lv.intValue())
			printSection(sector);
	}
	
	/**
	 * Prints a nice-ascii-formated section-identifier
	 * @param subSector
	 */
	public void printSubSection (String subSector) {
		if (subSector.length() >= LOGLINELEN)
		{
			printSeperator();
			info (subSector);
		}
		else {
			int len = (LOGLINELEN - (subSector.length() + 2)) / 2;
			StringBuilder builder = new StringBuilder(LOGLINELEN);
			builder.append(new String(new char[len]).replace("\0", "*"));
			builder.append(" ");
			builder.append(subSector);
			builder.append(" ");
			builder.append(new String(new char[len]).replace("\0", "*"));
			
			if (builder.length() < LOGLINELEN) 
				builder.append(new String(new char[LOGLINELEN - builder.length()]).replace("\0", "*"));
			
			info (builder.toString());
		}
	}
	
	
}
