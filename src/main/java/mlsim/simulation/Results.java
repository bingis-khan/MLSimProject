package mlsim.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mlsim.util.Tuple;
import mlsim.wrapper.GAWrapper;

/**
 * Results is a class that contains the solutions and their assigned fitness.
 * 
 * @author bingis_khan
 *
 * @param <Genotype> The type of the solution used to move the agent in the simulation.
 */
public class Results<Genotype> {
	private final List<GAWrapper> genotypes;
	private final int[] fitnessScores;
	
	Results(List<GAWrapper> genotypes) {
		this.genotypes = genotypes;
		fitnessScores = new int[genotypes.size()];
	}
	
	/**
	 * Returns the fitness of these genotypes.
	 * 
	 * @return List with the genotypes' fitness.
	 */
	public List<Integer> fitness() {
		// AAHHHH FUCK, ASDADSASDDSAOJDJAOSDNAODSN
		List<Integer> fitnessScoresList = new ArrayList<>();
		for (int id = 0; id < fitnessScores.length; id++) {
			fitnessScoresList.add(fitnessScores[id]);
		}
		
		return fitnessScoresList;
	}
	
	/**
	 * Returns the genotypes used in the original simulation.
	 * 
	 * @return Map with those genotypes and their fitness.
	 */
	public List<GAWrapper> genotypes() {
		return genotypes;
	}
	
	/**
	 * Appends the fitness of the genotype with this id.
	 * 
	 * @param id Id of the genotype.
	 * @param fitness Its fitness.
	 */
	void appendGenotype(int id, int fitness) {
		fitnessScores[id] = fitness;
	}
}
