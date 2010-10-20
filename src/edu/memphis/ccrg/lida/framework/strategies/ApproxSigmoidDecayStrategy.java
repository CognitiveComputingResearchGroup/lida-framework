/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.strategies;

import java.util.Map;

/**
 * 
 * @author Daqi Dong
 *
 */
public class ApproxSigmoidDecayStrategy implements DecayStrategy{
	
	private double[] excitationArray = new double[] /* Fomula is T= ln(y/(1-y)); Size is 99; Granularity is 0.1^3 */
   	                   {-4.595, -3.892, -3.476, -3.178, -2.944, -2.752, -2.587, -2.442, -2.314, -2.197,  /* 00 ~ 09 */
   						-2.091, -1.992, -1.901, -1.815, -1.735, -1.658, -1.586, -1.516, -1.450, -1.386,  /* 10 ~ 19 */
   						-1.325, -1.266, -1.208, -1.153, -1.099, -1.046, -0.995, -0.944, -0.895, -0.847,  /* 20 ~ 29 */
   						-0.800, -0.754, -0.708, -0.663, -0.619, -0.575, -0.532, -0.490, -0.447, -0.405,  /* 30 ~ 39 */
   						-0.364, -0.323, -0.282, -0.241, -0.201, -0.160, -0.120, -0.080, -0.040,  0.000,  /* 40 ~ 49 */
   						 0.040,  0.080,  0.120,  0.160,  0.201,  0.241,  0.282,  0.323,  0.364,  0.405,  /* 50 ~ 59 */
   						 0.447,  0.490,  0.532,  0.575,  0.619,  0.663,  0.708,  0.754,  0.800,  0.847,  /* 60 ~ 69 */
   						 0.895,  0.944,  0.995,  1.046,  1.099,  1.153,  1.208,  1.266,  1.325,  1.386,  /* 70 ~ 79 */
   						 1.450,  1.516,  1.586,  1.658,  1.735,  1.815,  1.901,  1.992,  2.091,  2.197,  /* 80 ~ 89 */
   						 2.314,  2.442,  2.587,  2.752,  2.944,  3.178,  3.476,  3.892,  4.595           /* 90 ~ 98 */
   			           };
	                       	
   	private double[] activationArray = new double[] /* Fomula is Y = 1/(1+ e^(-t)); Size is 101; Granularity is 0.1^6 */
   	                   {0.006693, 0.007392, 0.008163, 0.009013, 0.009952, 0.010987, 0.012128, 0.013387, 0.014774, 0.016302,  /* 00 ~ 09 */
   						0.017986, 0.019840, 0.021881, 0.024127, 0.026597, 0.029312, 0.032295, 0.035571, 0.039166, 0.043107,  /* 10 ~ 19 */
   						0.047426, 0.052154, 0.057324, 0.062973, 0.069138, 0.075858, 0.083173, 0.091123, 0.099750, 0.109097,  /* 20 ~ 29 */
   						0.119203, 0.130108, 0.141851, 0.154465, 0.167982, 0.182426, 0.197816, 0.214165, 0.231475, 0.249740,  /* 30 ~ 39 */
   						0.268941, 0.289050, 0.310026, 0.331812, 0.354344, 0.377541, 0.401312, 0.425557, 0.450166, 0.475021,  /* 40 ~ 49 */
   						0.500000, 0.524979, 0.549834, 0.574443, 0.598688, 0.622459, 0.645656, 0.668188, 0.689974, 0.710950,  /* 50 ~ 59 */
   						0.731059, 0.750260, 0.768525, 0.785835, 0.802184, 0.817574, 0.832018, 0.845535, 0.858149, 0.869892,  /* 60 ~ 69 */
   						0.880797, 0.890903, 0.900250, 0.908877, 0.916827, 0.924142, 0.930862, 0.937027, 0.942676, 0.947846,  /* 70 ~ 79 */
   						0.952574, 0.956893, 0.960834, 0.964429, 0.967705, 0.970688, 0.973403, 0.975873, 0.978119, 0.980160,  /* 80 ~ 89 */
   						0.982014, 0.983698, 0.985226, 0.986613, 0.987872, 0.989013, 0.990048, 0.990987, 0.991837, 0.992608,  /* 90 ~ 99 */
   						0.993307                                                                                             /* 100 */
   			           };
	@Override
	public void init(Map<String, ? extends Object> parameters) {
	    /* Is this function init necessary?*/
	}

	@Override
	public double decay(double currentActivation, long ticks, Map<String,? extends Object>params){
		int indexOfArray = 0, m;
		double currentActivation_tmp;
		double currentApproxExcitation, newApproxExcitation;
        double newActivation = 0.0;
        m = (Integer) params.get("m");
        currentActivation_tmp = currentActivation;

        /* Set scaledValue of activation is from 0.01 to 0.99*/
        if ( currentActivation_tmp < 0.01)
        	currentActivation_tmp = 0.01;
        if ( currentActivation_tmp > 0.99)
        	currentActivation_tmp = 0.99;
        
        /* Set granularity of activation is 0.01, and index of array is from 0 to 98*/   
        indexOfArray = (int)(currentActivation_tmp * 100 + 0.5) - 1;
        /*Get current approx value of excitation from precalculated array*/
        currentApproxExcitation = excitationArray[indexOfArray];
        
        newApproxExcitation = currentApproxExcitation - m * ticks;
        
        /* Set scaledValue of Excitation is from -5.0 to 5.0*/
        if ( newApproxExcitation < -5.0 )
        	newApproxExcitation = -5.0;
        if ( newApproxExcitation > 5.0 )
        	newApproxExcitation = 5.0;
        /*Set granularity of of Excitation is 0.1, and index of array is from 0 to 100*/
        if ( newApproxExcitation >= 0 )
        	indexOfArray = (int)(newApproxExcitation * 10 + 0.5) + 50;
        else
        	indexOfArray = (int)(newApproxExcitation * 10 - 0.5) + 50;
        
        newActivation = activationArray[indexOfArray];
        return newActivation;
	}

	@Override
	public double decay(double currentActivation, long ticks, Object... params) {
		// TODO Auto-generated method stub
		return 0;
	}

}
