package edu.memphis.ccrg.lida.workspace.sbCodelets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeFactory;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.perception.GraphImpl;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.perception.Percept;
import edu.memphis.ccrg.lida.perception.SpatialLocation;
import edu.memphis.ccrg.lida.util.Misc;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.broadcasts.PreviousBroadcasts;
import edu.memphis.ccrg.lida.workspace.broadcasts.PreviousBroadcastsContentImpl;
import edu.memphis.ccrg.lida.workspace.broadcasts.PreviousBroadcastsImpl;
import edu.memphis.ccrg.lida.workspace.csm.CurrentSituationalModelImpl;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBuffer;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBufferContentImpl;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBufferImpl;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBufferContentImpl;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBufferListener;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBufferImpl;

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
	private final boolean usesPBuffer = true, usesEBuffer = true, usesPBroads = true;
	
	private List<Thread> codeletThreads = new ArrayList<Thread>();
	private List<Stoppable> codelets = new ArrayList<Stoppable>();
	private String defaultNodeName = "edu.memphis.ccrg.lida.shared.NodeImp";
	
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
		Node n = NodeFactory.getInstance().getNode();
		n.setLabel("pit");
		nodes.put(n.getId(), n);
		
		CodeletObjective objective = new CodeletObjective(nodes);
		CodeletAction actions = new CodeletAction();		
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
		NodeStructure struct = null;
		synchronized(this){
			struct = (GraphImpl)pBufferContent.getContent();
		}
		
		//System.out.println("strcut" + struct);
		
		if(struct != null){
			Set<Node> nodes = struct.getNodes();
			//percept.print(keepRunning, "SB-CODELETS");
			
			System.out.println("nodes " + nodes);
			
			if(nodes != null){
				System.out.println(nodes.size());
				Set<SpatialLocation> locations = null;
				for(Node n: nodes){//TODO: should be in PamNode instead
	//				locations = n.getLocations();
	//				for(SpatialLocation s: locations){
	//					s.print();
	//				}
					System.out.println("\n");
				}
			}
		}//if struct not null
	}

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

}//public class SBCodeletsDriver 
