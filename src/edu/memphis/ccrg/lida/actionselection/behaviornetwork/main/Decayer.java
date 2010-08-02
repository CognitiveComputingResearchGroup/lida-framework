/*
 * Decayer.java
 *
 * Sidney D'Mello
 * Created on January 7, 2004, 7:08 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.*;

public class Decayer 
{    
    private LinkedList streams;
    
    public Decayer(LinkedList streams) throws NullPointerException
    {
        if(streams != null)
        {
            this.streams = streams;
        }
        else
            throw new NullPointerException();        
    }
    
    public void decay() 
    {

    }    
}
