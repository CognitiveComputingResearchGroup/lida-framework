package edu.memphis.ccrg.lida.workspace.episodicBuffer;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.workspace.sbCodelets.CodeletAccessible;

public class EpisodicBufferImpl implements EpisodicBuffer, CodeletAccessible{

    private List<EBufferListener> listeners;
	
	public EpisodicBufferImpl(){
		listeners = new ArrayList<EBufferListener>();
	}

	public void addEBufferListener(EBufferListener listener) {
		listeners.add(listener);		
	}

}
