/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * This class implements module of WorkspaceBuffer. WorkspaceBuffer is a submodule of workspace and 
 * it contains nodeStructures. Also this class maintains activation lower bound of its nodeStructures.
 * {@link WorkspaceBuffer} implementation. Uses a single NodeStructure for the content.
 * @author Ryan J. McCall
 */
public class WorkspaceBufferImpl extends LidaModuleImpl implements WorkspaceBuffer{
	
	private static final Logger logger = Logger.getLogger(WorkspaceBufferImpl.class.getCanonicalName());
	
	private final double DEFAULT_REMOVABLE_THRESHOLD = 0.01;
	
	//TODO Consider having multiple NodeStructures 
	private NodeStructure buffer;	
	
	/**
	 * Construction method. This method initializes nodeStructures that belong to the workspaceBuffer.
	 */
	public WorkspaceBufferImpl() {
		buffer = new NodeStructureImpl();
	}
	
	@Override
	public void init() {
		Double d = (Double) getParam("removableThreshold", DEFAULT_REMOVABLE_THRESHOLD);
		setRemovalThreshold(d);
	}
	
	@Override
	public void setRemovalThreshold(double activationLowerBound) {
		logger.log(Level.FINE, "Activation lower bound for buffer " + getModuleName() + 
					" set to " + activationLowerBound, LidaTaskManager.getCurrentTick());
		buffer.setLowerActivationBound(activationLowerBound);
	}

	@Override
	public Object getModuleContent(Object... params) {
		return buffer;
	}

	@Override
	public void decayModule(long ticks){
		super.decayModule(ticks);
		decayCounter++;
		if(decayCounter % 20 == 0){
			if(getModuleName() == ModuleName.PerceptualBuffer)
				buffer.decayNodeStructure(ticks);
			else{
				buffer.decayNodeStructure(ticks);
				decayCounter--;
			}
		}else{
			buffer.decayNodeStructure(ticks);
		}
	}
	
	private int decayCounter = 0;
	
	@Override
	public void addListener(ModuleListener listener) {
		//N/A
	}
}
