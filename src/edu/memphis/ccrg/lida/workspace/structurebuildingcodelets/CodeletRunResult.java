/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.Map;

/**
 * An encapsulation of the result of a run of a codelet.  
 * @author Ryan J. McCall
 *
 */
public interface CodeletRunResult {

	/**
	 * Stores the result of the codelet's current run.
	 * @param results Map of relavent data
	 */
	public void reportRunResults(Map<String, Object> results);
	
	/**
	 * Returns  the result of the codelet's current run.
	 * @return Map of relavent data
	 */
	public Map<String, Object> getRunResults();
	
}
