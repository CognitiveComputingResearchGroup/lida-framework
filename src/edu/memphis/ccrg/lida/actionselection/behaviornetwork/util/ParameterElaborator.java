package edu.memphis.ccrg.lida.actionselection.behaviornetwork.util;

import java.text.*;

public class ParameterElaborator 
{     
    public final static double INCREMENT = 0.1;
    
    private double parameters;
    private double increment;
    
    private int elaborated;
    
    private double[] current;
    
    public ParameterElaborator(int parameters)
    {
        increment = INCREMENT;
        
        this.parameters = parameters;
        this.current = new double[parameters];
    }   
    
    public ParameterElaborator(int parameters, double increment)
    {
      if(increment != 0)        
            this.increment = increment;
        else
        {
            increment = INCREMENT;
            throw new IllegalArgumentException("ERROR : ZERO INCREMENT");
        }
        this.parameters = parameters;
        this.current= new double[parameters]; 
    }    
    
    public boolean hasNext()
    {
        return elaborated < elaborations();
    }
    
    public double[] next()
    {
        int number = elaborated;
        boolean flag = true;
        int i = (int)parameters - 1;
 
        while(flag == true && i >=0)
        {
            current[i]=(current[i] + increment);
            if (current[i] > 1.0)
            {
                current[i] = 0.0;
                flag = true;
            }
            else
                flag = false;
            i--;
        }
        elaborated = elaborated + 1;

        return current;
    }
     
    
    public int elaborated()
    {
        return elaborated;
    }
    
    public int elaborations()
    {
        return(int) Math.pow((1 / increment) , parameters);
    }
 
    public void reset()
    {
        elaborated = 0;
    }
    
    public static void main(String args[])
    {
        ParameterElaborator pe = new ParameterElaborator(5, 0.5);
        System.out.println(pe.elaborations());

        DecimalFormat format = new DecimalFormat("0.00");                
        while (pe.hasNext())
        {
            double[] val = pe.next();
   
            for(int i = 0; i < val.length; i++)
                System.out.print(format.format(val[i]) + "\t");
            System.out.println();
        }
    }
}


