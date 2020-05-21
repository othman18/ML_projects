
public class Simulation {

	public static void main(String[] args) {
		String target = "to be or not to be";
/*		int n = 1000;
		int[] res = new int[n];
		for(int i = 0; i < n; i++) {
			Population s = new Population(target, 200);
			while (!s.foundTarget()) {
				s.generate();
				s.calcFitness();
			}
			System.out.println((float)i*100/n+"%");
			res[i] = s.getGenerations();
		}
		int sum = 0;
		for(int s : res)
			sum += s;
		System.out.println("average per generation" + sum/n);
		
//		*/

	//*		
  		Population s = new Population(target, 200);
		while (!s.foundTarget()) {
			
			s.generate();
			s.calcFitness();
			DNA bes = s.getBest();
			System.out.println("generations:" + s.getGenerations() + ", best genes: " + bes.getGenes()
					+ ", fitness: " + bes.getFitness());
		}
		System.out.println("found " + target + " after " + s.getGenerations());
//	*/
	}
}
