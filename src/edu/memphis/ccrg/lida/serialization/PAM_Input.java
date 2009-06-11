package edu.memphis.ccrg.lida.serialization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.XStream;

import edu.memphis.ccrg.lida.perception.FeatureDetector;
import edu.memphis.ccrg.lida.perception.FeatureDetectorImpl;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.perception.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeFactory;
import edu.memphis.ccrg.lida.shared.strategies.BasicDetectBehavior;

public class PAM_Input {

	//TODO return a boolean for success/fail?
	public void read(PerceptualAssociativeMemory pam, String inputPath) {

		XStream xstream = new XStream();
		Map<String, Object> params = new HashMap<String, Object>();
		//TODO: Loopify
		double upscale = 0.7, 
			   downscale = 0.5, 
			   selectivity = 0.9;		
		params.put("upscale", upscale);
		params.put("downscale", downscale);
		params.put("selectivity", selectivity);		
		pam.setParameters(params);
		
		//ADD NODES, FeatureDetectors, and LINKS
		Set<Node> nodes = new HashSet<Node>();

    	NodeFactory factory = NodeFactory.getInstance();
    	factory.addNodeType("PamNodeImpl", "edu.memphis.ccrg.lida.perception.PamNodeImpl");
    	//Ideally we loop over a text file reading in the ID and label for each node.
    	PamNodeImpl gold = (PamNodeImpl)factory.getNode("PamNodeImpl", 1, "gold");
    	PamNodeImpl pit = (PamNodeImpl)factory.getNode("PamNodeImpl", 2, "pit");
    	PamNodeImpl wumpus = (PamNodeImpl)factory.getNode("PamNodeImpl", 3, "wumpus");
    	PamNodeImpl agent = (PamNodeImpl)factory.getNode("PamNodeImpl", 4, "agent");
    	PamNodeImpl wall = (PamNodeImpl)factory.getNode("PamNodeImpl", 5, "wall");
    	
    	int layerDepthA = 0;
    	gold.setLayerDepth(layerDepthA);
    	pit.setLayerDepth(layerDepthA);
    	wumpus.setLayerDepth(layerDepthA);
    	agent.setLayerDepth(layerDepthA);
    	wall.setLayerDepth(layerDepthA);

    	//Add nodes to a Set
    	nodes.add(gold);
    	nodes.add(pit);
    	nodes.add(wumpus);
    	nodes.add(agent);
    	nodes.add(wall);

    	//Feature detectors
//    	Map<String, Integer> codeMap = new HashMap<String, Integer>();
//    	//TODO: Again this can be loopified.
//    	codeMap.put("pit", 0);
//    	codeMap.put("wumpus", 1);
//    	codeMap.put("gold", 2);
//    	codeMap.put("agent", 3);	
//    	codeMap.put("wall", 4);
    	List<FeatureDetector> featureDetectors = new ArrayList<FeatureDetector>();
    	BasicDetectBehavior featureDetectorBehavior = new BasicDetectBehavior(); 
		featureDetectors.add(new FeatureDetectorImpl(gold, featureDetectorBehavior));
		featureDetectors.add(new FeatureDetectorImpl(pit, featureDetectorBehavior));
		featureDetectors.add(new FeatureDetectorImpl(wumpus, featureDetectorBehavior));
		featureDetectors.add(new FeatureDetectorImpl(agent, featureDetectorBehavior));
		featureDetectors.add(new FeatureDetectorImpl(wall, featureDetectorBehavior));
		
		Set<Link> links = new HashSet<Link>();    	
		pam.addToPAM(nodes, featureDetectors, links);
		
		try{
			//FileReader fr = new FileReader(inputPath);
		
		}catch(Exception e){
			System.out.println(e);
		}		
	}//method
	
}//class
