package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.memphis.ccrg.lida.perception.PAMContentImpl;
import edu.memphis.ccrg.lida.perception.Percept;
import edu.memphis.ccrg.lida.perception.interfaces.PAMListener;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.workspace.sbCodelets.CodeletAccessible;
import edu.memphis.ccrg.lida.workspace.sbCodelets.CodeletObjective;
import edu.memphis.ccrg.lida.workspace.sbCodelets.WorkspaceContent;

public class PerceptualBufferImpl implements PAMListener, PerceptualBuffer, CodeletAccessible{
	
	private PAMContentImpl pamContent;	
	private List<Percept> perceptBuffer;
	private List<PerceptualBufferListener> pbListeners;	
	private final int PERCEPT_BUFFER_CAPACITY = 2;	
	
	public PerceptualBufferImpl(){
		pamContent = new PAMContentImpl();
		perceptBuffer = new LinkedList<Percept>();
		pbListeners = new ArrayList<PerceptualBufferListener>();
	}//public Workspace()
	
	public synchronized void receivePAMContent(PAMContentImpl pc){
		pamContent = pc;
	}
	
	private synchronized void storePAMContent(){
		Percept current = (Percept)pamContent.getContent();				
		if(!current.equals(null)){
			perceptBuffer.add(new Percept(current));			
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
		for(int i = 0; i < pbListeners.size(); i++){
			Percept p = new Percept(perceptBuffer.get(0));
			//p.print();
			PerceptualBufferContentImpl content = new PerceptualBufferContentImpl(p);
			pbListeners.get(i).receivePBufferContent(content);
			
		}//for
			
	}//sendContent

	public WorkspaceContent getCodeletsObjective(CodeletObjective objective) {
		PerceptualBufferContentImpl content = new PerceptualBufferContentImpl();
		
		Set<Node> objectives = objective.getNodeObjectives();
		
		synchronized(this){
		for(Percept p: perceptBuffer)
			for(Node n: p)
				if(objectives.contains(n))
					content.addNode(n);
		}
	
		//O(BufferSize * PerceptSize * log(objectives.size()))
		
		return content;

	}//getCodeletsObjective

}//PerceptualBuffer
