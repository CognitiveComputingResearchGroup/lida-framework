package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public class PamDriver extends ModuleDriverImpl{

	private PerceptualAssociativeMemory pam;
	
	public PamDriver(PerceptualAssociativeMemory pam, LidaTaskManager taskManager, int ticksPerCycle){
		super(taskManager, ticksPerCycle);
		this.pam = pam;
	}//constructor
		
	public void runSingleProcessingStep() {				
		if (pam instanceof PerceptualAssociativeMemoryImpl){
			((PerceptualAssociativeMemoryImpl)pam).sendEvent();
		}
		pam.decayPam();  //Decay the activations			
	}
	
}//class 