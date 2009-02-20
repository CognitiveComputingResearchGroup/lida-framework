package edu.memphis.ccrg.workspace.scratchpad;


import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.perception.Node;
import edu.memphis.ccrg.perception.Percept;
import edu.memphis.ccrg.workspace.perceptualBuffer.PBufferContent;
import edu.memphis.ccrg.workspace.perceptualBuffer.PBufferListener;


public class ScratchPad implements Runnable, ScratchPadInterface, PBufferListener{
	
	private PBufferContent pbContent;
	private Map<String, SBCodelet> codeletMap; 
	private boolean keepRunning = true;
	
	public ScratchPad(){
		pbContent = new PBufferContent();
		codeletMap = new HashMap<String, SBCodelet>();
	}
	
	public void run(){
		//TODO:
	}
	
	public void stopRunning(){
		keepRunning = false;
	}
	
	public void addSBCodelet(String codeletName, SBCodelet sbc){
		codeletMap.put(codeletName, sbc);		
	}

	public synchronized void receivePBufferContent(PBufferContent pbc) {
		pbContent = pbc;		
	}
	
	public void activateSBCodelets(){
		Percept p = new Percept();
		synchronized(this){
			p = (Percept)pbContent.getContent();
		}
		for(int i = 0; i < p.size(); i++){
			Node temp = p.get(i);
			temp.getLabel();//TODO: implement
		}//for	
	}//activateSBCodelets
}//class ScratchPad.java 