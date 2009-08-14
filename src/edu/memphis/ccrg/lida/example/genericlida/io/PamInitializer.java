package edu.memphis.ccrg.lida.example.genericlida.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.memphis.ccrg.lida.example.genericlida.pam.VisionFeatureDetector;
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

	//TODO return a boolean for success/fail?
	public void initModule(Properties properties) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("upscale", properties.getProperty("pamUpscale"));
		params.put("downscale", properties.getProperty("pamDownscale"));
		params.put("selectivity", properties.getProperty("pamSelectivity"));		
		pam.setParameters(params);
		
		//Nodes
    	NodeFactory factory = NodeFactory.getInstance();
    	PamNodeImpl gold = (PamNodeImpl)factory.storeNode("PamNodeImpl", "gold");
    	PamNodeImpl metal = (PamNodeImpl)factory.storeNode("PamNodeImpl", "metal");
    	PamNodeImpl solid = (PamNodeImpl)factory.storeNode("PamNodeImpl", "solid");
    	//Links
    	factory.storeLink(gold, metal, LinkType.GROUNDING);
    	factory.storeLink(metal, solid, LinkType.GROUNDING);   
    	
    	//Feature detectors
    	//TODO: Separate from PAM?
    	List<FeatureDetector> featureDetectors = new ArrayList<FeatureDetector>(); 
		featureDetectors.add(new VisionFeatureDetector(gold, sm, pam));

		pam.addToPam(factory.getStoredNodes(), featureDetectors, factory.getStoredLinks());
	}//method
	
}//class
