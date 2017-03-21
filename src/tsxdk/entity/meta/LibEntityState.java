package tsxdk.entity.meta;

/**
 * Allows us to keep track of State an entity is in for
 * later collecting events.
 * A little hacky, but does the trick {@link #test.EntityStatesTest#testStates() JUnit-test}
 *
 */
public enum LibEntityState implements EventEntity {
	INITIAL { // The initial state

		@Override
		public void setUnusedState(StatefulEntity entity) {
			entity.setState(UNUSED);
		}
		
		@Override
		public void setCreatedState(StatefulEntity entity) {
			entity.setState(CREATED);
		}

		@Override
		public void setTouchedState(StatefulEntity entity) {
			entity.setState(TOUCHED);				
		}

		@Override
		public void setInitialState(StatefulEntity entity) {
			setUnusedState(entity);		//This seems right for the moment, but beware it might be a bug source later on...
		}
	},

	TOUCHED { // If the Entity came with a Ts-response

		@Override
		public void setUpdatedState(StatefulEntity entity) {
			entity.setState(UPDATED);
		}

	},

	CREATED {}, // If the entity was new created from the ts-response



	UPDATED {}, // If the entity was changed by the ts-response



	UNUSED { // If the entity was removed and an remove-Event was triggered

		@Override
		public void setCreatedState(StatefulEntity entity) {
			entity.setState(CREATED);
		}
		
		@Override
		public void setInitialState(StatefulEntity entity) {
		}
		
	};

	@Override
	public void setInitialState(StatefulEntity entity) {
		entity.setState(INITIAL);
	}

	@Override
	public void setTouchedState(StatefulEntity entity) {}

	@Override
	public void setCreatedState(StatefulEntity entity) {}

	@Override
	public void setUpdatedState(StatefulEntity entity) {}

	@Override
	public void setUnusedState(StatefulEntity entity) {}
}