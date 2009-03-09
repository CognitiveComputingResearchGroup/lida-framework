package edu.memphis.ccrg.lida.workspace.scratchpad;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.perception.Percept;
import edu.memphis.ccrg.lida.workspace.broadcasts.PBroadsListener;
import edu.memphis.ccrg.lida.workspace.broadcasts.PrevBroadcastContent;
import edu.memphis.ccrg.lida.workspace.csm.SPadContent;
import edu.memphis.ccrg.lida.workspace.csm.ScratchPadListener;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EBufferContent;
import edu.memphis.ccrg.lida.workspace.episodicBuffer.EBufferListener;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PBufferContent;
import edu.memphis.ccrg.lida.workspace.perceptualBuffer.PBufferListener;

public class ScratchPad implements ScratchPadInterface, PBufferListener, 
								   EBufferListener, PBroadsListener{
	
	private List<ScratchPadListener> listeners = new ArrayList<ScratchPadListener>();
	private PBufferContent pbContent = new PBufferContent();
	private Map<String, SBCodelet> codeletMap = new HashMap<String, SBCodelet>();
	private SPadContent content; 
	
	public ScratchPad(){
	}
	
	public void addSBCodelet(String codeletName, SBCodelet sbc){
		codeletMap.put(codeletName, sbc);		
	}

	public synchronized void receivePBufferContent(PBufferContent pbc) {
		pbContent = pbc;		
	}
	
	public synchronized void receiveEBufferContent(EBufferContent c) {
		// TODO Auto-generated method stub		
	}
	
	public synchronized void receivePrevBroadcastContent(PrevBroadcastContent c) {
		// TODO Auto-generated method stub
		
	}
	
	public void storePBufferContent(){
		Percept p = new Percept();
		synchronized(this){
			p = (Percept)pbContent.getContent();
		}
		for(int i = 0; i < p.size(); i++){
			//Node temp = p.get(i);
			//M.p(temp.getLabel());//TODO: implement
		}//for	
				
	}//storePBuffer
	
	public void storeEBufferContent(){
		
	}
	
	public void storePBroadContent(){
		
	}
	
	public void activateSBCodelets(){
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).receiveSPadContent(content);
	}

	public void addSPadListener(ScratchPadListener l) {
		listeners.add(l);		
	}
	
}//class ScratchPad.java 