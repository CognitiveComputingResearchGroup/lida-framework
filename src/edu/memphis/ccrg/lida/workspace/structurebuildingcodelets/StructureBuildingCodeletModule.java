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

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.tasks.Codelet;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.workspace.Workspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * A module which maintains Codelets of workspace. This module manages Codelets
 * and responds for sending events to framework GUI.
 * 
 * @author Ryan J McCall
 *
 */
public class StructureBuildingCodeletModule extends FrameworkModuleImpl implements
		GuiEventProvider, CodeletManagerModule {

	private static final Logger logger = Logger
			.getLogger(StructureBuildingCodeletModule.class.getCanonicalName());

	private static final double DEFAULT_CODELET_ACTIVATION = 1.0;
	private double codeletActivation = DEFAULT_CODELET_ACTIVATION;

	private static final int DEFAULT_TICKS = 1;
	private int codeletTicksPerRun = DEFAULT_TICKS;

	// /*
	// * Pool keeping all recycled codelets.
	// * Key = CodeletType
	// * Value = finished, reset codelets of that type
	// */
	// private Map<CodeletType, List<StructureBuildingCodelet>> codeletPool;

	private List<FrameworkGuiEventListener> guis;

	private static ElementFactory factory = ElementFactory
			.getInstance();

	private String defaultCodeletName;

	private Workspace workspace;

	/**
	 * Constructor method. It responds for registration of this module to system
	 * and initialization of both Codelet and Framework GUI listener.
	 */
	public StructureBuildingCodeletModule() {
		guis = new ArrayList<FrameworkGuiEventListener>();

		Class<BasicStructureBuildingCodelet> cl = BasicStructureBuildingCodelet.class;
		factory.addCodeletType(cl.getSimpleName(), cl.getCanonicalName());
		defaultCodeletName = cl.getSimpleName();
	}

	@Override
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
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
	public void setAssociatedModule(FrameworkModule module, String moduleUsage) {
		if (module instanceof Workspace) {
			workspace = (Workspace) module;
		} else {
			logger.log(Level.WARNING, "Cannot add module "
					+ module.getModuleName(), TaskManager.getCurrentTick());
		}
	}

	@Override
	public void sendEventToGui(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener gui : guis) {
			gui.receiveFrameworkGuiEvent(evt);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletManager
	 * #getNewCodelet
	 * (edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletType)
	 */
	@Override
	public StructureBuildingCodelet getDefaultCodelet(Map<String, Object> params) {
		return getCodelet(defaultCodeletName, params);		
	}

	@Override
	public StructureBuildingCodelet getDefaultCodelet() {
		return getCodelet(defaultCodeletName, null);
	}

	@Override
	public StructureBuildingCodelet getCodelet(String type) {
		return getCodelet(type, null);
	}

	//TODO removal threshold parameter
	@Override
	public StructureBuildingCodelet getCodelet(String type, Map<String, Object> params) {
		StructureBuildingCodelet codelet = (StructureBuildingCodelet) factory.getCodelet(type,
						codeletTicksPerRun, codeletActivation, -1.0, null);
		if (codelet == null) {
			logger.log(Level.WARNING,
					"Codelet type not supported: " + type,
					TaskManager.getCurrentTick());
			return null;
		}

		String readable = "PerceptualBuffer";
		String writeable = "CurrentSituationalModel";

		if (params != null) {
			readable = (String) params.get(ModuleUsage.TO_READ_FROM);
			writeable = (String) params.get(ModuleUsage.TO_WRITE_TO);
		}

		WorkspaceBuffer readableBuffer = (WorkspaceBuffer) workspace
				.getSubmodule(readable);
		if (readableBuffer == null) {
			logger.log(Level.WARNING, "Readable buffer not found: " + readable,
					TaskManager.getCurrentTick());
			return null;
		}

		WorkspaceBuffer writeableBuffer = (WorkspaceBuffer) workspace
				.getSubmodule(writeable);
		if (writeableBuffer == null) {
			logger.log(Level.WARNING, "Writeable buffer not found: "
					+ writeable, TaskManager.getCurrentTick());
			return null;
		}

		codelet.setAssociatedModule(readableBuffer, ModuleUsage.TO_READ_FROM);
		codelet.setAssociatedModule(writeableBuffer, ModuleUsage.TO_WRITE_TO);
		return codelet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletManager
	 * #runCodelet(edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.
	 * StructureBuildingCodelet)
	 */
	@Override
	public void addCodelet(Codelet cod) {
		if (cod instanceof StructureBuildingCodelet) {
			logger.log(Level.FINER, "New codelet " + cod + "spawned",
					TaskManager.getCurrentTick());
			taskSpawner.addTask(cod);
		} else {
			logger.log(Level.WARNING,
					"codelet must be a structure-buidling codelet",
					TaskManager.getCurrentTick());
		}
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
	}

	@Override
	public void decayModule(long ticks) {
		// TODO decay codelets?
	}

}