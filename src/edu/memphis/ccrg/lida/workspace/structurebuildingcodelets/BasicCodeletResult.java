/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.HashMap;
import java.util.Map;

/**
 * Basic implementation of {@link CodeletRunResult}
 * @author ryanjmccall
 *
 */
public class BasicCodeletResult implements CodeletRunResult {
	
	private Map<String, Object> resultMap;
	
	public BasicCodeletResult(){
		resultMap = new HashMap<String, Object>();
	}

	@Override
	public Map<String, Object> getRunResults(){
		return resultMap;
	}

	@Override
	public void reportRunResults(Map<String, Object> results) {
		resultMap = results;
	}
}
