package mlsim.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mlsim.operators.Crossover;
import mlsim.operators.Mutable;
import mlsim.util.Tuple;

/**
 * Klasa reprezentuje ci¹g binarny, który koduje pewne warunki, 
 * pozwala na wykonanie operacji crossover i mutate.
 * 
 * This class is immutable.
 * 
 * @author Wszyscy :)
 *
 */
class GeneticAlgorithm implements Crossover<GeneticAlgorithm>, Mutable<GeneticAlgorithm> {
	private final List<Rule> rules;
	
	/**
	 * 
	 * 
	 * @param ruleSize Size of a single rule. 
	 * @param ruleNum Number of this GA's rules.
	 * 
	 */
	GeneticAlgorithm(int ruleSize, int ruleNum, final boolean[] binaryArray) {
		assert binaryArray.length == ruleNum * ruleSize : "Array size must"
				+ "be equal to ruleNum * ruleSize.";
		
		rules = new ArrayList<Rule>();
		for (int i = 0; i < ruleNum; i++) {
			rules.add(new Rule(Arrays.copyOfRange(binaryArray, i * ruleSize, (i+1) * ruleSize)));
		}
		
		assert ruleNum == rules.size() : "Number of created rules is not equal to the expected number";
	}
	
	/**
	 * 
	 * 
	 * @param flags
	 * @return
	 */
	int evaluate(final boolean[] flags) {
		for (Rule rule : rules) {
			if (rule.matches(flags)) {
				return rule.postCondition();
			}
		}
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


/**
 * Rule rezprezentuje jeden warunek w GA. 
 * 
 * @author Wszyscy :D 2012 wielki mix jutube, yolo
 *
 */
class Rule {
	private final boolean[] preCondition;
	private final int postCondition;
	
	Rule(final boolean[] arr, int postConditionSize) {
		int preConditionSize = arr.length - postConditionSize;
		
		preCondition = Arrays.copyOf(arr, preConditionSize);
		postCondition = fromBinaryArrayToInt(arr, postConditionSize);
	}
	
	
	private static int fromBinaryArrayToInt(final boolean[] arr, int pcSize) {
		int num = 0;
		for (int i = arr.length - 1; i < (arr.length - 1 - pcSize); i--) {
			num <<= 1;
			
			if (arr[i]) {
				num += 1;
			}
		}
		
		assert num >= 0 : "Post condition cannot be negative.";
		return num;
	}
	
	/**
	 * 
	 * 
	 * @param arr
	 * @return
	 */
	boolean matches(final boolean[] arr) {
		
	}
	
	int postCondition() {
		return postCondition;
	}
}