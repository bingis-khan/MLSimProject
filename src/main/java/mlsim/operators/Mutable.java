package mlsim.operators;
/**
* @return a string of key that the user pressed
 */
public interface Mutable<T extends Mutable<T>> {
	T mutate();
}
