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
package edu.memphis.ccrg.lida.framework.shared;

/**
 * @author Javier Snaider
 *
 */
public class DynamicNodeStructure extends NodeStructureImpl {

	/**
	 * 
	 */
	public DynamicNodeStructure() {
	}

	/**
	 * @param defaultNode
	 * @param defaultLink
	 */
	public DynamicNodeStructure(String defaultNode, String defaultLink) {
		super(defaultNode, defaultLink);
	}

	/**
	 * @param oldGraph
	 */
	public DynamicNodeStructure(NodeStructure oldGraph) {
		//super(oldGraph);
	}

}
