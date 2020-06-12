package mlsim.console;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mlsim.gui.Gui;
import mlsim.simulation.Entity;
import mlsim.simulation.Results;
import mlsim.simulation.Simulation;
import mlsim.simulation.SimulationFactory;
import mlsim.solution.GARandomFactory;
import mlsim.solution.GeneticAlgorithm;
import mlsim.wrapper.GAWrapper;

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
		addCommand(new Command("run simulation", "Runs a single simulation if simulation factory was set.", this::runSim, "run", "r"));
		addCommand(new Command("set parameters", "Sets simulation parameters for new simulations.", this::setSimulationParameters, "set", "se"));
		addCommand(new Command("initialize population [minSize] [maxSize] [populationSize]", "Initializes a random population of size populationSize with GAs with size between minSize and maxSize.", this::initializePopulation, "initialize", "init", "i"));
		addCommand(new Command("results", "Prints the results.", this::printResults, "results", "res", "r"));
		addCommand(new Command("update", "Updates the population using the current selector.", this::updatePopulation, "update", "upd"));
		addCommand(new Command("selector [bob|???] (selector parameters)*", "Sets the selector to the current one.", this::setSelector, "selector", "sel"));
		addCommand(new Command("batch num-rounds sims-per-round","Batch training.", this::batch, "batch"));
		addCommand(new Command("save file-name", "Saves the current population with this file-name.", this::save, "save"));
		addCommand(new Command("load file-name", "Loads the population with this file-name.", this::load, "load"));
		
		addCommand(new Command("step", "Steps through the simulation.", this::step, "step", "s"));
		addCommand(new Command("full", "XXXD.", this::full, "full"));
		addCommand(new Command("end", "XXXD.", this::end, "end"));
		addCommand(new Command("finish", "Finish the simulation and save the results. ", this::finish, "finish", "fin"));
		addCommand(new Command("adp", "XXXD.", this::addPerfect, "adp"));
		addCommand(new Command("gui", "Turn on the gui.", this::gui, "gui"));
		addCommand(new Command("steps", "Prints the number of steps of this simulation.", this::steps, "steps"));
		
		addCommand(new Command("define macro macro_name ([string]+ #)+", "Defines a single macro.", this::defineMacro, "define", "def"));
		addCommand(new Command("macro macro_name", "Runs a macro with this name.", this::runMacro, "macro", "m"));
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
	
	private void runSim(Query query, ConsoleApp context) {
		query.consume("simulation", "sim", "s");
		
		if (context.hasActiveSimulation()) {
			query.throwError("Cannot run a simulation while an active one is running.");
		}
		
		if (!context.isPopulationInitialized()) {
			query.throwError("Population not initialized.");
		}
		
		if (context.getSimulationFactory() == null) {
			query.throwError("Simulation parameters not set.");
		}
		
		Simulation sim = context.newSimulation();
		Results<GAWrapper> results = sim.finish();
		context.addResults(results);
	}
	
	private void updatePopulation(Query query, ConsoleApp context) {
		if (context.hasActiveSimulation()) {
			query.throwError("Cannot set a new population while a simulation is running.");
		}
		
		if (!context.hasSelector()) {
			query.throwError("There is no selector to update the population.");
		}
		
		Results<GAWrapper> results = context.getResults();
		
		context.updatePopulation(results.fitness());
	}
	
	 private void initializePopulation(Query query, ConsoleApp context) {
		query.consume("population", "pop", "p");
		
		int minSize = query.consumeInt();
		int maxSize = query.consumeInt();
		
		if (minSize < 1) query.throwError("Min size must be greater than 0.");
		if (maxSize < minSize) query.throwError("Max size must be greater than or equal to min size.");
		
		int populationSize = query.consumeInt();
		
		if (populationSize < 1) query.throwError("Population size must be greater than 0.");
		
		context.setPopulation(GARandomFactory.generatePopulation(minSize, maxSize, populationSize, 4, 2));
	}
	
	private void setSimulationParameters(Query query, ConsoleApp context) {
		query.consume("parameters", "params", "p");
		
		int width = query.consumeInt();
		int height = query.consumeInt();
		double foodPerAgent = query.consumeDouble();
		
		SimulationFactory simFactory = new SimulationFactory(width, height, foodPerAgent);
		context.setSimulationFactory(simFactory);
	}
	
	private void printResults(Query query, ConsoleApp context) {
		if (context.getResults() == null) query.throwError("No results to display.");
		
		List<Integer> fitness = context.getResults().fitness(); 
		
		context.print("id| steps");
		for (int i = 0; i < fitness.size(); i++) {
			context.print(i + "| " + fitness.get(i) + "\n");
		}
	}
	
	private void setSelector(Query query, ConsoleApp context) {
		if (context.hasActiveSimulation()) {
			query.throwError("Cannot set a new selector when there is a simulation running.");
		}
		
		query.consume("bob");
		
		int sel = query.consumeInt(),
			cro = query.consumeInt(),
			mut = query.consumeInt();
		
		Selector selector = new Selector(sel, cro, mut);
		
		context.setSelector(selector);
	}
	
	private static GAWrapper perfect() {
		final int pre = 4, post = 2, rule = pre + post;
		boolean[] perfectArr = new boolean[] {
			true, false, false, false, false, false,
			false, true, false, false, false, true,
			false, false, true, false, true, false,
			false, false, false, true, true, true
		};
		
		return new GAWrapper(new GeneticAlgorithm(perfectArr, rule, pre));
	}
	
	private void addPerfect(Query query, ConsoleApp context) {
		int amount = query.consumeInt();
		List<GAWrapper> gas = new ArrayList<GAWrapper>();
		for (int i = 0; i < amount; i++) {
			GAWrapper gaw = perfect();
			assert gaw != null;
			gas.add(gaw);
		}
		
		context.setPopulation(gas);
	}
	
	/* Stepping through simulations. */
	
	private void step(Query query, ConsoleApp context) {
		if (!context.hasActiveSimulation()) {
			Simulation sim = context.newSimulation();
			context.setActiveSimulation(sim);
		} else {
			Simulation sim = context.getSimulation();
			if (query.isAtEnd()) {
				sim.step();
			} else {
				int steps = query.consumeInt();
				for (int i = 0; i < steps; i++) {
					sim.step();
				}
			}
		}
	}
	
	private void end(Query query, ConsoleApp context) {
		context.removeActiveSimulation();
	}
	
	private static boolean existsAt(List<? extends Entity> entities, int x, int y) {
		for (Entity e : entities) {
			if (x == e.getX() && y == e.getY())
				return true;
		}
		
		return false;
	}
	
	private void full(Query query, ConsoleApp context) {
		if (!context.hasActiveSimulation()) {
			query.throwError("No active simulation.");
		}
		
		StringBuilder builder = new StringBuilder();
		Simulation sim = context.getSimulation();
		final int width = sim.getWidth();
		final int height = sim.getHeight();
		final List<? extends Entity> agents = sim.getSimulationState().agents();
		final List<? extends Entity> food = sim.getSimulationState().food();
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (existsAt(agents, x, y)) {
					builder.append('A');
				} else if (existsAt(food, x, y)) {
					builder.append('F');
				} else {
					builder.append(' ');
				}
			}
			
			builder.append('\n');
		}
		
		context.print(builder.toString());
	}
	
	/* TODO */
	private void inspect(Query query, ConsoleApp context) {
		if (!context.hasActiveSimulation()) {
			query.throwError("Must have an active simulation to inspect an organism.");
		}
		
		int agentIndex = query.consumeInt();
		List<? extends Entity> agents = context.getSimulation().getSimulationState().agents();
		
		if (agentIndex < 0) {
			query.throwError("Agent index must be greater or equal to 0..");
		}
		
		if (agentIndex >= agents.size()) {
			query.throwError("Agent index too high. Number of agents: " + agents.size());
		}
		
		//Agent<GAWrapper> agent = (Agent<GAWrapper>)agents.get(agentIndex);
	}
	
	private void area(Query query, ConsoleApp context) {
		if (!context.hasActiveSimulation()) {
			query.throwError("Must have an active simulation.");
		}
	}
	
	/* ??? */
	
	private void defineMacro(Query query, ConsoleApp context) {
		String macroName = query.next();
		
		List<String> macroCommands = new ArrayList<>();
		
		StringBuilder command = new StringBuilder();
		while (!query.isAtEnd()) {
			String next = query.next();
			
			if (next.equals("#")) {
				macroCommands.add(command.toString());
				command = new StringBuilder();
			} else {
				command.append(next).append(" ");
			}
			
		}
		macroCommands.add(command.toString());
		
		context.addMacro(macroName, macroCommands);
	}
	
	private void runMacro(Query query, ConsoleApp context) {
		String macroName = query.next();
		
		if (!context.runMacro(macroName)) {
			query.throwError("Macro " + macroName + " does not exist.");
		}
	}
	
	
	private void save(Query query, ConsoleApp context) {
		if (context.hasActiveSimulation()) {
			query.throwError("Cannot save while an there is an active simulation.");
		}
		
		String fileName = query.next();
		
		try {
			context.save(fileName);
		} catch (IOException e) {
			query.throwError("There was an error saving this file: " + e.getMessage());
		}
	}
	
	private void load(Query query, ConsoleApp context) {
		if (context.hasActiveSimulation()) {
			query.throwError("Cannot load while an there is an active simulation.");
		}
		
		String fileName = query.next();
		
		try {
			context.load(fileName);
		} catch (IOException e) {
			query.throwError("There was an error loading this file: " + e.getMessage());
		}
	}
	
	private void batch(Query query, ConsoleApp context) {
		
		if (context.hasActiveSimulation()) {
			query.throwError("Cannot batch train when there is an active simulation running.");
		}
		
		if (!context.hasSelector()) {
			query.throwError("No selector.");
		}
		
		if (!context.isPopulationInitialized()) {
			query.throwError("Population is not initialized.");
		}
		
		if (context.getSimulationFactory() == null) {
			query.throwError("Simulation parameters not set.");
		}
		
		final int trainingRounds = query.consumeInt();
		final int simulationsPerRound = query.consumeInt();
		
		if (trainingRounds <= 0) {
			query.throwError("Number of rounds must be greater than 0.");
		}
		
		if (simulationsPerRound <= 0) {
			query.throwError("Number of simulations per round must be greater than 0.");
		}
		
		
		for (int round = 0; round < trainingRounds; round++) {
			context.print("ROUND [" + (round+1) + "]: Running... ");
			List<Integer> fitness = round(context, simulationsPerRound);
			context.print("Completed!\n");
			
			context.updatePopulation(fitness);
		}
	}
	
	private List<Integer> round(ConsoleApp context, final int simulationsPerRound) {
		// Oh no, it all looks retarded.
		List<Integer> fitness = null;
		for (int i = 0; i < simulationsPerRound; i++) {
			List<Integer> currentFitness = context.newSimulation().finish().fitness();
			if (fitness == null) {
				fitness = currentFitness;
			} else {
				addTo(fitness, currentFitness);
			}
		}
		
		divideAll(fitness, simulationsPerRound);
		int max = Collections.max(fitness);
		double avg = calculateAverage(fitness);
		
		context.print("Max:" + max + " | Avg: " + avg + " ");
		
		return fitness;
	}
	
	private static double calculateAverage(List<Integer> list) {
		int sum = 0;
		for (int i : list) 
			sum += i;
		
		return sum / (double)list.size();
	}
	
	private static void addTo(List<Integer> to, List<Integer> from) {
		assert to.size() == from.size() : "To size must be equal to from size.";
		
		for (int i = 0; i < to.size(); i++) {
			int added = to.get(i) + from.get(i);
			to.set(i, added);
		}
	}
	
	private static void divideAll(List<Integer> list, final int divisor) {
		for (int i = 0; i < list.size(); i++) {
			int divided = list.get(i) / divisor;
			
			assert divided > 0;
			list.set(i, divided);
		}
	}
	
	private void finish(Query query, ConsoleApp context) {
		if (!context.hasActiveSimulation()) {
			query.throwError("No active simulation to 'finish'.");
		}
		
		Results<GAWrapper> results = context.getSimulation().finish();
		context.addResults(results);
		context.removeActiveSimulation();
	}
	
	private void gui(Query query, ConsoleApp context) {
		if (!context.hasActiveSimulation()) {
			query.throwError("Must have an active simulation to use gui.");
		}
		
		Gui gui = new Gui("Bap", 800, 600, context.getSimulation());
		gui.run();
	}
	
	private void steps(Query query, ConsoleApp context) {
		if (!context.hasActiveSimulation()) {
			query.throwError("Must have an active simulation.");
		}
		
		context.print(context.getSimulation().getSteps() + "\n");
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
