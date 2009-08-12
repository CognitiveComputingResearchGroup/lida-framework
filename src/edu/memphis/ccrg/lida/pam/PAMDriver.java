package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public class PAMDriver extends ModuleDriverImpl{

	private PerceptualAssociativeMemory pam;
	
	public PAMDriver(PerceptualAssociativeMemory pam, LidaTaskManager taskManager, int ticksPerCycle){
		super(taskManager, ticksPerCycle);
		this.pam = pam;
	}//constructor
		

	public void runDriverOneProcessingStep() {
		pam.detectSensoryMemoryContent();				
		pam.propogateActivation();//Pass activation	
		pam.sendOutPercept(); //Send the percept to p-Workspace
		if (pam instanceof PerceptualAssociativeMemoryImpl){
			((PerceptualAssociativeMemoryImpl)pam).sendEvent();
		}
		pam.decayPam();  //Decay the activations			
	}
	
}//class PAMDriver