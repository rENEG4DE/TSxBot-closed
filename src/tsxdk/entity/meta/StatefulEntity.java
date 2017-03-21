package tsxdk.entity.meta;

public interface StatefulEntity {
	void setUpdated();
	void setUnused();
	void setCreated();
	void setTouched();
	void setInitial();
	
	LibEntityState getState ();
	void setState(LibEntityState state);
}
