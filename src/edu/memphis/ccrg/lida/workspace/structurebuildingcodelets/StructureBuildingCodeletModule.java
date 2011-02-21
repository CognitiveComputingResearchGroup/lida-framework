/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.workspace.Workspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public class StructureBuildingCodeletModule extends LidaModuleImpl implements GuiEventProvider, CodeletManager {

	private static final Logger logger = Logger
			.getLogger(StructureBuildingCodeletModule.class.getCanonicalName());
	
	private static final double DEFAULT_CODELET_ACTIVATION = 1.0;
	private double codeletActivation = DEFAULT_CODELET_ACTIVATION;
	
	private static final int DEFAULT_TICKS = 1;
	private int codeletTicksPerRun = DEFAULT_TICKS;
	
	/*
	 * Pool keeping all recycled codelets.
	 * Key = CodeletType
	 * Value = finished, reset codelets of that type
	 */
	private Map<CodeletType, List<StructureBuildingCodelet>> codeletPool;

	private List<FrameworkGuiEventListener> guis;
	
	private LidaElementFactory factory = LidaElementFactory.getInstance();
	
	private String defaultCodeletName;

	private Workspace workspace;
	private WorkspaceBuffer csm;
	private WorkspaceBuffer perceptualBuffer;

	public StructureBuildingCodeletModule() {
		super(ModuleName.StructureBuildingCodeletModule);
		codeletPool = new HashMap<CodeletType, List<StructureBuildingCodelet>>();
		codeletPool.put(CodeletType.PERCEPTUAL_BUFFER_TYPE, new ArrayList<StructureBuildingCodelet>());
		
		guis = new ArrayList<FrameworkGuiEventListener>();		
		
		Class<StructureBuildingCodeletImpl> cl = StructureBuildingCodeletImpl.class;
		factory.addCodeletType(cl.getSimpleName(), cl.getCanonicalName());
		defaultCodeletName = cl.getSimpleName();
	}

	@Override
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}

	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
		if (module instanceof Workspace) {
			workspace = (Workspace) module;
			csm = (WorkspaceBuffer) workspace.getSubmodule(ModuleName.CurrentSituationalModel);
			perceptualBuffer = (WorkspaceBuffer) workspace.getSubmodule(ModuleName.PerceptualBuffer);
		}else {
			logger.log(Level.WARNING, "Cannot add module "
					+ module.getModuleName(), LidaTaskManager.getCurrentTick());
		}
	}

	@Override
	public void sendEventToGui(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener gui : guis)
			gui.receiveFrameworkGuiEvent(evt);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletManager#getNewCodelet(edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletType)
	 */
	@Override
	public StructureBuildingCodelet getNewCodelet(CodeletType t) {
		StructureBuildingCodelet codelet = (StructureBuildingCodelet) factory.getCodelet(defaultCodeletName, codeletTicksPerRun, codeletActivation, null);
		if (codelet == null) {
			logger.log(Level.WARNING,
						"Factory returned a null codelet, sb-codelet not created.",
						LidaTaskManager.getCurrentTick());
			return null;
		}
		switch(t){
			case CSM_TYPE:
				codelet.setAssociatedModule(perceptualBuffer, ModuleUsage.TO_READ_FROM);
				codelet.setAssociatedModule(csm, ModuleUsage.TO_WRITE_TO);
				return codelet;
			default:
				logger.log(Level.WARNING, "Codelet type not supported: " + t, LidaTaskManager.getCurrentTick());
				return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletManager#runCodelet(edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.StructureBuildingCodelet)
	 */
	@Override
	public void runCodelet(StructureBuildingCodelet cod){
		logger.log(Level.FINER, "New codelet " + cod + "spawned",
				LidaTaskManager.getCurrentTick());
		taskSpawner.addTask(cod);
	}
	

	// /**
	// *  code to determine when/what codelets to activate. e.g., if BufferContent activates a sbCodelet's context, start a new
	// codelet.
	// */
	// private void activateCodelets() {
	//
	// }
	
	
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletManager#recycle(edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.StructureBuildingCodelet)
	 */
	@Override
	public void recycle(StructureBuildingCodelet sbCode) {
		List<StructureBuildingCodelet> scraps = codeletPool.get(sbCode.getCodeletType());
		sbCode.reset();
		scraps.add(sbCode);
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
		super.decayModule(ticks);
		// TODO decay codelets?
	}

}