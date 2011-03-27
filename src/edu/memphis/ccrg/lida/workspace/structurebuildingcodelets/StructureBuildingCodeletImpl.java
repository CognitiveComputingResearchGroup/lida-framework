/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.CodeletImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * Basic implementation of a structure-building codelet
 * @author Ryan J McCall
 *
 */
public abstract class StructureBuildingCodeletImpl extends CodeletImpl implements StructureBuildingCodelet{
	
	private static final Logger logger=Logger.getLogger(StructureBuildingCodeletImpl.class.getCanonicalName());
	
	/*
	 * Set of workspace buffers this codelet 'looks at'
	 */
	protected Set<WorkspaceBuffer> readableBuffers;
	
	/*
	 *  Buffer written to.
	 */
	protected WorkspaceBuffer writableBuffer;
	
	/*
	 * The node structure required for this codelet's action to occur
	 */
	protected NodeStructure soughtContent;
	
	/*
	 * Expected results of this codelets
	 */
	protected CodeletRunResult results;
	
	/**
	 * 
	 */
	public StructureBuildingCodeletImpl(){
		super();
		readableBuffers = new HashSet<WorkspaceBuffer>();
		soughtContent = new NodeStructureImpl();
		results = new BasicCodeletResult();
	}
	
	@Override
	public void reset() {
		setTicksPerStep(1);
		setActivation(0.0);
		
		readableBuffers.clear();
		writableBuffer = null;
		soughtContent = null;
	}
	
	@Override
	public void setAssociatedModule(LidaModule module, String usage) {
		if(module instanceof WorkspaceBuffer){
			if(usage == ModuleUsage.TO_READ_FROM){
				readableBuffers.add((WorkspaceBuffer) module);		
			}else if(usage == ModuleUsage.TO_WRITE_TO){
				writableBuffer = (WorkspaceBuffer) module;
			}else{
				logger.log(Level.WARNING, "Specified usage is not supported.  See ModuleUsage", LidaTaskManager.getCurrentTick());
			}
		}else{
			logger.log(Level.WARNING, "Expected module to be a WorkspaceBuffer but it was not.  Module not added.", LidaTaskManager.getCurrentTick());
		}
	}
    	
	@Override
	protected abstract void runThisLidaTask();
	
	@Override
	public CodeletRunResult getCodeletRunResult(){
		return results;
	}

	@Override
	public String toString(){
		return " SBCodelet-"+ getTaskId();
	}

} 
