package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

/**
 * 
 * @author ryanjmccall
 */
public class ProceduralMemoryDriver extends ModuleDriverImpl {

	//private ProceduralMemory procMem;

	public ProceduralMemoryDriver(ProceduralMemory pm, int ticksPerCycle) {
		super(ticksPerCycle);
	//	procMem = pm;
	}// constructor

	/**
	 * This loop drives the procedural memory
	 */
	public void runThisLidaTask() {
		//procMem.sendEvent();   // Change for Logger 
	}// method

	@Override
	protected void processResults(LidaTask task) {
		// TODO Auto-generated method stub
		
	}

}// class