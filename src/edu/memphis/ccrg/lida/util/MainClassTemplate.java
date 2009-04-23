///*
// * Start.java - Initializes, starts, and ends the threads 
// * that run the main LIDA components.  Sets up the shared memory 
// * that the threads use to pass data. 
// */
//package main;
//
//import gui.CSMGui;
//import gui.ControlPanelGui;
//import gui.ActionControlGui;
//import gui.WumpusEnvironmentGui;
//import gui.NodeLinkFlowGui;
//import java.util.Map;
//import java.util.Set;
//import java.util.List;
//import java.lang.Thread; 
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.ArrayList;
//import edu.memphis.ccrg.lida.actionSelection.ActionSelectionImpl;
//import edu.memphis.ccrg.lida.attention.AttentionDriver;
//import edu.memphis.ccrg.lida.declarativeMemory.DeclarativeMemory;
//import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;
//import edu.memphis.ccrg.lida.globalworkspace.triggers.TimeOutTrigger;
//import edu.memphis.ccrg.lida.globalworkspace.triggers.Trigger;
//import edu.memphis.ccrg.lida.globalworkspace.triggers.TriggerListener;
//import edu.memphis.ccrg.lida.perception.FeatureDetector;
//import edu.memphis.ccrg.lida.perception.PAMDriver;
//import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryDriver;
//import edu.memphis.ccrg.lida.shared.Link;
//import edu.memphis.ccrg.lida.shared.LinkImpl;
//import edu.memphis.ccrg.lida.shared.LinkType;
//import edu.memphis.ccrg.lida.shared.Node;
//import edu.memphis.ccrg.lida.transientEpisodicMemory.TransientEpisodicMemory;
//import edu.memphis.ccrg.lida.util.FrameworkTimer;
//import edu.memphis.ccrg.lida.util.Stoppable;
//import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CSMDriver;
//import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBufferDriver;
//import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBufferImpl;
//import edu.memphis.ccrg.lida.workspace.main.WorkspaceImpl;
//import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBufferDriver;
//import edu.memphis.ccrg.lida.workspace.previousBroadcasts.PreviousBroadcastsDriver;
//import edu.memphis.ccrg.lida.workspace.previousBroadcasts.PreviousBroadcastsImpl;
//import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.StructureBuildingCodeletDriver;
//import edu.memphis.ccrg.lida.wumpusWorld.a_environment.Environment;
//import edu.memphis.ccrg.lida.wumpusWorld.a_environment.Starter;
//import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WumpusWorld;
//import edu.memphis.ccrg.lida.wumpusWorld.a_environment.WumpusNodeIDs;
//import edu.memphis.ccrg.lida.wumpusWorld.b_sensoryMemory.SensoryMemoryImpl;
//import edu.memphis.ccrg.lida.wumpusWorld.c_perception.featureDetection.FeatureDetectorImpl;
//import edu.memphis.ccrg.lida.wumpusWorld.c_perception.featureDetection.WumpusDetectBehavior;
//import edu.memphis.ccrg.lida.wumpusWorld.d_perception.RyanPamNode;
//import edu.memphis.ccrg.lida.wumpusWorld.d_perception.PerceptualAssociativeMemoryImpl;
//import edu.memphis.ccrg.lida.wumpusWorld.e_perceptualBuffer.PerceptualBufferImpl;
//import edu.memphis.ccrg.lida.wumpusWorld.i_csm.CurrentSituationalModelImpl;
//import edu.memphis.ccrg.lida.wumpusWorld.l_proceduralMemory.EasyProceduralMemoryDriver;
//
//public class Start implements Starter, Runnable{
//	//SHARED "DATA STRUCTURES"
//	//Perception
//	private WumpusWorld simulation;
//	private SensoryMemoryImpl sm;
//	private PerceptualAssociativeMemoryImpl pam;
//	//Workspace
//	private PerceptualBufferImpl pb;
//	private EpisodicBufferImpl eb;
//	private PreviousBroadcastsImpl pbroads;
//	private CurrentSituationalModelImpl csm = new CurrentSituationalModelImpl();
//	private WorkspaceImpl workspace;
//	private StructureBuildingCodeletDriver sbCodeletDriver;
//	//Episodic Memories
//	private TransientEpisodicMemory tem;
//	private DeclarativeMemory dm;	
//	//Attention
//	private AttentionDriver attnDriver;
//	private GlobalWorkspaceImpl gwksp;	
//	//Drivers
//	private PAMDriver pamDriver;
//	private EasyProceduralMemoryDriver procDriver;	
//	private CSMDriver csmDriver;
//	private ActionSelectionImpl bnet;	
//	//GUIS
//	private WumpusEnvironmentGui simGui = new WumpusEnvironmentGui();
//	private CSMGui csmGui = new CSMGui();
//	private NodeLinkFlowGui testGui;
//	//Threads and thread control
//	private List<Thread> threads = new ArrayList<Thread>();
//	private List<Stoppable> drivers = new ArrayList<Stoppable>();
//	private FrameworkTimer timer = new FrameworkTimer();
//	//Testing
//	private List<Environment> worlds = new ArrayList<Environment>();
//	
//	public Start(Environment e) {
//		worlds = e;
//	}
//
//	public void run(){
//		initSimThread();
//		initSMThread();
//		initPAMThread();
//		initPBufferThread();
//		initProcMemThread();//Needed for CSM, This implementation only!
//		initCSMThread();//Depends on GWKSP
//		initWorkspace();
//		initCodeletsThread();
//		initGui();//GUI last
//		//Define Observers
//		defineInterThreadCommunication();
//		//Start everything up, threads are terminated via GUI
//		startThreads();
//	}//setup	
//
//	private void initSimThread(){
//		simulation = new WumpusWorld(timer, worlds);				
//		threads.add(new Thread(simulation, "SIMULATION_THREAD"));   
//		drivers.add(simulation);	
//	}
//	private void initSMThread(){
//		sm = new SensoryMemoryImpl();
//		simulation.addEnvironmentListener(sm);
//		
//		SensoryMemoryDriver smDriver = new SensoryMemoryDriver(sm, timer);
//		threads.add(new Thread(smDriver, "SM_THREAD"));   
//		drivers.add(smDriver);				
//	}
//	private void initPAMThread(){
//		pam = new PerceptualAssociativeMemoryImpl();
//		
//		//SET PAM PARAMETERS
//		Map<String, Object> params = new HashMap<String, Object>();
//		double upscale = 0.7, 
//			   downscale = 0.5, 
//			   selectivity = 0.9;		
//		params.put("upscale", upscale);
//		params.put("downscale", downscale);
//		params.put("selectivity", selectivity);		
//		pam.setParameters(params);
//		
//		//ADD NODES, FeatureDetectors, and LINKS
//		Set<Node> nodes = new HashSet<Node>();
//        //  defaults used when creating the nodes below
//    	double baseActivation = 0.0;
//    	double currentActivation = 0.0;	
//    	int pfdType = 0;
//    	int regType = 1;
//
//    	//TODO: USE NODE FACTORY!!!
//    	//(ID, START-BLA, START-CLA, LABEL, TYPE) 	
//    	RyanPamNode gold = new RyanPamNode(WumpusNodeIDs.gold, baseActivation, currentActivation, 
//				"gold", regType);
//    	RyanPamNode pit = new RyanPamNode(WumpusNodeIDs.pit, baseActivation, currentActivation, 
//    						"pit", regType);
//    	RyanPamNode wumpus = new RyanPamNode(WumpusNodeIDs.wumpus, baseActivation, currentActivation, 
//				"wumpus", regType);
//    	RyanPamNode agent = new RyanPamNode(WumpusNodeIDs.agent, baseActivation, currentActivation, 
//			"agent", regType);
//    	RyanPamNode wall = new RyanPamNode(WumpusNodeIDs.wall, baseActivation, currentActivation, 
//				"wall", regType);
//    	//Next level
//    	RyanPamNode pleasure = new RyanPamNode(20, baseActivation, currentActivation, 
//				"pleasure", regType);
//    	RyanPamNode pain = new RyanPamNode(21, baseActivation, currentActivation, 
//				"pain", regType);
//    	
//    	int layerDepthA = 0;
//    	int layerDepthB = 1;
//    	gold.setLayerDepth(layerDepthA);
//    	pit.setLayerDepth(layerDepthA);
//    	wumpus.setLayerDepth(layerDepthA);
//    	agent.setLayerDepth(layerDepthA);
//    	wall.setLayerDepth(layerDepthA);
//    	//
//    	pleasure.setLayerDepth(layerDepthB);
//    	pain.setLayerDepth(layerDepthB);
//    	//Add nodes to a Set
//    	nodes.add(gold);
//    	nodes.add(pit);
//    	nodes.add(wumpus);
//    	nodes.add(agent);
//    	nodes.add(wall);
//    	//
//    	nodes.add(pleasure);
//    	nodes.add(pain);
//    
//    	//Feature detectors
//    	Map<String, Integer> codeMap = new HashMap<String, Integer>();
//    	codeMap.put("pit", 0);
//    	codeMap.put("wumpus", 1);
//    	codeMap.put("gold", 2);
//    	codeMap.put("agent", 3);	
//    	codeMap.put("wall", 4);
//    	List<FeatureDetector> featureDetectors = new ArrayList<FeatureDetector>();
//    	WumpusDetectBehavior featureDetectorBehavior = new WumpusDetectBehavior(codeMap); 
//		featureDetectors.add(new FeatureDetectorImpl(gold, featureDetectorBehavior));
//		featureDetectors.add(new FeatureDetectorImpl(pit, featureDetectorBehavior));
//		featureDetectors.add(new FeatureDetectorImpl(wumpus, featureDetectorBehavior));
//		featureDetectors.add(new FeatureDetectorImpl(agent, featureDetectorBehavior));
//		featureDetectors.add(new FeatureDetectorImpl(wall, featureDetectorBehavior));
//		
//		Set<Link> links = new HashSet<Link>();
////    	links.add(new LinkImpl(wall, pain, LinkType.child, 11));
//    	links.add(new LinkImpl(gold, pleasure, LinkType.child, 12 + ""));
//    	links.add(new LinkImpl(pit, pain, LinkType.child, 13 + ""));
//    	links.add(new LinkImpl(wumpus, pain, LinkType.child, 14 + ""));
//    	
//		pam.addToPAM(nodes, featureDetectors, links);
//		
//		//PAM THREAD		
//		pamDriver = new PAMDriver(pam, timer);
//		Thread pamThread = new Thread(pamDriver, "PAM_THREAD");
//		pamDriver.setThreadID(pamThread.getId());
//		threads.add(pamThread);   
//		drivers.add(pamDriver);
//	}//void initPAMThread()
//	
//	private void initWorkspace() {
//		workspace = new WorkspaceImpl(pb, eb, pbroads, csm, tem, dm, pam);		
//	}//method
//	
//	private void initPBufferThread(){
//		pb = new PerceptualBufferImpl();	
//		PerceptualBufferDriver pbDriver = new PerceptualBufferDriver(pb, timer);
//		Thread pBufferThread = new Thread(pbDriver, "PBUFFER");
//		pbDriver.setThreadID(pBufferThread.getId());
//		threads.add(pBufferThread);   
//		drivers.add(pbDriver);
//	}
//	private void initEBufferThread(){
//		eb = new EpisodicBufferImpl();
//		EpisodicBufferDriver ebDriver = new EpisodicBufferDriver(eb, timer);
//		Thread ebThread = new Thread(ebDriver, "EBUFFER");
//		ebDriver.setThreadID(ebThread.getId());		
//		threads.add(ebThread);   
//		drivers.add(ebDriver);
//	}
//	private void initPBroadsThread(){
//		pbroads = new PreviousBroadcastsImpl();
//		PreviousBroadcastsDriver pbDriver = new PreviousBroadcastsDriver(pbroads, timer);
//		Thread pbroadsThread = new Thread(pbDriver, "PBROADS");
//		pbDriver.setThreadID(pbroadsThread.getId());		
//		threads.add(pbroadsThread);   
//		drivers.add(pbDriver);		
//	}
//	private void initCodeletsThread() {
//		sbCodeletDriver = new StructureBuildingCodeletDriver(timer, workspace);		
//		Thread codeletThread = new Thread(sbCodeletDriver, "CODELETS_THREAD");
//		sbCodeletDriver.setThreadID(codeletThread.getId());
//		threads.add(codeletThread);   
//		drivers.add(sbCodeletDriver);			
//	}
//	private void initCSMThread(){
//		csmDriver = new CSMDriver(timer, csm, csmGui);
//		Thread csmThread = new Thread(csmDriver, "CSM_THREAD");
//		csmDriver.setThreadID(csmThread.getId());
//		threads.add(csmThread);
//		drivers.add(csmDriver);
//	}
//	
//	private void initGlobalWorkspace() {
//		gwksp = new GlobalWorkspaceImpl();
//		Trigger tr;
//		Map<String, Object> parameters;
//		
//		tr = new TimeOutTrigger();
//		parameters = new HashMap<String, Object>();
//		parameters.put("name", "TimeOut");
//		parameters.put("delay", 100L);
//		tr.setUp(parameters, (TriggerListener) gwksp);
//		gwksp.addTrigger(tr);
//
////		tr = new AggregateActivationTrigger();
////		parameters = new HashMap<String, Object>();
////		parameters.put("threshold", 0.8);
////		tr.setUp(parameters, (TriggerListener) gwksp);
////		gwksp.addTrigger(tr);
////
////		tr = new TimeOutLapTrigger();
////		parameters = new HashMap<String, Object>();
////		parameters.put("name", "TimeOutLap");
////		parameters.put("delay", 50L);
////		tr.setUp(parameters, (TriggerListener) gwksp);
////		gwksp.addTrigger(tr);
////
////		tr = new IndividualActivationTrigger();
////		parameters = new HashMap<String, Object>();
////		parameters.put("threshold", 0.5);
////		tr.setUp(parameters, (TriggerListener) gwksp);
////		gwksp.addTrigger(tr);
//	}//method
//	
//	private void initAttnThread(){
//		AttentionDriver attnDriver = new AttentionDriver(timer, csm, gwksp);
//		Thread attnThread = new Thread(attnDriver, "ATTN_DRIVER");
//		attnDriver.setThreadID(attnThread.getId());
//		threads.add(attnThread);
//		drivers.add(attnDriver);
//	}
//	private void initProcMemThread(){
//		procDriver = new EasyProceduralMemoryDriver(timer, simulation);
//		Thread procThread = new Thread(procDriver, "PROCEDURAL_DRIVER");
//		procDriver.setThreadID(procThread.getId());
//		threads.add(procThread);
//		drivers.add(procDriver);
//	}
//	private void initActionSelectionThread(){
//		//TODO: impl
//	}
//	
//	private void initGui() {	
//		//**For basic control of the system
//		new ControlPanelGui(timer, this, sbCodeletDriver, simulation, procDriver).setVisible(true);	
//		
//		//**To view the simulation
//		simulation.addEnvironmentListener(simGui);
//		simGui.setVisible(true);
//		
//		//**For GUI action control
//		new ActionControlGui(simulation, procDriver).setVisible(true);
//		
//		//**GUI to see how many nodes and links are traveling among the modules 
//		testGui = new NodeLinkFlowGui();
//		//testGui = new TestGui(pam, pb, sbCodeletDriver, csmDriver, gwksp, procDriver);
//		pamDriver.addTestGui(testGui);
//		pb.addTestGui(testGui);
//		sbCodeletDriver.addTestGui(testGui);
//		csmDriver.addTestGui(testGui);
//		//gwksp.addTestGui(testGui);
//		procDriver.addTestGui(testGui);
//		//actionSelection.addTestGui(testGui);	
//		testGui.setVisible(true);
//		
//			
//		//**GUI to see the contents of the CSM
//		csm.addCSMListener(csmGui);
//		csmGui.setVisible(true);
//	}
//	
//	//
//	private void defineInterThreadCommunication(){
//		simulation.addEnvironmentListener(sm);
//		sm.addSensoryListener(pam);
//		pam.addPAMListener(workspace);
//		pb.addPBufferListener(workspace);
//		csm.addCSMListener(workspace);
//		workspace.addCodeletWorkspaceListener(sbCodeletDriver);
//		workspace.addWorkspaceListener(procDriver);
//		
//		//sm.addSensoryListener(sma);//Sensory motor automata connection not to be implemented
//		//eb.addEBufferListener(workspace);
//		//pbroads.addPBroadsListener(workspace);		
//		//eb.addEBufferListener(codelets);
//		//pbroads.addPBroadsListener(codelets);
//		//bnet.addBehaviorListener(workspace);		
//		//gwksp.addBroadcastListener(pam);
//		//gwksp.addBroadcastListener(workspace);
//		//gwksp.addBroadcastListener(tem);
//		//gwksp.addBroadcastListener(attnDriver);
//		//gwksp.addBroadcastListener(procedMemory);
//		//bnet.addBehaviorListener(simulation);
//	}//method
//	
//	private void startThreads(){ //in normal order
//		int size = threads.size();
//		for(int i = 0; i < size; i++){
//			Thread t = threads.get(i);
//			if(t != null)
//				t.start();
//			else
//				System.out.println("There was a null thread object in start");
//		}//for
//	}//method
//	
//	public void stopThreads(){
//		System.out.println("\nMain thread is initiating shutdown...");		
//		int size = drivers.size();
//		for(int i = 0; i < size; i++){			
//			Stoppable s = drivers.get(size - 1 - i);//Stop in reverse order
//			if(s != null)
//				s.stopRunning();					
//		}//for	
//	}//method	
//
//	/**
//	 * For ControlGUI to display thread count
//	 * 
//	 * @return
//	 */
//	public int getThreadCount() {
//		return threads.size();
//	}
//
//	public void waitForFinish() {
//		pamDriver.waitForFinish();		
//	}
//	
//}//public class Start