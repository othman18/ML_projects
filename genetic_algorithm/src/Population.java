import java.util.ArrayList;

/*
 * TODO 
 * change the function of generate so that getMaxFitness is done in O(1)
 */

public class Population {
	private DNA[] population;
	private String targetPhrase;
	private float mutationRate = 0.01f;
	private ArrayList<DNA> matingPool;
	private int generations = 0;

	public Population(String phrase, int populationNum) {
		population = new DNA[populationNum];
		targetPhrase = phrase;
		for (int i = 0; i < population.length; i++)
			population[i] = new DNA(targetPhrase);
		calcFitness();
		matingPool = new ArrayList<DNA>();
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
		DNA parentA = null, parentB = null;
		for (int i = 0; i < population.length; i++) {
			parentA = pickRandomPool();
			parentB = pickRandomPool();
			DNA child = parentA.crossover(parentB);
			child.mutate(mutationRate);
			population[i] = child;
		}
		generations++;
	}

	public boolean foundTarget() {
		for (DNA p : population) {
			if (p.getGenes().equals(targetPhrase))
				return true;
		}
		return false;
	}

	// Generate a mating pool
	public void naturalSelection() {
		matingPool.clear();
		float maxFitness = 0;
		for (int i = 0; i < population.length; i++) {
			if (population[i].getFitness() > maxFitness)
				maxFitness = population[i].getFitness();

			int n = (int) (map(population[i].getFitness(), maxFitness) * 100);

			for (int j = 0; j < n; j++)
				matingPool.add(population[i]);
		}
	}

	private DNA pickRandomPool() {
		if (matingPool.size() != 0)
			return matingPool.get(DNA.getIntRandomRange(0, matingPool.size() - 1));
		return population[DNA.getIntRandomRange(0, population.length - 1)];

	}

	@SuppressWarnings("unused")
	// TODO change the way I pick an index using monti carlo
	private int pickRandomIdx() {
		int pickedIdx = 0;
		for (int i = 1; i < population.length; i++) {
			// rMath.random returns number between 0 and 1
			double rand1 = Math.random() * population[pickedIdx].getFitness() * 100;
			double rand2 = Math.random() * population[i].getFitness() * 100;
			rand1 = rand1 != 0 ? rand1 : Math.random();
			rand2 = rand2 != 0 ? rand2 : Math.random();
			if (rand1 < rand2)
				pickedIdx = i;
		}
		return pickedIdx;
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

	/**
	 * returns the ratio between @param val and @param fitnessRange
	 **/
	private float map(float val, float fitnessRange) {
		/*
		 * //using this equation is obsolete, since newRange == 1 and oldRange is
		 * betweenRange 0 and fitnessRange float oldRange = end1 - start1; // between 0
		 * and 1 float newRange = end2 - start2; float newVal = ((val - start1) *
		 * newRange / oldRange) + start2;
		 */
		// range between 0 and fitnessRange
		return val / fitnessRange;
	}
}