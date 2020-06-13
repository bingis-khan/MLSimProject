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
	@DisplayName("when given '101010' with precondition = 3")
	class WhenGivenASimpleBinaryArray {
		
		@BeforeEach
		public void setup() {
			rule = new Rule(sToBa("101010"), 3);
		}
		
		@Test
		@DisplayName("precondition in binary (b010) should be equal to its integer counterpart (2).")
		public void preconditionInBinaryShouldBeEqualToConvertedInteger() {
			int post = rule.postCondition();
			
			assertEquals(2, post); // 010 is 2 in decimal
		}
		
		@Test
		@DisplayName("calling matches() should return true with '100'.")
		public void callingMatchesShouldReturnTrueWithMatchingCondition() {
			boolean[] fulfillsCondition = sToBa("100");
			
			assertTrue(rule.matches(fulfillsCondition));
		}
		
		@Test
		@DisplayName("calling matches() should return false with '010'.")
		public void callingMatchesShouldReturnFalseWithNonMatchingFlags() {
			boolean[] unfulfilled = sToBa("010");
			
			assertFalse(rule.matches(unfulfilled));
		}
		
		@Test
		@DisplayName("calling size() should return 6.")
		public void callingSizeShouldReturnActualSizeOfThisRule() {
			int size = rule.size();
			
			assertEquals(6, size);
		}
	}
}
