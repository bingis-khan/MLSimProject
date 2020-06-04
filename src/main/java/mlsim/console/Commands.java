package mlsim.console;

import java.util.ArrayList;
import java.util.List;

/*
 * COMMAND EXAMPLES:
 * 
 *  run simulation
 *  initialize population [population number] [optional: rand max length] [optional: rand min length]
 *  setup [width] [height] [foodPerAgent]
 *  results
 *  apply (?) // Using results, creates a new population. 
 *  exit // Exits the program.
 *  
 *  step // Activates simulation stepping
 *  step [optional: amount of steps]
 *  stats // Stats returned in SimulationState
 *  finish // Finishes the simulation and saves results.
 *  exit // Exits without saving results.
 *  track [agent id] // Prints every move of the agent.
 *  				 // Consider having outside state for this.
 *  				 // AgentID visible in stats. 
 *                   // AgentIDs will probably be based on the position on 
 *                   // list returned in SimulationState, thus will not be 
 *                   // static ie. may change.
 *  
 *  // Testing/Fun
 *  echo [string]+ // Prints each given string in a new line.
 *  
 *  // Extra
 *  help [command name]
 *  usage [command name]
 * 
 */

/**
 * Class used for storing the commands used in CommandParser.
 * 
 * @author bingis_khan
 *
 */
class Commands {
	private final List<Command> commands = new ArrayList<>();
	
	/**
	 * Initializes and returns the new Commands object with those commands.
	 * Static method, because it looks nice.
	 * 
	 * @return Commands object with those commands.
	 */
	static Commands initialize() {
		return new Commands();
	}
	
	
	/**
	 * Attempts to find the command with the given name.
	 * If the command with that name cannot be found, returns null.
	 * 
	 * @param commandName Name of the command.
	 * @return The command with that name or null if command with this name does not exist.
	 */
	Command find(String commandName) {
		for (Command cmd : commands) {
			if (isOneOfNames(cmd, commandName)) {
				return cmd;
			}
		}
		
		return null;
	}
	
	/**
	 * Checks if commandName is one the names of cmd.
	 * 
	 * @param cmd A Command which will have its names checked. >:D
	 * @param commandName The supposed name of this command.
	 * @return True if commandName is the name of cmd.
	 */
	private boolean isOneOfNames(Command cmd, String commandName) {
		for (String name : cmd.names()) {
			if (name.equals(commandName)) {
				return true;
			}
		}
		
		return false;
	}
	
	Commands() {
		addCommand(new Command("echo [string]+", "Prints each given string in a new line.", this::echo, "echo", "e"));
		addCommand(new Command("exit", "Exits the application.", this::exit, "exit"));
	}
	
	/**
	 *  Adds a command to the total.
	 */
	private void addCommand(Command com) {
		commands.add(com);
	}
	
	
	/* Command Implementation Methods */
	
	private void echo(Query query, ConsoleApp context) {
		while (!query.isAtEnd()) {
			context.print(query.next() + "\n");
		}
	}
	
	private void exit(Query query, ConsoleApp context) {
		context.exit();
	}
	
	/* Debug */
	
	/**
	 *  Should only be used in tests.
	 *  Allows defining custom commands from outside.
	 */
	Commands(Command... cmds) {
		for (Command cmd : cmds) {
			addCommand(cmd);
		}
	}
}
