/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.transientepisodicmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.transientepisodicmemory.sdm.BasicTranslator;
import edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemory;
import edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemoryImpl;
import edu.memphis.ccrg.lida.transientepisodicmemory.sdm.Translator;
import edu.memphis.ccrg.lida.workspace.main.LocalAssociationListener;

/**
 * This is the cannonical implementation of TEM. It uses a sparse distributed
 * memory to store the information.
 * @author Javier Snaider
 */
public class TemImpl extends LidaModuleImpl implements TransientEpisodicMemory, BroadcastListener, CueListener {

	private static Logger logger = Logger.getLogger(TemImpl.class.getCanonicalName());
	
	public static final int DEF_HARD_LOCATIONS = 10000;
	public static final int DEF_ADDRESS_LENGTH = 1000;
	public static final int DEF_WORD_LENGTH = 1000;
	public static final int DEF_ACTIVATION_RADIOUS = 451;
	
    private SparseDistributedMemory sdm;
 	private Translator translator;
	private List<LocalAssociationListener> localAssocListeners = new ArrayList<LocalAssociationListener>();
	private PerceptualAssociativeMemory pam;
	private int numOfHardLoc = DEF_HARD_LOCATIONS;
	private int addressLength = DEF_ADDRESS_LENGTH;
	private int wordLength = DEF_WORD_LENGTH;
 
    public TemImpl() {
    	super(ModuleName.TransientEpisodicMemory);
	}

	/**
     * Receives the conscious broadcast and store its information in this TEM.
     * @param bc the content of the conscious broadcast
     */
    public void receiveBroadcast(BroadcastContent bc) {
        //logic for episodic learning goes here...
    }
    
    /**
     * Cues this episodic memory.
     * @param cue a set of nodes used to cue this episodic memory
     */
	@Override
    public void cue(MemoryCue cue) {
    	NodeStructure ns =cue.getNodeStructure();
    	receiveCue(ns);
    }
    
	@Override
	public void receiveCue(NodeStructure ns) {		
    	BitVector address = null;
		try {
			address = translator.translate(ns);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    	BitVector out = sdm.retrieveIterating(address);
        
        NodeStructure result = null;
		try {
			result = translator.translate(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
       
        for(LocalAssociationListener l : localAssocListeners){
        	l.receiveLocalAssociation(result);
    		logger.log(Level.FINER,"Local Association sent.",LidaTaskManager.getActualTick());
        }
   return;
	}

	@Override
	public void learn() {
		
	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}
	
	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof LocalAssociationListener){
			localAssocListeners.add((LocalAssociationListener)listener);
		}
	}
	
	@Override
	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof PerceptualAssociativeMemory
					&& module.getModuleName() == ModuleName.PerceptualAssociativeMemory) {
				pam = (PerceptualAssociativeMemory) module;
			}
		}
	}
	
	@Override
	public void init() {
		numOfHardLoc = (Integer)getParam("tem.numOfHardLoc",DEF_HARD_LOCATIONS);
		setAddressLength((Integer)getParam("tem.addressLength",DEF_ADDRESS_LENGTH));
		wordLength = (Integer)getParam("tem.wordLength",DEF_WORD_LENGTH);
		int radious = (Integer)getParam("tem.activationRadious",DEF_ACTIVATION_RADIOUS);
		translator=new BasicTranslator(wordLength,pam);
		sdm=new SparseDistributedMemoryImpl(numOfHardLoc,radious,wordLength);
	}

	public void setAddressLength(int addressLength) {
		this.addressLength = addressLength;
	}

	public int getAddressLength() {
		return addressLength;
	}

        public Object getState() {
            return sdm.getState();
        }
        public boolean setState(Object content) {
            return sdm.setState(content);
        }
}