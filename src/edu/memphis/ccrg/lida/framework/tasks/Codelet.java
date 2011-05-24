/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.framework.tasks;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;


/**
 * A task that represents a demon-like processor.
 * @author Javier Snaider, Ryan J. McCall
 */
public interface Codelet extends FrameworkTask{


	/**
	 * @return the sought content
	 */
	NodeStructure getSoughtContent();
	
	/**
	 * @param content the content the codelet looks for.
	 */
	void setSoughtContent(NodeStructure content);

	/**
	 * Returns true if specified WorkspaceBuffer contains this codelet's sought
	 * content.
	 * 
	 * @param buffer
	 *            the WorkspaceBuffer to be checked for content
	 * @return true, if successful
	 */
	boolean bufferContainsSoughtContent(WorkspaceBuffer buffer);
	
	/**
	 * Returns sought content and related content from specified
	 * WorkspaceBuffer.
	 * 
	 * @param buffer
	 *            the buffer
	 * @return the workspace content
	 */
	NodeStructure retrieveWorkspaceContent(WorkspaceBuffer buffer);
	
	
}
