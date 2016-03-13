package tabusearch;

import java.util.Random;

/**
 * A class representing a tabu list, which is the memory structure of our tabu
 * search algorithm.
 * 
 * @author Thiebout Dewitte
 * @version 1.0
 */
public class TabuList {

	/**
	 * Initialize an empty tabu list for a given problem. The minimum and
	 * maximum length of the tabu list will be initialised automatically.
	 * 
	 * @param p
	 *            The JSS instance.
	 */
	public TabuList(Problem p) {
		matrix = new int[p.getNumberOfOperations()][p.getNumberOfOperations()];

		Random rand = new Random();
		a = 2;
		b = a + (p.getNumberOfJobs() + p.getNumberOfMachines()) / 3;
		setMin(rand.nextInt((b - a) + 1) + a);
		A = min + 6;
		B = A + (p.getNumberOfJobs() + p.getNumberOfMachines()) / 3;
		setMax(rand.nextInt((B - A) + 1) + A);
	}

	/************************************
	 * CONTENT (matrix)
	 ************************************/

	/**
	 * Variable referencing the tabu list, which can be visualised as a matrix.
	 * 
	 * Matrix[i][j] contains the count of the iteration in which the arc (i,j)
	 * has been reversed last time.
	 */
	private final int[][] matrix;

	/**
	 * @return the matrix
	 */
	public int[][] getMatrix() {
		return matrix;
	}

	/************************************
	 * MODIFICATIONS TO THE TABULIST
	 ************************************/

	/**
	 * Update the tabu list.
	 * 
	 * @param m
	 * @param iterationCount
	 * @param phase
	 */
	public void update(Move m, int iterationCount, Phase phase) {
		try {
			matrix[m.getInversion().get(0).getId()][m.getInversion().get(1)
					.getId()] = iterationCount + 1;
			if (m.getNumberOfOperationsInInversion() == 3) {
				// We always consider reversal of three arcs, so memorize all
				// arcs considered.
				matrix[m.getInversion().get(1).getId()][m.getInversion().get(2)
						.getId()] = iterationCount + 1;
				matrix[m.getInversion().get(0).getId()][m.getInversion().get(2)
						.getId()] = iterationCount + 1;
			}

			// Every Lambda iterations, randomly choose min and max.
			if (iterationCount % getLambda() == 0) {
				randomlyChooseMinAndMax();
			}

		} catch (Exception e) {
			System.out.println("Invalid move.");
		} finally { // always update the list length
			updateListLength(phase);
		}
	}

	/**
	 * Randomly choose the value min and max. Min will be chosen between a and
	 * b, max will be chosen between A and B.
	 */
	private void randomlyChooseMinAndMax() {
		Random rand = new Random();
		setMin(rand.nextInt((b - a) + 1) + a);
		setMax(rand.nextInt((B - A) + 1) + A);
	}

	/**
	 * Update the length of tabu list. The change will depend on the given phase
	 * of our tabu search.
	 * 
	 * If the current objective function value is less than the best value found
	 * before, then set the list length to 1.
	 * 
	 * If we are in an improving phase of the search and the length of the tabu
	 * list is greater than a given value min, decrease the list length by one
	 * unit.
	 * 
	 * If we are not in an improving phase ("worsen") and the length of the tabu
	 * list is smaller than a given value max, increase the list length by one
	 * unit.
	 */
	private void updateListLength(Phase phase) {
		if (phase == Phase.EUREKA) {
			setLength(1);
		} else if (phase == Phase.IMPROVING) {
			if (getLength() > getMin()) {
				setLength(getLength() - 1);
			}
		} else if (phase == Phase.WORSEN) {
			if (getLength() < getMax()) {
				setLength(getLength() + 1);
			}
		}
	}

	/**
	 * Check if a certain move is allowed during the k-th iteration of the tabu
	 * search process.
	 * 
	 * @param m
	 *            The move to check.
	 * @param k
	 *            The number of the iteration process.
	 */
	public boolean isAllowed(Move m, int k) {
		boolean res = checkTabuStatus(m.getInversion().get(0).getId(), m
				.getInversion().get(1).getId(), k + 1);
		if (m.getNumberOfOperationsInInversion() == 3) {
			res = res
					&& checkTabuStatus(m.getInversion().get(0).getId(), m
							.getInversion().get(2).getId(), k + 1)
					&& checkTabuStatus(m.getInversion().get(1).getId(), m
							.getInversion().get(2).getId(), k + 1);
		}
		return res;
	}

	/**
	 * Check if the reversal of arc (i,j) is forbidden, during the k-th
	 * iteration.
	 * 
	 * The reversal is forbidden if the value of the current operation is lower
	 * than the sum of k and the length of the tabu list.
	 */
	private boolean checkTabuStatus(int i, int j, int k) {
		return (getMatrix()[j][i] + getLength()) <= k;
	}

	/************************************
	 * CONSTANTS
	 ************************************/

	/**
	 * The length of the tabu list is the number of iterations a move maintains
	 * a tabu status.
	 * 
	 * This value will change according to the following rules: If cost(current)
	 * < cost(best solution) then new.length == 1. Else if cost(current) <
	 * cost(solution at previous iteration) && length > min then new.length ==
	 * (length-1). Else if cost(current) >= cost(solution at previous iteration)
	 * && length < max then new.length == (length+1).
	 */
	private static int length = 1;

	/**
	 * @return the length
	 */
	public static int getLength() {
		return length;
	}

	/**
	 * @param length
	 *            the length to set
	 */
	public static void setLength(int length) {
		TabuList.length = length;
	}

	/**
	 * Variable referencing the treshold max. If we are not in an improving
	 * phase the length of the list must be less than a given max to increase
	 * the list length by one unit.
	 */
	private static int max;

	/**
	 * @return the max
	 */
	public static int getMax() {
		return max;
	}

	/**
	 * @param max
	 *            the max to set
	 */
	public static void setMax(int max) {
		TabuList.max = max;
	}

	/**
	 * Variable referencing the treshold min. If we are in an improving phase
	 * the length of the list must be greater than a given min to decrease the
	 * list length by one unit.
	 */
	private static int min;

	/**
	 * @return the min
	 */
	public static int getMin() {
		return min;
	}

	/**
	 * @param min
	 *            the min to set
	 */
	public static void setMin(int min) {
		TabuList.min = min;
	}

	/**
	 * Variable referencing the value of lambda. Every lambda iterations, we
	 * randomly choose min and max.
	 */
	private final int lambda = 60;

	/**
	 * @return the lambda
	 */
	public int getLambda() {
		return lambda;
	}

	/**
	 * Variable referencing the lower bound for min.
	 */
	private final int a;

	/**
	 * Variable referencing the upper bound for min.
	 */
	private final int b;

	/**
	 * Variable referencing the lower bound for max.
	 */
	private final int A;

	/**
	 * Variable referencing the upper bound for max.
	 */
	private final int B;

	/************************************
	 * VISUAL REPRESENTATION
	 ************************************/

	/**
	 * Get a String representation of the tabu list.
	 */
	@Override
	public String toString() {
		int[][] m = getMatrix();
		String res = "";
		for (int i = 0; i < m.length; i++) {
			res += i + ": |";
			for (int j = 0; j < m.length; j++) {
				res += (matrix[i][j] + " ");
			}
			res += ("|\n");
		}
		return res;
	}

}
