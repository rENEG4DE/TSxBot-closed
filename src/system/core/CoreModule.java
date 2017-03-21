package system.core;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import system.plugin.serviceprovider.QueryProviderObject;
import tsxdk.entity.EntityManager;
import tsxdk.entity.LibTsEvent;
import tsxdk.io.query.engine.QueryAgent;
import tsxdk.io.query.engine.QueryEngine;
import tsxdk.parser.Parser;
import utility.bulletin.AdvancedGlobalBulletin;
import utility.log.Log;
import utility.profiler.AdvancedProfiler;
import utility.systemInfo.SystemInfo;
import api.data.TsClientDTI;
import bot.EventServer;

public enum CoreModule implements Runnable {
	// workList, loop all clients and print 'em
	PRINTCLIENTS {
		final long	period	= Period.WORK_LIST.getValue();

		// ProfileSet profile = getProfiler().createProfileSet("Work-list");
		@Override
		public final void run() {
			// System.out.println(this.name() + " is active" );
			try {
				// profile.start();
				Log tsLog = Context.getSharedInstance().getTsLog();
				tsLog.printSection("Loop all clients ");

				for (TsClientDTI client : EntityManager.getSharedInstance().getClientList().getIterable()) {
					tsLog.fine("[name] " + client.getClient_Nickname() + "[dbid] " + client.getClient_Database_id());
				}

				tsLog.printSeperator();
				// profile.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected long getPeriod() {
			return period;
		}
	},

	// taskedCmd, queries info from Teamspeak
	QUERYRUNTIMEDATA {
		final long	period	= Period.QUERY_TS.getValue();
		
		QueryProviderObject qpo = new QueryProviderObject();
		QueryAgent agent = Context.getQueryAgent();
		
		// ProfileSet profile = getProfiler().createProfileSet("Query-Runtime-data");

		@Override
		public final void run() {
			// System.out.println(this.name() + " is active" );
			try {
				// profile.start();
				// AsynchronousQueryAgent.getSharedInstance().resetDeniedCount();
				//log.fine("Tasked Commands");
				agent.pushQuery(qpo.requestComplainList());   
				agent.pushQuery(qpo.requestChannelList());
				agent.pushQuery(qpo.requestClientList());
				// profile.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected long getPeriod() {
			return period;
		}
	},

//	DEPLOYQUERIES {
//		final long	period	= Period.QUERY_DEPLOY.getValue();
//
//		ProfileSet	profile	= getProfiler().createProfileSet("Deploy-queries");
//
//		@Override
//		public final void run() {
////			// System.out.println(this.name() + " is active" );
////			try {
////				profile.start();
////				Context.getQueryAgent().deployAll();
////				profile.stop();
////			} catch (Exception e) {
////				e.printStackTrace();
////			}
//		}
//
//		@Override
//		protected long getPeriod() {
//			return period;
//		}
//	},

	// brainBeat, updates talk-times, causes Brain-beat-event
	BRAINBEAT {
		final long	period	= Period.BRAIN_BEAT.getValue();

		// ProfileSet profile = getProfiler().createProfileSet("Brain-beat");

		@Override
		public final void run() {
			// System.out.println(this.name() + " is active" );
			try {
				// profile.start();
				EventServer.getSharedInstance().triggerBrainBeat(LibTsEvent.BRAINBEAT);
				// profile.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected long getPeriod() {
			return period;
		}
	},

	// actionSched, Thread to execute actions from plugins etc.
	ACTION_SCHEDULER {
		final long	period	= Period.ACTION_BEAT.getValue();

		// ProfileSet profile = getProfiler().createProfileSet("Action-schedule");
		@Override
		public final void run() {
			// System.out.println(this.name() + " is active" );
			try {
				// profile.start();
				bot.action.ActionScheduler.getSharedInstance().run();
				// profile.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected long getPeriod() {
			return period;
		}
	},

	// tsSniffer, Collects Data from Teamspeak-server
//	STREAMER {
//		final long		period	= Period.SNIFF_BEAT.getValue();				// NOT USED !!!
//
//		ProfileSet		profile	= getProfiler().createProfileSet("Streamer");
//		final Parser	parser	= new Parser();
//
//		@Override
//		public final void run() {
//			// System.out.println(this.name() + " is active" );
//			try {
//				profile.start();
//				String l = tsxdk.io.TsPipe.getSharedInstance().in();
//				if (l != null) {
//					parser.parse(l);
//				}
//				profile.stop();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		@Override
//		protected long getPeriod() {
//			return period;
//		}
//	},

	QUERY_LOOP {
		private final static int	DEPLOY_MODE		= 1;
		private final static int	COLLECT_MODE	= 2;

		private int					mode			= DEPLOY_MODE;

		private final QueryAgent	agent			= Context.getQueryAgent();
		private final Parser		parser			= new Parser();

		@Override
		public void run() {
			try {
			if (mode == DEPLOY_MODE) {
//				System.out.println("DEPLOY_MODE");
				((QueryEngine) agent).deployQueryQueue();
				mode = COLLECT_MODE;
				return;
			} else if (mode == COLLECT_MODE) {
//				System.out.println("COLLECT_MODE");
				String l = tsxdk.io.TsPipe.getSharedInstance().in();

				while (l != null) {
					parser.parse(l);
					l = tsxdk.io.TsPipe.getSharedInstance().in();
				}

//				if (!((QueryEngine4) agent).hasExpectations()) {		// what happens if there are no expectations ?
					mode = DEPLOY_MODE;
//				}
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		protected long getPeriod() {
			return 50;
		}

	},
	
	SYSINFO {
		final long	period	= Period.STATISTICS.getValue();

		@Override
		public void run() {
			// System.out.println(this.name() + " is active" );
			getSysInfo().dumpInfos((String) Default.file_sysinfo.getValue());
		}

		@Override
		protected long getPeriod() {
			return period;
		}
	};

	private static AdvancedProfiler getProfiler() {
		if (profiler == null)
			profiler = AdvancedProfiler.getSharedInstance();
		return profiler;
	}

	private static SystemInfo getSysInfo() {
		if (sysInfo == null)
			sysInfo = SystemInfo.getSharedInstance();
		return sysInfo;
	}

	private static volatile AdvancedProfiler	profiler;
	private static volatile SystemInfo			sysInfo;

	private Future<?>							futureRef;
	private boolean								attached;

	public void setFutureRef(Future<?> future) {
		futureRef = future;
	}

	public Future<?> getFutureRef() {
		return futureRef;
	}

	protected abstract long getPeriod();

	public void attachTo(ScheduledThreadPoolExecutor executor) {
		if (!attached) {
			setFutureRef(executor.scheduleAtFixedRate(this, 0, getPeriod(), TimeUnit.MILLISECONDS));
			AdvancedGlobalBulletin.getSharedInstance().SYSTEM.Info.push(
					"Attach CoreModule to main thread-pool-executor, name, period",
					new String[] { "CoreModule", "attachTo" },
					new Object[] { this.name(), getPeriod() }
					);
			attached = true;
		}
	}

	public void detachFrom(ScheduledThreadPoolExecutor executor) {
		if (attached) {
			Future<?> futureRef = getFutureRef();
			if (!futureRef.isDone() && !futureRef.isCancelled()) {
				AdvancedGlobalBulletin.getSharedInstance().SYSTEM.Info.push(
						"Detach CoreModule from main thread-pool-executor, name",
						new String[] { "CoreModule", "detachFrom" },
						new Object[] { this.name() }
						);
				futureRef.cancel(true);
			}
			executor.remove(this);
			attached = false;
		}
	}

	public boolean isAttached() {
		return attached;
	}
}
