package mlsim.console;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import mlsim.simulation.SimulationFactory;
import mlsim.wrapper.GAWrapper;

@Disabled
@DisplayName("Command")
public class CommandsTest {
	private final Commands commands = Commands.initialize();
	
	private ConsoleApp consoleApp;
	private Query query;
	private Command command;
	
	@Nested
	@DisplayName("'run simulation'")
	class RunSimulation {
		@BeforeEach
		public void setup() {
			consoleApp = mockConsoleApp();
			query = new Query("run simulation");
			command = findCommand(query);
		}
		
		@Test
		@DisplayName("should run a single simulation then save the result.")
		public void shouldRunSimulation_thenSaveResult() {
			consoleApp.setPopulation(mockPopulation());
			consoleApp.setSimulationFactory(mockSimulationFactory());
			
			// Should be always true.
			assertTrue(consoleApp.getResults() == null, "A mock simulation already has results saved??!");
			
			command.execute(query, consoleApp);
			
			assertTrue(consoleApp.getResults() != null);
		}
		
		@Test
		@DisplayName("should not run when population is not initialized.")
		public void shouldNotRunWhenPopulationIsNotInitialized() {
			consoleApp.setSimulationFactory(mockSimulationFactory());
			
			command.execute(query, consoleApp);
			
			assertEquals(null, consoleApp.getResults());
		}
		
		@Test
		@DisplayName("should not run when simulation factory is not set.")
		public void shouldNotRunWhenSimulationFactoryIsNotSet() {
			consoleApp.setPopulation(mockPopulation());
			
			command.execute(query, consoleApp);
			
			assertEquals(null, consoleApp.getResults());
		}
	}
	
	@Nested
	@DisplayName("'setup 200 200 3.5'")
	class Setup {
		private SimulationFactory factory;
		
		@BeforeEach
		public void setup() {
			query = new Query("setup 200 100 3.5");
			consoleApp = mockConsoleApp();
			command = findCommand(query);
		}
		
		@Nested
		@DisplayName("should set a simulation factory with this")
		class shouldSetASimulationFactoryyWithThis {
			@BeforeEach
			public void factorySetup() {
				factory = consoleApp.getSimulationFactory();
			}
			
			@Test
			@DisplayName("width.")
			public void thisWidth() {
				assertEquals(200, factory.getWidth());
			}
			
			@Test
			@DisplayName("height.")
			public void thisHeight() {
				assertEquals(100, factory.getHeight());
			}
			
			@Test
			@DisplayName("foodPerAgent.")
			public void thisFoodPerAgent() {
				assertEquals(3.5, factory.getFoodPerAgent());
			}
		}
	}
	
	@Nested
	@DisplayName("'initialize population 100 2'")
	class InitializePopulation {
		
		@BeforeEach
		public void setup() {
			query = new Query("initialize population 100");
			consoleApp = mockConsoleApp();
			command = findCommand(query);
		}
		
		@Test
		@DisplayName("should initialize a new population.")
		public void shouldInitializeANewPopulation() {
			command.execute(query, consoleApp);
			
			assertTrue(consoleApp.isPopulationInitialized());
		}
	}
	
	/**
	 * 
	 * @param q A query with the first string being the command name.
	 * @return Command with the specified name.
	 */
	private Command findCommand(Query q) {
		String name = q.next();
		
		return commands.find(name);
	}
	
	private ConsoleApp mockConsoleApp() {
		return new ConsoleApp(new CommandParser(commands));
	}
	
	private SimulationFactory mockSimulationFactory() {
		return new SimulationFactory(100, 100, 2.5);
	}
	
	private List<GAWrapper> mockPopulation() {
		return new ArrayList<GAWrapper>();
	}
}
