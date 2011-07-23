/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package alifeagent.environment.operations;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.alife.elements.ALifeObject;
import edu.memphis.ccrg.alife.elements.Cell;
import edu.memphis.ccrg.alife.opreations.WorldOperation;
import edu.memphis.ccrg.alife.world.ALifeWorld;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

public class EatOperation implements WorldOperation {

	private static final Logger logger = Logger.getLogger(EatOperation.class.getCanonicalName());
	private static final double DEFAULT_FOOD_HEALTH_BONUS = 0.5;
	private final String soughtObjectName = "food";
	
	@Override
	public Object performOperation(ALifeWorld world, ALifeObject subject,
								   ALifeObject[] objects, Object... params) {
		Cell agentCell = (Cell) subject.getContainer();
		Set<ALifeObject> cellObjects = agentCell.getObjects();
		for(ALifeObject o: cellObjects){
			if(soughtObjectName.equals(o.getName())){
				agentCell.removeObject(o);
				subject.increaseHealth(DEFAULT_FOOD_HEALTH_BONUS);
				logger.log(Level.INFO, "agent eats food",TaskManager.getCurrentTick());
				return o;
			}
		}
		return null;
	}

}
