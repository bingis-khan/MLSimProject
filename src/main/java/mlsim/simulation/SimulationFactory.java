package mlsim.simulation;

import java.util.List;

import mlsim.wrapper.GAWrapper;


/**
 * A simulation factory used to create new simulations with the same parameters.
 * 
 * @author bingis_khan
 *
 */
public class SimulationFactory {
	private final int width, height;
	private final double foodPerAgent;

	public SimulationFactory(int width, int height, double foodPerAgent) {
		this.width = width;
		this.height = height;
		this.foodPerAgent = foodPerAgent;
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public double getFoodPerAgent() {
		return foodPerAgent;
	}
	
	/**
	 * Creates a new simulation with the parameters in this SimulationFactory.
	 * 
	 * @param solutions Solutions to insert to the simulation.
	 * @return A newly initialized simulation.
	 */
	public Simulation newSimulation(List<GAWrapper> solutions) {
		return new Simulation(width, height, foodPerAgent, solutions);
	}

}
