package edu.memphis.ccrg.lida.framework.initialization;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.memphis.ccrg.lida.example.genericlida.featuredetectors.TopLeftDetector;
import edu.memphis.ccrg.lida.example.genericlida.main.VisionFeatureDetector;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.shared.LinkType;
import edu.memphis.ccrg.lida.framework.shared.NodeFactory;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.featuredetector.FeatureDetector;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

public class PamInitializer implements Initializer {
	
	private PerceptualAssociativeMemory pam;
	private SensoryMemory sm;
	private LidaTaskManager taskManager;
	
	public PamInitializer(PerceptualAssociativeMemory pam, SensoryMemory sm, LidaTaskManager tm){
		taskManager = tm;
		this.pam = pam;
		this.sm = sm;
	}

	public void initModule(Properties properties) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("upscale", properties.getProperty("pam.Upscale"));
		params.put("downscale", properties.getProperty("pam.Downscale"));
		params.put("selectivity", properties.getProperty("pam.Selectivity"));		
		pam.setParameters(params);
		
		//Nodes
		//TODO: Make this a loop.  Reading in the nodes from a file.
    	NodeFactory factory = NodeFactory.getInstance();
    	PamNodeImpl gold = (PamNodeImpl)factory.storeNode("PamNodeImpl", "gold");
    	PamNodeImpl metal = (PamNodeImpl)factory.storeNode("PamNodeImpl", "metal");
    	PamNodeImpl solid = (PamNodeImpl)factory.storeNode("PamNodeImpl", "solid");
//    	PamNodeImpl iron = (PamNodeImpl)factory.storeNode("PamNodeImpl", "iron");
//    	PamNodeImpl plastic = (PamNodeImpl)factory.storeNode("PamNodeImpl", "plastic");
//    	PamNodeImpl noMetal = (PamNodeImpl)factory.storeNode("PamNodeImpl", "noMetal");
//    	PamNodeImpl wood = (PamNodeImpl)factory.storeNode("PamNodeImpl", "wood");
    	pam.addNodes(factory.getStoredNodes());
    	
    	//Links
    	//TODO: make this a loop
    	factory.storeLink(gold, metal, LinkType.CHILD);
    	factory.storeLink(metal, solid, LinkType.CHILD);   
//    	factory.storeLink(iron, metal, LinkType.CHILD);   
//    	factory.storeLink(wood, noMetal, LinkType.CHILD);   
//    	factory.storeLink(plastic, noMetal, LinkType.CHILD);   
//    	factory.storeLink(metal, noMetal, LinkType.CHILD);   
//    	factory.storeLink(wood, solid, LinkType.GROUNDING);   
    	pam.addLinks(factory.getStoredLinks());
    	
    	//Feature detectors
    	//TODO: make this a loop
    	//FeatureDetector fd = new VisionFeatureDetector(gold, sm, pam, taskManager);
    	//pam.addFeatureDetector(fd);
    	FeatureDetector fd = new TopLeftDetector(gold, sm, pam, taskManager);
    	pam.addFeatureDetector(fd);
	}//method
	
}//class
