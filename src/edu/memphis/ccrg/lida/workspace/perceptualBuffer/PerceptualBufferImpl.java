package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.memphis.ccrg.lida._perception.GraphImpl;
import edu.memphis.ccrg.lida._perception.PAMContentImpl;
import edu.memphis.ccrg.lida._perception.interfaces.PAMContent;
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
	
	public PerceptualBufferImpl(){
		pamContent = new PAMContentImpl();
		perceptBuffer = new ArrayList<NodeStructure>();
		pbListeners = new ArrayList<PerceptualBufferListener>();
	}//public Workspace()
	
	public synchronized void receivePAMContent(PAMContent pc){
		pamContent = pc;
	}
	
	private synchronized void storePAMContent(){
		GraphImpl struct = (GraphImpl)pamContent.getContent();	
		
		if(struct != null){
			Set<Node> nodes = struct.getNodes();
			//System.out.println("in pbuffer there are " + nodes.size());			
			
			perceptBuffer.add(new GraphImpl(struct));			
		}
		if(perceptBuffer.size() > PERCEPT_BUFFER_CAPACITY){
			perceptBuffer.remove(0);
		}		
	}//public void storePAMContent()
	
	public void addPBufferListener(PerceptualBufferListener l){
		pbListeners.add(l);
	}
	
	public void sendContent(){
		storePAMContent();
		
		if(perceptBuffer.size() > 0){
			for(int i = 0; i < pbListeners.size(); i++){
				GraphImpl tempGraph = new GraphImpl((GraphImpl)perceptBuffer.get(0));
				//System.out.println(tempGraph.getNodes().size() + " nodes send from pam content");
				
				//p.print();
				PerceptualBufferContentImpl content = new PerceptualBufferContentImpl();
				content.addContent(tempGraph);
				pbListeners.get(i).receivePBufferContent(content);
				
			}//for
		}
			
	}//sendContent

	////O(BufferSize * PerceptSize * log(objectives.size()))
	public WorkspaceContent getCodeletsObjective(CodeletObjective objective) {
		PerceptualBufferContentImpl content = new PerceptualBufferContentImpl();
		
		synchronized(this){
			for(NodeStructure struct: perceptBuffer){
				Set<Node> nodes = struct.getNodes();
				if(nodes != null){
					
					for(Node n: nodes){
						//Node x = n;
						if(n != null){
							//System.out.println("giving to codelet " + n.getLabel());
							content.addNode(n);
						}
					}
				}//if nodes != null
			}//for each struct
		}
		
		return content;

	}//getCodeletsObjective

}//PerceptualBuffer
