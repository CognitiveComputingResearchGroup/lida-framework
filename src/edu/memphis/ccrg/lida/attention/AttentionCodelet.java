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
import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;
/**The interface for LIDA's AttentionCodelet.
 * Specific implementations of AttentionCodlet must implement this interface.
 * AttentionCodelet in LIDA communicates with the workspace returning coalitions
 * @author Murali Krishna Ankaraju
 */
public interface AttentionCodelet extends LidaTask{
 /**
  * Provides an accessable content in WorkspaceBuffer
  * @param csm
  * @return
  */
	public abstract boolean hasSoughtContent(WorkspaceBuffer csm);

	/**
	 * 
	 * @param csm
	 * @return
	 */
	public abstract NodeStructure getSoughtContent(WorkspaceBuffer csm);

}
