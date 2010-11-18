/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.example.framework.initialization;

import java.util.Map;

import edu.memphis.ccrg.lida.attention.AttentionCodelet;
import edu.memphis.ccrg.lida.attention.AttentionModuleImpl;
import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.Initializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.shared.NodeFactory;
import edu.memphis.ccrg.lida.framework.tasks.CodeletModuleUsage;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * @author Javier Snaider
 *
 */
public class AttentionModuleInitializer implements Initializer {

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.initialization.Initializer#initModule(edu.memphis.ccrg.lida.framework.initialization.Initializable, edu.memphis.ccrg.lida.framework.Lida, java.util.Properties)
	 */
	@Override
	public void initModule(Initializable module, Lida lida, Map<String,?> params) {
		AttentionModuleImpl driver = (AttentionModuleImpl)module;
		NodeFactory factory = NodeFactory.getInstance();
		GlobalWorkspace gw = (GlobalWorkspace)lida.getSubmodule(ModuleName.GlobalWorkspace);
		WorkspaceBuffer csm = (WorkspaceBuffer)lida.getSubmodule(ModuleName.Workspace).getSubmodule(ModuleName.CurrentSituationalModel);
		
//		AttentionCodelet ac = driver.getNewAttentionCodelet();
		AttentionCodelet ac=(AttentionCodelet)factory.getCodelet("BasicAttention" , 20, 1.0, null);
		//TODO getCodelet not working
		ac.setAssociatedModule(CodeletModuleUsage.TO_WRITE_TO, gw);
		ac.setAssociatedModule(CodeletModuleUsage.TO_READ_FROM, csm);
		driver.runAttentionCodelet(ac);
	}

}
