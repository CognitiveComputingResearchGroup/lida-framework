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
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.framework.tasks.ModuleUsage;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;
 
public class AttentionModuleImpl extends LidaModuleImpl implements BroadcastListener, PreafferenceListener {

	private static final Logger logger = Logger.getLogger(AttentionModuleImpl.class.getCanonicalName());
	
	private WorkspaceBuffer csm;
	private GlobalWorkspace globalWorkspace;
	
	private LidaElementFactory factory = LidaElementFactory.getInstance();

	public AttentionModuleImpl() {
	}
	
	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
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

	private class ProcessBroadcastTask extends LidaTaskImpl{		
		private NodeStructure broadcast;
		public ProcessBroadcastTask(NodeStructure broadcast) {
			super();
			this.broadcast = broadcast;
		}
		@Override
		protected void runThisLidaTask() {
			learn((BroadcastContent) broadcast);
			setTaskStatus(LidaTaskStatus.FINISHED);
		}	
	}

	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		synchronized (this) {
			ProcessBroadcastTask task = new ProcessBroadcastTask(((NodeStructure) bc).copy());		
			taskSpawner.addTask(task);
		}
	}
	
	//TODO better for these to be part of this class or the factory that creates the codelets?
	private String defaultCodeletName = "AttentionCodeletImpl";
	private int defaultCodeletTicksPerStep = 5;
	private double defaultCodeletActivation = 1.0;
	private Map<String, Object> params = null;

	public AttentionCodelet getNewAttentionCodelet() {
		AttentionCodelet codelet = (AttentionCodelet) factory.getCodelet(defaultCodeletName, defaultCodeletTicksPerStep, defaultCodeletActivation, params);
		codelet.setAssociatedModule(globalWorkspace, ModuleUsage.TO_WRITE_TO);
		codelet.setAssociatedModule(csm, ModuleUsage.TO_READ_FROM);
		return codelet;
	}// method
	
	public void runAttentionCodelet(AttentionCodelet codelet){
		taskSpawner.addTask(codelet);
		logger.log(Level.FINER,"New attention codelet "+codelet.toString()+" spawned.",LidaTaskManager.getActualTick());
	}

	@Override
	public void receivePreafference(NodeStructure addSet, NodeStructure deleteSet) {
		// TODO Recive results from Action Selection and create Attention Codelets. We need
		// to figure out how to create coalitions and detect that somsething was "deleted"
	}
	
	@Override
	public void learn(BroadcastContent content) {
		NodeStructure ns = (NodeStructure) content;
		Collection<Node> nodes = ns.getNodes();
		for (Node n : nodes) {
			// Implement learning here
			n.getId();
		}
	}

	@Override
	public String toString() {
		return ModuleName.AttentionModule + "";
	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
	}

}// class