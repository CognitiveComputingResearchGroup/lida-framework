/*
 * PerceptionStub.java
 *
 * Created on November 9, 2006, 1:13 PM
 *
 * Sidney D'Mello
 * The University of Memphis
 * sdmello@memphis.edu
 */

package edu.memphis.ccrg.lida.actionselection2.exp.sensorial;

import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;

import edu.memphis.ccrg.lida.actionselection2.util.WOFSelector;

public class PerceptionStub
{
    private String states[];
    private double weights[];
    
    private Hashtable<String, Boolean> currentState;        
        
    public PerceptionStub(Collection<String> events)
    {
        initialize(events);        
    }
    
    private void initialize(Collection<String> events)
    {
        this.currentState = new Hashtable();
        this.states = new String[events.size()];
        this.weights = new double[events.size()];
        
        int si = 0;
        for(String event:events)
        {
            this.currentState.put(event, new Boolean(false));
            states[si] = event;
            si ++;
        }
        Arrays.sort(states);
        Arrays.fill(this.weights, 1/(double)this.states.length);        
    }
    
    public void bias(String state, double amount)
    {
        double relative_bias = (1/(double)this.states.length) + amount;
        for(int i = 0; i < states.length; i++)
        {
            if(states[i].equals(state))
                weights[i] = relative_bias;
            else
                weights[i] = (1-relative_bias)/(states.length - 1);
        }
    }    
    
    public void perceive()
    {
        this.currentState.clear();
        
        WOFSelector selector = new WOFSelector(this.weights);
        int perceived[] = selector.selectRN();
        for(int i = 0; i < perceived.length; i++)
            this.currentState.put(this.states[perceived[i]], Boolean.TRUE);
    }
}
