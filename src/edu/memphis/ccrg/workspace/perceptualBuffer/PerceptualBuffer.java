package edu.memphis.ccrg.workspace.perceptualBuffer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import edu.memphis.ccrg.perception.PAMContent;
import edu.memphis.ccrg.perception.PAMListener;
import edu.memphis.ccrg.perception.Percept;

public class PerceptualBuffer implements Runnable, PAMListener, PerceptualBufferInterface{
	
	private PAMContent pamContent;	
	private Queue<Percept> perceptBuffer;
	private ArrayList<PBufferListener> pbListeners;	
	private final int PERCEPT_BUFFER_CAPACITY = 2;	
	private boolean keepRunning;
	
	public PerceptualBuffer(){
		pamContent = new PAMContent();
		perceptBuffer = new LinkedList<Percept>();
		pbListeners = new ArrayList<PBufferListener>();
		keepRunning = true;
	}//public Workspace()
	
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void stopRunning(){
		keepRunning = false;
	}
	
	public synchronized void receivePAMContent(PAMContent pc){
		pamContent = pc;
	}
	
	private void storePAMContent(){
		Percept current = null;
		synchronized(this){
    		current = (Percept)pamContent.getContent();
    	}
		if(!current.equals(null)){
			perceptBuffer.add(current);			
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
			Percept p = new Percept(perceptBuffer.peek());
			PBufferContent content = new PBufferContent(p);
			pbListeners.get(i).receivePBufferContent(content);
			
		}//for
			
	}//sendContent

}//PerceptualBuffer
