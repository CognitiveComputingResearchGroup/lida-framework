/*
 * Start.java - Initializes, starts, and ends the threads 
 * that run the main LIDA components.  Sets up the shared memory 
 * that the threads use to pass data. 
 */
package edu.memphis.ccrg.main;

import java.lang.Thread;
import java.util.ArrayList;
import java.util.List;
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
	
	private List<Thread> threads = new ArrayList<Thread>();
	private List<Stoppable> drivers = new ArrayList<Stoppable>();
	
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
		
		startThreads();
		
		//give the threads time to execute
		int runTime = 600;
		try{ Thread.sleep(runTime);}catch(Exception e){}
		
		//SHUT THREADS DOWN
		System.out.println("\nMain thread is initiating shutdown...\n");		
		stopThreads();
	}//setup	
	
	public void initSimThread(){
		simulation = new WorldApplication();				
		threads.add(new Thread(simulation, "SIMULATION_THREAD"));   
		drivers.add(simulation);	
	}
	public void initSMThread(){
		sm = new SensoryMemory();
		simulation.addSimulationListener(sm);
		
		SMDriver smDriver = new SMDriver(sm);
		threads.add(new Thread(smDriver, "SM_THREAD"));   
		drivers.add(smDriver);				
	}
	public void initPAMThread(){
		pam = new PerceptualAssociativeMemory();
		PAMDriver pamDriver = new PAMDriver(pam);
		threads.add(new Thread(pamDriver, "PAM_THREAD"));   
		drivers.add(pamDriver);
	}
	
	public void initPBufferThread(){
		pb = new PerceptualBuffer();	
		PBufferDriver pbDriver = new PBufferDriver(pb);
		threads.add(new Thread(pbDriver, "PBUFFER"));   
		drivers.add(pbDriver);
	}
	public void initEBufferThread(){
		eb = new EpisodicBuffer();
		EBufferDriver ebDriver = new EBufferDriver(eb);
		threads.add(new Thread(ebDriver, "EBUFFER"));   
		drivers.add(ebDriver);
	}
	public void initPBroadsThread(){
		pbroads = new PreviousBroadcasts();
		PBroadsDriver pbDriver = new PBroadsDriver(pbroads);
		threads.add(new Thread(pbDriver, "PBROADS"));   
		drivers.add(pbDriver);		
	}
	
	public void initSPadThread(){
		sPad = new ScratchPad();
		ScratchPadDriver spDriver = new ScratchPadDriver(sPad);
		threads.add(new Thread(spDriver, "SCRATCHPAD"));   
		drivers.add(spDriver);
	}
	
	public void initCSMThread(){
		csm = new CSM();
		CSMDriver csmDriver = new CSMDriver(csm);
		threads.add(new Thread(csmDriver, "CSM"));   
		drivers.add(csmDriver);
	}
	
	public void initAttnThread(){
		//Attention attn = new Attention();
	}
	
	public void initGWThread(){
		gwksp = new GlobalWorkspaceImpl();
		GlobalWkspDriver gwkspDriver = new GlobalWkspDriver(gwksp);
		threads.add(new Thread(gwkspDriver, "GWKSP"));   
		drivers.add(gwkspDriver);
	}
	
	public void initProcMemThread(){
		
	}
	
	public void intiActionSelectionThread(){
		
	}
		
	public void startThreads(){ //in normal order
		int size = threads.size();
		for(int i = 0; i < size; i++){
			Thread t = threads.get(i);
			if(t != null)
				t.start();
			else
				System.out.println("oh crap a null thread object");
		}//for each thread
	}//public void startThreads()
	
	public void stopThreads(){
		int size = drivers.size();
		for(int i = 0; i < size; i++){			
			Stoppable s = drivers.get(size - 1 - i);
			if(s != null)
				s.stopRunning();		
			//try {Thread.sleep(10);} catch(InterruptedException e){}
		}//for each thread	
	}//public void stopThreads()	
	
}//Parallel