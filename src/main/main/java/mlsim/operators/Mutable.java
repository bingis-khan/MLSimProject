package mlsim.operators;
/**
 *  Interface is responsible for mutate object
 */
public interface Mutable<T extends Mutable<T>> {
	T mutate();
}
