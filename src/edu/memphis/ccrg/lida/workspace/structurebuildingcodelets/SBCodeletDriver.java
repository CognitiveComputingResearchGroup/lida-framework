package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.Workspace;

public class SBCodeletDriver extends ModuleDriverImpl implements GuiContentProvider {

	private Logger logger=Logger.getLogger("lida.workspace.structurebuildingcodelets.SBCodeletDriver");
	private SBCodeletFactory sbCodeletFactory;
	//Gui
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();

	public SBCodeletDriver(Workspace w, LidaTaskManager timer, int ticksPerCycle) {
		super(timer, ticksPerCycle);
		setNumberOfTicksPerStep(10);//TODO: check
		sbCodeletFactory = SBCodeletFactory.getInstance(w, timer);
	}// method
	
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}
	
	public void runDriverOneProcessingStep() {
		activateCodelets();
		//sendEvent();
	}

	/**
	 * if BufferContent activates a sbCodelet's context, start a new codelet
	 */
	private void activateCodelets() {
		// TODO: CODE to determine when/what codelets to activate
	}// method

	public void sendEvent() {
		if (!guis.isEmpty()) {
			FrameworkGuiEvent event = new FrameworkGuiEvent(
					Module.structureBuildingCodelets, "data", getRunningTasks());
			for (FrameworkGuiEventListener gui : guis) {
				gui.receiveGuiEvent(event);
			}
		}
	}//method

	/*
	 * @param type - See SBCodeletFactory for which integer values correspond to
	 * which type
	 */
	@SuppressWarnings("unused")
	private void spawnNewCodelet(int type, double activation, NodeStructure context, 
								 							  CodeletAction actions){
		StructureBuildingCodelet sbc = sbCodeletFactory.getCodelet(type, activation, 
																   context, actions);
		addTask(sbc);
		logger.log(Level.FINER,"New codelet {0} spawned",sbc);
	}// method

}// class