package mlsim.simulation;

import mlsim.wrapper.Wrapper;

abstract class Entity {
	int getX() {
		return 0;
	}
	
	int getY() {
		return 0;
	}
}

class Food extends Entity {}


class Agent<T extends Wrapper<T>> extends Entity {
	Agent(T w) {}
	
	void move(SimulationState s) {}
	
}