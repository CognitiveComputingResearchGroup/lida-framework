/*
 * Start.java - Initializes, starts, and ends the threads 
 * that run the main LIDA components.  Sets up the shared memory 
 * that the threads use to pass data. 
 */
package edu.memphis.ccrg.main;

import java.lang.Thread;

import edu.memphis.ccrg.perception.PerceptualAssociativeMemory;
import edu.memphis.ccrg.sensoryMemory.SensoryMemory;
import edu.memphis.ccrg.workspace.CSM.CSM;
import edu.memphis.ccrg.workspace.broadcasts.PreviousBroadcasts;
import edu.memphis.ccrg.workspace.episodicBuffer.EpisodicBuffer;
import edu.memphis.ccrg.workspace.perceptualBuffer.PerceptualBuffer;
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
		simulation.addBroadcastListner(sm);
		SMDriver smDriver = new SMDriver(sm);
		Thread smThread = new Thread(smDriver, "SM_THREAD");
	
		//PAM THREAD
		PerceptualAssociativeMemory pam = new PerceptualAssociativeMemory();
		sm.addBroadcastListener(pam);
		PAMDriver pamDriver = new PAMDriver(pam);
		Thread pamThread = new Thread(pamDriver, "PAM_THREAD");
		
		//WORKSPACE THREAD
		final int pBufferSize = 2;
		PerceptualBuffer pb = new PerceptualBuffer(pBufferSize);
		EpisodicBuffer eb = new EpisodicBuffer();
		PreviousBroadcasts pbroads = new PreviousBroadcasts();
		CSM model = new CSM();
		
		pam.addBroadcastListener(pb);
		WorkspaceDriver wkspDriver = new WorkspaceDriver(pb, eb, pbroads, model);
		Thread wkspcThread = new Thread(wkspDriver, "WORKSPACE_THREAD");
		
		//Start threads
		simulationThread.start(); //start receiving thread first
		smThread.start();
		pamThread.start();
		wkspcThread.start();
		
		try{ Thread.sleep(600);} //give the threads time to execute
		catch(Exception e){}
		
		System.out.println("Main thread is initiating shutdown...\n");
		wkspDriver.stopRunning();
		pamDriver.stopRunning(); //tell PAM to stop running	
		smDriver.stopRunning();
		simulation.stopRunning(); //tell simulation to stop running			
	}//setup
	
}//Parallel