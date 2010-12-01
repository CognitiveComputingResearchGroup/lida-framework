/*******************************************************************************

 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.attention;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.CodeletImpl;
import edu.memphis.ccrg.lida.framework.tasks.ModuleUsage;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * Basic attention codelet checking CSM and if finding sought content, creates a coalition
 * and puts it in the global workspace.
 * 
 * @author Ryan J McCall
 * 
 */
public class AttentionCodeletImpl extends CodeletImpl implements AttentionCodelet {
	
	private static Logger logger = Logger.getLogger(AttentionCodeletImpl.class.getCanonicalName());
	protected NodeStructure soughtContent;
	protected WorkspaceBuffer currentSituationalModel;
	protected GlobalWorkspace globalWorkspace;
	private CheckForContentStrategy checkForContentStrategy = new BasicCheckForContentStrategy();
	private GetContentStrategy getStrategy = new BasicGetContentStrategy();
	
	/**
	 * 
	 */
	public AttentionCodeletImpl(){
		super();
	}
	
	@Override
	public void init(){
		//"constructor" for factory-created objects
		//this class inherits parameters from LidaTaskImpl.  already set when factory creates this object
//		double tr = (Double)super.getParam("threshold", .5);
	}
    
	@Override
	public void setAssociatedModule(LidaModule module, int usage) {
		switch(usage){
			case ModuleUsage.TO_READ_FROM:
				if(module instanceof WorkspaceBuffer){
					currentSituationalModel = (WorkspaceBuffer) module;
				}
				break;
				
			case ModuleUsage.TO_WRITE_TO:
				if (module instanceof GlobalWorkspace){
					globalWorkspace = (GlobalWorkspace) module;
				}
				break;
		}
	}
	
	@Override
	protected void runThisLidaTask() {
		if (hasSoughtContent(currentSituationalModel)) {
			NodeStructure csmContent = getWorkspaceContent();
			if (csmContent != null){
				globalWorkspace.addCoalition(new CoalitionImpl(csmContent, getActivation()));
				logger.log(Level.FINE, this + " adds coalition", LidaTaskManager.getActualTick());
			}
		}
	}
	
	/**
  	 * Returns true if specified WorkspaceBuffer contains the content which the codelet seeks.
  	 * @param buffer the WorkspaceBuffer to be checked for content
     */
	private boolean hasSoughtContent(WorkspaceBuffer buffer) {
		return checkForContentStrategy.hasSoughtContent(buffer, soughtContent);
	}

	//TODO make abstract and include in interface
	private NodeStructure getWorkspaceContent() {
		return getStrategy.getWorkspaceContent(currentSituationalModel, soughtContent);
	}

	/**
	 * @return the sought content
	 */
	@Override
	public NodeStructure getSoughtContent(){
		return soughtContent;
	}

	@Override
	public void setSoughtContent(NodeStructure content) {
		soughtContent = content;
	}
	
	@Override
	public String toString(){
		return "AttentionCodelet-"+ getTaskId();
	}

	@Override
	public CheckForContentStrategy getHasSoughtContentStrategy() {
		return checkForContentStrategy;
	}

	@Override
	public void setHasSoughtContentStrategy(CheckForContentStrategy strategy) {
		checkForContentStrategy = strategy;
	}

}// class