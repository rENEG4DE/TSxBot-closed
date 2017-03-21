package tsxdk.parser;

import java.util.HashMap;
import java.util.Map;

import api.data.LibFieldType;

import static tsxdk.parser.LibTsSym.*;


public class LibConvert {
	private static final Map<LibTsSym,LibFieldType> SymbolFieldTypes = new HashMap<>();
	
	//Fill the symbol2FieldTypes map
	static {
		//New fields... thx to if-ts-admin...
		SymbolFieldTypes.put(CLIENT_BADGES, LibFieldType.STRING);				//this is one of those value-less parameters!!!
		
		//Integers
		SymbolFieldTypes.put(CFID, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CHANNEL_CODEC, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CHANNEL_CODEC_QUALITY, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CHANNEL_FLAG_DEFAULT, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CHANNEL_FLAG_PASSWORD, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CHANNEL_FLAG_PERMANENT, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CHANNEL_FLAG_SEMI_PERMANENT, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CHANNEL_MAXCLIENTS, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CHANNEL_MAXFAMILYCLIENTS, LibFieldType.INTEGER);

		SymbolFieldTypes.put(CHANNEL_NEEDED_SUBSCRIBE_POWER, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CHANNEL_NEEDED_TALK_POWER, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CHANNEL_ORDER, LibFieldType.INTEGER);
		
		SymbolFieldTypes.put(CID, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLID, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_AWAY, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_CHANNEL_GROUP_ID, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_CHANNEL_GROUP_INHERITED_CHANNEL_ID, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_CREATED, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_DATABASE_ID , LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_FLAG_TALKING, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_ICON_ID, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_INPUT_HARDWARE, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_INPUT_MUTED, LibFieldType.INTEGER);		//boolean?
		SymbolFieldTypes.put(CLIENT_IS_CHANNEL_COMMANDER, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_IS_PRIORITY_SPEAKER, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_IS_RECORDING, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_IS_TALKER, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_LASTCONNECTED, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_NEEDED_SERVERQUERY_VIEW_POWER, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_OUTPUTONLY_MUTED, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_OUTPUT_HARDWARE, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_OUTPUT_MUTED, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_TALK_POWER, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_TALK_REQUEST, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_TOTALCONNECTIONS, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_TYPE, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CLIENT_UNREAD_MESSAGES, LibFieldType.INTEGER);
		SymbolFieldTypes.put(CTID, LibFieldType.INTEGER);
		SymbolFieldTypes.put(FCLDBID, LibFieldType.INTEGER);
		SymbolFieldTypes.put(ID, LibFieldType.INTEGER);
		SymbolFieldTypes.put(INVOKERID, LibFieldType.INTEGER);
		SymbolFieldTypes.put(PID, LibFieldType.INTEGER);
		SymbolFieldTypes.put(REASONID, LibFieldType.INTEGER);
		SymbolFieldTypes.put(TARGET, LibFieldType.INTEGER);
		SymbolFieldTypes.put(TARGETMODE, LibFieldType.INTEGER);
		SymbolFieldTypes.put(TCLDBID, LibFieldType.INTEGER);
		SymbolFieldTypes.put(TOTAL_CLIENTS, LibFieldType.INTEGER);
		SymbolFieldTypes.put(TOTAL_CLIENTS_FAMILY, LibFieldType.INTEGER);

		//Longs
		SymbolFieldTypes.put(CLIENT_MONTH_BYTES_DOWNLOADED, LibFieldType.LONG);
		SymbolFieldTypes.put(CLIENT_MONTH_BYTES_UPLOADED, LibFieldType.LONG);
		SymbolFieldTypes.put(CLIENT_TOTAL_BYTES_DOWNLOADED, LibFieldType.LONG);
		SymbolFieldTypes.put(CLIENT_TOTAL_BYTES_UPLOADED, LibFieldType.LONG);
		SymbolFieldTypes.put(CONNECTION_BANDWIDTH_RECEIVED_LAST_MINUTE_TOTAL, LibFieldType.LONG);
		SymbolFieldTypes.put(CONNECTION_BANDWIDTH_RECEIVED_LAST_SECOND_TOTAL, LibFieldType.LONG);
		SymbolFieldTypes.put(CONNECTION_BANDWIDTH_SENT_LAST_MINUTE_TOTAL, LibFieldType.LONG);
		SymbolFieldTypes.put(CONNECTION_BANDWIDTH_SENT_LAST_SECOND_TOTAL, LibFieldType.LONG);
		SymbolFieldTypes.put(CONNECTION_BYTES_RECEIVED_TOTAL, LibFieldType.LONG);
		SymbolFieldTypes.put(CONNECTION_BYTES_SENT_TOTAL, LibFieldType.LONG);
		SymbolFieldTypes.put(CONNECTION_FILETRANSFER_BANDWIDTH_RECEIVED, LibFieldType.LONG);
		SymbolFieldTypes.put(CONNECTION_FILETRANSFER_BANDWIDTH_SENT, LibFieldType.LONG);
		SymbolFieldTypes.put(CONNECTION_PACKETS_RECEIVED_TOTAL, LibFieldType.LONG);
		SymbolFieldTypes.put(CONNECTION_PACKETS_SENT_TOTAL, LibFieldType.LONG);
		SymbolFieldTypes.put(TIMESTAMP, LibFieldType.LONG);
		SymbolFieldTypes.put(CLIENT_IDLE_TIME, LibFieldType.LONG);
		
		//BigIntegers
		SymbolFieldTypes.put(CONNECTION_CONNECTED_TIME, LibFieldType.BIGINTEGER);

		//Array of Integer
		SymbolFieldTypes.put(CLIENT_SERVERGROUPS, LibFieldType.ARRAY_OF_INTEGER);
		
		//Strings
		SymbolFieldTypes.put(CHANNEL_NAME, LibFieldType.STRING);
		SymbolFieldTypes.put(CHANNEL_TOPIC, LibFieldType.STRING);
		SymbolFieldTypes.put(CLIENT_AWAY_MESSAGE, LibFieldType.STRING);
		SymbolFieldTypes.put(CLIENT_COUNTRY, LibFieldType.STRING);
		SymbolFieldTypes.put(CLIENT_DEFAULT_CHANNEL, LibFieldType.STRING);			//Wirklich String? warum nicht int? beobachten!
		SymbolFieldTypes.put(CLIENT_DESCRIPTION, LibFieldType.STRING);
		SymbolFieldTypes.put(CLIENT_FLAG_AVATAR, LibFieldType.STRING);
		SymbolFieldTypes.put(CLIENT_META_DATA, LibFieldType.STRING);
		SymbolFieldTypes.put(CLIENT_NICKNAME, LibFieldType.STRING);
		SymbolFieldTypes.put(CLIENT_NICKNAME_PHONETIC, LibFieldType.STRING);
		SymbolFieldTypes.put(CLIENT_PLATFORM, LibFieldType.STRING);
		SymbolFieldTypes.put(CLIENT_TALK_REQUEST_MSG, LibFieldType.STRING);
		SymbolFieldTypes.put(CLIENT_UNIQUE_IDENTIFIER, LibFieldType.STRING);
		SymbolFieldTypes.put(CLIENT_VERSION, LibFieldType.STRING);					// Weis nicht ob das so richtig ist, siehe TQRH get_data_type client_version !!!
		SymbolFieldTypes.put(CONNECTION_CLIENT_IP, LibFieldType.STRING);
		SymbolFieldTypes.put(FNAME, LibFieldType.STRING);
		SymbolFieldTypes.put(INVOKERNAME, LibFieldType.STRING);
		SymbolFieldTypes.put(INVOKERUID, LibFieldType.STRING);
		SymbolFieldTypes.put(MESSAGE, LibFieldType.STRING);
		SymbolFieldTypes.put(MSG, LibFieldType.STRING);
		SymbolFieldTypes.put(REASONMSG, LibFieldType.STRING);
		SymbolFieldTypes.put(TNAME, LibFieldType.STRING);
	}

	public static LibFieldType getConvertFor (LibTsSym symbol) {
//		System.out.println("Trying to get convert for: " + symbol);
		return SymbolFieldTypes.get(symbol);
	}
}
