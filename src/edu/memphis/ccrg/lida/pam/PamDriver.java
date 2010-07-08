package edu.memphis.ccrg.lida.pam;

import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * PamDriver run the PAM
 * @author Ryan J McCall
 *
 */
public class PamDriver extends ModuleDriverImpl implements GuiEventProvider {
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger("lida.pam.PamDriver");
	
	/**
	 * Counter for the decay 
	 */
	private int decayCounter = 0;
	
	/**
	 * Determines how often the Pam is decayed by this Driver.
	 */
	private int decayTime = DEFAULT_DECAY_TIME;
	
	/**
	 * Parameter for how often pam is decayed.
	 */
	private static final int DEFAULT_DECAY_TIME = 10;
	
	private PerceptualAssociativeMemory pam;
	
	public PamDriver(PerceptualAssociativeMemory pam, int ticksPerCycle,
			LidaTaskManager tm) {
		super(ticksPerCycle, tm,ModuleName.PamDriver);
		this.pam = pam;
	}// constructor

	public PamDriver() {
		super(DEFAULT_TICKS_PER_CYCLE,ModuleName.PamDriver);
	}// constructor

	@Override
	protected void runThisDriver() {
	//	System.out.println("PamDriver lives!");
		decayCounter++;
		if (decayCounter >= decayTime) {
//			PamNodeStructure pm = pam.getNodeStructure();
//			System.out.println("\n upscale " + pm.getUpscale());
//			System.out.println("downscale " + pm.getDownscale());
//			System.out.println("selectivity " + pm.getSelectivity());
			//pam.decayPam(); // Decay the activations TODO: delete this class
			decayCounter = 0;
		}
	}//
	
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

	@Override
	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof PerceptualAssociativeMemory
					&& module.getModuleName() == ModuleName.PerceptualAssociativeMemory) {
				pam = (PerceptualAssociativeMemory) module;
			}
		}
	}
}// class 