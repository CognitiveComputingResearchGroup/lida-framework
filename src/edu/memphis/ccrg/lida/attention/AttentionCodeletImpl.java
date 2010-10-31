/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.attention;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * Basic attention codelet checking CSM and if finding sought content, creates a coalition
 * and puts it in the global workspace.
 * 
 * TODO Tutorial on setting for LidaTasks, ModuleDrivers, and LidaModules
 * 
 * @author Ryan J McCall
 * 
 */
public class AttentionCodeletImpl extends LidaTaskImpl implements AttentionCodelet {
	
	protected NodeStructure soughtContent = new NodeStructureImpl();
	protected WorkspaceBuffer currentSituationalModel;
	protected GlobalWorkspace globalWorkspace;
	
	/**
	 * 
	 */
	public AttentionCodeletImpl(){
		super();
	}
	
	/**
	 * TODO Use default constructor with factory and init
	 * @param csm
	 * @param g
	 * @param ticksPerStep
	 * @param activation
	 * @param s
	 */
    public AttentionCodeletImpl(WorkspaceBuffer csm, GlobalWorkspace g, int ticksPerStep,
    							double activation, NodeStructure soughtContent){
    	super(ticksPerStep);
    	setActivation(activation);
    	this.currentSituationalModel = csm;
    	globalWorkspace = g;
    	this.soughtContent=soughtContent;
    }
    
	public void init(){
		currentSituationalModel=(WorkspaceBuffer)getParam("csm",null);
		soughtContent=(NodeStructure)getParam("soughtContent",null);
		globalWorkspace=(GlobalWorkspace)getParam("gw",null);
	}

	@Override
	public void setAssociatedModule(LidaModule module) {
		if (module instanceof GlobalWorkspace){
		globalWorkspace = (GlobalWorkspace) module;
		}		
	}
	
	/**
	 * @param csm the WorkspaceBuffer to set
	 */
	public void setWorkspaceBuffer(WorkspaceBuffer csm) {
		this.currentSituationalModel = csm;
	}

	/**
	 * @param gw the GlobalWorkspace to set
	 */
	public void setGlobalWorkspace(GlobalWorkspace gw) {
		this.globalWorkspace = gw;
	}

	/**
	 * @param desiredContent the soughtContent to set
	 */
	public void setDesiredContent(NodeStructure desiredContent) {
		this.soughtContent = desiredContent;
	}

	protected void runThisLidaTask() {
		if (csmHasDesiredContent()) {
			NodeStructure csmContent = getCsmContent();
			if (csmContent != null)
				globalWorkspace.addCoalition(new CoalitionImpl(csmContent, getActivation()));
		}
	}

	//TODO Strategy pattern
	public boolean csmHasDesiredContent() {
		NodeStructure model = (NodeStructure)currentSituationalModel.getModuleContent();
		Collection<Node> nodes = soughtContent.getNodes();
		Collection<Link> links = soughtContent.getLinks();
		for (Node n : nodes)
			if (!model.containsNode(n))
				return false;

		for (Link l : links)
			if (!model.containsLink(l))
				return false;

		return true;
	}

	public NodeStructure getCsmContent() {
		if (csmHasDesiredContent())
			return (NodeStructure) currentSituationalModel.getModuleContent();
		else
			return null;
	}
	public String toString(){
		return "AttentionCodelet-"+ getTaskId();
	}

	/**
	 * @return the desired content
	 */
	private NodeStructure getDesiredContent() {
		return soughtContent;
	}	

}// class