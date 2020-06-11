package mlsim.console;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mlsim.util.Tuple;
import mlsim.wrapper.GAWrapper;

/**
 * Selector is a class that handles the selection of new population, 
 * which includes:
 * - selection
 * - crossover
 * - mutation
 * 
 * @param <GAWrapper>
 * @param old Old population.
 * @param fitness Fitness of this population
 */
class Selector {
	private static final Random RAND = new Random();
	
	private final int selection, crossover, mutation;
	
	public Selector(int sel, int cro, int mut) {
		selection = sel;
		crossover = cro;
		mutation = mut;
		
		assert sel + cro + mut == 100 : "Selection, crossover and mutation must add up to 100.";
	}
	
	private GAWrapper randomSelect(List<GAWrapper> old, List<Integer> fitness) {
		int combinedFitness = 0;
		for (int fit : fitness) {
			combinedFitness += fit;
		}
		
		// Selecting
		int selectedFitness = RAND.nextInt(combinedFitness) + 1;
		int selectIndex = -1;
		for (int fitnessSum = 0; fitnessSum < selectedFitness; selectIndex++, fitnessSum += fitness.get(selectIndex));
		
		return old.get(selectIndex);
	}
	
	List<GAWrapper> updatePopulation(List<GAWrapper> old, List<Integer> fitness) {
		assert old.size() == fitness.size() : "Population size must be equal to the size of the list of fitnesses.";
		
		int selectGa = Math.round(old.size() * selection / (float)100),
			crossoverGa = Math.round(old.size() * crossover / (float)100),
			mutationGa = old.size() - selectGa - crossoverGa;
		
		List<GAWrapper> newPopulation = new ArrayList<>();
		
		// Selection
		for (int i = 0; i < selectGa; i++) {
			GAWrapper select = randomSelect(old, fitness);
			newPopulation.add(select);
		}
		
		// Crossover
		for (int i = 0; i < crossoverGa; i++) {
			GAWrapper co1 = randomSelect(old, fitness),
					  co2 = randomSelect(old, fitness);
			
			Tuple<GAWrapper, GAWrapper> cod = co1.crossover(co2);
			
			newPopulation.add(cod.first());
			newPopulation.add(cod.second());
		}
		
		// Mutation
		for (int i = 0; i < mutationGa; i++) {
			GAWrapper mutated = randomSelect(old, fitness).mutate();
			
			newPopulation.add(mutated);
		}
		
		assert old.size() == newPopulation.size() : "The size of the new population must be the same as the old one.";
		
		return newPopulation;
	}
}
