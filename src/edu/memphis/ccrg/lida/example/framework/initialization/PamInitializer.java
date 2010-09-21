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
import edu.memphis.ccrg.lida.framework.ModuleDriver;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.Initializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.NodeFactory;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.PropagationBehavior;
import edu.memphis.ccrg.lida.pam.UpscalePropagationBehavior;
import edu.memphis.ccrg.lida.pam.featuredetector.FeatureDetector;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

public class PamInitializer implements Initializer {

	public PamInitializer() {
	}

	public void initModule(Initializable module, Lida lida,
			Map<String,?> params) {		
		PerceptualAssociativeMemory pam = (PerceptualAssociativeMemory) module;
		SensoryMemory sm = (SensoryMemory) lida
				.getSubmodule(ModuleName.SensoryMemory);
		ModuleDriver driver = lida.getModuleDriver(ModuleName.PamDriver);
		pam.setTaskSpawner(driver);

		pam.init(params);

		// Nodes
		NodeFactory factory = NodeFactory.getInstance();
		PamNodeImpl wood = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"wood");
		pam.addNode(wood);
		PamNodeImpl gold = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"gold");
		pam.addNode(gold);
		PamNodeImpl metal = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"metal");
		pam.addNode(metal);
		PamNodeImpl solid = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"solid");
		pam.addNode(solid);
		PamNodeImpl iron = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"iron");
		pam.addNode(iron);
		PamNodeImpl plastic = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"plastic");
		pam.addNode(plastic);
		PamNodeImpl noMetal = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"noMetal");
		pam.addNode(noMetal);
		PamNodeImpl topLeft = (PamNodeImpl) factory.getNode("PamNodeImpl",
				"topLeft");
		pam.addNode(topLeft);
		PamNodeImpl bottomRight = (PamNodeImpl) factory.getNode(
				"PamNodeImpl", "bottomRight");
		pam.addNode(bottomRight);
		// Links
		pam.addNewLink(gold, metal, LinkCategory.Child,1.0);
		pam.addNewLink(metal, solid, LinkCategory.Child,1.0);
		pam.addNewLink(iron, metal, LinkCategory.Child,1.0);
		pam.addNewLink(wood, noMetal, LinkCategory.Child,1.0);
		pam.addNewLink(plastic, noMetal, LinkCategory.Child,1.0);
		pam.addNewLink(metal, noMetal, LinkCategory.Child,1.0);
		pam.addNewLink(wood, solid, LinkCategory.Grounding,1.0);

		pam.addNewLink(topLeft, wood, LinkCategory.Child,1.0);

		// Feature detectors
		FeatureDetector fd = new BasicDetector(gold, sm, pam);
		fd.setNumberOfTicksPerRun(5);
		pam.addFeatureDetector(fd);
		fd = new BasicDetector(iron, sm, pam);
		fd.setNumberOfTicksPerRun(3);
		pam.addFeatureDetector(fd);
		fd = new BasicDetector(wood, sm, pam);
		fd.setNumberOfTicksPerRun(2);
		pam.addFeatureDetector(fd);
		fd = new TopLeftDetector(topLeft, sm, pam);
		fd.setNumberOfTicksPerRun(7);
		pam.addFeatureDetector(fd);
		fd = new BottomRightDetector(bottomRight, sm, pam);
		fd.setNumberOfTicksPerRun(3);
		pam.addFeatureDetector(fd);

		PropagationBehavior b = new UpscalePropagationBehavior();
		pam.setPropagationBehavior(b);
		//driver.setInitialTasks(pam.getFeatureDetectors());
	}// method

}// class
