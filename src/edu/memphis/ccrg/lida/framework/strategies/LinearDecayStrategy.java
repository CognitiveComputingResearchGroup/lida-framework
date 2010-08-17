package edu.memphis.ccrg.lida.framework.strategies;

import java.util.Map;


public class LinearDecayStrategy implements DecayStrategy{

	    
	    /** The default slope (m = 1). */
	    public static final double DEFAULT_M = 0.01;
	    
	    /** The default intercept (b = 0). */
	    public static final double DEFAULT_B = 0.0;
	    
	    /** The slope of this linear curve. */
	    private double m;
	    
	    /** The y intercept of this linear curve. */
	    private double b;

		@SuppressWarnings("unused")
		private Map<String, ? extends Object> params;
	    
	    /** Creates a new instance of LinearCurve. Values for slope and intercept
	     * are set to the default ones.
	     */
	    public LinearDecayStrategy() {
	        m = DEFAULT_M;
	        b = DEFAULT_B;
	    }
	    
	    /**
	     * Creates a new instance of LinearCurve, with specific values for the slope
	     * and the intercept.
	     * @param m     The value of the slope.
	     * @param b     The value of the intercept.
	     */
	    public LinearDecayStrategy(double m, double b) {
	        this.m = m;
	        this.b = b;
	    }
	    
	    public double decay(double activ, long ticks){
	    	
	    	activ= activ - (m*ticks+b);
	    	return (activ>0.0)?activ:0.0;
	    }	    

		public void init(Map<String, ? extends Object> params) {
			this.params=params;
		}
}