package tsxdk.io.query.engine;

import tsxdk.entity.TsEntity;
import tsxdk.entity.TsReturn;
import tsxdk.io.query.TSxQuery;
import utility.misc.Debuggable;
import utility.systemInfo.SystemObject;
import api.data.TsEntityType;
import api.query.Query;

public interface QueryAgent extends Debuggable, SystemObject {

	void pushQuery(Query query);
	
	TSxQuery instantQuery(String query, TsEntityType resultType);

	TSxQuery pushNewQuery(String query);

	TSxQuery pushNewQuery(String queryContent, TsEntityType resultType);

	TSxQuery prepareQuery(String queryContent);

	TSxQuery prepareQuery(String queryContent, TsEntityType resultType);

	TSxQuery prepareFormattableQuery(String string, TsEntityType expectedReturn);
	
	void deployAll();

	void pushReturn(TsReturn entity);

	void pushResult(TsEntity entity);

	void clearCache();

	void removeQuery(String slot);

	
}