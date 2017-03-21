package afkMover;

import java.util.concurrent.TimeUnit;

import api.data.*;
import api.plugin.Plugin;
import api.plugin.EventClient;
import api.service.BotServices;


public class AfkMover extends Plugin implements EventClient{

	//settings
	private CVarDTI	afk_groups_by_order;
	private CVarDTI	afk_time_to_groups_in_order;
	private CVarDTI	max_idle_time_input_muted;
	private CVarDTI	max_idle_time_away;
	private CVarDTI	max_idle_time_outputmuted;
	private CVarDTI	max_idle_time_hardware_input_muted;
	private CVarDTI	max_idle_time_hardware_output_muted;
	private CVarDTI	max_idle_time;
	private CVarDTI	afk_cid;
	
	int move_afk = 0;
	int move_back = 0;
	int Pcid=0;
	
	boolean first_run=true;
	
	int[ ][ ] timing_group_matrix = new int[10][2];
	
    int max_afk_group_id=0;
    int max_afk_timing=0;
    
    int afk_group_count=0;
    int afk_timing_count=0;
	
    int put_in_this_afk_group=0; 
    int last_afk_group_timing=0;
    
	int t_max_idle_time_input_muted=0;
	int t_max_idle_time_away=0;
	int t_max_idle_time_outputmuted=0;
	int t_max_idle_time_hardware_input_muted=0;
	int t_max_idle_time_hardware_output_muted=0;
	int t_max_idle_time=20;
	int t_afk_cid=23;
    

	//default init shit
	int showed_vars=0;

	
	public AfkMover(BotServices service) {
		super ("AfkMover", service);
	}
	
	@Override
	public EventClient[] getEventClients() {
		return new EventClient[] {this};
	}
	
	@Override
	protected void initConfig() {
		System.out.println("Loading Cvars");
		cVarUtil.registerCVar(afk_groups_by_order = cVarUtil.createArrayVar(createCVarID("afk_groups_by_order"), LibFieldType.CVAR_ARRAY_OF_INTEGER));
		cVarUtil.registerCVar(afk_time_to_groups_in_order = cVarUtil.createArrayVar(createCVarID("afk_time_to_groups_in_order"), LibFieldType.CVAR_ARRAY_OF_INTEGER));		
		cVarUtil.registerCVar(max_idle_time_input_muted = cVarUtil.createIntegerVar(createCVarID("max_idle_time_input_muted"), 0));
		cVarUtil.registerCVar(max_idle_time_away = cVarUtil.createIntegerVar(createCVarID("max_idle_time_away"), 0));
		cVarUtil.registerCVar(max_idle_time_outputmuted = cVarUtil.createIntegerVar(createCVarID("max_idle_time_outputmuted"), 0));
		cVarUtil.registerCVar(max_idle_time_hardware_input_muted = cVarUtil.createIntegerVar(createCVarID("max_idle_time_hardware_input_muted"),0));
		cVarUtil.registerCVar(max_idle_time_hardware_output_muted = cVarUtil.createIntegerVar(createCVarID("max_idle_time_hardware_output_muted"), 0));
		cVarUtil.registerCVar(max_idle_time = cVarUtil.createIntegerVar(createCVarID("max_idle_time"),0));
		cVarUtil.registerCVar(afk_cid = cVarUtil.createIntegerVar(createCVarID("afk_cid"), 0));
		
		
		
        //workaround cause the database is erased on restart ^^ !!!! 
		 for (TsClientDTI client : clientUtil.getClientIterable()) {
			 optUtil.saveOptVar((TsEntityObject) client, "LAST_CID", client.getCid()); // so we dont chrash	
		 }				
	
	}
	
	
	public long minutestomillis(long mili) {
		return TimeUnit.MINUTES.toMillis(mili);
	}
	
	
	@Override
	public void onBrainBeat(EventDTI data) {
    	    
//		System.out.println(data.getEventDescriptor() + "BrainBeat from Runtime-Plugin-module, exampleCVar{" + super.getModCVarVal("exampleCVar") + "}");
		//System.out.println("[" + getPluginName() + "][" + data.getEventDescriptor() + "][forAllClients][performActionn]");
		
	
		String Nickname ="";
		String reason_to_move ="";
		
		
		for (TsClientDTI client : clientUtil.getClientIterable()) {
			for (TsChannelDTI channel : channelUtil.getChannelIterable()) {
				if(client.getCid()==channel.getCid()){
					Pcid=channel.getPid();
				}
			}
		
			Nickname = service.decodeTS3String(client.getClient_Nickname());
            move_afk = 0;
            move_back = 0;
            reason_to_move ="Default";
			//System.out.println("Name="+Nickname+" Type="+client.getClient_type());
			//service.addAction(service.createAction(0).clientMessagePrivate(client, "sry Test Message "+Nickname)); 
            
            if(client.getClient_type()==0){
            
            	//--- idle ----------------------------------------------------------------
				if (client.getIdle_time() > minutestomillis(t_max_idle_time)) {// client idle time is bigger then the max idle time
					reason_to_move = "[" + getPluginName() + "][" + data.getEventDescriptor() + "]Move Client " + Nickname + " to channel " + afk_cid
							+ " cause idle for " + client.getIdle_time();
					move_afk = 1;
				}

				if (client.getIdle_time() > minutestomillis(t_max_idle_time)) {
					if (move_afk != 1) {
						reason_to_move = "[" + getPluginName() + "][" + data.getEventDescriptor() + "]Move Client " + Nickname + " back to channel "
								+ optUtil.getOptVar((TsEntityObject) client, "LAST_CID") + " cause no longer idle";
						move_back = 1;
					}
				}
          
        	  //--- input muted ----------------------------------------------------------------
				if (client.getClient_input_muted() == 1) {// client input is muted
					if (client.getIdle_time() > minutestomillis(t_max_idle_time_input_muted)) {// client idle time is bigger then the max idle time
						reason_to_move = "[" + getPluginName() + "][" + data.getEventDescriptor() + "]Move Client " + Nickname + " to channel " + afk_cid
								+ " cause input is muted and idle for " + client.getIdle_time();
						move_afk = 1;
					}

				} else {
					if (move_afk != 1) {
						reason_to_move = "[" + getPluginName() + "][" + data.getEventDescriptor() + "]Move Client " + Nickname + " back to channel "
								+ optUtil.getOptVar((TsEntityObject) client, "LAST_CID") + " cause input is no loger muted";
						move_back = 1;
					}
				}
            
				//--- away -----------------------------------------------------------------------
				if (client.getClient_Away() == 1) {
					if (client.getIdle_time() > minutestomillis(t_max_idle_time_away)) {// client idle time is bigger then the max idle time
						reason_to_move = "[" + getPluginName() + "][" + data.getEventDescriptor() + "]Move Client " + Nickname + " to channel " + afk_cid
								+ " cause away status is 1 and idle time is " + client.getIdle_time();
						move_afk = 1;
					}
				} else {
					if (move_afk != 1) {
						reason_to_move = "[" + getPluginName() + "][" + data.getEventDescriptor() + "]Move Client " + Nickname + " back to channel "
								+ optUtil.getOptVar((TsEntityObject) client, "LAST_CID") + " cause away status is 0";
						move_back = 1;
					}
				}
            
				//--- output muted -----------------------------------------------------------------------
				if (client.getClient_output_muted() == 1) {
					if (client.getIdle_time() > minutestomillis(t_max_idle_time_outputmuted)) {// client idle time is bigger then the max idle time
						reason_to_move = "[" + getPluginName() + "][" + data.getEventDescriptor() + "]Move Client " + Nickname + " to channel " + afk_cid
								+ " cause output_muted status is 1 and idle time is " + client.getIdle_time();
						move_afk = 1;
					}
				} else {
					if (move_afk != 1) {
						reason_to_move = "[" + getPluginName() + "][" + data.getEventDescriptor() + "]Move Client " + Nickname + " back to channel "
								+ optUtil.getOptVar((TsEntityObject) client, "LAST_CID") + " cause output_muted status is 0";
						move_back = 1;
					}
				}
           
			    //--- Hardware input -----------------------------------------------------------------------
				if (client.getClient_input_hardware() == 0) {
					if (client.getIdle_time() > minutestomillis(t_max_idle_time_hardware_input_muted)) {// client idle time is bigger then the max idle time
						reason_to_move = "[" + getPluginName() + "][" + data.getEventDescriptor() + "]Move Client " + Nickname + " to channel " + afk_cid
								+ " cause max_idle_time_hardware_input_muted status is 0 and idle time is " + client.getIdle_time();
						move_afk = 1;
					}

				} else {
					if (move_afk != 1) {
						reason_to_move = "[" + getPluginName() + "][" + data.getEventDescriptor() + "]Move Client " + Nickname + " back to channel "
								+ optUtil.getOptVar((TsEntityObject) client, "LAST_CID") + " cause max_idle_time_hardware_input_muted status is 1";
						move_back = 1;
					}

				}
            
				// --- Hardware output -----------------------------------------------------------------------
				if (client.getClient_output_hardware() == 0) {
					if (client.getIdle_time() > minutestomillis(t_max_idle_time_hardware_output_muted)) {// client idle time is bigger then the max idle time
						reason_to_move = "[" + getPluginName() + "][" + data.getEventDescriptor() + "]Move Client " + Nickname + " to channel " + afk_cid
								+ " cause max_idle_time_hardware_output_muted status is 0 and idle time is " + client.getIdle_time();
						move_afk = 1;
					}

				} else {
					if (move_afk != 1) {
						reason_to_move = "[" + getPluginName() + "][" + data.getEventDescriptor() + "]Move Client " + Nickname + " back to channel "
								+ optUtil.getOptVar((TsEntityObject) client, "LAST_CID") + " cause max_idle_time_hardware_output_muted status is 1";
						move_back = 1;
					}

				}
            
            
            //*******************************************************************
            //*********************** Re Worked Server Group Stuff **************
            //*******************************************************************
            
            //** vars ***
      
            
            put_in_this_afk_group=0; 
            last_afk_group_timing=0;
            
         
            
            if(first_run==true){//stuff we do only once :D	  
            	t_max_idle_time_input_muted=(int)max_idle_time_input_muted.getValue();
        		t_max_idle_time_away=(int)max_idle_time_away.getValue();
        		t_max_idle_time_outputmuted=(int)max_idle_time_outputmuted.getValue();
        		t_max_idle_time_hardware_input_muted=(int)max_idle_time_hardware_input_muted.getValue();
        		t_max_idle_time_hardware_output_muted=(int)max_idle_time_hardware_output_muted.getValue();
        		t_max_idle_time=(int)max_idle_time.getValue();
        		t_afk_cid=(int)afk_cid.getValue();
        		
	            for(int i = 0; i < 10; i++){
	       	       for(int j = 0; j < 2; j++)
	       	       {
	       	    	timing_group_matrix[i][j]=-1;
	       	       }
	       	    }
            	
            	for (int current_afk_group : (Integer[])afk_groups_by_order.getValue()) {
                	max_afk_group_id=current_afk_group; 
                	timing_group_matrix[afk_group_count][0]=current_afk_group;
                	afk_group_count+=1;
                	
                }
                
                for (int current_afk_timing : (Integer[])afk_time_to_groups_in_order.getValue()) {
                	max_afk_timing=current_afk_timing; 
                	timing_group_matrix[afk_timing_count][1]=current_afk_timing;
                	afk_timing_count+=1;
                }
                
                if(afk_timing_count!=afk_group_count){
                	System.out.println("[" + getPluginName() + "][" + data.getEventDescriptor() + "][Config Error >> Afk Groups and Afk Timings need to be the same amount]");
                }
                
                 System.out.println("Server_group_id:max_idle_time");
            	 for(int i = 0; i < 9; i++){
					if(timing_group_matrix[i][0]!= -1){	
					   System.out.println("             "+timing_group_matrix[i][0] + ":" + timing_group_matrix[i][1]);
					}
            	 }
                first_run=false;
            }
            
            
            
            //-------------------------
            //check if this client is in the afk channel or not
            boolean in_afk_channel=false;
            if(client.getCid()==t_afk_cid){
            	in_afk_channel=true;
            }else{
            	in_afk_channel=false;
            }
       
      	  if(move_afk==1 || move_back==1 ){
	       	 for(int i = 0; i < 9; i++){
					if(timing_group_matrix[i][0]!= -1){	
					  if(client.getIdle_time()>minutestomillis(timing_group_matrix[i][1])){
	           			   if((move_afk==1)||(in_afk_channel==true)){
	           				   put_in_this_afk_group=timing_group_matrix[i][0];
	           			   }
	           		   }
					}
	     	 }
            
            
            
            //---- add the right group remove the rest :D
            
            for (int current_afk_group : (Integer[])afk_groups_by_order.getValue()) {
         	    	//-----------------------------------------------------------------------------------------------------------------------------------------------------------
		     	   	if(put_in_this_afk_group!=current_afk_group){//remove server group
	     	   		 	for (int client_servergroup : client.getServergroups()) {//loop all client server groups
	     	   		 		if(current_afk_group==client_servergroup){
	     	   		 		    service.addAction(actionUtil.createQueryAction(0, 0, queryUtil.removeClientFromGroup(client.getClient_Database_id(), current_afk_group)));
	     	   		 		}		
	     	   		 	}
		 	    	}
         	    	
         	    	//-----------------------------------------------------------------------------------------------------------------------------------------------------------
		     	   	if(put_in_this_afk_group==current_afk_group){//add server group
		     	   		boolean client_is_in_that_group=false;
	     	   		 	for (int client_servergroup : client.getServergroups()) {//loop all client server groups
	     	   		 		if(current_afk_group==client_servergroup){
	     	   		 		  client_is_in_that_group=true;
	     	   		 		}		
	     	   		 	}
	     	   		 	
		     	   		if(client_is_in_that_group==false){
		     	   		    service.addAction(actionUtil.createQueryAction(0, 0, queryUtil.addClientToGroup(client.getClient_Database_id(), current_afk_group)));
	  	   		 		}	
	     	   		 	
		 	    	}

            }
      	   }
            
            
            
            

         
            
            
           
            //******* do the magic *******   
           if(client!=service.getBotClient()){
	           if(client.getClient_type()==0){
		            if(move_afk==1){
		        	  if(client.getCid()!=t_afk_cid){//client is not in afk channel
		            	  System.out.println("reason to move: " + reason_to_move);
		            	  if(client.getCid()!=t_afk_cid){
		            	   // service.sqlWrite(client, "LAST_CID", String.valueOf(client.getCid()));
		            		optUtil.saveOptVar((TsEntityObject) client, "LAST_CID", client.getCid());
		            		optUtil.saveOptVar((TsEntityObject) client, "LAST_PCID", Pcid);
		            	    
		            	  }
		            	  
		            	  System.out.println("adding Action!");
		            
		            	  service.addAction(actionUtil.createQueryAction(0, 0, queryUtil.moveClientTo((Integer) t_afk_cid, client.getClid())));

		               } 
		            }
		            if((move_back==1)&&(move_afk!=1)){
		            	 if(client.getCid()==t_afk_cid){//client is in afk channel
	            			int channelexists=0;
		            		for (TsChannelDTI channel2 : channelUtil.getChannelIterable()) {
	            				if(((int)optUtil.getOptVar((TsEntityObject)client, "LAST_CID"))==channel2.getCid()){
	            					
	            					service.addAction(actionUtil.createQueryAction(0, 0, queryUtil.moveClientTo((Integer) optUtil.getOptVar((TsEntityObject)client, "LAST_CID"), client.getClid())));
	            					channelexists=1;
	            				}
	            			}
		            	
		            		if(channelexists==0){//defaul channel dosent exits so we move to last parent channel
		            			System.out.println("[" + getPluginName() + "][" + data.getEventDescriptor() + "]Move Client "+Nickname +" To Parent Channel cause the last dosent exist anymore");
		            			//service.addQueryAction(0,0,queryUtil.moveClientTo((Integer) optUtil.getOptVar((TsEntityObject)client, "LAST_PCID"), client.getClid()));
		            			service.addAction(actionUtil.createQueryAction(0, 0, queryUtil.moveClientTo((Integer) optUtil.getOptVar((TsEntityObject)client, "LAST_PCID"), client.getClid())));
		            		}
		            	    System.out.println(reason_to_move);
		                 
		            	    service.addAction(actionUtil.createQueryAction(0, 0, queryUtil.moveClientTo((Integer) optUtil.getOptVar((TsEntityObject)client, "LAST_CID"), client.getClid())));
		                	
		            	 }
					}
				}
			}

		}
	  }
	
	}
}
