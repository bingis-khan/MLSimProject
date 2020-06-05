package mlsim.solution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static mlsim.solution.GeneticAlgorithmTest.sToBa;

@DisplayName("A single rule")
public class RuleTest {
	private Rule rule;
	
	@Nested
	@DisplayName("when given a simple binary array")
	class WhenGivenASimpleBinaryArray {
		
		@BeforeEach
		public void setup() {
			rule = new Rule(sToBa("101010"), 3);
		}
		
		@Test
		@DisplayName("precondition in binary should be equal to its integer counterpart.")
		public void preconditionInBinaryShouldBeEqualToConvertedInteger() {
			int post = rule.postCondition();
			
			assertEquals(2, post); // 010 is 2 in decimal
		}
		
		@Test
		@DisplayName("calling matches() should return true with matching flags.")
		public void callingMatchesShouldReturnTrueWithMatchingCondition() {
			boolean[] fulfillsCondition = sToBa("100");
			
			assertTrue(rule.matches(fulfillsCondition));
		}
		
		@Test
		@DisplayName("calling matches() should return false with non matching flags.")
		public void callingMatchesShouldReturnFalseWithNonMatchingFlags() {
			boolean[] unfulfilled = sToBa("010");
			
			assertFalse(rule.matches(unfulfilled));
		}
		
		@Test
		@DisplayName("calling size() should return the actual size of this rule.")
		public void callingSizeShouldReturnActualSizeOfThisRule() {
			int size = rule.size();
			
			assertEquals(6, size);
		}
	}
}
