package edu.memphis.ccrg.lida.framework.strategies;


public class LinearDecayBehavior implements DecayBehavior{

	    
	    /** The default slope (m = 1). */
	    public static final double DEFAULT_M = 0.6;
	    
	    /** The default intercept (b = 0). */
	    public static final double DEFAULT_B = 0.0d;
	    
	    /** The slope of this linear curve. */
	    protected double m;
	    
	    /** The y intercept of this linear curve. */
	    protected double b;
	    
	    /** Creates a new instance of LinearCurve. Values for slope and intercept
	     * are set to the default ones.
	     */
	    public LinearDecayBehavior() {
	        m = DEFAULT_M;
	        b = DEFAULT_B;
	    }
	    
	    /**
	     * Creates a new instance of LinearCurve, with specific values for the slope
	     * and the intercept.
	     * @param m     The value of the slope.
	     * @param b     The value of the intercept.
	     */
	    public LinearDecayBehavior(double m, double b) {
	        this.m = m;
	        this.b = b;
	    }
	    
	    public double decay(double activ){
	    	return m*activ + b;
	    }
	    
	    /**
	     * Calculates the value of x for a given y.
	     * @param y     The value of the ordinate.
	     * @return      The value of x for which the curve value is y.
	     */
	    public double calcX(double y) {
	        return (y - b) / m;
	    }
	    
	    /**
	     * Calculates the value of y for a given x.
	     * @param x     The value of the abscissa.
	     * @return      The value of the curve at x.
	     */
	    public double calcY(double x) {
	        return m * x + b;
	    }
	    
	    /**
	     * Gets the value of the slope.
	     * @return      The value of the slope for this curve.
	     */
	    public double getM() {
	        return m;
	    }
	    
	    /**
	     * Gets the value of the intercept.
	     * @return      The value of the intercept for this curve.
	     */
	    public double getB() {
	        return b;
	    }
	}