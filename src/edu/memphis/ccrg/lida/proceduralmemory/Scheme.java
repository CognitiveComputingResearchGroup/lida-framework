/*
 * @(#)Scheme.java  1.0  February 14, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */
package edu.memphis.ccrg.lida.proceduralmemory;

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
	
	public NodeStructure getContext();
	public void setContext(NodeStructure ns);
	
	public NodeStructure getResult();
	public void setResult(NodeStructure ns);
	
	public LidaAction getSchemeAction();
	public void setSchemeAction(LidaAction a);
	
}//interface
