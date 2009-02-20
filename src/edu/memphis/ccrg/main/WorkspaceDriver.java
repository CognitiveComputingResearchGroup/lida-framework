package edu.memphis.ccrg.main;

import edu.memphis.ccrg.workspace.CSM.CSM;
import edu.memphis.ccrg.workspace.broadcasts.PreviousBroadcasts;
import edu.memphis.ccrg.workspace.episodicBuffer.EpisodicBuffer;
import edu.memphis.ccrg.workspace.perceptualBuffer.PerceptualBuffer;
import edu.memphis.ccrg.workspace.scratchpad.ScratchPad;

public class WorkspaceDriver implements Runnable{
	
	private boolean keepRunning;
	private PerceptualBuffer pb;
	private EpisodicBuffer eb;
	private PreviousBroadcasts pbroads;
	private CSM csm;
	private ScratchPad pad;
	
	public WorkspaceDriver(PerceptualBuffer pb, EpisodicBuffer eb, 
						   PreviousBroadcasts broads, CSM csm){
		keepRunning = true;
		this.pb = pb;	
		this.eb = eb;
		pbroads = broads;
		this.csm = csm;
		pad = new ScratchPad();
	}//public WorkspaceDriver

	public void run(){		
		Thread pBufferThread = new Thread(pb, "PBUFFER"),
			   eBufferThread = new Thread(eb, "EBUFFER"),
			   csmThread = new Thread(csm, "CSM"), 
			   pbroadsThread = new Thread(pbroads, "PBROADS"),
			   padThread = new Thread(pad, "SCRATCHPAD");
		
		pBufferThread.start();
		eBufferThread.start();		
		pbroadsThread.start();
		padThread.start();
		csmThread.start();
		
		while(keepRunning){			
			try{Thread.sleep(25);}catch(Exception e){}
						
			pb.sendContent();
			
		}//while	
		
		pb.stopRunning();
		eb.stopRunning();
		pbroads.stopRunning();
		csm.stopRunning();
		pad.stopRunning();	
	}//public void run()
	
	public void stopRunning(){
		keepRunning = false;
	}
	
}//public class WorkspaceDriver
