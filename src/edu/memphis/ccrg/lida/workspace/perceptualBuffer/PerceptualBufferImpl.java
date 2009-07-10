package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.memphis.ccrg.lida.framework.gui.FrameworkGui;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;

public class PerceptualBufferImpl implements PerceptualBuffer, GuiContentProvider{
	
	private NodeStructure pamContent = new NodeStructureImpl();	
	private List<NodeStructure> perceptBuffer = new ArrayList<NodeStructure>();
	private List<WorkspaceBufferListener> pbListeners = new ArrayList<WorkspaceBufferListener>();
	private final int PERCEPT_BUFFER_CAPACITY;
	private List<Object> guiContent = new ArrayList<Object>();
	private List<FrameworkGui> guis = new ArrayList<FrameworkGui>();
	
	public PerceptualBufferImpl(int capacity){
		PERCEPT_BUFFER_CAPACITY = capacity;
		perceptBuffer.add(pamContent);
	}
	
	public void addBufferListener(WorkspaceBufferListener l){
		pbListeners.add(l);
	}
	
	public void addFrameworkGui(FrameworkGui listener) {
		guis.add(listener);
	}
	
	public synchronized void receivePAMContent(NodeStructure ns){
		perceptBuffer.add(ns);		
		//Keep the buffer at a fixed size
		if(perceptBuffer.size() > PERCEPT_BUFFER_CAPACITY)
			perceptBuffer.remove(0);//remove oldest	
	}
	
	/**
	 * Main method of the perceptual buffer.  Stores shared content 
	 * and then sends it to the codelet driver.
	 */
	public void activateCodelets(){
		NodeStructureImpl nStruct = new NodeStructureImpl((NodeStructure) perceptBuffer.get(0));
		for(int i = 0; i < pbListeners.size(); i++)		
			pbListeners.get(i).receiveBufferContent(WorkspaceBufferListener.PBUFFER, nStruct);				

		guiContent.clear();
		guiContent.add(nStruct.getNodeCount());
		guiContent.add(nStruct.getLinkCount());					
	}//sendContent
	
//	/**
//	 * for codelets to get Content from the buffer.  Eventually based on an objective.
//	 * Currently objective not used.
//	 */
//	public NodeStructure lookForContent(NodeStructure objective){
//		NodeStructureImpl result = new NodeStructureImpl();
//		synchronized(this){
//			for(NodeStructure content: perceptBuffer){
//				Collection<Node> nodes = content.getNodes();					
//				for(Node n: nodes)
//					result.addNode(n);				
//			}//for each struct in the buffer
//		}//synchronized
//		return result;
//	}//method

	public List<NodeStructure> getBuffer() {
		return Collections.unmodifiableList(perceptBuffer);
	}

	public void sendGuiContent() {
		for(FrameworkGui fg: guis)
			fg.receiveGuiContent(FrameworkGui.FROM_PERCEPTUAL_BUFFER, guiContent);
	}

}//PerceptualBuffer