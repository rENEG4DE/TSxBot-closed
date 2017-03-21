package tsxdk.parser;

import java.util.HashMap;
import java.util.Map;

import utility.bulletin.AdvancedGlobalBulletin;

public class Parser implements AbstractParser {
	private static Map<LibTsSym, AbstractParser> symbol2ParserMap = new HashMap<> ();
	private static final AdvancedGlobalBulletin Bulletin = AdvancedGlobalBulletin.getSharedInstance();
	
	{//fill symbol2ParserMapping
		symbol2ParserMap.put(LibTsSym.ERROR, LibParse.ReturnParser);
		symbol2ParserMap.put(LibTsSym.CID, LibParse.ListParser);
		symbol2ParserMap.put(LibTsSym.CLID, LibParse.ListParser);
		symbol2ParserMap.put(LibTsSym.CLID_ADVANCED, LibParse.AdvancedClientListParser);
		symbol2ParserMap.put(LibTsSym.TCLDBID, LibParse.ListParser);
		symbol2ParserMap.put(LibTsSym.HASH, LibParse.SnapshotParser);
		symbol2ParserMap.put(LibTsSym.NOTIFYCLIENTENTERVIEW, LibParse.EventParser);
		symbol2ParserMap.put(LibTsSym.NOTIFYCLIENTLEFTVIEW, LibParse.EventParser);
		symbol2ParserMap.put(LibTsSym.NOTIFYTEXTMESSAGE, LibParse.EventParser);
	}

	@Override
	public void parse(final String toParse) {
		//Take care of list types which start immediately with clid=6
		
//		System.out.println("\nNow Parsing " + toParse );
		final String[] posDesc = new String[] {"ParentParser","parseSymbol"};
		
		String dType;
		String fields = null;
//		boolean listType = false;
		
		if (isListData(toParse)) {			//If we have a list
//			dType = new String(toParse.split("=", 2)[0]);
			dType = toParse.split("=", 2)[0];
			fields = toParse;
		} else if (isEventData(toParse)){
			dType = toParse.split(" ", 2)[0];
			fields = toParse;
		} else {								//If we have anything else	
//			dType = new String(toParse.split(" ", 2)[0]);
			dType = toParse.split(" ", 2)[0];
		}

		LibTsSym dataType = null;
		
		try {
			dataType = LibTsSym.parseSymbol(dType);
		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
			Bulletin.PARSER.Warn.push("Can not find symbol", posDesc ,new Object[] {dType});
			return;
		}

//		profiler.startProfile("ParentParser.parse");
//		if (dataType != null) {
//			System.out.println(dataType);
			if (fields == null) {
//				fields = new String(toParse.split(" ", 2)[1]);
				fields = toParse.split(" ", 2)[1];
			}
			getParserFor(dataType).parse(fields);
//			System.out.println(getParserFor(dataType));
			int deleted = 0;
			if ((deleted = LibParse.fieldStack.ensureEmpty()) != 0) {
				Bulletin.PARSER.Warn.push("Fieldstack contained " + deleted + " unused elements, removed!", posDesc);
			}
			
//		} else {
//			System.out.println("Could not find parser for " + temp[0]);
//		}
//		profiler.endProfile();
//		
//		profiler.printProfiles();
	}

	private AbstractParser getParserFor (final LibTsSym symbol) {
		AbstractParser ret = null;
		if ((ret = symbol2ParserMap.get(symbol)) != null) {
			return ret;
		}
		
		return LibParse.TerminalParser;
	}

	private boolean isListData(final String toParse) {
		return toParse.startsWith("clid=") 
				|| toParse.startsWith("cid=") 
				|| toParse.startsWith("tcldbid=");
	}

	private boolean isEventData(final String toParse) {
		return toParse.startsWith("notifycliententerview") 
				|| toParse.startsWith("notifyclientleftview") 
				|| toParse.startsWith("notifytextmessage");
	}
}
