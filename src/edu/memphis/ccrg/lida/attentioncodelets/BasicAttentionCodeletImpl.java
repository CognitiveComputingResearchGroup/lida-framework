/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.attentioncodelets;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * WARNING:	Renaming this class requires renaming values in
 * 	  configs/factoriesData.xml
 */
public class BasicAttentionCodeletImpl extends AttentionCodeletImpl {
	
	private static final Logger logger = Logger.getLogger(BasicAttentionCodeletImpl.class.getCanonicalName());
	
	public BasicAttentionCodeletImpl(){
		super();
	}
	
	/**
	 * Returns true if specified WorkspaceBuffer contains this codelet's sought
	 * content.
	 * 
	 * @param buffer
	 *            the WorkspaceBuffer to be checked for content
	 * @return true, if successful
	 */
	@Override
	public boolean hasSoughtContent(WorkspaceBuffer buffer) {
		NodeStructure model = (NodeStructure) buffer.getModuleContent();
		
//		System.out.println("BUFFER HAS");
//		System.out.println(model.toString());
//		
//		System.out.println("\nSEEKING");
//		System.out.println(soughtContent.toString());
		
		Collection<Node> nodes = soughtContent.getNodes();
		Collection<Link> links = soughtContent.getLinks();
		for (Node n : nodes){
			if (!model.containsNode(n)){
				return false;
			}
		}

		for (Link l : links){
			if (!model.containsLink(l)){
				return false;
			}
		}

		logger.log(Level.FINE, "Attn codelet " + this.toString() + " found sought content", LidaTaskManager.getCurrentTick());
		return true;
	}
	
	/**
	 * Returns sought content and related content from specified
	 * WorkspaceBuffer.
	 * 
	 * @param buffer
	 *            the buffer
	 * @return the workspace content
	 */
	@Override
	public NodeStructure getWorkspaceContent(WorkspaceBuffer buffer) {
		//TODO Naive implementation. Should not copy entire buffer.
		return ((NodeStructure) buffer.getModuleContent()).copy();
	}

}
