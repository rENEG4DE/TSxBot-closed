package api.query;

import api.data.TsEntityType;

public interface QueryBuilder {
	
	QueryBuilder With(LibTsParm parm);
	
	QueryBuilder With(LibTsParm parm, Object value);

	QueryBuilder WithChained(LibTsParm parm, Object[] values);
	
	QueryBuilder Expecting(TsEntityType type);
	
	QueryBuilder Preparable();

	AbstractQuery Publish();
	
}