/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.tasks;

/**
 * Enumeration of the possible statuses of LidaTasks
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
	 * Task is finished and cannot be restarted
	 */
	CANCELED,
	
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
	 * Task has finished and must be reset and started again.
	 */
	TO_RESET,

	/**
	 * LidaTask status value:
	 * Task has finished and has results to process
	 * TODO: How is this one different fron FInished?
	 */
	FINISHED_WITH_RESULTS
}
