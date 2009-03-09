package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.memphis.ccrg.lida.perception.PAMContent;
import edu.memphis.ccrg.lida.perception.PAMListener;
import edu.memphis.ccrg.lida.perception.Percept;

public class PerceptualBuffer implements PAMListener, PerceptualBufferInterface{
	
	private PAMContent pamContent;	
	private List<Percept> perceptBuffer;
	private List<PBufferListener> pbListeners;	
	private final int PERCEPT_BUFFER_CAPACITY = 2;	
	
	public PerceptualBuffer(){
		pamContent = new PAMContent();
		perceptBuffer = new LinkedList<Percept>();
		pbListeners = new ArrayList<PBufferListener>();
	}//public Workspace()
	
	public synchronized void receivePAMContent(PAMContent pc){
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
	
	public void addPBufferListener(PBufferListener l){
		pbListeners.add(l);
	}
	
	public void sendContent(){
		storePAMContent();
		for(int i = 0; i < pbListeners.size(); i++){
			Percept p = new Percept(perceptBuffer.get(0));
			p.print();
			PBufferContent content = new PBufferContent(p);
			pbListeners.get(i).receivePBufferContent(content);
			
		}//for
			
	}//sendContent

}//PerceptualBuffer
