package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleType;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;

/**
 * 
 * @author Ryan J McCall, Javier Snaider
 */
public class ProceduralMemoryDriver extends ModuleDriverImpl {

	private ProceduralMemory proceduralMemory;
	private final int DEFAULT_SCHEME_ACTIVATION_RATE = 10;
	private int schemeActivationRate = DEFAULT_SCHEME_ACTIVATION_RATE;
	private int activateCounter = 0;

	public ProceduralMemoryDriver(ProceduralMemory pm, int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, tm);
		proceduralMemory = pm;
	}

	/**
	 * 
	 */
	public void runThisDriver() {
		if(activateCounter > schemeActivationRate){
			proceduralMemory.activateSchemesWithBroadcast();
			activateCounter = 0;
		}else
			activateCounter++;
	}// method

	//TODO: Should we inherit these methods instead?  Should they even be in the module driver interface?
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private List<Object> guiContent = new ArrayList<Object>();
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		// TODO Auto-generated method stub
		
	}
	public void sendEvent(FrameworkGuiEvent evt) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String toString() {
		return ModuleType.ProceduralDriver + "";
	}

}// class