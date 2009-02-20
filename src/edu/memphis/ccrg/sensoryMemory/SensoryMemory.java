package edu.memphis.ccrg.sensoryMemory;

import java.util.ArrayList;
import java.util.List;

public class SensoryMemory implements SimulationListener{
	
	private int size;
	private SimulationContent simContent;
	private SensoryContent senseContent;
	private List<SensoryListener> listeners;
	
		
	public SensoryMemory(int size){
		this.size = size;
		simContent = new SimulationContent(size);
		senseContent = new SensoryContent(size);	
		listeners = new ArrayList<SensoryListener>();
	}//SensoryMemory
	
	public synchronized void receiveSimContent(SimulationContent sc){//SimulationListener
		simContent = sc;		
	}	
	
	public void processSimContent(){
		int[] src = new int[size];
		int[] dest = new int[size];
		synchronized(this){
			src = (int[])simContent.getContent();
			System.arraycopy(src, 0, dest, 0, size);
		}
		//do processing		
		senseContent.setContent(dest);		
	}//
	
	//broadcast to all listeners
	public void sendSensoryContent(){
		for(int i = 0; i < listeners.size(); i++)
			(listeners.get(i)).receiveSense(senseContent);
	}
	
	
	
	public void addBroadcastListener(SensoryListener sl){
		listeners.add(sl);
	}
	
}//class SensoryMemory
