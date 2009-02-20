package edu.memphis.ccrg.main;

import edu.memphis.ccrg.workspace.perceptualBuffer.PerceptualBuffer;

public class WorkspaceDriver implements Runnable{
	
	private boolean keepRunning;
	private PerceptualBuffer pb;
	
	public WorkspaceDriver(PerceptualBuffer pb){
		keepRunning = true;
		this.pb = pb;	
	}//public WorkspaceDriver

	public void run(){		
		while(keepRunning){			
			try{Thread.sleep(25);}catch(Exception e){}
						
			pb.sendContent();
			
		}//while		
	}//public void run()
	
	public void stopRunning(){
		keepRunning = false;
	}
	
}//public class WorkspaceDriver
