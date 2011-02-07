/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.example.framework.initialization;


import java.util.Map;

import edu.memphis.ccrg.lida.example.genericlida.featuredetectors.BasicDetector;
import edu.memphis.ccrg.lida.example.genericlida.featuredetectors.BottomRightDetector;
import edu.memphis.ccrg.lida.example.genericlida.featuredetectors.TopLeftDetector;
import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.Initializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.shared.LinkCategoryNode;
import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.PropagationBehavior;
import edu.memphis.ccrg.lida.pam.UpscalePropagationBehavior;
import edu.memphis.ccrg.lida.pam.tasks.FeatureDetector;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

public class PamInitializer implements Initializer {

	public PamInitializer() {
	}

	@Override
	public void initModule(Initializable module, Lida lida, Map<String, ?> params) {		
		PerceptualAssociativeMemory pam = (PerceptualAssociativeMemory) module;
		SensoryMemory sm = (SensoryMemory) lida
				.getSubmodule(ModuleName.SensoryMemory);

		// Nodes
		LidaElementFactory factory = LidaElementFactory.getInstance();
		PamNodeImpl wood = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"wood");
		pam.addDefaultNode(wood);
		PamNodeImpl gold = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"gold");
		pam.addDefaultNode(gold);
		PamNodeImpl metal = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"metal");
		pam.addDefaultNode(metal);
		PamNodeImpl solid = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"solid");
		pam.addDefaultNode(solid);
		PamNodeImpl iron = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"iron");
		pam.addDefaultNode(iron);
		PamNodeImpl plastic = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"plastic");
		pam.addDefaultNode(plastic);
		PamNodeImpl noMetal = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"noMetal");
		pam.addDefaultNode(noMetal);
		PamNodeImpl topLeft = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"topLeft");
		pam.addDefaultNode(topLeft);
		PamNodeImpl bottomRight = (PamNodeImpl) factory.getNode(
				"PamNodeImpl", "bottomRight");
		pam.addDefaultNode(bottomRight);
		// Links
		pam.addNewLink(gold, metal, LinkCategoryNode.CHILD,1.0);
		pam.addNewLink(metal, solid, LinkCategoryNode.CHILD,1.0);
		pam.addNewLink(iron, metal, LinkCategoryNode.CHILD,1.0);
		pam.addNewLink(wood, noMetal, LinkCategoryNode.CHILD,1.0);
		pam.addNewLink(plastic, noMetal, LinkCategoryNode.CHILD,1.0);
		pam.addNewLink(metal, noMetal, LinkCategoryNode.CHILD,1.0);
		pam.addNewLink(wood, solid, LinkCategoryNode.GROUNDING,1.0);

		pam.addNewLink(topLeft, wood, LinkCategoryNode.CHILD,1.0);

		// Feature detectors
		FeatureDetector fd = new BasicDetector(gold, sm, pam);
		fd.setTicksPerStep(5);
		pam.addFeatureDetector(fd);
		fd = new BasicDetector(iron, sm, pam);
		fd.setTicksPerStep(3);
		pam.addFeatureDetector(fd);
		fd = new BasicDetector(wood, sm, pam);
		fd.setTicksPerStep(2);
		pam.addFeatureDetector(fd);
		fd = new TopLeftDetector(topLeft, sm, pam);
		fd.setTicksPerStep(7);
		pam.addFeatureDetector(fd);
		fd = new BottomRightDetector(bottomRight, sm, pam);
		fd.setTicksPerStep(3);
		pam.addFeatureDetector(fd);

		PropagationBehavior b = new UpscalePropagationBehavior();
		pam.setPropagationBehavior(b);
		//driver.setInitialTasks(pam.getFeatureDetectors());
	}

}
