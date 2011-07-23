/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package alifeagent.environment;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.alife.elements.ALifeObject;
import edu.memphis.ccrg.alife.elements.ALifeObjectImpl;
import edu.memphis.ccrg.alife.elements.Cell;
import edu.memphis.ccrg.alife.world.ALifeWorld;
import edu.memphis.ccrg.alife.world.WorldLoader;
import edu.memphis.ccrg.lida.environment.EnvironmentImpl;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

public class ALifeEnvironment extends EnvironmentImpl {

    private static final Logger logger = Logger.getLogger(ALifeEnvironment.class.getCanonicalName());
    private final int DEFAULT_TICKS_PER_RUN = 100;
    private final double DEFAULT_HEALTH_DECAY = 0.005;
    private double healthDecayRate;
    
	private ALifeWorld world;
	private ALifeObject agentObject;
	private static Set<ALifeObject> outOfBounds;

	@Override
	public void init() {
		Properties operationsProperties = new Properties();
        Properties objectsProperties = new Properties();
        try {
            operationsProperties.load(new FileReader((String) getParam("operationsConfig","configs/operations.properties")));
            objectsProperties.load(new FileReader((String) getParam("objectsConfig","configs/objects.properties")));

        } catch (IOException ex) {
        	logger.log(Level.SEVERE, "Error reading ALifeWorld properties files", ex);
        }

        healthDecayRate = (Double)getParam("environment.healthDecayRate", DEFAULT_HEALTH_DECAY);
        
        world = WorldLoader.loadWorld(10, 10, operationsProperties, objectsProperties);
        agentObject = world.getObject((String) getParam("agentName","agent"));
        int ticksPerRun = (Integer)getParam("environment.ticksPerRun",DEFAULT_TICKS_PER_RUN);
        taskSpawner.addTask(new EnvironmentBackgroundTask(ticksPerRun));
        
        outOfBounds = new HashSet<ALifeObject>();
        ALifeObject ooB = new ALifeObjectImpl();
        ooB.setName("outOfBounds");
        outOfBounds.add(ooB);
	}

	@SuppressWarnings("serial")
	private class EnvironmentBackgroundTask extends FrameworkTaskImpl{

		public EnvironmentBackgroundTask(int ticksPerRun){
			super(ticksPerRun);
		}
		@Override
		protected void runThisFrameworkTask() {
			world.updateWorldState();		
			agentObject.decreaseHealth(healthDecayRate);
		}		
	}
	
	@Override
	public Object getState(Map<String, ?> params) {		
		String mode = (String)params.get("mode");
		if("seethis".equals(mode)){
			Cell cell = (Cell) agentObject.getContainer();
			try{
				return world.performOperation("seeobjects", agentObject, null, cell.getXCoordinate(),cell.getYCoordinate());
			}catch(Exception e){
				e.printStackTrace();
				return null;
			}
		}else if("seenext".equals(mode)){
			Cell cell = (Cell) agentObject.getContainer();
			int x = cell.getXCoordinate();
			int y = cell.getYCoordinate();
			Object o = agentObject.getAttribute("direction");
			char direction = (Character)o;
			switch (direction){
			case 'N':
				y--;
				break;
			case 'S':
				y++;
				break;
			case 'E':
				x++;
				break;
			case 'W':
				x--;
				break;	
			}
			if(x>=0 && x<world.getWidth()&&y>=0 && y<world.getHeight()){
				return world.performOperation("seeobjects", agentObject, null,x,y);
			}else{
				return outOfBounds;
			}
		}else if("health".equals(mode)){
			return agentObject.getHealth();
		}
		return null;
	}

	@Override
	public void processAction(Object o) {
		String actionName = (String)o;
		if(actionName != null){
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST, "performing action {1}", 
						new Object[]{TaskManager.getCurrentTick(),actionName});
			}
			world.performOperation(actionName, agentObject, null);
		}else{
			logger.log(Level.WARNING, "received a null actionName", TaskManager.getCurrentTick());
		}
	}

	@Override
	public void resetState() {
		
	}
	
	@Override
	public Object getModuleContent(Object... params) {
		return world;
	}

}
