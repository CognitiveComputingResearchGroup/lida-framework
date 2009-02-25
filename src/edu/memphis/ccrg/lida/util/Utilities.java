/*
 * Utilities.java
 *
 * Created on July 3, 2005, 11:52 AM
 */

package edu.memphis.ccrg.lida.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 *
 * @author scott
 */
public class Utilities
{
    
    /** Creates a new instance of Utilities */
    public Utilities()
    {
    }
    
    public static String getStackTrace( Throwable aThrowable )
    {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter( result );
        aThrowable.printStackTrace( printWriter );
        return result.toString();
    }
}
