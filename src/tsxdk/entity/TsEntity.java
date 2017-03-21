package tsxdk.entity;

import api.data.TsEntityType;
import tsxdk.parser.TsFieldStack;

public interface TsEntity {

	void update(TsFieldStack stack);
	TsEntityType getType ();
	
	void setTsPropHash(String str);		//Set the  property-string this channel was last updated with
	int getTsPropHash();		//Get the hash of the property-string this channel was last updated with

	void setTSXDBID (int id);
	int getTSXDBID ();
	
	void setGlueID (int id);
	int getGlueID ();
	
}
