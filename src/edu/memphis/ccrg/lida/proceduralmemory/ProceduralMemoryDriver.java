package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

/**
 * 
 * @author ryanjmccall
 */
public class ProceduralMemoryDriver extends ModuleDriverImpl {

	private ProceduralMemory procMem;

	public ProceduralMemoryDriver(ProceduralMemory pm, LidaTaskManager timer, int ticksPerCycle) {
		super(timer, ticksPerCycle);
		procMem = pm;
	}// constructor

	/**
	 * This loop drives the procedural memory
	 */
	public void runDriverOneProcessingStep() {
		//procMem.sendEvent();   // Change for Logger 
	}// method

}// class