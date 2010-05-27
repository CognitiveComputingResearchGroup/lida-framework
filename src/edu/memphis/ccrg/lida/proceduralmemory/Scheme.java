/*
 * @(#)Scheme.java  1.0  February 14, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */
package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.framework.shared.Activatible;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 *
 * @author Ryan J. McCall
 */
public interface Scheme extends Activatible{
	
//  TODO: To consider for next version: Curiosity, Reliability
	
	public void setId(long id);
	public long getId();
	
	public double getCuriosity();
	public void setCuriosity(double curiosity);

	public double getReliability();
	public boolean isReliable();
	
	/**
	 * Standard getter for intrinsicBehavior.
	 * @return the boolean value of intrinsicBehavior
	 */
	public boolean isIntrinsicBehavior();
	/**
	 * Standard setter for intrinsicBehavior.
	 * @param intrinsicBehavior the boolean value of intrinsicBehavior
	 */
	public void setIntrinsicBehavior(boolean intrinsicBehavior);
	
	public int getNumberOfExecutions();
	/**
	 * Standard setter for numberOfExecutions.
	 * @param numberOfExecutions the int value of numberOfExecutions
	 */
	public void setNumberOfExecutions(int numberOfExecutions);
	/**
	 * Increment numberOfExecutions.
	 */
	public void incrementNumberOfExecutions();

	/**
	 * Decay curiosity.
	 */
	public void decayCuriosity();

	
	public List<NodeStructure> getContextConditions();
	public List<NodeStructure>  getContextConditions(long argumentId);
	
	public void addContextCondition(long argumentId,NodeStructure ns);
	
	public List<NodeStructure> getResultConditions();
	public List<NodeStructure> getResultConditions(long argumentId);
	public void addResultConditions(long argumentId,NodeStructure ns);
	
	public void addArgument (Argument a);
	public Argument getArgument (long argumentId);
	public Collection<Argument> getArguments ();
	
	public long getSchemeActionId();
	public void setSchemeActionId(long actionId);
	
}//interface
