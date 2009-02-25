/*
 * Percept.java
 *
 * Sidney D'Mello (sdmello@memphis.edu)
 * Created on March 4, 2006, 4:01 PM
 *
 */
package edu.memphis.ccrg.lida.perception;

import java.util.*;

public class Percept extends LinkedList<Node>{       
	
	final static long serialVersionUID = 0; 
    
    public Percept(){
        super();        
    }      
    
    public Percept(Percept p){
    	super(p);
    }
    
    public void print(){
    	int size = this.size();
    	for(int i = 0; i < size; i++){
    		Node temp = this.get(i);
    		System.out.println(temp.toString());
    	}
    	System.out.println();
    }
    
}//class Percept
