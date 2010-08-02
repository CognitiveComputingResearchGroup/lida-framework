/*
 * ReinforcementCurve.java
 *
 * Created on July 21, 2004, 3:17 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main2;

public interface ReinforcementCurve 
{    
    public double getXIntercept(double y) throws ArithmeticException;
    public double getYIntercept(double x) throws ArithmeticException;        
}
