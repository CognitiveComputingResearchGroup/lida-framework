/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.sensorymotormemory.sensorymotorsystem.EE;

import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;

/**
 * Set the Kalman filter as a computational framework task
 * @author Daqi
 */
public abstract class KFImpl extends FrameworkTaskImpl implements KF{

    protected static final double ARM2_INIT_POS = -0.18;
    protected static final double ARM3_INIT_POS = -1.19;
    protected static final double ARM4_INIT_POS = -1.77;

    protected static final double ARM2_LENGTH = 0.155;
    protected static final double ARM3_LENGTH = 0.135;
    protected static final double ARM4_LENGTH = 0.218;

    protected static final double FIXED_BASE_HEIGHT = 0.251596;//251.596 mm
    protected static final double FIXED_BASE_DISTANCE = 0.1897258; //189.7258 mm

    protected static final double TABLE_SURFACE_HEIGHT = 0.1;

    /*
    /* This hand position is represented in a 2D dimension (X-Y)
    /* Current X-dim (Distance) = FIXED_BASE_DISTANCE + D1
    /* Current Y-dim (Height) = FIXED_BASE_HEIGHT + H2
    /* D1 = the value of "Length of arm 2 * sine(Degree of arm2) + Length of arm3 * sine(Degree of arm4)"
     * H2 = H1 - 218 (The length of arm4)
     * H1 = The value of "Length of arm 2 * cosine(Degree of arm2) - Length of arm3 * cosine(Degree of arm4)"
     */

    protected double Process_Variance_Q, Measurement_Variance_R, Estimation_Noise_P;

    protected double Parameter_A, Parameter_B, Parameter_H;

    protected double KFGain_K;

    public KFImpl(int ticksPerRun) {
        super(ticksPerRun);
    }
    
    @Override
    public abstract void recieveMotorCommand(Object o);

    @Override
    public abstract void recieveMeasturement(Object o);

    @Override
    public abstract Object estimatedSensoryData();

    @Override
    protected void runThisFrameworkTask() {
        execute();
    }

    @Override
    public abstract void execute();

}
