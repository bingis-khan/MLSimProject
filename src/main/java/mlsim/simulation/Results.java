package mlsim.simulation;

import java.util.ArrayList;
import java.util.List;

import mlsim.util.Tuple;

/**
 * Results is a class that contains the solutions and their assigned fitness.
 * 
 * @author bingis_khan
 *
 * @param <Genotype> The type of the solution used to move the agent in the simulation.
 */
public class Results<Genotype> {
	private final List<Tuple<Genotype, Integer>> genotypes = new ArrayList<Tuple<Genotype, Integer>>();
	
	/**
	 * Returns the genotypes used in the original simulation.
	 * 
	 * @return Map with those genotypes and their fitness.
	 */
	public List<Tuple<Genotype, Integer>> scoredGenotypes() {
		return genotypes;
	}
	
	/**
	 * Appends the genotype with their fitness to the results.
	 * 
	 * @param ga Genotype.
	 * @param fitness Its fitness.
	 */
	void appendGenotype(Genotype ga, int fitness) {
		genotypes.add(new Tuple<Genotype, Integer>(ga, fitness));
	}
}
