package tabusearch;

/**
 * Class representing a neighbor of type 1. This is a neighbor from neighborhood
 * structure N1.
 * 
 * @author Thiebout Dewitte
 * @version 1.0
 */
public class Neighbor1 extends Neighbor {

	/*********************************
	 * CONSTRUCTOR
	 *********************************/

	/**
	 * Initialize a neighbor with a given move and a given original solution to
	 * which it neighbors.
	 * 
	 * @param m
	 * @param sOriginal
	 */
	public Neighbor1(Move m, Solution sOriginal) {
		super(m, sOriginal);
	}

}
