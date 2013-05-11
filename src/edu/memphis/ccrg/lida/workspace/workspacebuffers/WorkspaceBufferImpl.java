/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.workspacebuffers;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.shared.CognitiveContentStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.ns.Linkable;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.UnmodifiableNodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.ns.WorkspaceNS;

/**
 * The default implementation of {@link WorkspaceBuffer} often a 
 * submodule of the {@link WorkspaceNS}. This implementation contains a single {@link NodeStructure} as content.
 * 
 * @author Ryan J. McCall
 */
public class WorkspaceBufferImpl extends FrameworkModuleImpl implements	WorkspaceBuffer {

	private NodeStructure buffer = new NodeStructureImpl();
	
	/**
	 * Note that this method <i>merges</i> the specified content into the
	 * buffer. Since {@link NodeStructure} copies all added {@link Linkable} objects, the
	 * resultant content inside the buffer consists of different Java objects
	 * than those supplied in the argument. The {@link ExtendedId} of the new
	 * {@link Linkable} are still the same as the originals.
	 */
	@Override
	public void addBufferContent(CognitiveContentStructure content) {
		buffer.mergeWith(content);
	}

	@Override
	public CognitiveContentStructure getBufferContent(Map<String, Object> params) {
		return buffer;
	}

	@Override
	public void decayModule(long t) {
		buffer.decayNodeStructure(t);
	}

	@Override
	public Object getModuleContent(Object... params) {
		return new UnmodifiableNodeStructureImpl(buffer);
	}
}