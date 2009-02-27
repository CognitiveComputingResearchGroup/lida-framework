package edu.memphis.ccrg.lida.workspace.sbCodelets;

import edu.memphis.ccrg.lida.sensoryMemory.Stoppable;
import edu.memphis.ccrg.lida.util.M;
import edu.memphis.ccrg.lida.workspace.csm.CSM;
import edu.memphis.ccrg.lida.workspace.scratchpad.ScratchPad;

public class SBCodeletsDriver implements Runnable, Stoppable {

	private boolean keepRunning = true;
	private CSM csm = null;
	private ScratchPad spad = null;
	
	public void run(){
		int counter = 0;		
		long startTime = System.currentTimeMillis();		
		while(keepRunning){
			try{Thread.sleep(24);}catch(Exception e){}

			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();				
		System.out.println("\nCODE: Ave. cycle time: " + 
							M.rnd((finishTime - startTime)/(double)counter));
		System.out.println("CODE: Num. cycles: " + counter);		
	}//public void run()

	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//public void stopRunning()

	public void addCSM(CSM csm) {
		this.csm = csm;		
	}

	public void addSPAD(ScratchPad spad) {
		this.spad = spad;
	}

}//public class SBCodeletsDriver 
