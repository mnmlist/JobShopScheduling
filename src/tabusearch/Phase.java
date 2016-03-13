package tabusearch;

/**
 * Enum representing the phase of the tabu search algorithm.
 * 
 * @author Thiebout Dewitte
 * @version 1.0
 */
public enum Phase {

	/**
	 * Phases in the tabu search algorithm.
	 * 
	 * If the current objective function value is less than the best value found
	 * before, then the phase is being called an EUREKA phase.
	 * 
	 * If we are in an improving phase of the search, then the phase is being
	 * called an IMPROVING phase.
	 * 
	 * If we are not in an improving phase of the search, then the phase is
	 * being called a WORSEN phase.
	 */
	EUREKA, IMPROVING, WORSEN;

}
