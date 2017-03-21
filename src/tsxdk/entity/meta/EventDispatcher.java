package tsxdk.entity.meta;

public interface EventDispatcher {
	void dispatchCreated();
	void dispatchRemoved();
	void dispatchChanged();
	void dispatchAll();
}
