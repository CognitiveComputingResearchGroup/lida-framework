/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import java.util.logging.Level;
import java.util.logging.Logger;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.LearnableActivatible;
import edu.memphis.ccrg.lida.framework.shared.activation.LearnableActivatibleImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalValueStrategy;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * Default implementation of {@link PamNode}. A {@link LearnableActivatible} {@link Node}. 
 * Has a private {@link LearnableActivatibleImpl} to help 
 * implement all {@link LearnableActivatible} methods.
 * @author Ryan J. McCall
 * @author Rodrigo Silva-Lugo
 */
public class PamNodeImpl extends NodeImpl implements PamNode {

    private static final Logger logger = Logger.getLogger(PamNodeImpl.class.getCanonicalName());
    /*
     * Private Learnable object used for all learnable methods
     */
    private LearnableActivatibleImpl learnableImpl;
    private BitVector sdmId;
    @SuppressWarnings("unused")
	private static final int DEFAULT_BITVECTOR_LENGTH = 1000;

    /**
     * Default constructor
     */
    public PamNodeImpl() {
        super();
        groundingPamNode = this;
        learnableImpl = new LearnableActivatibleImpl();
    }

    /**
     * Copy constructor
     * @deprecated Use {@link ElementFactory#getNode(Node, String)} or similar method in {@link ElementFactory} instead.
     * @param pn source {@link PamNodeImpl}
     */
    @Deprecated
    public PamNodeImpl(PamNodeImpl pn) {
        super(pn);
        groundingPamNode = this;
        learnableImpl = new LearnableActivatibleImpl(pn.learnableImpl);
    }

    /** 
     * Must call the {@link #init()} of the internal {@link LearnableActivatible}.
     * @see LearnableActivatibleImpl#init()
     * @see ElementFactory#getNode(String, String, String, String, double, double)
     */
    @Override
    public void init() {
        learnableImpl.init(getParameters());
    }

    @Override
    public void updateNodeValues(Node n) {
        if (n instanceof PamNodeImpl) {
            PamNodeImpl pn = (PamNodeImpl) n;
            learnableImpl.setBaseLevelActivation(pn.getBaseLevelActivation());   
        } else {
            logger.log(Level.FINEST, "Cannot set PamNodeImpl-specific values. Required: {1} \n Received: {2}",
                    new Object[]{TaskManager.getCurrentTick(), PamNodeImpl.class.getCanonicalName(), n});
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof PamNodeImpl) {
            return getId() == ((PamNodeImpl) o).getId();
        }
        return false;
    }

    //LEARNABLE METHODS
    @Override
    public double getActivation() {
        return learnableImpl.getActivation();
    }

    @Override
    public void setActivation(double a) {
        learnableImpl.setActivation(a);
    }

    @Override
    public double getTotalActivation() {
        return learnableImpl.getTotalActivation();
    }

    @Override
    public void excite(double amount) {
        learnableImpl.excite(amount);
    }

    @Override
    public void setExciteStrategy(ExciteStrategy strategy) {
        learnableImpl.setExciteStrategy(strategy);
    }

    @Override
    public ExciteStrategy getExciteStrategy() {
        return learnableImpl.getExciteStrategy();
    }

    @Override
    public void decay(long ticks) {
        learnableImpl.decay(ticks);
    }

    @Override
    public void setDecayStrategy(DecayStrategy strategy) {
        learnableImpl.setDecayStrategy(strategy);
    }

    @Override
    public DecayStrategy getDecayStrategy() {
        return learnableImpl.getDecayStrategy();
    }

    @Override
    public void setActivatibleRemovalThreshold(double threshold) {
        learnableImpl.setActivatibleRemovalThreshold(threshold);
    }

    @Override
    public double getActivatibleRemovalThreshold() {
        return learnableImpl.getActivatibleRemovalThreshold();
    }

    @Override
    public boolean isRemovable() {
        return learnableImpl.isRemovable();
    }

    @Override
    public double getBaseLevelActivation() {
        return learnableImpl.getBaseLevelActivation();
    }

    @Override
    public void setBaseLevelActivation(double amount) {
        learnableImpl.setBaseLevelActivation(amount);
    }

    @Override
    public void reinforceBaseLevelActivation(double amount) {
        learnableImpl.reinforceBaseLevelActivation(amount);
    }

    @Override
    public void setBaseLevelExciteStrategy(ExciteStrategy strategy) {
        learnableImpl.setBaseLevelExciteStrategy(strategy);
    }

    @Override
    public ExciteStrategy getBaseLevelExciteStrategy() {
        return learnableImpl.getBaseLevelExciteStrategy();
    }

    @Override
    public void decayBaseLevelActivation(long ticks) {
        learnableImpl.decayBaseLevelActivation(ticks);
    }

    @Override
    public void setBaseLevelDecayStrategy(DecayStrategy strategy) {
        learnableImpl.setBaseLevelDecayStrategy(strategy);
    }

    @Override
    public DecayStrategy getBaseLevelDecayStrategy() {
        return learnableImpl.getBaseLevelDecayStrategy();
    }

    @Override
    public void setBaseLevelRemovalThreshold(double threshold) {
        learnableImpl.setBaseLevelRemovalThreshold(threshold);
    }

    @Override
    public double getLearnableRemovalThreshold() {
        return learnableImpl.getLearnableRemovalThreshold();
    }

    @Override
    public TotalValueStrategy getTotalValueStrategy() {
        return learnableImpl.getTotalValueStrategy();
    }

    @Override
    public void setTotalValueStrategy(TotalValueStrategy strategy) {
        learnableImpl.setTotalValueStrategy(strategy);
    }

    /**
     * Sets this node ID used for translation to SDM. This ID
     * is a bit vector that will be used to create the representation of a node
     * structure containing this node.
     */
    // FIXME: change method
    @Override
    public void setSdmId(BitVector vector) {
        //if (!hasSdmId()) {

            // Determine the size of the bit vector (from config file?).
            //int length = (Integer) getParam("tem.wordLength", DEFAULT_BITVECTOR_LENGTH);

            // Initialize the bit vector with random bits.
            //sdmId = new BitVector(length);
            //sdmId = BitVectorUtils.getRandomVector(length);
        //}
        sdmId = vector;
    }

    /**
     * Get the ID used for representation of this node in SDM.
     * @return a unique random bit vector
     */
    @Override
    public BitVector getSdmId() {
        return sdmId;
    }
}