package mlsim.operators;

public interface Mutable<T extends Mutable<T>> {
	T mutate();
}
