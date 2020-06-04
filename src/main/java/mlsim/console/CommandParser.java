package mlsim.console;

class CommandParser {
	private final Commands commands;

	/**
	 * Creates a command parser with supplied commands.
	 * 
	 * @param commands Object of type Commands which stores the commands to be used by the parser.
	 */
	CommandParser(Commands commands) {
		this.commands = commands;
	}
	
	/**
	 * Parses and (if it's valid) executes
	 * the given input. If the command cannot
	 * be parsed, prints usage info of this command.
	 * 
	 * @param input The user's input.
	 * @param environment The context in which the command will be executed.
	 */
	void parse(String input, ConsoleApp context) {
		Query userQuery = new Query(input);
		
		// Checks if query is empty.
		if (userQuery.isAtEnd()) {
			return;
		}
		
		// First string is the command name.
		String commandName = userQuery.next();
		Command command = commands.find(commandName);
		
		// If command does not exist, raise error.
		if (command == null) {
			context.print("placeholder error\n");
			return;
		}
		
		execute(command, userQuery, context);
	}
	
	/**
	 * Attempts to execute this command. If it fails, 
	 * prints usage through given context.
	 * 
	 * @param command The command to be executed.
	 * @param query User query to be parsed.
	 * @param context Context in which the command will be executed which serves as the state of the program.
	 */
	private void execute(Command command, Query query, ConsoleApp context) {
		try {
			command.execute(query, context);
		} catch (ParseException e) {
			context.print(executeError(command, e.errorMessage()));
		}
	}
	
	/**
	 * Returns a string which contains an error message
	 * which accompanies a command execution error.
	 * 
	 * @param com Command whose execution has failed.
	 * @param errorMessage An error message of this error.
	 * @return Error string tailored to the command com.
	 */
	private String executeError(Command com, String errorMessage) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("ERROR: ").append(errorMessage).append('\n')
			.append(com.usage()).append('\n');
		
		return sb.toString();
	}

}
