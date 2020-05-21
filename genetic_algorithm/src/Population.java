
/*
 * TODO 
 * change the function of generate so that getMaxFitness is done in O(1)
 */

public class Population {
	private DNA[] population;
	private String targetPhrase;
	private float mutationRate = 0.01f;
	private int generations = 0;

	public Population(String phrase, int populationNum) {
		population = new DNA[populationNum];
		targetPhrase = phrase;
		for (int i = 0; i < population.length; i++)
			population[i] = new DNA(targetPhrase);
		calcFitness();
		generations = 0;
	}

	public Population(String phrase) {
		this(phrase, 200);
	}

	public void calcFitness() {
		for (int i = 0; i < population.length; i++)
			population[i].calculateFitness();
	}

	public void generate() {
		DNA[] newPopulation = new DNA[this.population.length];
		DNA parentA = null, parentB = null;
		for (int i = 0; i < population.length; i++) {
			parentA = randomAcceptReject();
			parentB = randomAcceptReject();
			DNA child = parentA.crossover(parentB);
			child.mutate(mutationRate);
			newPopulation[i] = child;
		}
		population = newPopulation;
		generations++;
	}

	public boolean foundTarget() {
		for (DNA p : population) {
			if (p.getGenes().equals(targetPhrase))
				return true;
		}
		return false;
	}

	// monti carlo accept reject method
	private DNA randomAcceptReject() {
		int counter = 0;
		float maxFitness = 0;
		for (int i = 1; i < population.length; i++) {
			if (population[i].getFitness() > maxFitness)
				maxFitness = population[i].getFitness();
		}

		while (counter < 10000) {
			int index = DNA.getIntRandomRange(0, population.length - 1);
			DNA curr = population[index];
			float prob = (float) Math.random() * maxFitness;
			if (curr.getFitness() > prob)
				return curr;
			counter++;
		}
		return null;
	}

	public DNA getBest() {
		int idx = 0;
		for (int i = 1; i < population.length; i++) {
			if (population[idx].getFitness() < population[i].getFitness())
				idx = i;
		}
		return population[idx];
	}

	public int getGenerations() {
		return generations;
	}
}