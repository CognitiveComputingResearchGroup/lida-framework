package edu.memphis.ccrg.lida.framework;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionImpl;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.attention.AttentionDriver;
import edu.memphis.ccrg.lida.declarativememory.DeclarativeMemoryImpl;
import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.initialization.GlobalWorkspaceInitalizer;
import edu.memphis.ccrg.lida.example.framework.initialization.PamInitializer;
import edu.memphis.ccrg.lida.example.genericlida.main.VisionEnvironment;
import edu.memphis.ccrg.lida.example.genericlida.main.VisionSensoryMemory;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
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
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorListener;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemory;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryImpl;
import edu.memphis.ccrg.lida.transientepisodicmemory.CueListener;
import edu.memphis.ccrg.lida.transientepisodicmemory.TemImpl;
import edu.memphis.ccrg.lida.workspace.broadcastbuffer.BroadcastQueueImpl;
import edu.memphis.ccrg.lida.workspace.main.Workspace;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceListener;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.SbCodeletDriver;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

/**
 * This class implements the Model of the framework. All modules, drivers, and other structures that comprise LIDA. 
 * To customize modules, this class should be extended and the various init method should be overwritten.
 * 
 * TODO: This class must be rewritten to use the classes for modules than can be specified in a file.
 * 
 * @author Javier Snaider
 */
public class Lida1 extends LidaModuleImpl implements Lida{
	
	private static Logger logger = Logger.getLogger("lida.framework.Lida");
	
	/**
	 * List of drivers which run the major components of LIDA
	 */
	//protected List<ModuleDriver> moduleDrivers = new ArrayList<ModuleDriver>();
//	/**
//	 * List of drivers which run the major components of LIDA
//	 */
//	protected Map<ModuleName, LidaModule> modules = new ConcurrentHashMap<ModuleName, LidaModule>();
	
	/**
	 * To read a parameter file
	 */
	protected Properties lidaProperties;
	
//	// Perception
//	protected Environment environment;
//	protected SensoryMotorMemory sensoryMotorMemory;
//	protected SensoryMemory sensoryMemory;
//	protected PerceptualAssociativeMemory pam;
//	protected PamDriver pamDriver;
//	// Episodic memory
//	protected TransientEpisodicMemory tem;
//	protected DeclarativeMemory declarativeMemory;
//	// Workspace
//	protected Workspace workspace;
//	protected SbCodeletDriver sbCodeletDriver;
//	// Attention
//	protected AttentionDriver attentionDriver;
//	protected GlobalWorkspace globalWksp;
//	// Action Selection
//	protected ProceduralMemory proceduralMemory;
//	protected ActionSelection actionSelection;
	// A class that helps pause and control the drivers.
	protected LidaTaskManager taskManager;

	private Map<ModuleName,ModuleDriver> drivers = new ConcurrentHashMap<ModuleName,ModuleDriver>();
	/**
	 * Constructs a new LIDA.
	 * This constructor calls initComponents(), initDrivers()and initListeners()
	 * 
	 * @param environ the Environment
	 * @param sm
	 * @param properties
	 */
	public Lida1(Properties properties) {
		super(ModuleName.LIDA);
		logger.log(Level.FINE, "Starting Lida",0L);
		//Properties for Lida module parameters
		this.lidaProperties = properties;
		initComponents();
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
	protected void initComponents() {
		
		LidaModule module;
		Map<ModuleName,LidaModule> modules=getSubmodules();
		//Task manager
		int tickDuration = Integer.parseInt(lidaProperties.getProperty("taskManager.tickDuration"));
		int maxNumberOfThreads = Integer.parseInt(lidaProperties.getProperty("taskManager.maxNumberOfThreads"));
		
		taskManager = new LidaTaskManager(tickDuration, maxNumberOfThreads);
		//Declare an EnvironmentImpl
		int height = 10, width = 10;
		EnvironmentImpl environ = new VisionEnvironment(height, width);
		
		//Declare a SensoryMemoryImpl
		SensoryMemoryImpl sm = new VisionSensoryMemory();
		

		//Environment
		environ.setTaskManager(taskManager);
		modules.put(environ.getModuleName(), environ);
		if (environ instanceof ModuleDriver){
			drivers.put(environ.getModuleName(),(ModuleDriver)environ);
		}
		
		//Sensory Memory
		sm.setAssociatedModule(environ); 
		modules.put(sm.getModuleName(), sm);
		
		//Perceptual Associative Memory		
		module = new PerceptualAssociativeMemoryImpl();
//		((PerceptualAssociativeMemoryImpl)module).setTaskManager(taskManager);
		Map<String,Object> params = new HashMap<String,Object>();
		for(Entry<Object, Object> e:lidaProperties.entrySet()){
			params.put((String)e.getKey(), e.getValue());
		}
		module.init(params);
		Initializer initializer = new PamInitializer();
		initializer.initModule(module,this,params);
		modules.put(module.getModuleName(), module);
		
		//Transient Episodic Memory
		module = new TemImpl(); 
		modules.put(module.getModuleName(), module);
		
		//Declarative Memory
		module = new DeclarativeMemoryImpl();
		modules.put(module.getModuleName(), module);
		
		//Workspace
		int queueCapacity = Integer.parseInt(lidaProperties.getProperty("broadcastQueueCapacity"));
		module = new WorkspaceImpl(new WorkspaceBufferImpl(ModuleName.EpisodicBuffer),new WorkspaceBufferImpl(ModuleName.PerceptualBuffer),
				new WorkspaceBufferImpl(ModuleName.CurrentSituationalModel),new BroadcastQueueImpl(queueCapacity));
		modules.put(module.getModuleName(), module);
		
		
		//Global Workspace
		int gwtTicksPerStep = Integer.parseInt(lidaProperties.getProperty("globalWorkspace.ticksPerStep"));
		
		module = new GlobalWorkspaceImpl(taskManager);
		initializer = new GlobalWorkspaceInitalizer();
		initializer.initModule(module,this,params);
		modules.put(module.getModuleName(), module);
		

		
		//Procedural Memory
		module = new ProceduralMemoryImpl();
		modules.put(module.getModuleName(), module);
		
		//Action Selection
		module = new ActionSelectionImpl();
		modules.put(module.getModuleName(), module);
		
		//Sensory-motor Memory
		module = new SensoryMotorMemoryImpl();
		modules.put(module.getModuleName(), module);
		
		taskManager.setDecayingModules(modules.values());
		logger.log(Level.FINE, "Lida submodules Created",0L);		
	}

	/**
	 * Initializes the ModuleDrivers of LIDA
	 */
	protected void initDrivers() {
		ModuleDriver driver;
		int pamTicksPerStep = Integer.parseInt(lidaProperties.getProperty("pam.ticksPerStep"));
		int attnTicksPerStep = Integer.parseInt(lidaProperties.getProperty("attention.ticksPerStep"));
		int sbCodeletTicksPerStep = Integer.parseInt(lidaProperties.getProperty("sbCodelets.ticksPerStep"));
		int smTicksPerStep = Integer.parseInt(lidaProperties.getProperty("sensoryMemory.ticksPerStep"));
		int procMemTicksPerStep = Integer.parseInt(lidaProperties.getProperty("proceduralMemory.ticksPerStep"));
		
		LidaModule module=null;
//		//Environment
//		LidaModule
//		if (environment instanceof ModuleDriver){
//			drivers.put(environment.getModuleName(),(ModuleDriver)environment);
//		}
		
		//Sensory Memory Driver
		driver=new SensoryMemoryDriver((SensoryMemory)getSubmodule(ModuleName.SensoryMemory), smTicksPerStep, taskManager);
		drivers.put(driver.getModuleName(),driver);
		
		//Pam Driver
		module=getSubmodule(ModuleName.PerceptualAssociativeMemory);
		driver = new PamDriver((PerceptualAssociativeMemory)module,
				pamTicksPerStep, taskManager);
		((PerceptualAssociativeMemory)module).setTaskSpawner(driver);//TODO:VER
		driver.setInitialTasks(((PerceptualAssociativeMemory)module).getFeatureDetectors());
		drivers.put(driver.getModuleName(),driver);
		
		//Attention Driver
		module=getSubmodule(ModuleName.Workspace);
		GlobalWorkspace gwt=(GlobalWorkspace) getSubmodule(ModuleName.GlobalWorkspace);
		driver = new AttentionDriver((WorkspaceBuffer) (((Workspace)module).getSubmodule(ModuleName.CurrentSituationalModel)), gwt, attnTicksPerStep, taskManager);
		drivers.put(driver.getModuleName(),driver);
		
		//SbCodelet Driver
		driver = new SbCodeletDriver(((Workspace)module), sbCodeletTicksPerStep, taskManager);
		drivers.put(driver.getModuleName(),driver);
		
		//Procedural Memory Driver
		module=getSubmodule(ModuleName.ProceduralMemory);
		driver=new ProceduralMemoryDriver((ProceduralMemory)module, procMemTicksPerStep, taskManager);
		drivers.put(driver.getModuleName(),driver);

		//Globalworkspace
		drivers.put(gwt.getModuleName(),gwt);

		logger.log(Level.FINE, "Lida drivers Created",0L);		
	}

	//Below comments "A ---> B" signify that the statement is establishing
	// the relationship  "ModuleType A is sending data to ModuleType B" 
	protected void initListeners() {
		//Sensory-Motor Memory ---> SensoryMemory
		LidaModule listener=getSubmodule(ModuleName.SensoryMemory);
		LidaModule module=getSubmodule(ModuleName.SensoryMotorMemory);
		if (listener instanceof SensoryMotorListener)
			((SensoryMotorMemory)module).addSensoryMotorListener((SensoryMotorListener) listener);
		else
			logger.log(Level.WARNING,"Cannot add a listener",0L);
		
		//Sensory Memory ---> Sensory-Motor Memory
		listener=getSubmodule(ModuleName.SensoryMotorMemory);
		module=getSubmodule(ModuleName.SensoryMemory);
		if (listener instanceof SensoryMemoryListener)
			((SensoryMemory)module).addSensoryMemoryListener((SensoryMemoryListener) listener);
		else
			logger.log(Level.WARNING,"Cannot add a listener",0L);
		
		//Pam ---> Workspace
		listener=getSubmodule(ModuleName.Workspace);
		module=getSubmodule(ModuleName.PerceptualAssociativeMemory);
		
		if (listener instanceof PamListener)
			((PerceptualAssociativeMemory)module).addPamListener((PamListener) listener);
		else
			logger.log(Level.WARNING,"Cannot add WORKSPACE as a listener", 0L);

		//Workspace ---> Declarative Memory
		listener=getSubmodule(ModuleName.DeclarativeMemory);
		module=getSubmodule(ModuleName.Workspace);
		if (listener instanceof CueListener)
			((Workspace)module).addCueListener((CueListener)listener);
		else
			logger.log(Level.WARNING,"Cannot add DM as a listener", 0L);
		
		//Workspace ---> Transient Episodic Memory
		listener=getSubmodule(ModuleName.TransientEpisodicMemory);
		module=getSubmodule(ModuleName.Workspace);
		if (listener instanceof CueListener)		
			((Workspace)module).addCueListener((CueListener)listener);
		else
			logger.log(Level.WARNING,"Cannot add TEM as a listener", 0L);
		
		//Workspace ---> Pam
		listener=getSubmodule(ModuleName.PerceptualAssociativeMemory);
		module=getSubmodule(ModuleName.Workspace);
		if(listener instanceof WorkspaceListener)
			((Workspace)module).addWorkspaceListener((WorkspaceListener) listener);
		else 
			logger.log(Level.WARNING,"Cannot add PAM as a listener", 0L);
		
		//PerceptualBuffer ---> Workspace
//		if(workspace instanceof WorkspaceBufferListener)
//			perceptualBuffer.addBufferListener((WorkspaceBufferListener) workspace);
//		
		
		//Global Workspace ---> PAM
		module=getSubmodule(ModuleName.GlobalWorkspace);
		listener=getSubmodule(ModuleName.PerceptualAssociativeMemory);
		if(listener instanceof BroadcastListener)
			((GlobalWorkspace)module).addBroadcastListener((BroadcastListener) listener);
		//Global Workspace ---> Workspace
		listener=getSubmodule(ModuleName.Workspace);
		if(listener instanceof BroadcastListener)
			((GlobalWorkspace)module).addBroadcastListener((BroadcastListener) listener);
		//Global Workspace ---> TEM
		listener=getSubmodule(ModuleName.TransientEpisodicMemory);
		if(listener instanceof BroadcastListener)
			((GlobalWorkspace)module).addBroadcastListener((BroadcastListener) listener);
		//Global Workspace ---> ATTN
		listener=getSubmodule(ModuleName.AttentionDriver);
		if(listener instanceof BroadcastListener)
			((GlobalWorkspace)module).addBroadcastListener((BroadcastListener) listener);
		//Global Workspace ---> Procedural Memory
		listener=getSubmodule(ModuleName.ProceduralMemory);
		if(listener instanceof BroadcastListener)
			((GlobalWorkspace)module).addBroadcastListener((BroadcastListener) listener);

		//Procedural Memory ---> Action Selection
		module=getSubmodule(ModuleName.ProceduralMemory);
		listener=getSubmodule(ModuleName.ActionSelection);
		if(listener instanceof ProceduralMemoryListener)
			((ProceduralMemory)module).addProceduralMemoryListener((ProceduralMemoryListener)listener);
		
		//Action Selection ---> Workspace
		module=getSubmodule(ModuleName.ActionSelection);
		listener=getSubmodule(ModuleName.Workspace);
		if(listener instanceof ActionSelectionListener)
			((ActionSelection)module).addActionSelectionListener((ActionSelectionListener)listener);
		
		listener=getSubmodule(ModuleName.Environment);		
		if(listener instanceof ActionSelectionListener)
			((ActionSelection)module).addActionSelectionListener((ActionSelectionListener)listener);
		
		logger.log(Level.FINE, "Lida listeners added",0L);	
	}
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.Lida#start()
	 */
	public void start(){
		//globalWksp.start(); 
		taskManager.getMainTaskSpawner().setInitialTasks(drivers.values());		
		logger.log(Level.INFO,"Lida modules have been started\n", 0L);		
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.Lida#getModuleDriver(edu.memphis.ccrg.lida.framework.ModuleName)
	 */
	public ModuleDriver getModuleDriver(ModuleName name){
		return drivers.get(name);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.Lida#getTaskManager()
	 */
	public LidaTaskManager getTaskManager() {
		return taskManager;
	}

	public LidaModule getModule(ModuleName mt){
		
		return getSubmodule(mt);
		
	}

	public Object getModuleContent() {
		return null;
	}

	public void addModuleDriver(ModuleDriver driver) {
	}

	public void addListener(ModuleListener listener) {
	}
}//class