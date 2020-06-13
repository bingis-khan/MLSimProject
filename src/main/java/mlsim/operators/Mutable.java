package mlsim.operators;
/**
 *  Mutable is an interface that represents the object that can be mutated.
 */
public interface Mutable<T extends Mutable<T>> {
	T mutate();
}
