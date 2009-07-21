package edu.memphis.ccrg.lida.example.genericlida.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.memphis.ccrg.lida.example.genericlida.pam.VisionFeatureDetector;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.perception.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.perception.featuredetector.FeatureDetector;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;
import edu.memphis.ccrg.lida.shared.LinkType;
import edu.memphis.ccrg.lida.shared.NodeFactory;

public class PamInput {

	//TODO return a boolean for success/fail?
	public void read(PerceptualAssociativeMemory pam, SensoryMemory sm, String inputPath) {

		Map<String, Object> params = new HashMap<String, Object>();
		double upscale = 0.7, 
			   downscale = 0.5, 
			   selectivity = 0.9;		
		params.put("upscale", upscale);
		params.put("downscale", downscale);
		params.put("selectivity", selectivity);		
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
    	List<FeatureDetector> featureDetectors = new ArrayList<FeatureDetector>(); 
		featureDetectors.add(new VisionFeatureDetector(gold, sm, pam));

		pam.addToPAM(factory.getStoredNodes(), featureDetectors, factory.getStoredLinks());
	}//method
	
}//class
