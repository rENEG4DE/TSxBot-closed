package api.service;

import java.util.Map;

public interface AbstractCLICommand {
	void execute(Map<String, Object> arguments);
	
	String getId ();
	String getInfoString ();

}
