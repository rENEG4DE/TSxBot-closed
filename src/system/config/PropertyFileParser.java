package system.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;

import system.config.configurable.CVar;
import system.config.configurable.CVarManager;
import system.core.Context;
import utility.log.Log;

/**
 * Parses a standard java property-file with assistance of CVars
 * @author kornholio
 *
 */

 public class PropertyFileParser implements ConfigParser {

	private Log log = Context.getSharedInstance().getLog();
	private Properties config;
	private String filename;
	
	public PropertyFileParser (String filename) {

		log.printSection("Loading config {" + filename + "}");

	    this.filename = filename;
		config = new Properties();

	    try (final FileInputStream iStream = new FileInputStream(filename)) {
	        config.load(iStream);
	    } catch (FileNotFoundException e) {	    	
	        log.logp(Level.SEVERE, "PropertyFileParser", 
	        		"PropertyFileParser (String filename) ", 
	        		"Config-File "+ filename+ " could not be found!", e);
	        return;
	    } catch (IOException e) {
	    	log.logp(Level.SEVERE, "PropertyFileParser", 
	        		"PropertyFileParser (String filename) ", 
	        		"Config-File "+ filename+ " could not be accessed!", e);
	        return;
	    }
	    
	    log.fine("Config-File ready to be parsed!");
	    log.printSeperator();
	}
	
	@Override
	public void parseConfigFile() {

		CVarManager manager = CVarManager.getSharedInstance();
		Enumeration<Object> en = config.keys();
		
	    log.printSection("Parsing System configuration {" + filename + "}");
	    while (en.hasMoreElements()) {
	        
	        String key = (String) en.nextElement();
	        String value = (String) config.get(key);
	   
	        CVar current = (CVar) manager.getCVar(key);
	        if (current != null) {
	        	log.config(key + ":=" + value);
	        	current.parse(value);
	        	//continue;
	        } else {
	        	log.config("Unknown CVar \"" + key + "\"");
	        }
	    }
	    log.printSeperator();
	}
	
	@Override
	public void parseModuleConfigFile(String prefix) {

		CVarManager manager = CVarManager.getSharedInstance();
		Enumeration<Object> en = config.keys();
		
	    log.printSection("Parsing Plugin configuration {" + filename + "}");
	    while (en.hasMoreElements()) {
	        
	        String key = (String) en.nextElement();
	        String value = (String) config.get(key);
	        String name = prefix + "." + key;
	        
	        CVar current = (CVar) manager.getCVar(name);
	        if (current != null) {
	        	log.config(name + ":=" + value);
	        	current.parse(value);
	        } else {
	        	log.config("Unknown CVar \"" + name + "\"");
	        }
	    }
	    log.printSeperator();
	}
}
