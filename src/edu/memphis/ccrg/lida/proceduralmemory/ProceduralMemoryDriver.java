package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;

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

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		// TODO Auto-generated method stub
		
	}

	public void sendEvent(FrameworkGuiEvent evt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return Module.ProceduralDriver + "";
	}

}// class