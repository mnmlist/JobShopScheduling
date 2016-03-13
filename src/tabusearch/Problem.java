package tabusearch;

import java.util.*;

/**
 * A class representing an instance of the JSS problem.
 * 
 * A JSS problem can be represented using the disjunctive graph model. This
 * results into G=(V,A,E) where V is the set of operations, A is a list
 * expressing the precedence relationship of the operations and E is a list
 * expressing the machine constraints.
 * 
 * @author Thiebout Dewitte
 * @version 1.0
 */
public class Problem {

	/************************************
	 * CONSTRUCTORS
	 ************************************/

	/**
	 * Initialize a JSS problem with given V, A, E and optimal solution.
	 * 
	 * @param V
	 * @param A
	 * @param E
	 * @param optSol
	 */
	public Problem(ArrayList<Operation> V, LinkedList<Operation>[] A,
			LinkedList<Operation>[] E, float optSol) {
		v = V;
		a = A;
		e = E;
		optimalCost = optSol;
	}

	/**
	 * Initialize a JSS problem of which the optimal value is not known.
	 * 
	 * @param V
	 * @param A
	 * @param E
	 */
	public Problem(ArrayList<Operation> V, LinkedList<Operation>[] A,
			LinkedList<Operation>[] E) {
		this(V, A, E, -1);
	}

	/************************************
	 * OPERATIONS
	 ************************************/

	/**
	 * Variable referencing the operations of a JSS problem.
	 */
	private final ArrayList<Operation> v;

	/**
	 * @return the v
	 */
	public ArrayList<Operation> getV() {
		return v;
	}

	/**
	 * Get the number of operations, associated with the problem.
	 */
	public int getNumberOfOperations() {
		return getV().size();
	}

	/************************************
	 * OPERATION PRECEDENCE RELATIONSHIP
	 ************************************/

	/**
	 * Variable referencing the precedences of the operations.
	 */
	private final LinkedList<Operation>[] a;

	/**
	 * @return the a
	 */
	public LinkedList<Operation>[] getA() {
		return a;
	}

	/**
	 * Get the number of jobs.
	 */
	public int getNumberOfJobs() {
		return getA().length;
	}

	/**
	 * Get the overall maximum number of operations, which are part of a job.
	 */
	public int getMaximumNumberOfOperationsOnJob() {
		int max = 0;
		for (LinkedList<Operation> l : getA()) {
			if (l.size() > max) {
				max = l.size();
			}
		}
		return max;
	}

	/**
	 * Get the successor of an operation, given an iterator and the operation
	 * itself.
	 * 
	 * Note that this method will return a null value, if there is no successor.
	 */
	public Operation getSuccessor(Iterator<Operation> it, Operation i) {
		while (it.hasNext()) {
			Operation o = it.next();
			if (o.equals(i)) {
				try {
					return it.next();
				} catch (NoSuchElementException e) {
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * Get the predecessor of an operation, given an iterator and the operation
	 * itself.
	 * 
	 * Note that this method will return a null value, if there is no
	 * predecessor.
	 */
	public Operation getPredecessor(Iterator<Operation> it, Operation i) {
		Operation p = null;
		while (it.hasNext()) {
			Operation o = it.next();
			if (o.equals(i)) {
				return p;
			}
			p = o;
		}
		return null;
	}

	/**
	 * Get the immediate successor of an operation, on the job of the given
	 * operation.
	 */
	public Operation getSJOfOperation(Operation i) {
		Iterator<Operation> it = getA()[i.getJob().getId()].iterator();
		return getSuccessor(it, i);
	}

	/**
	 * Get the immediate predecessor of an operation, on the job of the given
	 * operation.
	 */
	public Operation getPJOfOperation(Operation i) {
		try {
			Iterator<Operation> it = getA()[i.getJob().getId()].iterator();
			return getPredecessor(it, i);
		} catch (Exception e) {
			return null;
		}
	}

	/************************************
	 * MACHINE OF PROCESSING RELATIONSHIP
	 ************************************/

	/**
	 * Variable referencing the machine constraints of a JSS problem.
	 */
	private final LinkedList<Operation>[] e;

	/**
	 * @return the e
	 */
	public LinkedList<Operation>[] getE() {
		return e;
	}

	/**
	 * Get the number of machines.
	 */
	public int getNumberOfMachines() {
		return getE().length;
	}

	/**
	 * Get the overall maximum number of operations, which will be processed on
	 * a machine.
	 */
	public int getMaximumNumberOfOperationsOnMachine() {
		int max = 0;
		for (LinkedList<Operation> l : getE()) {
			if (l.size() > max) {
				max = l.size();
			}
		}
		return max;
	}

	/************************************
	 * OPTIMAL SOLUTION
	 ************************************/

	/**
	 * Variable referencing the optimal cost of a solution.
	 */
	private final float optimalCost;

	/**
	 * Get the optimal cost of a JSS problem.
	 */
	public float getOptimalCost() {
		return optimalCost;
	}

	/************************************
	 * VISUAL REPRESENTATION
	 ************************************/

	/**
	 * Get a textual representation of a problem.
	 */
	@Override
	public String toString() {
		String res = "";
		res += "\n******** V: " + getV().size() + " rows ********";
		for (Operation o : getV()) {
			res += "\n" + o.getId() + ": " + o;
		}
		res += "\n******** A: " + getA().length + " rows ********";
		for (LinkedList<Operation> l : getA()) {
			res += "\n" + l;
		}
		res += "\n******** E: " + getE().length + " rows ********";
		for (LinkedList<Operation> l : getE()) {
			res += "\n" + l;
		}
		return res;
	}

}
