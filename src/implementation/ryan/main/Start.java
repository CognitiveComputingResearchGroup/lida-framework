/*
 * Start.java - Initializes, starts, and ends the threads 
 * that run the main LIDA components.  Sets up the shared memory 
 * that the threads use to pass data. 
 */
package implementation.ryan.main;

import implementation.ryan.gui.ControlGui;
import implementation.ryan.gui.SimGui;
import implementation.ryan.wumpus.WorldApplication;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.lang.Thread; 
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import edu.memphis.ccrg.lida.attention.AttentionDriver;
import edu.memphis.ccrg.lida.behaviorNet.BehaviorNetImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.perception.LinkImpl;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.perception.PAMDriver;
import edu.memphis.ccrg.lida.perception.PerceptualAssociativeMemoryImpl;
import edu.memphis.ccrg.lida.perception.featureDetection.FeatureDetectorImpl;
import edu.memphis.ccrg.lida.perception.featureDetection.WumpusDetectBehavior;
import edu.memphis.ccrg.lida.perception.interfaces.FeatureDetector;
import edu.memphis.ccrg.lida.proceduralMemory.ProceduralMemoryDriver;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryDriver;
import edu.memphis.ccrg.lida.sensoryMemory.SensoryMemoryImpl;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.LinkType;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CSMDriver;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModelImpl;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBufferDriver;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBufferImpl;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBufferDriver;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBufferImpl;
import edu.memphis.ccrg.lida.workspace.previousBroadcasts.PreviousBroadcastsDriver;
import edu.memphis.ccrg.lida.workspace.previousBroadcasts.PreviousBroadcastsImpl;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.StructureBuildingCodeletDriver;

public class Start{
	
	//SHARED "DATA STRUCTURES"
	private WorldApplication simulation;
	private SensoryMemoryImpl sm;
	private PerceptualAssociativeMemoryImpl pam;
	//
	private PerceptualBufferImpl pb;
	private EpisodicBufferImpl eb;
	private PreviousBroadcastsImpl pbroads;
	//
	private StructureBuildingCodeletDriver sbCodeletDriver;
	private CurrentSituationalModelImpl csm = new CurrentSituationalModelImpl();
	private GlobalWorkspaceImpl gwksp = new GlobalWorkspaceImpl();	
	private BehaviorNetImpl bnet;
	
	//GUIS
	private SimGui simGui = new SimGui();
		
	private List<Thread> threads = new ArrayList<Thread>();
	private List<Stoppable> drivers = new ArrayList<Stoppable>();
	private FrameworkTimer timer = new FrameworkTimer();
	
	public static void main(String[] args) {
		//new Start().simTest();
		new Start().setup();
	}//main
	
//	private void simTest(){
//		initSimThread();
//		startThreads();
//		try{Thread.sleep(900);}catch(Exception e){}
//		stopThreads();
//		
//	}
	
	private void setup(){
		initSimThread();
		initSMThread();
		initPAMThread();
		
		//Workspace Components
		initPBufferThread();
		//initEbufferThread();
		//initPBroadsThread();
		initCSMThread();
		
		//These two use & depend on the components
		initCodeletsThread();
		//initAttnThread();//Depends on CSM and GWKSPC
		
		//Global Workspace already taken care of
		initProcMemThread();
		//initActionSelectionThread();
		
		//GUI last
		initGui();
		
		//Add all the listeners  
		defineInter_ThreadCommunication();
		
		//Start everything up, threads are terminated via GUI
		startThreads();
	}//setup	

	private void initSimThread(){
		simulation = new WorldApplication(timer);				
		threads.add(new Thread(simulation, "SIMULATION_THREAD"));   
		drivers.add(simulation);	
	}
	private void initSMThread(){
		sm = new SensoryMemoryImpl();
		simulation.addSimulationListener(sm);
		
		SensoryMemoryDriver smDriver = new SensoryMemoryDriver(sm, timer);
		threads.add(new Thread(smDriver, "SM_THREAD"));   
		drivers.add(smDriver);				
	}
	private void initPAMThread(){
		pam = new PerceptualAssociativeMemoryImpl();
		
		//SET PAM PARAMS
		Map<String, Object> params = new HashMap<String, Object>();
		double upscale = 0.7, 
			   downscale = 0.5, 
			   selectivity = 0.9;		
		params.put("upscale", upscale);
		params.put("downscale", downscale);
		params.put("selectivity", selectivity);		
		pam.setParameters(params);
		
		//ADD NODES AND LINKS
		Set<Node> nodes = new HashSet<Node>();
		Set<Link> links = new HashSet<Link>();
		
        //  defaults used when creating the nodes below
    	double baseActivation = 0.0;
    	double currentActivation = 0.0;	
    	int pfdType = 0;
    	int regType = 1;
    	
    	//(ID, START-BLA, START-CLA, LABEL, TYPE) 	
//    	PamNodeImpl bump = new PamNodeImpl(01, baseActivation, currentActivation, 
//									"bump", pfdType);
//    	PamNodeImpl glitter = new PamNodeImpl(02, baseActivation, currentActivation, 
//									"glitter", pfdType);
//    	PamNodeImpl breeze  = new PamNodeImpl(03, baseActivation, currentActivation, 
//    								"breeze", pfdType);       
//    	PamNodeImpl stench = new PamNodeImpl(04, baseActivation, currentActivation, 
//    								"stench", pfdType);
//    	PamNodeImpl scream = new PamNodeImpl(05, baseActivation, currentActivation, 
//									"scream", pfdType);
//    	bump.setLayerDepth(0);
//    	glitter.setLayerDepth(0);
//    	breeze.setLayerDepth(0);
//    	stench.setLayerDepth(0);
//    	scream.setLayerDepth(0);
    	
    	//Feature detectors
    	Map<String, Integer> codeMap = new HashMap<String, Integer>();
//    	codeMap.put("bump", 0);
//		codeMap.put("glitter", 1);
//		codeMap.put("breeze", 2);
//		codeMap.put("stench", 3);
//		codeMap.put("scream", 4);
    	codeMap.put("pit", 0);
    	codeMap.put("wumpus", 1);
    	codeMap.put("gold", 2);
    	codeMap.put("agent", 3);		
		
		WumpusDetectBehavior featureDetectBehav = new WumpusDetectBehavior(codeMap);   
   
		Set<FeatureDetector> featureDetectors = new HashSet<FeatureDetector>();
//		//TODO: equals and hashcode for PAMFeatureDetectors
//		featureDetectors.add(new FeatureDetectorImpl(bump, featureDetectBehav));
//		featureDetectors.add(new FeatureDetectorImpl(glitter, featureDetectBehav));
//		featureDetectors.add(new FeatureDetectorImpl(breeze, featureDetectBehav));
//		featureDetectors.add(new FeatureDetectorImpl(stench, featureDetectBehav));
//		featureDetectors.add(new FeatureDetectorImpl(scream, featureDetectBehav));
    	
    	//1st level
//    	PamNodeImpl wall = new PamNodeImpl(11, baseActivation, currentActivation, 
//				"wall", regType);
    	PamNodeImpl gold = new PamNodeImpl(12, baseActivation, currentActivation, 
				"gold", regType);
    	PamNodeImpl pit = new PamNodeImpl(13, baseActivation, currentActivation, 
    						"pit", regType);
    	PamNodeImpl wumpus = new PamNodeImpl(14, baseActivation, currentActivation, 
				"wumpus", regType);
    	PamNodeImpl agent = new PamNodeImpl(15, baseActivation, currentActivation, 
				"agent", regType);
    	
    	//TODO: USE NODE FACTORY!!!
    	
		featureDetectors.add(new FeatureDetectorImpl(gold, featureDetectBehav));
		featureDetectors.add(new FeatureDetectorImpl(pit, featureDetectBehav));
		featureDetectors.add(new FeatureDetectorImpl(wumpus, featureDetectBehav));
		featureDetectors.add(new FeatureDetectorImpl(agent, featureDetectBehav));
    	
    	int layerDepthA = 0;
//    	PamNodeImpl wumpusDeath = new PamNodeImpl(15, baseActivation, currentActivation, 
//				"wumpusDeath", regType);
    	
//    	wall.setLayerDepth(layerDepthA);
    	gold.setLayerDepth(layerDepthA);
    	pit.setLayerDepth(layerDepthA);
    	wumpus.setLayerDepth(layerDepthA);
    	agent.setLayerDepth(layerDepthA);
//    	wumpusDeath.setLayerDepth(layerDepthA);

    	//Next level
    	PamNodeImpl pleasure = new PamNodeImpl(20, baseActivation, currentActivation, 
				"pleasure", regType);
    	PamNodeImpl pain = new PamNodeImpl(21, baseActivation, currentActivation, 
				"pain", regType);
    	int layerDepthB = 1;
    	pleasure.setLayerDepth(layerDepthB);
    	pain.setLayerDepth(layerDepthB);
    
    	//FD nodes to 2nd level
//    	links.add(new LinkImpl(bump, wall, LinkType.child, 01));
//    	links.add(new LinkImpl(glitter, gold, LinkType.child, 02));
//    	links.add(new LinkImpl(breeze, pit, LinkType.child, 03));
//    	links.add(new LinkImpl(stench, wumpus, LinkType.child, 04));
//    	links.add(new LinkImpl(scream, wumpusDeath, LinkType.child, 05));

//    	links.add(new LinkImpl(wall, pain, LinkType.child, 11));
    	links.add(new LinkImpl(gold, pleasure, LinkType.child, 12));
    	links.add(new LinkImpl(pit, pain, LinkType.child, 13));
    	links.add(new LinkImpl(wumpus, pain, LinkType.child, 14));
//    	links.add(new LinkImpl(wumpusDeath, pleasure, LinkType.child, 15));
    	
    	//Put nodes in a set
//    	nodes.add(bump);
//    	nodes.add(glitter);
//       	nodes.add(breeze); 	
//    	nodes.add(stench);
//    	nodes.add(scream); 
//    	//
//    	nodes.add(wall);
    	nodes.add(gold);
    	nodes.add(pit);
    	nodes.add(wumpus);
    	nodes.add(agent);
//    	nodes.add(wumpusDeath);
    	//
    	nodes.add(pleasure);
    	nodes.add(pain);
    	//
		pam.addToPAM(nodes, featureDetectors, links);
		
		//PAM THREAD		
		PAMDriver pamDriver = new PAMDriver(pam, timer);
		Thread pamThread = new Thread(pamDriver, "PAM_THREAD");
		pamDriver.setThreadID(pamThread.getId());
		threads.add(pamThread);   
		drivers.add(pamDriver);
	}//void initPAMThread()
	
	private void initPBufferThread(){
		pb = new PerceptualBufferImpl();	
		PerceptualBufferDriver pbDriver = new PerceptualBufferDriver(pb, timer);
		Thread pBufferThread = new Thread(pbDriver, "PBUFFER");
		pbDriver.setThreadID(pBufferThread.getId());
		threads.add(pBufferThread);   
		drivers.add(pbDriver);
	}
	private void initEBufferThread(){
		eb = new EpisodicBufferImpl();
		EpisodicBufferDriver ebDriver = new EpisodicBufferDriver(eb, timer);
		Thread ebThread = new Thread(ebDriver, "EBUFFER");
		ebDriver.setThreadID(ebThread.getId());		
		threads.add(ebThread);   
		drivers.add(ebDriver);
	}
	private void initPBroadsThread(){
		pbroads = new PreviousBroadcastsImpl();
		PreviousBroadcastsDriver pbDriver = new PreviousBroadcastsDriver(pbroads, timer);
		Thread pbroadsThread = new Thread(pbDriver, "PBROADS");
		pbDriver.setThreadID(pbroadsThread.getId());		
		threads.add(pbroadsThread);   
		drivers.add(pbDriver);		
	}
	private void initCodeletsThread() {
		sbCodeletDriver = new StructureBuildingCodeletDriver(timer, pb, eb, pbroads, csm);		
		Thread codeletThread = new Thread(sbCodeletDriver, "CODELETS_THREAD");
		sbCodeletDriver.setThreadID(codeletThread.getId());
		threads.add(codeletThread);   
		drivers.add(sbCodeletDriver);			
	}
	private void initCSMThread(){
		CSMDriver csmDriver = new CSMDriver(timer, csm, gwksp);
		Thread csmThread = new Thread(csmDriver, "CSM_THREAD");
		csmDriver.setThreadID(csmThread.getId());
		threads.add(csmThread);
		drivers.add(csmDriver);
	}
	private void initAttnThread(){
		AttentionDriver attnDriver = new AttentionDriver(timer);
		Thread attnThread = new Thread(attnDriver, "ATTN_DRIVER");
		attnDriver.setThreadID(attnThread.getId());
		threads.add(attnThread);
		drivers.add(attnDriver);
	}
	private void initProcMemThread(){
		ProceduralMemoryDriver procDriver = new ProceduralMemoryDriver(timer, simulation);
		Thread procThread = new Thread(procDriver, "PROCEDURAL_DRIVER");
		procDriver.setThreadID(procThread.getId());
		threads.add(procThread);
		drivers.add(procDriver);
	}
	private void initActionSelectionThread(){
		//TODO: impl
	}
	private void initGui() {		
		new ControlGui(timer, this, pam).setVisible(true);	
		simGui.setVisible(true);
	}
	
	//
	private void defineInter_ThreadCommunication(){
		simulation.addSimulationListener(sm);
		simulation.addSimulationListener(simGui);
		
		sm.addSensoryListener(pam);
		//sm.addSensoryListener(sma);//Sensory motor automata connection not to be implemented
		
		pam.addPAMListener(pb);
		
		pb.addPBufferListener(sbCodeletDriver);
		//pb.addPBufferListener(dm);
		//pb.addPBufferListener(tem);
		
		//eb.addEBufferListener(codelets);
		//eb.addEBufferListener(pam);
		//eb.addEBufferListener(dm);
		//eb.addPBufferListener(tem);
		
		//pbroads.addPBroadsListener(codelets);
 
		//bnet.addBehaviorListener(csm);		
		//csm.addCSMListener(tem);
		//csm.addCSMListener(dm);
		
		//attnDriver.addCSM(csm);
		
		//gwksp.addBroadcastListener(pam);
		//gwksp.addBroadcastListener(pbroads);
		//gwksp.addBroadcastListener(tem);
		//gwksp.addBroadcastListener(attnDriver);
		//gwksp.addBroadcastListener(procedMemory);
	
		gwksp.addBroadcastListener(bnet);		
		
		//bnet.addBehaviorListener(simulation);
	}
		
	private void startThreads(){ //in normal order
		int size = threads.size();
		for(int i = 0; i < size; i++){
			Thread t = threads.get(i);
			if(t != null)
				t.start();
			else
				System.out.println("oh crap a null thread object");
		}//for each thread
	}//private void startThreads()
	
	public void stopThreads(){
		System.out.println("\nMain thread is initiating shutdown...");		
		int size = drivers.size();
		for(int i = 0; i < size; i++){			
			Stoppable s = drivers.get(size - 1 - i);
			if(s != null)
				s.stopRunning();					
		}//for each thread	
	}//private void stopThreads()	

	/**
	 * For ControlGUI
	 * 
	 * @return
	 */
	public int getThreadCount() {
		return threads.size();
	}
	
}//public class Start