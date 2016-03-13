package tabusearch;

/**
 * Class representing a neighbor of a JSS instance.
 *
 * @author Thiebout Dewitte
 * @version 1.0
 */
public abstract class Neighbor {

	/*********************************
	 * CONSTRUCTOR
	 *********************************/

	/**
	 * Initialize a new neighbor with a given move and a given original
	 * solution. The new neighboring solution will automatically be initialised.
	 * 
	 * @param s
	 * @param t
	 */
	public Neighbor(Move m, Solution sOriginal) {
		move = m;
		originalSolution = sOriginal;
		makeNeighborSolution();
	}

	/*********************************
	 * MOVE
	 *********************************/

	/**
	 * Variable referencing the move of the neighboring solution.
	 */
	private final Move move;

	/**
	 * @return the move
	 */
	public Move getMove() {
		return move;
	}

	/*********************************
	 * SOLUTIONS
	 *********************************/

	/**
	 * Variable referencing the original solution.
	 */
	private final Solution originalSolution;

	/**
	 * @return the originalSolution
	 */
	public Solution getOriginalSolution() {
		return originalSolution;
	}

	/**
	 * Variable referencing the solution of the neighbor.
	 */
	private Solution newSolution = null;

	/**
	 * @return the newSolution
	 */
	public Solution getNewSolution() {
		return newSolution;
	}

	/**
	 * @param newSolution
	 *            the newSolution to set
	 */
	public void setNewSolution(Solution newSolution) {
		this.newSolution = newSolution;
	}

	/**
	 * Build the neighboring solution from the move and original solution.
	 * 
	 * This neighboring solution has exactly the same operations, job precedence
	 * relationship, machine constrains and optimal cost. The only change is the
	 * different schedule (2 operations of the inversion will be swapped), which
	 * might cause another cost of the solution and critical path.
	 */
	protected void makeNeighborSolution() {
		// initialisations
		Operation[][] newSchedule = getOriginalSolution().cloneSchedule();
		int machineId = getMove().getInversion().getFirst().getMachine()
				.getId();
		Operation o1 = getMove().getInversion().get(0);
		Operation o2 = getMove().getInversion().get(1);

		// search for position of o1 in schedule
		int k;
		for (k = 0; k < newSchedule[machineId].length; k++) {
			if (newSchedule[machineId][k].equals(o1))
				break;
		}

		// swap o1 and o2
		newSchedule[machineId][k] = o2;
		newSchedule[machineId][k + 1] = o1;

		// create new built solution
		Solution newSolution = new Solution(new Problem(getOriginalSolution()
				.getV(), getOriginalSolution().getA(), getOriginalSolution()
				.getE(), getOriginalSolution().getOptimalCost()), newSchedule);
		setNewSolution(newSolution);
	}

}
