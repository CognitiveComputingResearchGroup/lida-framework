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
public class StructureBuildingCodeletImpl extends CodeletImpl implements StructureBuildingCodelet{
	
	private static final Logger logger=Logger.getLogger(StructureBuildingCodeletImpl.class.getCanonicalName());

	private static Long idGenerator = 0L;
	
	private Long id;
	
	/*
	 * Set of workspace buffers this codelet 'looks at'
	 */
	private Set<WorkspaceBuffer> readableBuffers;
	
	/*
	 *  Buffer written to.
	 */
	private WorkspaceBuffer writableBuffer;
	
	/*
	 * The node structure required for this codelet's action to occur
	 */
	private NodeStructure soughtContent;
	
	/*
	 * This codelet's action
	 */
	private CodeletAction action;
	
	/*
	 * This codelet's type
	 */
	private CodeletType type;
	
	/*
	 * Expected results of this codelets
	 */
	private CodeletRunResult results;
	
	/**
	 * 
	 */
	public StructureBuildingCodeletImpl(){
		super();
		id = idGenerator++;
		readableBuffers = new HashSet<WorkspaceBuffer>();
		soughtContent = new NodeStructureImpl();
		action = new BasicCodeletAction();
	}
	
	@Override
	public void reset() {
		setTicksPerStep(1);
		setActivation(0.0);
		
		readableBuffers.clear();
		writableBuffer = null;
		soughtContent = null;
		action = null;
		type = CodeletType.ALL_TYPE;		
	}
	
	@Override
	public void setAssociatedModule(LidaModule module, int usage) {
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
	protected void runThisLidaTask(){	
		logger.finest("SB codelet " + this.toString() + " being run.");
		for(WorkspaceBuffer readableBuffer: readableBuffers){
			action.performAction(soughtContent, readableBuffer, writableBuffer);	
		}
		results.reportFinished();
		logger.finest("SB codelet " + this.toString() + " finishes one run.");
	}
	
	@Override
	public void setSoughtContent(NodeStructure content){
		soughtContent = content;
	}
	@Override
	public NodeStructure getSoughtContent(){
		return soughtContent;
	}
	
	@Override
	public void setCodeletAction(CodeletAction a){
		action = a;
	}	
	@Override
	public CodeletAction getCodeletAction(){
		return action;
	}

	@Override
	public CodeletRunResult getCodeletRunResult(){
		return results;
	}

	@Override
	public void setType(CodeletType t){
		type = t;
	}
	@Override
	public CodeletType getCodeletType() {
		return type;
	}

	@Override
	public String toString(){
		return type + " SBCodelet-"+ getTaskId();
	}

	@Override
	public Long getId() {
		return id;
	}

} 
