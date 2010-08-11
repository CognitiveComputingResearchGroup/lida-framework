/*
 * Codelet.java
 *
 * Sidney D'Mello
 * Created on December 18, 2003, 6:57 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public abstract class SidneyCodelet{
    
    protected String name = "SidneyCodelet";
    protected BehaviorImpl behavior = null;
    
 //   protected Map<String, String> properties = new HashMap<String, String>();    
    
    public SidneyCodelet(){
    }
    
    public SidneyCodelet(String name){
    	this.name = name;
    }
    
    public abstract void execute(NodeStructure ns);
    
    public BehaviorImpl getBehavior()
    {
        return behavior;
    }
    
    public final String getName()
    {
        return name;
    }
//    
//    public final Map<String, String> getProperties(){
//        return properties;
//    }
    
    public void setName(String name){
    	this.name = name;
  //      properties = new HashMap<String, String>();   
    }
//
//    public final void setProperties(Map<String, String> properties){                      
//    	this.properties = properties;
//    }
    
    public final void setBehavior(BehaviorImpl behavior){
    	this.behavior = behavior;
    }
    
//    public void addProperty(String name, String value){
//        properties.put(name, value);
//    }
//    
//    public Object getProperty(Object name)
//    {
//        return properties.get(name);
//    }
    
    public String toString()
    {
        return name;
    }

}
