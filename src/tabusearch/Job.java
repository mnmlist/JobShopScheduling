package tabusearch;

/**
 * Class representing a job in a JSS instance.
 * 
 * @author Thiebout Dewitte
 * @version 1.0
 */
public class Job {

	/*********************************
	 * CONSTRUCTOR
	 *********************************/

	/**
	 * Initialize a job with a given id.
	 * 
	 * @param identification
	 */
	public Job(int identification) {
		id = identification;
	}

	/*********************************
	 * IDENTIFICATION
	 *********************************/

	/**
	 * Variable referencing the id of a job.
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
	 * Get a textual representation of a job.
	 */
	@Override
	public String toString() {
		return "" + getId();
	}

	/**
	 * Check if the given job is equal to this job.
	 * 
	 * @param j
	 *            The job to check.
	 * @return True if and only if the id's of both jobs are equal.
	 */
	public boolean equals(Job j) {
		return j.getId() == getId();

	}
}
