/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package alifeagent.featuredetectors;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.tasks.DetectionAlgorithm;
import edu.memphis.ccrg.lida.pam.tasks.MultipleDetectionAlgorithm;

public class HealthDetector extends MultipleDetectionAlgorithm implements DetectionAlgorithm {

	private PamLinkable goodHealth;
	private PamLinkable fairHealth;
	private PamLinkable badHealth;
	private final String modality = "";
	private Map<String, Object> detectorParams = new HashMap<String, Object>();
	
	@Override
	public void init(){
		super.init();
		goodHealth = pamNodeMap.get("goodHealth");
		fairHealth = pamNodeMap.get("fairHealth");
		badHealth = pamNodeMap.get("badHealth");
		detectorParams.put("mode","health");
	}
	
	@Override
	public void detectLinkables() {
		double healthValue = (Double)sensoryMemory.getSensoryContent(modality, detectorParams);

		if(healthValue > 0.66){
			pam.receiveExcitation(goodHealth, 1.0);
		}else if(healthValue > 0.33){
			pam.receiveExcitation(fairHealth, 1.0);
		}else{
			pam.receiveExcitation(badHealth, 1.0);
		}
		
	}

}
