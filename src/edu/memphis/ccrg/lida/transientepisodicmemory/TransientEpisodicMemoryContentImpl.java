/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

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
     */
    public NodeStructure getContent() {
        return structure;
    }

}
