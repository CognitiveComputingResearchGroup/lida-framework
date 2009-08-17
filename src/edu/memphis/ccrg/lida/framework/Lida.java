package edu.memphis.ccrg.lida.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionImpl;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.attention.AttentionDriver;
import edu.memphis.ccrg.lida.declarativememory.DeclarativeMemory;
import edu.memphis.ccrg.lida.declarativememory.DeclarativeMemoryImpl;
import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.initialization.PamInitializer;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.pam.PamDriver;
import edu.memphis.ccrg.lida.pam.PamListener;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemory;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryDriver;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryImpl;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryDriver;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryImpl;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryListener;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemory;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryImpl;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorListener;
import edu.memphis.ccrg.lida.transientepisodicmemory.CueListener;
import edu.memphis.ccrg.lida.transientepisodicmemory.TEMImpl;
import edu.memphis.ccrg.lida.transientepisodicmemory.TransientEpisodicMemory;
import edu.memphis.ccrg.lida.workspace.broadcastbuffer.BroadcastQueueImpl;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModelImpl;
import edu.memphis.ccrg.lida.workspace.episodicbuffer.EpisodicBufferImpl;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;
import edu.memphis.ccrg.lida.workspace.perceptualbuffer.PerceptualBufferImpl;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.SBCodeletDriver;

/**
 * @author Javier Snaider
 */
public class Lida {
	
	private Logger logger = Logger.getLogger("lida.framework.Lida");
	
	/**
	 * List of drivers which run the major components of LIDA
	 */
	private List<ModuleDriver> moduleDrivers = new ArrayList<ModuleDriver>();
	
	/**
	 * To read a parameter file
	 */
	private Properties lidaProperties;
	
	// Perception
	private EnvironmentImpl environment;
	private SensoryMotorMemory sensoryMotorMemory;
	private SensoryMemory sensoryMemory;
	private PerceptualAssociativeMemory pam;
	private PamDriver pamDriver;
	// Episodic memory
	private TransientEpisodicMemory tem;
	private DeclarativeMemory declarativeMemory;
	// Workspace
	private Workspace workspace;
	private SBCodeletDriver sbCodeletDriver;
	// Attention
	private AttentionDriver attentionDriver;
	private GlobalWorkspace globalWksp;
	// Action Selection
	private ProceduralMemory proceduralMemory;
	private ActionSelection actionSelection;
	// A class that helps pause and control the drivers.
	private LidaTaskManager taskManager;

	

	public Lida(EnvironmentImpl environ, SensoryMemoryImpl sm, Properties properties) {
		logger.info("Starting Lida");
		initComponents(environ, sm, properties);
		initDrivers();
		initListeners();
		start();
	}

	private void initComponents(EnvironmentImpl environ, SensoryMemoryImpl sm, Properties properties) {
		//Properties for Module Parameters
		this.lidaProperties = properties;
		
		//Task manager
		//TODO: Use Properties
    	boolean tasksStartOutRunning = false;
		int tickDuration = 10;
		taskManager = new LidaTaskManager(tasksStartOutRunning, tickDuration);

		//Environment
		environment = environ;
		environment.setTaskManager(taskManager);
		
		//Sensory Memory
		sensoryMemory = sm;
		sm.setEnvironment(environment);
		
		//Perceptual Associative Memory		
		pam = new PerceptualAssociativeMemoryImpl();
		PamInitializer initializer = new PamInitializer(pam, sm);
		initializer.initModule(lidaProperties);
		
		//Transient Episodic Memory
		tem = new TEMImpl(); 
		
		//Declarative Memory
		declarativeMemory = new DeclarativeMemoryImpl();
		
		//Workspace
		int bufferCapacity = 2;//TODO: this will be unnecessary soon
		int queueCapacity = Integer.parseInt(lidaProperties.getProperty("broadcastQueueCapacity"));
		workspace = new WorkspaceImpl(new PerceptualBufferImpl(bufferCapacity),
									  new EpisodicBufferImpl(bufferCapacity), 
									  new BroadcastQueueImpl(queueCapacity),
									  new CurrentSituationalModelImpl());
		
		//Global Workspace
		globalWksp = new GlobalWorkspaceImpl();
		
		//Procedural Memory
		proceduralMemory = new ProceduralMemoryImpl();
		
		//Action Selection
		actionSelection = new ActionSelectionImpl();
		
		//Sensory-motor Memory
		sensoryMotorMemory = new SensoryMotorMemoryImpl();
		
		logger.info("Lida submodules Created");		
	}

	private void initDrivers() {
		//TODO: Use Properties
		int pamTicksPerStep = 10;
		int attnTicksPerStep = 10;
		int sbCodeletTicksPerStep = 10;
		int smTicksPerStep = 10;
		int procMemTicksPerStep = 10;
		
		//finish initializing drivers 
		attentionDriver = new AttentionDriver(taskManager, workspace.getCSM(), 
											  globalWksp, attnTicksPerStep);
		sbCodeletDriver = new SBCodeletDriver(workspace, taskManager, sbCodeletTicksPerStep);
		//Add drivers to a list for execution
		moduleDrivers.add(environment);
		moduleDrivers.add(new SensoryMemoryDriver(sensoryMemory, taskManager, smTicksPerStep));
		
		pamDriver = new PamDriver(pam, taskManager, pamTicksPerStep);
		pamDriver.setInitialTasks(pam.getFeatureDetectors());
		pam.setTaskSpawner(pamDriver);
		
		moduleDrivers.add(pamDriver);
		moduleDrivers.add(attentionDriver);
		moduleDrivers.add(sbCodeletDriver);
		moduleDrivers.add(new ProceduralMemoryDriver(proceduralMemory, taskManager, procMemTicksPerStep));
		
		//done creating drivers
		logger.info("Lida drivers Created");		
	}

	private void initListeners() {
		if (sensoryMemory instanceof SensoryMotorListener)
			sensoryMotorMemory.addSensoryMotorListener((SensoryMotorListener) sensoryMemory);
		else
			logger.warning("Cannot add SM as a listener");
		
		if(sensoryMotorMemory instanceof SensoryMemoryListener)
			sensoryMemory.addSensoryMemoryListener((SensoryMemoryListener) sensoryMotorMemory);
		
		if (workspace instanceof PamListener)
			pam.addPamListener((PamListener) workspace);
		else
			logger.warning("Cannot add WORKSPACE as a listener");

		if (declarativeMemory instanceof CueListener)
			workspace.addCueListener((CueListener)declarativeMemory);
		else
			logger.warning("Cannot add DM as a listener");
		
		if (tem instanceof CueListener)		
			workspace.addCueListener((CueListener)tem);
		else
			logger.warning("Cannot add TEM as a listener");
		
		if(pam instanceof WorkspaceListener)
			workspace.addPamWorkspaceListener((WorkspaceListener) pam);
		else 
			logger.warning("Cannot add PAM as a listener");
		
		if(pam instanceof BroadcastListener)
			globalWksp.addBroadcastListener((BroadcastListener) pam);			
		if(workspace instanceof BroadcastListener)	
			globalWksp.addBroadcastListener((BroadcastListener) workspace);
		if(tem instanceof BroadcastListener)
			globalWksp.addBroadcastListener((BroadcastListener) tem);
		if(attentionDriver instanceof BroadcastListener)
			globalWksp.addBroadcastListener((BroadcastListener) attentionDriver);
		if(proceduralMemory instanceof BroadcastListener)
			globalWksp.addBroadcastListener((BroadcastListener) proceduralMemory);

		if(actionSelection instanceof ProceduralMemoryListener)
			proceduralMemory.addProceduralMemoryListener((ProceduralMemoryListener)actionSelection);
		
		if(workspace instanceof ActionSelectionListener)
			actionSelection.addActionSelectionListener((ActionSelectionListener)workspace);
		actionSelection.addActionSelectionListener(environment);
		
		logger.info("Lida listeners added");	
	}
	public void start(){
		globalWksp.start(); //TODO: change to the ThreadSpawner  <--What does this comment mean? - (Ryan)
		taskManager.setInitialTasks(moduleDrivers);		
		logger.info("Lida submodules Started\n");		
	}

	/**
	 * @return the environment
	 */
	public Environment getEnvironment() {
		return environment;
	}
	/**
	 * @return the sensoryMotorMemory
	 */
	public SensoryMotorMemory getSensoryMotorMemory() {
		return sensoryMotorMemory;
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
	
	public PamDriver getPamDriver(){
		return pamDriver;
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
	 * @return the sbCodeletDriver
	 */
	public SBCodeletDriver getSbCodeletDriver() {
		return sbCodeletDriver;
	}
	public AttentionDriver getAttentionDriver() {
		return attentionDriver;
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
		return proceduralMemory;
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
	public LidaTaskManager getTaskManager() {
		return taskManager;
	}

}//class