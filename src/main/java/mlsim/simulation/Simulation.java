package mlsim.simulation;

import java.util.List;
import java.util.Map;

import mlsim.wrapper.GAWrapper;
import mlsim.wrapper.Wrapper;


public class Simulation {
	
	Simulation(int width, int height, int foodPerAgent, List<GAWrapper> genotypes) {}
		
	public boolean ended() {
		return true;
	}
	
	public void step() {}
	
	public SimulationState getSimulationState() {
		return null;
	}
	
	public Results<GAWrapper> getResults() {
		assert ended() : "Program attempted to retrieve results of the simulation before it has ended.";
		return null;
	}
	
	public int getWidth() {
		return 0;
	}
	
	public int getHeight() {
		return 0;
	}
}

class Results<Genotype> {
	Map<Genotype, Integer> scoredGenotypes() {
		return null;
	}
}