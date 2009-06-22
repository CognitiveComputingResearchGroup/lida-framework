package edu.memphis.ccrg.lida.example.vision.environ_SM;

import java.util.ArrayList;
import java.util.List;
import edu.memphis.ccrg.lida.environment.EnvironmentContent;
import edu.memphis.ccrg.lida.environment.EnvironmentListener;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryContent;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryContentImpl;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryListener;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemory;

public class VisionSensoryMemory implements SensoryMemory, EnvironmentListener{
	
	private VisionEnvironmentContent environmentContent = new VisionEnvironmentContent(); 
	private SensoryMemoryContent sensoryContent = new VisionSensoryContent();
	private List<SensoryMemoryListener> listeners = new ArrayList<SensoryMemoryListener>();
	
	public void addSensoryListener(SensoryMemoryListener listener) {
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
		for(SensoryMemoryListener l: listeners)
			l.receiveSense(sensoryContent);
	}
	
}//class