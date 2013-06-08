/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.CognitiveContentStructure;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;

/**
 * Default implementation of {@link StructureBuildingCodelet}. Checks for sought
 * content in all accessible {@link WorkspaceBuffer}s and adds all buffer
 * content to the Current Situational Model.
 * 
 * @author Ryan J. McCall
 * 
 */
public class BasicStructureBuildingCodelet extends StructureBuildingCodeletImpl {

	private static Logger logger = Logger
			.getLogger(BasicStructureBuildingCodelet.class.getCanonicalName());

	/**
	 * Default constructor
	 */
	public BasicStructureBuildingCodelet() {
	}

	@Override
	protected void runThisFrameworkTask() {
		logger.log(Level.FINEST, "SB codelet {1} being run.", new Object[] {
				TaskManager.getCurrentTick(), this });
		for (WorkspaceBuffer readableBuffer : readableBuffers.values()) {
			if (bufferContainsSoughtContent(readableBuffer)) {
				writableBuffer
						.addBufferContent((CognitiveContentStructure) retrieveWorkspaceContent(readableBuffer));
			}
		}
		logger.log(Level.FINEST, "SB codelet {1} finishes one run.",
				new Object[] { TaskManager.getCurrentTick(), this });
	}

	@Override
    public CognitiveContentStructure retrieveWorkspaceContent(WorkspaceBuffer buffer) {
		return (CognitiveContentStructure)buffer.getBufferContent(null);
	}

	@Override
	public boolean bufferContainsSoughtContent(WorkspaceBuffer buffer) {
		CognitiveContentStructure ns = (CognitiveContentStructure) buffer.getBufferContent(null);
		
		// TODO: Need a Strategy for determining sought content
		
		logger.log(Level.FINEST, "SBcodelet {1} found sought content",
				new Object[] { TaskManager.getCurrentTick(), this });
		return true;
	}

}
