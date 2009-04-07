package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import edu.memphis.ccrg.lida._perception.GraphImpl;
import edu.memphis.ccrg.lida._perception.PAMContentImpl;
import edu.memphis.ccrg.lida._perception.interfaces.PAMContent;
import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletAccessible;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletObjective;

public class PerceptualBufferImpl implements PerceptualBuffer, CodeletAccessible{
	
	private PAMContent pamContent;	
	private List<NodeStructure> perceptBuffer;
	private List<PerceptualBufferListener> pbListeners;	
	private final int PERCEPT_BUFFER_CAPACITY = 2;
	private FrameworkGui testGui;	
	
	public PerceptualBufferImpl(){
		pamContent = new PAMContentImpl();
		perceptBuffer = new ArrayList<NodeStructure>();
		pbListeners = new ArrayList<PerceptualBufferListener>();
	}//public Workspace()

	public void addTestGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}
	
	public void addPBufferListener(PerceptualBufferListener l){
		pbListeners.add(l);
	}
	
	public synchronized void receivePAMContent(PAMContent pc){
		pamContent = pc;
	}
	
	private synchronized void storePAMContent(){
		GraphImpl struct = (GraphImpl)pamContent.getContent();	
		
		if(struct != null)		
			perceptBuffer.add(new GraphImpl(struct));			
		
		if(perceptBuffer.size() > PERCEPT_BUFFER_CAPACITY)
			perceptBuffer.remove(0);	
	}//public void storePAMContent()
	
	/**
	 * Main method of the perceptual buffer.  Stores shared content 
	 * and then sends it to the codelet driver.
	 */
	public void sendContent(){
		storePAMContent();
		
		if(perceptBuffer.size() > 0){
			GraphImpl tempGraph = new GraphImpl((GraphImpl)perceptBuffer.get(0));
			for(int i = 0; i < pbListeners.size(); i++){
				PerceptualBufferContentImpl content = new PerceptualBufferContentImpl(tempGraph);
				pbListeners.get(i).receivePBufferContent(content);				
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
	public WorkspaceContent getCodeletsObjective(CodeletObjective objective) {
		PerceptualBufferContentImpl content = new PerceptualBufferContentImpl();
		
		synchronized(this){
			for(NodeStructure struct: perceptBuffer){
				Set<Node> nodes = struct.getNodes();					
				for(Node n: nodes)
					content.addNode(n);				
			}//for each struct in the buffer
		}//synchronized
		
		return content;
	}//getCodeletsObjective

}//PerceptualBuffer
