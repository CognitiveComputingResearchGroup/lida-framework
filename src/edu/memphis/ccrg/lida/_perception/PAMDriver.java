package edu.memphis.ccrg.lida._perception;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.gui.FrameworkGui;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Misc;
import edu.memphis.ccrg.lida.util.Stoppable;

public class PAMDriver implements Runnable, Stoppable{

	private PerceptualAssociativeMemoryImpl pam;
	private FrameworkTimer timer;
	private boolean keepRunning = true;		
	private long threadID;
	private FrameworkGui testGui;
	
	public PAMDriver(PerceptualAssociativeMemoryImpl pam, FrameworkTimer timer){
		this.pam = pam;
		this.timer = timer;
	}//PAMDrive constructor
		
	public void run(){
		int counter = 0;		
		//boolean runOneStep = false;
		
		long startTime = System.currentTimeMillis();	
		while(keepRunning){
			try{Thread.sleep(22  + timer.getSleepTime());}catch(Exception e){}						
			timer.checkForStartPause();//won't return if paused until started again			
			//runOneStep = timer.checkForNextStep(runOneStep, threadID);		
					
			pam.sense();	//Sense sensory memory data				
			pam.passActivation();//Pass activation	
			pam.sendPercept(); //Send the percept to p-Workspace
			pam.decay();  //Decay the activations	
	        sendContentToGui();

			counter++;
		}//while keepRunning
		long finishTime = System.currentTimeMillis();			
		System.out.println("\nPAM: Ave. cycle time: " + 
							Misc.rnd((finishTime - startTime)/(double)counter));
		System.out.println("PAM: Num. cycles: " + counter + "\n");	
	}//method run
	
	private void sendContentToGui(){
		if(testGui != null){
	        List<Object> content = new ArrayList<Object>();
	        content.add(pam.getNodeCount());
	        content.add(pam.getLinkCount());
	        testGui.receiveGuiContent(FrameworkGui.FROM_PAM, content);
		}
	}//method

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
	
}//class PAMDriver
