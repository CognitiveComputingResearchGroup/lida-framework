/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.sensorymotormemory.sensorymotorsystem.MPT;

import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;

/**
 * FSM: Finite State Machine
 * @author Daqi
 */
public interface FSM extends FrameworkTask{

    /*
     * Initialize the variables,
     * when the Motor Plan Template was instantiated to the Motor Plan
     */
    @Override
    public void init();

    /*
     * Receive data on running time with one parameter
     */
    public void receiveData(Object o);

    /*
     * Receive data on running time with two parameters
     */
    public void receiveData(Object o1, Object o2);

    /*
     * Receive data on running time with three parameters
     */
    public void receiveData(Object o1, Object o2, Object o3);

    /*
     * Output the command
     */
    public Object outputCommands();

    /*
     * To specify the motor (variables) values inside the FSM
     * This method is called once by a MPT's specify() method, so that
     * the MPT is specified into a MP
     */
    public void specify(Object o);

    /*
     * To update the motor (variables) values inside the FSM
     * This method might be called every moment in the running time,
     * so taht the motor values are updated in online control process
     */
    public void update(Object o);

     /*
     * To run the FSM
     */
    public void execute();

}
