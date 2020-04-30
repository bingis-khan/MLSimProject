package mlsim;

/**
 * Generic tuple class. (add doxx later)
 * 
 * @author bingis_khan
 *
 * @param <T>
 * @param <S>
 */
class Tuple<T, S> {
	private final T first;
	private final S second;
	
	Tuple(T first, S second) {
		this.first = first;
		this.second = second;
	}
	
	T first() {
		return first;
	}
	
	S second() {
		return second;
	}
}
