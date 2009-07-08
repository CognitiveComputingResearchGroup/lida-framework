package edu.memphis.ccrg.lida.workspace.broadcastBuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.GuiContentProvider;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

public class BroadcastQueueImpl implements BroadcastQueue, CodeletReadable, GuiContentProvider{
	
	private NodeStructure broadcastContent = new NodeStructureImpl();	
	private List<NodeStructure> broadcastQueue = new ArrayList<NodeStructure>();
	private List<WorkspaceBufferListener> listeners = new ArrayList<WorkspaceBufferListener>();	
	private List<FrameworkGui> guis = new ArrayList<FrameworkGui>();
	private final int broadcastQueueCapacity;
	private List<Object> guiContent = new ArrayList<Object>();	

	public BroadcastQueueImpl(int capacity){
		broadcastQueueCapacity = capacity;
		broadcastQueue.add(broadcastContent);
	}

	public void addBufferListener(WorkspaceBufferListener l) {
		listeners.add(l);		
	}
	
	public void addFrameworkGui(FrameworkGui listener) {
		guis.add(listener);
	}

	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastQueue.add((NodeStructure) bc);		
		//Keep the buffer at a fixed size
		if(broadcastQueue.size() > broadcastQueueCapacity)
			broadcastQueue.remove(0);//remove oldest	
	}

	/**
	 * Main method of the perceptual buffer.  Stores shared content 
	 * and then sends it to the codelet driver.
	 */
	public void activateCodelets(){
		NodeStructureImpl copiedStruct = new NodeStructureImpl((NodeStructure) broadcastQueue.get(0));
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).receiveBufferContent(WorkspaceBufferListener.BQUEUE, copiedStruct);				
		
		guiContent.clear();
		guiContent.add(copiedStruct.getNodeCount());
		guiContent.add(copiedStruct.getLinkCount());					
	}//sendContent

	/**
	 * for codelets to get Content from the buffer.  Eventually based on an objective.
	 * Currently objective not used.
	 */
	public NodeStructure lookForContent(NodeStructure objective) {
		NodeStructureImpl result = new NodeStructureImpl();
		synchronized(this){
			for(NodeStructure content: broadcastQueue){
				Collection<Node> nodes = content.getNodes();					
				for(Node n: nodes)
					result.addNode(n);				
			}//for each struct in queue
		}//synchronized
		return result;
	}//method

	public void sendGuiContent() {
		for(FrameworkGui g: guis)
			g.receiveGuiContent(FrameworkGui.FROM_BROADCAST_QUEUE, guiContent);
	}

}//class