/**
 * @(#)AttentionCodelet.java  1.0  Feb, 27, 2009
 *
 * Copyright 2006-2008 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * http://ccrg.cs.memphis.edu/
 * All rights reserved.
 */
package edu.memphis.ccrg.lida.attention;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * These classes implements the coalitions for the tasks performed at LidaTask
 * @author Ryan J McCall
 * 
 */
public class AttentionCodeletImpl extends LidaTaskImpl implements AttentionCodelet {

	protected WorkspaceBuffer csm;
	protected GlobalWorkspace global;
	protected NodeStructure desiredContent;
	
	protected long id;
	
    public AttentionCodeletImpl(WorkspaceBuffer csm, GlobalWorkspace g, int ticksPerStep,
    							double activation, NodeStructure soughtContent, long id){
    	super(ticksPerStep);
    	setActivation(activation);
    	this.csm = csm;
    	global = g;
    	this.desiredContent=soughtContent;
    	this.id = id;
    }

	protected void runThisLidaTask() {
		if (csmHasDesiredContent()) {
			NodeStructure csmContent = getCsmContent();
			
		//	System.out.println("attention codelet finds nodes: " + csmContent.getNodeCount());
			if (csmContent != null){
		//		System.out.println("forming a coalition!");
				if(global.containsCoalition(id))
					global.updateCoalition(id);
				else
					global.addCoalition(id, new CoalitionImpl(csmContent, getActivation()));
			}
		}// if
	}

	public boolean csmHasDesiredContent() {
		NodeStructure model = (NodeStructure)csm.getModuleContent();
		Collection<Node> nodes = desiredContent.getNodes();
		Collection<Link> links = desiredContent.getLinks();
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
			return (NodeStructure)csm.getModuleContent();
		else
			return null;
	}
	public String toString(){
		return "AttentionCodelet-"+ getTaskId();
	}
	/**
	 * @return the WorkspaceBuffer
	 */
	public WorkspaceBuffer getWorkspaceBuffer() {
		return csm;
	}

	/**
	 * @param csm the WorkspaceBuffer to set
	 */
	public void setWorkspaceBuffer(WorkspaceBuffer csm) {
		this.csm = csm;
	}

	/**
	 * @return the GlobalWorkspace
	 */
	public GlobalWorkspace getGlobalWorkspace() {
		return global;
	}

	/**
	 * @param gw the GlobalWorkspace to set
	 */
	public void setGlobalWorkspace(GlobalWorkspace gw) {
		this.global = gw;
	}

	/**
	 * @return the desired content
	 */
	public NodeStructure getDesiredContent() {
		return desiredContent;
	}

	/**
	 * @param desiredContent the soughtContent to set
	 */
	public void setDesiredContent(NodeStructure desiredContent) {
		this.desiredContent = desiredContent;
	}
	
	public void init(){
		csm=(WorkspaceBuffer)getParam("csm",null);
		desiredContent=(NodeStructure)getParam("soughtContent",null);
		global=(GlobalWorkspace)getParam("gw",null);
	}

	@Override
	public void setAssociatedModule(LidaModule module) {
		if (module instanceof GlobalWorkspace){
		global = (GlobalWorkspace) module;
		}		
	}

}// class