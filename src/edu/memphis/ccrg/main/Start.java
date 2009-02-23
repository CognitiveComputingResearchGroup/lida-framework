/*
 * Start.java - Initializes, starts, and ends the threads 
 * that run the main LIDA components.  Sets up the shared memory 
 * that the threads use to pass data. 
 */
package edu.memphis.ccrg.main;

import java.lang.Thread;

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

	public static void main(String[] args) {
		new Start().setup();
	}//main
	
	public void setup(){	
		//SIMULATION THREAD
		final int timeSteps = 100;
		WorldApplication simulation = new WorldApplication(timeSteps);
		Thread simulationThread = new Thread(simulation, "SIMULATION_THREAD");	
		
		//SENSORY MEMORY THREAD
		final int senseSize = 5;
		SensoryMemory sm = new SensoryMemory(senseSize);
		SMDriver smDriver = new SMDriver(sm);
		Thread smThread = new Thread(smDriver, "SM_THREAD");
	
		//PAM THREAD
		PerceptualAssociativeMemory pam = new PerceptualAssociativeMemory();
		PAMDriver pamDriver = new PAMDriver(pam);
		Thread pamThread = new Thread(pamDriver, "PAM_THREAD");
		
		//p-WORKSPACE THREAD
		final int pBufferSize = 2;
		PerceptualBuffer pb = new PerceptualBuffer(pBufferSize);
		EpisodicBuffer eb = new EpisodicBuffer();
		PreviousBroadcasts pbroads = new PreviousBroadcasts();
		ScratchPad sPad = new ScratchPad();
		CSM csm = new CSM();
		WorkspaceDriver wkspDriver = new WorkspaceDriver(pb, eb, pbroads, sPad, csm);
		Thread wkspcThread = new Thread(wkspDriver, "WORKSPACE_THREAD");
		
		//TEM/DM THREAD
		
		//ATTENTION THREAD
		//Attention attn = new Attention();
		
		//GWKSP THREAD
		GlobalWorkspaceImpl gwksp = new GlobalWorkspaceImpl();
		
		//PROC MEMORY THREAD
		
		//ACTION SELECTION THREAD
		
		//SET UP LISTENERS
		simulation.addSimulationListener(sm);
		
		sm.addSensoryListener(pam);
		//sm.addSensoryListener(sma);
		
		pam.addPAMListener(pb);
		
		pb.addPBufferListener(sPad);
		//pb.addPBufferListener(dm);
		//pb.addPBufferListener(tem);
		
		eb.addEBufferListener(pam);
		eb.addEBufferListener(sPad);
		//eb.addEBufferListener(dm);
		//eb.addPBufferListener(tem);
		
		pbroads.addPBroadsListener(sPad);
		
		sPad.addSPadListener(csm);
		//bn.addBehaviorListener(csm);
		
		//csm.addCSMListener(tem);
		//csm.addCSMListener(dm);
		
		//attn.addAttnListener(csm);
		gwksp.addBroadcastListener(pbroads);
		
		//START THREADS
		simulationThread.start(); //start receiving thread first
		smThread.start();
		pamThread.start();
		wkspcThread.start();
		
		try{ Thread.sleep(600);} //give the threads time to execute
		catch(Exception e){}
		
		//SHUT THREADS DOWN
		System.out.println("Main thread is initiating shutdown...\n");
		wkspDriver.stopRunning();
		pamDriver.stopRunning(); //tell PAM to stop running	
		smDriver.stopRunning();
		simulation.stopRunning(); //tell simulation to stop running			
	}//setup
	
}//Parallel