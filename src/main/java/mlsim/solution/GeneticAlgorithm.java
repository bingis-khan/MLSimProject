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
	GeneticAlgorithm(int ruleSize, int ruleNum, final boolean[] binaryArray, int pcSize) {
		assert binaryArray.length == ruleNum * ruleSize : "Array size must"
				+ "be equal to ruleNum * ruleSize.";
		
		rules = new ArrayList<Rule>();
		for (int i = 0; i < ruleNum; i++) {
			rules.add(new Rule(Arrays.copyOfRange(binaryArray, i * ruleSize, (i+1) * ruleSize), pcSize));
		}
		
		assert ruleNum == rules.size() : "Number of created rules is not equal to the expected number";
	}
	
	/**
	 * Given a
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
		// Sentinel value '-1' to symbolize that none matched.
		return -1;
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
 * @author Wszyscy :D 2012 wielki mix jutube, jeja
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
	
	/**
	 * Basic conversion from boolean array to integer.
	 * Converts the last part of the array, whose size is
	 * denoted by pcSize.
	 * 
	 * @param arr Array whose part to convert to an integer.
	 * @param pcSize Post condition size ie. number of bytes assigned to post condition.
	 * @return Post condition as an integer.
	 */
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
	 * Checks if the given flags fulfill the condition in this rule.
	 * 
	 * @param flags Flags to compare against this rule's precondition.
	 * @return True if flags fulfill this condition.
	 */
	boolean matches(final boolean[] flags) {
		return false;
	}
	
	/**
	 * Returns the post condition of this rule.
	 * 
	 * @return Post condition as an integer.
	 */
	int postCondition() {
		return postCondition;
	}
	
	/**
	 * Returns the size of this rule.
	 * 
	 * @return Size of this rule.
	 */
	int size() {
		return preCondition.length + postCondition;
	}
}