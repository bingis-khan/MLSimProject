package mlsim.console;

class Command {
	private final String[] names;
	private final String usage, description;
	private final CommandImplementation impl;
	
	/**
	 *  Serves as a 'container' of sorts to store the command
	 *  and its details.
	 * 
	 * @param usage A string that should tell the user the command's usage.
	 * @param impl An implementation of the command. Ideally, it should not depend on any outside context.
	 * @param names Different names a command can have (ex. 'run' and 'r').
	 */
	Command(String usage, String description, CommandImplementation impl, 
					String... names) {
		this.names = names;
		this.usage = usage;
		this.description = description;
		this.impl = impl;
	}
	
	/**
	 *  Executes this command.
	 * 	
	 * 	@param query Query for this command.
	 * 	@param context ConsoleApp context of this command.
	 */
	void execute(Query query, ConsoleApp context) {
		impl.execute(query, context);
	}
	
	/**
	 * Returns this command's usage.
	 * 
	 * @return String representing this commands usage.
	 */
	String usage() {
		return usage;
	}
	
	/**
	 * Returns this command's use description.
	 * 
	 * @return String with this command's description.
	 */
	String description() {
		return description;
	}
	
	/**
	 * Returns an array with this command's different names.
	 * 
	 * @return A String array which stores this command's different names.
	 */
	String[] names() {
		return names;
	}

}

@FunctionalInterface
interface CommandImplementation {
	public void execute(Query userQuery, ConsoleApp context);
}