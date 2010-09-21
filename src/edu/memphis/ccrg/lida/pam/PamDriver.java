/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;

/**
 * PamDriver run the PAM
 * @author Ryan J McCall
 *
 */
public class PamDriver extends ModuleDriverImpl implements GuiEventProvider {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger("lida.pam.PamDriver");
	
	@SuppressWarnings("unused")
	private PerceptualAssociativeMemory pam;

	public PamDriver() {
		super(DEFAULT_TICKS_PER_CYCLE,ModuleName.PamDriver);
	}// constructor

	@Override
	protected void runThisDriver() {
	}

	@Override
	public String toString() {
		return ModuleName.PamDriver + "";
	}

	@Override
	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof PerceptualAssociativeMemory
					&& module.getModuleName() == ModuleName.PerceptualAssociativeMemory) {
				pam = (PerceptualAssociativeMemory) module;
			}
		}
	}
}// class 