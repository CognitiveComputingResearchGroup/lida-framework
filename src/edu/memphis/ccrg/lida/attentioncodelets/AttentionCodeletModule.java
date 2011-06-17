/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.attentioncodelets;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.PreafferenceListener;
import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
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
	
/**
 * {@link FrameworkModule} which creates and manages all {@link AttentionCodelet}.
 * @author Ryan J. McCall
 *
 */
public class AttentionCodeletModule extends FrameworkModuleImpl implements
		BroadcastListener, PreafferenceListener, CodeletManagerModule {

	private static final Logger logger = Logger
			.getLogger(AttentionCodeletModule.class.getCanonicalName());
	private static ElementFactory factory = ElementFactory.getInstance();

	private static final String DEFAULT_CODELET_TYPE = BasicAttentionCodelet.class.getSimpleName();
	private String defaultCodeletType = DEFAULT_CODELET_TYPE;

	private static final double DEFAULT_CODELET_ACTIVATION = 1.0;
	private double codeletActivation = DEFAULT_CODELET_ACTIVATION;
	
	private static final double DEFAULT_CODELET_REMOVAL_THRESHOLD = -1.0;
	private double codeletRemovalThreshold = DEFAULT_CODELET_REMOVAL_THRESHOLD;
	
	private Map<ModuleName, FrameworkModule> modulesMap = new HashMap<ModuleName, FrameworkModule>();
		
	/**
	 * Default constructor
	 */
	public AttentionCodeletModule() {
	}
	
	@Override
	public void init() {
		defaultCodeletType = (String) getParam("attentionModule.defaultCodeletType", DEFAULT_CODELET_TYPE);
		codeletActivation = (Double) getParam("attentionModule.codeletActivation", DEFAULT_CODELET_ACTIVATION);
		codeletRemovalThreshold = (Double) getParam("attentionModule.codeletRemovalThreshold", DEFAULT_CODELET_REMOVAL_THRESHOLD);
	}
	
	@Override
	public void setAssociatedModule(FrameworkModule module, String moduleUsage) {
		if (module instanceof Workspace) {
			FrameworkModule m = module.getSubmodule(ModuleName.CurrentSituationalModel);
			modulesMap.put(m.getModuleName(), m);
		}else if (module instanceof GlobalWorkspace) {
			modulesMap.put(module.getModuleName(), module);
		}
	}
	
	@Override
	public void setDefaultCodeletType(String type){
		if(factory.containsTaskType(type)){
			defaultCodeletType = type;
		}else{
			logger.log(Level.WARNING, 
					"Cannot set default codelet type, factory does not have type {1}",
					new Object[]{TaskManager.getCurrentTick(),type});
		}
	}

	/*
	 * Schedules a task that receives broadcast from global workspace
	 */
	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		synchronized (this) {
			ProcessBroadcastTask task = new ProcessBroadcastTask(
					((NodeStructure) bc).copy());
			taskSpawner.addTask(task);
		}
	}
	
	/*
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
	}

	@Override
	public AttentionCodelet getDefaultCodelet(Map<String, Object> params) {
		return getCodelet(defaultCodeletType, params);
	}

	@Override
	public AttentionCodelet getDefaultCodelet() {
		return getCodelet(defaultCodeletType, null);
	}
	
	@Override
	public AttentionCodelet getCodelet(String type) {
		return getCodelet(type, null);
	}	

	@Override
	public AttentionCodelet getCodelet(String type, Map<String, Object> params) {
		AttentionCodelet codelet = (AttentionCodelet) factory.getFrameworkTask(type, params, modulesMap);
		if (codelet == null) {
			logger.log(
					Level.WARNING,
					"Specified type does not exist in the factory. Attention codelet not created.",
					TaskManager.getCurrentTick());
			return null;
		}
		codelet.setActivation(codeletActivation);
		codelet.setActivatibleRemovalThreshold(codeletRemovalThreshold);
		return codelet;
	}

	@Override
	public void addCodelet(Codelet codelet) {
		if(codelet instanceof AttentionCodelet){
			taskSpawner.addTask(codelet);
			logger.log(Level.FINER, "New attention codelet: {1} added to run.", 
					new Object[]{TaskManager.getCurrentTick(),codelet});
		}else{
			logger.log(Level.WARNING, "Can only add an AttentionCodelet", TaskManager.getCurrentTick());
		}
	}

	@Override
	public void receivePreafference(NodeStructure addSet,
			NodeStructure deleteSet) {
		// TODO Receive results from Action Selection and create Attention
		// Codelets. We need
		// to figure out how to create coalitions and detect that something was
		// "deleted"
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
	public Object getModuleContent(Object... params) {
		if(params != null && params.length > 0 && params[0] instanceof String){
			if("GlobalWorkspace".equalsIgnoreCase((String) params[0])){
				return modulesMap.get(ModuleName.GlobalWorkspace);
			}
		}
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
	}

	@Override
	public void decayModule(long ticks) {
	}

}