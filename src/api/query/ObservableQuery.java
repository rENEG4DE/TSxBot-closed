package api.query;


public interface ObservableQuery {
	void registerResultAction (GetResultAction listener);
	void registerReturnAction (GetReturnAction listener);
	void registerDeployAction (OnDeployAction listener);
}