/*
 * Start.java - Initializes, starts, and ends the threads 
 * that run the main LIDA components.  Sets up the shared memory 
 * that the threads use to pass data. 
 */
package edu.memphis.ccrg.main;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import edu.memphis.ccrg.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.perception.PerceptualAssociativeMemory;
import edu.memphis.ccrg.sensoryMemory.SensoryMemory;
import edu.memphis.ccrg.workspace.CSM.CSM;
import edu.memphis.ccrg.workspace.broadcasts.PreviousBroadcasts;
import edu.memphis.ccrg.workspace.episodicBuffer.EpisodicBuffer;
import edu.memphis.ccrg.workspace.perceptualBuffer.PerceptualBuffer;
import edu.memphis.ccrg.workspace.scratchpad.ScratchPad;
import edu.memphis.ccrg.wumpus.WorldApplication;

public class Start{
	
	private Map<Thread, Stoppable> threadMap = new TreeMap<Thread, Stoppable>();
	
	private WorldApplication simulation;
	private SensoryMemory sm;
	private PerceptualAssociativeMemory pam;
	private PerceptualBuffer pb;
	private EpisodicBuffer eb;
	private PreviousBroadcasts pbroads;
	private ScratchPad sPad;
	private CSM csm;
	private GlobalWorkspaceImpl gwksp;	

	public static void main(String[] args) {
		new Start().setup();
	}//main
	
	public void setup(){
		initSimThread();
		initSMThread();
		initPAMThread();
		initPBufferThread();
		//initEbufferThread();
		//initPBroadsThread();
		//initSPadThread();	
		//initCSMThread();
		//initAttnThread();
		//initGWThread();
		
		//
		simulation.addSimulationListener(sm);
		
		sm.addSensoryListener(pam);
		//sm.addSensoryListener(sma);
		
		pam.addPAMListener(pb);
		
		//pb.addPBufferListener(sPad);
		//pb.addPBufferListener(dm);
		//pb.addPBufferListener(tem);
		
		//eb.addEBufferListener(pam);
		//eb.addEBufferListener(sPad);
		//eb.addEBufferListener(dm);
		//eb.addPBufferListener(tem);
		
		//pbroads.addPBroadsListener(sPad);
		
		//sPad.addSPadListener(csm);
		//bn.addBehaviorListener(csm);
		
		//csm.addCSMListener(tem);
		//csm.addCSMListener(dm);
		
		//attn.addAttnListener(csm);
		//gwksp.addBroadcastListener(pbroads);
		
		//give the threads time to execute
		int runTime = 600;
		try{ Thread.sleep(runTime);}catch(Exception e){}
		
		//SHUT THREADS DOWN
		System.out.println("Main thread is initiating shutdown...\n");		
		stopThreads();
	}//setup	
	
	public void initSimThread(){
		simulation = new WorldApplication();				
		threadMap.put(new Thread(simulation, "SIMULATION_THREAD"), simulation);	
	}
	public void initSMThread(){
		sm = new SensoryMemory();
		simulation.addSimulationListener(sm);
		
		SMDriver smDriver = new SMDriver(sm);
		threadMap.put(new Thread(smDriver, "SM_THREAD"), smDriver);				
	}
	public void initPAMThread(){
		pam = new PerceptualAssociativeMemory();
		PAMDriver pamDriver = new PAMDriver(pam);
		threadMap.put(new Thread(pamDriver, "PAM_THREAD"), pamDriver);
	}
	
	public void initPBufferThread(){
		pb = new PerceptualBuffer();	
		PBufferDriver pbDriver = new PBufferDriver(pb);
		threadMap.put(new Thread(pbDriver, "PBUFFER"), pbDriver);
	}
	public void initEBufferThread(){
		eb = new EpisodicBuffer();
		EBufferDriver ebDriver = new EBufferDriver(eb);
		threadMap.put(new Thread(ebDriver, "EBUFFER"), ebDriver);
	}
	public void initPBroadsThread(){
		pbroads = new PreviousBroadcasts();
		PBroadsDriver pbDriver = new PBroadsDriver(pbroads);
		threadMap.put(new Thread(pbDriver, "PBROADS"), pbDriver);		
	}
	
	public void initSPadThread(){
		sPad = new ScratchPad();
		ScratchPadDriver spDriver = new ScratchPadDriver(sPad);
		threadMap.put(new Thread(spDriver, "SCRATCHPAD"), spDriver);
	}
	
	public void initCSMThread(){
		csm = new CSM();
		CSMDriver csmDriver = new CSMDriver(csm);
		threadMap.put(new Thread(csmDriver, "CSM"), csmDriver);
	}
	
	public void initAttnThread(){
		//Attention attn = new Attention();
	}
	
	public void initGWThread(){
		gwksp = new GlobalWorkspaceImpl();
		GlobalWkspDriver gwkspDriver = new GlobalWkspDriver(gwksp);
		threadMap.put(new Thread(gwkspDriver, "GWKSP"), gwkspDriver);
	}
	
	public void initProcMemThread(){
		
	}
	
	public void intiActionSelectionThread(){
		
	}
		
	public void startThreads(){ //in normal order
		Set<Thread> threads = threadMap.keySet();
		for(Thread t: threads){
			if(t != null)
				t.start();
			else
				System.out.println("oh crap a null thread object");
		}//for each thread
	}//public void startThreads()
	
	public void stopThreads(){
		ArrayList<Stoppable> drivers = (ArrayList<Stoppable>)threadMap.values();
		int size = drivers.size();
		for(int i = 0; i < size; i++){
			Stoppable s = drivers.get(size - 1 - i);
			if(s != null)
				s.stopRunning();			
		}//for each thread
		
	}	
	
}//Parallel