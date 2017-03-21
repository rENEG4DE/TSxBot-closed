package api.util;

import api.data.CVarDTI;
import api.data.LibFieldType;
import api.service.UtilityProvider;

public interface CVarUtil extends UtilityProvider {
	
	CVarDTI getCVar(String plugin, String identifier);
	
	void registerCVar (CVarDTI cVar);
		
	CVarDTI createBoolVar(String identifier, Boolean defVal);
	
	CVarDTI createIntegerVar(String identifier, int defVal);

	CVarDTI createStringVar(String identifier, String defVal);

	CVarDTI createArrayVar(String identifier, LibFieldType typeHint);

	CVarDTI createEnumVar(String identifier, String... choices);

}