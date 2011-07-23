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

public class AttackOperation implements WorldOperation {

	private static final Logger logger = Logger.getLogger(AttackOperation.class.getCanonicalName());
	private static final double DEFAULT_ATTACK_HEALTH_DECREASE = -0.3;
	String soughtObjectName = "agent";
	
	@Override
	public Object performOperation(ALifeWorld world, ALifeObject subject,
								   ALifeObject[] objects, Object... params) {
		
		soughtObjectName = (String) params[0];
		Cell currentCell = (Cell) subject.getContainer();
		Set<ALifeObject> cellObjects = currentCell.getObjects();
		for(ALifeObject obj: cellObjects){
			if(soughtObjectName.equals(obj.getName())){				
				obj.increaseHealth(DEFAULT_ATTACK_HEALTH_DECREASE);
				logger.log(Level.INFO, "agent attacked!",TaskManager.getCurrentTick());
				return obj;
			}
		}
		return null;
	}

}
