/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.sensorymotormemory.sensorymotorsystem;

import edu.memphis.ccrg.lida.sensorymotormemory.BasicSensoryMotorMemory;
import edu.memphis.ccrg.lida.sensorymotormemory.sensorymotorsystem.MPT.MPT;

/**
 * The Sensory Motor System (SMS) maintains multiple Motor Plan Template (MPT) that
 * generate motor commands
 * 
 * @author Daqi
 */
public abstract class SensoryMotorSystem extends BasicSensoryMotorMemory{
    
    //Load possible motor plan templates (MPTs)
    //the conctete data structure of the variable that store the MPTs need to 
    //be defined in the concrete method
    public abstract void loadMPT();
    
    //select a proper motor plan tempate (MPT) based on the selected behavior, a algorithem defined here
    public abstract MPT selectMPT(Object alg);
    
}
