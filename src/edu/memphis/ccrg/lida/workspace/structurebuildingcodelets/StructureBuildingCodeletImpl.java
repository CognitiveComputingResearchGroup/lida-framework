/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
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

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.tasks.CodeletImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
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
	 * Expected results of this codelets
	 */
	protected CodeletRunResult runResults;
	
	/**
	 * Default Constructor
	 */
	public StructureBuildingCodeletImpl(){
		super();
		readableBuffers = new HashSet<WorkspaceBuffer>();
		runResults = new BasicCodeletResult();
	}
	
	@Override
	public void reset() {
		setTicksPerStep(1);
		setActivation(0.0);
		
		readableBuffers.clear();
		writableBuffer = null;
		super.soughtContent = null;
	}
	
	@Override
	public void setAssociatedModule(FrameworkModule module, String usage) {
		if(module instanceof WorkspaceBuffer){
			if(usage == ModuleUsage.TO_READ_FROM){
				readableBuffers.add((WorkspaceBuffer) module);		
			}else if(usage == ModuleUsage.TO_WRITE_TO){
				writableBuffer = (WorkspaceBuffer) module;
			}else{
				logger.log(Level.WARNING, "Specified usage is not supported.  See ModuleUsage", TaskManager.getCurrentTick());
			}
		}else{
			logger.log(Level.WARNING, "Expected module to be a WorkspaceBuffer but it was not.  Module not added.", TaskManager.getCurrentTick());
		}
	}
    	
	@Override
	protected abstract void runThisFrameworkTask();
	
	@Override
	public CodeletRunResult getCodeletRunResult(){
		return runResults;
	}

	@Override
	public String toString(){
		return " SBCodelet-"+ getTaskId();
	}

} 
