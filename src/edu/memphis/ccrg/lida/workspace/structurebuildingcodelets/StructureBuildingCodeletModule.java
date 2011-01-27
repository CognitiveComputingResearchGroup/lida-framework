/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public class StructureBuildingCodeletModule extends LidaModuleImpl implements GuiEventProvider {

	private static final Logger logger=Logger.getLogger(StructureBuildingCodeletModule.class.getCanonicalName());
	
	private Workspace workspace;
	
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();

	public StructureBuildingCodeletModule() {
		super(ModuleName.StructureBuildingCodeletModule);
	}
	
	@Override
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}
	
	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
		if (module != null) {
			if (module instanceof Workspace
					&& module.getModuleName() == ModuleName.Workspace) {
				workspace = (Workspace) module;
			}
		}
	}
		
//	/**
//	 * TODO If BufferContent activates a sbCodelet's context, start a new codelet
//	 *  TODO code to determine when/what codelets to activate
//	 */
//	private void activateCodelets() {
//		
//	}
	
	@Override
	public void sendEventToGui(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener gui : guis)
			gui.receiveFrameworkGuiEvent(evt);
	}

	/** 
	 *  TODO use factory
	 *  TODO configuration for codelet that is spawned
	 *  TODO Rename
	 */
	public void spawnNewCodelet(){
		WorkspaceBuffer csm = (WorkspaceBuffer) workspace.getSubmodule(ModuleName.CurrentSituationalModel);
		WorkspaceBuffer perceptualBuffer = (WorkspaceBuffer) workspace.getSubmodule(ModuleName.PerceptualBuffer);
		StructureBuildingCodelet basic = new StructureBuildingCodeletImpl();
		basic.addAccessibleBuffer(csm);
		basic.addAccessibleBuffer(perceptualBuffer);
		taskSpawner.addTask(basic);
		logger.log(Level.FINER,"New codelet "+basic+"spawned",LidaTaskManager.getCurrentTick());
	}

	@Override
	public String toString() {
		return ModuleName.StructureBuildingCodeletModule + "";
	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void decayModule(long ticks) {
		super.decayModule(ticks);
		//TODO
	}

}// class