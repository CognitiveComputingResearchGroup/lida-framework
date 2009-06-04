package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletsDesiredContent;

public class PerceptualBufferImpl implements PerceptualBuffer, CodeletReadable{
	
	private WorkspaceContent pamContent;	
	private List<NodeStructure> perceptBuffer;
	private List<PerceptualBufferListener> pbListeners;	
	private final int PERCEPT_BUFFER_CAPACITY = 2;
	private FrameworkGui testGui;	
	
	public PerceptualBufferImpl(){
		pamContent = new NodeStructureImpl();
		perceptBuffer = new ArrayList<NodeStructure>();
		pbListeners = new ArrayList<PerceptualBufferListener>();
	}//constructor

	public void addFlowGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}
	
	public void addPBufferListener(PerceptualBufferListener l){
		pbListeners.add(l);
	}
	
	public synchronized void receivePAMContent(WorkspaceContent pc){
		pamContent = pc;
	}
	
	private synchronized void storePAMContent(){
		NodeStructure struct = (NodeStructure)pamContent.getContent();		
		if(struct != null)
			perceptBuffer.add(new NodeStructureImpl(struct));		
		
		
		if(perceptBuffer.size() > PERCEPT_BUFFER_CAPACITY)
			perceptBuffer.remove(0);	
	}//public void storePAMContent()
	
	/**
	 * Main method of the perceptual buffer.  Stores shared content 
	 * and then sends it to the codelet driver.
	 */
	public void sendContentToCodelets(){
		storePAMContent();
		
		if(perceptBuffer.size() > 0){
			NodeStructureImpl tempGraph = new NodeStructureImpl((NodeStructure)perceptBuffer.get(0));
			for(int i = 0; i < pbListeners.size(); i++){				
				pbListeners.get(i).receivePBufferContent(tempGraph);				
			}//for

			List<Object> guiContent = new ArrayList<Object>();			
			guiContent.add(tempGraph.getNodes().size());
			guiContent.add(tempGraph.getLinks().size());			
			testGui.receiveGuiContent(FrameworkGui.FROM_PERCEPTUAL_BUFFER, guiContent);
		}
			
	}//sendContent

	/**
	 * for codelets to get Content from the buffer.  Eventually based on an objective.
	 * Currently objective not used.
	 */
	public WorkspaceContent getCodeletsDesiredContent(CodeletsDesiredContent objective) {
		NodeStructureImpl content = new NodeStructureImpl();
		
		synchronized(this){
			for(NodeStructure struct: perceptBuffer){
				Collection<Node> nodes = struct.getNodes();					
				for(Node n: nodes)
					content.addNode(n);				
			}//for each struct in the buffer
		}//synchronized
		
		return content;
	}//getCodeletsObjective

}//PerceptualBuffer