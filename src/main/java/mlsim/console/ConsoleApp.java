package mlsim.console;

import java.util.List;
import java.util.Scanner;

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
	//private Population population;
	private Results<GAWrapper> lastResults;
	
	// Interactive simulation.
	private Simulation activeSimulation;
	
	public static void main(String[] args) {
		CommandParser parser = new CommandParser(Commands.initialize());
		new ConsoleApp(parser).run();
	}
	
	public ConsoleApp(CommandParser parser) {
		this.parser = parser;
	}
	
	/**
	 *  Method used to start the program which contains its main loop
	 */
	void run() {
		
		while (!exit) {
			print(">");
			
			String input = readIn();
			parser.parse(input, this);
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
		return null;
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
		
	}
	
	/**
	 *  Returns true if population is initialized.
	 * 
	 */
	public boolean isPopulationInitialized() {
		return false;
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
	
	/* Tracking organisms */
	
	/**
	 *  Add a solution to be tracked.
	 *  
	 *  @param sol Solution to be tracked (must exist in a list).
	 *  
	 */
	public void track(Object sol) {
		
	}
	
	/**
	 * Untrack a solution.
	 * 
	 * @param sol Solution to be untracked.
	 */
	public void untrack(Object sol) {
		
	}
	
	/** 
	 *  Untracks all solutions.
	 */
	public void untrackAll() {
		
	}
}
