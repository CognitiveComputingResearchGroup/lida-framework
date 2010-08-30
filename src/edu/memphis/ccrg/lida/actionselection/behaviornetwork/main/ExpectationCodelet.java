package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import edu.memphis.ccrg.lida.attention.AttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

//From old code: Iterate through the add list of an expecation codelet's associated behavior.
//If a proposition in the add list matches the current state 
//then increment this codelets performance else decrement it.
public class ExpectationCodelet extends AttentionCodeletImpl{
	
	private NodeStructure currentState = new NodeStructureImpl();
	
	private double performance = 0.0;
	
	private Behavior behavior;
	
	public ExpectationCodelet(Behavior b){
		this.behavior = b;
	}
	
	public void setCurrentState(NodeStructure currentState) {
		this.currentState = currentState;
	}

	@Override
	protected void runThisLidaTask() {
		evaluate();
	}

    protected void evaluate(){
        performance = 0; 
        for(Object proposition: behavior.getAddList()){
            if(currentState.hasNode((Node)proposition) || currentState.hasLink((Link) proposition))                        
                performance ++;                        
            else
                performance --;
        }        
    }                         
    
    public double getPerformance(){
        return performance;
    }   

}