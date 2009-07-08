package edu.memphis.ccrg.lida.example.genericLIDA.main;

import java.util.concurrent.FutureTask;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.transientEpisodicMemory.LocalAssociation;

public interface CueListener2 {
	
	public FutureTask<LocalAssociation> receiveCue(NodeStructure cue);


}

