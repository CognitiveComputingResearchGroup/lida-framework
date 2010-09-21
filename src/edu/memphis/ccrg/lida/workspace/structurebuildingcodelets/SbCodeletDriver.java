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
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public class SbCodeletDriver extends ModuleDriverImpl implements GuiEventProvider {

	private static Logger logger=Logger.getLogger("lida.workspace.structurebuildingcodelets.SBCodeletDriver");
	private SbCodeletFactory sbCodeletFactory;
	private Workspace workspace;
	
	public SbCodeletDriver(Workspace w, int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, (LidaTaskManager) tm,ModuleName.SbCodeletDriver);
		sbCodeletFactory = SbCodeletFactory.getInstance();
	}// method

	public SbCodeletDriver() {
		super();
	}// method

	public void runThisDriver() {
		//activateCodelets();
		//sendEventToGui();
	}
	

	
	@Override
	public void init(Map<String, ?> params) {
		super.init(params);
		
	}

	/**
	 * if BufferContent activates a sbCodelet's context, start a new codelet
	 */
	private void activateCodelets() {
		// CODE to determine when/what codelets to activate
	}// method

	//**************GUI***************
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}	
	public void sendEventToGui(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener gui : guis)
			gui.receiveFrameworkGuiEvent(evt);
	}//method

	/*
	 * @param type - See SBCodeletFactory for which integer values correspond to
	 * which type
	 */
	public void spawnNewCodelet(){
		//TODO: use factory
		WorkspaceBuffer csm = (WorkspaceBuffer) workspace.getSubmodule(ModuleName.CurrentSituationalModel);
		WorkspaceBuffer perceptualBuffer = (WorkspaceBuffer) workspace.getSubmodule(ModuleName.PerceptualBuffer);
		StructureBuildingCodelet basic = new SbCodeletImpl(csm, perceptualBuffer);
		
		//sbCodeletFactory.getCodelet(type, activation, context, actions);
		this.addTask(basic);
		logger.log(Level.FINER,"New codelet "+basic+"spawned",LidaTaskManager.getActualTick());
	}// method

	@Override
	public String toString() {
		return ModuleName.StructureBuildingCodelets + "";
	}
	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof Workspace
					&& module.getModuleName() == ModuleName.Workspace) {
				workspace = (Workspace) module;
			}
		}
	}

}// class