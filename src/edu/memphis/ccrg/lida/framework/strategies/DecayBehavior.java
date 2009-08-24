package edu.memphis.ccrg.lida.framework.strategies;

public interface DecayBehavior {

    /**
     * Decays the current activation according to some internal decay function.
     * @param currentActivation The activation of the entity before decay.
     * 
     */
    public double decay(double activation);
        
    /**
     * Calculates the value of x for a given y.
     * @param y     The value of the ordinate.
     * @return      The value of x for which the curve value is y.
     */
    public double calcX(double y);
    
    /**
     * Calculates the value of y for a given x.
     * @param x     The value of the abscissa.
     * @return      The value of the curve at x.
     */
    public double calcY(double x);
}
