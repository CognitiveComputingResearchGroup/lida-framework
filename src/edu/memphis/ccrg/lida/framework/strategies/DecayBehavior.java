package edu.memphis.ccrg.lida.framework.strategies;

public interface DecayBehavior {

    /**
     * Decays the current activation according to some internal decay function.
     * @param currentActivation The activation of the entity before decay.
     * @param ticks The number of ticks to decay.
     * 
     */
    public double decay(double currentActivation,long ticks);
        
}
