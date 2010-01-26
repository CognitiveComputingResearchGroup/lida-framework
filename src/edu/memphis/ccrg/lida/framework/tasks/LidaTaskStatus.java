package edu.memphis.ccrg.lida.framework.tasks;

/**
 * Possible status of a LidaTask
 * @author Javier Snaider
 *
 */
public enum LidaTaskStatus {
	/**
	 * LidaTask status value:
	 * Task is scheduled to be run, but is not running yet.
	 */
	WAITING,
	
	/**
	 * LidaTask status value:
	 * Task is running
	 */
	RUNNING,
	
	/**
	 * LidaTask status value:
	 * Task is finished
	 */
	CANCELLED,
	
	/**
	 * LidaTask status value:
	 * Task is finished and has a result
	 */
	FINISHED,
	
	/**
	 * LidaTask status value:
	 * Task is not running and not finished.
	 */
	WAITING_TO_RUN,
	
	/**
	 * LidaTask status value:
	 * Task has finished and must be reseted and started again.
	 */
	TO_RESET,

	/**
	 * LidaTask status value:
	 * Task has finished and has results to process
	 */
	FINISHED_WITH_RESULTS
}
