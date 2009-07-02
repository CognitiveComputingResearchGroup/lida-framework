package edu.memphis.ccrg.lida.transientEpisodicMemory;

import edu.memphis.ccrg.lida.shared.NodeStructure;

/**
 * 
 * @author rsilva
 */
public interface CueListener {
	
    /**
     *
     * @param cue
     */
    void receiveCue(NodeStructure cue);

}
