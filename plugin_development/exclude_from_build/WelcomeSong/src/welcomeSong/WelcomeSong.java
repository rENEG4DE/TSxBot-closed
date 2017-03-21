package welcomeSong;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import api.data.*;
import api.plugin.Plugin;
import api.plugin.EventClient;
import api.service.BotServices;


public class WelcomeSong extends Plugin implements EventClient{
	int member_group_id=35;
	int friend_group_id=16;
	int lolTimer=0;
	int lolTimerCid=0;
	
	public WelcomeSong(BotServices service) {
		super ("WelcomeSong", service);
//		member_clid = (Integer)getModCVarVal("member_clid");
//		friend_clid = (Integer)getModCVarVal("friend_clid");
	}
	
	@Override
	public EventClient[] getEventClients() {
		return new EventClient[] {this};
	}
	
	@Override
	protected void initConfig() {
		//Nothing to init here yet
//		service.createIntegerVar(createCVarID("member_group_id"), 35);
//		service.createIntegerVar(createCVarID("friend_group_id"), 16);
//		System.out.println("[member group id loaded] " + member_group_id);
//		System.out.println("[friend group id loaded] " + friend_group_id);
	}
	
	public void sendMsg(TsClientDTI current,String msg) {
		service.addAction(service.createAction(1).clientMessagePrivate(current,msg));
	}
	
	public boolean checkServerGroup(TsClientDTI current,int group) {
		for (int groups : current.getServergroups()) {
			if (groups == group) {
				return true;
			}
		}
		return false;
	}
	
	
	@Override
	public void onTextMessage(EventDTI data) {
		
		System.out.println(data.getEventDescriptor() + " from "+ data.getCurrent_client().getClient_Nickname()+" [msg] "
				 + data.getMsg() );
		TsClientDTI current = data.getCurrent_client();
		String msg = service.decodeTS3String(data.getMsg());
		String[] commands =  msg.split(" ");
		// System.out.println("Old = "+data.getMsg());
		
		 //System.out.println("New = "+msg);
		
	
		  if(commands[0].equals("!play")){
			if(checkServerGroup(current,35)==true){
				
				    File file = new File("D:\\Inst\\New folder\\teamspeak3-server_win64\\files\\virtualserver_1\\channel_8\\Member\\"+current.getClient_Database_id()+"\\Sounds\\"+commands[1]+".mp3");
			        if (!file.exists()) {
			            System.out.println(commands[1] +" .mp3 file does not exist!!!");
			            //service.addAction(service.createAction(1).botSay(commands[1] +" .mp3 file does not exist!!!"));
			            service.addAction(service.createAction(1).clientMessagePrivate(current, commands[1] +" .mp3 file does not exist!!!"));
			
	
			        }else{
			        	service.addAction(service.createAction(1).clientMove(service.getBotClient(), current.getCid()));
			        	service.addAction(service.createAction(500).botPlayMP3("D:\\Inst\\New folder\\teamspeak3-server_win64\\files\\virtualserver_1\\channel_8\\Member\\"+current.getClient_Database_id()+"\\Sounds\\"+commands[1]+".mp3"));	
			        	
			        }
			}else{
				sendMsg(current,"sorry "+ service.decodeTS3String(current.getClient_Nickname())+" you cant use this command.");
			}
		  }
		  
		 
		  

		  
		  

			
		
		  
		  
	}
 
	@Override
	public void onBrainBeat(EventDTI data) {
	

       
	}
	
	@Override
	public void onClientJoined (EventDTI data) {
		try {
			TsClientDTI current = data.getCurrent_client();
			int ctid = data.getCtid();
			String RankName ="";
			
			String name =current.getClient_Nickname();
			name = name.replaceAll("-=IF=-","");
			name = service.decodeTS3String(name);
			name = name.replaceAll("_"," ");

		
			if (current.getClient_type() == 0) {// if its no query client but a real
				
				service.addAction(service.createAction(1).clientMessagePrivate(current,"Hi im TSX Bot. All chat commands go here and not to the speaking client."));
				service.addAction(service.createAction(1).clientMessagePrivate(current,"type !help to get more info"));
				//service.addAction(service.createAction(1).clientMessagePrivate(current,"Your Dbid:"+current.getClient_Database_id()));
				
				
				
				System.out.println("Join event Plugin Welcome song Triggered");								// user play song

				int current_servergroup = -1;
				for (int group : current.getServergroups()) {

					if (group == member_group_id) {
						current_servergroup = group;
					}
					if (group == friend_group_id) {
						current_servergroup = group;
					}
					
					//************* ranks ****************
					if (group == 14) {
						RankName="Private"; 
					}
					if (group == 15) {
						RankName="Private First Class"; 
					}
					if (group == 16) {
						RankName="Specialist"; 
					}
					if (group == 17) {
						RankName="Corporal"; 
					}
					if (group == 18) {
						RankName="Sergeant"; 
					}
					if (group == 18) {
						RankName="Sergeant"; 
					}
					if (group == 19) {
						RankName="Staff Sergeant"; 
					}
					if (group == 20) {
						RankName="Sergeant First Class"; 
					}
					if (group == 21) {
						RankName="Master Sergeant"; 
					}
					if (group == 22) {
						RankName="Sergeant Major"; 
					} 
					if (group == 23) {
						RankName="Second Lieutenant"; 
					} 
					if (group == 24) {
						RankName="First Lieutenant"; 
					} 
					if (group == 25) {
						RankName="Captain"; 
					} 
					if (group == 26) {
						RankName="Major"; 
					} 
					if (group == 27) {
						RankName="Lieutenant Colonel"; 
					} 
					if (group == 28) {
						RankName="Colonel"; 
					}   
					if (group == 29) {
						RankName="Brigadier General"; 
					}   
					if (group == 30) {
						RankName="Major General"; 
					}
					if (group == 31) {
						RankName="Lieutenant General"; 
					}
					if (group == 32) {
						RankName="General"; 
					}
					if (group == 33) {
						RankName="Leader"; 
					}
					if (group == 34) {
						RankName="CO Leader"; 
					}


					
				}

				if(current_servergroup==-1) {
					//play song and welcome ----------------------------------------stranger(guest)      
					System.out.println("[Playing song for guest] "+current.getClient_Nickname());
					service.addAction(service.createAction(1).clientMove(service.getBotClient(), ctid));		
					service.addAction(service.createAction(4000).botPlayMP3("sounds/welcome_to_if.mp3"));
					service.addAction(service.createAction(4000).botSay(  name + " enjoy your stay "));
					
				} else if(current_servergroup==member_group_id) {
					//play song and welcome---------------------- member  
					System.out.println("[Playing song for member] "+current.getClient_Nickname());
					service.addAction(service.createAction(1).clientMove(service.getBotClient(), ctid));
					
					service.addAction(service.createAction(1).botSay("Welcome "));
					service.addAction(service.createAction(2000).botPlayMP3("sounds/ranks/"+RankName+".mp3"));
					service.addAction(service.createAction(2000).botSay(name));
					File file = new File("D:\\Inst\\New folder\\teamspeak3-server_win64\\files\\virtualserver_1\\channel_8\\Member\\"+current.getClient_Database_id()+"\\Sounds\\welcome.mp3");
			       
			        if (!file.exists()) {

			            System.out.println("welcome.mp3 file does not exist!!!");
			           // service.addAction(service.createAction(100).clientMove(service.getBotClient(), 571));

			        }else{
			        	service.addAction(service.createAction(1500).botPlayMP3("D:\\Inst\\New folder\\teamspeak3-server_win64\\files\\virtualserver_1\\channel_8\\Member\\"+current.getClient_Database_id()+"\\Sounds\\welcome.mp3"));	
			        	//service.addAction(service.createAction(100).clientMove(service.getBotClient(), 571));
			        	// System.out.println("Sample.txt file exist!!!");

			        }

				} else if(current_servergroup==friend_group_id) {
					//play song and welcome---------------------- friend
					System.out.println("[Playing song for friend ] "+current.getClient_Nickname());
					service.addAction(service.createAction(1).clientMove(service.getBotClient(), ctid));
					service.addAction(service.createAction(4000).botSay("Welcome my friend " + name));
					service.addAction(service.createAction(1000).botPlayMP3("sounds/welcome_to_if.mp3"));
				}

			}
		} catch (Exception e) {
			System.out.println("[" + getPluginName() + "][Warning]" +e);
			e.printStackTrace();
		}
	}
}
