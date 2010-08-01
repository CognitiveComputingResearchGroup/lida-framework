/**
 * @(#)AttentionCodelet.java  1.0  Feb, 27, 2009
 *
 * Copyright 2006-2008 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * http://ccrg.cs.memphis.edu/
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.attention;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.Codelet;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * The interface for LIDA's AttentionCodelet.
 * Specific implementations of AttentionCodlet must implement this interface.
 * AttentionCodelet in LIDA communicates with the workspace returning coalitions
 * @author Ryan J McCall
 */
public interface AttentionCodelet extends Codelet{
	
	/**
  	 * Returns true if the current situational model contains the content which this codelet seeks.
  	 * 
     */
	public abstract boolean csmHasDesiredContent();

	/**
	 * 
	 */
	public abstract NodeStructure getCsmContent();
	
	/**
	 * @param gw the GlobalWorkspace to set
	 */
	public void setGlobalWorkspace(GlobalWorkspace gw);
	
	public void setWorkspaceBuffer (WorkspaceBuffer wb);
	/**
	 * @return the WorkspaceBuffer
	 */
	public WorkspaceBuffer getWorkspaceBuffer();

}
