package mlsim.console;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import mlsim.simulation.Entity;
import mlsim.simulation.Results;
import mlsim.simulation.Simulation;
import mlsim.simulation.SimulationFactory;
import mlsim.wrapper.GAWrapper;

/**
 * An entry point to the application. Hosts a basic 
 * read-eval loop and the program state. Uses delegation 
 * with CommandParser to change it's state.
 * 
 * @author bingis_khan
 */
public class ConsoleApp {
	private boolean exit = false;
	
	private final Scanner scanner = new Scanner(System.in);
	private final CommandParser parser;
	
	private SimulationFactory simFactory;
	private Selector selector;
	private Results<GAWrapper> lastResults;
	private List<GAWrapper> currentPopulation;
	
	// Interactive simulation.
	private Simulation activeSimulation;
	
	// Macros
	private final Map<String, List<String>> macros = new HashMap<>();
	
	// Saving and loading
	private final FileIO io = new FileIO();
	
	public static void main(String[] args) {
		CommandParser parser = new CommandParser(Commands.initialize());
		new ConsoleApp(parser).run();
	}
	
	public ConsoleApp(CommandParser parser) {
		this.parser = parser;
	}
	
	/**
	 *  Method used to start the program which contains its main loop.
	 */
	void run() {
		// Temporary macro definitions.
		parse("def adptest adp 1 # set p 30 7 5 # step");
		parse("def basic set p 100 100 2.5 # i p 1 4 60 # sel bob 50 49 1");
		parse("def sf step # full");
		parse("def ru run simulation # update");
		
		while (!exit) {
			print(">");
			
			String input = readIn();
			parse(input);
		}
		
		scanner.close();
	}
	
	/**
	 * Reads one line from System.in and returns it.
	 * 
	 * @return The line from stdin as String.
	 */
	private String readIn() {
		return scanner.nextLine();
	}
	
	/**
	 * Parses the input and executes it.
	 * 
	 * @param input Input string.
	 */
	private void parse(String input) {
		parser.parse(input, this);
	}
	
	/* Delegate interface */
	
	/**
	 * Creates a new simulation with the saved parameters and 
	 * puts the given solutions into it.
	 * (basically a convenience method so you would not 
	 *  have to get SimulationFactory every time)
	 * 
	 * @return New simulation.
	 */
	public Simulation newSimulation() {
		return simFactory.newSimulation(currentPopulation);
	}
	
	/**
	 * Adds the results of the last simulation.
	 * 
	 * @param results Results of the simulation to be saved.
	 */
	public void addResults(Results<GAWrapper> results) {
		assert !hasActiveSimulation() : "Program tried to save results during "
										+ "while a simulation was running";
		
		lastResults = results;
	}
	
	/**
	 * Returns the results of the last simulation.
	 * 
	 * @return Saved results of the last simulation.
	 */
	public Results<GAWrapper> getResults() {
		return lastResults;
	}
	
	/**
	 * Update the population, given this fitness.
	 * 
	 * @param fitness Fitness
	 */
	public void updatePopulation(List<Integer> fitness) {
		currentPopulation = selector.updatePopulation(currentPopulation, fitness);
	}
	
	/**
	 * Checks if the selector is set.
	 * 
	 * @return True if it has an active selector.
	 */
	public boolean hasSelector() {
		return selector != null;
	}
	
	/**
	 *  Sets this ConsoleApp's current selector.
	 *  
	 *  @param newSelector New Selector to replace the current one.
	 */
	public void setSelector(Selector newSelector) {
		selector = newSelector;
	}
	
	/**
	 * 	Sets a new SimulationFactory.
	 * 
	 * 	@param factory The factory containing desired parameters of the simulation.
	 */
	public void setSimulationFactory(SimulationFactory factory) {
		simFactory = factory;
	}
	
	/**
	 *  Return this SimulationFactory.
	 * 
	 * @return Saved SimulationFactory.
	 */
	public SimulationFactory getSimulationFactory() {
		return simFactory;
	}
	
	/**
	 * Sets the current population.
	 * 
	 * @param population New current population.
	 */
	public void setPopulation(List<GAWrapper> population) {
		currentPopulation = population;
		addResults(null); // Removes the results of the last simulation, to avoid accidental double updating.
	}
	
	/**
	 *  Returns true if population is initialized.
	 * 
	 */
	public boolean isPopulationInitialized() {
		return currentPopulation != null;
	}
	
	/**
	 * Exits out of the simulation loop.
	 * 
	 */
	public void exit() {
		exit = true;
	}
	
	/**
	 * Prints this string to console. (also provides a single 
	 * point through which every command's output goes.)
	 * 
	 * @param s The string to print.
	 */
	public void print(String s) {
		System.out.print(s);
	}
	
	/* Simulation control. */
	
	/**
	 * Checks if a simulation is currently running.
	 * 
	 * @return True if there is an active simulation.
	 */
	public boolean hasActiveSimulation() {
		return activeSimulation != null;
	}
	
	/**
	 * Adds a new simulation.
	 */
	public void setActiveSimulation(Simulation sim) {
		activeSimulation = sim;
	}
	
	/**
	 * Returns the active simulation.
	 * 
	 * @return Active simulation
	 */
	public Simulation getSimulation() {
		return activeSimulation;
	}
	
	/**
	 * Removes the current simulation with its (if exits) other state.
	 */
	public void removeActiveSimulation() {
		activeSimulation = null;
	}
	
	/* TODO Tracking organisms */
	
	/**
	 *  Add a solution to be tracked.
	 *  
	 *  @param sol Solution to be tracked (must exist in a list).
	 *  
	 */
	public void track(Entity sol) {
		
	}
	
	/**
	 * Returns the Set of tracked entities. Modifying the contents
	 * is, as always, undefined behavior. Probably returns the copy of the set.
	 * Probably... 
	 * 
	 * @return A (copy of?) Set with tracked entities.
	 */
	public Set<Entity> trackedEntities() {
		return null;
	}
	
	/**
	 * Untrack a solution.
	 * 
	 * @param sol Solution to be untracked.
	 */
	public void untrack(Entity sol) {
		
	}
	
	/** 
	 *  Untracks all solutions.
	 */
	public void untrackAll() {
		
	}
	
	/* Macros */
	
	/**
	 * Saves this macro command as macroName.
	 * 
	 * @param macroName Name of this macro.
	 * @param macroCommands Commands that compose this macro.
	 */
	public void addMacro(String macroName, List<String> macroCommands) {
		macros.put(macroName, macroCommands);
	}
	
	/**
	 * Runs a macro with the given name.
	 * Returns false if the macro with the
	 * given name does not exist.
	 * 
	 * If there was an error during one the parsing of one of commands,
	 * stops the execution of a macro.
	 * 
	 * @param macroName Name of the macro.
	 * @return True if macro was run, false if a macro with this name does not exist.
	 */
	public boolean runMacro(String macroName) {
		List<String> macro = macros.get(macroName);
		
		if (macro == null) {
			return false;
		}
		
		for (String command : macro) {
			parse(command);
			
			if (parser.hadError()) {
				break;
			}
		}
		
		return true;
	}
	
	/* SAVING & LOADING */
	
	public void save(String fileName) throws IOException {
		io.save(fileName, currentPopulation);
	}
	
	public void load(String fileName) throws IOException {
		setPopulation(io.load(fileName));
	}
}
