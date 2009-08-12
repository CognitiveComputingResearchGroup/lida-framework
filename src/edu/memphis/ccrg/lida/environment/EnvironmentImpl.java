package edu.memphis.ccrg.lida.environment;

import edu.memphis.ccrg.lida.actionselection.ActionContent;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public class EnvironmentImpl extends ModuleDriverImpl implements Environment{

	public EnvironmentImpl(LidaTaskManager timer, int ticksPerStep) {
		super(timer, ticksPerStep);
	}
	
	public void runDriverOneProcessingStep() {
		// TODO override this in specific environment
	}

	//Environ
	public void receiveBehaviorContent(ActionContent content) {
		// TODO override this in specific environment
	}

	public void resetEnvironment() {
		// TODO override this in specific environment
	}

}//class
