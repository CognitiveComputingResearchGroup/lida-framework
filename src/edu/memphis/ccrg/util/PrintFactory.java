/*
 * Printer.java
 *
 * Created on July 28, 2005, 1:21 PM
 */

package edu.memphis.ccrg.util;

import java.text.DecimalFormat;
import java.util.*;

public class PrintFactory 
{
    public final static String SEP = " ";
    public final static String REG = "\\s";
    public final static String BRK = "\n";
    
    private static DecimalFormat format = new DecimalFormat("0.000");
    
    public static void print(double data[][])
    {
        System.out.println(format(data));
    }    
    
    public static void print(double data[])
    {
        System.out.println(format(data));
    }
    
    public static void print(int data[][])
    {
        System.out.println(format(data));
    }    
    
    public static void print(int data[])
    {
        System.out.println(format(data));
    }
    
   public static void print(String data[][])
    {
        System.out.println(format(data));
    }    
    
    public static void print(String data[])
    {
        System.out.println(format(data));
    }
    
    public static String format(double data[][])
    {
        if(data == null)
            return "";
        
        String fmt = "";
        
        for(int i = 0; i < data.length; i++)
        {            
            fmt += format(data[i]);
        }
        return fmt;
    } 
    
    public static String format(double data[])
    {
        if(data == null)
            return "";
                
        String fmt = "";
        for(int i = 0;  i < data.length; i++)
        {
            fmt += format.format(data[i]) + SEP;
        }
        fmt += BRK;
        
        return fmt;
    }
    
    public static String format(int data[][])
    {
        if(data == null)
            return "";
        
        String fmt = "";
        
        for(int i = 0; i < data.length; i++)
        {            
            fmt += format(data[i]);
        }
        return fmt;
    } 
    
    public static String format(int data[])
    {
        if(data == null)
            return "";
        
        String fmt = "";
        for(int i = 0;  i < data.length; i++)
        {
            fmt += format.format(data[i]) + SEP;
        }
        fmt += BRK;
        
        return fmt;
    }
    
    public static String format(String data[][])
    {
        if(data == null)
            return "";
        
        String fmt = "";
        
        for(int i = 0; i < data.length; i++)
        {            
            fmt += format(data[i]);
        }
        return fmt;
    } 
    
    public static String format(String data[])
    {
        if(data == null)
            return "";
        
        String fmt = "";
        for(int i = 0;  i < data.length; i++)
        {
            fmt += data[i] + SEP;
        }
        fmt += BRK;
        
        return fmt;
    }
    
    public static String format(Object row_lbl[], Object col_lbl[], double data[][])
    {
        String str = "";
        
        int LMT = 5;
                
        str += align(str, LMT) + " ";
        
        for(int j = 0; j < col_lbl.length; j++)        
            str += align(col_lbl[j].toString(), LMT) + " ";
        
        str += "\n";
                                
        for(int i = 0; i < row_lbl.length; i++)
        {
            str += align(row_lbl[i].toString(), LMT) + " ";
            for(int j = 0; j < col_lbl.length; j++)
            {
                str += align(String.valueOf(data[i][j]), LMT) + " ";
            }
            str += "\n";
        }
        return str;
    }
    
    private static String align(String s, int l)
    {        
        if(s.length() > l)
            return s.substring(0, l);
        else
        {
            String b = s;
            for(int i = 0; i < (l - s.length()); i++)
                b += " ";
            return b;
        }
    }
    
    public static String format(Collection data)
    {
        String str = null;
        
        if(data != null)
        {
            str = "";
            for(Iterator i = data.iterator(); i.hasNext();)
                str += i.next().toString();
        }
        return str;
    }
}
