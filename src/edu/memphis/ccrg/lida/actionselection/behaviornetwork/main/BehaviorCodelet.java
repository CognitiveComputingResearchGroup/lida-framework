/*
 * BehaviorCodelet.java
 *
 * Created on December 26, 2003, 3:25 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public class BehaviorCodelet extends SidneyCodelet{
	
	
    public BehaviorCodelet(){
        super();
        setType(SidneyCodelet.BEHAVIOR);
    }
    public BehaviorCodelet(String name){
        super(name, SidneyCodelet.BEHAVIOR);
    }
    
    public void execute(NodeStructure ns) {
    }

}
