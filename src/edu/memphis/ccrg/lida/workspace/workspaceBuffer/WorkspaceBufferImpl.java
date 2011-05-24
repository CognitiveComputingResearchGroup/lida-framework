/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;

/**
 * This class implements module of WorkspaceBuffer. WorkspaceBuffer is a submodule of workspace and 
 * it contains nodeStructures. Also this class maintains activation lower bound of its nodeStructures.
 * {@link WorkspaceBuffer} implementation. Uses a single NodeStructure for the content.
 * @author Ryan J. McCall
 */
public class WorkspaceBufferImpl extends FrameworkModuleImpl implements WorkspaceBuffer{
	
	private static final Logger logger = Logger.getLogger(WorkspaceBufferImpl.class.getCanonicalName());
	
	private NodeStructure buffer;	
	
	/**
	 * Default constructor initializes nodeStructure.
	 */
	public WorkspaceBufferImpl() {
		buffer = new NodeStructureImpl();
	}
	
	@Override
	public void init() {
	}

	@Override
	public Object getModuleContent(Object... params) {
		return buffer;
	}

	@Override
	public void decayModule(long ticks){
		logger.log(Level.FINE, "Decaying buffer.", TaskManager.getCurrentTick());
		buffer.decayNodeStructure(ticks);
	}

	@Override
	public void addListener(ModuleListener listener) {
		// NA		
	}

	@Override
	public void addBufferContent(WorkspaceContent content) {
		buffer.mergeWith(content);
	}

	@Override
	public WorkspaceContent getBufferContent(Map<String, Object> params) {
		return (WorkspaceContent) buffer;
	}

}
