/*
 * @(#)AttentionCodelet.java  1.0  Feb, 27, 2009
 *
 * Copyright 2006-2008 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.attention;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public interface AttentionCodelet extends LidaTask{

	public abstract boolean hasSoughtContent(WorkspaceBuffer csm);

	public abstract NodeStructure getSoughtContent(WorkspaceBuffer csm);

}
