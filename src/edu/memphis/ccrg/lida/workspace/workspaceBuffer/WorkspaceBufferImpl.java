package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class WorkspaceBufferImpl extends LidaModuleImpl implements WorkspaceBuffer{
	
	public WorkspaceBufferImpl(ModuleName lidaModule) {
		super(lidaModule);
	}

	private NodeStructure buffer = new NodeStructureImpl();

	/**
	 * @return the buffer
	 */

	public NodeStructure getModuleContent() {
		return buffer;
	}
		
	/**
	 * decays all the nodes in the buffer.
	 * If a node's activation results lower than lowerActivationBound, it is removed from the buffer.
	 * 
	 * @param lowerActivationBound
	 */
	public void decayNodes(double lowerActivationBound){
		
		Collection<Node> nodes=buffer.getNodes();
		for (Node n:nodes){
			n.decay();
			if (n.getActivation()<=lowerActivationBound){
				buffer.deleteNode(n);
			}
		}
	}
}
