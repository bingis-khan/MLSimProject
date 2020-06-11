package mlsim.simulation;

/**
 * An Entity is an abstract class that represents a single entity
 * on a simulation's plane by it's coordinates.
 * 
 * @author bingis_khan
 *
 */
public abstract class Entity {
	protected int x, y;
	
	protected Entity(int x, int y) {
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
	public int getX() {
		return x;
	}
	
	/**
	 * Returns this entity's y coordinate.
	 * 
	 * @return Entity's y coordinate.
	 */
	public int getY() {
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

