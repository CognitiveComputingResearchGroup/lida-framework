package edu.memphis.ccrg.lida.pam;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.gui.events.TaskCountEvent;

public class PamDriver extends ModuleDriverImpl implements GuiEventProvider{

	private PerceptualAssociativeMemory pam;
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger("lida.pam.PamDriver");
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	
	public PamDriver(PerceptualAssociativeMemory pam, int ticksPerCycle, LidaTaskManager tm){
		super(ticksPerCycle, tm);
		this.pam = pam;
	}//constructor
	
	@Override
	protected void runThisDriver() {
	//	FrameworkGuiEvent event = new TaskCountEvent(Module.PerceptualAssociativeMemory, 
	//												 getSpawnedTaskCount());
	//	sendEvent(event);
		pam.decayPam();  //Decay the activations	
	}
	
	//**************GUI***************
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}	
	public void sendEvent(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener gui: guis)
			gui.receiveGuiEvent(evt);
	}//method

	@Override
	public String toString() {
		return Module.PamDriver + "";
	}

}//class 