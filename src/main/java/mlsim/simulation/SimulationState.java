package mlsim.simulation;

import java.util.List;

/**
 * SimulationState is a class that contains:
 *  - placement of agents on this simulation's plane.
 *  - placement of food.
 * 
 * @author bingis_khan
 *
 */
public class SimulationState {
	private final List<? extends Entity> agents, food;
	
	SimulationState(List<? extends Entity> agents, List<? extends Entity> food) {
		this.agents = agents;
		this.food = food;
	}
	
	public List<? extends Entity> agents() {
		return agents;
	}
	
	public List<? extends Entity> food() {
		return food;
	}
}
