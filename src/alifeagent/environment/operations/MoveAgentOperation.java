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
import edu.memphis.ccrg.alife.opreations.MoveOperation;
import edu.memphis.ccrg.alife.world.ALifeWorld;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

public class MoveAgentOperation extends MoveOperation {

	private static final Logger logger = Logger.getLogger(MoveAgentOperation.class.getCanonicalName());
	private static final double DEFAULT_BUMP_HEALTH_PENALTY = 0.05;

	@Override
	public Object performOperation(ALifeWorld world, ALifeObject subject,
			ALifeObject[] object, Object... params) {

		if (!move(world, subject, (Character) subject.getAttribute("direction"))) {
			subject.decreaseHealth(DEFAULT_BUMP_HEALTH_PENALTY);
			return false;
		}
		logger.log(Level.FINE, "agent moves forward",TaskManager.getCurrentTick());
		return true;
	}
}
