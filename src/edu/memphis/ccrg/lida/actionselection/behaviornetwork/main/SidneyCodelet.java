/*
 * Codelet.java
 *
 * Sidney D'Mello
 * Created on December 18, 2003, 6:57 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;
import java.util.*;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public abstract class SidneyCodelet{
    
    protected String name = "SidneyCodelet";
    protected Behavior behavior = null;
    
    protected Map<Object, Object> properties = new HashMap<Object, Object>();    
    
    public SidneyCodelet(){
    }
    
    public SidneyCodelet(String name){
    	this.name = name;
    }
    
    public abstract void execute(NodeStructure ns);
    
    public Behavior getBehavior()
    {
        return behavior;
    }
    
    public final String getName()
    {
        return name;
    }
    
    public final Map<Object, Object> getProperties(){
        return properties;
    }
    
    public void setName(String name){
    	this.name = name;
        properties = new HashMap<Object, Object>();   
    }

    public final void setProperties(Map<Object, Object> properties){                      
    	this.properties = properties;
    }
    
    public final void setBehavior(Behavior behavior){
    	this.behavior = behavior;
    }
    
    public void addProperty(Object name, Object value){
        properties.put(name, value);
    }
    
    public Object getProperty(Object name)
    {
        return properties.get(name);
    }
    
    public String toString()
    {
        return name;
    }

}
