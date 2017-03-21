package tsxdk.entity;

import static tsxdk.parser.LibTsSym.*;
import tsxdk.entity.meta.LibEntityState;
import tsxdk.parser.LibTsSym;
import tsxdk.parser.TsFieldStack;
import utility.bulletin.AdvancedGlobalBulletin;
import utility.misc.BatchSetter;
import api.data.EventDTI;
import api.data.TsClientDTI;
import api.data.TsComplainDTI;
import api.data.TsEntityType;

public enum LibTsEvent implements TsEntity, EventDTI {
	CLIENTJOINED {
			
		transient TsClient client;

		int cfid; 
		int ctid;
		int reasonid;
		int client_outputonly_muted;
		int client_talk_request;
		String client_talk_request_msg;
		int client_unread_messages;
		
		@Override
		public TsEntityType getType() {
			return TsEntityType.TSEVENTCLIENTJOINED;
		}

		public void prepareForInsert(BatchSetter setter) {
			setter.injectArgs(getEventDescriptor(),getTimeStamp(),0,cfid,ctid,reasonid,client_outputonly_muted,
					client_talk_request,client_talk_request_msg,"",0,0,0,"","","");
		}
		
		@Override
		public void update(TsFieldStack stack) {
			//Create the client if it does not exist....
			
			TsClientList clients = EntityManager.getSharedInstance().getClientList(); 
			String name = (String) stack.popFirst(CLIENT_NICKNAME);
			
			client = clients.acquire(name);			//Assure the client exists
			client.setClient_Nickname(name);
			client.setInitial();
			client.update(stack);

			cfid = (Integer) stack.popFirst(CFID);
			ctid = (Integer) stack.popFirst(CTID);
			reasonid = (Integer) stack.popFirst(REASONID);
			client_outputonly_muted = (Integer) stack.popFirst(CLIENT_OUTPUTONLY_MUTED);
			client_talk_request = (Integer) stack.popFirst(CLIENT_TALK_REQUEST);
			client_talk_request_msg = (String) stack.popFirst(CLIENT_TALK_REQUEST_MSG);
			client_unread_messages = (Integer) stack.popFirst(CLIENT_UNREAD_MESSAGES);
		}
		
		@Override
		public TsClientDTI getClient() {
			return client;
		}
		
		@Override
		public int getCfid() {
			return cfid;
		}

		@Override
		public int getCtid() {
			return ctid;
		}

		@Override
		public int getReasonid() {
			return reasonid;
		}
		
		@Override
		public int getClient_outputonly_muted() {
			return client_outputonly_muted;
		}

		@Override
		public int getClient_talk_request() {
			return client_talk_request;
		}

		@Override
		public String getClient_talk_request_msg() {
			return client_talk_request_msg;
		}

		@Override
		public int getClient_unread_messages() {
			return client_unread_messages;
		}
	},
	
	CLIENTLEFT {
		transient TsClientDTI client;
		int clid;
		int cfid; 
		int ctid;
		int reasonid;
		String reasonmsg = "";
		
		public void prepareForInsert(BatchSetter setter) {
			setter.injectArgs(getEventDescriptor(),getTimeStamp(),clid,cfid,ctid,reasonid,0,
					0,"",reasonmsg,0,0,0,"","","");
		}
		
		@Override
		public TsClientDTI getClient() {
			return client;
		}
		
		@Override
		public int getClid() {
			return cfid;
		}
		
		@Override
		public int getCfid() {
			return cfid;
		}

		@Override
		public int getCtid() {
			return ctid;
		}

		@Override
		public int getReasonid() {
			return reasonid;
		}
		
		@Override
		public String getReasonmsg() {
			return reasonmsg;
		}
		
//		public void setClient(TsClientDTI client) {
//			this.client = client;
//		}
		
		@Override
		public TsEntityType getType() {
			return TsEntityType.TSEVENTCLIENTLEFT;
		}
		
		@Override
		public void update(TsFieldStack stack) {

//			for (TsField field : stack.getFields()) {
//				System.out.println(field.symbol);
//			}
			
			clid = (Integer) stack.popFirst(CLID);
			cfid = (Integer) stack.popFirst(CFID);
			ctid = (Integer) stack.popFirst(CTID);
			reasonid = (Integer) stack.popFirst(REASONID);
			reasonmsg = (String) stack.popFirst(REASONMSG);

			client = EntityManager.getSharedInstance().getClientList().selectClientClidEquals(clid);
			if (client == null) {
				AdvancedGlobalBulletin.getSharedInstance().EVENT.Error.push(
						" unable to bind client to event / can not find client with clid", 
						new String[]{"TsEvent","CLIENTLEFT"}, 
						new Integer[]{clid}
				);
				return;
			}
			((TsClient) client).setState(LibEntityState.UNUSED);
			EntityManager.getSharedInstance().getClientList().remove(client);
		}
	},
	
	COMPLAINCOMMITTED {
		transient private TsComplain complain;
		
		public void prepareForInsert(BatchSetter setter) {
			setter.injectArgs(getEventDescriptor(),getTimeStamp(),0,0,0,0,0,0,"","",0,0,0,"","","");
		}
		
		@Override
		public TsEntityType getType() {
			return TsEntityType.TSXEVENTCOMPLAINCOMMITTED;
		}
		
		@Override
		public void setComplain (TsComplain complain) {
			this.complain = complain;
		}
		
		@Override
		public TsComplainDTI getComplain() {
			return complain;
		}
	},
	
	COMPLAINEXPIRED {
		transient private TsComplain complain;
		
		public void prepareForInsert(BatchSetter setter) {
			setter.injectArgs(getEventDescriptor(),getTimeStamp(),0,0,0,0,0,0,"","",0,0,0,"","","");
		}
		
		@Override
		public TsEntityType getType() {
			return TsEntityType.TSXEVENTCOMPLAINEXPIRED;
		}

		public void setComplain (TsComplain complain) {
			this.complain = complain;
		}
		
		@Override
		public TsComplainDTI getComplain() {
			return complain;
		} 
	},
	
	BRAINBEAT {
		public void prepareForInsert(BatchSetter setter) {
			setter.injectArgs(getEventDescriptor(),getTimeStamp(),0,0,0,0,0,0,"","",0,0,0,"","","");
		}
		@Override
		public TsEntityType getType() {
			return TsEntityType.TSXEVENTBRAINBEAT;
		}
		
	},
	
	TEXTMESSAGE {
		transient private TsClientDTI client;
		
		private int targetmode;
		private int target;
		private int invokerid;
		
		private String invokername;
		private String invokeruid;
		private String msg;
				
		public void prepareForInsert(BatchSetter setter) {
			setter.injectArgs(getEventDescriptor(),getTimeStamp(),0,0,0,0,0,0,"","",targetmode,target,invokerid,invokername,invokeruid,msg);
		}
		
		@Override
		public TsEntityType getType() {
			return TsEntityType.TSEVENTTEXTMESSAGE;
		}
		
		
		@Override
		public void update(TsFieldStack stack) {
			targetmode = (Integer) stack.popFirst(TARGETMODE);
//			target = (Integer) stack.popFirst(TARGET);				//!!!caused an exception
			target = (Integer) stack.ifSet(target, LibTsSym.TARGET);
			invokerid = (Integer) stack.popFirst(INVOKERID);
			invokername = (String) stack.popFirst(INVOKERNAME);
			invokeruid = (String) stack.popFirst(INVOKERUID);
			msg = (String) stack.popFirst(MSG);
			
			client = EntityManager.getSharedInstance().getClientList().selectClientClidEquals(invokerid);
			if (client == null) {
				AdvancedGlobalBulletin.getSharedInstance().EVENT.Error.push(
						"unable to bind client to event / can not find client with invoker-id", 
						new String[]{"TsEvent","TEXTMESSAGE"}, 
						new Integer[]{invokerid}
				);
			}
		}
		
		@Override
		public int getTarget() {
			return target;
		}

		@Override
		public int getInvokerid() {
			return invokerid;
		}

		@Override
		public int getTargetmode() {
			return targetmode;
		}

		@Override
		public String getMsg() {
			return msg;
		}

		@Override
		public String getInvokername() {
			return invokername;
		}

		@Override
		public String getInvokeruid() {
			return invokeruid;
		}
		
		@Override
		public TsClientDTI getClient() {
			return client;
		}

	};
	
//	@Override
//	public void update(TsFieldStack stack) {
//	
//	}

	private int propHash;
	private int tsxDbId;
	
	@Override
	public TsEntityType getType() {
		return null;
	}

	public void setComplain(TsComplain complain) {
	}

	@Override
	public int getTsPropHash() {
		return propHash;
	}

	@Override
	public void setTsPropHash(String str) {
		propHash = str.hashCode();
	}
	
	@Override
	public void update(TsFieldStack stack) {
	}
	
	@Override
	public String getEventDescriptor() {
		return getType().toString();
	}

	@Override
	public TsClientDTI getClient() {
		return null;
	}
	
	
	/**
	 * @deprecated
	 */
	@Override
	public TsClientDTI getCurrent_client() {
		return getClient();
	}
	
	@Override
	public TsComplainDTI getComplain() {
		return null;
	} 
	
	@Override
	public int getClid() {
		return 0;
	}
	
	@Override
	public int getCfid() {
		return 0;
	}

	@Override
	public int getCtid() {
		return 0;
	}

	@Override
	public int getReasonid() {
		return 0;
	}

	@Override
	public int getClient_outputonly_muted() {
		return 0;
	}

	@Override
	public int getClient_talk_request() {
		return 0;
	}

	@Override
	public String getClient_talk_request_msg() {
		return null;
	}

	@Override
	public int getClient_unread_messages() {
		return 0;
	}

	@Override
	public TsComplainDTI getCurrent_complain() {
		return getComplain();
	}

	@Override
	public int getTarget() {
		return 0;
	}

	@Override
	public int getInvokerid() {
		return 0;
	}

	@Override
	public int getTargetmode() {
		return 0;
	}

	@Override
	public String getMsg() {
		return null;
	}

	@Override
	public String getInvokername() {
		return null;
	}

	@Override
	public String getInvokeruid() {
		return null;
	}

	@Override
	public String getReasonmsg() {
		return null;
	}

	public void prepareForInsert(BatchSetter setter) {
		throw new UnsupportedOperationException("NOTHING TO DO");
	}
	
	public void prepareForUpdate(BatchSetter setter) {
		throw new UnsupportedOperationException("NOTHING TO DO");
	}
	
	int tsxGlueId;
	private long timeStamp = 0L;
	
	@Override
	public void setTSXDBID(int id) {
		this.tsxDbId = id;
	}
	@Override
	public int getTSXDBID() {
		return this.tsxDbId;
	}
	
	@Override
	public int getGlueID() {
		return this.tsxGlueId;
	}
	
	@Override
	public void setGlueID(int id) {
		this.tsxGlueId = id;
	}

	public void setTimeStamp(Long rtMillis) {
		timeStamp = rtMillis;
	}

	public long getTimeStamp() {
		return timeStamp;
	}
}
