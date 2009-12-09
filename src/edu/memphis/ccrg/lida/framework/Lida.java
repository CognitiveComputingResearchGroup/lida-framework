package edu.memphis.ccrg.lida.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionImpl;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.attention.AttentionDriver;
import edu.memphis.ccrg.lida.declarativememory.DeclarativeMemory;
import edu.memphis.ccrg.lida.declarativememory.DeclarativeMemoryImpl;
import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.framework.initialization.GlobalWorkspaceInitalizer;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.initialization.PamInitializer;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.globalworkspace.triggers.AggregateCoalitionActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.IndividualCoaltionActivationTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.NoBroadcastOccurringTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.NoCoalitionArrivingTrigger;
import edu.memphis.ccrg.lida.globalworkspace.triggers.TriggerListener;
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
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorListener;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemory;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryImpl;
import edu.memphis.ccrg.lida.transientepisodicmemory.CueListener;
import edu.memphis.ccrg.lida.transientepisodicmemory.TemImpl;
import edu.memphis.ccrg.lida.transientepisodicmemory.TransientEpisodicMemory;
import edu.memphis.ccrg.lida.workspace.broadcastbuffer.BroadcastQueueImpl;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.SbCodeletDriver;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

/**
 * This class implements the Model of the framework. All modules, drivers, and other structures that comprise LIDA. 
 * To customize modules, this class should be extended and the various init method should be overwritten.
 * 
 * TODO: This class must be rewritten to use the classes for modules than can be specified in a file.
 * 
 * @author Javier Snaider
 */
public class Lida {
	
	private Logger logger = Logger.getLogger("lida.framework.Lida");
	
	/**
	 * List of drivers which run the major components of LIDA
	 */
	protected List<ModuleDriver> moduleDrivers = new ArrayList<ModuleDriver>();
	
	/**
	 * To read a parameter file
	 */
	protected Properties lidaProperties;
	
	// Perception
	protected Environment environment;
	protected SensoryMotorMemory sensoryMotorMemory;
	protected SensoryMemory sensoryMemory;
	protected PerceptualAssociativeMemory pam;
	protected PamDriver pamDriver;
	// Episodic memory
	protected TransientEpisodicMemory tem;
	protected DeclarativeMemory declarativeMemory;
	// Workspace
	protected Workspace workspace;
	protected SbCodeletDriver sbCodeletDriver;
	// Attention
	protected AttentionDriver attentionDriver;
	protected GlobalWorkspace globalWksp;
	// Action Selection
	protected ProceduralMemory proceduralMemory;
	protected ActionSelection actionSelection;
	// A class that helps pause and control the drivers.
	protected LidaTaskManager taskManager;

	
	/**
	 * Constructs a new LIDA.
	 * This constructor calls initComponents(), initDrivers()and initListeners()
	 * 
	 * @param environ the Environment
	 * @param sm
	 * @param properties
	 */
	public Lida(Environment environ, SensoryMemoryImpl sm, Properties properties) {
		logger.log(Level.FINE, "Starting Lida",0L);
		//Properties for Lida module parameters
		this.lidaProperties = properties;
		initComponents(environ, sm);
		initDrivers();
		initListeners();
		start();
	}

	/**
	 * Initializes the main component modules of LIDA
	 * 
	 * @param environ The Environment
	 * @param sm The Sensory Memory
	 */
	protected void initComponents(Environment environ, SensoryMemoryImpl sm) {
		
		//Task manager
		int tickDuration = Integer.parseInt(lidaProperties.getProperty("taskManager.tickDuration"));
		int maxNumberOfThreads = Integer.parseInt(lidaProperties.getProperty("taskManager.maxNumberOfThreads"));
		
		taskManager = new LidaTaskManager(tickDuration, maxNumberOfThreads);

		//Environment
		environment = environ;
		environment.setTaskManager(taskManager);
		
		//Sensory Memory
		sensoryMemory = sm;
		sm.setEnvironment(environment);
		
		//Perceptual Associative Memory		
		pam = new PerceptualAssociativeMemoryImpl();
		pam.setTaskManager(taskManager);
		pam.init(lidaProperties);
		Initializer initializer = new PamInitializer(pam, sm, taskManager);
		initializer.initModule(lidaProperties);
		
		//Transient Episodic Memory
		tem = new TemImpl(); 
		
		//Declarative Memory
		declarativeMemory = new DeclarativeMemoryImpl();
		
		//Workspace
		int queueCapacity = Integer.parseInt(lidaProperties.getProperty("broadcastQueueCapacity"));
		workspace = new WorkspaceImpl(new WorkspaceBufferImpl(),new WorkspaceBufferImpl(),  new WorkspaceBufferImpl(),
									  new BroadcastQueueImpl(queueCapacity));
		
		//Global Workspace
		int gwtTicksPerStep = Integer.parseInt(lidaProperties.getProperty("globalWorkspace.ticksPerStep"));
		
		globalWksp = new GlobalWorkspaceImpl(gwtTicksPerStep, taskManager);
		initializer = new GlobalWorkspaceInitalizer(globalWksp);
		initializer.initModule(lidaProperties);
		

		
		//Procedural Memory
		proceduralMemory = new ProceduralMemoryImpl();
		
		//Action Selection
		actionSelection = new ActionSelectionImpl();
		
		//Sensory-motor Memory
		sensoryMotorMemory = new SensoryMotorMemoryImpl();
		
		logger.log(Level.FINE, "Lida submodules Created",0L);		
	}

	/**
	 * Initializes the ModuleDrivers of LIDA
	 */
	protected void initDrivers() {
		int pamTicksPerStep = Integer.parseInt(lidaProperties.getProperty("pam.ticksPerStep"));
		int attnTicksPerStep = Integer.parseInt(lidaProperties.getProperty("attention.ticksPerStep"));
		int sbCodeletTicksPerStep = Integer.parseInt(lidaProperties.getProperty("sbCodelets.ticksPerStep"));
		int smTicksPerStep = Integer.parseInt(lidaProperties.getProperty("sensoryMemory.ticksPerStep"));
		int procMemTicksPerStep = Integer.parseInt(lidaProperties.getProperty("proceduralMemory.ticksPerStep"));
		
		//Environment
		if (environment instanceof ModuleDriver){
			moduleDrivers.add((ModuleDriver)environment);
		}
		
		//Sensory Memory Driver
		moduleDrivers.add(new SensoryMemoryDriver(sensoryMemory, smTicksPerStep, taskManager));
		
		//Pam Driver
		pamDriver = new PamDriver(pam, pamTicksPerStep, taskManager);
		pam.setTaskSpawner(pamDriver);
		pamDriver.setInitialTasks(pam.getFeatureDetectors());
		moduleDrivers.add(pamDriver);
		
		//Attention Driver
		attentionDriver = new AttentionDriver(workspace.getCSM(), globalWksp, attnTicksPerStep, taskManager);
		moduleDrivers.add(attentionDriver);
		
		//SbCodelet Driver
		sbCodeletDriver = new SbCodeletDriver(workspace, sbCodeletTicksPerStep, taskManager);
		moduleDrivers.add(sbCodeletDriver);
		
		//Procedural Memory Driver
		moduleDrivers.add(new ProceduralMemoryDriver(proceduralMemory, procMemTicksPerStep, taskManager));

		//Globalworkspace
		moduleDrivers.add(globalWksp);

		logger.log(Level.FINE, "Lida drivers Created",0L);		
	}

	//Below comments "A ---> B" signify that the statement is establishing
	// the relationship  "ModuleType A is sending data to ModuleType B" 
	protected void initListeners() {
		//Sensory-Motor Memory ---> SensoryMemory
		if (sensoryMemory instanceof SensoryMotorListener)
			sensoryMotorMemory.addSensoryMotorListener((SensoryMotorListener) sensoryMemory);
		else
			logger.log(Level.WARNING,"Cannot add SM as a listener",0L);
		
		//Sensory Memory ---> Sensory-Motor Memory
		if(sensoryMotorMemory instanceof SensoryMemoryListener)
			sensoryMemory.addSensoryMemoryListener((SensoryMemoryListener) sensoryMotorMemory);
		
		//Pam ---> Workspace
		if (workspace instanceof PamListener)
			pam.addPamListener((PamListener) workspace);
		else
			logger.log(Level.WARNING,"Cannot add WORKSPACE as a listener", 0L);

		//Workspace ---> Declarative Memory
		if (declarativeMemory instanceof CueListener)
			workspace.addCueListener((CueListener)declarativeMemory);
		else
			logger.log(Level.WARNING,"Cannot add DM as a listener", 0L);
		
		//Workspace ---> Transient Episodic Memory
		if (tem instanceof CueListener)		
			workspace.addCueListener((CueListener)tem);
		else
			logger.log(Level.WARNING,"Cannot add TEM as a listener", 0L);
		
		//Workspace ---> Pam
		if(pam instanceof WorkspaceListener)
			workspace.addPamWorkspaceListener((WorkspaceListener) pam);
		else 
			logger.log(Level.WARNING,"Cannot add PAM as a listener", 0L);
		
		//PerceptualBuffer ---> Workspace
//		if(workspace instanceof WorkspaceBufferListener)
//			perceptualBuffer.addBufferListener((WorkspaceBufferListener) workspace);
//		
		
		//Global Workspace ---> PAM
		if(pam instanceof BroadcastListener)
			globalWksp.addBroadcastListener((BroadcastListener) pam);
		//Global Workspace ---> Workspace
		if(workspace instanceof BroadcastListener)	
			globalWksp.addBroadcastListener((BroadcastListener) workspace);
		//Global Workspace ---> TEM
		if(tem instanceof BroadcastListener)
			globalWksp.addBroadcastListener((BroadcastListener) tem);
		//Global Workspace ---> ATTN
		if(attentionDriver instanceof BroadcastListener)
			globalWksp.addBroadcastListener((BroadcastListener) attentionDriver);
		//Global Workspace ---> Procedural Memory
		if(proceduralMemory instanceof BroadcastListener)
			globalWksp.addBroadcastListener((BroadcastListener) proceduralMemory);

		//Procedural Memory ---> Action Selection
		if(actionSelection instanceof ProceduralMemoryListener)
			proceduralMemory.addProceduralMemoryListener((ProceduralMemoryListener)actionSelection);
		
		//Action Selection ---> Workspace
		if(workspace instanceof ActionSelectionListener)
			actionSelection.addActionSelectionListener((ActionSelectionListener)workspace);
		actionSelection.addActionSelectionListener(environment);
		
		logger.log(Level.FINE, "Lida listeners added",0L);	
	}
	public void start(){
		//globalWksp.start();   
		taskManager.getMainTaskSpawner().setInitialTasks(moduleDrivers);		
		logger.log(Level.INFO,"Lida modules have been started\n", 0L);		
	}

	/**
	 * @return the Environment
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
	public SbCodeletDriver getSbCodeletDriver() {
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