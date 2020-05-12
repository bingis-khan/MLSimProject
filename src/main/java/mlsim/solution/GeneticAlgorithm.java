package mlsim.solution;

import mlsim.operators.Crossover;
import mlsim.operators.Mutable;
import mlsim.util.Tuple;

class GeneticAlgorithm implements Crossover<GeneticAlgorithm>, Mutable<GeneticAlgorithm> {
	
	// Think of a proper way to construct genotypes.
	GeneticAlgorithm() {
		
	}
	
	int evaluate(final boolean[] flags) {
		return 0;
	}

	@Override
	public GeneticAlgorithm mutate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Tuple<GeneticAlgorithm, GeneticAlgorithm> crossover(GeneticAlgorithm other) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
