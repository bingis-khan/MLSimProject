package mlsim.wrapper;

import mlsim.operators.Crossover;
import mlsim.operators.Mutable;
import mlsim.simulation.Move;
import mlsim.simulation.SimulationState;
import mlsim.util.Tuple;

public abstract class Wrapper<T extends Wrapper<T>> implements Mutable<T>, Crossover<T> {
	
	abstract Move evaluate(SimulationState s);
	
	public Tuple<T, T> crossover(T other) {
		return null;
	}
	
	public T mutate() {
		return null;
	}
}
