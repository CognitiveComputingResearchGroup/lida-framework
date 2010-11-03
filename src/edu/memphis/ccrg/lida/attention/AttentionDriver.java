/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.attention;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.PreafferenceListener;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeFactory;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.CodeletModuleUsage;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;
 
public class AttentionDriver extends ModuleDriverImpl implements BroadcastListener, PreafferenceListener {

	private static Logger logger = Logger.getLogger("lida.attention.AttentionDriver");
	
	private WorkspaceBuffer csm;
	private GlobalWorkspace globalWorkspace;
	private NodeStructure broadcastContent;
	
	private NodeFactory factory = NodeFactory.getInstance();

	public AttentionDriver() {
		super(DEFAULT_TICKS_PER_CYCLE, ModuleName.AttentionDriver);
	}
	
	@Override
	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof Workspace
					&& module.getModuleName() == ModuleName.Workspace) {
				csm = (WorkspaceBuffer) module.getSubmodule(ModuleName.CurrentSituationalModel);
			} else if (module instanceof GlobalWorkspace
					&& module.getModuleName() == ModuleName.GlobalWorkspace) {
				globalWorkspace = (GlobalWorkspace) module;
			}
		}
	}

	@Override
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = ((NodeStructure) bc).copy();
	}

	@Override
	public void runThisDriver(){
		activateCodelets();
	}

	private void activateCodelets() {
		//TODO
	}
	
	//TODO better for these to be part of this class or the factory that creates the codelets?
	private String defaultCodeletName = "AttentionCodeletImpl";
	private int defaultCodeletTicksPerStep = 5;
	private double defaultCodeletActivation = 1.0;
	private Map<String, Object> params = null;

	public AttentionCodelet getNewAttentionCodelet() {
		AttentionCodelet codelet = (AttentionCodelet) factory.getCodelet(defaultCodeletName, defaultCodeletTicksPerStep, defaultCodeletActivation, params);
		codelet.setAssociatedModule(CodeletModuleUsage.TO_WRITE_TO, globalWorkspace);
		codelet.setAssociatedModule(CodeletModuleUsage.TO_READ_FROM, csm);
		return codelet;
	}// method
	
	public void runAttentionCodelet(AttentionCodelet codelet){
		super.addTask(codelet);
		logger.log(Level.FINER,"New attention codelet "+codelet.toString()+" spawned.",LidaTaskManager.getActualTick());
	}

	@Override
	public void receivePreafference(Collection<Node> addSet, Collection<Node> deleteSet) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void learn() {
		Collection<Node> nodes = broadcastContent.getNodes();
		for (Node n : nodes) {
			// Implement learning here
			n.getId();
		}
	}

	@Override
	public String toString() {
		return ModuleName.AttentionDriver + "";
	}

}// class