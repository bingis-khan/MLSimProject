package mlsim.simulation;

import mlsim.wrapper.Wrapper;

/**
 * An Entity is an abstract class that represents a single entity
 * on a simulation's plane by it's coordinates.
 * 
 * @author bingis_khan
 *
 */
public abstract class Entity {
	private int x, y;
	
	Entity(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 *  Checks if a second entity has the same coordinates as this one.
	 *  
	 *  @return True if it has the same coordinates.
	 */
	boolean hasSameCoordinates(Entity other) {
		return getX() == other.getX() && getY() == other.getY();
	}
	
	/**
	 * Returns this entity's x coordinate.
	 * 
	 * @return Entity's x coordinate.
	 */
	int getX() {
		return x;
	}
	
	/**
	 * Returns this entity's y coordinate.
	 * 
	 * @return Entity's y coordinate.
	 */
	int getY() {
		return y;
	}
}

/**
 * Food is a subclass of Entity that represents a food sample.
 * 
 * @author bingis_khan
 *
 */
class Food extends Entity {

	Food(int x, int y) {
		super(x, y);
	}
}

/**
 * Agent is a subclass of entity that represents a single organism in a simulation, 
 * along with coordinates and its hunger meter.
 * 
 * @author bingis_khan
 *
 * @param <T> The type of the algorithm it uses.
 */
class Agent<T extends Wrapper<T>> extends Entity {
	private final T genotype;
	
	private int food;
	
	Agent(T w, int x, int y, int startingFood) {
		super(x, y);
		
		assert startingFood >= 0 : "Starting food of an agent cannot be negative";
		genotype = w;
		food = startingFood;
	}
	
	/**
	 * Moves the agent using its algorithm given the state of the simulation.
	 * 
	 * @param s State of the simulation in which the agent exists.
	 */
	void move(SimulationState s) {}
	
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
}