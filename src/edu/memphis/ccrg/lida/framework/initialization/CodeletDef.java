/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.framework.initialization;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.framework.tasks.Codelet;

/**
 * Definition of a {@link Codelet} object
 * @see LidaXmlFactory
 * @author Javier Snaider
 *
 */
public class CodeletDef {
	private String className;
	private Map<String,String>defaultStrategies;
	private String name;
	private Map<String,Object> params;
	private char type;
	/**
	 * 
	 */
	public CodeletDef() {
		this.params = new HashMap<String,Object>();
	}
	/**
	 * @param className Class name of codelet
	 * @param defaultStrategies map of strategies
	 * @param name label for codelet
	 * @param params optional parameters
	 */
	public CodeletDef(String className, Map<String,String> defaultStrategies,
			String name, Map<String,Object> params) {
		this.className = className;
		this.defaultStrategies = defaultStrategies;
		this.name = name;
		this.params = params;
	}


	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * @return the defeaultBehaviors
	 */
	public Map<String,String> getDefaultStrategies() {
		return defaultStrategies;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the params
	 */
	public Map<String,Object> getParams() {
		return params;
	}
	/**
	 * @return the type
	 */
	public char getType() {
		return type;
	}
	/**
	 * @param className the className to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @param defaultStrategies the defeaultBehaviors to set
	 */
	public void setDefaultStrategies(Map<String,String> defaultStrategies) {
		this.defaultStrategies = defaultStrategies;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @param params the params to set
	 */
	public void setParams(Map<String,Object> params) {
		this.params = params;
	}
	/**
	 * valid types:
	 * B: Structure Building Codelets
	 * F: Feature Detectors
	 * A: Attention Codelets
	 * @param type the type to set
	 */
	public void setType(char type) {
		if(type=='B'||type=='F'||type=='A'){
			this.type = type;
		}
	}
}
