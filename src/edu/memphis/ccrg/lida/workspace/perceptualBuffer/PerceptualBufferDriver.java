package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;

public class PerceptualBufferDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private PerceptualBuffer pb;
	private FrameworkTimer timer;
	private long threadID;
	
	public PerceptualBufferDriver(PerceptualBuffer pb, FrameworkTimer timer){
		this.pb = pb;
		this.timer = timer;
	}//

	public void run(){
		//int counter = 0;		
		//long startTime = System.currentTimeMillis();		
		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());
			}catch(Exception e){}
			timer.checkForStartPause();
			pb.sendContentToCodelets();
		//	counter++;			
		}//while keepRunning
		//long finishTime = System.currentTimeMillis();				
		//System.out.println("PBF: Ave. cycle time: " + 
		//					Printer.rnd((finishTime - startTime)/(double)counter));
		//System.out.println("PBF: Num. cycles: " + counter);		
	}//public void run()

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

}
