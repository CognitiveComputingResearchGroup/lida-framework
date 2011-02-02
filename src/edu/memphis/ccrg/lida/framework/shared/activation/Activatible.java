/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared.activation;

import java.io.Serializable;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;

/**
 * An object with activation, has strategies 
 * to both excite and decay this activation
 * 
 * @author Ryan J McCall
 *
 */
public interface Activatible extends Serializable {
	
	/**
	 * Returns excitation.  
	 * @return amount of excitation needed to achieve this Activatible's current activation 
	 */
	public double getExcitation();
	
	/**
	 * Sets excitation. Used for initialization, not during regular execution, use excite instead.
	 * @param excitation amount of excitation needed to achieve this Activatible's current activation
	 */
	public void setExcitation(double excitation);
	
	/**
	 * @return the current activation.
	 */
	public double getActivation();
	
	/**
	 * Set the current activation. Used for initialization, not during regular execution, use excite instead.
	 * @param activation new activation
	 */
    public void setActivation(double activation);
    /**
     * 
     * @return The total activation. It should return the 
     * current activation if no based activation is used.
     */
	public double getTotalActivation();
    
	/**
	 * The current activation of this node is increased 
	 * using the excitation value as a parameter for the ExciteStrategy 
	 * 
	 * @param   amount the value to be used to increase the current activation of
	 *          this node
	 */
    public void excite(double amount); 
    /**
     * 
     * @param strategy the Excite strategy for the current activation.
     */
	public void setExciteStrategy(ExciteStrategy strategy);
	/**
	 * 
	 * @return the excite strategy
	 */
	public ExciteStrategy getExciteStrategy();
	
	/**
	 * decay the current activation using the decay strategy. The decay depends on 
	 * the time since the last decaying. It is indicated by the parameter ticks.
	 * 
	 * @param ticks the number of ticks to decay
	 */
	public void decay(long ticks);	

    /**
     * 
     * @param strategy the decay strategy for the current activation.
     */
	public void setDecayStrategy(DecayStrategy strategy);
	/**
	 * 
	 * @return the decay strategy.
	 */
	public DecayStrategy getDecayStrategy();
	
	/**
	 * Sets activatibleRemovalThreshold
	 * @param threshold threshold for removal of this activatible
	 */
	public void setActivatibleRemovalThreshold(double threshold);
	
	/**
	 * Gets activatibleRemovalThreshold
	 * @return threshold for removal of this activatible
	 */
	public double getActivatibleRemovalThreshold();
	
	/**
	 * Returns if this Activatible is removable
	 * @return true if activation is less than activatibleRemovalThreshold
	 */
	public boolean isRemovable();

}
