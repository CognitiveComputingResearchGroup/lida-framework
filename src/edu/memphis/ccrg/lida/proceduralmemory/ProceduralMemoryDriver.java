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

	public ProceduralMemoryDriver(ProceduralMemory pm, int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, tm);
	//	procMem = pm;
	}// constructor

	/**
	 * This loop drives the procedural memory
	 */
	public void runThisDriver() {
		//procMem.sendEvent();   // Change for Logger 
	}// method

	@Override
	protected void processResults(LidaTask task) {
		// TODO Auto-generated method stub
		
	}

}// class