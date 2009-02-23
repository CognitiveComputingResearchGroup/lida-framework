package edu.memphis.ccrg.workspace.episodicBuffer;

import java.util.ArrayList;
import java.util.List;

public class EpisodicBuffer implements Runnable, EpisodicBufferInt{
	
    private boolean keepRunning = true;
    private List<EBufferListener> listeners;
	
	public EpisodicBuffer(){
		listeners = new ArrayList<EBufferListener>();
	}
	
	public void run() {
		while(keepRunning){
			
		}
		
	}
	
	public void stopRunning(){
		keepRunning = false;
	}

	public void addEBufferListener(EBufferListener listener) {
		listeners.add(listener);		
	}

}
