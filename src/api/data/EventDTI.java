package api.data;


/**
 * A data transfer interface for accessing EventData
 * all known Data of an Event can be accessed with this. 
 * //NOTE: Please review this, for the javadoc is only rudimentary!
 * @author kornholio
 */
public interface EventDTI {

	 /**
	  * Get a a decent Descriptor for this Event
	  * @return an event-descriptor
	  */
	String getEventDescriptor();

	 /**
	  * Get the client bound to this Event
	 * @return a TsClient
	 */
	TsClientDTI getClient();

	 /**
	  * @return the Clid
	  */
	int getClid();
	
	 /**
	 * @return the Cfid
	 */
	int getCfid();

	 /**
	 * @return the Ctid
	 */
	int getCtid();

	 /**
	 * @return the reasonId
	 */
	int getReasonid();

	 /**
	  * //NOTE: might change to boolean soon!
	 * @return if outputonly muted
	 */
	int getClient_outputonly_muted();

	/**
	 * @return if the client requested talking
	 */
	int getClient_talk_request();

	 /**
	 * @return the client-talk-request message
	 */
	String getClient_talk_request_msg();

	 /**
	 * @return the client's unread messages
	 */
	int getClient_unread_messages();

	 /**
	 * @return the Complain bound to this Event
	 * @deprecated
	 */
	TsComplainDTI getCurrent_complain();

	/**
	 * @return the Complain bound to this Event
	 */
	 TsComplainDTI getComplain();

	 /**
	 * @return the Target of this Event
	 */
	int getTarget();

	 /**
	 * @return the Invokers Database ID
	 */
	int getInvokerid();

	 /**
	 * @return the TargetMode
	 */
	int getTargetmode();

	 /**
	 * @return the Message
	 */
	String getMsg();

	 /**
	 * @return the Invokers name
	 */
	String getInvokername();

	 /**
	 * @return the Invokers uid
	 */
	String getInvokeruid();

	 /**
	 * @return the Reason the client left
	 */
	String getReasonmsg();

	TsClientDTI getCurrent_client();

}