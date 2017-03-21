package api.query;


public interface AbstractPreparableQuery extends AbstractQuery {

	public void prepare (Object... values);
}
