/**
 * 
 */
package edu.memphis.ccrg.lida.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;



import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionImpl;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.attention.AttentionDriver;
import edu.memphis.ccrg.lida.declarativememory.DeclarativeMemory;
import edu.memphis.ccrg.lida.declarativememory.DeclarativeMemoryImpl;
import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.example.genericLIDA.environSensoryMem.VisionEnvironment;
import edu.memphis.ccrg.lida.example.genericLIDA.environSensoryMem.VisionSensoryMemory;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.perception.PAMDriver;
import edu.memphis.ccrg.lida.perception.PAMListener;
import edu.memphis.ccrg.lida.perception.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.perception.PerceptualAssociativeMemoryImpl;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemory;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryDriver;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryImpl;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryDriver;
import edu.memphis.ccrg.lida.sensorymotorautomatism.SensoryMotorAutomatism;
import edu.memphis.ccrg.lida.sensorymotorautomatism.SensoryMotorAutomatismImpl;
import edu.memphis.ccrg.lida.sensorymotorautomatism.SensoryMotorListener;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.transientepisodicmemory.CueListener;
import edu.memphis.ccrg.lida.transientepisodicmemory.TEMImpl;
import edu.memphis.ccrg.lida.transientepisodicmemory.TransientEpisodicMemory;
import edu.memphis.ccrg.lida.workspace.broadcastbuffer.BroadcastQueueImpl;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CSMDriver;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModelImpl;
import edu.memphis.ccrg.lida.workspace.episodicbuffer.EpisodicBufferImpl;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;
import edu.memphis.ccrg.lida.workspace.perceptualbuffer.PerceptualBufferImpl;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.SBCodeletDriver;

/**
 * @author Javier Snaider
 * 
 */
public class Lida {
	
	private Logger logger = Logger.getLogger("lida.framework.Lida");
	// Perception
	private Environment environment;
	private SensoryMotorAutomatism sma;
	private SensoryMemory sensoryMemory;
	private PerceptualAssociativeMemory pam;
	// Episodic memory
	private TransientEpisodicMemory tem;
	private DeclarativeMemory declarativeMemory;
	// Workspace
	private Workspace workspace;
	// Attention
	private GlobalWorkspace globalWksp;
	// Action Selection
	private ProceduralMemory procMem;
	private ActionSelection actionSelection;
	// A class that helps pause and control the drivers.
	private FrameworkTimer timer;
	private SBCodeletDriver sbCodeletDriver;
	private AttentionDriver attnDriver;

	/**
	 * List of drivers which run the major components of LIDA
	 */
	private List<ModuleDriver> drivers = new ArrayList<ModuleDriver>();

	public Lida() {
		logger.info("Starting Lida");
		initComponents();
		initDrivers();
		initListeners();
		start();
	}

	private void initComponents() {
		timer = new FrameworkTimer(true,150);
		environment = new VisionEnvironment(timer, 10, 10);
		sensoryMemory = new VisionSensoryMemory(environment);
		pam = new PerceptualAssociativeMemoryImpl();
		tem = new TEMImpl(new NodeStructureImpl()); // SEE!!!!!!
		declarativeMemory = new DeclarativeMemoryImpl();
		workspace = new WorkspaceImpl(new PerceptualBufferImpl(2),
				new EpisodicBufferImpl(10), new BroadcastQueueImpl(10),
				new CurrentSituationalModelImpl());
		globalWksp = new GlobalWorkspaceImpl();
		procMem = new ProceduralMemoryImpl();
		actionSelection = new ActionSelectionImpl();
		sma = new SensoryMotorAutomatismImpl();
		logger.info("Lida submodules Created");		
	}

	private void initDrivers() {
		ModuleDriver module;

		drivers.add((ModuleDriver) environment);

		module = new SensoryMemoryDriver(sensoryMemory, timer);
		drivers.add(module);
		module = new PAMDriver(pam, timer);
		drivers.add(module);
		module = new ProceduralMemoryDriver(procMem, timer);
		drivers.add(module);
		attnDriver = new AttentionDriver(timer, workspace.getCSM(), globalWksp);
		drivers.add(attnDriver);
		sbCodeletDriver = new SBCodeletDriver(workspace, timer);
		drivers.add(sbCodeletDriver);
		logger.info("Lida drivers Created");		

	}

	private void initListeners() {
		if (sensoryMemory instanceof SensoryMotorListener)
			sma.addSensoryMotorListener((SensoryMotorListener) sensoryMemory);

		if (workspace instanceof PAMListener)
			pam.addPAMListener((PAMListener) workspace);
		

//		addBufferListener(workspace);
//		//
//		episodicBuffer.addBufferListener(workspace);
//		episodicBuffer.addFrameworkGui(nodeLinkFlowGui);
//		//
//		broadcastQueue.addBufferListener(workspace);
//		broadcastQueue.addFrameworkGui(nodeLinkFlowGui);
//		//
//		csm.addBufferListener(workspace);
//		csm.addFrameworkGui(nodeLinkFlowGui);

		if (declarativeMemory instanceof CueListener)
			workspace.addCueListener((CueListener)declarativeMemory);
		
		if (tem instanceof CueListener)		
		workspace.addCueListener((CueListener)tem);
		
		//check
		//workspace.add_SBCodelet_WorkspaceListener((WorkspaceListener)sbCodeletDriver);

		globalWksp.addBroadcastListener(pam);
		globalWksp.addBroadcastListener((BroadcastListener)workspace);
		globalWksp.addBroadcastListener(tem);
		globalWksp.addBroadcastListener((BroadcastListener)attnDriver);
		globalWksp.addBroadcastListener(procMem);

		procMem.addProceduralMemoryListener((ProceduralMemoryListener)actionSelection);

		//actionSelection.addBehaviorListener(environment);
		actionSelection.addActionSelectionListener((ActionSelectionListener)workspace);
		logger.info("Lida listeners added");		

	}
	public void start(){
		drivers.add((VisionEnvironment)environment);
		timer.setInitialTasks(drivers);
		globalWksp.start(); // change to the ThreadSpawner
		logger.info("Lida submodules Started");		
	}

	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}

	/**
	 * @return the sma
	 */
	public SensoryMotorAutomatism getSma() {
		return sma;
	}

	/**
	 * @return the sensoryMemory
	 */
	public SensoryMemory getSensoryMemory() {
		return sensoryMemory;
	}

	/**
	 * @return the pam
	 */
	public PerceptualAssociativeMemory getPam() {
		return pam;
	}

	/**
	 * @return the tem
	 */
	public TransientEpisodicMemory getTem() {
		return tem;
	}

	/**
	 * @return the declarativeMemory
	 */
	public DeclarativeMemory getDeclarativeMemory() {
		return declarativeMemory;
	}

	/**
	 * @return the workspace
	 */
	public Workspace getWorkspace() {
		return workspace;
	}

	/**
	 * @return the globalWksp
	 */
	public GlobalWorkspace getGlobalWksp() {
		return globalWksp;
	}

	/**
	 * @return the procMem
	 */
	public ProceduralMemory getProcMem() {
		return procMem;
	}

	/**
	 * @return the actionSelection
	 */
	public ActionSelection getActionSelection() {
		return actionSelection;
	}

	/**
	 * @return the timer
	 */
	public FrameworkTimer getTimer() {
		return timer;
	}

	/**
	 * @return the sbCodeletDriver
	 */
	public SBCodeletDriver getSbCodeletDriver() {
		return sbCodeletDriver;
	}
}
