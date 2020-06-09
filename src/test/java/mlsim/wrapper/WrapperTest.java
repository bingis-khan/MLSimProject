package mlsim.wrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import mlsim.simulation.Entity;
import mlsim.simulation.Move;
import mlsim.simulation.SimulationState;
import mlsim.util.Tuple;

@DisplayName("For a Wrapper")
public class WrapperTest {
	private Wrapper<?> gaw;
	
	@Nested
	@DisplayName("given a new instance")
	class GivenANewInstance {
		@BeforeEach
		public void newInstance() {
			gaw = newWrapperInstance();
		}
		
		@Nested
		@DisplayName("calling xDistance()") 
		class CallingXDistance {
			
			@Test
			@DisplayName("should return 2 with entities with coordinates (3, 7) and (1, 12).")
			public void shouldReturn2WithEntitiesX3Y7AndX1Y12() {
				assertEquals(2, gaw.xDistance(newEntity(3, 7), newEntity(1, 12)));
			}
			
			@Test
			@DisplayName("should return 3 with entities with coordinates (3, 15) and (6, 3).")
			public void shouldReturn3WithEntitiesX3Y15AndX6Y3() {
				assertEquals(3, gaw.xDistance(newEntity(3, 15), newEntity(6, 3)));
			}
		}
		
		@Nested
		@DisplayName("calling yDistance()") 
		class CallingYDistance {
			
			@Test
			@DisplayName("should return 5 with entities with coordinates (3, 7) and (1, 12).")
			public void shouldReturn5WithEntitiesX3Y7AndX1Y12() {
				assertEquals(5, gaw.yDistance(newEntity(3, 7), newEntity(1, 12)));
			}
			
			@Test
			@DisplayName("should return 12 with entities with coordinates (3, 15) and (5, 3).")
			public void shouldReturn12WithEntitiesX3Y15AndX5Y3() {
				assertEquals(12, gaw.yDistance(newEntity(3, 15), newEntity(5, 3)));
			}
		}
		
		@Nested
		@DisplayName("calling distanceOf()")
		class CallingDistanceOf {
			
			@Test
			@DisplayName("should return 7 with entities with coordinates (2, 4) and (5, 8).")
			public void shouldReturn7WithEntitiesX2Y4AndX5Y8() {
				assertEquals(7, gaw.distanceOf(newEntity(2, 4), newEntity(5, 8)));
			}
			
			@Test
			@DisplayName("should return 17 with entities with coordinates (6, 4) and (1, -8).")
			public void shouldReturn17WithEntitiesX6Y4AndX1YNegative8() {
				assertEquals(17, gaw.distanceOf(newEntity(6, 4), newEntity(1, -8)));
			}
		}
		
		@Nested
		@DisplayName("calling moveBalanced()")
		class CallingMoveBalanced {
			private Entity main, moveTo;
			
			@Test
			@DisplayName("should return SOUTH with main = e(0, 0) and moveTo = (1, 3).")
			public void shouldReturnSouthWithMainX0Y0AndMoveToX1Y3() {
				main = newEntity(0, 0);
				moveTo = newEntity(1, 3);
				
				assertEquals(Move.SOUTH, gaw.moveBalanced(main, moveTo));
			}
			
			@Test
			@DisplayName("should return WEST with main = e(6, 7) and moveTo = (2, 5).")
			public void shouldReturnWestWithMainX6Y7AndMoveToX2Y5() {
				main = newEntity(6, 7);
				moveTo = newEntity(2, 5);
				
				assertEquals(Move.WEST, gaw.moveBalanced(main, moveTo));
			}
		}
		
		@Nested
		@DisplayName("calling smallestDistance()")
		class CallingSmallestDistance {
			private Entity main;
			private List<Entity> entityList;
			
			@Test
			@DisplayName("should return e(5, 3) when given e(4, 2) and list {(12, 3), (5, 3), (3, 6)}.")
			public void shouldReturnX5Y3WhenGivenX4Y2WithListX12Y3AndX5Y3AndX3Y6() {
				Entity closest = newEntity(5, 3);
				main = newEntity(4, 2);
				entityList = List.of(newEntity(12, 3), closest, newEntity(3, 6));
				
				assertEquals(closest, gaw.smallestDistance(main, entityList));
			}
			
			@Test
			@DisplayName("should return e(5, 3) when given e(4, 2) and list {(12, 3), (5, 3), (4, 2)(itself), (3, 6)}.")
			public void shouldReturnX5Y3WhenGivenX4Y2WithListX12Y3AndX5Y3AndX4Y2AndX3Y6() {
				Entity closest = newEntity(5, 3);
				main = newEntity(4, 2);
				entityList = List.of(newEntity(12, 3), closest, main, newEntity(3, 6));
				
				assertEquals(closest, gaw.smallestDistance(main, entityList));
			}
			
			@Test
			@DisplayName("should return null when given e(4, 2) and an empty list.")
			public void shouldReturnNullWhenGivenAnEmptyList() {
				main = newEntity(4, 2);
				entityList = List.of();
				
				assertNull(gaw.smallestDistance(main, entityList));
			}
			
			@Test
			@DisplayName("should return null when given e(4, 2) and a list with only itself.")
			public void shouldReturnNullWhenGivenAListWithOnlyItself() {
				main = newEntity(4, 2);
				entityList = List.of(main);
				
				assertNull(gaw.smallestDistance(main, entityList));
			}
		}
	}
	
	private Entity newEntity(int x, int y) {
		return new Entity(x, y) {};
	}
	
	private Wrapper<?> newWrapperInstance() {
		return new MockWrapper();
	}
	
	private class MockWrapper extends Wrapper<MockWrapper> {

		@Override
		public MockWrapper mutate() {
			return new MockWrapper();
		}

		@Override
		public Tuple<MockWrapper, MockWrapper> crossover(MockWrapper other) {
			return new Tuple<MockWrapper, MockWrapper>(new MockWrapper(), new MockWrapper());
		}

		@Override
		public Move evaluate(Entity main, SimulationState s) {
			return Move.NORTH;
		}
		
	}
}
