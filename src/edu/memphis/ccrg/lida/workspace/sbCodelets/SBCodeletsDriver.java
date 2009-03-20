package edu.memphis.ccrg.lida.workspace.sbCodelets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.perception.Percept;
import edu.memphis.ccrg.lida.perception.SpatialLocation;
import edu.memphis.ccrg.lida.util.Misc;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.broadcasts.PrevBroadcastContent;
import edu.memphis.ccrg.lida.workspace.broadcasts.PreviousBroadcasts;
import edu.memphis.ccrg.lida.workspace.csm.CSM;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EBufferContent;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBuffer;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PBufferContent;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PBufferListener;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PerceptualBuffer;

public class SBCodeletsDriver implements Runnable, Stoppable, PBufferListener {

	//Basics
	private boolean keepRunning = true;
	private FrameworkTimer timer;	
	private long threadID;
	
	//For this Driver, contents of the buffers and codeletMap	
	private PBufferContent pBufferContent = new PBufferContent();
	private Percept percept = new Percept();
	//Not yet implemented
	private EBufferContent eBufferContent = new EBufferContent();
	private PrevBroadcastContent prevBroadcastContent = new PrevBroadcastContent();
	//
	private Map<Context, SBCodelet> codeletMap = new HashMap<Context, SBCodelet>();//TODO: equals, hashCode
	
	//For codelets to be able to move contents around.
	private PerceptualBuffer pBuffer = null;
	private EpisodicBuffer eBuffer = null;
	private PreviousBroadcasts pBroads = null;
	private CSM csm = null;
	private final double defaultCodeletActivation = 1.0;	
	private final boolean usesPBuffer = true, usesEBuffer = true, usesPBroads = true;
	
	private List<Thread> codeletThreads = new ArrayList<Thread>();
	private List<Stoppable> codelets = new ArrayList<Stoppable>();
	
	public SBCodeletsDriver(FrameworkTimer timer, PerceptualBuffer p, EpisodicBuffer e, 
							PreviousBroadcasts pbroads, CSM csm){
		this.timer = timer;
		pBuffer = p;
		eBuffer = e;
		this.pBroads = pbroads;
		this.csm = csm;		
	}

	public void run(){
		Context context = new Context();
		CodeletAction actions = new CodeletAction();		
		spawnNewCodelet(usesPBuffer, !usesEBuffer, !usesPBroads, defaultCodeletActivation, context, actions);
		
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

	private void spawnNewCodelet(boolean usesPBuffer, boolean usesEBuffer, boolean usesPBroads,
								 double startingActivation, Context context, CodeletAction actions) {
		if(usesPBuffer || usesEBuffer || usesPBroads){
			SBCodelet newCodelet = new SBCodelet(timer, pBuffer, null, null, csm, 
					  							  defaultCodeletActivation, context, actions);

			long threadNumber = codeletThreads.size() + 1;
			Thread codeletThread = new Thread(newCodelet, "CODELET: " + threadNumber);
			newCodelet.setThreadID(codeletThread.getId());
			codeletThreads.add(codeletThread);   
			codelets.add(newCodelet);	
			codeletThread.start();			
		}//if at least 1 buffer is specified			
	}//spawnNewCodelet

	private void getPBufferContent() {
		synchronized(this){
			percept = (Percept)pBufferContent.getContent();
		}
		percept.print(keepRunning, "SB-CODELETS");
		
		Set<SpatialLocation> locations = null;
		for(PamNodeImpl n: percept){//TODO: should be in PamNode instead
			locations = n.getLocations();
			for(SpatialLocation s: locations){
				s.print();
			}
			System.out.println("\n");
		}
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

	public void addCSM(CSM csm) {
		this.csm = csm;		
	}

	public synchronized void receivePBufferContent(PBufferContent pbc) {
		pBufferContent = pbc;		
	}

}//public class SBCodeletsDriver 
