package edu.memphis.ccrg.lida.pam;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleType;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;

public class PamDriver extends ModuleDriverImpl implements GuiEventProvider {

	private static final int DEFAULT_DECAY_TIME=10; 
	private PerceptualAssociativeMemory pam;
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger("lida.pam.PamDriver");
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private int decayCounter = 0;
	private int decayTime=DEFAULT_DECAY_TIME;

	/**
	 * @param decayTime
	 *            the decayTime to set
	 */
	public void setDecayTime(int decayTime) {
		this.decayTime = decayTime;
	}

	public PamDriver(PerceptualAssociativeMemory pam, int ticksPerCycle,
			LidaTaskManager tm) {
		super(ticksPerCycle, tm);
		this.pam = pam;
	}// constructor

	@Override
	protected void runThisDriver() {
		// FrameworkGuiEvent event = new
		// TaskCountEvent(ModuleType.PerceptualAssociativeMemory,
		// getSpawnedTaskCount());
		// sendEvent(event);
		decayCounter++;
		if (decayCounter >= decayTime) {
			pam.decayPam(); // Decay the activations
			decayCounter = 0;
		}
	}

	// **************GUI***************
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}

	public void sendEvent(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener gui : guis)
			gui.receiveGuiEvent(evt);
	}// method

	@Override
	public String toString() {
		return ModuleType.PamDriver + "";
	}

}// class 