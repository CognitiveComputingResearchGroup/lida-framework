package edu.memphis.ccrg.lida.sensoryMemory;

import java.util.ArrayList;
import java.util.List;

public class SensoryMemory implements SimulationListener{
	
	private SimulationContent simContent = null;
	private SensoryContent senseContent = new SensoryContent();
	private List<SensoryListener> listeners = new ArrayList<SensoryListener>();
	
	public synchronized void receiveSimContent(SimulationContent sc){//SimulationListener
		simContent = sc;		
	}	
	
	public void processSimContent(){
		
		char[][][] src = null;
		char[][][] dest = null;
		
		if(simContent != null){
			int worldSize = -1;
			synchronized(this){
				worldSize = simContent.getSize();
			}
			
			if(worldSize != -1){
				src = new char[worldSize][worldSize][4];	
				dest = new char[worldSize][worldSize][4];
			}
			
			synchronized(this){
				src = (char[][][])simContent.getSenseContent();
				System.arraycopy(src, 0, dest, 0, worldSize);//TODO: WRY??
			}
		}
		//do processing		
		senseContent.setContent(dest);		
	}//
	
	public void addSensoryListener(SensoryListener sl){
		listeners.add(sl);
	}
	
	//broadcast to all listeners
	public void sendSensoryContent(boolean print){
		if(print)
			senseContent.print();
		for(int i = 0; i < listeners.size(); i++)
			(listeners.get(i)).receiveSense(senseContent);
	}
	
}//class SensoryMemory
