package tabusearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

/**
 * A class representation the solution of a JSS problem. A solution can be
 * interpreted as a problem with a schedule. The schedule and the problem will
 * also determine a longest path of our solution.
 * 
 * @author Thiebout Dewitte
 * @version 1.0
 */
public class Solution extends Problem {

	/*********************************
	 * CONSTRUCTORS
	 *********************************/

	/**
	 * Initialize a new solution with a given problem and given schedule.
	 */
	public Solution(Problem p, Operation[][] s) {
		super(p.getV(), p.getA(), p.getE());
		setLongestPath(new LinkedList<Operation>());
		schedule = s;
	}

	/**
	 * Initialize a new solution with a given problem and an empty schedule.
	 */
	public Solution(Problem p) {
		this(p, new Operation[p.getNumberOfMachines()][p
				.getMaximumNumberOfOperationsOnMachine()]);
	}

	/*********************************
	 * SOLUTION
	 *********************************/

	/**
	 * Variable referencing the schedule.
	 */
	private final Operation[][] schedule;

	/**
	 * @return the schedule
	 */
	public Operation[][] getSchedule() {
		return schedule;
	}

	/**
	 * Schedule an operation on the first free place, from the left. The
	 * operation will be scheduled time increasing.
	 * 
	 * @param oToBeScheduled
	 */
	public void scheduleOperationLeft(Operation oToBeScheduled) {
		int index = 0;
		int machineId = oToBeScheduled.getMachine().getId();

		boolean searchingNextFreePlace = true;
		while (searchingNextFreePlace) {
			if (getSchedule()[machineId][index] == null)
				searchingNextFreePlace = false;
			index++; // go to the right to find next free spot
		}

		int dropIndex = index - 1;
		getSchedule()[machineId][dropIndex] = oToBeScheduled;
	}

	/**
	 * Schedule an operation on the first free place, from the right. The
	 * operation will be scheduled time decreasing.
	 * 
	 * @param oToBeScheduled
	 */
	public void scheduleOperationRight(Operation oToBeScheduled) {
		int index = schedule[0].length - 1; // schedule is squared matrix, this
											// is the last position
		int machineId = oToBeScheduled.getMachine().getId();

		boolean searchingNextFreePlace = true;
		while (searchingNextFreePlace) {
			if (getSchedule()[machineId][index] == null)
				searchingNextFreePlace = false;
			index--; // go to the left to find next free spot
		}

		int dropIndex = index + 1;
		getSchedule()[machineId][dropIndex] = oToBeScheduled;
	}

	/**
	 * Clone a schedule.
	 */
	public Operation[][] cloneSchedule() {
		Operation[][] schedule = new Operation[getNumberOfMachines()][getMaximumNumberOfOperationsOnMachine()];
		for (int i = 0; i < getNumberOfMachines(); i++) {
			for (int j = 0; j < getMaximumNumberOfOperationsOnMachine(); j++) {
				schedule[i][j] = getSchedule()[i][j];
			}
		}
		return schedule;
	}

	/*********************************
	 * COST FUNCTION
	 *********************************/

	/**
	 * Variable referencing the longest path of a solution (the sum of the nodes
	 * on this path is the cost).
	 */
	private LinkedList<Operation> longestPath = new LinkedList<Operation>();

	/**
	 * @return the longestPath
	 */
	public LinkedList<Operation> getLongestPath() {
		return longestPath;
	}

	/**
	 * @param longestPath
	 *            the longestPath to set
	 */
	private void setLongestPath(LinkedList<Operation> longestPath) {
		this.longestPath = longestPath;
	}

	/**
	 * Get the cost of the solution.
	 */
	public float getCost() {
		float[][] longestPath = calculateLongestPath();
		return longestPath[longestPath.length - 1][0];
	}

	/**
	 * Get the adjacency list representation of the JSS problem.
	 */
	public HashMap<Operation, Float>[] getAdjacencyListRepresentation() {
		@SuppressWarnings("unchecked")
		HashMap<Operation, Float>[] adj = new HashMap[getNumberOfOperations()];
		for (int i = 0; i < adj.length; ++i)
			adj[i] = new HashMap<Operation, Float>();

		// fill hashmap with initial and final operations
		for (LinkedList<Operation> list : getA()) {
			adj[0].put(list.getFirst(),
					(float) list.getFirst().getDuration() / 2);
			adj[list.getLast().getId()].put(new Operation(0, null, null,
					getNumberOfOperations() - 1), (float) list.getLast()
					.getDuration() / 2);
		}

		// Add successor of each operation to the adjacency representation
		for (Operation o : getV()) {
			if (o.getMachine() != null && o.getJob() != null
					&& getSJOfOperation(o) != null) { // don't handle
														// source and
														// sink again
				adj[o.getId()].put(getSJOfOperation(o),
						(float) getSJOfOperation(o).getDuration() / 2
								+ (float) o.getDuration() / 2);
			}
		}

		// iterate through edges showing precedence on machine i
		for (int i = 0; i < getSchedule().length; i++) {
			for (int j = 0; j < getSchedule()[i].length - 1; j++) {
				if (getSchedule()[i][j + 1] != null)
					adj[getSchedule()[i][j].getId()].put(
							getSchedule()[i][j + 1],
							(float) getSchedule()[i][j].getDuration()
									/ 2
									+ (float) getSchedule()[i][j + 1]
											.getDuration() / 2);
			}
		}

		return adj;
	}

	/**
	 * Help method used by topologicalSort().
	 */
	private void topologicalSortUtil(Operation o, Boolean visited[],
			Stack<Operation> stack) {
		// Mark the current node as visited.
		visited[o.getId()] = true;
		Operation i;

		// Recur for all the vertices adjacent to this vertex
		Iterator<Operation> it = getAdjacencyListRepresentation()[o.getId()]
				.keySet().iterator();
		while (it.hasNext()) {
			i = it.next();
			if (!visited[i.getId()])
				topologicalSortUtil(i, visited, stack);
		}

		// Push current vertex to stack which stores result
		stack.push(o);
	}

	/**
	 * Topological sorting for Directed Acyclic Graph (DAG) is a linear ordering
	 * of vertices such that for every directed edge uv, vertex u comes before v
	 * in the ordering.
	 */
	private Stack<Operation> topologicalSort() {
		Stack<Operation> stack = new Stack<Operation>();

		// Mark all the vertices as not visited
		Boolean visited[] = new Boolean[getNumberOfOperations()];
		for (int i = 0; i < getNumberOfOperations(); i++)
			visited[i] = false;

		// Call the recursive helper function to store Topological
		// Sort starting from all vertices one by one
		for (int i = 0; i < getNumberOfOperations(); i++)
			if (!visited[i])
				topologicalSortUtil(getV().get(i), visited, stack);

		return stack;
	}

	/**
	 * Calculate the longest path will set the longest path, to a longest path
	 * found in the graph. It will return the value of this longest path.
	 * 
	 * @return
	 */
	private float[][] calculateLongestPath() {
		Stack<Operation> stack = topologicalSort();

		// Set all labels to "-Inf" (min value of an integer)
		Label[] label = new Label[getNumberOfOperations()];
		for (int i = 0; i < getNumberOfOperations(); i++) {
			label[i] = new Label();
			label[i].setDistance(Integer.MIN_VALUE);
		}

		label[0].setDistance(0); // begin searching the longest path from source

		// Process all operations in topological order
		Operation o = null;
		while (!stack.empty()) {
			// Get the next operation
			o = stack.peek();
			stack.pop();

			// Update labels
			if (label[o.getId()].getDistance() != Integer.MIN_VALUE) {
				HashMap<Operation, Float>[] adjacencyList = getAdjacencyListRepresentation();
				for (Operation adjOp : adjacencyList[o.getId()].keySet()) {
					if (label[adjOp.getId()].getDistance() < label[o.getId()]
							.getDistance()
							+ (float) adjOp.getDuration()
							/ 2
							+ (float) o.getDuration() / 2) {
						label[adjOp.getId()].setDistance(label[o.getId()]
								.getDistance()
								+ (float) adjOp.getDuration()
								/ 2 + (float) o.getDuration() / 2);
						label[adjOp.getId()].setOperation(o);
					}
				}
			}
		}

		// Make sure that the longest path is first empty
		setLongestPath(new LinkedList<Operation>());

		// Build the path itself using our labels (operations), starting from
		// the sink
		getLongestPath().addFirst(new Operation(0, null, null, label.length));
		Operation prev = label[label.length - 1].getOperation();
		while (prev != null) {
			getLongestPath().addFirst(prev);
			prev = label[prev.getId()].getOperation();
		}

		// Construct solution as in example Dropbox
		float[][] sol = new float[getNumberOfJobs() + 1][getMaximumNumberOfOperationsOnJob()];
		int id = 1;
		for (int row = 0; row < getNumberOfJobs(); row++) {
			for (int column = 0; column < getMaximumNumberOfOperationsOnJob(); column++) {
				float earliestStartingTime = 0;
				Operation vorige = label[id].getOperation();
				while (vorige != null) {
					earliestStartingTime += vorige.getDuration();
					vorige = label[vorige.getId()].getOperation();
				}
				sol[row][column] = earliestStartingTime;
				id++;
			}
		}

		// Add additional row with longest path length
		sol[getNumberOfJobs()][0] = label[getNumberOfOperations() - 1]
				.getDistance();

		return sol;
	}

	/**
	 * Make a clone of an ArrayList with operations.
	 */
	public static ArrayList<Operation> cloneList(ArrayList<Operation> list) {
		ArrayList<Operation> clone = new ArrayList<Operation>(list.size());
		for (Operation o : list)
			clone.add(o);
		return clone;
	}

	/*********************************
	 * VISUAL REPRESENTATION
	 *********************************/

	/**
	 * Get a string representation of the schedule of the solution.
	 */
	@Override
	public String toString() {
		String res = "****** Schedule: ******";
		for (int i = 0; i < getSchedule().length; i++) {
			res += "\n";
			for (int j = 0; j < getSchedule()[i].length; j++) {
				res += getSchedule()[i][j] + ", ";
			}
		}
		res += "\ncost: " + getCost();
		res += "\n" + getLongestPath();
		return res + "\n****** ******";
	}

	/**
	 * Get a string representation of the schedule of the solution.
	 */
	public String toStringDetailed() {
		String res = super.toString() + "\n";
		res += toString();
		return res;
	}

	/**
	 * Print the solution in a form, such that it can be verified by the given
	 * program jss.jar.
	 */
	public String printSolution() {
		String res = "";
		res += getNumberOfJobs() + " ";
		res += getNumberOfMachines() + "\n";
		float[][] sol = calculateLongestPath();

		for (int i = 0; i < sol.length - 1; i++) {
			for (int j = 0; j < sol[i].length; j++) {
				res += (Math.round(sol[i][j]) + " ");
			}
			res += "\n";
		}
		res += Math.round(sol[sol.length - 1][0]);
		return res;
	}

	/**
	 * Get the possible inversions (type N1) of a solution. In this case we
	 * consider inversions of (i,j), where i and j are successive operations
	 * processed on the same machine and they are on a longest path.
	 */
	public HashSet<Move> getPossibleInversionsN1() {
		HashSet<Move> inversions = new HashSet<Move>();
		LinkedList<Operation> longestPath = getLongestPath();

		// Iterate through critical path to find possible inversions
		Operation prev = null;
		for (Operation o : longestPath) {
			if (prev != null && prev.getMachine() != null
					& o.getMachine() != null) {
				if (prev.getMachine().getId() == o.getMachine().getId())
					inversions.add(new Move(prev, o)); // consider inversion of
														// (prev,o)
			}
			prev = o;
		}

		return inversions;
	}

	/**
	 * Get the possible inversions (type NA) of a solution. In this case we
	 * consider all permutations of {PM[i],i,j} and {i,j,SM[j]} in which arc
	 * (i,j) is inverted. This will be inversions of type N1 with some
	 * additional inversions. Note that (i,j) should be successive operations,
	 * processed on the same machine on a longest path. PM[i] and SM[j] should
	 * also be on this longest path if they are reversed as well.
	 */
	public HashSet<Move> getPossibleInversionsNA() {
		// NA is extension of N1. It will consider all inversions of N1,
		// and some additional ones.
		HashSet<Move> inversionsN1 = getPossibleInversionsN1();
		HashSet<Move> inversionsNA = new HashSet<Move>();
		LinkedList<Operation> longestPath = getLongestPath();

		for (Move mN1 : inversionsN1) {
			inversionsNA.add(mN1);

			Operation u = mN1.getInversion().get(0);
			Operation v = mN1.getInversion().get(1);
			Operation PMu = getPMOfOperation(u);
			Operation SMv = getSMOfOperation(v);
			Operation PMPMu = getPMOfOperation(PMu);
			Operation SMSMv = getSMOfOperation(SMv);

			if (PMu != null) {
				if (firstOperationImmediatelyPrecedesSecondOperationOnLongestPath(
						PMu, u, longestPath)
						&& (firstOperationImmediatelyPrecedesSecondOperationOnLongestPath(
								v, SMv, longestPath) || SMv == null)
						&& (firstOperationImmediatelyPrecedesSecondOperationOnLongestPath(
								PMPMu, PMu, longestPath) || PMPMu == null)
						&& (firstOperationImmediatelyPrecedesSecondOperationOnLongestPath(
								v, PMPMu, longestPath) || PMPMu == null)
						&& (firstOperationImmediatelyPrecedesSecondOperationOnLongestPath(
								SMv, PMu, longestPath) || SMv == null)) {
					inversionsNA.add(new Move(PMu, u, v));
				}

			}

			if (SMv != null) {
				if (firstOperationImmediatelyPrecedesSecondOperationOnLongestPath(
						v, SMv, longestPath)
						&& (firstOperationImmediatelyPrecedesSecondOperationOnLongestPath(
								PMu, u, longestPath) || PMu == null)
						&& (firstOperationImmediatelyPrecedesSecondOperationOnLongestPath(
								SMv, SMSMv, longestPath) || SMSMv == null)
						&& (firstOperationImmediatelyPrecedesSecondOperationOnLongestPath(
								v, PMu, longestPath) || PMu == null)
						&& (firstOperationImmediatelyPrecedesSecondOperationOnLongestPath(
								SMSMv, u, longestPath) || SMSMv == null)) {
					inversionsNA.add(new Move(u, v, SMv));
				}
			}

		}
		return inversionsNA;
	}

	/**
	 * Check if a given operation (uP), immediately precedes the given operation
	 * (u) on the given longest path (longestPath).
	 */
	private static boolean firstOperationImmediatelyPrecedesSecondOperationOnLongestPath(
			Operation uP, Operation u, LinkedList<Operation> longestPath) {
		try {
			int i = 0;
			for (i = 0; i < longestPath.size(); i++) {
				if (longestPath.get(i).equals(uP)) {
					break;
				}
			}
			return u.equals(longestPath.get(i + 1));
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get the immediate successor of an operation on its machine.
	 * 
	 * Please note that this method will return a null value, if the immediate
	 * predecessor does not exist.
	 */
	public Operation getPMOfOperation(Operation i) {
		try {
			Operation[] operations = getSchedule()[i.getMachine().getId()];
			Operation prev = null;
			for (int k = 0; k < operations.length; k++) {
				if (operations[k].equals(i))
					return prev;
				prev = operations[k];
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get the immediate predecessor of an operation on its machine.
	 *
	 * Please note that this method will return a null value, if the immediate
	 * successor does not exist.
	 */
	public Operation getSMOfOperation(Operation i) {
		try {
			Operation[] operations = getSchedule()[i.getMachine().getId()];
			for (int k = 0; k < operations.length; k++) {
				if (operations[k].equals(i)) {
					return operations[k + 1];
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

}
