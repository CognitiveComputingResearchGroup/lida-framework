package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Misc;
import edu.memphis.ccrg.lida.util.Stoppable;

public class PBufferDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private PerceptualBuffer pb;
	private FrameworkTimer timer;
	
	public PBufferDriver(PerceptualBuffer pb, FrameworkTimer timer){
		this.pb = pb;
		this.timer = timer;
	}//

	public void run(){
		int counter = 0;		
		long startTime = System.currentTimeMillis();		
		while(keepRunning){
			try{Thread.sleep(23);}catch(Exception e){}
			timer.checkForClick();
			pb.sendContent();
			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();				
		System.out.println("\nPBF: Ave. cycle time: " + 
							Misc.rnd((finishTime - startTime)/(double)counter));
		System.out.println("PBF: Num. cycles: " + counter);		
	}//public void run()

	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//public void stopRunning()

}
