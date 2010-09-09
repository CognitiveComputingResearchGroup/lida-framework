/*
 * BehaviorStream.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 6:11 PM
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.*;

/**
 * Stream is a list of behaviors that may be "instantiated"
 *  Sidney D'Mello, Ryan McCall
 */
public class StreamImpl implements Stream{
	
	//TODO how can this support a stream which repeats behaviors?
	
    private String name = "blank stream";
    
    private List<Behavior> behaviors = new ArrayList<Behavior>();
    
   // private Map<Behavior, List<Behavior>> successorLinks = new HashMap<Behavior, List<Behavior>>();   
    
    private boolean instantiated = false;
    
    private long id;
    
    public StreamImpl(String name, long id){
        this.name = name;              
    }
    public StreamImpl(long id){
    	this("no name", id);
    }
    
    public String getName(){
        return name;
    }
    public String toString(){
        return name;
    }
    
    public void instantiate(){
        instantiated = true;
    }
    public void uninstantiate(){
        instantiated = false;
    }
    public boolean isInstantiated(){
        return instantiated;
    } 
    
    public boolean addBehavior(Behavior behavior){
    	return behaviors.add(behavior);
    }    
    public Collection<Behavior> getBehaviors(){
        return Collections.unmodifiableCollection(behaviors);
    }

	public int getBehaviorCount(){
		return behaviors.size();
	}
	
	public void removeBehavior(Behavior behavior) {
		behaviors.remove(behavior);
	}
	
	public long getId(){
		return id;
	}
	public int size() {
		return behaviors.size();
	}
	
//	public void addSuccessorLink(long predecessorId, long successorId){
//		List<Long> values = successorLinks.get(predecessorId);
//		if(values == null){
//			values = new ArrayList<Long>();
//			successorLinks.put(predecessorId, values);
//		}
//		values.add(successorId);
//	}
//	
//	public List<Behavior> getSuccessors(long id){
//		List<Behavior> successors = new ArrayList<Behavior>();
//		List<Long> successorsIds = successorLinks.get(id);
//		for(Long l: successorsIds)
//	}
//	
}//class
