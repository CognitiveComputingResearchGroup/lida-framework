/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.episodicmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.episodicmemory.sdm.BasicTranslator;
import edu.memphis.ccrg.lida.episodicmemory.sdm.SparseDistributedMemory;
import edu.memphis.ccrg.lida.episodicmemory.sdm.SparseDistributedMemoryImpl;
import edu.memphis.ccrg.lida.episodicmemory.sdm.Translator;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * This is the canonical implementation of EM. It uses a sparse distributed
 * memory to store the information.
 * 
 * @author Javier Snaider
 */
public class EpisodicMemoryImpl extends LidaModuleImpl implements
		EpisodicMemory, BroadcastListener, CueListener {

	private static final Logger logger = Logger
			.getLogger(EpisodicMemoryImpl.class.getCanonicalName());

	public static final int DEF_HARD_LOCATIONS = 10000;
	public static final int DEF_ADDRESS_LENGTH = 1000;
	public static final int DEF_WORD_LENGTH = 1000;
	public static final int DEF_ACTIVATION_RADIUS = 451;

	private SparseDistributedMemory sdm;
	private Translator translator;
	private List<LocalAssociationListener> localAssocListeners = new ArrayList<LocalAssociationListener>();
	private PerceptualAssociativeMemory pam;
	private int numOfHardLoc = DEF_HARD_LOCATIONS;
	private int addressLength = DEF_ADDRESS_LENGTH;
	private int wordLength = DEF_WORD_LENGTH;

	public EpisodicMemoryImpl() {
		super(ModuleName.TransientEpisodicMemory);
	}

	/**
	 * Receives the conscious broadcast and store its information in this EM.
	 * 
	 * @param bc
	 *            the content of the conscious broadcast
	 */
	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		// TODO logic for episodic learning goes here...

	}

	@Override
	/**
	 * Receive a cue as a {@link NodeStructure}
	 * In this implementation, first the cue is translated to a BitVector. 
	 * TODO parallelize using task.  
	 * @see {@link Translator}
	 * @param cue NodeStructure to recall
	 */
	public void receiveCue(NodeStructure ns) {
		BitVector address = null;
		try {
			address = translator.translate(ns);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//TODO make sure this method is thread-safe
		BitVector out = sdm.retrieveIterating(address);

		NodeStructure result = null;
		try {
			result = translator.translate(out);
		} catch (Exception e) {
			e.printStackTrace();
		}

		sendLocalAssociation(result);
		return;
	}

	/**
	 * Sends local association to all listeners
	 * @param localAssociation NodeStructure to send
	 */
	private void sendLocalAssociation(NodeStructure localAssociation) {
		for (LocalAssociationListener l : localAssocListeners) {
			l.receiveLocalAssociation(localAssociation);
			logger.log(Level.FINER, "Local Association sent.", LidaTaskManager
					.getCurrentTick());
		}
	}

	@Override
	public void learn(BroadcastContent content) {

	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof LocalAssociationListener) {
			localAssocListeners.add((LocalAssociationListener) listener);
		}
	}

	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
		if (module != null) {
			if (module instanceof PerceptualAssociativeMemory
					&& module.getModuleName() == ModuleName.PerceptualAssociativeMemory) {
				pam = (PerceptualAssociativeMemory) module;
			}
		}
	}

	@Override
	public void init() {
		numOfHardLoc = (Integer) getParam("tem.numOfHardLoc",
				DEF_HARD_LOCATIONS);
		setAddressLength((Integer) getParam("tem.addressLength",
				DEF_ADDRESS_LENGTH));
		wordLength = (Integer) getParam("tem.wordLength", DEF_WORD_LENGTH);
		int radious = (Integer) getParam("tem.activationRadious",
				DEF_ACTIVATION_RADIUS);
		translator = new BasicTranslator(wordLength, pam);
		sdm = new SparseDistributedMemoryImpl(numOfHardLoc, radious, wordLength);
	}

	/**
	 * Sets the addressLength of the SDM
	 * @param addressLength the length (in number of bits) of SDM's the address space.
	 */
	public void setAddressLength(int addressLength) {
		this.addressLength = addressLength;
	}

	/**
	 * @return the length (in number of bits) of SDM's the address space.
	 */
	public int getAddressLength() {
		return addressLength;
	}

	@Override
	public Object getState() {
		return sdm.getState();
	}

	@Override
	public boolean setState(Object content) {
		return sdm.setState(content);
	}
}