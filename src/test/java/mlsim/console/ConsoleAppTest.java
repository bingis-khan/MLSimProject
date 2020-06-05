package mlsim.console;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import mlsim.simulation.Simulation;
import mlsim.simulation.SimulationFactory;
import mlsim.wrapper.GAWrapper;

@DisplayName("For a ConsoleApp")
public class ConsoleAppTest {
	private ConsoleApp consoleApp;
	
	private final Simulation mockSimulation;
	
	{
		// List with a null in case the new simulation cannot take a null.
		List<GAWrapper> agents = new ArrayList<>();
		agents.add(null);
		mockSimulation = new SimulationFactory(100, 100, 2.5).newSimulation(agents);
	}
	
	@Nested
	@DisplayName("after calling setActiveSimulation() with an actual simulation")
	class WhenAnActiveSimulationIsSet {
		@BeforeEach
		public void setActiveSimulationCall() {
			consoleApp = new ConsoleApp(null);
			consoleApp.setActiveSimulation(mockSimulation);
		}
		
		@Test
		@DisplayName("calling hasActiveSimulation() should return true.")
		public void callingHasActiveSimulation_shouldReturnTrue() {
			assertTrue(consoleApp.hasActiveSimulation());
		}
	}
	
	@Nested
	@DisplayName("after calling removeSimulation()")
	class AfterCallingSetActiveSimulationWithNull {
		@BeforeEach
		public void setup() {
			consoleApp = new ConsoleApp(null);
			consoleApp.removeActiveSimulation();
		}
		
		@Test
		@DisplayName("hasActiveSimulation() should return false.")
		public void hasActiveSimulationShouldReturnFalse() {
			assertFalse(consoleApp.hasActiveSimulation());
		}
	}
}
