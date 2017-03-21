package system.core;

public enum Period {
		WORK_LIST	(60*1000),
		QUERY_TS (333),
		BRAIN_BEAT (1000),
		ACTION_BEAT (100),
		SNIFF_BEAT (5), 
		QUERY_DEPLOY(100),
		STATISTICS(60 * 1000),
		
		//Non Thread-timings - these are timing constants used anywhere in the bot
		QUERY_LIFETIME(300), 
		QUERY_DEPLOY_RHYTHM(20), 
		NIO_CHANNEL_WRITE_SLEEP(3);			//The time waited between query-pushing to the socket. If we do not do this, cpu-usage goes wild

		private long value;

		Period (long del) {
			value = del;
		}

		public long getValue() {
			return value;
		}
	}