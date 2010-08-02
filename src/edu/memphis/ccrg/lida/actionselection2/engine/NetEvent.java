/*
 * BehaviorNetEvent.java
 *
 * Sidney D'Mello
 * Created on September 22, 2004, 5:05 PM
 */

package edu.memphis.ccrg.lida.actionselection2.engine;

import java.util.*;

public class NetEvent extends EventObject 
{
    private Collection status;
    
    public NetEvent(Object source) 
    {
        super(source);
    }
    
    public Collection getStatus()
    {
        return status;
    }
    
    public void setStatus(Collection status)
    {
        this.status = status;;
    }
    
}
