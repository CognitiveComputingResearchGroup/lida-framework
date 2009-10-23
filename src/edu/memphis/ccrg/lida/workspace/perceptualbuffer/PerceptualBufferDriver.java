package edu.memphis.ccrg.lida.workspace.perceptualbuffer;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;

/**
 * PerceptualBufferDriver is not currently being used. Considered for removal.
 * @author ryanjmccall
 *
 */
public class PerceptualBufferDriver extends ModuleDriverImpl {

	private PerceptualBuffer pb;

	public PerceptualBufferDriver(PerceptualBuffer pb, int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, tm);
		this.pb = pb;
	}// constructor

	public void runThisDriver() {
		pb.cueEpisodicMemory();
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		// TODO Auto-generated method stub
		
	}


	public void sendEvent(FrameworkGuiEvent evt) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString() {
		return Module.PerceptualBufferDriver + "";
	}

}// class