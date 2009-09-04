package edu.memphis.ccrg.lida.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Javier Snaider
 */
public class Lida {
	
	private Logger logger = Logger.getLogger("lida.framework.Lida");
	// Perception
//	private EnvironmentImpl environment;
//	private SensoryMotorMemory sensoryMotorMemory;
//	private SensoryMemory sensoryMemory;
//	private PerceptualAssociativeMemory pam;
//	// Episodic memory
//	private TransientEpisodicMemory tem;
//	private DeclarativeMemory declarativeMemory;
//	// Workspace
//	private Workspace workspace;
//	private SBCodeletDriver sbCodeletDriver;
//	// Attention
//	private AttentionDriver attentionDriver;
//	private GlobalWorkspace globalWksp;
//	// Action Selection
//	private ProceduralMemory proceduralMemory;
//	private ActionSelection actionSelection;
//	// A class that helps pause and control the drivers.
//	private LidaTaskManager taskManager;

	/**
	 * List of drivers which run the major components of LIDA
	 */
//	private List<ModuleDriver> drivers = new ArrayList<ModuleDriver>();

//	public Lida(LidaTaskManager ft, EnvironmentImpl e, SensoryMemory sm, Map<Module, String> configFilesMap) {
//		logger.info("Starting Lida");
//		initComponents(ft, e, sm, configFilesMap);
//		initDrivers();
//		initListeners();
//		start();
//	}

//	private void initComponents(LidaTaskManager tm, EnvironmentImpl e, SensoryMemory sm, Map<Module, String> configFilesMap) {
//		taskManager = tm;
//		environment = e;
//		sensoryMemory = sm;
//
//		pam = new PerceptualAssociativeMemoryImpl();
//		String path = configFilesMap.get(Module.perceptualAssociativeMemory);
//		if(path != null){
//			PamConfigReader pamInput = new PamConfigReader(pam, sm);
//			pamInput.loadInputFromFile(path);
//		}
//
//		//TODO: config files for other modules
//		tem = new TEMImpl();
//		declarativeMemory = new DeclarativeMemoryImpl();
//		//
//		int bufferCapacity = 2;
//		int queueCapacity = 10;
//		workspace = new WorkspaceImpl(new PerceptualBufferImpl(bufferCapacity),
//									  new EpisodicBufferImpl(bufferCapacity),
//									  new BroadcastQueueImpl(queueCapacity),
//									  new CurrentSituationalModelImpl());
//		//
//		globalWksp = new GlobalWorkspaceImpl();
//		proceduralMemory = new ProceduralMemoryImpl();
//		actionSelection = new ActionSelectionImpl();
//		sensoryMotorMemory = new SensoryMotorMemoryImpl();
//		logger.info("Lida submodules Created");
//	}
//
//	private void initDrivers() {
//		//finish initializing drivers
//		attentionDriver = new AttentionDriver(taskManager, workspace.getCSM(), globalWksp);
//		sbCodeletDriver = new SBCodeletDriver(workspace, taskManager);
//		//Add drivers to a list for execution
//		drivers.add(environment);
//		drivers.add(new SensoryMemoryDriver(sensoryMemory, taskManager));
//		drivers.add(new PAMDriver(pam, taskManager));
//		drivers.add(attentionDriver);
//		drivers.add(sbCodeletDriver);
//		drivers.add(new ProceduralMemoryDriver(proceduralMemory, taskManager));
//
//		//done creating drivers
//		logger.info("Lida drivers Created");
//	}
//
//	private void initListeners() {
//		if (sensoryMemory instanceof SensoryMotorListener)
//			sensoryMotorMemory.addSensoryMotorListener((SensoryMotorListener) sensoryMemory);
//		else
//			logger.warning("Cannot add SM as a listener");
//
//		if(sensoryMotorMemory instanceof SensoryMemoryListener)
//			sensoryMemory.addSensoryMemoryListener((SensoryMemoryListener) sensoryMotorMemory);
//
//		if (workspace instanceof PAMListener)
//			pam.addPAMListener((PAMListener) workspace);
//		else
//			logger.warning("Cannot add WORKSPACE as a listener");
//
//		if (declarativeMemory instanceof CueListener)
//			workspace.addCueListener((CueListener)declarativeMemory);
//		else
//			logger.warning("Cannot add DM as a listener");
//
//		if (tem instanceof CueListener)
//			workspace.addCueListener((CueListener)tem);
//		else
//			logger.warning("Cannot add TEM as a listener");
//
//		if(pam instanceof WorkspaceListener)
//			workspace.addPamWorkspaceListener((WorkspaceListener) pam);
//		else
//			logger.warning("Cannot add PAM as a listener");
//
//		if(pam instanceof BroadcastListener)
//			globalWksp.addBroadcastListener((BroadcastListener) pam);
//		if(workspace instanceof BroadcastListener)
//			globalWksp.addBroadcastListener((BroadcastListener) workspace);
//		if(tem instanceof BroadcastListener)
//			globalWksp.addBroadcastListener((BroadcastListener) tem);
//		if(attentionDriver instanceof BroadcastListener)
//			globalWksp.addBroadcastListener((BroadcastListener) attentionDriver);
//		if(proceduralMemory instanceof BroadcastListener)
//			globalWksp.addBroadcastListener((BroadcastListener) proceduralMemory);
//
//		if(actionSelection instanceof ProceduralMemoryListener)
//			proceduralMemory.addProceduralMemoryListener((ProceduralMemoryListener)actionSelection);
//
//		if(workspace instanceof ActionSelectionListener)
//			actionSelection.addActionSelectionListener((ActionSelectionListener)workspace);
//		actionSelection.addActionSelectionListener(environment);
//
//		logger.info("Lida listeners added");
//	}
	public void start(){
//		globalWksp.start(); //TODO: change to the ThreadSpawner
//		taskManager.setInitialTasks(drivers);
//		logger.info("Lida submodules Started\n");
	}

	/**
	 * @return the environment
	 */
//	public Environment getEnvironment() {
//		return null;
//	}
//
//	/**
//	 * @return the sensoryMotorMemory
//	 */
//	public SensoryMotorMemory getSensoryMotorMemory() {
//		return null;
//	}
//
//	/**
//	 * @return the sensoryMemory
//	 */
//	public SensoryMemory getSensoryMemory() {
//		return null;
//	}
//
//	/**
//	 * @return the pam
//	 */
//	public PerceptualAssociativeMemory getPam() {
//		return null;
//	}
//
//	/**
//	 * @return the tem
//	 */
//	public TransientEpisodicMemory getTem() {
//		return null;
//	}
//
//	/**
//	 * @return the declarativeMemory
//	 */
//	public DeclarativeMemory getDeclarativeMemory() {
//		return null;
//	}
//
//	/**
//	 * @return the workspace
//	 */
//	public Workspace getWorkspace() {
//		return null;
//	}
//
//	/**
//	 * @return the globalWksp
//	 */
//	public GlobalWorkspace getGlobalWksp() {
//		return null;
//	}
//
//	/**
//	 * @return the procMem
//	 */
//	public ProceduralMemory getProcMem() {
//		return null;
//	}
//
//	/**
//	 * @return the actionSelection
//	 */
//	public ActionSelection getActionSelection() {
//		return null;
//	}
//
//	/**
//	 * @return the timer
//	 */
//	public LidaTaskManager getTaskManager() {
//		return null;
//	}
//
//	/**
//	 * @return the sbCodeletDriver
//	 */
//	public SBCodeletDriver getSbCodeletDriver() {
//		return null;
//	}
//
//	public AttentionDriver getAttentionDriver() {
//		return null;
//	}
}
