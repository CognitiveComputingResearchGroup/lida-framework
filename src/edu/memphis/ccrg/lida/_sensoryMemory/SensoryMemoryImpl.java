package edu.memphis.ccrg.lida._sensoryMemory;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.environment.EnvironmentListener;
import edu.memphis.ccrg.lida.wumpusWorld.environment.EnvironmentContentImpl;

public class SensoryMemoryImpl implements SensoryMemory, EnvironmentListener{
	
	private EnvironmentContentImpl simContent = null;
	private SensoryContentImpl senseContent = new SensoryContentImpl();
	private List<SensoryListener> listeners = new ArrayList<SensoryListener>();
	
	public synchronized void receiveSimContent(EnvironmentContentImpl sc){//SimulationListener
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
