/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package alifeagent.environment.operations;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.alife.elements.ALifeObject;
import edu.memphis.ccrg.alife.opreations.WorldOperation;
import edu.memphis.ccrg.alife.world.ALifeWorld;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

public class TurnAroundOperation implements WorldOperation {
	
	private static final Logger logger = Logger.getLogger(TurnAroundOperation.class.getCanonicalName());
	private final String attributeName = "direction";

	@Override
	public Object performOperation(ALifeWorld world, ALifeObject subject,
			ALifeObject[] objects, Object... params) {
		char currentDirection = (Character)subject.getAttribute(attributeName);
		switch(currentDirection){
			case 'N':
				subject.setAttribute(attributeName, 'S');
				break;
			case 'W':
				subject.setAttribute(attributeName, 'E');
				break;
			case 'S':
				subject.setAttribute(attributeName, 'N');
				break;
			case 'E':
				subject.setAttribute(attributeName, 'W');
				break;
		}		
		logger.log(Level.FINE, "agent turns around",TaskManager.getCurrentTick());
		return subject;
	}

}
