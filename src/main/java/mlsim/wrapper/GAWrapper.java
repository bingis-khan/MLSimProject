package mlsim.wrapper;

import mlsim.simulation.Entity;
import mlsim.simulation.Move;
import mlsim.simulation.SimulationState;

public class GAWrapper extends Wrapper<GAWrapper> {
	
	@Override
	Move evaluate(SimulationState s) {
		
	}
	
	/**
	 * Returns the taxicab distance of e1 to e2.
	 * 
	 * @param e1 First entity.
	 * @param e2 Second entity.
	 * @return Taxicab distance.
	 */
	private int distanceTo(Entity e1, Entity e2) {
		return Math.abs(e1.getX() - e2.getX()) + Math.abs(e1.getY() - e2.getY()); 
	}
	
}
