package mlsim;


interface Crossover<T extends Crossover<T>> {
	Tuple<T, T> crossover(T other);
}

interface Mutable<T extends Mutable<T>> {
	T mutate();
}