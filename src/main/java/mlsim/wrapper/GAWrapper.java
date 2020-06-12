package mlsim.wrapper;

import java.util.List;

import mlsim.simulation.Entity;
import mlsim.simulation.Move;
import mlsim.simulation.SimulationState;
import mlsim.solution.GeneticAlgorithm;
import mlsim.util.Tuple;


public class GAWrapper extends Wrapper<GAWrapper> {
	public static final int PRE = 4, POST = 2;
	
	private final GeneticAlgorithm ga;
	
	public GAWrapper(GeneticAlgorithm ga) {
		assert ga.postSize() == POST; // Four movement types: NSWE
		
		this.ga = ga;
	}
	
	@Override
	public Move evaluate(Entity self, SimulationState s) {
		List<? extends Entity> food = s.food();
		
		
		Entity closestFood = smallestDistance(self, food);
		Move moveFood = moveBalanced(self, closestFood);
		
		boolean[] flags = new boolean[PRE];
		
		switch (moveFood) {
			case NORTH: flags[0] = true; break;
			case SOUTH: flags[1] = true; break;
			case WEST:  flags[2] = true; break;
			case EAST:  flags[3] = true; break;
		}
		
		int intMove = ga.evaluate(flags);
		return toMove(intMove);
	}
	
	private Move toMove(int i) {
		assert i >= 0 : "i must be greater or equal 0.";
		assert i < 4 : "i must be smaller than 4.";
		
		switch (i) {
			default: // Should not happen.
			case 0: return Move.NORTH;
			case 1: return Move.SOUTH;
			case 2: return Move.WEST;
			case 3: return Move.EAST;
		}
	}

	@Override
	public GAWrapper mutate() {
		return new GAWrapper(ga.mutate());
	}

	@Override
	public Tuple<GAWrapper, GAWrapper> crossover(GAWrapper other) {
		Tuple<GeneticAlgorithm, GeneticAlgorithm> tuple = ga.crossover(other.ga);
		return new Tuple<GAWrapper, GAWrapper>(new GAWrapper(tuple.first()), new GAWrapper(tuple.second()));
	}
	
	public String asString() {
		return ga.asString();
	}
}
