package bot.permission;

import java.util.LinkedList;
import java.util.List;


public enum ServerGroupPermissions {

	PERM_I_AM_ROOT (6, new LinkedList<String>() {{add("I_AM_ROOT");}});
	
	final int sgid;
	final List<String> entries;
	
	ServerGroupPermissions(int clid, List<String> entries) {
		this.sgid = clid;
		this.entries = entries;
	}
	
	public int getSgid () {
		return sgid;
	}
	
	public List<String> getEntries () {
		return entries;
	}
}
