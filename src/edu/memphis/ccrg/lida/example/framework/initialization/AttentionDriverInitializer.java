/**
 * 
 */
package edu.memphis.ccrg.lida.example.framework.initialization;

import java.util.Map;

import edu.memphis.ccrg.lida.attention.AttentionCodelet;
import edu.memphis.ccrg.lida.attention.AttentionDriver;
import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.Initializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.shared.NodeFactory;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * @author Javier Snaider
 *
 */
public class AttentionDriverInitializer implements Initializer {

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.initialization.Initializer#initModule(edu.memphis.ccrg.lida.framework.initialization.Initializable, edu.memphis.ccrg.lida.framework.Lida, java.util.Properties)
	 */
	@Override
	public void initModule(Initializable module, Lida lida, Map<String,?> params) {
		AttentionDriver driver = (AttentionDriver)module;
		NodeFactory factory = NodeFactory.getInstance();
		GlobalWorkspace gw = (GlobalWorkspace)lida.getSubmodule(ModuleName.GlobalWorkspace);
		WorkspaceBuffer csm = (WorkspaceBuffer)lida.getSubmodule(ModuleName.Workspace).getSubmodule(ModuleName.CurrentSituationalModel);
		
		AttentionCodelet ac=(AttentionCodelet)factory.getCodelet("BasicAttention" , 20, 1.0, null);
		ac.setGlobalWorkspace(gw);
		ac.setWorkspaceBuffer(csm);
		driver.addTask(ac);
	}

}