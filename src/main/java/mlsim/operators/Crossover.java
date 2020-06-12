package mlsim.operators;

import mlsim.util.Tuple;

/**
 * Crossover is an interface that represents the crossover operation
 *  and the ability to perform it.
 */
public interface Crossover<T extends Crossover<T>> {
	Tuple<T, T> crossover(T other);
}
