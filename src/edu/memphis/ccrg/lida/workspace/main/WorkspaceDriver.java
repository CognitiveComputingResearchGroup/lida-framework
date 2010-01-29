/**
 * 
 */
package edu.memphis.ccrg.lida.workspace.main;

import java.util.Properties;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * @author Javier Snaider
 * 
 */
public class WorkspaceDriver extends ModuleDriverImpl {

	private Workspace workspace;
	private double actThreshold;

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.ModuleDriverImpl#runThisDriver()
	 */
	@Override
	protected void runThisDriver() {
		cue();

	}

	private void cue() {
		WorkspaceBuffer percepBuff = (WorkspaceBuffer) workspace
				.getSubmodule(ModuleName.PerceptualBuffer);
		NodeStructure ns = (NodeStructure) percepBuff.getModuleContent();
		NodeStructure cueNs = new NodeStructureImpl();

		for (Node n : ns.getNodes()) {
			if (n.getActivation() >= actThreshold) {
				cueNs.addNode(n);
			}
		}
		if (cueNs.getNodeCount() > 0) {
			workspace.cue(cueNs);
		}
	}

	@Override
	public String toString() {
		return ModuleName.WorkspaceDriver + "";
	}

	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof Workspace
					&& module.getModuleName() == ModuleName.Workspace) {
				workspace = (Workspace) module;
			}
		}
	}

	public void init(Properties lidaProperties) {
		this.lidaProperties = lidaProperties;
		actThreshold = Double.parseDouble(lidaProperties
				.getProperty("workspace.actThreshold"));
		// addressLength =
		// Integer.parseInt(lidaProperties.getProperty("tem.addressLength"
		// ,""+DEF_ADDRESS_LENGTH));
	}

}
