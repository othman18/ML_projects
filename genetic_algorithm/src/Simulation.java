
public class Simulation {

	public static void main(String[] args) {
		String target = "to be or not to be";
		Population s = new Population(target, 200);
		while (!s.foundTarget()) {
			s.naturalSelection();
			s.generate();
			s.calcFitness();
			DNA bes = s.getBest();
			System.out.println("generations:" + s.getGenerations() + ", best genes: " + bes.getGenes()
					+ ", fitness: " + bes.getFitness());
		}
		System.out.println("found " + target + " after " + s.getGenerations());
	}
}
