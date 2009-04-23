package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Printer;
import edu.memphis.ccrg.lida.util.Stoppable;

public class PAMDriver implements Runnable, Stoppable{

	private PerceptualAssociativeMemory pam;
	private FrameworkTimer timer;
	private boolean keepRunning = true;		
	private long threadID;
	private FrameworkGui testGui;
	
	public PAMDriver(PerceptualAssociativeMemory pam, FrameworkTimer timer){
		this.pam = pam;
		this.timer = timer;
	}//PAMDrive constructor
		
	public void run(){
		//int counter = 0;		
		//boolean runOneStep = false;
		
		//long startTime = System.currentTimeMillis();	
		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());}catch(Exception e){}						
			timer.checkForStartPause();//won't return if paused until started again			
			//runOneStep = timer.checkForNextStep(runOneStep, threadID);		
					
			pam.sense();	//Sense sensory memory data				
			pam.passActivation();//Pass activation	
			pam.sendPercept(); //Send the percept to p-Workspace
			pam.decay();  //Decay the activations	
			if(testGui != null)
		        testGui.receiveGuiContent(FrameworkGui.FROM_PAM, pam.getGuiContent());

			//counter++;
		}//while keepRunning
		long finishTime = System.currentTimeMillis();			
		//System.out.println("PAM: Ave. cycle time: " + 
		//					Printer.rnd((finishTime - startTime)/(double)counter));
		//System.out.println("PAM: Num. cycles: " + counter + "\n");	
	}//method run
	
	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//method stopRunning

	public void setThreadID(long id){
		threadID = id;
	}
	
	public long getThreadID() {
		return threadID;
	}

	public void addTestGui(FrameworkGui testGui) {
		this.testGui = testGui;		
	}

	public void waitForFinish() {
		while(keepRunning)
			try{Thread.sleep(100);}catch(Exception e){}		
	}
	
}//class PAMDriver
