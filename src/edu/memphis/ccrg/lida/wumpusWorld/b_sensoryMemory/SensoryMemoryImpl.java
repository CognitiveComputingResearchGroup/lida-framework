//package edu.memphis.ccrg.lida.wumpusWorld.b_sensoryMemory;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import edu.memphis.ccrg.lida.environment.EnvironmentContent;
//import edu.memphis.ccrg.lida.environment.EnvironmentListener;
//import edu.memphis.ccrg.lida.sensoryMemory.SensoryContentImpl;
//import edu.memphis.ccrg.lida.sensoryMemory.SensoryListener;
//import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemory;
//import edu.memphis.ccrg.lida.wumpusWorld.a_environment.EnvironmentContentImpl;
//
//public class SensoryMemoryImpl implements SensoryMemory, EnvironmentListener{
//	
//	private EnvironmentContentImpl simContent = null;
//	private SensoryContentImpl senseContent = new SensoryContentImpl();
//	private List<SensoryListener> listeners = new ArrayList<SensoryListener>();
//	
//	public synchronized void receiveSimContent(EnvironmentContent sc){//SimulationListener
//		simContent = (EnvironmentContentImpl) sc;		
//	}	
//	
//	public void processSimContent(){
//		
//		char[][][] src = null;
//		char[][][] dest = null;
//		
//		if(simContent != null){
//			int worldSize = -1;
//			worldSize = simContent.getSize();
//	
//			
//			if(worldSize != -1){
//				src = new char[worldSize][worldSize][4];	
//				dest = new char[worldSize][worldSize][4];
//			}
//	
//			src = (char[][][])simContent.getContent();
//			System.arraycopy(src, 0, dest, 0, worldSize);//TODO: WRY??
//
//		}
//		//do processing		
//		senseContent.setContent(dest);		
//	}//
//	
//	public void addSensoryListener(SensoryListener sl){
//		listeners.add(sl);
//	}
//	
//	//broadcast to all listeners
//	public void sendSensoryContent(){
//		for(int i = 0; i < listeners.size(); i++)
//			(listeners.get(i)).receiveSense(senseContent);
//	}//method
//	
//}//class SensoryMemory
