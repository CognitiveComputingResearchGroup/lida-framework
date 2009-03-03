package edu.memphis.ccrg.lida.workspace.sbCodelets;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.util.M;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.csm.CSM;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PBufferContent;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PBufferListener;

public class SBCodeletsDriver implements Runnable, Stoppable, PBufferListener {

	private boolean keepRunning = true;
	private CSM csm = null;
	private PBufferContent pBufferContent;
	
	private Map<Context, SBCodelet> codeletMap = new HashMap<Context, SBCodelet>();//TODO: equals, hashCode
	
	public void run(){
		SBCodelet testCodelet = new SBCodelet();
		
		
		int counter = 0;		
		long startTime = System.currentTimeMillis();		
		while(keepRunning){
			try{Thread.sleep(24);}catch(Exception e){}//TODO: if PBUFFER Content is changed wake up
			
			//if BufferContent activates a sbCodelet's context start a new codelet
			
			

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

	public synchronized void receivePBufferContent(PBufferContent pbc) {
		pBufferContent = pbc;		
	}

}//public class SBCodeletsDriver 
