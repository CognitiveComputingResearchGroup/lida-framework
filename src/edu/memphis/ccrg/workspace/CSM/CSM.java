package edu.memphis.ccrg.workspace.CSM;

public class CSM implements Runnable, CSMInterface, ScratchPadListener{
	
	private boolean keepRunning = true;
	
	public CSM(){
		
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void stopRunning(){
		keepRunning = false;
	}

	public void receiveSPadContent(SPadContent c) {
		// TODO Auto-generated method stub
		
	}

}
