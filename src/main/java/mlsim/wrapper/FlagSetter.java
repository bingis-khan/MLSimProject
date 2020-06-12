package mlsim.wrapper;

import java.util.ArrayList;
import java.util.List;

import mlsim.simulation.Entity;
import mlsim.simulation.Move;
import mlsim.simulation.SimulationState;


class FlagSetter {
	private final List<Part> parts = new ArrayList<>();
	
	static FlagSetter defaultSetter() {
		FlagSetter flg = new FlagSetter();
		
		// Flags
		flg.add(flg::closestFood);
		
		return flg;
	}
	
	private FlagSetter() {}
	
	/**
	 * Converts this SimulationState into flags for genetic algorithm.
	 * 
	 * @param self The entity whose perspective we see from.
	 * @param s Simulation state.
	 * @return SimulationState converted into flags for our GA.
	 */
	public boolean[] convert(Entity self, SimulationState s) {
		List<boolean[]> flags = new ArrayList<>();
		for (Part p : parts) {
			flags.add(p.convert(self, s));
		}
		
		return merge(flags);
	}
	
	private boolean[] merge(List<boolean[]> arrList) {
		// Determine the size of the merged array.
		int size = 0;
		for (boolean[] arr : arrList) {
			size += arr.length;
		}
		
		// Copy it into that.
		int current = 0;
		boolean[] merged = new boolean[size];
		for (boolean[] arr : arrList) {
			System.arraycopy(arr, 0, merged, current, arr.length);
			current += arr.length;
		}
		
		assert current == size : "current is supposed to point to the end of the array at the end of execution.";
		return merged;
	}
	
	private void add(Part p) {
		parts.add(p);
	}
	
	/* PARTS */
	
	private boolean[] closestFood(Entity self, SimulationState s) {
		Entity closestFood = Wrapper.smallestDistance(self, s.food());
		
		if (closestFood == null) {
			return new boolean[4];
		}
		
		Move moveFood = Wrapper.moveBalanced(self, closestFood);
		
		boolean[] flags = new boolean[4];
		
		switch (moveFood) {
			case NORTH: flags[0] = true; break;
			case SOUTH: flags[1] = true; break;
			case WEST:  flags[2] = true; break;
			case EAST:  flags[3] = true; break;
		}
		
		return flags;
	}
	
	@FunctionalInterface
	private interface Part {
		boolean[] convert(Entity self, SimulationState s);
	}
}
