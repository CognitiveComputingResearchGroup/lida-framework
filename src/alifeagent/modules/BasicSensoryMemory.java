/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package alifeagent.modules;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.alife.elements.ALifeObject;
import edu.memphis.ccrg.lida.framework.shared.ConcurrentHashSet;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryImpl;

/**
 *
 * @author Javier Snaider
 * @author Ryan McCall
 */
public class BasicSensoryMemory extends SensoryMemoryImpl{

	private Set<ALifeObject> originObjects = new ConcurrentHashSet<ALifeObject>();
	private Set<ALifeObject> nextCellObjects = new ConcurrentHashSet<ALifeObject>();
	private volatile double health=1.0; 
    private Map<String,Object> sensorParam = new HashMap<String, Object>();
       
    @Override
    public void init() {
    }

    @SuppressWarnings("unchecked")
	public void runSensors() {
    	sensorParam.put("mode","seethis");
    	originObjects.clear();
    	originObjects.addAll((Set<ALifeObject>) environment.getState(sensorParam));
    	
    	sensorParam.put("mode","seenext");
    	nextCellObjects.clear();
    	Set<ALifeObject> nextObjects = (Set<ALifeObject>) environment.getState(sensorParam);
    	if(nextObjects != null){
    		nextCellObjects.addAll(nextObjects);
    	}
    	
    	sensorParam.put("mode","health");
    	health = (Double)environment.getState(sensorParam);
    }

    public Object getSensoryContent(String string, Map<String, Object> params) {
    	String mode = (String)params.get("mode");
        if("health".equals(mode)){
        	return health;
        }
        
        
    	
    	int location = (Integer)params.get("position");
    	String name = (String)params.get("object");
        Set<ALifeObject> requestedObjects = (location==0)?originObjects:nextCellObjects;
    	if(name == null || "".equals(name)){
    		return requestedObjects.isEmpty();
    	}
        
        for(ALifeObject o: requestedObjects){
        	if(o.getName().equals(name)){
        		return true;
        	}
        }
        return false;
    }
    
    @Override
    public Object getModuleContent(Object... os) {
        return null;
    }
    
    @Override
    public void decayModule(long l) {
    	//NA
    }

}
