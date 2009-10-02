package edu.memphis.ccrg.lida.framework;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.gui.panels.LidaPanel;
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
import edu.memphis.ccrg.lida.transientepisodicmemory.TemImpl;
import edu.memphis.ccrg.lida.transientepisodicmemory.TransientEpisodicMemory;
import edu.memphis.ccrg.lida.workspace.broadcastbuffer.BroadcastQueueImpl;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModelImpl;
import edu.memphis.ccrg.lida.workspace.episodicbuffer.EpisodicBufferImpl;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;
import edu.memphis.ccrg.lida.workspace.perceptualbuffer.PerceptualBuffer;
import edu.memphis.ccrg.lida.workspace.perceptualbuffer.PerceptualBufferDriver;
import edu.memphis.ccrg.lida.workspace.perceptualbuffer.PerceptualBufferImpl;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.SbCodeletDriver;

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
	private SbCodeletDriver sbCodeletDriver;
	private PerceptualBuffer perceptualBuffer;
	private PerceptualBufferDriver pbDriver;
	// Attention
	private AttentionDriver attentionDriver;
	private GlobalWorkspace globalWksp;
	// Action Selection
	private ProceduralMemory proceduralMemory;
	private ActionSelection actionSelection;
	// A class that helps pause and control the drivers.
	private LidaTaskManager taskManager;

	public Lida(EnvironmentImpl environ, SensoryMemoryImpl sm, Properties properties) {
		logger.log(Level.FINE, "Starting Lida");
		initComponents(environ, sm, properties);
		initDrivers();
		initListeners();
		start();
	}

	private void initComponents(EnvironmentImpl environ, SensoryMemoryImpl sm, Properties properties) {
		//Properties for Lida module parameters
		this.lidaProperties = properties;
		
		//Task manager
		int tickDuration = Integer.parseInt(lidaProperties.getProperty("taskManager.tickDuration"));
		taskManager = new LidaTaskManager(tickDuration);

		//Environment
		environment = environ;
		environment.setTaskManager(taskManager);
		
		//Sensory Memory
		sensoryMemory = sm;
		sm.setEnvironment(environment);
		
		//Perceptual Associative Memory		
		pam = new PerceptualAssociativeMemoryImpl();
		
		PamInitializer initializer = new PamInitializer(pam, sm, taskManager);
		initializer.initModule(lidaProperties);
		
		//Transient Episodic Memory
		tem = new TemImpl(); 
		
		//Declarative Memory
		declarativeMemory = new DeclarativeMemoryImpl();
		
		//Workspace
		int episodicBufferCapacity = 2; //TODO: Will this be unnecessary?
		int queueCapacity = Integer.parseInt(lidaProperties.getProperty("broadcastQueueCapacity"));
		perceptualBuffer = new PerceptualBufferImpl();
		workspace = new WorkspaceImpl(perceptualBuffer,
									  new EpisodicBufferImpl(episodicBufferCapacity), 
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
		
		logger.log(Level.FINE, "Lida submodules Created");		
	}

	private void initDrivers() {
		int pamTicksPerStep = Integer.parseInt(lidaProperties.getProperty("pam.ticksPerStep"));
		int attnTicksPerStep = Integer.parseInt(lidaProperties.getProperty("attention.ticksPerStep"));
		int sbCodeletTicksPerStep = Integer.parseInt(lidaProperties.getProperty("sbCodelets.ticksPerStep"));
		int smTicksPerStep = Integer.parseInt(lidaProperties.getProperty("sensoryMemory.ticksPerStep"));
		int procMemTicksPerStep = Integer.parseInt(lidaProperties.getProperty("proceduralMemory.ticksPerStep"));
		
		//Environment
		moduleDrivers.add(environment);
		
		//Sensory Memory Driver
		moduleDrivers.add(new SensoryMemoryDriver(sensoryMemory, smTicksPerStep, taskManager));
		
		//Pam Driver
		pamDriver = new PamDriver(pam, pamTicksPerStep, taskManager);
		pam.setTaskSpawner(pamDriver);
		pamDriver.setInitialTasks(pam.getFeatureDetectors());
		moduleDrivers.add(pamDriver);
		
		//Perceptual Buffer Driver
		pbDriver = new PerceptualBufferDriver(perceptualBuffer, 10, taskManager);
		moduleDrivers.add(pbDriver);
		
		//Attention Driver
		attentionDriver = new AttentionDriver(workspace.getCSM(), globalWksp, attnTicksPerStep, taskManager);
		moduleDrivers.add(attentionDriver);
		
		//SbCodelet Driver
		sbCodeletDriver = new SbCodeletDriver(workspace, sbCodeletTicksPerStep, taskManager);
		moduleDrivers.add(sbCodeletDriver);
		
		//Procedural Memory Driver
		moduleDrivers.add(new ProceduralMemoryDriver(proceduralMemory, procMemTicksPerStep, taskManager));

		logger.log(Level.FINE, "Lida drivers Created");		
	}

	//Below comments "A ---> B" signify that the statement is establishing
	// the relationship  "Module A is sending data to Module B" 
	private void initListeners() {
		//Sensory-Motor Memory ---> SensoryMemory
		if (sensoryMemory instanceof SensoryMotorListener)
			sensoryMotorMemory.addSensoryMotorListener((SensoryMotorListener) sensoryMemory);
		else
			logger.warning("Cannot add SM as a listener");
		
		//Sensory Memory ---> Sensory-Motor Memory
		if(sensoryMotorMemory instanceof SensoryMemoryListener)
			sensoryMemory.addSensoryMemoryListener((SensoryMemoryListener) sensoryMotorMemory);
		
		//Pam ---> Workspace
		if (workspace instanceof PamListener)
			pam.addPamListener((PamListener) workspace);
		else
			logger.warning("Cannot add WORKSPACE as a listener");

		//Workspace ---> Declarative Memory
		if (declarativeMemory instanceof CueListener)
			workspace.addCueListener((CueListener)declarativeMemory);
		else
			logger.warning("Cannot add DM as a listener");
		
		//Workspace ---> Transient Episodic Memory
		if (tem instanceof CueListener)		
			workspace.addCueListener((CueListener)tem);
		else
			logger.warning("Cannot add TEM as a listener");
		
		//Workspace ---> Pam
		if(pam instanceof WorkspaceListener)
			workspace.addPamWorkspaceListener((WorkspaceListener) pam);
		else 
			logger.warning("Cannot add PAM as a listener");
		
		//PerceptualBuffer ---> Workspace
		if(workspace instanceof WorkspaceBufferListener)
			perceptualBuffer.addBufferListener((WorkspaceBufferListener) workspace);
		
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
		
		logger.log(Level.FINE, "Lida listeners added");	
	}
	public void start(){
		globalWksp.start();   
		taskManager.setInitialTasks(moduleDrivers);		
		logger.info("Lida modules have been started\n");		
	}

	/**
	 * @return the ENVIRONMENT
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

	public void addPanels(Collection<LidaPanel> panels) {
		for(ModuleDriver m: moduleDrivers){
			for(LidaPanel p: panels)
				m.addFrameworkGuiEventListener(p);
		}//
	}

}//class