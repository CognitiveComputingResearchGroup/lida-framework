/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.example.framework.initialization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.Initializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.proceduralmemory.ArgumentImpl;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemory;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.SchemeImpl;

/**
 * @author Javier Snaider
 * 
 */
public class ProceduralMemoryInitializer implements Initializer {

	private static Logger logger = Logger.getLogger("lida.framework.initialization");

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.initialization.Initializer#initModule
	 * (edu.memphis.ccrg.lida.framework.initialization.Initializable,
	 * edu.memphis.ccrg.lida.framework.Lida, java.util.Map)
	 */
	@Override
	public void initModule(Initializable module, Lida lida, Map<String, ?> params) {

		ProceduralMemory pm = (ProceduralMemory) module;
		PerceptualAssociativeMemory pam = (PerceptualAssociativeMemory) lida.getSubmodule(ModuleName.PerceptualAssociativeMemory);
		Node[] nodes = (Node[]) pam.getNodes().toArray(new Node[0]);
		List<Scheme> schemes = new ArrayList<Scheme>();
		for (int i = 0; i < 20; i++) {
			NodeStructure ctxt = new NodeStructureImpl();
			NodeStructure rstl = new NodeStructureImpl();

			for (int j = 0; j < 2; j++) {
				int idx = (int) (Math.random() * nodes.length);
				Node n = nodes[idx];
				ctxt.addNode(n);
			}
			for (int j = 0; j < 2; j++) {
				int idx = (int) (Math.random() * nodes.length);
				Node n = nodes[idx];
				rstl.addNode(n);
			}
			Scheme scheme = new SchemeImpl("test scheme", i, 1L);
			scheme.addArgument(new ArgumentImpl(1L));
			scheme.setContext(ctxt);
			scheme.setAddingResult(rstl);
			schemes.add(scheme);

			pm.addSchemes(schemes);
			logger.log(Level.INFO, "Random Schemes Loaded");

		}//for
	}//method
}
