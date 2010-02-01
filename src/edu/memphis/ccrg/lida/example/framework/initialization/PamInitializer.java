package edu.memphis.ccrg.lida.example.framework.initialization;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.memphis.ccrg.lida.example.genericlida.featuredetectors.BasicDetector;
import edu.memphis.ccrg.lida.example.genericlida.featuredetectors.BottomRightDetector;
import edu.memphis.ccrg.lida.example.genericlida.featuredetectors.TopLeftDetector;
import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.ModuleDriver;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.Initializable;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;
import edu.memphis.ccrg.lida.framework.shared.LinkType;
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
			Properties properties) {
		
		
		PerceptualAssociativeMemory pam = (PerceptualAssociativeMemory) module;
		SensoryMemory sm = (SensoryMemory) lida
				.getSubmodule(ModuleName.SensoryMemory);
		ModuleDriver driver = lida.getModuleDriver(ModuleName.PamDriver);
		pam.setTaskSpawner(driver);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("upscale", properties.getProperty("pam.Upscale"));
		params.put("downscale", properties.getProperty("pam.Downscale"));
		params.put("selectivity", properties.getProperty("pam.Selectivity"));
		pam.setParameters(params);

		// Nodes
		// TODO: Make this a loop. Reading in the nodes from a file.
		NodeFactory factory = NodeFactory.getInstance();
		// PamNodeImpl gold = (PamNodeImpl)factory.storeNode("PamNodeImpl",
		// "gold");
		// PamNodeImpl metal = (PamNodeImpl)factory.storeNode("PamNodeImpl",
		// "metal");
		// PamNodeImpl solid = (PamNodeImpl)factory.storeNode("PamNodeImpl",
		// "solid");
		// PamNodeImpl iron = (PamNodeImpl)factory.storeNode("PamNodeImpl",
		// "iron");
		// PamNodeImpl plastic = (PamNodeImpl)factory.storeNode("PamNodeImpl",
		// "plastic");
		// PamNodeImpl noMetal = (PamNodeImpl)factory.storeNode("PamNodeImpl",
		// "noMetal");
		PamNodeImpl wood = (PamNodeImpl) factory.storeNode("PamNodeImpl",
				"wood");
		PamNodeImpl gold = (PamNodeImpl) factory.storeNode("PamNodeImpl",
				"gold");
		PamNodeImpl metal = (PamNodeImpl) factory.storeNode("PamNodeImpl",
				"metal");
		PamNodeImpl solid = (PamNodeImpl) factory.storeNode("PamNodeImpl",
				"solid");
		PamNodeImpl iron = (PamNodeImpl) factory.storeNode("PamNodeImpl",
				"iron");
		PamNodeImpl plastic = (PamNodeImpl) factory.storeNode("PamNodeImpl",
				"plastic");
		PamNodeImpl noMetal = (PamNodeImpl) factory.storeNode("PamNodeImpl",
				"noMetal");
		PamNodeImpl topLeft = (PamNodeImpl) factory.storeNode("PamNodeImpl",
				"topLeft");
		PamNodeImpl bottomRight = (PamNodeImpl) factory.storeNode(
				"PamNodeImpl", "bottomRight");
		pam.addNodes(factory.getStoredNodes());
		// Links
		// TODO: make this a loop
		factory.storeLink(gold, metal, LinkType.CHILD);
		factory.storeLink(metal, solid, LinkType.CHILD);
		factory.storeLink(iron, metal, LinkType.CHILD);
		factory.storeLink(wood, noMetal, LinkType.CHILD);
		factory.storeLink(plastic, noMetal, LinkType.CHILD);
		factory.storeLink(metal, noMetal, LinkType.CHILD);
		factory.storeLink(wood, solid, LinkType.GROUNDING);

		factory.storeLink(topLeft, wood, LinkType.CHILD);
		pam.addLinks(factory.getStoredLinks());

		// Feature detectors
		// TODO: make this a loop
		FeatureDetector fd = new BasicDetector(gold, sm, pam);
		fd.setNumberOfTicksPerStep(5);
		pam.addFeatureDetector(fd);
		fd = new BasicDetector(iron, sm, pam);
		fd.setNumberOfTicksPerStep(3);
		pam.addFeatureDetector(fd);
		fd = new BasicDetector(wood, sm, pam);
		fd.setNumberOfTicksPerStep(2);
		pam.addFeatureDetector(fd);
		fd = new TopLeftDetector(topLeft, sm, pam);
		fd.setNumberOfTicksPerStep(7);
		pam.addFeatureDetector(fd);
		fd = new BottomRightDetector(bottomRight, sm, pam);
		fd.setNumberOfTicksPerStep(3);
		pam.addFeatureDetector(fd);

		PropagationBehavior b = new UpscalePropagationBehavior();
		pam.setPropagationBehavior(b);
		driver.setInitialTasks(pam.getFeatureDetectors());
	}// method

}// class
