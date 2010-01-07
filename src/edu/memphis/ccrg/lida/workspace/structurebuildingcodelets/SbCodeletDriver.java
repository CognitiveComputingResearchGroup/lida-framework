package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.Workspace;

public class SbCodeletDriver extends ModuleDriverImpl implements GuiEventProvider {

	private static Logger logger=Logger.getLogger("lida.workspace.structurebuildingcodelets.SBCodeletDriver");
	private SbCodeletFactory sbCodeletFactory;
	private Workspace workspace;
	
	public SbCodeletDriver(Workspace w, int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, (LidaTaskManager) tm,ModuleName.SbCodeletDriver);
		sbCodeletFactory = SbCodeletFactory.getInstance();
	}// method

	public SbCodeletDriver() {
		super();
	}// method

	public void runThisDriver() {
		activateCodelets();
		//sendEvent();
	}

	/**
	 * if BufferContent activates a sbCodelet's context, start a new codelet
	 */
	private void activateCodelets() {
		// TODO: CODE to determine when/what codelets to activate
	}// method

	//**************GUI***************
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}	
	public void sendEvent(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener gui : guis)
			gui.receiveGuiEvent(evt);
	}//method

	/*
	 * @param type - See SBCodeletFactory for which integer values correspond to
	 * which type
	 */
	@SuppressWarnings("unused")
	private void spawnNewCodelet(int type, double activation, 
								 NodeStructure context, CodeletAction actions){
		//TODO: use factory?
		StructureBuildingCodelet sbc = null;//sbCodeletFactory.getCodelet(type, activation, context, actions);
		this.addTask(sbc);
		logger.log(Level.FINER,"New codelet "+sbc+"spawned",LidaTaskManager.getActualTick());
	}// method

	@Override
	public String toString() {
		return ModuleName.StructureBuildingCodelets + "";
	}
	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof Workspace
					&& module.getModuleName() == ModuleName.Workspace) {
				workspace = (Workspace) module;
			}
		}
	}

}// class