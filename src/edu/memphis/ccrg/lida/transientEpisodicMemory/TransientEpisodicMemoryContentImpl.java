/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.transientEpisodicMemory;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

/**
 *
 * @author Rodrigo Silva L. <rsilval@acm.org>
 */
public class TransientEpisodicMemoryContentImpl implements TransientEpisodicMemoryContent {

    private NodeStructure structure;
    
    /**
     * 
     */
    public TransientEpisodicMemoryContentImpl() {
        structure = new NodeStructureImpl();
    }
    
    /**
     * 
     * @return
     */
    public NodeStructure getContent() {
        return structure;
    }

}
