package system.config;

public interface ConfigParser {
	
	void parseConfigFile ();

	void parseModuleConfigFile(String module);
}
