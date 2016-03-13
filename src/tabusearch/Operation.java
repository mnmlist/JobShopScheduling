/**
 * 
 */
package tabusearch;

/**
 * Class for an operation in the job shop scheduling problem. Each operation has
 * a certain duration, job, machine and identification.
 * 
 * @author Thiebout Dewitte
 * @version 1.0
 */
public class Operation {

	/*********************************
	 * CONSTRUCTOR
	 *********************************/

	/**
	 * Initialize a new operation with a given duration, job, machine and
	 * identification.
	 * 
	 * @param d
	 *            The time needed to process this operation.
	 * @param j
	 *            The job to which the operation belongs.
	 * @param m
	 *            The machine on which the operation should be processed.
	 * @param identification
	 *            The id of the operation.
	 */
	public Operation(int d, Job j, Machine m, int identification) {
		duration = d;
		job = j;
		machine = m;
		id = identification;
	}

	/*********************************
	 * IDENTIFICATION
	 *********************************/

	/**
	 * Variable referencing the id of an operation.
	 */
	private final int id;

	/**
	 * Get the id of an operation.
	 */
	public int getId() {
		return id;
	}

	/*********************************
	 * DURATION
	 *********************************/

	/**
	 * Variable referencing the duration during which the operation should be
	 * processed.
	 */
	private final int duration;

	/**
	 * Get the duration of an operation.
	 */
	public int getDuration() {
		return duration;
	}

	/*********************************
	 * JOB
	 *********************************/

	/**
	 * Variable referencing the job of the operation.
	 */
	private final Job job;

	/**
	 * Get the job to which the operation belongs.
	 */
	public Job getJob() {
		return job;
	}

	/*********************************
	 * MACHINE
	 *********************************/

	/**
	 * Variable referencing the machine on which the operation should be
	 * processed.
	 */
	private final Machine machine;

	/**
	 * Get the machine on which the operation should be processed.
	 */
	public Machine getMachine() {
		return machine;
	}

	/*********************************
	 * TOSTRING
	 *********************************/

	/**
	 * Get a textual representation of an operation.
	 */
	@Override
	public String toString() {
		if (getJob() != null && getMachine() != null) {
			return "Op. J" + getJob() + " M" + getMachine() + " T"
					+ getDuration();
		} else if (getId() == 0)
			return "Source";
		return "Sink";

	}

	/**
	 * Check if two operations are equal.
	 * 
	 * @param o
	 *            The operation to check.
	 * @return True if and only if the duration of both operations is the same
	 *         and their job and machine is equal.
	 */
	public boolean equals(Operation o) {
		return (getDuration() == o.getDuration())
				&& (getJob().equals(o.getJob()))
				&& (getMachine().equals(o.getMachine()));
	}

}
