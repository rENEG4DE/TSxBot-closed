package api.query;

import api.data.TsEntityType;

public interface Query {
	String getContent ();
	TsEntityType getExpectedType ();
	Long getTimestamp ();
	Long getTimeout ();
	
	AbstractQuery copy();
}