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
public class GeneticAlgorithm implements Crossover<GeneticAlgorithm>, Mutable<GeneticAlgorithm> {
	private final List<Rule> rules;
	private final int pre, post;
	
	/**
	 * Creates a new genetic algorithm.
	 * 
	 * @param ruleSize Size of a single rule. 
	 * @param preSize Size of a rule's precondition.
	 * 
	 */
	public GeneticAlgorithm(final boolean[] binaryArray, int ruleSize, int preSize) {
		assert binaryArray.length % ruleSize == 0 : "Length of binaryArray must be equal to a multiple"
				+ "of ruleSize.";
		assert preSize < ruleSize : "Pre condition size (pcSize) must be smaller than single ruleSize.";
		assert ruleSize >= 2 : "Rule size must be greater than 2 (minimum for postSize and preSize equal to 1).";
		assert preSize >= 1: "Precondition size must be greater or equal to 1."; 
		
		// Sizes of precondition and postcondition respectively.
		pre = preSize;
		post = ruleSize - preSize;
	
		int ruleNum = binaryArray.length / ruleSize; // Number of single rules in this algorithm.
		rules = new ArrayList<Rule>();
		for (int i = 0; i < ruleNum; i++) {
			// Divides the array into equal chunks that symbolize single rules.
			rules.add(new Rule(Arrays.copyOfRange(binaryArray, i * ruleSize, (i+1) * ruleSize), preSize));
		}
		
		assert ruleNum == rules.size() : "Number of created rules is not equal to the expected number";
	}
	
	/**
	 * Given an array of flags, checks if it matches any condition. 
	 * If so, returns its postcondition value, otherwise returns '-1'
	 * sentinel value.
	 * 
	 * @param flags Flags to compare against each rule's precondition.
	 * @return A postcondition value or -1 if none matched..
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
	
	/**
	 * Returns the amount of rules in this GA.
	 * 
	 * @return Numbers of rules ie. size.
	 */
	public int size() {
		return rules.size();
	}
	
	/**
	 * Returns the size of precondition in bits.
	 * Example: 10011
	 * Pre:100
	 * Post:11
	 * preSize() == 3
	 * postSize() == 2
	 * 
	 * @return Size in bits of precondition.
	 */
	public int preSize() {
		return pre;
	}
	
	/**
	 * Returns the size of postcondition in bits.
	 * Example: 10011
	 * Pre:100
	 * Post:11
	 * preSize() == 3
	 * postSize() == 2
	 * 
	 * @return Size in bits of postcondition.
	 */
	public int postSize() {
		return post;
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
	private final boolean[] postCondition;
	
	Rule(final boolean[] arr, int preSize) {
		
		preCondition = Arrays.copyOf(arr, preSize);
		postCondition = Arrays.copyOfRange(arr, preSize, arr.length);
		
		assert preCondition.length + postCondition.length == arr.length : "Size of precondition + postcondition should be the same as arr.";
	}
	
	/**
	 * Basic conversion from boolean array to integer.
	 * 
	 * @param arr Array whose part to convert to an integer.
	 * @return An integer which was represented by arr.
	 */
	private static int binaryArrayToInt(final boolean[] arr) {
		int num = 0;
		for (int i = 0; i < arr.length; i++) {
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
	 * Basically, for every bit, it checks if the bit in flags implicates 
	 * bit in preCondition. Precondition is kinda like: 
	 * "IF (Cond11 OR Cond12) AND Cond21 Then PC". 
	 * Note, that flag strings like "00000" will always trigger the postCondition,
	 * but should never happen, because that basically means "the absence of state," 
	 * which is, most likely, incorrect.
	 * 
	 * @param flags Flags to compare against this rule's precondition.
	 * @return True if flags fulfill this condition.
	 */
	boolean matches(final boolean[] flags) {
		assert preCondition.length == flags.length : "Length of rule precondition must be equal"
				+ "to the length of given flags. (Flags: " + flags.length + "; PC: " + preCondition.length + ")";
		
		for (int i = 0; i < flags.length; i++) {
			if (!implicates(flags[i], preCondition[i])) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Logical implication.
	 *  l r 
	 * |0|0|1|
	 * |0|1|1|
	 * |1|0|0|
	 * |1|1|1|
	 * 
	 * @param left Left bool.
	 * @param right Right bool.
	 * @return Result of implication.
	 */
	private static boolean implicates(boolean left, boolean right) {
		if (left) {
			return right;
		}
		
		return true;
	}
	
	/**
	 * Returns the post condition of this rule.
	 * 
	 * @return Post condition as an integer.
	 */
	int postCondition() {
		return binaryArrayToInt(postCondition);
	}
	
	/**
	 * Returns the size of this rule.
	 * 
	 * @return Size of this rule.
	 */
	int size() {
		return preCondition.length + postCondition.length;
	}
}