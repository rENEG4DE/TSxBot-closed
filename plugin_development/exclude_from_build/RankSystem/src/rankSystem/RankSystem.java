package rankSystem;

import api.data.EventDTI;
import api.data.TsClientDTI;
import api.data.TsEntityObject;
import api.plugin.EventClient;
import api.plugin.Plugin;
import api.service.BotServices;

public class RankSystem extends Plugin implements EventClient{

	
	class Rank {
		  int id;
		  String name;
		  int pointsNeeded;
		  int servergroup;
		  
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getPointsNeeded() {
			return pointsNeeded;
		}
		public void setPointsNeeded(int pointsNeeded) {
			this.pointsNeeded = pointsNeeded;
		}
		public int getServergroup() {
			return servergroup;
		}
		public void setServergroup(int servergroup) {
			this.servergroup = servergroup;
		}
	
	}
	
	public RankSystem(BotServices service) {
		super ("RankSystem", service);
	}
	
	@Override
	protected void initConfig() {
		System.out.println("[" + getPluginName() + "]Loading Ranks to array..");
		Rank[] ranks = new Rank[18];//cause we have 17 ranks
		for (int i=0; i<ranks.length-1; i++ ) {
			ranks[i] = new Rank();
		}
		
		//this hardcoded arry needs to be load in future by a xml file !!!
		ranks[0].setId(0);
		ranks[0].setName("Private");
		ranks[0].setPointsNeeded(0);
		ranks[0].setServergroup(14);
		
		ranks[1].setId(1);
		ranks[1].setName("Private First Class");
		ranks[1].setPointsNeeded(86400);
		ranks[1].setServergroup(15);
		
		ranks[2].setId(2);
		ranks[2].setName("Private"); 
		ranks[2].setPointsNeeded(0);
		ranks[2].setServergroup(14);
        
		ranks[3].setId(3);
		ranks[3].setName("Private First Class"); 
		ranks[3].setPointsNeeded(86400);//1 day online
		ranks[3].setServergroup(15);
        
		ranks[4].setId(4);
		ranks[4].setName("Specialist"); 
		ranks[4].setPointsNeeded(172800);//2 days online
		ranks[4].setServergroup(16);
		
		ranks[5].setId(5);
		ranks[5].setName("Corporal"); 
		ranks[5].setPointsNeeded(259200);//3 days online
		ranks[5].setServergroup(17);
	
		ranks[6].setId(6);
		ranks[6].setName("Sergeant"); 
		ranks[6].setPointsNeeded(432000);//5 days online
		ranks[6].setServergroup(18);
		
		ranks[7].setId(7);
		ranks[7].setName("Staff Sergeant");
		ranks[7].setPointsNeeded(691200);//8 days online
		ranks[7].setServergroup(19);
		
		ranks[8].setId(8);
		ranks[8].setName("Sergeant First Class");
		ranks[8].setPointsNeeded(1123200);//13 days online
		ranks[8].setServergroup(20);
		
		ranks[9].setId(9);
		ranks[9].setName("Master Sergeant");
		ranks[9].setPointsNeeded(1468800);//17 days online
		ranks[9].setServergroup(21);
		
		ranks[10].setId(10);
		ranks[10].setName("Sergeant Major");
		ranks[10].setPointsNeeded(2592000);//30 days online
		ranks[10].setServergroup(22);
		
		ranks[11].setId(11);
		ranks[11].setName("Second Lieutenant"); 
		ranks[11].setPointsNeeded(3456000);//40 days online
		ranks[11].setServergroup(23);
		
		ranks[12].setId(12);
		ranks[12].setName("First Lieutenant"); 
		ranks[12].setPointsNeeded(4320000);//50 days online
		ranks[12].setServergroup(24);
		
		ranks[13].setId(13);
		ranks[13].setName("Captain"); 
		ranks[13].setPointsNeeded(5184000);//60 days online
		ranks[13].setServergroup(25);
		
		ranks[14].setId(14);
		ranks[14].setName("Major"); 
		ranks[14].setPointsNeeded(6912000);//80 days online
		ranks[14].setServergroup(26);
		
		ranks[15].setId(15);
		ranks[15].setName("Lieutenant Colonel"); 
		ranks[15].setPointsNeeded(8640000);//100 days online
		ranks[15].setServergroup(27);
		
		ranks[16].setId(16);
		ranks[16].setName("Colonel"); 
		ranks[16].setPointsNeeded(10368000);//120 days online
		ranks[16].setServergroup(28);
	
		for (int i = 0; i < ranks.length-1; i++) {
			 System.out.println(ranks[i].getName());
			 System.out.println(ranks[i].getPointsNeeded());
			 System.out.println(ranks[i].getServergroup());
			 System.out.println("***************");	 
		}
	
		System.out.println("[" + getPluginName() + "]Loading Ranks to array done...");

	}
	
	@Override
	public EventClient[] getEventClients() {
		return new EventClient[] {this};
	}

	@Override
	public void onClientJoined (EventDTI data) {

		for (TsClientDTI client : this.service.getClientIterable()) {
				
				this.service.saveOptVar((TsEntityObject) client, "ACTIVE_TIME", 0);
		
				
				for (int group : client.getServergroups()) {

					if (group == 36) {
						this.service.addAction(this.service.createAction(1).clientAdd2Group(client, 35));
						this.service.addAction(this.service.createAction(1).clientDelFromFroup(client, 36));
					}
		
				}
				
		 }

		
	}
	
	public void rank(TsClientDTI client) {
		int rankPoints=0;
		int member_group_id=35;
		int member_group_id2=36;
		System.out.println(client.getClient_Nickname()+" is no beeing checked ");

		int current_servergroup = -1;
		for (int group : client.getServergroups()) {//check if we have a clan member by group 35 or 36 wich is the imba group

			if (group == member_group_id) {
				current_servergroup = group;
			}
			if(current_servergroup==member_group_id2){
				current_servergroup = group;
			}

		}
		if((current_servergroup==member_group_id)|(current_servergroup==member_group_id2)) {//client is member
			rankPoints=this.service.getOptVar((TsEntityObject)client, "RANK_POINTS");
			if(client.getIdle_time()<240000){
				rankPoints+=1;	
			}
			System.out.println(client.getClient_Nickname()+" Is member Points="+rankPoints);
			this.service.saveOptVar((TsEntityObject)client, "RANK_POINTS", rankPoints);
		}
		
		
	}
	
	
	@Override
	public void onBrainBeat(EventDTI data) {
		System.out.println("Brain Beat Ranked!!");
		try {
			for (TsClientDTI client : this.service.getClientIterable()) {
				//rank(client);
//				int Time = 0;
//				
//	@SuppressWarnings("unused")
//				int current_servergroup = -1;
//				
//				Time = this.service.getDatabaseServices().sqlReadInt(client, "ACTIVE_TIME");
//			
//				if(client.getIdle_time()<240000){
//				
//					Time = Time + 1;
//				
//					// log.fine("[Rank] time changed to " + Time);
//					this.service.getDatabaseServices().sqlWrite(client, "ACTIVE_TIME", Time);
//				}else{
//					Time = 0;
//					
//					for (String group : client.getServergroups()) {
//
//						if (Integer.parseInt(group) == 36) {
//							this.service.addAction(this.service.createAction(1).clientAdd2Group(client, 35));
//							this.service.addAction(this.service.createAction(1).clientDelFromFroup(client, 36));
//						}
//					}
//					this.service.getDatabaseServices().sqlWrite(client, "ACTIVE_TIME", Time);
//				}
//				if(Time>7200){
//					
//					for (String group : client.getServergroups()) {
//
//						if (Integer.parseInt(group) == 35) {
//							  this.service.addAction(this.service.createAction(1).clientAdd2Group(client, 36));
//						    	this.service.addAction(this.service.createAction(1).clientDelFromFroup(client, 35));
//                              this.service.addAction(this.service.createAction(1).clientMove(this.service.getBotClient(), client.getCid()));
//							
//							  this.service.addAction(this.service.createAction(1).botSay(this.service.decodeTS3String(client.getClient_Nickname()+" is now on fire")));
//							
//						}
//					}
//				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("[" + getPluginName() + "][Warning]" +e);
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void onClientLeft(final EventDTI data) {
		

		
		
    	}
//		System.out.println("[" + getPluginName() + "]{" + data.getEventDescriptor() + "}[forAllClients][performAction]");
//		for (TsClientDTI client : service.getClientIterable()) {
//			String Time = "0";
//			int Time2 = 0;
//			Time = service.sqlRead(client, "ACTIVE_TIME");
//			if (Time.equals("")) {
//				Time = "0";
//			}
//			Time2 = Integer.parseInt(Time);
//			Time2 = Time2 + 1;
//			Time = String.valueOf(Time2);
//			// log.fine("[Rank] time changed to " + Time);
//			service.sqlWrite(client, "ACTIVE_TIME", Time);
//		}

	@Override
	public void onComplainExpired(EventDTI data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onComplainNew(EventDTI data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextMessage(EventDTI data) {
		// TODO Auto-generated method stub
		
	}
	}

