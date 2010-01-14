package samples;
import java.io.IOException;

public class SdmStartup extends Thread 
{
    public static Recog sdm= new Recog();
    
    boolean gui = false;

    private static boolean sdmIsInitialized = false;

    public SdmStartup(boolean inputBoolean)
    {
	gui = inputBoolean;
	start();
    }

    public void run() {
	try {
	  Recog.initSdm(gui); // from file  // was from scratch
        } catch (IOException e) {
          System.out.println("Sdm:SdmStartup: Sdm initialization failed.");
        }
	
	// Let the Focus know that sdm is initialized.
	System.out.println("Sdm:SdmStartup: Sdm initialization is completed Successfully");
	sdmIsInitialized = true;

    }

    public static boolean getSdmIsInitialized() {
	return sdmIsInitialized;
    }

    public static void setSdmIsInitialized(boolean inputBoolean) {
	//	sdmIsInitialized = inputBoolean;
    }
        
    /*
    public class SdmRW
    {
        public boolean myconvergence=false;
        int written_count=0;
        
        public boolean read(String[] perceptionRegisters) throws IOException, InterruptedException
        {
            myconvergence=Recog.readSdm(perceptionRegisters); 
            MemoryReadReturn.setConvergence(myconvergence);
            return(myconvergence);
        }
        public void write(String[] perceptionRegisters) throws IOException, InterruptedException
        {
            written_count=Recog.writeSdm(perceptionRegisters); 
            //return(written_count);
        }
        public String[] getLatestSdmData() throws IOException, InterruptedException
        {
            return(MemoryReadReturn.output); 
        }
    } //end SdmRW
    */
    
    //    public static void mainSdmStartup() throws IOException, InterruptedException
    //    {
	//        boolean gui=false;
	//        if(args[0]=="true")
	//  {
	//     gui=true;
	//  }
    //        SdmPackage.Recog.initSdm(gui); // from file
    //        while (true)
    //       {
    //       sleep(1000);
            //yield();
    //     }
    //    }  end mainSdmStartup
} // end class  SdmStartup
