/*
 * NetEventListener.java
 *
 * Sidney D'Mello
 * Created on September 22, 2004, 5:33 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main2;

import java.util.*;

public interface NetEventListener extends EventListener
{       
    public void stateUpdate(NetEvent e);
    public void goalUpdate(NetEvent e);
    
    public void cycleStart(NetEvent e);
    public void cycleEnd(NetEvent e);    
}
