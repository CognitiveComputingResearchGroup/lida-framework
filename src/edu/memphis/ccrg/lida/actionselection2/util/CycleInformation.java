/*
 * CycleInformation.java
 *
 * Sidney D'Mello
 * Created on May 19, 2004, 8:01 PM
 */

package edu.memphis.ccrg.lida.actionselection2.util;


public class CycleInformation implements Cloneable
{   
    private int cycle;
    
    private String winner;    
    double firingEnergy;
    
    public CycleInformation()
    {
        
    }
    
    public CycleInformation(int cycle) 
    {
        this.cycle = cycle;
    }
    
    public Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }
        
    public void setWinner(String winnerName)
    {
        this.winner = winnerName;
    }
    
        
    public void setFiringEnergy(double firingEnergy)
    {
        this.firingEnergy = firingEnergy;
    }    
    
    public int getCycle()
    {
        return cycle;
    }
    
    public String getWinner()
    {
        return winner;
    }
    
    public double getFiringEnergy()
    {
        return firingEnergy;
    }
    
    public String toString()
    {
        return winner;
    }
}
