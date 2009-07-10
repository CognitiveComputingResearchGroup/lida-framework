package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class PAMDriver extends GenericModuleDriver{

	private PerceptualAssociativeMemoryImpl pam;
	
	public PAMDriver(PerceptualAssociativeMemoryImpl pam, FrameworkTimer timer){
		super(timer);
		this.pam = pam;
	}//constructor
		
	@Override
	public void cycleStep() {
		pam.detectSensoryMemoryContent();				
		pam.propogateActivation();//Pass activation	
		pam.sendOutPercept(); //Send the percept to p-Workspace
		pam.sendGuiContent();
		pam.decayPAM();  //Decay the activations			
	}
	
}//class PAMDriver