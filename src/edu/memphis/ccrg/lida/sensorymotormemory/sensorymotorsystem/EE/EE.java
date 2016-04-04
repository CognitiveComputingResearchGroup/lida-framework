/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.sensorymotormemory.sensorymotorsystem.EE;

import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * An environment emulator (EE) estimates the sensory data, the actuators states,
 * based on the copy of motor commands (MCs) sent to the actuators and
 * the new measurements (real sensory data with its noise)
 * @author Daqi
 */
public interface EE {

     /*
     * Store EE in long-term memory
     */
    public void load();

    public void save();

    public void init();

    /*
     * recieve the copy of motor commands sent to the actuators
     */
    public void recieveMotorCommands(Object o);

    /*
     * recieve the new measurements (real sensory data with noise)
     */
    public void recieveMeasurements(Object o);

    /*
     * send out the esitmated sensory data
     */
    public Object estSensoryData();

    public void estimation();
    
    /*
     * An EE involves executable elements, such as emulator, a kind of FrameworkTask.
     * Therefore we need a receiveTS method to reuse the TS of SMM.
     */
    public void receiveTS(TaskSpawner ts);

}
