package mlsim.console;

import java.util.Random;

class TestingUtils {
	private static final Random RANDOM = new Random();
	
	/**
	 * Returns a string consisting of random whitespace (' ' and '\t')
	 * between 1 and max, inclusive. Instantiates 
	 * its own Random object.
	 * 
	 * @param max Max amount of whitespace. Must be greater or equal to 1.
	 * @return String containing the whitespace.
	 */
	static String randomSpacing(int max) {
		assert max >= 1;
		
		// Amount of whitespace ACTUALLY written.
		int amount = RANDOM.nextInt(max) + 1;
		
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < amount; i++) {
			if (RANDOM.nextBoolean()) {
				builder.append(' ');
			} else {
				builder.append('\t');
			}
		}
		
		return builder.toString();
	}
	
}
