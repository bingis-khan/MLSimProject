package mlsim.solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
	private static final Random RAND = new Random();
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
	
	private GeneticAlgorithm(List<Rule> left, Rule newRule, List<Rule> right, int pre, int post) {
		this.pre = pre;
		this.post = post;
		
		rules = new ArrayList<Rule>();
		rules.addAll(left);
		rules.add(newRule);
		rules.addAll(right);
	}
	
	/**
	 * Given an array of flags, checks if it matches any condition. 
	 * If so, returns its postcondition value, otherwise returns '-1'
	 * sentinel value.
	 * 
	 * @param flags Flags to compare against each rule's precondition.
	 * @return A postcondition value or -1 if none matched..
	 */
	public int evaluate(final boolean[] flags) {
		
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
		int mutatePoint = RAND.nextInt(size());
		
		return new GeneticAlgorithm(to(mutatePoint), ruleAt(mutatePoint).ruleMutate(), from(mutatePoint), pre, post);
	}

	@Override
	public Tuple<GeneticAlgorithm, GeneticAlgorithm> crossover(GeneticAlgorithm other) {
		int coPoint1 = RAND.nextInt(rules.size()),
			coPoint2 = RAND.nextInt(other.size());
		
		Rule rule1 = ruleAt(coPoint1),
			 rule2 = other.ruleAt(coPoint2);
		
		Tuple<Rule, Rule> coTuple = rule1.rulesCrossover(rule2);
		
		GeneticAlgorithm coGa1 = new GeneticAlgorithm(other.to(coPoint2), coTuple.first(), from(coPoint1), pre, post),
						 coGa2 = new GeneticAlgorithm(to(coPoint1), coTuple.second(), other.from(coPoint2), pre, post);
		
		return new Tuple<GeneticAlgorithm, GeneticAlgorithm>(coGa1, coGa2);
	}
	
	/**
	 * Returns i-th rule.
	 */
	private Rule ruleAt(int i) {
		assert i >= 0;
		assert i < rules.size();
		
		return rules.get(i);
	}
	
	/**
	 * Copies rules <0, to)
	 */
	private List<Rule> to(int to) {
		assert to >= 0;
		assert to < rules.size();
		
		List<Rule> list = new ArrayList<Rule>();
		for (int i = 0; i < to; i++) {
			list.add(rules.get(i));
		}
		
		return list;
	}
	
	/**
	 * Copies rules (i, size()>
	 * 
	 * @param i
	 * @return
	 */
	private List<Rule> from(int from) {
		assert from >= 0;
		assert from < rules.size();
		
		List<Rule> list = new ArrayList<Rule>();
		for (int i = from+1; i < rules.size(); i++) {
			list.add(rules.get(i));
		}
		
		return list;
	}
	
	/**
	 * Presents the genetic algorithm as string.
	 * 
	 * @return Binary string.
	 */
	public String asString() {
		StringBuilder code = new StringBuilder();
		
		for (Rule rule : rules) {
			code.append(rule.asString());
		}
		
		return code.toString();
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
	private static final Random RAND = new Random();
	private final boolean[] preCondition;
	private final boolean[] postCondition;
	
	private boolean[] onerule;
	
	Rule(final boolean[] arr, int preSize) {
		
		preCondition = Arrays.copyOf(arr, preSize);
		postCondition = Arrays.copyOfRange(arr, preSize, arr.length);
		onerule = Arrays.copyOf(arr, arr.length);
		
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
	
	/**
	 * String representation of a single rule.
	 * 
	 * @return Binary string.
	 */
	String asString() {
		StringBuilder rule = new StringBuilder();
		
		for (boolean b : preCondition) {
			rule.append(booleanToChar(b));
		}
		
		for (boolean b : postCondition) {
			rule.append(booleanToChar(b));
		}
		
		return rule.toString();
	}
	
	/**
	 * Converts this boolean to char '1' or '0'.
	 */
	private char booleanToChar(boolean b) {
		return b ? '1' : '0';
	}
	
	
	/* MGD (poprawione)*/
	
	private Rule(Rule other) {

		preCondition = other.preCondition.clone();
		postCondition = other.postCondition.clone();
		onerule = new boolean[size()]; // Tu tworzysz zmienn¹ w œrodku konstruktora i przes³aniasz 
												 // t¹ na zewn¹trz, nie zmieniasz wartoœci pola obiektu.
		for (int i = 0; i < preCondition.length; i++) {
			onerule[i] = preCondition[i];
		}
		for (int i = preCondition.length; i < postCondition.length; i++) {
			onerule[i] = postCondition[i]; 
		} // Wiêc to nic nie robi.
		
	}

	private void preConditionMutate(Rule result, int mutatePoint) {
		// To nie jest jakoœ wa¿ne, ale to mo¿e byæ skrócone do result.preCondition[mutatePoint] = !result.preCondition[mutatePoint];
		if (preCondition[mutatePoint] == true) {
			result.preCondition[mutatePoint] = false;
		} else {
			result.preCondition[mutatePoint] = true;
		}
	}

	private void postConditionMutate(Rule result, int mutatePoint) {

		if (postCondition[mutatePoint] == true) {
			result.postCondition[mutatePoint] = false;
		} else {
			result.postCondition[mutatePoint] = true;
		}

	}

	Rule ruleMutate() { 
		Random rn = new Random();
		int mutatePoint = rn.nextInt(size());
		Rule result = new Rule(this);
		if (mutatePoint >= preCondition.length) {
			postConditionMutate(result, mutatePoint - preCondition.length);
		} else {
			preConditionMutate(result, mutatePoint);
		}
		
		return result;
	}
	
	// Dzia³a... ale
	void doCrossPartOne(int crossoverPoint, Rule result, Rule result2, Rule other) {

		for (int i = 0; i < crossoverPoint; i++) {
			boolean temp = onerule[i];
			boolean temp2 = other.onerule[i];
			result2.onerule[i] = temp;
			result.onerule[i] = temp2;
		}
	}
	
	// Copying from 'onerule' to conditions.
	// Ah fufjcjancibci
	private void copyOneRule() {
		System.arraycopy(onerule, 0, preCondition, 0, preCondition.length);
		System.arraycopy(onerule,  preCondition.length,  postCondition,  0, postCondition.length);
	}

	Tuple<Rule, Rule> rulesCrossover(Rule other) {
		int crossoverPoint = RAND.nextInt(size());
		Rule result1 = new Rule(this);
		Rule result2 = new Rule(other);
		// xd. Gdy masz rule1 A(crossoverPoint)B, rule2 C(...)D, dla ABCD bêd¹cych ci¹giem bitów to wygl¹da to tak:
		doCrossPartOne(crossoverPoint, result1, result2, other); // rule1 CB, rule2 AD
		//doCrossPartTwo(crossoverPoint, result1, result2, other); // rule1 CD, rule2 AB (tak naprawdê zamieniliœmy dwa rule.)Powinnaœ wywo³aæ tylko jedno z nich. Jeœli w GeneticAlgorithm zamieniasz ci¹g przed, to wywo³ujesz to pierwsze. Jeœli ci¹g po, to to drugie.
		result1.copyOneRule();
		result2.copyOneRule();
		
		return new Tuple<Rule, Rule>(result1, result2);
	}
}