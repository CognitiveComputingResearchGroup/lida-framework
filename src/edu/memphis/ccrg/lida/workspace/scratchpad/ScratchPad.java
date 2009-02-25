package edu.memphis.ccrg.lida.workspace.scratchpad;


import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.perception.Node;
import edu.memphis.ccrg.lida.perception.Percept;
import edu.memphis.ccrg.lida.workspace.CSM.CSM;
import edu.memphis.ccrg.lida.workspace.CSM.ScratchPadListener;
import edu.memphis.ccrg.lida.workspace.broadcasts.PBroadsListener;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EBufferContent;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EBufferListener;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PBufferContent;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PBufferListener;

public class ScratchPad implements Runnable, ScratchPadInterface, 
									PBufferListener, EBufferListener, PBroadsListener{
	
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

	public void receiveEBufferContent(EBufferContent c) {
		// TODO Auto-generated method stub
		
	}

	public void addSPadListener(ScratchPadListener l) {
		// TODO Auto-generated method stub
		
	}
}//class ScratchPad.java 