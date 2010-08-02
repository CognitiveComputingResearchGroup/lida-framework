/*
 * Property.java
 *
 * Sidney D'Mello (original code by Lee McCauley)
 * Created on July 31, 2003, 12:29 PM
 */

package edu.memphis.ccrg.lida.actionselection2.util;

public class Property
{  
    private String name;
    private Object value;
    
    public Property() 
    {
        name = null;
        value = null;
    }
  
    public Property(String name, Object value)
    {
        setName(name);
        setValue(value);
    }
  
    public String getName() 
    {
        return name;
    }
    
    public Object getValue() 
    {
        return value;
    }
  
    public void setName(String name) throws NullPointerException
    {
        if(name != null)
            this.name = name;
        else
            throw new NullPointerException();
    }
      
    public void setValue(Object value) throws NullPointerException
    {
        if(value != null)
            this.value = value;
        else
            throw new NullPointerException();
    }
  
    public String getValueClass() 
    {
        return value.getClass().getName();
    }
}
