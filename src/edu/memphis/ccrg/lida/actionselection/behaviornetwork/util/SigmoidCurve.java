/*
 * SigmoidCurve.java
 *
 * Sidney D'Mello
 * Created on June 9, 2004, 7:53 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.util;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main2.ReinforcementCurve;

public class SigmoidCurve implements ReinforcementCurve
{
    private double x;
    private double y;
    
    private double a;
    private double c;
                
    public SigmoidCurve(double a, double c) 
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
