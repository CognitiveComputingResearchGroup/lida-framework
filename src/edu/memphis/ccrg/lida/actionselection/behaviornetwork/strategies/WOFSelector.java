/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/*
 * WOFSelector.java
 *
 * Created on November 9, 2006, 1:32 PM
 *
 * Sidney D'Mello
 * The University of Memphis
 * sdmello@memphis.edu
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import java.util.Arrays;
import java.util.Random;

public class WOFSelector
{
    private double weights[];
    private double min[];
    private double max[];
    
    public WOFSelector(double weights[])
    {
        this.weights = weights;

        min = new double[weights.length];
        max = new double[weights.length];
        
        min[0] = 0;
        max[0] = weights[0];        
        for(int i = 1; i < weights.length; i++)
        {
            min[i] = max[i-1];
            max[i] = min[i] + weights[i];                    
        }
                
        /*
        for(int i = 0; i < weights.length; i++)
            System.out.println(i + "\t" + min[i] + "\t" + max[i]);                
         **/
    }
    
    public int[] selectRN()
    {
        Random random = new Random(System.currentTimeMillis());
        
        int N = 0;
        while(N == 0)        
            N = random.nextInt(this.weights.length);
        
        return selectN(N);
    }
    
    public int[] selectN(int n)
    {
        int s[] = new int[n];        
        Arrays.fill(s, -1);
        
        for(int i = 0; i < n; i++)
        {
            int index = select();
            if(index != -1)
            {
                boolean found = false;
                for(int j = 0; j < i; j++)
                {
                    if(s[j] == index)
                        found = true;
                }
                if(!found)
                {
                    s[i] = index;                                                         
                    //System.err.println(i + "\t" + index + "\t" + Arrays.toString(s));
                }
                else
                    i--;
            }
            else
                i--;
        }
        return s;
    }
    
    public int select()
    {
        int selected = -1;
                
        double rand = Math.random();        
        for(int i = 0; i < weights.length; i++)
        {
            if(rand >= min[i] && rand <= max[i])
                selected = i;
        }        
        return selected;
    }
    
    public static void main(String[] args)
    {
        double d[] = {0.2,0.16,0.16,0.16,0.16, 0.16};        
        
        double w[] = new double[d.length];
        
        WOFSelector s = new WOFSelector(d);
        for(int i = 0; i < 100; i++)
        {
            int c[] = s.selectRN();
            System.err.println(Arrays.toString(c));
                        
            for(int j = 0; j < c.length; j++)
                w[c[j]] ++;             
        }
        
        for(int i = 0; i < d.length; i++)
            System.err.println(i + "\t" + d[i] + "\t" + w[i]);
    }
    
}
