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
import java.util.Map;
import java.util.Properties;
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
import edu.memphis.ccrg.lida.transientepisodicmemory.sdm.SparseDistributedMemoryImp;
import edu.memphis.ccrg.lida.transientepisodicmemory.sdm.Translator;
import edu.memphis.ccrg.lida.workspace.main.LocalAssociationListener;

/**
 * This is the cannonical implementation of TEM. It uses a sparse distributed
 * memory to store the information.
 * @author Javier Snaider
 */
public class TemImpl extends LidaModuleImpl implements TransientEpisodicMemory, BroadcastListener, CueListener {

	private static Logger logger = Logger.getLogger("lida.transientepisodicmemory.TemImpl");
	
	private final int DEF_HARD_LOCATIONS = 10000;
	private final int DEF_ADDRESS_LENGTH = 1000;
	private final int DEF_WORD_LENGTH = 1000;
	private final int DEF_ACTIVATION_RADIOUS = 451;
	
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
        //TODO: logic for episodic learning goes here...
    }

    /**
     * Cues this episodic memory.
     * @param cue a set of nodes used to cue this episodic memory
     * @return a future task with the local association related to the cue
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getModuleContent() {
		// TODO Auto-generated method stub
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
	public void init(Map<String,?> params) {
		this.lidaProperties=params;
		numOfHardLoc = (Integer)getParam("tem.numOfHardLoc",DEF_HARD_LOCATIONS);
		addressLength = (Integer)getParam("tem.addressLength",DEF_ADDRESS_LENGTH);
		wordLength = (Integer)getParam("tem.wordLength",DEF_WORD_LENGTH);
		int radious = (Integer)getParam("tem.activationRadious",DEF_ACTIVATION_RADIOUS);
		translator=new BasicTranslator(wordLength,pam);
		sdm=new SparseDistributedMemoryImp(numOfHardLoc,radious,wordLength);
	}

}