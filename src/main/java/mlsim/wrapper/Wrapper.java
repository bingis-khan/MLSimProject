package mlsim.wrapper;

import java.util.List;

import mlsim.operators.Crossover;
import mlsim.operators.Mutable;
import mlsim.simulation.Entity;
import mlsim.simulation.Move;
import mlsim.simulation.SimulationState;

public abstract class Wrapper<T extends Wrapper<T>> implements Mutable<T>, Crossover<T> {
	
	public abstract Move evaluate(Entity self, SimulationState s);
	
	/**
	 * Calculates where the main should move to get closer to moveTo.
	 * However, 'balanced' means, that if the yDistance > xDistance, 
	 * it will chose to move in y instead of x.
	 * 
	 * @param main The entity of our perspective.
	 * @param moveTo The entity we're moving towards.
	 * @return A move that should decrease the distance between the two.
	 */
	protected static Move moveBalanced(Entity main, Entity moveTo) {
		int xDist = xDistance(main, moveTo),
			yDist = yDistance(main, moveTo);
		
		if (xDist > yDist) {
			return moveTowardsX(main, moveTo);
		} else {
			return moveTowardsY(main, moveTo);
		}
	}
	
	/**
	 *  Returns the move which main should take to get closer
	 *  to moveTo on x axis.
	 */
	private static Move moveTowardsX(Entity main, Entity moveTo) {
		return main.getX() > moveTo.getX() ? Move.WEST : Move.EAST;
	}
	
	/**
	 *  Returns the move which main should take to get closer
	 *  to moveTo on y axis.
	 */
	private static Move moveTowardsY(Entity main, Entity moveTo) {
		return main.getY() > moveTo.getY() ? Move.NORTH : Move.SOUTH;
	}
	
	
	/**
	 * Returns the entity that has the smallest distance to main entity.
	 * If the main entity is in the given list, ignores it.
	 * If there are no entities other than main in this list, returns null.
	 * 
	 * @param main Main entity those distance to other entities is being compared to.
	 * @param entities List of entities that will have their distance to entity checked.
	 * @return Closest entity in taxicab distance to main or null if none found.
	 */
	protected static Entity smallestDistance(Entity main, List<? extends Entity> entities) {
		int distance = Integer.MAX_VALUE;
		Entity closest = null;
		
		for (Entity entity : entities) {
			if (!entity.equals(main) && distanceOf(main, entity) < distance) {
				closest = entity;
				distance = distanceOf(main, entity);
			}
		}
		
		return closest;
	}
	
	
	/**
	 * Returns the taxicab distance of e1 to e2.
	 * 
	 * @param e1 First entity.
	 * @param e2 Second entity.
	 * @return Taxicab distance.
	 */
	protected static int distanceOf(Entity e1, Entity e2) {
		return xDistance(e1, e2) + yDistance(e1, e2); 
	}
	
	
	/**
	 *  Returns the difference between y coordinates of the two entities.
	 *  
	 *  @param e1 First entity.
	 *  @param e2 Second entity.
	 *  @return Y coordinate difference.
	 */
	protected static int yDistance(Entity e1, Entity e2) {
		return Math.abs(e1.getY() - e2.getY());
	}
	
	
	/**
	 *  Returns the difference between x coordinates of the two entities.
	 *  
	 *  @param e1 First entity.
	 *  @param e2 Second entity.
	 *  @return X coordinate difference.
	 */
	protected static int xDistance(Entity e1, Entity e2) {
		return Math.abs(e1.getX() - e2.getX());
	}
}
