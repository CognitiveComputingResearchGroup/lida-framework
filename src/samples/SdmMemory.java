package samples;
// Ashraf Anwar    2/16/97  CMattie */

// all strings passed are byte[]
import java.io.IOException;
import java.io.RandomAccessFile;

public class SdmMemory

/* to be instantiated for episodic memory(s) or word dictionary */

/*-----------------------------------------------------*/

{ //begin sdm class defn

  protected int dimension;
  protected int num_hardloc;
  protected int critical_distance;
  protected int access_radius;
  protected int cnv_distance;
  protected int max_iter=10;

  protected boolean address[][]=null;
  protected short bit_counter[][]=null;
  protected short sdm_wc[]=null;

  protected RandomAccessFile raf=null;
  //protected RandomAccessFile raf_bit_counter=null;
  //protected RandomAccessFile raf_address=null;

  public static String file_prfx="./";
  //"f:/Dissertation/SDM/recognit/JBuilder_sdm/";
/*-----------------------------------------------------*/
  //  constructor 1
    public SdmMemory(int dimen,int num_hrd, int critical_dist, int acc_rad,
              int cnv_dist, int max_it,String sdm_db_file_name, boolean from_file)
        throws IOException
    {
        dimension=dimen;
        num_hardloc=num_hrd;
        critical_distance=critical_dist;
        access_radius=acc_rad;
        cnv_distance=cnv_dist;
        max_iter=max_it;

        System.out.println("Sdm:SdmMemory: Please wait while init SDM : constructor 1");
        address= new boolean[num_hardloc][dimension];
        bit_counter= new short[num_hardloc][dimension];
        sdm_wc= new short[num_hardloc];
        double rr;
        if(from_file)
        {
			System.out.println("USE Memory Only\n");
          //disk_to_memory();
	  }
        //else
        {
         for (int i=0;i<num_hardloc;i++)
         {
            sdm_wc[i]=0; /* write count for that loc */
            for (int j=0;j<dimension;j++)
            {
              rr=Math.random();
	          boolean addrt=true;
              if (rr <0.5)
                addrt=false; /* binary address */
              else
                addrt=true; /* binary address */
              address[i][j]=addrt;
              bit_counter[i][j]=0;

	          //raf_address.writeBoolean(addrt);
              //raf_bit_counter.writeShort(0);//bit_counter[i][j]=0;
            } // end for inner
         } // end for outer
        }

       //}catch (IOException ioe){System.out.println("Sdm:SdmMemory: Cannot open sdm.db");}

    } // end sdm constructor  1

/*-----------------------------------------------------*/
  public int Hamming_DistanceSCue(boolean str1[], boolean str2[], int dstStartIndex, int dstEndIndex)
   {
	    // calculates hamming distance
	   int distance = 0;
	   for(int i=dstStartIndex; i<dstEndIndex; i++)
	   {
	      if(str1[i] != str2[i])
	         distance++;
	   }
	   return distance;
   } // end hamming_distance
/*-----------------------------------------------------*/
  public int Hamming_Distance(boolean str1[], boolean str2[])
   {
	    // calculates hamming distance
	   int distance = 0;
	   for(int i=0; i<dimension; i++)
	   {
	      if(str1[i] != str2[i])
	         distance++;		  
	   }
	   return distance;
   } // end hamming_distance
/*-----------------------------------------------------*/
  public int write_locations(boolean newword[]) throws IOException
  {
    int numloc=0;
    int hamd=0;
    for (int i=0;i<num_hardloc;i++)
    {
    	//System.out.println("Sdm:SdmMemory: Checking write at loc "+i);
        //raf_address.seek(i*dimension);
        //for(int ii=0;i<dimension;i++)
	    //    address[ii]=raf_address.readBoolean();

	    hamd=Hamming_Distance(address[i],newword);
	    //System.out.println("Sdm:SdmMemory: Hamming dist = "+hamd);
        if(hamd<=access_radius)
    	{
	     for(int j=0; j<dimension; j++)
         {
	       //raf_bit_counter.seek((i*dimension+j)*2);
	       //short si=raf_bit_counter.readShort();
            if(newword[j])  // if 1
                    //si++;
                bit_counter[i][j]++;
            else
                    //si--;
                bit_counter[i][j]--;

	    //raf_bit_counter.seek((i*dimension+j)*2);
	    //raf_bit_counter.writeShort(si);

                    /* 1 ==> add to counter
                       0 ==> subtract from counter */
         } // end inner for
         sdm_wc[i]++;
	     numloc=numloc+1;
	    } // end if within access circle
    } // end for
	return numloc;
  }  // end write_locations
/*-----------------------------------------------------*/
  public void restoreSCue(int wk_word[], boolean restored_wk_word[], int dstStartIndex, int dstEndIndex)
  {
       for(int i=dstStartIndex;i<dstEndIndex;i++)
	   {
	         if(wk_word[i]<0)   // if negative counter aggregate ==> o
		        restored_wk_word[i]=false;
	         else               // if positive counter aggregate ==> 1
		        restored_wk_word[i]=true;
	   }

  }  // end restoreSCue
/*-----------------------------------------------------*/
  public void restore(int wk_word[], boolean restored_wk_word[])
  {
       for(int i=0;i<dimension;i++)
	   {
	         if(wk_word[i]<0)   // if negative counter aggregate ==> o
		        restored_wk_word[i]=false;
	         else               // if positive counter aggregate ==> 1
		        restored_wk_word[i]=true;
	   }

  }  // end restore
/*-----------------------------------------------------*/
  public int read_locationsSCue(boolean objectword[], int wk_word[], int srcStartIndex, int srcEndIndex, int dstStartIndex, int dstEndIndex) throws IOException
  {
    int numloc = 0;
    for (int j=0; j<this.dimension; j++)
	     wk_word[j] = 0;  // init counter to zeros
    for(int i=0; i<num_hardloc; i++)
	{
        //raf_address.seek(i*dimension);
        //for(int ii=0;i<dimension;i++)
	    //   	address[ii]=raf_address.readBoolean();
        if(Hamming_DistanceSCue(address[i], objectword, dstStartIndex, dstEndIndex) <= access_radius)
		{
          for(int j=0; j<dimension; j++)
		  {
		    //raf_bit_counter.seek((i*dimension+j)*2);
		    //short si=raf_bit_counter.readShort();
            wk_word[j] = wk_word[j] + bit_counter[i][j]; //si;

            // NO NEED to update : just don't care
		  }
            // bitcounter  +ve or 0 or -ve
          numloc++;  /* number for cotributing locs in reading */

        } // end if
	}
	return numloc;
  } // end read_locationsSCue
/*-----------------------------------------------------*/
  public int read_locations(boolean objectword[], int wk_word[]) throws IOException
  {
    int numloc = 0;
    for (int j=0; j<dimension; j++)
	     wk_word[j] = 0;  // init counter to zeros
    for(int i=0; i<num_hardloc; i++)
	{
        //raf_address.seek(i*dimension);
        //for(int ii=0;i<dimension;i++)
	    //   	address[ii]=raf_address.readBoolean();
        if(Hamming_Distance(address[i], objectword) <= access_radius)
		{
          for(int j=0; j<dimension; j++)
		  {
		    //raf_bit_counter.seek((i*dimension+j)*2);
		    //short si=raf_bit_counter.readShort();
            wk_word[j] = wk_word[j] + bit_counter[i][j]; //si;
		  }
            // bitcounter  +ve or 0 or -ve
          numloc++;  /* number for cotributing locs in reading */

        } // end if
	}
	return numloc;
  } // end read_locations
/*-----------------------------------------------------*/
  public boolean read_sdmSCue(boolean objectword[],boolean restored_wk_word[], int srcStartIndex, int srcEndIndex, int dstStartIndex, int dstEndIndex) throws IOException
  {
    // objectword is the i/p cue for recognition/associative memory
    // boolean restored_wk_word[] is the o/p restore word if CONVERGENCE

    int wk_word[]= new int[dimension];
    // byte restored_wk_word[]= new byte[dimension];
    // begin read_sdm code
	int cntrcntr=0;
	boolean cnv=false;
	boolean read_done=false;
	int iter=0;
	int i,j=0;
	try {
	    cntrcntr = read_locationsSCue(objectword, wk_word, srcStartIndex, srcEndIndex, dstStartIndex, dstEndIndex);
	    // put correct word in wk_word in integer (0 + -)
	    //System.out.println("Sdm:SdmMemory:read_sdmSCue: after 1st read_locations");

	if (cntrcntr >0)  // at least 1 loc was in access circle
	{
	  // at the entry of the loop, wk_word has the int counter values
	  while (!cnv && iter<max_iter)
	  {
		iter++;
		restoreSCue(wk_word,restored_wk_word, dstStartIndex, dstEndIndex);
		//System.out.println("Sdm:SdmMemory:read_sdmSCue: after restore");
		if (Hamming_DistanceSCue(objectword,restored_wk_word, dstStartIndex, dstEndIndex)>critical_distance)
		{
		    System.out.println("Sdm:SdmMemory:read_sdmSCue: after Hamming_Distance");
		    break; // DIVERGE
		}
		else
        {
		    read_done=true;
	        if (Hamming_DistanceSCue(objectword,restored_wk_word, dstStartIndex, dstEndIndex)<cnv_distance)
	        {
		      cnv=true;
		      break; //exit loop
		    }
		}
        cntrcntr = read_locationsSCue(restored_wk_word, wk_word, srcStartIndex, srcEndIndex, dstStartIndex, dstEndIndex);
        // READ AGAIN : loop while < max_iter and NO Divergence
        //System.out.println("Sdm:SdmMemory:read_sdmSCue: after loop  read_locations");

        if (cntrcntr==0)
            break; // exit NOW with previous results

	  } // end while

   } // end outer if
   else
   {
    for(int boo=0;boo<dimension;boo++)
        	    restored_wk_word[boo]=false;
   }
	} catch (NullPointerException SdmMemoryExcept) {
	  System.out.println("Sdm:SdmMemory:read_sdmSCue: Nullpointerexception in SdmMemory's " +
			     "read_sdm().");
	}

     return cnv;
  } /* end read_sdmSCue */
/*-----------------------------------------------------*/
  public boolean read_sdm(boolean objectword[],boolean restored_wk_word[]) throws IOException
  {
    // objectword is the i/p cue for recognition/associative memory
    // boolean restored_wk_word[] is the o/p restore word if CONVERGENCE

    int wk_word[]= new int[dimension];
    // byte restored_wk_word[]= new byte[dimension];
    // begin read_sdm code
	int cntrcntr=0;
	boolean cnv=false;
	boolean read_done=false;
	int iter=0;
	int i,j=0;
	try {
	cntrcntr = read_locations(objectword, wk_word);
	    // put correct word in wk_word in integer (0 + -)
	    System.out.println("Sdm:SdmMemory:read_sdm: after 1st read_locations");

	if (cntrcntr >0)  // at least 1 loc was in access circle
	{
	  // at the entry of the loop, wk_word has the int counter values
	  while (!cnv && iter<max_iter)
	  {
		iter++;
		restore(wk_word,restored_wk_word);
		System.out.println("Sdm:SdmMemory:read_sdm: after restore");
		if (Hamming_Distance(objectword,restored_wk_word)>critical_distance)
		{
		    System.out.println("Sdm:SdmMemory:read_sdm: after Hamming_Distance");
		    break; // DIVERGE
		}
		else
        {
		    read_done=true;
	        if (Hamming_Distance(objectword,restored_wk_word)<cnv_distance)
	        {
		      cnv=true;
		      break; //exit loop
		    }
		}
        cntrcntr = read_locations(restored_wk_word, wk_word);
        // READ AGAIN : loop while < max_iter and NO Divergence
        System.out.println("Sdm:SdmMemory:read_sdm: after loop  read_locations");

        if (cntrcntr==0)
            break; // exit NOW with previous results

	  } // end while

	  /*for(int boo=0;boo<dimension;boo++)
        	    {if(restored_wk_word[boo])
        	        System.out.print(1);
        	     else System.out.print(0);
        	    }
      */
   } // end outer if
   else
   {
    for(int boo=0;boo<dimension;boo++)
        	    restored_wk_word[boo]=false;
   }
	} catch (NullPointerException SdmMemoryExcept) {
	  System.out.println("Sdm:SdmMemory:read_sdm - Nullpointerexception in SdmMemory's " +
			     "read_sdm().");
	}
     return cnv;

  } /* end read_sdm */
/*-----------------------------------------------------*/
  public int memory_to_disk() throws IOException
  {
    System.out.println("Sdm:SdmMemory:memory_to_disk: Wait while dump to disk");
    //open , write, close raf
    raf=new RandomAccessFile(file_prfx+"sdm.db","rw");
    raf.seek(0);
    // put everything in one single file
    for (int i=0;i<num_hardloc;i++)
    {
        for (int j=0;j<dimension;j++)
        {
              raf.writeBoolean(address[i][j]);
              raf.writeShort(bit_counter[i][j]);
        }
        raf.writeShort(sdm_wc[i]); /* write count for that loc */
    } // end for outer
    raf.close();
    return(0);
  } //end memory_to_disk
/*-----------------------------------------------------*/
  public int disk_to_memory() throws IOException
  {
    raf= new RandomAccessFile(file_prfx+"sdm.db","r");
    System.out.println("Sdm:SdmMemory:disk_to_memory: Wait while read from disk");
    //open , read, close raf

    raf.seek(0);
    for (int i=0;i<num_hardloc;i++)
    {
        //try{sdm_wc[i]=raf.readShort(); /* write count for that loc */
        for (int j=0;j<dimension;j++)
        {
              address[i][j]=raf.readBoolean() ;
              bit_counter[i][j]=raf.readShort() ;
        }
        sdm_wc[i]=raf.readShort(); /* write count for that loc */
    } // end for outer
    raf.close();
    return(0);
  } //end disk_to_memory
/*-----------------------------------------------------*/
  public int clean_up()  throws IOException
  {
    //memory_to_disk(); // dump to disk last image

    //try{  // close sdm.db
	//  raf.close();
	//raf_bit_counter.close();
	//raf_address.close();
    //}catch (IOException ioe){System.out.println("Sdm:SdmMemory:clean_up: Cannot close sdm.db");}

    return(0);
  } // end clean_up
/*-----------------------------------------------------*/
}  // end class sdm
/*-----------------------------------------------------*/

