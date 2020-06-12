package mlsim.solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mlsim.wrapper.GAWrapper;

/**
 * GARandomFactory is a class that is responsible for
 * creating new random GeneticAlgorithms.
 * 
 * @author bingis_khan
 *
 */
public class GARandomFactory {
	private static final Random RAND = new Random();
	
	/**
	 * Generates a population of random GeneticAlgorithms, automatically wrapping them
	 * in GAWrapper.
	 * 
	 * @param minSize Minimum GA size.
	 * @param maxSize Maximum GA size.
	 * @param populationSize Size of the population ie. how many GAs to generate.
	 * @param preSize Precondition size.
	 * @param postSize Postcondition size.
	 * @return Population of size populationSize of random genetic algorithms.
	 */
	public static List<GAWrapper> generatePopulation(int minSize, int maxSize, int populationSize, int preSize, int postSize) {
		assert minSize >= 1 : "Minimum size cannot be smaller than 1.";
		assert maxSize >= minSize : "Maximum size must be greater or equal to minimum size.";
		assert populationSize > 0 : "Population size cannot be empty, smaller than 0.";
		
		List<GAWrapper> population = new ArrayList<GAWrapper>();
		for (int i = 0; i < populationSize; i++) {
			population.add(new GAWrapper(generateGeneticAlgorithm(minSize, maxSize, preSize, postSize)));
		}
		
		assert population.size() == populationSize : "Size of population list must be equal to populationSize.";
		return population;
	}

	private static GeneticAlgorithm generateGeneticAlgorithm(int minSize, int maxSize, int preSize, int postSize) {
		int ruleSize = preSize + postSize;
		int randomizedSize = minSize + RAND.nextInt(maxSize - minSize + 1);
		boolean[] randomArray = randomBinaryArray(randomizedSize * ruleSize);
		
		GeneticAlgorithm ga = new GeneticAlgorithm(randomArray, ruleSize, preSize);
		
		assert ga.size() >= minSize : "Size of a GA must be greater than or equal to minSize.";
		assert ga.size() <= maxSize : "Size of a GA must be smaller or equal to maxSize.";
		return ga;
	}
	
	private static boolean[] randomBinaryArray(int size) {
		assert size > 0;
		
		boolean[] randomArray = new boolean[size];
		for (int i = 0; i < size; i++) {
			randomArray[i] = RAND.nextBoolean();
		}
		
		return randomArray;
	}
}
