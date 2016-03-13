package tabusearch;

/**
 * Class representing a machine a JSS instance.
 * 
 * @author Thiebout Dewitte
 * @version 1.0
 */
public class Machine {

	/*********************************
	 * CONSTRUCTORS
	 *********************************/

	/**
	 * Initialize a machine with a given id.
	 * 
	 * @param identification
	 */
	public Machine(int identification) {
		id = identification;
	}

	/*********************************
	 * IDENTIFICATION
	 *********************************/

	/**
	 * Variable referencing the id of a machine.
	 */
	private final int id;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/*********************************
	 * ADDITIONAL METHODS
	 *********************************/

	/**
	 * Get a textual representation of a machine.
	 */
	@Override
	public String toString() {
		return "" + getId();
	}

	/**
	 * Check if the given machine is equal to this machine.
	 * 
	 * @param m
	 *            The machine to check.
	 * @return True if and only if the id's of both machines are equal.
	 */
	public boolean equals(Machine m) {
		return m.getId() == getId();
	}
}
