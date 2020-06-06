package mlsim.solution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import mlsim.util.Tuple;

@DisplayName("A genetic algorithm")
public class GeneticAlgorithmTest {
	GeneticAlgorithm ga;
	
	@Nested
	@DisplayName("given a bit string = '110101', rule size = 3 and precondition = 2")
	class WithBitStringEqual110101 {
		@BeforeEach
		public void setup() {
			ga = new GeneticAlgorithm(sToBa("101011"), 3, 2);
		}
		
		@Test
		@DisplayName("calling size() should return 2.")
		public void callingSize_shouldReturn2() {
			assertEquals(2, ga.size());
		}
		
		@Nested
		@DisplayName("calling mutate()")
		class CallingMutate {
			private GeneticAlgorithm mutated;
			
			@BeforeEach
			public void mutateCall() {
				mutated = ga.mutate();
			}
			
			@Disabled
			@Test
			@DisplayName("should create a new instance.")
			public void shouldCreateNewInstance() {
				assertNotEquals(mutated, ga);
			}
			
			@Disabled
			@Test
			@DisplayName("should return a genetic algorithm with the same size.")
			public void shouldReturnGAWithSameSize() {
				assertEquals(ga.size(), mutated.size());
			}
		}
		
		@Nested
		@DisplayName("calling crossover() with another GA = '101110011'")
		class CallingCrossoverWithAnotherGAEqual101110011 {
			private GeneticAlgorithm other, co1, co2;
			@BeforeEach
			public void callingCrossoverAndAssignment() {
				other = new GeneticAlgorithm(sToBa("101110011"), 3, 1);
				
				Tuple<GeneticAlgorithm, GeneticAlgorithm> tuple = ga.crossover(other);
				
				co1 = tuple.first();
				co2 = tuple.second();
			}
			
			@Disabled
			@Test
			@DisplayName("should create new instances.")
			public void shouldCreateNewInstances() {
				assertNotEquals(ga, co1);
				assertNotEquals(ga, co2);
				assertNotEquals(other, co1);
				assertNotEquals(other, co2);
			}
			
			@Disabled
			@Test
			@DisplayName("should create instances greater or equal to 1 in size.")
			public void shouldCreateInstancesGreaterOrEqualTo1InSize() {
				assertTrue(co1.size() >= 1);
				assertTrue(co2.size() >= 1);
			}
			
			@Disabled
			@Test
			@DisplayName("should create instances smaller or equal to 4 in size. (sz1 - 1 + sz2)")
			public void shouldCreateInstancesSmallerOrEqualTo4InSize() {
				assertTrue(co1.size() <= 4);
				assertTrue(co2.size() <= 4);
			}
			
			@Disabled
			@Test
			@DisplayName("should create instances with sum of their sizes equal to the sum of their parents' sizes.")
			public void shouldCreateInstancesWithSizeSumEqualToTheirParentsSizeSum() {
				assertEquals(ga.size() + other.size(), co1.size() + co2.size());
			}
		}
	}
	
	@Nested
	@DisplayName("with bit string = '01011 11001' and preSize = 3")
	public class WithBitStringEqual0101111001 {
		
		@BeforeEach
		public void setup() {
			ga = new GeneticAlgorithm(sToBa("0101111001"), 5, 3);
		}
		
		@Test
		@DisplayName("calling evaluate() with '010' should return 3 (b11).")
		public void callingEvaluate_shouldReturn3() {
			int eval = ga.evaluate(sToBa("010"));
			
			assertEquals(3, eval);
		}
		
		@Test
		@DisplayName("calling evaluate() with '100' should return 1 (b01).")
		public void callingEvaluateWith100_shouldReturn1() {
			int eval = ga.evaluate(sToBa("100"));
			
			assertEquals(1, eval);
		}
		
		@Test
		@DisplayName("calling evaluate() with '001' should return -1 (sentinel, should not match).")
		public void callingEvaluateWith001ShouldReturnNegative1() {
			int eval = ga.evaluate(sToBa("001"));
			
			assertEquals(-1, eval);
		}
	}
	
	
	/**
	 * Quick function to convert a string to boolean array.
	 * 
	 * @param bin
	 * @return
	 */
	public static boolean[] sToBa(String bin) {
		boolean[] binArray = new boolean[bin.length()];
		
		for (int i = 0; i < binArray.length; i++) {
			assert (bin.charAt(i) == '1' || bin.charAt(i) == '0') : "Given binary string must be only 0s and 1s."
					+ " This one had '" + bin.charAt(i) + "' at " + i + ".";
			
			if (bin.charAt(i) == '1') {
				binArray[i] = true;
			} else if (bin.charAt(i) == '0') {
				binArray[i] = false;
			}
		}
		
		return binArray;
	}
}
