package mlsim;

import java.util.List;
import java.util.Map;


class Simulation<Genotype extends Wrapper<Genotype>> {
	
	Simulation(SimulationSetup setup, List<Genotype> genotypes) {}
		
	boolean ended() {
		return true;
	}
	
	void step() {}
	
	SimulationState getSimulationState() {
		return null;
	}
	
	Results<Genotype> getResults() {
		assert ended() : "Cannot get results of this simulation before it has ended!";
		return null;
	}
}

class SimulationSetup {
	SimulationSetup(int width, int height, double foodPerAgent) {}
	
	<Genotype extends Wrapper<Genotype>> Simulation<Genotype>  startSimulation(List<Genotype> genotypes) {
		return new Simulation<Genotype>(this, genotypes);
	}
}

class Results<Genotype> {
	Map<Genotype, Integer> scoredGenotypes() {
		return null;
	}
}

class SimulationState {
	List<Entity> agents() {
		return null;
	}
	
	List<Entity> food() {
		return null;
	}
}