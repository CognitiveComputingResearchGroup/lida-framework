package edu.memphis.ccrg.lida.example.vision;

import java.util.ArrayList;
import java.util.List;
import edu.memphis.ccrg.lida.environment.EnvironmentContent;
import edu.memphis.ccrg.lida.environment.EnvironmentListener;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryContentImpl;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryListener;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemory;

public class VisionSensoryMemory implements SensoryMemory, EnvironmentListener{
	
	private VisionEnvironmentContent environmentContent = new VisionEnvironmentContent(); 
	private SensoryContent sensoryContent = new SensoryContentImpl();
	private List<SensoryListener> listeners = new ArrayList<SensoryListener>();
	
	public void addSensoryListener(SensoryListener listener) {
		listeners.add(listener);
	}
	
	public synchronized void receiveEnvironmentContent(EnvironmentContent ec){
		environmentContent = (VisionEnvironmentContent) ec;
	}	
	
	public void processSimContent(){
		double[][] mat = (double[][])environmentContent.getContent();
		sensoryContent.setContent(mat);
	}

	public void sendSensoryContent(){
		for(SensoryListener l: listeners)
			l.receiveSense(sensoryContent);
	}
	
}//class