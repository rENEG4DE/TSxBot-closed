package tsxdk.entity.meta;

public interface EventEntity {
	
	public void setInitialState(StatefulEntity entity);
	
	public void setTouchedState(StatefulEntity entity);
	
	public void setUpdatedState(StatefulEntity entity);

	public void setCreatedState(StatefulEntity entity);
	
	public void setUnusedState(StatefulEntity entity);

}
