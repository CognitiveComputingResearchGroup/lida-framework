package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class WorkspaceBufferImpl implements WorkspaceBuffer{
	
	protected Queue<NodeStructure> buffer = new ConcurrentLinkedQueue<NodeStructure>();
	protected int capacity = 10;
	
	public WorkspaceBufferImpl(Queue<NodeStructure> b){
		buffer = b;
	}
	public WorkspaceBufferImpl(int capacity){
		this.capacity = capacity;
		buffer.add(new NodeStructureImpl());
	}

	public boolean addLink(Link l) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addNode(Node n) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteLink(Link l) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean deleteNode(Node n) {
		// TODO Auto-generated method stub
		return false;
	}

	public NodeStructure getModuleContent() {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<NodeStructure> getModuleContentCollection() {
		// TODO Auto-generated method stub
		return null;
	}

	public void mergeIn(NodeStructure ns) {
		// TODO Auto-generated method stub
		
	}
	
	public int hashCode(){
		//TODO:
		return 0;
	}
	public boolean equals(){
		//TODO:
		return false;
	}

}
