package mlsim.console;

/**
 * Exception used to immediately stop parsing and
 * 'return' an error. Extends RuntimeException for
 * the sole reason of not needing to write 'throws'
 * for every command.
 * 
 * @author bingis_khan
 *
 */
class ParseException extends RuntimeException {
	/**
	 * shuddup
	 */
	private static final long serialVersionUID = 5194835979391618765L;
	
	private final String errorMessage;

	public ParseException(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	/**
	 * Returns the error message of this ParseException.
	 * 
	 * @return The error message of this exception.
	 */
	public String errorMessage() {
		return errorMessage;
	}
}
