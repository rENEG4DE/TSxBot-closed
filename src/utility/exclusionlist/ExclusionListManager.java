package utility.exclusionlist;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utility.misc.Debuggable;

/**
 * This realizes the an abstract concept of Black/White-lists
 * The user does (except for creation) not care about whether he has a black or white-list,
 * he just asks his list whether an entry is excluded or included by his list
 */
public class ExclusionListManager implements Debuggable{
	private static final String ELIST_COMMENT_PREFIX = "#";

	private interface AbstractExclusionListHelper {
		void addEntry (String entry);
	}
	
	public interface AbstractExclusionList extends Debuggable {
		boolean isIncluded (String entry);
		boolean isExcluded (String entry);
	}
	
	private final static class WhiteList implements AbstractExclusionList,  AbstractExclusionListHelper {
		private List<String> entries;
	
		private WhiteList () {
			entries = new ArrayList<String> (3);
		}
		
		@Override
		public boolean isIncluded(String entry) {
			return entries.contains(entry);
		}

		@Override
		public boolean isExcluded(String entry) {
			return !entries.contains(entry);
		}

		@Override
		public void addEntry(String entry) {
			entries.add(entry);			
		}

		@Override
		public String getStateView() {
			StringBuilder builder = new StringBuilder ("{");
			for (String entry : entries) {
				builder.append("\n\t");
				builder.append(entry);
			}
			builder.append("\n}");
			return builder.toString();
		}
	}
	
	private final static class BlackList implements AbstractExclusionList,  AbstractExclusionListHelper {
		private List<String> entries;
	
		private BlackList () {
			entries = new ArrayList<String> (3);
		}
		
		@Override
		public boolean isIncluded(String entry) {
			return !entries.contains(entry);
		}

		@Override
		public boolean isExcluded(String entry) {
			return entries.contains(entry);
		}
		
		@Override
		public void addEntry(String entry) {
			entries.add(entry);			
		}

		@Override
		public String getStateView() {
			StringBuilder builder = new StringBuilder ("{");
			for (String entry : entries) {
				builder.append("\n\t");
				builder.append(entry);
			}
			builder.append("\n}");
			return builder.toString();
		}
	}
	
	//Start of ExclusionListManager
	
	Map<String, AbstractExclusionList> exListMap;
	
	private static final class SingletonHolder {
		private static final ExclusionListManager INSTANCE = new ExclusionListManager();
	}
	
	public static final ExclusionListManager getSharedInstance () {
		return SingletonHolder.INSTANCE;
	}
	
	private ExclusionListManager() {
		exListMap = new HashMap<>();
		
		addVirtualBlackList("FALLBACK_DEFAULT", "");
	}
	
	public AbstractExclusionList getList (String name) {
		AbstractExclusionList exList = exListMap.get(name);
		if (exList == null) {
			exList = exListMap.get("FALLBACK_DEFAULT");
		}
		return exList;
	}
	
	private void addList (String name, AbstractExclusionList list ) {
		exListMap.put(name, list);
	}
	
	public void addVirtualWhiteList (String name, String... entries) {
		AbstractExclusionList list = new WhiteList ();
		populateList (list, entries);
		addList(name, list);
	}
	
	public void addVirtualBlackList (String name, String... entries) {
		AbstractExclusionList list = new BlackList ();
		populateList (list, entries);
		addList(name, list);
	}
	
	public void addWhiteList (String name, String file) {
		AbstractExclusionList list = new WhiteList ();
		populateList (list, file);
		exListMap.put(name, list);
	};
	
	public void addBlackList (String name, String file) {
		AbstractExclusionList list = new BlackList ();
		populateList (list, file);
		exListMap.put(name, list);
	};
	
	private void populateList(AbstractExclusionList list, String... entries) {
		for (String entry : entries) {
			if (!entry.startsWith(ELIST_COMMENT_PREFIX)) {
				((AbstractExclusionListHelper)list).addEntry(entry);
			}
		}		
	}
	
	private void populateList(AbstractExclusionList list, String file) {
		try (BufferedReader in = new BufferedReader(new FileReader(file))){	
			if (in.ready()) {
				String line;
				while ((line = in.readLine()) != null) {
					if (!line.startsWith(ELIST_COMMENT_PREFIX)) {
						((AbstractExclusionListHelper)list).addEntry(line);
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//List is not found, though non existent
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Unable to read file: " + file, e);
		} 
	}

	public void reset() {
		exListMap.clear();
	}

	@Override
	public String getStateView() {
		StringBuilder builder = new StringBuilder("Exclusion-lists:{");
		for (String key : exListMap.keySet()) {			
			builder.append("\n")
			.append(key)
			.append(":")
			.append(exListMap.get(key).getStateView());
		}
		builder.append("}");
		return builder.toString();
	}
}
