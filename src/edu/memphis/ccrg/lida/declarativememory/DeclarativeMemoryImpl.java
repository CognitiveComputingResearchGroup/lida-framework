package edu.memphis.ccrg.lida.declarativememory;

import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryListener;
import edu.memphis.ccrg.lida.transientepisodicmemory.CueListener;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public class DeclarativeMemoryImpl extends LidaModuleImpl implements DeclarativeMemory, CueListener{

    public DeclarativeMemoryImpl() {
		super(ModuleName.DeclarativeMemory);
		// TODO Auto-generated constructor stub
	}

	// This method is called continually.  The rate at which is called 
	// can be modified by changing the 'ticksPerCycle' parameter of PerceptualBufferDriver.
	// This is set in the Lida Class.  The higher the value for 'tickPerCycle' the slower
	// the rate of cueing will be.
	public synchronized void receiveCue(NodeStructure cue) {
	
	}

	public Object getModuleContent() {
		// TODO Auto-generated method stub
		return null;
	}
	public void addListener(ModuleListener listener) {
	}

}
