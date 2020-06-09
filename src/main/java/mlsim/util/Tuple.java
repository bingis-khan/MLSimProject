package mlsim.util;

/**
 * Generic tuple class. (add doxx later)
 * 
 * @author bingis_khan
 *
 * @param <T> Type of the first object.
 * @param <S> Type of the second object.
 */
public class Tuple<T, S> {
	private final T first;
	private final S second;
	
	public Tuple(T first, S second) {
		this.first = first;
		this.second = second;
	}
	
	public T first() {
		return first;
	}
	
	public S second() {
		return second;
	}
}
