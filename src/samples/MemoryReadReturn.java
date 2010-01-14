package samples;



public class MemoryReadReturn
{
    public static boolean convergence=false;
    public static String[] output;

    
    
    public MemoryReadReturn()
    { //dummy constructor
        
    }
    
    
    
    
    public static boolean getConvergence()
    {
        return convergence;
    }
    
    public static void setConvergence(boolean inputBoolean)
    {
        convergence=inputBoolean;
    }
    
    public String[] getOutput()
    {
        return output;
    }
    
    public void setOutput(String[] inputArray)
    {
        output=inputArray;
    }
} // end class MemoryReadReturn
    
    