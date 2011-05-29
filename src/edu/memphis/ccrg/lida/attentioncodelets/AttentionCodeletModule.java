/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
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

import edu.memphis.ccrg.lida.actionselection.PreafferenceListener;
import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.Codelet;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.Workspace;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletManagerModule;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;
	
/**
 * {@link FrameworkModule} which creates and manages all {@link AttentionCodelet}s.
 * @author Ryan J. McCall
 *
 */
public class AttentionCodeletModule extends FrameworkModuleImpl implements
		BroadcastListener, PreafferenceListener, CodeletManagerModule {

	private static final Logger logger = Logger
			.getLogger(AttentionCodeletModule.class.getCanonicalName());

	private WorkspaceBuffer csm;
	
	private GlobalWorkspace globalWorkspace;

	private static ElementFactory factory = ElementFactory.getInstance();

	private String defaultCodeletName;

	private static final int defaultCodeletTicksPerStep = 5;
	private int codeletTicksPerStep = defaultCodeletTicksPerStep;

	private static final double defaultCodeletActivation = 1.0;
	private double codeletActivation = defaultCodeletActivation;
	
	private static final double defaultCodeletRemovalThreshold = -1.0;
	private double codeletRemovalThreshold = defaultCodeletRemovalThreshold;

	/**
	 * Default constructor
	 */
	public AttentionCodeletModule() {
		Class<BasicAttentionCodelet> cl = BasicAttentionCodelet.class;
		factory.addCodeletType(cl.getSimpleName(), cl.getCanonicalName());
		defaultCodeletName = cl.getSimpleName();
	}
	
	@Override
	public void setDefaultCodeletType(String type){
		if(factory.containsCodeletType(type)){
			defaultCodeletName = type;
		}else{
			logger.log(Level.WARNING, 
					"Cannot set default codelet type, factory does not have type: " + type, 
					TaskManager.getCurrentTick());
		}
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
	public void setAssociatedModule(FrameworkModule module, String moduleUsage) {
		if (module instanceof Workspace) {
			csm = (WorkspaceBuffer) module
					.getSubmodule(ModuleName.CurrentSituationalModel);
		}else if (module instanceof GlobalWorkspace) {
			globalWorkspace = (GlobalWorkspace) module;
		}
	}

	/**
	 * Class that receives the broadcast and the broadcast will then be used for
	 * learning
	 * 
	 */
	private class ProcessBroadcastTask extends FrameworkTaskImpl {
		private NodeStructure broadcast;

		public ProcessBroadcastTask(NodeStructure broadcast) {
			super();
			this.broadcast = broadcast;
		}

		@Override
		protected void runThisFrameworkTask() {
			learn((BroadcastContent) broadcast);
			setTaskStatus(TaskStatus.FINISHED);
		}
		@Override
		public String toString() {
			return AttentionCodeletModule.class.getSimpleName() + "Broadcast";
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
	//TODO default removal threshold parameters
	@Override
	public AttentionCodelet getCodelet(String type, Map<String, Object> params) {
		AttentionCodelet codelet = (AttentionCodelet) factory.getCodelet(type, codeletTicksPerStep, 
				codeletActivation, codeletRemovalThreshold, params);
		if (codelet == null) {
			logger.log(
					Level.WARNING,
					"Specified type does not exist in the factory. Attention codelet not created.",
					TaskManager.getCurrentTick());
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
				+ " spawned.", TaskManager.getCurrentTick());
		}else{
			logger.log(Level.WARNING, "Can only add an AttentionCodelet", TaskManager.getCurrentTick());
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
		return ModuleName.AttentionModule.toString();
	}

	@Override
	public Object getModuleContent(Object... params) {
		if(params != null && params.length > 0 && params[0] instanceof String){
			if("GlobalWorkspace".equalsIgnoreCase((String) params[0])){
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
		// TODO decay codelets?
	}


}