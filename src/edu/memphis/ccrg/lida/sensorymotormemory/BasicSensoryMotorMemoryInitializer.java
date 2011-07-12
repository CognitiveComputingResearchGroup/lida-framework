/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.memphis.ccrg.lida.sensorymotormemory;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.initialization.FullyInitializable;
import edu.memphis.ccrg.lida.framework.initialization.GlobalInitializer;
import edu.memphis.ccrg.lida.framework.initialization.Initializer;

/**
 * Basic SensoryMotorMemory {@link Initializer} which reads 
 * String parameters beginning with 'smm.' and creates a action-algorithm mapping based
 * on the parameter.  The definition is: <br/>
 * <b>actionName,algorithm</b>
 * @author Ryan McCall
 * @author Javier Snaider
 */
public class BasicSensoryMotorMemoryInitializer implements Initializer {

	private static final Logger logger = Logger.getLogger(BasicSensoryMotorMemoryInitializer.class.getCanonicalName());
	private static final GlobalInitializer initializer = GlobalInitializer.getInstance();
	private BasicSensoryMotorMemory smm; 
	
    @Override
    public void initModule(FullyInitializable module, Agent agent, Map<String, ?> params) {
        smm = (BasicSensoryMotorMemory) module;
        
        for(String key: params.keySet()){
	    	if(key.startsWith("smm.")){
	    		String smmDef = (String) params.get(key);
	    		logger.log(Level.INFO, "loading smm action-algorithm mapping: {0}", smmDef);
	    		String[] elements = smmDef.split(",");
	    		if(elements.length == 2){
	    			String actionName = elements[0].trim();
	    			String algorithmName = elements[1].trim();
	    			if("".equals(algorithmName)){
	    				logger.log(Level.WARNING, "missing algorithm name for smm: {0}",smmDef);
	    				continue;
	    			}
	    			Action action = (Action) initializer.getAttribute(actionName);
	    	        if(action != null){
	    	        	smm.addActionAlgorithm(action.getId(), algorithmName);
	    	        }else{
	    	        	logger.log(Level.WARNING, "could not find agent action: {0}",actionName);
	    	        }
	    		}else{
	    			logger.log(Level.WARNING, 
	    					"incorrect smm def: {0} must have form 'actionName,algorithm'",smmDef);
	    		}
	    		
	    	}
        }
    }
}