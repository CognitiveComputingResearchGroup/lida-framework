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
public abstract class AttentionCodeletImpl extends CodeletImpl implements AttentionCodelet {
	
	private static Logger logger = Logger.getLogger(AttentionCodeletImpl.class.getCanonicalName());
	protected WorkspaceBuffer currentSituationalModel;
	protected GlobalWorkspace globalWorkspace;
	
	protected NodeStructure soughtContent;
	
	@Override
	public void init(){
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
			NodeStructure csmContent = getWorkspaceContent(currentSituationalModel);
			if (csmContent.getLinkableCount() > 0){
				globalWorkspace.addCoalition(new CoalitionImpl(csmContent, getActivation()));
				logger.log(Level.FINE, this + " adds coalition", LidaTaskManager.getActualTick());
			}
		}
	}
	
	@Override
	public abstract boolean hasSoughtContent(WorkspaceBuffer buffer);

	@Override
	public abstract NodeStructure getWorkspaceContent(WorkspaceBuffer buffer); 

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

}