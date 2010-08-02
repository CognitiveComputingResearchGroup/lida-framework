/*
 * FileRepresentation.java
 *
 * Sidney D'Mello
 * Created on November 22, 2004, 3:41 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.util;

import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

public class FileRepresentation 
{        
    public final static String TOKEN = ",";
    public final static String D_FORMAT = "0.000";
    
    private DecimalFormat formatter = new DecimalFormat(D_FORMAT);    
    
    public FileRepresentation() 
    {
    }
        
    public Collection represent(String inFileName) throws IOException, NullPointerException
    {
        LinkedList buffer = new LinkedList();
                        
        if(inFileName != null)
        {
            BufferedReader reader = new BufferedReader(new FileReader(inFileName));
            String line = null;
            
            while((line = reader.readLine()) != null)
                buffer.add(line.trim());
            
            reader.close();                      
        }
        else
            throw new NullPointerException();
        
        return buffer;
    }
    
    public double[][] representAsArray(String inFileName) throws IOException, NullPointerException, NumberFormatException
    {
        double outgoing[][] = null;
        
        Vector incoming = new Vector(represent(inFileName));
        
        outgoing = new double[incoming.size()][];
        for(int i = 0; i < outgoing.length; i++)
            outgoing[i] = stringToArray((String)incoming.get(i));
        
        return outgoing;
    }
    
    public void represent(Collection collection, String outFileName) throws IOException
    {
        if(collection != null && outFileName != null)
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outFileName));
            for(Iterator i = collection.iterator(); i.hasNext();)
            {
                writer.write(i.next().toString());
                writer.newLine();
            }
            writer.close();
        }
        else
            throw new NullPointerException();
    }
    
    public Collection representFromArray(double[][] buffer) throws IOException
    {
        if(buffer != null)
        {                        
            Vector collection = new Vector(buffer.length);
            for(int i = 0; i < buffer.length; i++)                        
            {
                if(buffer[i] != null)
                    collection.add(arrayToString(buffer[i]));            
            }                
            return collection;
        }
        else
            throw new NullPointerException();
    } 
    
    public void represent(double[][] buffer, String outFileName) throws IOException
    {
        represent(representFromArray(buffer), outFileName);
    }
    
    private double[] stringToArray(String str) throws NumberFormatException
    {
        double[] buffer = null;
        
        StringTokenizer tokenizer = new StringTokenizer(str, TOKEN);
        buffer = new double[tokenizer.countTokens()];
                
        for(int i = 0; i < buffer.length; i++)
            buffer[i] = Double.parseDouble(tokenizer.nextToken().trim());
        
        return buffer;
    }
    
    private String arrayToString(double[] arr)
    {
        String line = "";
        for(int i = 0; i < arr.length - 1; i++)
            line += formatter.format(arr[i]) + TOKEN;
        line += formatter.format(arr[arr.length - 1]);        
        
        return line;
    }
}
