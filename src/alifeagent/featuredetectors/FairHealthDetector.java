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

import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;

public class FairHealthDetector extends BasicDetectionAlgorithm {

    private final String modality = "";
    private Map<String, Object> detectorParams = new HashMap<String, Object>();

    @Override
    public void init() {
        super.init();
        detectorParams.put("mode", "health");
    }

    @Override
    public double detect() {
        double healthValue = (Double) sensoryMemory.getSensoryContent(modality, detectorParams);
        double activation = 0.0;
        if (healthValue < 0.66 && healthValue > 0.33) {
            activation = 1.0;
        }
        return activation;
    }
}
