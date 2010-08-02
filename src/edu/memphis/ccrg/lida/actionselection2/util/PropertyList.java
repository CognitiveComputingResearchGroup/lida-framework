/*
 * PropertyList.java
 *
 * Sidney D'Mello (original code by Lee McCauley)
 * Created on July 31, 2003, 12:23 PM
 */

package edu.memphis.ccrg.lida.actionselection2.util;

import java.util.LinkedList;
import java.util.Iterator;

public class PropertyList extends LinkedList
{      
    public PropertyList() 
    {
        super();
    }
  
    public void setValue(String name, Object value) 
    {
        Property property = getProperty(name);
        if (property == null) 
        {
            property = new Property(name, value);
            add(property);
        }
        else
            property.setValue(value);
    }
   
    public Object getValue(String name) 
    {
        Object value = null;
      
        Property property = getProperty(name);
        if (property != null)
            value = property.getValue();
    
        return value;
    }
  
    public String getValueClass(String name) 
    {
        String valueClass = null;
        
        Property property = getProperty(name);
        if(property != null)
            valueClass = property.getValueClass();
        
        return valueClass;
    }
  
    public Property getProperty(String name) throws NullPointerException
    {
        Property property = null;
        
        if(name != null)
        {         
            Iterator iterator = this.iterator();
            while(iterator.hasNext() && property == null)
            {
                Property current = (Property)iterator.next();
                if(current.getName().compareTo(name) == 0)
                    property = current;
            }
        }
        else
            throw new NullPointerException();
        
        return property;
    }
}
