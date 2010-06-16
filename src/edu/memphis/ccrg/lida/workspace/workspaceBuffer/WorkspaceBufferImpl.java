package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class WorkspaceBufferImpl extends LidaModuleImpl implements WorkspaceBuffer{
	
	private double activationLowerBound = 0.01;
	public WorkspaceBufferImpl(ModuleName lidaModule) {
		super(lidaModule);
	}
	public WorkspaceBufferImpl() {
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
	public void decayModule(long ticks){
		Collection<Node> nodes = buffer.getNodes();
		Collection<Link> links = buffer.getLinks();
		for(Node n: nodes){
			n.decay(ticks);
			//System.out.println(this.toString() + ": actual activation " + n.getActivation() + " " + lowerActivationBound);
			if (n.getActivation() <= activationLowerBound)
				buffer.deleteNode(n);	
		}
		for(Link l: links){
			l.decay(ticks);
			if(l.getActivation() <= activationLowerBound)
				buffer.deleteLink(l);
		}
	}

	public void setLowerActivationBound(double lowerActivationBound) {
		//System.out.println("lower activation boudn being set "+ lowerActivationBound);
		this.activationLowerBound=lowerActivationBound;		
	}
	public void addListener(ModuleListener listener) {
	}
}
