package edu.memphis.ccrg.lida.transientEpisodicMemory;

import java.util.concurrent.FutureTask;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.transientEpisodicMemory.LocalAssociation;

public interface CueListener {
	
	public FutureTask<LocalAssociation> receiveCue(NodeStructure cue);

}

