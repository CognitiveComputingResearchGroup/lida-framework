/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.attention;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.Codelet;

/**
 * The interface for LIDA's AttentionCodelet.
 * Specific implementations of AttentionCodlet must implement this interface.
 * AttentionCodelet in LIDA communicates with the workspace returning coalitions
 * @author Ryan J McCall
 */
public interface AttentionCodelet extends Codelet{
	
	/**
	 * Sets the content the codelet will look for.
	 * @param content NodeStructure
	 */
	public void setSoughtContent(NodeStructure content);
	
	/**
	 * Get content codelet will look for.
	 * @return sought content
	 */
	public NodeStructure getSoughtContent();
	
	/**
	 * How this codelet determines whether its sought content is present in the csm
	 * @param strategy CheckForContentStrategy
	 */
	public void setHasSoughtContentStrategy(CheckForContentStrategy strategy);
	
	/**
	 * 
	 */
	public CheckForContentStrategy getHasSoughtContentStrategy();

}
