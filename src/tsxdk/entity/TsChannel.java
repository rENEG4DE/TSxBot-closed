//** this part is auto generated dont touch this

// Revision: $Revision: 370 $
// Last modified: $Date: 2012-11-22 20:26:39 +0100 (Do, 22 Nov 2012) $
// Last modified by: $Author: KoRnHolio $

package tsxdk.entity;

import static tsxdk.parser.LibTsSym.CHANNEL_CODEC;
import static tsxdk.parser.LibTsSym.CHANNEL_CODEC_QUALITY;
import static tsxdk.parser.LibTsSym.CHANNEL_FLAG_DEFAULT;
import static tsxdk.parser.LibTsSym.CHANNEL_FLAG_PASSWORD;
import static tsxdk.parser.LibTsSym.CHANNEL_FLAG_PERMANENT;
import static tsxdk.parser.LibTsSym.CHANNEL_FLAG_SEMI_PERMANENT;
import static tsxdk.parser.LibTsSym.CHANNEL_MAXCLIENTS;
import static tsxdk.parser.LibTsSym.CHANNEL_MAXFAMILYCLIENTS;
import static tsxdk.parser.LibTsSym.CHANNEL_NEEDED_SUBSCRIBE_POWER;
import static tsxdk.parser.LibTsSym.CHANNEL_NEEDED_TALK_POWER;
import static tsxdk.parser.LibTsSym.CHANNEL_ORDER;
import static tsxdk.parser.LibTsSym.CHANNEL_TOPIC;
import static tsxdk.parser.LibTsSym.CID;
import static tsxdk.parser.LibTsSym.PID;
import static tsxdk.parser.LibTsSym.TOTAL_CLIENTS;
import static tsxdk.parser.LibTsSym.TOTAL_CLIENTS_FAMILY;
import tsxdk.entity.meta.LibEntityState;
import tsxdk.entity.meta.StatefulEntity;
import tsxdk.parser.LibTsSym;
import tsxdk.parser.TsFieldStack;
import utility.misc.BatchSetter;
import utility.reclaimable.Reclaimable;
import api.data.TsChannelDTI;
import api.data.TsEntityType;

//import api.data.TsChannelDTI;

public class TsChannel implements TsChannelDTI, Reclaimable, TsEntity, StatefulEntity /*, TsChannelDTI */ {
	private int cid = -1;
	private int pid = 0;
	private int channel_order = 0;
	private String channel_name = "";
	private String channel_topic = "";
	private int channel_flag_default = 0;
	private int channel_flag_password = 0;
	private int channel_flag_permanent = 0;
	private int channel_flag_semi_permanent = 0;
	private int channel_codec = 0;
	private int channel_codec_quality = 0;
	private int channel_needed_talk_power = 0;
	private int total_clients_family = 0;
	private int channel_maxclients = 0;
	private int channel_maxfamilyclients = 0;
	private int total_clients = 0;
	private int channel_needed_subscribe_power = 0;
	
	// ++++++++++++++++++ nothing to do with the server ++++++++
	
	private LibEntityState state;
	private int propHash = 0;
	//Reclaimable stuff
	boolean reclaimable;
	private int tsxDbId;
	private int tsxGlueId;

	public TsChannel() {
		// log.fine("create new channel object");
		// log.fine("cid "+this.cid);
		 state = LibEntityState.INITIAL; 
	}

	@Override
	public void clearForReuse () {
		//do nothing, we rely on update () 
	}
	
	//@Override
	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	//@Override
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	//@Override
	public int getChannel_order() {
		return channel_order;
	}

	public void setChannel_order(int channel_order) {
		this.channel_order = channel_order;
	}

	//@Override
	public String getChannel_name() {
		return channel_name;
	}

	public void setChannel_name(String channel_name) {
		this.channel_name = channel_name;
	}

	//@Override
	public String getChannel_topic() {
		return channel_topic;
	}

	public void setChannel_topic(String channel_topic) {
		this.channel_topic = channel_topic;
	}

	//@Override
	public int getChannel_flag_default() {
		return channel_flag_default;
	}

	public void setChannel_flag_default(int channel_flag_default) {
		this.channel_flag_default = channel_flag_default;
	}

	//@Override
	public int getChannel_flag_password() {
		return channel_flag_password;
	}

	public void setChannel_flag_password(int channel_flag_password) {
		this.channel_flag_password = channel_flag_password;
	}

	//@Override
	public int getChannel_flag_permanent() {
		return channel_flag_permanent;
	}

	public void setChannel_flag_permanent(int channel_flag_permanent) {
		this.channel_flag_permanent = channel_flag_permanent;
	}

	//@Override
	public int getChannel_flag_semi_permanent() {
		return channel_flag_semi_permanent;
	}

	public void setChannel_flag_semi_permanent(int channel_flag_semi_permanent) {
		this.channel_flag_semi_permanent = channel_flag_semi_permanent;
	}

	//@Override
	public int getChannel_codec() {
		return channel_codec;
	}

	public void setChannel_codec(int channel_codec) {
		this.channel_codec = channel_codec;
	}

	//@Override
	public int getChannel_codec_quality() {
		return channel_codec_quality;
	}

	public void setChannel_codec_quality(int channel_codec_quality) {
		this.channel_codec_quality = channel_codec_quality;
	}

	//@Override
	public int getChannel_needed_talk_power() {
		return channel_needed_talk_power;
	}

	public void setChannel_needed_talk_power(int channel_needed_talk_power) {
		this.channel_needed_talk_power = channel_needed_talk_power;
	}

	//@Override
	public int getTotal_clients_family() {
		return total_clients_family;
	}

	public void setTotal_clients_family(int total_clients_family) {
		this.total_clients_family = total_clients_family;
	}

	//@Override
	public int getChannel_maxclients() {
		return channel_maxclients;
	}

	public void setChannel_maxclients(int channel_maxclients) {
		this.channel_maxclients = channel_maxclients;
	}

	//@Override
	public int getChannel_maxfamilyclients() {
		return channel_maxfamilyclients;
	}

	public void setChannel_maxfamilyclients(int channel_maxfamilyclients) {
		this.channel_maxfamilyclients = channel_maxfamilyclients;
	}

	//@Override
	public int getTotal_clients() {
		return total_clients;
	}

	public void setTotal_clients(int total_clients) {
		this.total_clients = total_clients;
	}

	//@Override
	public int getChannel_needed_subscribe_power() {
		return channel_needed_subscribe_power;
	}

	public void setChannel_needed_subscribe_power(
			int channel_needed_subscribe_power) {
		this.channel_needed_subscribe_power = channel_needed_subscribe_power;
	}

	@Override
	public void update(TsFieldStack stack) {
		cid = (Integer) stack.popFirst(CID);
		pid = (Integer) stack.popFirst(PID);
//		int order_old = channel_order;
		channel_order = (Integer) stack.popFirst(CHANNEL_ORDER);
		
//		if (channel_order != order_old)
//			System.out.println("******* Channel order has changed");
		
		total_clients = (Integer) stack.popFirst(TOTAL_CLIENTS);
		channel_needed_subscribe_power = (Integer) stack.popFirst(CHANNEL_NEEDED_SUBSCRIBE_POWER);
		
//		channel_name = (String) stack.popFirst(CHANNEL_NAME);				//this is not parsed because the channel is already determined on creation
		
//-topic parm
		if (stack.hasField(CHANNEL_TOPIC)) {
			channel_topic = (String) stack.popFirst(CHANNEL_TOPIC);	
		}
		
//-flags parm
		if (stack.hasFields(new LibTsSym[]{CHANNEL_FLAG_DEFAULT, CHANNEL_FLAG_PASSWORD, CHANNEL_FLAG_PERMANENT,CHANNEL_FLAG_SEMI_PERMANENT})) {
			channel_flag_default = (Integer) stack.popFirst(CHANNEL_FLAG_DEFAULT);
			channel_flag_password = (Integer) stack.popFirst(CHANNEL_FLAG_PASSWORD);
			channel_flag_permanent = (Integer) stack.popFirst(CHANNEL_FLAG_PERMANENT);
			channel_flag_semi_permanent = (Integer) stack.popFirst(CHANNEL_FLAG_SEMI_PERMANENT);
		}
//-voices parm
		if (stack.hasFields(new LibTsSym[]{CHANNEL_CODEC, CHANNEL_CODEC_QUALITY, CHANNEL_NEEDED_TALK_POWER})) {
			channel_codec = (Integer) stack.popFirst(CHANNEL_CODEC);
			channel_codec_quality = (Integer) stack.popFirst(CHANNEL_CODEC_QUALITY);
			channel_needed_talk_power = (Integer) stack.popFirst(CHANNEL_NEEDED_TALK_POWER);
		}
//-limits parm
		if(stack.hasFields(new LibTsSym[]{TOTAL_CLIENTS_FAMILY, CHANNEL_MAXCLIENTS, CHANNEL_MAXFAMILYCLIENTS})) {
			total_clients_family = (Integer) stack.popFirst(TOTAL_CLIENTS_FAMILY);
			channel_maxclients = (Integer) stack.popFirst(CHANNEL_MAXCLIENTS);
			channel_maxfamilyclients = (Integer) stack.popFirst(CHANNEL_MAXFAMILYCLIENTS);
		}

		setUpdated();
	}

	@Override
	public TsEntityType getType() {
		return TsEntityType.TSCHANNEL;
	}

	
	@Override
	public String getSlotID() {
		return channel_name+cid;
	}
	
	@Override
	public void updateSlotID(Object k) {
		channel_name = (String)k;
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
	public void setUpdated() {
		state.setUpdatedState(this);
	}

	@Override
	public void setCreated() {
		state.setCreatedState(this);
	}

	@Override
	public void setTouched() {
		state.setTouchedState(this);
	}

	@Override
	public void setUnused() {
		state.setUnusedState(this);
	}
	
	@Override
	public void setInitial() {
		state.setInitialState(this);
	}
	
	@Override
	public LibEntityState getState() {
		return this.state;
	}

	@Override
	public void setState(LibEntityState state) {
		this.state = state;
	}
	

	@Override
	public void setTSXDBID(int id) {
		this.tsxDbId = id;
	}
	@Override
	public int getTSXDBID() {
		return this.tsxDbId;
	}
	
	public void prepareForInsert(BatchSetter setter) {
		setter.injectArgs(cid,pid,channel_order,channel_name,channel_topic,channel_flag_default,channel_flag_password,
				channel_flag_permanent,channel_flag_semi_permanent,channel_codec,channel_codec_quality,
				channel_needed_talk_power,total_clients_family,channel_maxclients,channel_maxfamilyclients,total_clients,channel_needed_subscribe_power);
	}
	
	public void prepareForUpdate(BatchSetter setter) {
		prepareForInsert(setter);
		setter.injectArgs(getTSXDBID());
	}
	
	@Override
	public int getGlueID() {
		return tsxGlueId;
	}
	
	@Override
	public void setGlueID(int id) {
		this.tsxGlueId = id;
	}
}