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
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.CodeletImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
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
	
	private static final Logger logger = Logger.getLogger(AttentionCodeletImpl.class.getCanonicalName());
	
	/**
	 * Where codelet will look for and retrieve content from
	 */
	protected WorkspaceBuffer currentSituationalModel;
	
	protected GlobalWorkspace globalWorkspace;
	
	protected NodeStructure soughtContent;
	
	public AttentionCodeletImpl(){
		super();
		soughtContent =new NodeStructureImpl();
	}
	
	@Override
	public void init(){
	}
    
	/**
	 * Sets associated Module
	 * @param module the module to be associated with
	 * @param usage - way of associating the module
	 */
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
	
	/**
	 * On finding sought content in CSM, create a coalition
     * and put it in the global workspace 
	 */
	@Override
	protected void runThisLidaTask() {
		if (hasSoughtContent(currentSituationalModel)) {
			NodeStructure csmContent = getWorkspaceContent(currentSituationalModel);
			if (csmContent.getLinkableCount() > 0){
				globalWorkspace.addCoalition(new CoalitionImpl((BroadcastContent)csmContent, getActivation()));
				logger.log(Level.FINE, this + " adds coalition", LidaTaskManager.getCurrentTick());
			}
		}
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
	public abstract boolean hasSoughtContent(WorkspaceBuffer buffer);

	/**
	 * Returns sought content and related content from specified
	 * WorkspaceBuffer.
	 * 
	 * @param buffer
	 *            the buffer
	 * @return the workspace content
	 */
	@Override
	public abstract NodeStructure getWorkspaceContent(WorkspaceBuffer buffer); 

	/**
	 * @return the sought content
	 */
	@Override
	public NodeStructure getSoughtContent(){
		return soughtContent;
	}

	/**
	 * @param content sought content
	 */
	@Override
	public void setSoughtContent(NodeStructure content) {
		soughtContent = content;
	}
	
	@Override
	public String toString(){
		return "AttentionCodelet-"+ getTaskId();
	}

}