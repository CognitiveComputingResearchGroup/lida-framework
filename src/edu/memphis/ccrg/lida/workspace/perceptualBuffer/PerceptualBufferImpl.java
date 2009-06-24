package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

public class PerceptualBufferImpl implements PerceptualBuffer, CodeletReadable{
	
	private WorkspaceContent pamContent = new NodeStructureImpl();	
	private List<WorkspaceContent> perceptBuffer = new ArrayList<WorkspaceContent>();
	private List<PerceptualBufferListener> pbListeners = new ArrayList<PerceptualBufferListener>();	
	private final int PERCEPT_BUFFER_CAPACITY;
	private List<Object> guiContent = new ArrayList<Object>();	
	
	public PerceptualBufferImpl(int capacity){
		PERCEPT_BUFFER_CAPACITY = capacity;
		perceptBuffer.add(pamContent);
	}
	
	public void addPBufferListener(PerceptualBufferListener l){
		pbListeners.add(l);
	}
	
	public synchronized void receivePAMContent(NodeStructure pc){
		pamContent = (NodeStructureImpl) pc;
	}
	
	/**
	 * Main method of the perceptual buffer.  Stores shared content 
	 * and then sends it to the codelet driver.
	 */
	public void activateCodelets(){
		storePAMContent();

		NodeStructureImpl copiedStruct = new NodeStructureImpl((NodeStructure) perceptBuffer.get(0));
		for(int i = 0; i < pbListeners.size(); i++)		
			pbListeners.get(i).receivePBufferContent(copiedStruct);				
		
		guiContent.add(copiedStruct.getNodeCount());
		guiContent.add(copiedStruct.getLinkCount());					
	}//sendContent
	
	private synchronized void storePAMContent(){
		perceptBuffer.add(new NodeStructureImpl((NodeStructure) pamContent));		
		//Keep the buffer at a fixed size
		if(perceptBuffer.size() > PERCEPT_BUFFER_CAPACITY)
			perceptBuffer.remove(0);//remove oldest	
	}//method

	/**
	 * for codelets to get Content from the buffer.  Eventually based on an objective.
	 * Currently objective not used.
	 */
	public WorkspaceContent lookForContent(NodeStructure objective) {
		NodeStructureImpl result = new NodeStructureImpl();
		synchronized(this){
			for(WorkspaceContent content: perceptBuffer){
				Collection<Node> nodes = ((NodeStructure) content).getNodes();					
				for(Node n: nodes)
					result.addNode(n);				
			}//for each struct in the buffer
		}//synchronized
		return result;
	}//method

	public List<Object> getGuiContent() {
		return guiContent;
	}

}//PerceptualBuffer