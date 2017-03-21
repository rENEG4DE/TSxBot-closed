package api.service;

@FunctionalInterface
public interface WorkerAction {
		/**
		 * The action to perform per Object
		 * @param T
		 */
		public void performAction (Object object);

}
