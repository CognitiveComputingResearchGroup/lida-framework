/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.sensorymotormemory.sensorymotorsystem.EE;

import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;

/**
 * KF: the Kalman filter
 * A Kalman filter is implemented to estimate a specific actuators states
 *
 * @author Daqi
 */
public interface KF extends FrameworkTask{

    @Override
    public void init();

    /*
     * recieve motor command that drives the estimation
     */
    public void recieveMotorCommand(Object o);

    /*
     * recieve new measurement(s), which is supposed to be combined with
     * prior knowledge of the statess
     */
    public void recieveMeasturement(Object o);

    /*
     * output the current (newest) estimated state
     */
    public Object estimatedSensoryData();

    /*
     * To run the Kalman filter
     */
    public void execute();

}
