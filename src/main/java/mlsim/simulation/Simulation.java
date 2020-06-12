package mlsim.simulation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import mlsim.wrapper.GAWrapper;


/**
 * Simulation is a class that represents a single simulation.
 * <br>
 * Given its width and height, creates a plane with those dimensions.
 * On this plane, organisms can move, eat, fight.
 * Food per Agent is the number of food samples that will be placed randomly on a plane. 
 * Total number of food samples is calculated as follows: round(foodPerAgents x numAgents)
 * A simulation also needs the solutions themselves to control the agents.
 * <br>
 * 
 * 
 * @author bingis_khan
 *
 */
public class Simulation {
	private final int width, height;
	
	// The number of this simulation's steps.
	private int steps = 1;
	
	private final List<Food> food;
	private final List<Agent<GAWrapper>> agents;
	
	private static final Random rand = new Random();
	
	// A set for agents marked for removal.
	private final Set<Entity> marked = new HashSet<>();
	
	// Results for tracking the scores of agents' genotypes.
	private final Results<GAWrapper> results;
	
	// Used for assigning ids.
	private int nextId = 0;
	
	/* CONSTANTS */
	// Protected, so it can be used by it's subclasses.
	protected static final int STARTING_ENERGY = 30;
	private static final int ENERGY_SUB_PER_STEP = 1,
							 FOOD_ENERGY = 10,
							 SIZE_THRESHOLD = 6;
	
	private static final float PER_SIZE_PENALTY = ENERGY_SUB_PER_STEP / (float)4;
	
	
	/**
	 * Creates a new simulation with given parameters.
	 * 
	 * @param width Width if the plane.
	 * @param height Height of the plane.
	 * @param foodPerAgent How many food samples will be created per agent.
	 * @param genotypes Genotypes themselves that will dictate agent movement.
	 */
	Simulation(int width, int height, double foodPerAgent, List<GAWrapper> genotypes) {
		this.width = width;
		this.height = height;
		
		food = new ArrayList<>();
		agents = new ArrayList<>();
		
		placeAgents(genotypes); // Must be called BEFORE placing food, because it does not check for possible food placement.
		placeFood(calculateFoodAmount(foodPerAgent, agents.size()));
		
		// Create results object.
		results = new Results<>(genotypes);
	}
	
	
	/**
	 *  Calculates the amount of food to place.
	 */
	private int calculateFoodAmount(double foodPerAgent, int numAgents) {
		return (int)Math.round(foodPerAgent * numAgents);
	}
	
	
	/**
	 * Method used to place agents on the simulation plane. 
	 * 
	 * @param genotypes Genotypes to be turned into agents.
	 */
	private void placeAgents(List<GAWrapper> genotypes) {
		for (GAWrapper gtype : genotypes) {
			Agent<GAWrapper> agent;
			
			// Choose random coordinates and repeat if something exists on them already.
			do {
				int x = randomX();
				int y = randomY();
				
				agent = makeAgent(gtype, x, y);
			} while (collidesAgent(agent)); // D: pretty retarded, can run infinitely
			
			agents.add(agent);
			nextId++; // Looks ugly af.
		}
		
		assert genotypes.size() == agents.size() : "The amount of given genotypes"
				+ " and created agents is not equal. Missed some?";
	}
	
	
	/**
	 *  Places food on the simulation plane. Two food samples cannot be placed
	 *  on the same coordinates.
	 */
	private void placeFood(int amount) {
		for (int i = 0; i < amount; i++) {
			Food foodSample;
			
			do {
				int x = randomX(),
					y = randomY();
				
				foodSample = new Food(x, y);
			} while (collidesAgent(foodSample) || collidesFood(foodSample));
			
			food.add(foodSample);
		}
	}
	
	/**
	 * A simple, 'overridable' method for creating a single agent.
	 * 
	 * @param genotype Genotype of this agent.
	 * @param x X position.
	 * @param y Y position.
	 * @return An agent with these coordinates.
	 */
	protected Agent<GAWrapper> makeAgent(GAWrapper genotype, int x, int y) {
		// ASSIGNING IDS IS A BOTCHED AND RETARDED SOLUTION.
		return new Agent<GAWrapper>(genotype, x, y, STARTING_ENERGY, nextId);
	}
	
	/**
	 *  Chooses a random valid x position.
	 */
	protected int randomX() {
		return rand.nextInt(width);
	}
	
	/**
	 * Chooses a random valid y position.
	 */
	protected int randomY() {
		return rand.nextInt(height);
	}
	
	
	/**
	 *  Checks if this simulation ended (when all agents are removed).
	 * 
	 * @return True if ended, false otherwise.
	 */
	public boolean ended() {
		return agents.isEmpty();
	}
	
	
	/**
	 *  Computes a single step of this simulation.
	 *  
	 *  A single step is composed of:
	 *   - agent movement
	 *   - taking care of out-of-bounds agents
	 *   - fights (agents which lost are turned to food)
	 *   - eating food (which includes dead agents turned to food)
	 *   - removal of starved agents
	 *   
	 */
	public void step() {
		
		for (Agent<GAWrapper> agent : agents) {
			// Already removed, so leave it.
			if (isMarked(agent)) {
				continue;
			}
			
			agent.move(getSimulationState());
			
			// Removes this agent if it goes out of bounds.
			if (outOfBounds(agent)) {
				markForRemoval(agent);
				continue;
			}
			
			// Fighting.
			if (collidesAgent(agent)) {
				markForRemoval(collidingAgent(agent));
			}
			
			// Eating.
			if (collidesFood(agent)) {
				feed(agent);
			}
			
			// Subtraction of energy.
			subtractFood(agent);
			
			// Starvation.
			if (agent.starved()) {
				markForRemoval(agent);
			}
		}

		removeMarked();
		
		steps++;
	}
	
	/**
	 *  Subtracting food of an agent.
	 */
	private void subtractFood(Agent<GAWrapper> agent) {
		int size = agent.getSize();
		int penalty = 0;
		
		// Penalty if an organism is too big.
		if (size > SIZE_THRESHOLD) {
			penalty = Math.round((size - SIZE_THRESHOLD) * PER_SIZE_PENALTY);
		}
		
		agent.subtractFood(ENERGY_SUB_PER_STEP + penalty);
	}
	
	
	/**
	 *  Feeding an agent.
	 */
	private void feed(Agent<GAWrapper> agent) {
		agent.feed(FOOD_ENERGY);
		food.remove(collidingFood(agent));
	}
	
	
	/**
	 *  Mark an entity to be removed.
	 */
	private void markForRemoval(Agent<GAWrapper> agent) {
		marked.add(agent);
	}
	
	
	/**
	 *  Check if an entity is marked for removal.
	 */
	private boolean isMarked(Entity listEntity) {
		return marked.contains(listEntity);
	}
	
	
	/**
	 *  Removed all entities marked for removal.
	 */
	@SuppressWarnings("unchecked")
	private void removeMarked() {
		// Removes all agents form main agent list 
		// and saves their scores.
		for (Entity agent : marked) {
			agents.remove(agent);
			addScore((Agent<GAWrapper>) agent);
		}
		
		// Clears this set.
		marked.clear();
	}
	
	
	/**
	 *  Saves an agent and its score to be returned as part
	 *  of this simulation's results.
	 */
	private void addScore(Agent<GAWrapper> agent) {
		results.appendGenotype(agent.getId(), steps);
	}
	
	
	/**
	 *  Checks if this entity is out of bounds.
	 *  
	 *  @return True if it's out of bounds.
	 */
	private boolean outOfBounds(Entity e) {
		int x = e.getX(),
			y = e.getY();
		
		return !(x >= 0 && x < width && y >= 0 && y < height);
	}
	
	
	/**
	 *  Checks if an entity lay on the same space as some agent.
	 */
	private boolean collidesAgent(Entity e) {
		return collides(e, agents) != null;
	}
	
	
	/**
	 *  Checks if this agent collides with an entity and 
	 *  returns that entity.
	 */
	private Agent<GAWrapper> collidingAgent(Entity e) {
		@SuppressWarnings("unchecked") // We know what type it is.
		Agent<GAWrapper> agent = (Agent<GAWrapper>) collides(e, agents);
		
		assert agent != null : "No colliding agents found (returned null), but this is not a valid return value.";
		return agent;
	}
	
	/**
	 *  Returns a food sample that collides with that entity.
	 */
	private Food collidingFood(Entity e) {
		Food fd = (Food) collides(e, food);
		
		assert fd != null : "No colliding agents found (returned null), but this is not a valid return value.";
		return fd;
	}
	
	
	/**
	 *  Checks if an entity lay on the same space as a food sample.
	 */
	private boolean collidesFood(Entity e) {
		return collides(e, food) != null;
	}
	
	/**
	 *  Checks if this entity collides an entity in a given list and returns it.
	 *  If this entity is in this list, it does not count as collision.
	 */
	private Entity collides(Entity e, List<? extends Entity> entities) {
		for (Entity listEntity : entities) {
			if (listEntity.hasSameCoordinates(e) && !listEntity.equals(e)) {
				// Should not count entities that are marked for removal.
				// This method should not care about that and it looks stupid
				// but I'm too retarded to think of something else :(
				if (!isMarked(listEntity)) {
					return listEntity;
				}
			}
		}
		
		return null;
	}
	
	
	/**
	 * A simple convenience method that automatically 
	 * steps through this simulation until it has ended
	 * and returns the results.
	 * 
	 * @return Results of this simulation.
	 */
	public Results<GAWrapper> finish() {
		while (!ended()) {
			step();
		}
		
		return getResults();
	}
	
	
	/**
	 * Returns the simulation state of this simulation.
	 * Changing state through this object is <b>undefined behavior<b>.
	 * 
	 * @return SimulationState of this simulation.
	 */
	public SimulationState getSimulationState() {
		return new SimulationState(agents, food);
	}
	
	
	/**
	 * Computes fitness and returns the results of
	 * this simulation. A program should not be able
	 * to get the results before the simulation has finished.
	 * 
	 * @return Results of this simulation.
	 */
	public Results<GAWrapper> getResults() {
		assert ended() : "Program attempted to retrieve results of the simulation before it has ended.";
		return results;
	}
	
	
	/**
	 * Returns this simulation's plane width.
	 * 
	 * @return Width of the plane.
	 */
	public int getWidth() {
		return width;
	}
	
	
	/**
	 * Returns this simulation's plane height.
	 * 
	 * @return Height of the plane.
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Returns the number of steps this simulation had.
	 * 
	 * @return Number of this simulation's steps.
	 */
	public int getSteps() {
		return steps;
	}
}