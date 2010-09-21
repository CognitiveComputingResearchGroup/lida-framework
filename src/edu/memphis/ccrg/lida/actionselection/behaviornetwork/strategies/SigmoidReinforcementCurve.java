/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * SigmoidCurve.java
 *
 * Sidney D'Mello
 * Created on June 9, 2004, 7:53 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;


public class SigmoidReinforcementCurve implements ReinforcementCurve{
	
    private double x;
    private double y;
    
    private double a;
    private double c;
                
    public SigmoidReinforcementCurve(double a, double c) 
    {
        x = 0;
        y = 0;
        
        this.a = a;
        this.c = c;
    }
    
    public double getYIntercept(double x) throws ArithmeticException
    {
        if(this.x != x)
            y = 1/(1 + Math.exp((-a * x) + c));
        
        return y;
    }
    
    public double getXIntercept(double y) throws ArithmeticException
    {
        if(this.y != y)            
            x =  (-Math.log(((1-y)/y)) + c)/a;
        
        return x;
    }
    
    public double getA()
    {
        return a;
    }
    
    public double getC()
    {
        return c;
    }    
}
