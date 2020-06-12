package mlsim.console;

import org.checkerframework.checker.nullness.qual.NonNull;

/**
 * Query is a class made to simplify input parsing for commands.
 * Based immutability. 
 * 
 * @author bingis_khan aka superXbob69420
 *
 */
class Query {
	// Array with split tokens for parsing.
	private final String[] tokens;
	
	// Current token.
	private int index = 0;
	
	public Query(@NonNull String userString) {
		
		// If string is only whitespace, create a 0 length array,
		// so each method should run properly.
		if (userString.isBlank()) {
			tokens = new String[0];
			return;
		}
		
		// Avoid an empty string at the start.
		String trimmed = userString.trim();
		
		tokens = trimmed.split("\\s+");
	}
	
	/** 
	 * Gets the next string and, if it matches, 
	 * consumes ie. deletes it and returns
	 * the matched string. If none of given
	 * the strings match, throws ParseError.
	 * 
	 * @param strings Strings to be matched.
	 */
	public String consume(String... strings) {
		String next = next();
		
		// When no parameters are given, return next string.
		if (strings.length == 0) {
			return next;
		}
		
		for (String s : strings) {
			if (next.equals(s)) {
				return s;
			}
		}
		
		// Creating an error message.
		StringBuilder sb = new StringBuilder()
				.append(next).append(" is not one of: ");
		for (String s : strings) {
			sb.append('"').append(s).append('"');
		}
		
		throwError(sb.append('!').toString());
		
		// Impossible to reach.
		return null;
	}
	
	/**
	 * Consumes and returns the next integer.
	 * If that's not possible, throws a ParseException.
	 * 
	 * @throws ParseException
	 * @return Consumed integer.
	 */
	public int consumeInt() {
		String next = next();
		
		// See: consumeDouble()
		int i = 0;
		
		try {
			i = Integer.parseInt(next);
		} catch (NumberFormatException e) {
			throwError(next + " is not an integer!");
		}
		
		return i;
	}
	
	/**
	 * Consumes and returns the next double. 
	 * If that's not possible, throws a ParseException.
	 * 
	 * @throws ParseException
	 * @return Consumed double.
	 */
	public double consumeDouble() {
		String next = next();
		
		// Initialized to 0.0 to stop compile errors, but we know 
		// it's impossible to NOT initialize it without error.
		double d = 0.0;
		
		try {
			d = Double.parseDouble(next);
		} catch (NumberFormatException e) {
			throwError(next + "is not a double!");
		}
		
		return d;
	}
	
	/**
	 * Consumes and returns the next String.
	 * If that's not possible, throws a ParseException.
	 * 
	 * @throws ParseException
	 * @return Consumed String.
	 */
	public String next() {
		if (isAtEnd()) {
			throwError("There are no more tokens left to consume.");
		}
		
		return tokens[index++];
	}
	
	/**
	 * Returns false if there are no tokens to retrieve.
	 * 
	 * @return True if any tokens remain.
	 */
	public boolean isAtEnd() {
		return index >= tokens.length;
	}
	
	/**
	 * Used to stop parsing and safely return when an error occurs.
	 * 
	 * @throws ParseException
	 */
	public void throwError(String errorMessage) {
		throw new ParseException(errorMessage);
	}
}
