package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import edu.memphis.ccrg.lida.perception.PAMContentImpl;
import edu.memphis.ccrg.lida.perception.Percept;
import edu.memphis.ccrg.lida.perception.interfaces.PAMListener;
import edu.memphis.ccrg.lida.workspace.sbCodelets.CodeletAccessible;
import edu.memphis.ccrg.lida.workspace.sbCodelets.CodeletObjective;
import edu.memphis.ccrg.lida.workspace.sbCodelets.WorkspaceContent;

public class PerceptualBuffer implements PAMListener, PerceptualBufferInterface, CodeletAccessible{
	
	private PAMContentImpl pamContent;	
	private List<Percept> perceptBuffer;
	private List<PerceptualBufferListener> pbListeners;	
	private final int PERCEPT_BUFFER_CAPACITY = 2;	
	
	public PerceptualBuffer(){
		pamContent = new PAMContentImpl();
		perceptBuffer = new LinkedList<Percept>();
		pbListeners = new ArrayList<PerceptualBufferListener>();
	}//public Workspace()
	
	public synchronized void receivePAMContent(PAMContentImpl pc){
		pamContent = pc;
	}
	
	private void storePAMContent(){
		Percept current = null;
		synchronized(this){
    		current = (Percept)pamContent.getContent();
    	}
				
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

	public PerceptualBufferContentImpl getObjective(CodeletObjective objective) {
		// TODO Auto-generated method stub
		return null;
	}

	public WorkspaceContent getCodeletsObjective(CodeletObjective objective) {
		// TODO Auto-generated method stub
		return null;
	}

}//PerceptualBuffer
