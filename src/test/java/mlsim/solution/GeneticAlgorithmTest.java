package mlsim.solution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import mlsim.util.Tuple;

@DisplayName("A genetic algorithm")
class GeneticAlgorithmTest {
	GeneticAlgorithm ga;
	
	@Test
	@DisplayName("should create a new instance when mutating.")
	public void shouldCreateNewInstanceWhenMutating() {
		ga = new GeneticAlgorithm(sToBa("101011"), 3, 1);
		
		GeneticAlgorithm mutated = ga.mutate();
		
		assertNotEquals(mutated, ga);
	}
	
	@Test
	@DisplayName("should create a new instance when crossing over.")
	public void shouldCreateNewInstanceWhenCrossingOver() {
		GeneticAlgorithm toCo, co1, co2;
		
		ga = new GeneticAlgorithm(sToBa("111011"), 3, 1);
		toCo = new GeneticAlgorithm(sToBa("101110110"), 3, 1);
		
		Tuple<GeneticAlgorithm, GeneticAlgorithm> tuple = ga.crossover(toCo);
		co1 = tuple.first();
		co2 = tuple.second();
		
		assertNotEquals(ga, co1);
		assertNotEquals(ga, co2);
	}
	
	@Nested
	@DisplayName("with code '01011 11001' and pc = 2")
	class WithCode0101111001 {
		
		@BeforeAll
		public void setup() {
			ga = new GeneticAlgorithm(sToBa("0101111001"), 5, 2);
		}
		
		@Test
		@DisplayName("calling evaluate() with '010' should return 3 (b11).")
		public void callingEvaluateShouldReturn3() {
			int eval = ga.evaluate(sToBa("010"));
			
			assertEquals(3, eval);
		}
		
		@Test
		@DisplayName("calling evaluate() with '100' should return 1 (b01).")
		public void callingEvaluateWith100ShouldReturn1() {
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
			assert bin.charAt(i) == '1' || bin.charAt(i) == '0' : "Given binary string must be only 0s and 1s."
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
