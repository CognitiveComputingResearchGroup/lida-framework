package edu.memphis.ccrg.lida.declarativememory;

import edu.memphis.ccrg.lida.transientepisodicmemory.CueListener;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public class DeclarativeMemoryImpl implements DeclarativeMemory, CueListener{

    // This method is called continually.  The rate at which is called 
	// can be modified by changing the 'ticksPerCycle' parameter of PerceptualBufferDriver.
	// This is set in the Lida Class.  The higher the value for 'tickPerCycle' the slower
	// the rate of cueing will be.
	public synchronized void receiveCue(NodeStructure cue) {
	
	}

}
