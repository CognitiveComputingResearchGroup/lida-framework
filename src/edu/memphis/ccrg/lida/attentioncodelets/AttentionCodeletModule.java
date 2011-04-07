/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.attentioncodelets;

import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.PreafferenceListener;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.Codelet;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.Workspace;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletManagerModule;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;
	
/**
 * {@link LidaModule} which creates and manages all {@link AttentionCodelet}s.
 * @author Ryan J. McCall
 *
 */
public class AttentionCodeletModule extends LidaModuleImpl implements
		BroadcastListener, PreafferenceListener, CodeletManagerModule {

	private static final Logger logger = Logger
			.getLogger(AttentionCodeletModule.class.getCanonicalName());

	private WorkspaceBuffer csm;
	
	private GlobalWorkspace globalWorkspace;

	private static LidaElementFactory factory = LidaElementFactory.getInstance();

	private String defaultCodeletName;

	private static final int defaultCodeletTicksPerStep = 5;
	private int codeletTicksPerStep = defaultCodeletTicksPerStep;

	private static final double defaultCodeletActivation = 1.0;
	private double codeletActivation = defaultCodeletActivation;

	public AttentionCodeletModule() {
		Class<BasicAttentionCodeletImpl> cl = BasicAttentionCodeletImpl.class;
		factory.addCodeletType(cl.getSimpleName(), cl.getCanonicalName());
		defaultCodeletName = cl.getSimpleName();
	}

	@Override
	public void init() {
	}

	/**
	 * Sets associated Module
	 * 
	 * @param module
	 *            the module to be associated with
	 * @param moduleUsage
	 *            way of associating the module
	 */
	@Override
	public void setAssociatedModule(LidaModule module, String moduleUsage) {
		if (module instanceof Workspace) {
			csm = (WorkspaceBuffer) module
					.getSubmodule(ModuleName.CurrentSituationalModel);
		} else if (module instanceof GlobalWorkspace) {
			globalWorkspace = (GlobalWorkspace) module;
		}
	}

	/**
	 * Class that receives the broadcast and the broadcast will then be used for
	 * learning
	 * 
	 */
	private class ProcessBroadcastTask extends LidaTaskImpl {
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

	/**
	 * Schedules a task that receives broadcast from global workspace
	 * 
	 * @param bc
	 *            the content of broadcast
	 * 
	 */
	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		synchronized (this) {
			ProcessBroadcastTask task = new ProcessBroadcastTask(
					((NodeStructure) bc).copy());
			taskSpawner.addTask(task);
		}
	}

	/**
	 * Creates an attention codelet from the factory and associates it with
	 * global workspace and CSM
	 * 
	 * @return AttentionCodelet - the new attention codelet
	 * 
	 */
	@Override
	public AttentionCodelet getDefaultCodelet(Map<String, Object> params) {
		return getCodelet(defaultCodeletName, params);
	}

	@Override
	public AttentionCodelet getDefaultCodelet() {
		return getCodelet(defaultCodeletName, null);
	}
	
	@Override
	public AttentionCodelet getCodelet(String type) {
		return getCodelet(type, null);
	}	

	//TODO params not being used currently
	@Override
	public AttentionCodelet getCodelet(String type, Map<String, Object> params) {
		AttentionCodelet codelet = (AttentionCodelet) factory.getCodelet(
				type, codeletTicksPerStep, codeletActivation, null);
		if (codelet == null) {
			logger.log(
					Level.WARNING,
					"Specified type does not exist in the factory. Attention codelet not created.",
					LidaTaskManager.getCurrentTick());
			return null;
		}
		codelet.setAssociatedModule(csm, ModuleUsage.TO_READ_FROM);
		codelet.setAssociatedModule(globalWorkspace, ModuleUsage.TO_WRITE_TO);
		return codelet;
	}

	/**
	 * Schedule the attention codelet in the task manager
	 * 
	 * @param codelet
	 *            the new attention codelet to be run
	 * 
	 */
	@Override
	public void addCodelet(Codelet codelet) {
		if(codelet instanceof AttentionCodelet){
			taskSpawner.addTask(codelet);
			logger.log(Level.FINER, "New attention codelet " + codelet.toString()
				+ " spawned.", LidaTaskManager.getCurrentTick());
		}else{
			logger.log(Level.WARNING, "Can only add an AttentionCodelet", LidaTaskManager.getCurrentTick());
		}
	}

	@Override
	public void receivePreafference(NodeStructure addSet,
			NodeStructure deleteSet) {
		// TODO Recive results from Action Selection and create Attention
		// Codelets. We need
		// to figure out how to create coalitions and detect that something was
		// "deleted"
	}

	/**
	 * Implements learning in Attention Module
	 * 
	 * @param content
	 *            the broadcast that needs to be learned
	 * 
	 */
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
		if(params.length>0){
			for(int i=0;i<params.length;i++){
				if(params[i]=="GlobalWorkspace")
					return globalWorkspace;
			}			
		}
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
	}

	@Override
	public void decayModule(long ticks) {
		super.decayModule(ticks);
		// TODO decay codelets?
	}


}