/*
 * @(#)TEMImpl.java  1.0  May 1, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory;

import java.util.ArrayList;
import java.util.List;

import cern.colt.bitvector.BitVector;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryListener;
import edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory;
import edu.memphis.ccrg.lida.transientepisodicmemory.sdm.Translator;
import edu.memphis.ccrg.lida.transientepisodicmemory.sdm.TranslatorImpl;
import edu.memphis.ccrg.lida.workspace.main.LocalAssociationListener;

/**
 * This is the cannonical implementation of TEM. It uses a sparse distributed
 * memory to store the information.
 * @author Rodrigo Silva L.
 */
public class TemImpl extends LidaModuleImpl implements TransientEpisodicMemory, BroadcastListener, CueListener {

    private SparseDistributedMemory sdm;
 	private Translator translator;
	private List<LocalAssociationListener> localAssocListeners = new ArrayList<LocalAssociationListener>();
    
    /**
     * The constructor of the class.
     * @param structure the structure with the nodes used for this TEM
     */
    public TemImpl(NodeStructure structure) {
    	super(ModuleName.TransientEpisodicMemory);
        translator = new TranslatorImpl(structure);
     }

    public TemImpl() {
    	super(ModuleName.TransientEpisodicMemory);
	}

	/**
     * Receives the conscious broadcast and store its information in this TEM.
     * @param bc the content of the conscious broadcast
     */
    public void receiveBroadcast(BroadcastContent bc) {
        //TODO: logic for episodic learning goes here...
    }

    /**
     * Cues this episodic memory.
     * @param cue a set of nodes used to cue this episodic memory
     * @return a future task with the local association related to the cue
     */
    public void cue(MemoryCue cue) {
    	NodeStructure ns =cue.getNodeStructure();
//        Collection<Node> nodes = cue.getNodeStructure().getNodes();
//        LocalAssociationImpl association = new LocalAssociationImpl();
//        FutureTask<LocalAssociation> future = null;
    
        	byte[] address = null;
			try {
				address = translator.translate(ns);
			} catch (Exception e) {
				e.printStackTrace();
			}
            
//        	BitVector out = sdm.retrieve(address);
            
            NodeStructure result = null;
			try {
//				result = translator.translate(out);
			} catch (Exception e) {
				e.printStackTrace();
			}
           
            for(LocalAssociationListener l : localAssocListeners){
            	l.receiveLocalAssociation(result);
            }
        return;
    }
    
    // Rodrigo, this method is called continually.  The rate at which is called 
	// can be modified by changing the 'ticksPerCycle' parameter of PerceptualBufferDriver.
	// This is set in the Lida Class.  The higher the value for 'tickPerCycle' the slower
	// the rate of cueing will be.
	public synchronized void receiveCue(NodeStructure cue) {		
		//cue(cue);
	}

	public void learn() {
		// TODO Auto-generated method stub
		
	}

	public Object getModuleContent() {
		// TODO Auto-generated method stub
		return null;
	}
	public void addListener(ModuleListener listener) {
	}
}