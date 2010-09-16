/**
 * 
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * 
 */
public class StreamImpl implements Stream{
	
	//TODO should nodes without any connections to other nodes be allowed?
	private static Logger logger = Logger.getLogger("lida.actionselection.behaviornetwork.main");
	
    private String name = "blank stream";
    
    private long id;
    
    private Set<Behavior> behaviors;
    
    private Map<Behavior, Set<Behavior>> successorLinks;   

    public StreamImpl(String name, long id){
        this.name = name;    
        this.id = id;
        behaviors = new HashSet<Behavior>();
        successorLinks = new HashMap<Behavior, Set<Behavior>>();
    }
    public StreamImpl(long id){
    	this("no name", id);
    }
    
    public boolean addBehavior(Behavior behavior){
    	return behaviors.add(behavior);
    }
	public void removeBehavior(Behavior behavior) {
		behaviors.remove(behavior);
	}
    public Collection<Behavior> getBehaviors(){
        return Collections.unmodifiableCollection(behaviors);
    }
	public int getBehaviorCount(){
		return behaviors.size();
	}
	
	public long getId(){
		return id;
	}
    public String getName(){
        return name;
    }
	
	public void addSuccessorLink(Behavior predecessor, Behavior successor, Node commonNode){
		if(predecessor.containsAddItem(commonNode) && successor.containsContextCondition(commonNode)){
			Set<Behavior> values = successorLinks.get(predecessor);
			if(values == null){
				values = new HashSet<Behavior>();
				successorLinks.put(predecessor, values);
			}
			values.add(successor);
		}else
			logger.log(Level.WARNING, "Tried to add a successor link but supplied nodes don't have that relationship", 
						LidaTaskManager.getActualTick());
	}
	
	public void removeSuccessorLink(Behavior predecessor, Behavior successor){
		if(successorLinks.containsKey(predecessor))
			successorLinks.get(predecessor).remove(successor);
	}
	
	public Set<Behavior> getSuccessors(Behavior behavior){
		return successorLinks.get(behavior);
	}
	
}//class
