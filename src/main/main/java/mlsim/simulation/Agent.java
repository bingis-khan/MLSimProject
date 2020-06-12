package mlsim.simulation;

import mlsim.wrapper.Wrapper;

/**
 * Agent is a subclass of entity that represents a single organism in a simulation, 
 * along with coordinates and its hunger meter.
 * 
 * You also provide an id to identify this agent. A shitty duct tape-like solution,
 * because the deadline is tomorrow. 죽고 싶어요.
 * 
 * @author bingis_khan
 *
 * @param <T> The type of the algorithm it uses.
 */
public class Agent<T extends Wrapper<T>> extends Entity {
	private final T genotype;
	private final int id;
	
	private int food;
	
	Agent(T w, int x, int y, int startingFood, int id) {
		super(x, y);
		
		assert startingFood >= 0 : "Starting food of an agent cannot be negative";
		genotype = w;
		food = startingFood;
		
		this.id = id;
	}
	
	/**
	 * Moves the agent using its algorithm given the state of the simulation.
	 * 
	 * @param s State of the simulation in which the agent exists.
	 */
	void move(SimulationState s) {
		Move move = genotype.evaluate(this, s);
		
		switch (move) {
			case NORTH: y--; break;
			case SOUTH: y++; break;
			case WEST:  x--; break;
			case EAST:  x++; break;
		}
	}
	
	/**
	 * Feeds this amount of food to this agent.
	 * 
	 * @param foodAmount Amount of food this agent consumes.
	 */
	void feed(int foodAmount) {
		food += foodAmount;
	}
	
	
	/**
	 * A method used to subtract food from the organism.
	 * 
	 * @param subFood Food to be subtracted from this agent's total.
	 */
	void subtractFood(int subFood) {
		 food -= subFood;
	}
	
	/**
	 *  Checks if an agent has starved.
	 */
	boolean starved() {
		return food <= 0;
	}
	
	/**
	 * Returns this agent's size as defined by the Wrapper class it uses.
	 * 
	 * (Wrapper does not yet have that method, thus this one should not be used.)
	 * 
	 * @return Agent's size.
	 */
	int getSize() {
		return 0;
	}
	
	/**
	 * Returns the algorithm this agent uses to calculate moves.
	 * 
	 * @return The algorithm.
	 */
	T getAlgorithm() {
		return genotype;
	}
	
	/**
	 *  Returns this agent's id.
	 */
	int getId() {
		return id;
	}
}
