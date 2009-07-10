package edu.memphis.ccrg.lida.proceduralMemory;

import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

/**
 * 
 * @author ryanjmccall
 */
public class ProceduralMemoryDriver extends GenericModuleDriver {

	private ProceduralMemoryImpl procMem;

	public ProceduralMemoryDriver(ProceduralMemoryImpl pm, FrameworkTimer timer) {
		super(timer);
		procMem = pm;
	}// constructor

	/**
	 * This loop drives the procedural memory
	 */
	public void cycleStep() {
		procMem.sendGuiContent();
	}// method

}// class