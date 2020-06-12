package mlsim.operators;

import mlsim.util.Tuple;

public interface Crossover<T extends Crossover<T>> {
	Tuple<T, T> crossover(T other);
}
