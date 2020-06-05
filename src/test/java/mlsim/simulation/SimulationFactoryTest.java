package mlsim.simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("For a SimulationFactory")
public class SimulationFactoryTest {
	private SimulationFactory factory;
	
	@Nested
	@DisplayName("with parameters 200, 100, 2.5")
	class WithGivenParameters {
		private Simulation simulation;
		
		@BeforeEach
		public void factorySetup() {
			factory = new SimulationFactory(200, 100, 2.5);
		}
		
		@Nested
		@DisplayName("calling newSimulation(zero-length List)")
		class CallingNewSimulation {
			
			@BeforeEach
			public void newSimulationSetup() {
				simulation = factory.newSimulation(new ArrayList<>());
			}
			
			@Test
			@DisplayName("should return the simulation with equal width.")
			public void shouldReturnSimulationWithEqualWidth() {
				assertEquals(200, simulation.getWidth());
			}
			
			@Test
			@DisplayName("should return the simulation with equal height.")
			public void shouldReturnSimulationWithEqualHeight() {
				assertEquals(100, simulation.getHeight());
			}
			
			@Disabled
			@Test
			@DisplayName("should return the simulation with the same amount of organisms (ie. be untouched).")
			public void shouldReturnSimulationWithSameAmountOfGivenOrganisms() {
				assertEquals(0, simulation.getSimulationState().agents().size());
			}
		}
		
	}
}
