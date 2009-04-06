package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida._perception.GraphImpl;
import edu.memphis.ccrg.lida._perception.PamNodeImpl;
import edu.memphis.ccrg.lida._perception.SpatialLocation;
import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.util.Misc;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModelImpl;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBuffer;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBufferContentImpl;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBufferImpl;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBufferContentImpl;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBufferListener;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBufferImpl;
import edu.memphis.ccrg.lida.workspace.previousBroadcasts.PreviousBroadcasts;
import edu.memphis.ccrg.lida.workspace.previousBroadcasts.PreviousBroadcastsContentImpl;
import edu.memphis.ccrg.lida.workspace.previousBroadcasts.PreviousBroadcastsImpl;

public class StructureBuildingCodeletDriver implements Runnable, Stoppable, PerceptualBufferListener {

	//Basics
	private boolean keepRunning = true;
	private FrameworkTimer timer;	
	private long threadID;
	
	//For this Driver, contents of the buffers and codeletMap	
	private PerceptualBufferContentImpl pBufferContent = new PerceptualBufferContentImpl();
	//Not yet implemented
	private EpisodicBufferContentImpl eBufferContent = new EpisodicBufferContentImpl();
	private PreviousBroadcastsContentImpl prevBroadcastContent = new PreviousBroadcastsContentImpl();
	//
	private Map<CodeletActivatingContext, StructureBuildingCodeletImpl> codeletMap = new HashMap<CodeletActivatingContext, StructureBuildingCodeletImpl>();//TODO: equals, hashCode
	
	//For codelets to be able to move contents around.
	private PerceptualBufferImpl pBuffer = null;
	private EpisodicBufferImpl eBuffer = null;
	private PreviousBroadcastsImpl pBroads = null;
	private CurrentSituationalModelImpl csm = null;
	private final double defaultCodeletActivation = 1.0;	
	
	private List<Thread> codeletThreads = new ArrayList<Thread>();
	private List<Stoppable> codelets = new ArrayList<Stoppable>();
	private FrameworkGui testGui;
	
	public StructureBuildingCodeletDriver(FrameworkTimer timer, PerceptualBufferImpl p, EpisodicBufferImpl e, 
							PreviousBroadcastsImpl pbroads, CurrentSituationalModelImpl csm){
		this.timer = timer;
		pBuffer = p;
		eBuffer = e;
		this.pBroads = pbroads;
		this.csm = csm;		
	}

	public void run(){
		Map<Long, Node> nodes = new HashMap<Long, Node>();
		PamNodeImpl pit = new PamNodeImpl(13, 0.0, 0.0, "pit", 0);
		nodes.put(13L, pit);
		
		CodeletObjective objective = new CodeletObjective(nodes);
		CodeletAction actions = new TestCodeletAction();		
		spawnNewCodelet(pBuffer, null, null, defaultCodeletActivation, objective, actions);
		
		int counter = 0;		
		long startTime = System.currentTimeMillis();		
		while(keepRunning){
			try{Thread.sleep(24 + timer.getSleepTime());
			}catch(Exception e){}//TODO: if PBUFFER Content is changed wake up
			timer.checkForStartPause();
			//if BufferContent activates a sbCodelet's context start a new codelet
			getPBufferContent();
		
			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();				
		System.out.println("\nCODE: Ave. cycle time: " + 
							Misc.rnd((finishTime - startTime)/(double)counter));
		System.out.println("CODE: Num. cycles: " + counter);		
	}//public void run()

	private void spawnNewCodelet(PerceptualBufferImpl pBuffer, EpisodicBuffer eBuffer, PreviousBroadcasts pBroads,
								 double startingActivation, CodeletObjective context, CodeletAction actions) {
		StructureBuildingCodeletImpl newCodelet = new StructureBuildingCodeletImpl(timer, pBuffer, null, null, csm, 
					  							  defaultCodeletActivation, context, actions);
		long threadNumber = codeletThreads.size() + 1;
		Thread codeletThread = new Thread(newCodelet, "CODELET: " + threadNumber);
		newCodelet.setThreadID(codeletThread.getId());
		codeletThreads.add(codeletThread);   
		codelets.add(newCodelet);	
		codeletThread.start();	
	}//spawnNewCodelet

	private void getPBufferContent() {
		GraphImpl struct = null;
		synchronized(this){
			struct = (GraphImpl)pBufferContent.getContent();
		}

		
		Set<Node> nodes = struct.getNodes();
		for(Node n: nodes){//TODO: should be in PamNode instead
			//Activate codelets
		}
		
		List<Object> guiContent = new ArrayList<Object>();			
		guiContent.add(struct.getNodes().size());
		guiContent.add(struct.getLinks().size());			
		testGui.receiveGuiContent(FrameworkGui.SB_CODELETS, guiContent);
	}//method

	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//public void stopRunning()
	
	public void setThreadID(long id){
		threadID = id;
	}
	
	public long getThreadID() {
		return threadID;
	}

	public void addCSM(CurrentSituationalModelImpl csm) {
		this.csm = csm;		
	}

	public synchronized void receivePBufferContent(PerceptualBufferContentImpl pbc) {
		pBufferContent = pbc;		
	}

	public void addTestGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}

}//public class SBCodeletsDriver 
