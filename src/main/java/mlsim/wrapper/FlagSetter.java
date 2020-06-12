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
		flg.add(flg::space);
		
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
		
		boolean[] flags = new boolean[5];
		
		if (closestFood == null) {
			flags[4] = true;
		} else {
			Move moveFood = Wrapper.moveBalanced(self, closestFood);
			
			switch (moveFood) {
				case NORTH: flags[0] = true; break;
				case SOUTH: flags[1] = true; break;
				case WEST:  flags[2] = true; break;
				case EAST:  flags[3] = true; break;
			}
		}
		
		return flags;
	}
	
	private boolean[] space(Entity self, SimulationState s) {
		List<? extends Entity> agents = s.agents();
		List<? extends Entity> food = s.food();
		
		boolean[] arr = new boolean[3*4];
		
		// Should be a function.
		// ... I don't care anymore~~~
		
		// N
		if (entityExistsAt(self.getX(), self.getY() - 1, agents, self)) {
			arr[0] = true;
		} else if(entityExistsAt(self.getX(), self.getY() - 1, food, self)) {
			arr[1] = true;
		} else {
			arr[2] = true;
		}
		
		// S
		if (entityExistsAt(self.getX(), self.getY() + 1, agents, self)) {
			arr[3] = true;
		} else if(entityExistsAt(self.getX(), self.getY() + 1, food, self)) {
			arr[4] = true;
		} else {
			arr[5] = true;
		}
		
		// W
		if (entityExistsAt(self.getX() - 1, self.getY(), agents, self)) {
			arr[6] = true;
		} else if(entityExistsAt(self.getX() - 1, self.getY() - 1, food, self)) {
			arr[7] = true;
		} else {
			arr[8] = true;
		}
		
		// E
		if (entityExistsAt(self.getX() + 1, self.getY(), agents, self)) {
			arr[9] = true;
		} else if(entityExistsAt(self.getX() + 1, self.getY(), food, self)) {
			arr[10] = true;
		} else {
			arr[11] = true;
		}
		
		return arr;
	}
	
	private static boolean entityExistsAt(int x, int y, List<? extends Entity> entities, Entity self) {
		for (Entity e : entities) {
			if (e.getX() == x && e.getY() == y && self.equals(e)) {
				return true;
			}
		}
		
		return false;
	}
	
	@FunctionalInterface
	private interface Part {
		boolean[] convert(Entity self, SimulationState s);
	}
}
