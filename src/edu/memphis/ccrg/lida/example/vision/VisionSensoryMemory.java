package edu.memphis.ccrg.lida.example.vision;

import edu.memphis.ccrg.lida.environment.EnvironmentContent;
import edu.memphis.ccrg.lida.environment.EnvironmentListener;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryListener;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemory;
import edu.memphis.ccrg.lida.util.Printer;

public class VisionSensoryMemory implements SensoryMemory, EnvironmentListener{
	
	private VisionEnvironmentContent content = new VisionEnvironmentContent(); 
	
	public synchronized void receiveEnvironmentContent(EnvironmentContent ec){
		content = (VisionEnvironmentContent) ec;
	}	
	
	public void processSimContent(){
		double[][] mat = (double[][])content.getContent();
		Printer p = new Printer();
		p.print(mat);
	}

	public void sendSensoryContent(){
		
	}

	public void addSensoryListener(SensoryListener sml) {
		
	}
	
}//class