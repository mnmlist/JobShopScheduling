package tabusearch;

import java.util.LinkedList;

/**
 * Class representing a move for our JSS instance. A move is a partial
 * modification, which leads from one solution to another slightly different
 * solution.
 * 
 * @author Thiebout Dewitte
 * @version 1.0
 */
public class Move {

	/*********************************
	 * CONSTRUCTOR
	 *********************************/

	/**
	 * Initialize a new move with given operations. The given number of
	 * operations might vary.
	 * 
	 * @param operations
	 */
	public Move(Operation... operations) {
		for (Operation o : operations) {
			inversion.add(o);
		}
	}

	/*********************************
	 * MOVE
	 *********************************/

	/**
	 * Variable referencing the inversion of the move.
	 */
	private LinkedList<Operation> inversion = new LinkedList<Operation>();

	/**
	 * @return the inversion
	 */
	public LinkedList<Operation> getInversion() {
		return inversion;
	}

	/**
	 * @param inversion
	 *            the inversion to set
	 */
	public void setInversion(LinkedList<Operation> move) {
		this.inversion = move;
	}

	/**
	 * Get the number of operations in the inversion.
	 */
	public int getNumberOfOperationsInInversion() {
		return getInversion().size();
	}

	/*********************************
	 * VISUAL REPRESENTATION
	 *********************************/

	/**
	 * Get a string representation of a move.
	 */
	@Override
	public String toString() {
		String res = "Move ";
		for (Operation o : getInversion()) {
			res += o + " ";
		}
		return res;
	}

}
