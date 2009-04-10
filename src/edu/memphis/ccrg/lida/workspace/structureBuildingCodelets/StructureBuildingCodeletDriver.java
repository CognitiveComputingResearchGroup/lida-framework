package edu.memphis.ccrg.lida.workspace.structureBuildingCodelets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.util.Misc;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBuffer;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBufferContent;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBuffer;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBufferContent;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBufferListener;
import edu.memphis.ccrg.lida.workspace.previousBroadcasts.PreviousBroadcasts;
import edu.memphis.ccrg.lida.workspace.previousBroadcasts.PreviousBroadcastsContent;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.PamNodeImpl;
import edu.memphis.ccrg.lida.wumpusWorld.f_sbCodelets.SpatialLinkCodeletAction;
import edu.memphis.ccrg.lida.wumpusWorld.f_sbCodelets.StructureBuildingCodeletImpl;

public class StructureBuildingCodeletDriver implements Runnable, Stoppable, PerceptualBufferListener {

	//Basics
	private boolean keepRunning = true;
	private FrameworkTimer timer;	
	private long threadID;
	
	//For this Driver, contents of the buffers and codeletMap	
	private PerceptualBufferContent pBufferContent = null;
	//Not yet implemented
	private EpisodicBufferContent eBufferContent = null;
	private PreviousBroadcastsContent prevBroadcastContent = null;
	//
	private Map<CodeletActivatingContext, StructureBuildingCodelet> codeletMap = new HashMap<CodeletActivatingContext, StructureBuildingCodelet>();//TODO: equals, hashCode
	
	//For codelets to be able to move contents around.
	private PerceptualBuffer pBuffer = null;
	private EpisodicBuffer eBuffer = null;
	private PreviousBroadcasts pBroads = null;
	private CurrentSituationalModel csm = null;
	private final double defaultCodeletActivation = 1.0;	
	
	private List<Thread> codeletThreads = new ArrayList<Thread>();
	private List<Stoppable> codelets = new ArrayList<Stoppable>();
	private FrameworkGui testGui;
	
	public StructureBuildingCodeletDriver(FrameworkTimer timer, PerceptualBuffer p, EpisodicBuffer e, 
							PreviousBroadcasts pbroads, CurrentSituationalModel csm){
		this.timer = timer;
		pBuffer = p;
		eBuffer = e;
		this.pBroads = pbroads;
		this.csm = csm;		
	}//method

	public void run(){
		Map<Long, Node> nodes = new HashMap<Long, Node>();
		PamNodeImpl pit = new PamNodeImpl(13, 0.0, 0.0, "pit", 0);
		nodes.put(13L, pit);
		
		CodeletsDesiredContent objective = new CodeletsDesiredContent(nodes);
		CodeletAction actions = new SpatialLinkCodeletAction();		
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
	}//method

	private void spawnNewCodelet(PerceptualBuffer pBuffer, EpisodicBuffer eBuffer, PreviousBroadcasts pBroads,
								 double startingActivation, CodeletsDesiredContent context, CodeletAction actions) {
		StructureBuildingCodelet newCodelet = new StructureBuildingCodeletImpl(timer, pBuffer, null, null, csm, 
					  							  defaultCodeletActivation, context, actions);
		long threadNumber = codeletThreads.size() + 1;
		Thread codeletThread = new Thread((Runnable)newCodelet, "CODELET: " + threadNumber);
		((Stoppable) newCodelet).setThreadID(codeletThread.getId());
		codeletThreads.add(codeletThread);   
		codelets.add((Stoppable) newCodelet);	
		codeletThread.start();	
	}//method

	private void getPBufferContent() {
		if(pBufferContent != null){
			NodeStructure struct = null;
			synchronized(this){
				struct = (NodeStructure)pBufferContent.getContent();
			}
			Set<Node> nodes = struct.getNodes();
			for(Node n: nodes){//TODO: should be in PamNode instead
				//Activate codelets
			}
			
			List<Object> guiContent = new ArrayList<Object>();			
			guiContent.add(struct.getNodes().size());
			guiContent.add(struct.getLinks().size());			
			testGui.receiveGuiContent(FrameworkGui.FROM_SB_CODELETS, guiContent);
		}//if
	}//method

	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//method
	
	public void setThreadID(long id){
		threadID = id;
	}//
	
	public long getThreadID() {
		return threadID;
	}//

	public void addCSM(CurrentSituationalModel csm) {
		this.csm = csm;		
	}//

	public synchronized void receivePBufferContent(PerceptualBufferContent pbc) {
		pBufferContent = pbc;		
	}//

	public void addTestGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}//

}//public class SBCodeletsDriver 