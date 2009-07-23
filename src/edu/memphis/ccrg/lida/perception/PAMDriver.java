package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.framework.FrameworkThreadManager;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class PAMDriver extends GenericModuleDriver{

	private PerceptualAssociativeMemory pam;
	
	public PAMDriver(PerceptualAssociativeMemory pam, FrameworkThreadManager timer){
		super(timer);
		this.pam = pam;
	}//constructor
		
	@Override
	public void cycleStep() {
		pam.detectSensoryMemoryContent();				
		pam.propogateActivation();//Pass activation	
		pam.sendOutPercept(); //Send the percept to p-Workspace
		if (pam instanceof PerceptualAssociativeMemoryImpl){
			((PerceptualAssociativeMemoryImpl)pam).sendEvent();
		}
		pam.decayPAM();  //Decay the activations			
	}
	
}//class PAMDriver