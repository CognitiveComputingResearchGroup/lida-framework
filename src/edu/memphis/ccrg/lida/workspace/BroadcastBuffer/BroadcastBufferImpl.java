package edu.memphis.ccrg.lida.workspace.BroadcastBuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

public class BroadcastBufferImpl implements BroadcastBuffer, CodeletReadable{
	
	private WorkspaceContent broadcastContent = new NodeStructureImpl();	
	private List<WorkspaceContent> broadcastBuffer = new ArrayList<WorkspaceContent>();
	private List<BroadcastBufferListener> bBufferListeners = new ArrayList<BroadcastBufferListener>();	
	private final int BROADCAST_BUFFER_CAPACITY;
	private List<Object> guiContent = new ArrayList<Object>();	

	public BroadcastBufferImpl(int capacity){
		BROADCAST_BUFFER_CAPACITY = capacity;
		broadcastBuffer.add(broadcastContent);
	}

	public void addBroadcastBufferListener(BroadcastBufferListener l) {
		bBufferListeners.add(l);		
	}

	public void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = (WorkspaceContent) bc;		
	}

	/**
	 * Main method of the perceptual buffer.  Stores shared content 
	 * and then sends it to the codelet driver.
	 */
	public void activateCodelets(){
		storeBroadcastContent();

		NodeStructureImpl copiedStruct = new NodeStructureImpl((NodeStructure) broadcastBuffer.get(0));
		for(int i = 0; i < bBufferListeners.size(); i++)		
			bBufferListeners.get(i).receiveBroadcastBufferContent(copiedStruct);				
		
		guiContent.add(copiedStruct.getNodeCount());
		guiContent.add(copiedStruct.getLinkCount());					
	}//sendContent
	
	private synchronized void storeBroadcastContent(){
		broadcastBuffer.add(new NodeStructureImpl((NodeStructure) broadcastContent));		
		//Keep the buffer at a fixed size
		if(broadcastBuffer.size() > BROADCAST_BUFFER_CAPACITY)
			broadcastBuffer.remove(0);//remove oldest	
	}//method

	/**
	 * for codelets to get Content from the buffer.  Eventually based on an objective.
	 * Currently objective not used.
	 */
	public WorkspaceContent lookForContent(NodeStructure objective) {
		NodeStructureImpl result = new NodeStructureImpl();
		synchronized(this){
			for(WorkspaceContent content: broadcastBuffer){
				Collection<Node> nodes = ((NodeStructure) content).getNodes();					
				for(Node n: nodes)
					result.addNode(n);				
			}//for each struct in the buffer
		}//synchronized
		return result;
	}//method

	public List<Object> getGuiContent() {
		return guiContent;
	}//method

}//class