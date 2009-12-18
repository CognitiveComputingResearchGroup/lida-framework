package edu.memphis.ccrg.lida.pam;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;

/**
 * PamDriver run the PAM
 * @author ryanjmccall
 *
 */
public class PamDriver extends ModuleDriverImpl implements GuiEventProvider {
	
	@SuppressWarnings("unused")
	private Logger logger = Logger.getLogger("lida.pam.PamDriver");
	
	/**
	 * Counter for the decay 
	 */
	private int decayCounter = 0;
	
	/**
	 * Determines how often the Pam is decayed by this Driver.
	 */
	private int decayTime = DEFAULT_DECAY_TIME;
	
	/**
	 * Parameter for how ofen pam is decayed.
	 */
	private static final int DEFAULT_DECAY_TIME = 5;
	
	/**
	 * 
	 */
	private PerceptualAssociativeMemory pam;
	
	public PamDriver(PerceptualAssociativeMemory pam, int ticksPerCycle,
			LidaTaskManager tm) {
		super(ticksPerCycle, tm);
		this.pam = pam;
	}// constructor

	@Override
	protected void runThisDriver() {
		decayCounter++;
		if (decayCounter >= decayTime) {
			pam.decayPam(); // Decay the activations
			decayCounter = 0;
		}
	}
	
	/**
	 * @param decayTime
	 *            the decayTime to set
	 */
	public void setDecayTime(int decayTime) {
		this.decayTime = decayTime;
	}



	@Override
	public String toString() {
		return ModuleName.PamDriver + "";
	}

}// class 