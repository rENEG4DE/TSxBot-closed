package system.plugin.serviceprovider;

import system.config.configurable.CVarFactory;
import system.config.configurable.CVarManager;
import api.data.CVarDTI;
import api.data.LibFieldType;
import api.util.CVarUtil;

public class CVarUtilProvider implements CVarUtil {
	
	@Override
	public CVarDTI getCVar(String plugin, String identifier) {
		return CVarManager.getSharedInstance().getCVar(plugin + "." + identifier);
	}
	
	@Override
	public void registerCVar(CVarDTI cVar) {
		CVarManager.getSharedInstance().registerCVar(cVar);
	}
	
	@Override
	public CVarDTI createBoolVar(String identifier, Boolean defVal) {
		return (CVarFactory.createBoolVar(identifier, defVal));
	}
	
	@Override
	public CVarDTI createIntegerVar(String identifier, int defVal) {
		return (CVarFactory.createIntegerVar(identifier, defVal));
	}

	@Override
	public CVarDTI createStringVar(String identifier, String defVal) {
		return (CVarFactory.createStringVar(identifier, defVal));
	}
	
	@Override
	public CVarDTI createArrayVar(String identifier, LibFieldType typeHint) {
		return (CVarFactory.createArrayVar(identifier, typeHint));
	}

	@Override
	public CVarDTI createEnumVar(String identifier, String... choices) {
		return (CVarFactory.createEnumVar(identifier, choices));
	}
}
