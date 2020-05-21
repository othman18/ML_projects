import java.util.Random;

public class DNA {

	private char[] genes;
	private float fitness;
	private int genesLen;
	private String targetPhrase = null;
	// ASCII values
	private int min = 32, max = 126;

	DNA(String target) {
		targetPhrase = target;
		this.genesLen = targetPhrase.length();
		genes = new char[genesLen];
		for (int i = 0; i < targetPhrase.length(); i++) {
			// generate ranged random number in range between inclusive min and max
			// inclusive
			genes[i] = (char) (getIntRandomRange(min, max));
		}
	}

	void calculateFitness() {
		int score = 0;
		for (int i = 0; i < genesLen; i++) {
			if (genes[i] == targetPhrase.charAt(i))
				score++;
		}
		this.fitness = 0.01f + (float) score / (float) genesLen;
		//improves the fitness function
		this.fitness = (float)Math.pow(this.fitness , 25);
	}

	DNA crossover(DNA partner) {
		DNA child = new DNA(partner.targetPhrase);
		int midPoint = getIntRandomRange(0, genesLen - 1);
		for (int i = 0; i < genesLen; i++) {
			if (midPoint > i)
				child.genes[i] = this.genes[i];
			else
				child.genes[i] = partner.genes[i];
		}
		child.calculateFitness();
		return child;
	}

	void mutate(double mutationRate) {
		for (int i = 0; i < genesLen; i++) {
			if (Math.random() < mutationRate) {
				genes[i] = (char) (getIntRandomRange(min, max));// newChar;
			}
		}
	}

	float getFitness() {
		return fitness;
	}

	String getGenes() {
		return new String(genes);
	}

	String getTarget() {
		return targetPhrase;
	}

	static int getIntRandomRange(int min, int max) {
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}
}
