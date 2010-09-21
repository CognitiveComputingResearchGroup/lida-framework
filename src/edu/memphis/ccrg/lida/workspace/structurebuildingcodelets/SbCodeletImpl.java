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
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * Basic implementation of a structure-building codelet
 * @author Ryan J McCall
 *
 */
public class SbCodeletImpl extends LidaTaskImpl implements StructureBuildingCodelet{
	
	private static Logger logger=Logger.getLogger("lida.workspace.structurebuildingcodelets.SbCodeletImpl");

	/**
	 * Set of workspace buffers this codelet 'looks at'
	 */
	private Set<WorkspaceBuffer> accessibleModules = new HashSet<WorkspaceBuffer>();
	private WorkspaceBuffer csm;
	
	/**
	 * The node structure that is requisite for this codelet's action 
	 */
	private NodeStructure soughtContent = new NodeStructureImpl();
	
	/**
	 * This codelet's action
	 */
	private CodeletAction action = new BasicCodeletAction();
	
	/**
	 * This codelet's type
	 */
	private int type = 0;
	
	/**
	 * Expected results of this codelets
	 */
	private CodeletResult results = new BasicCodeletResult();
	
	public SbCodeletImpl(WorkspaceBuffer csm, WorkspaceBuffer perceptualBuffer){
		super(100);
		this.csm = csm;
		accessibleModules.add(perceptualBuffer);
	}
                            
	protected void runThisLidaTask(){	
		logger.finest("SB codelet " + this.toString() + " being run.");
		for(WorkspaceBuffer buffer: accessibleModules){
			action.performAction(buffer, csm);	
		}
		results.reportFinished();
		logger.finest("SB codelet " + this.toString() + " finishes one run.");
	}
	
	/**
	 * Clears this codelet's fields in preparation for reuse. 
	 * Idea is that the same codelet object is reconfigured at runtime
	 * after it finishes to be run as a different altogether codelet. 
	 */
	public void reset() {
		accessibleModules.clear();
		setNumberOfTicksPerRun(50);
		setActivation(0.0);
		soughtContent = new NodeStructureImpl();
		action = new BasicCodeletAction();
		type = 0;		
	}
	
	public void setSoughtContent(NodeStructure content){
		soughtContent = content;
	}
	public NodeStructure getSoughtContent(){
		return soughtContent;
	}
	
	public void setCodeletAction(CodeletAction a){
		action = a;
	}	
	public CodeletAction getCodeletAction(){
		return action;
	}
	
	public void setResults(CodeletResult r){
		results = r;
	}
	public Object getResults(){
		return results;
	}

	public void addAccessibleModule(WorkspaceBuffer module) {
		accessibleModules.add(module);		
	}
	public Set<WorkspaceBuffer> getAccessibleModules() {
		return accessibleModules;
	}

	public void setType(int t){
		type = t;
	}
	public int getType() {
		return type;
	}

	/**
	 * Returns a String represetation of this codelet
	 */
	public String toString(){
		return "SBCodelet-"+ getTaskId();
	}
	
}//class 
