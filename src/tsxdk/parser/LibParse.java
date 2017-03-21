package tsxdk.parser;

import system.core.Context;
import system.core.Default;
import tsxdk.entity.EntityManager;
import tsxdk.entity.TsChannel;
import tsxdk.entity.TsChannelList;
import tsxdk.entity.TsClient;
import tsxdk.entity.TsClientList;
import tsxdk.entity.TsComplain;
import tsxdk.entity.TsComplainList;
import tsxdk.entity.TsEntity;
import tsxdk.entity.LibTsEvent;
import tsxdk.entity.TsReturn;
import tsxdk.entity.TsSimpleResult;
import tsxdk.entity.meta.EntityList;
import tsxdk.entity.meta.LibEntityState;
import utility.bulletin.AdvancedGlobalBulletin;
import bot.EventServer;

enum LibParse implements AbstractParser {
	TerminalParser {
		@Override
		public void parse(final String toParse) {
			// profiler.startProfile("Parser.TerminalParser.parse");
			for (String field : toParse.split("[ ]+")) {
				String[] tempFieldContents = field.split("[=]+", 2);
				String valueString = "";

				if (tempFieldContents.length == 2) {
					valueString = tempFieldContents[1];
				}

				LibTsSym symbol = null;

				try {
					symbol = LibTsSym.parseSymbol(tempFieldContents[0]);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					return;
				}
				Object value = LibConvert.getConvertFor(symbol).parse(valueString);
				fieldStack.push(symbol, value);

			}
			// profiler.endProfile();
		}
	},

	ReturnParser {
		@Override
		public void parse(final String toParse) {
			TerminalParser.parse(toParse);
			TsReturn ret = new TsReturn(
					(Integer) fieldStack.popFirst(LibTsSym.ID),
					(String) fieldStack.popFirst(LibTsSym.MSG));

			Context.getQueryAgent().pushReturn(ret);
		}
	},

	EventParser {
		@Override
		public void parse(String toParse) {

			// System.out.println("ChannelListParser active");
//			BasicProfiler profiler = BasicProfiler.getSharedInstance();
//			profiler.startProfile("Parser-parse-list");

			final String[] splitted = toParse.split(" ", 2);
			final LibTsSym notifySymbol = LibTsSym.parseSymbol(splitted[0]);
			toParse = splitted[1];

			TerminalParser.parse(toParse);

			switch (notifySymbol) {
				case NOTIFYTEXTMESSAGE: {
					LibTsEvent.TEXTMESSAGE.update(fieldStack);
					EventServer.getSharedInstance().triggerTextMessage(LibTsEvent.TEXTMESSAGE);
				}
					break;
				case NOTIFYCLIENTENTERVIEW: {
					LibTsEvent.CLIENTJOINED.update(fieldStack);
					// We can not set anything different than toParses hash -
					// because of this, an update event will be fired
					// as this client first appears in a client-list
					// ((TsClient)
					// TsEvent.CLIENTJOINED.getClient()).setTsPropHash(toParse);
					EventServer.getSharedInstance().triggerClientJoined(LibTsEvent.CLIENTJOINED);
				}
					break;
				case NOTIFYCLIENTLEFTVIEW: {
					LibTsEvent.CLIENTLEFT.update(fieldStack);
					EventServer.getSharedInstance().triggerClientLeft(LibTsEvent.CLIENTLEFT);
				}
					break;
				default: {
					Bulletin.PARSER.Error.push("unable to handle ", new String[] { "Parser", "EventParser" }, new Object[] { notifySymbol });
				}
					break;
			}

			if (fieldStack.getFieldCount() > 0) {
				Bulletin.PARSER.Info.push("unused elements on fieldStack ", new String[] { "Parser", "EventParser" }, new Object[] { fieldStack.getContent() });
			}

			fieldStack.ensureEmpty();
		}
	},

	ListParser {
		@Override
		public void parse(final String toParse) {
			// System.out.println("ChannelList: " + toParse);
			if (isSimpleResult(toParse)) {
				SimpleResultParser.parse(toParse);
				return;
			}
			EntityManager manager = EntityManager.getSharedInstance();

			// System.out.println("ChannelListParser active");
//			BasicProfiler profiler = BasicProfiler.getSharedInstance();
//			profiler.startProfile("Parser-parse-list");

			String listID = toParse.split("=", 2)[0];

			EntityList<? extends TsEntity, String> entityList;
			TsEntity entity = null;

			switch (LibTsSym.parseSymbol(listID)) {
				case CID: { // Channel list

					entityList = manager.getChannelList();
					entityList.setInitial();

					if (((TsEntity) entityList).getTsPropHash() != toParse.hashCode()) {
						String name = null;
						final String[] entities = toParse.split("[|]+");
						for (String current : entities) {
							// current = new String(current).intern();
							TerminalParser.parse(current);
							name = (String) fieldStack.popFirst(LibTsSym.CHANNEL_NAME);
							int cid = (Integer) fieldStack.peekFirst(LibTsSym.CID);
							entity = entityList.acquire(name + cid);

							if (current.hashCode() != entity.getTsPropHash()) {
								((TsChannel) entity).setChannel_name(name);
								entity.update(fieldStack);
								entity.setTsPropHash(current);
							}

							fieldStack.ensureEmpty();
							// current = null;
						}

						((TsEntity) entityList).setTsPropHash(toParse);
					} else {
						entityList.setTouched();
						Bulletin.ENTITYSTATES.Verbose.push("Nothing changed", new String[] { "ListParser", "parse", "ChannelList" });
					}

					manager.getChannelList().collectStates();
					entityList.cleanUp();
					Context.getQueryAgent().pushResult((TsChannelList) entityList);
				}
					break;
				case CLID: {
					entityList = manager.getClientList();
					entityList.setInitial();

					if (((TsEntity) entityList).getTsPropHash() != toParse.hashCode()) {
						String name = null;
						for (String current : toParse.split("[|]+")) {
							// current = new String(current).intern();
							TerminalParser.parse(current);
							name = (String) fieldStack.popFirst(LibTsSym.CLIENT_NICKNAME);
							entity = entityList.acquire(name);
							if (current.hashCode() != entity.getTsPropHash()) {
								((TsClient) entity).setClient_Nickname(name);
								entity.update(fieldStack);
								entity.setTsPropHash(current);
							}

							fieldStack.ensureEmpty();
							// current = null;
						}

						((TsEntity) entityList).setTsPropHash(toParse);

						if ((Boolean) Default.trap_unused_clients.getValue()) {
							for (TsEntity current : entityList.getIterable()) {
								TsClient client = (TsClient) current;
								if (client.getState() == LibEntityState.UNUSED) {
									Bulletin.PARSER.Info.push("Unused Client trapped", new String[] { "ListParser", "parse", "ClientList" },
											new Object[] { client.getClient_Nickname() });
									((TsClientList) entityList).remove(client);
								}
							}
						}
					} else {
						entityList.setTouched();
						Bulletin.PARSER.Verbose.push("Nothing changed", new String[] { "ListParser", "parse", "ClientList" });
					}

					manager.getClientList().collectStates();
					Context.getQueryAgent().pushResult((TsClientList) entityList);
				}
					break;
				case TCLDBID: {
					entityList = manager.getComplainList();
					entityList.setInitial();

					if (((TsEntity) entityList).getTsPropHash() != toParse.hashCode()) {
						String message = null;
						Long timestamp = 0L;
						for (String current : toParse.split("[|]+")) {
							// current = new String(current).intern();
							TerminalParser.parse(current);
							message = (String) fieldStack.popFirst(LibTsSym.MESSAGE);
							timestamp = (Long) fieldStack.popFirst(LibTsSym.TIMESTAMP);
							entity = entityList.acquire(timestamp.toString());

							if (current.hashCode() != entity.getTsPropHash()) {
								((TsComplain) entity).setMessage(message);
								((TsComplain) entity).setTimestamp(timestamp);
								// Bulletin.DEBUG.push("updated Complain-data",
								// new String[]{"ListParser","ComplainList"},new
								// Object[]{fieldStack.getContent()});
								entity.update(fieldStack);
								entity.setTsPropHash(current);
							}

							fieldStack.ensureEmpty();
							// current = null;
						}
						((TsEntity) entityList).setTsPropHash(toParse);
					} else {
						Bulletin.PARSER.Verbose.push("Nothing changed", new String[] { "ListParser", "parse", "ComplainList" });
						entityList.setTouched();
					}

					manager.getComplainList().triggerEvents();
					manager.getComplainList().collectStates();
//					entityList.cleanUp();
					Context.getQueryAgent().pushResult((TsComplainList) entityList);
				}
					break;
				default: {
					Bulletin.PARSER.Error.push("reached undefined state", new String[] { "ListParser", "parse" });
				}
					break;
			}

//			profiler.endProfile();
		}

		boolean isSimpleResult(final String toParse) {
			final String[] splitted = toParse.split("[=]+", 3);
			if (splitted.length < 3)
				return true;
			return false;
		}
	},

	SnapshotParser {
		public void parse(final String toParse) {
			// System.out.println("I am the SnapshotParser, I do nothing");
		}
	},

	AdvancedClientListParser {
		public void parse(final String toParse) {
			// System.out.println("I am the AdvancedChannelListParser, I do nothing");
		}
	},

	SimpleResultParser {
		public void parse(final String toParse) {
			TerminalParser.parse(toParse);
			TsSimpleResult result = new TsSimpleResult();
			result.update(fieldStack);
			Context.getQueryAgent().pushResult(result);
		}
	};

	static final TsFieldStack			fieldStack	= new TsFieldStack();
	private static final AdvancedGlobalBulletin	Bulletin	= AdvancedGlobalBulletin.getSharedInstance();

}
