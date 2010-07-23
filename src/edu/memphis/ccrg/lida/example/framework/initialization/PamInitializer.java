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
			Map<String,?> params) {		
		PerceptualAssociativeMemory pam = (PerceptualAssociativeMemory) module;
		SensoryMemory sm = (SensoryMemory) lida
				.getSubmodule(ModuleName.SensoryMemory);
		ModuleDriver driver = lida.getModuleDriver(ModuleName.PamDriver);
		pam.setTaskSpawner(driver);

		pam.setParameters(params);

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
		// TODO: make this a loop
		pam.addNewLink(gold, metal, LinkType.CHILD,1.0);
		pam.addNewLink(metal, solid, LinkType.CHILD,1.0);
		pam.addNewLink(iron, metal, LinkType.CHILD,1.0);
		pam.addNewLink(wood, noMetal, LinkType.CHILD,1.0);
		pam.addNewLink(plastic, noMetal, LinkType.CHILD,1.0);
		pam.addNewLink(metal, noMetal, LinkType.CHILD,1.0);
		pam.addNewLink(wood, solid, LinkType.Grounding,1.0);

		pam.addNewLink(topLeft, wood, LinkType.CHILD,1.0);

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
		//driver.setInitialTasks(pam.getFeatureDetectors());
	}// method

}// class
