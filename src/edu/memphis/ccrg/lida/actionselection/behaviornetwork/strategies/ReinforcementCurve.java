/*
 * ReinforcementCurve.java
 *
 * Created on July 21, 2004, 3:17 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

public interface ReinforcementCurve{
	
    public double getXIntercept(double y);
    public double getYIntercept(double x);        
}
