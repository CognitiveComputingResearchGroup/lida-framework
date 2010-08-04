/*
 * ExpectationCodelet.java
 *
 * Created on December 26, 2003, 3:28 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;


import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.Node;

public class ExpectationCodelet extends SidneyCodelet{        
    protected double performance;
    
    public ExpectationCodelet(){
        super();
    }
    public ExpectationCodelet(String name){
        super(name);
    }
    
    public void execute(NodeStructure e)
    {
        evaluate(e);
    }
    
    /**
     * Ryan says: Iterate throught the add list of this codelets behavior.
     * If a proposition in the add list matches the current state then increment this codelets performance
     * else decrement it.
     * 
     * @param e
     */
    protected void evaluate(NodeStructure e){
        performance = 0; 
        for(Object proposition: behavior.getAddList()){
            if(e.hasNode((Node)proposition) || e.hasLink((Link) proposition))                        
                performance ++;                        
            else
                performance --;
        }        
    }                         
    
    public double getPerformance()
    {
        return performance;
    }    
}
