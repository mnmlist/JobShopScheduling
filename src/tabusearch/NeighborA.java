package tabusearch;

/**
 * Class representing a neighbor of type A. This is a neighbor from neighborhood
 * structure NA.
 * 
 * IMPORTANT NOTE: 
 * This class might create cycles while making the neighbor solutions. 
 * It should not be used yet in combination with tabu search. 
 * 
 * @author Thiebout Dewitte
 * @version 1.0
 *
 */
public class NeighborA extends Neighbor {

	/**
	 * Initialize a neighbor with a given move and a given original solution to
	 * which it neighbors.
	 * 
	 * @param m
	 * @param sOriginal
	 */
	public NeighborA(Move m, Solution sOriginal) {
		super(m, sOriginal);
	}

	/**
	 * Build the neighboring solution from the move and original solution.
	 * 
	 * If this number of operations in the inversion of the move is exactly
	 * equal to 2, the effect will be exactly the same as makeNeighborSolution()
	 * for every type of neighbor.
	 * If the number of operations in the inversion of the move is exactly
	 * equal to 3, the effect will be different.
	 */
	@Override
	protected void makeNeighborSolution() {
		if (getMove().getNumberOfOperationsInInversion() == 2) {
			super.makeNeighborSolution();
		} else {
			// move will contain 3 operations
			Operation[][] newSchedule = getOriginalSolution().cloneSchedule();
			int machineId = getMove().getInversion().getFirst().getMachine()
					.getId();
			Operation o1 = getMove().getInversion().get(0);
			Operation o2 = getMove().getInversion().get(1);
			Operation o3 = getMove().getInversion().get(2);

			// search for first position that matches o1, o2 or o3.
			int k;
			for (k = 0; k < newSchedule[machineId].length; k++) {
				if (newSchedule[machineId][k].equals(o1)
						|| newSchedule[machineId][k].equals(o2)
						|| newSchedule[machineId][k].equals(o3))
					break;
			}

			// move o1, o2 and o3 in schedule
			newSchedule[machineId][k] = o3;
			newSchedule[machineId][k + 1] = o2;
			newSchedule[machineId][k + 2] = o1;

			// create new built solution
			Solution newSolution = new Solution(new Problem(
					getOriginalSolution().getV(), getOriginalSolution().getA(),
					getOriginalSolution().getE(), getOriginalSolution()
							.getOptimalCost()), newSchedule);
			System.out.println(newSolution.printSolution());
			setNewSolution(newSolution);
		}
	}

}
