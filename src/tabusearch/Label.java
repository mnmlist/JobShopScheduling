package tabusearch;

/**
 * Class representing a label in our longest path algorithm. Each label consists
 * of a distance and an operation.
 * 
 * @author Thiebout Dewitte
 * @version 1.0
 */
public class Label {

	/*********************************
	 * CONSTRUCTOR
	 *********************************/

	/**
	 * Initialize a label with a given distance and operation.
	 * 
	 * @param dist
	 * @param o
	 */
	public Label(float dist, Operation o) {
		setDistance(dist);
		setOperation(o);
	}

	/**
	 * Initialize an empty label.
	 * 
	 * This is a label with 0 distance and without operation.
	 */
	public Label() {
		this(0, null);
	}

	/*********************************
	 * DISTANCE
	 *********************************/

	/**
	 * Variable referencing the current distance of an operation.
	 */
	private float distance;

	/**
	 * @return the distance
	 */
	public float getDistance() {
		return distance;
	}

	/**
	 * @param distance
	 *            the distance to set
	 */
	public void setDistance(float distance) {
		this.distance = distance;
	}

	/*********************************
	 * OPERATION
	 *********************************/

	/**
	 * Variable referencing the operation, from which we last improved the
	 * distance.
	 */
	private Operation operation;

	/**
	 * @return the operation
	 */
	public Operation getOperation() {
		return operation;
	}

	/**
	 * @param operation
	 *            the operation to set
	 */
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
}
