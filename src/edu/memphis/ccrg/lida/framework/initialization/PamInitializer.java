package edu.memphis.ccrg.lida.framework.initialization;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import edu.memphis.ccrg.lida.example.genericlida.main.VisionFeatureDetector;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.featuredetector.FeatureDetector;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;
import edu.memphis.ccrg.lida.shared.LinkType;
import edu.memphis.ccrg.lida.shared.NodeFactory;

public class PamInitializer implements Initializer {
	
	private PerceptualAssociativeMemory pam;
	private SensoryMemory sm;
	
	public PamInitializer(PerceptualAssociativeMemory pam, SensoryMemory sm){
		this.pam = pam;
		this.sm = sm;
	}

	//TODO: return a boolean for success/fail?
	public void initModule(Properties properties) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("upscale", properties.getProperty("pam.Upscale"));
		params.put("downscale", properties.getProperty("pam.Downscale"));
		params.put("selectivity", properties.getProperty("pam.Selectivity"));		
		pam.setParameters(params);
		
		//Nodes
    	NodeFactory factory = NodeFactory.getInstance();
    	PamNodeImpl gold = (PamNodeImpl)factory.storeNode("PamNodeImpl", "gold");
    	PamNodeImpl metal = (PamNodeImpl)factory.storeNode("PamNodeImpl", "metal");
    	PamNodeImpl solid = (PamNodeImpl)factory.storeNode("PamNodeImpl", "solid");
    	pam.addNodes(factory.getStoredNodes());
    	
    	//Links
    	factory.storeLink(gold, metal, LinkType.GROUNDING);
    	factory.storeLink(metal, solid, LinkType.GROUNDING);   
    	pam.addLinks(factory.getStoredLinks());
    	
    	//Feature detectors
    	//TODO: Separate from PAM?
    	FeatureDetector fd = new VisionFeatureDetector(gold, sm, pam);
    	pam.addFeatureDetector(fd);
	}//method
	
}//class
