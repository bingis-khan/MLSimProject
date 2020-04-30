package mlsim;

abstract class Wrapper<T extends Wrapper<T>> implements Mutable<T>, Crossover<T> {
	
	abstract Move evaluate(SimulationState s);
	
	public Tuple<T, T> crossover(T other) {
		return null;
	}
	
	public T mutate() {
		return null;
	}
}
