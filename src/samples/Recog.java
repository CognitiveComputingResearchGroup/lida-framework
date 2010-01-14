// Ashraf Anwar  2001/03/05  11/03/99  03/29/99   5/12/1998   4/15/98  */
package samples;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

// Import the Focus item.
//import ConsciousToolkit.Consciousness.FocusPackage.MemoryReadReturn;

public class Recog
{
    public static int r_sizeSCue=3890; //82 %
    public static int h_locSCue=32000;  // SAME as ORIGINAL
    public static int crtcl_distSCue=0; //17%
    public static int acc_radSCue=0;   //  37%
    public static int cnv_distSCue=0;     // 16%

    public static int r_size=4736; // in bits;  was 4736
    public static int r_sizeInBytes=592;  // 379 char + 1000 NL outgoing msg + repetition
    public static int h_loc=120;							//was 4000 was 32000
    public static int crtcl_dist=1184; //21%?  1184
    public static int acc_rad=2250; //50%;    2368 ==> 47.5%
    public static int cnv_dist=1137; // 24%
    public static int max_iter=10;
    public static int maxInputRegisters = 100; // was 17
    public static int maxOutputRegisters = 100; // was 23
    public static int cue_max=03;
    public static int cue_max_all=06;
    public static int write_max=1;
    public static byte cue_start=0;
    public static int keyLength=4;
    public static String wrkstr=new String("");
    public static SdmMemory s = null;
    // instantiate  100 dim & 1000 Hloc
    // cannot be within main
    public static boolean cue[][]= new boolean[cue_max_all][r_size];
    public static boolean op_cue[]= new boolean[r_size];
    public static boolean op_cueSCue[]= new boolean[r_size];
    public static DataInputStream dis = new DataInputStream(System.in);
    public static String cue_strings[]=new String[cue_max_all];
    public static String cue_stringsSCue[]=new String[cue_max_all];
    public boolean myconvergence=false;
    public static int written_count=0;

	public static int maxFieldLength=50;
	public static String spacesString= new String();

	private static String[] dummy = {"FJ","JA","070101","67890","ashr","ashr","ashr","ashr",
      "ashr","ashr","ashr","ashr","ashr","ashr","ashr","ashr",
      "ashr", "ashr","ashr", "ashr", "ashr","ashr", "ashr"};


	public static MemoryReadReturn mrt = new MemoryReadReturn();

	private static PrintWriter logFile;

	public static int numberOfFields=128;
	public static int[] fieldStart={0, 1, 2, 3, 7, 8, 10, 19, 35, 39, 47, 67, 68, 69, 85, 89, 95, 99, 103, 107, 111, 115, 116,
									 121, 122, 125, 128, 131, 134, 137, 140, 143, 146, 149, 150, 158, 159, 175, 176, 179, 187,
									 195, 196, 199, 215, 220, 221, 224, 228, 232, 236, 237, 238, 241, 249, 269, 273, 274, 278,
									 282, 286, 287, 288, 289, 293, 297, 302, 306, 308, 309, 310, 311, 314, 330, 346, 350, 358, 359, 360, 366, 374,
									 382, 390, 392, 400, 408, 416, 417, 423, 431, 439, 447, 449, 457, 465, 473, 474, 475, 477, 479,
									 480, 482, 483, 485, 486, 490, 494, 498, 502, 506, 507, 511, 519, 527, 530, 533, 549, 565,
									 566, 568, 569, 585, 586, 587, 588, 589, 590, 591};
	public static int	lengthOfLastField = 1;


	/*
	first number: enumeration of fields, second: starting position from 0,
	third: an example, fourth: actual field length
	1 0 private String findMeAJobKey = "find me a job"; // 02/06/01
	1	1
	2 1 private String jobAcceptanceKey = "job acceptance"; // 02/06/01
	1	1
	3 2 private String nextRCKey = "next RC"; // 02/06/01
	0	1
	4 3 private String billetFromEmailKey = "billet number from email"; // 02/06/01
	1234	4  ?????
	5 7 private String numberOfJobFromEmailKey = "number of job from email"; // 02/06/01
	3	1
	6 8 private String systemCycleKey = "system cycle"; //01/07/01
	10	2

	private String systemEmailKey = "system Email"; //01/07/01
	messagem body (stored in database) ==> not included

	7 10 private String sailorSSNKey = "sailor's SSN";
	123456789	9
	8 19 private String sailorNameFromEmailKey = "sailor's name from email";
	John Smith	16
	9 35 private String sailorRateFromEmailKey = "sailor's rate from email";
	ASMC		4
	10 39 private String emailDateKey = "email's date";
	01012001	8
	11 47 private String sailorEmailAddressKey = "sailor's contact address";
	john_smith@navy.gov	20
	12 67 private String approachingPRDIdeaKey = "approaching PRD";
	1	1
	13 68 private String endOfEmailIdeaKey = "end of email";
	1	1
	14 69 private String sailorNameKey = "sailorName";
	John Smith	16
	15 85 private String sailorRateKey = "sailorRate";
	ASMC	4
	16 89 private String sailorPRDKey = "sailor PRD";
	072001	6
	17 95 private String sailorNEC1Key = "sailor NEC1";
	5783	4
	18 99 private String sailorNEC2Key = "sailor NEC2";
	5783	4
	19 103 private String sailorNEC3Key = "sailor NEC3";
	5783	4
	20 107 private String sailorNEC4Key = "sailor NEC4";
	5783	4
	21 111 private String sailorNEC5Key = "sailor NEC5";
	5783	4
	22 115 private String sailorSEASHOREKey = "sailorSeaShore";
	1	1  (1-6)
	23 116 private String sailorONBD_UICKey = "sailor ONBD_UIC";
	12345	5
	24 121 private String sailorDependentsKey = "sailor Dependents";
	2	1
	25 122 private String sailorSEALOC1Key = "sailor SEALOC1";
	ABC	3
	26 125 private String sailorSEALOC2Key = "sailor SEALOC2";
	ABC	3
	27 128 private String sailorSEALOC3Key = "sailor SEALOC3";
	ABC 3
	28 131 private String sailorSHORELOC1Key = "sailor SHORELOC1";
	ABC 3
	29 134 private String sailorSHORELOC2Key = "sailor SHORELOC2";
	ABC 3
	30 137 private String sailorSHORELOC3Key = "sailor SHORELOC3";
	ABC 3
	31 140 private String sailorOVERSEASLOC1Key = "sailor OVERSEASLOC1";
	ABC 3
	32 143 private String sailorOVERSEASLOC2Key = "sailor OVERSEASLOC2";
	ABC 3
	33 146 private String sailorOVERSEASLOC3Key = "sailor OVERSEASLOC3";
	ABC 3
	34 149 private String sailorSEXKey = "sailor SEX";
	1	M
	35 150 private String sailorEAOSKey = "sailor EAOS";
	01012002	8
	36 158 private String sailorPAYGRADEKey = "sailorPaygrade";
	3	1
	37 159 private String sailorONBD_ACTIVITY_NAMEKey = "sailor ONBD_Activity_Name";
	Radar Tech	16

	38 175 private String sailorLIMITED_DUTYKey = "sailor Limited Duty";
	1	1
	39 176 private String sailorGeographicLocationKey = "sailor Geographic Location";
	ABC	3

	 Job# specific Keys

Following do not need to be written to SDM:

	private String job1Key = "job1";
	hashatble
	private String job2Key = "job2";
	0	1
	private String job3Key = "job3";
	0	1
	private String job4Key = "job4";
	Radar Tech	16
	private String job5Key = "job5";
	Radar Tech	16
	private String job6Key = "job6";
	Radar Tech	16
	private String job7Key = "job7";
	Radar Tech	16
	private String job8Key = "job8";
	Radar Tech	16
	private String job9Key = "job9";
	Radar Tech	16
	private String job10Key = "job10";
	Radar Tech	16
	private String job11Key = "job11";
	Radar Tech	16
	private String job12Key = "job12";
	Radar Tech	16
	private String job13Key = "job13";
	Radar Tech	16
	private String job14Key = "job14";
	Radar Tech	16
	private String job15Key = "job15";
	Radar Tech	16
	private String job16Key = "job16";
	Radar Tech	16
	private String job17Key = "job17";
	Radar Tech	16
	private String job18Key = "job18";
	Radar Tech	16
	private String job19Key = "job19";
	Radar Tech	16
	private String job20Key = "job20";
	Radar Tech	16

			Job-field specific Keys

	40 179 private String jobReqnDateKey ="job-REQN-NBR-DATE";
	01012001	8
	41 187 private String jobTUMKey = "TUM";
	01012001	8
	42 195 private String jobPaygradeKey ="job-PAYGRADE";
	6	1
	43 196 private String jobPriorityKey ="Original Requisition Priority";
	101	3
	44 199 private String jobACTIVITY_NAMEKey="activityName";
	Radar Tech	16
	45 215 private String jobUICKey = "job-UIC";
	12345	5
	46 220 private String jobSEASHOREKey = "jobSeaShore";
	1	1
	47 221 private String jobATCKey = "ATC";
	ABC	3
	48 224 private String jobNEC1Key = "jobNEC1";
	2345	4
	49 228 private String jobNEC2Key = "job NEC2";
	2345	4

			Job Primary Keys

	50 232 private String jobAbbreviationNECKey = "jobAbbreviationNEC";
	1245	4
	51 236 private String jobMCAKey = "jobMCA";
	L	1
	52 237 private String jobSeaShoreCompositeKey = "jobSeaShoreComposite";
	1	1
	53 238 private String jobPriorityNumberKey = "jobPriorityNumber";
	102	3
	54 241 private String jobValueIndicatorKey = "jobValueIndicator";
	12345678	8
	55 249 private String incomingMessageTypeKey = "IncomingMessageType";
	Find me a Job	20


		 Linear Functional	 keys

	56 269 private String jobNECReutilizationMatchKey = "jobNECReutilizationMatch";          07/31/2000
	0.23	4
	57 273 private String jobDependentsMatchForOverseasKey = "jobDependentsMatchForOverseas";
	1 	1
	58 274 private String jobPriorityMatchKey = "jobPriorityMatch";
	0.23	4
	59 278 private String jobOrderCostMatchKey = "jobOrderCostMatch";
	0.23	4
	60 282 private String jobLocationPreferenceMatchKey = "jobLocationPreferenceMatch";
	0.23	4
    61 286 private String jobOVERSEASFlagKey = "jobOVERSEASFlag"; /* 07/31/2000
	1	1
    62 287 private String sailorDataRetrievedLFKey = "sailorDataRetrievedLF"; /* 07/31/2000
	1	1
    63 288 private String jobSEASHOREMatchKey = "jobSEASHOREMatch"; /* 07/31/2000
	1	1
	64 289 private String jobPaygradeMatchKey = "jobPaygradeMatch";
	0.23	4
	65 293 private String jobGeographicLocationMatchKey = "jobGeographicLocationMatch";
	0.23	4
    66 297 private String jobPCSCostKey = "jobPCSCost";
	12345	5
    67 302 private String jobFitnessKey = "jobFitness";
	0.23	4
    68 305 private String currentJobNumberKey = "Current Job Number LF";
	12	2
    69 308 private String exceptionalFamilyMemberKey = "Exceptional Family Member";  /* 02/05/01
	4	1
    70 309 private String flagForTrainingKey = "Flag For Training";  /* 02/05/01
	1	1
    71 310 private String mCAKey = "MCA"; /* 02/05/01
	L	1
    72 311 private String fleetBalanceKey = "Fleet Balance"; /* 02/05/01
	L10	3
    73 314 private String sailorLocationPreferenceKey = "Sailor Location Preference";  /* 02/05/01
	Norfolk	16
    74 330 private String sailorDutyPreferenceKey = "Sailor Duty Preference";  /* 02/05/01
	Sonar Tech	16
    75 346 private String sailorTrainingPreferenceKey = "Sailor Training Preference";  /* 02/05/01
	1234	4
    76 350 private String eAOSKey = "EAOS";  /* 02/05/01
	01012001	8

		Deliberation keys

	77 358 private String jobToBeOfferedFlagKey = "job to be offered";
	1	1
	78 359 private String jobScenarioCreationFlagKey = "Scenario Creation Flag";
	1	1
    79 360 private String jobDetachMonth1Key = "jobDetachMonth1";
	102000	6
    80 366 private String jobDetachTravelTime1Key = "job DetachMonth + travel time 1";
	01012000	8
    81 374 private String jobDetachTravelLeaveTime1Key = "job DetachMonth+travel time + LeaveTime1";
	01012001	8
    82 382 private String jobDetachTravelTimeSchool1Key = "job detach month + travel time to school1";
	01012001	8
    83 390 private String jobCurrentLeaveTime1Key = "current leave time to school1";
	30	2
    84 392 private String jobGradDateRemainingLeaveTime1Key = "graduation date + remaining leave time 1";
	10202001	8
    85 400 private String jobGradDateRemainingLeaveTravelTimeJob1Key = " graduation date + remaining leave time + travel time to job1";
	10202001	8
    86 408 private String jobEDA1Key = "jobEDA1";
	01012001	8
    87 416 private String jobGapOverlap1Key = "jobGapOverlap1";
	1	1
    88 417 private String jobDetachMonth2Key = "jobDetachMonth2";
	102001	6
   	89 423 private String jobDetachTravelTime2Key = "job DetachMonth + travel time 2";
	01012001	8
    90 431 private String jobDetachTravelLeaveTime2Key = "job DetachMonth+travel time + LeaveTime2";
	01012001	8
    91 439 private String jobDetachTravelTimeSchool2Key = "job detach month + travel time to school2";
	01012001	8
    92 447 private String jobCurrentLeaveTime2Key = "current leave time to school2";
	02	2
    93 449 private String jobGradDateRemainingLeaveTime2Key = "graduation date + remaining leave time 2";
	01012001	8
    94 457 private String jobGradDateRemainingLeaveTravelTimeJob2Key = " graduation date + remaining leave time + travel time to job2";
	01012001	8
    95 465 private String jobEDA2Key = "jobEDA2";
	01012001	8
    96 473 private String jobGapOverlap2Key = "jobGapOverlap2";
	1	1

    97 474 private String jobGapKey = "jobGapKey"; // this is to maintain the final gap.
	1	1
    98 475 private String jobTravelTimeKey = "jobTravelTime";
	10	2
	99 477 private String numberOfJobsKey = "number Of jobs";
	12	2
  	100 479 private String numberOfJobsOfferedKey = "numberOfJobsToOffer";
	3	1
  	101 480 private String numberOfScenariosCreatedKey = "number of Scenarios Created";
	12	2
  	102 482 private String currentGoalStructureFlagKey = "current Goal Structure Flag";
	1	1
  	103 483 private String currentJobNumberDeliberationKey = "Current Job Number in Deliberation";// 08/04/2000
	12	2
  	104 485 private String endOfDeliberationFlagKey = "end Of Deliberation";
	1	1


  		 Training Keys

	105 486 private String training1Key = "training1";
	1234	4
	106 490 private String training2Key = "training2";
	1234	4
	107 494 private String training3Key = "training3";
	1234	4
	108 498 private String training4Key = "training4";
	1234	4
	109 502 private String training5Key = "training5";
	1234	4
  	110 506 private String trainingFlagKey = "Training Flag";
	1	1
  	111 507 private String trainingNECKey = "Training NEC";
	1234	4
  	112 511 private String trainingStartDateKey = "TrainingStartDate";
	01012001	8
  	113 519 private String trainingEndDateKey = "Training End Date";
	01012001	8
  	114 527 private String trainingDurationKey = "Training Duration";
	100	3
  	115 530 private String trainingNumberOfSeatsKey = "Number Of Seats";   /*----> there is no data ion database
	100	3
  	116 533 private String trainingLocationKey = "Location";
	Norfolk	16
  	117 549 private String trainingNameKey = "Name";
	Radar Tech	16
  	118 565 private String numberOfTrainingsKey = "number Of trainings";
	7	1
  	119 566 private String currentTrainingNumberKey = " current training number";
	11	2


  		 Language Generation keys

	120 568 private String jobOfferedFlagKey = "jobOfferedFlag";
	1	1
    121 569 private String outgoingMessageTypeKey = "outgoingMessageType";
	Job Accept	16

	private String outgoingMessageKey = "outgoingMessage";
	??????? (stored in Database)   ==> not included

	122 585 private String salutationFlagKey = "salutationWritten";
	1	1
	123 586 private String introductionFlagKey = "introductionWritten";
	1	1
	124 587 private String jobDescrFlagKey = "jobDescrWritten";
	1	1
	125 588 private String jobOfferFlagKey = "jobOfferWritten";
	1	1
	126 589 private String closingFlagKey = "closingWritten";
	1	1
	127 590 private String bodyDoneFlagKey = "bodyDoneFlag";
	1	1
	128 591 private String noOfJobsWrittenKey = "numberOfJobsWritten";
	3	1

			LENGTH = 592 x 8 =     4736
	*/

    //-------------------------------------------------------------------


    public Recog()
    {		// dummy constructor
    		//$$ memoryReadReturn1.move(0,0);
    }

    //-------------------------------------------------------------------
    public static void main(String args[]) throws IOException
    {
		System.out.println("Hloc= "+ h_loc);
		spacesString="";
		for (int al=0;al<maxFieldLength;al++)
			spacesString += " ";
        initSdm(true);
        localWriteSdm();
        localReadSdm();
        s.clean_up();
        exitSdm();
    } // end main
    //-------------------------------------------------------------------
    public static void exitSdm() throws IOException
    {

        System.out.println("Sdm:Recog:exitSdm: Done!");
		logFile.close();
        System.exit(0);
        //String ttos=dis.readLine();
    }
    //-------------------------------------------------------------------
    public static void initSdmFromScratch(boolean inputBoolean) throws IOException
    {
        //Htbl.mainHtbl();
        String sdm_ss= new String("");
        sdm_ss="sdm.db"; //+r_size+".db";
        s=new SdmMemory(r_size,h_loc,crtcl_dist,acc_rad,cnv_dist,max_iter,sdm_ss,false);
        //s=new sdm(r_size,10000,sdm_ss,true);  // from File
        System.out.println("Sdm:Recog:initSdmFromScratch "+s.dimension+ " "+s.num_hardloc+" "+ s.critical_distance+" "+s.access_radius+" "+s.cnv_distance+" "+ s.max_iter);
        //s.clean_up();

        //System.exit(0);
    } // end initSdmFromScratch
    //-------------------------------------------------------------------
    public static void initSdm(boolean gui) throws IOException
    {
        //Htbl.mainHtbl(); // put keys in hash
		try {
			logFile = new PrintWriter(new FileOutputStream("./SdmPackage/sdmLog.txt"));
			System.out.println("Log File Created");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
//        if(gui)
//        { // starts the beanbox GUI
//            Sdm sdmGui=null;
//	    //           sdmGui=new Sdm();
//        }
        String sdm_ss=new String("");
        sdm_ss="sdm.db"; //+r_size+".db";
        // file_prfx DEFINED in SdmMemory

        s=new SdmMemory(r_size,h_loc,crtcl_dist,acc_rad,cnv_dist,max_iter,sdm_ss,true);
        //s=new sdm(r_size,10000,sdm_ss,true);  // from File
        System.out.println("Sdm:Recog:initSdm:"+s.dimension+ " "+s.num_hardloc+" "+ s.critical_distance+" "+s.access_radius+" "+s.cnv_distance+" "+ s.max_iter);
        //s.clean_up();

    //System.exit(0);

        for(int i=0;i<cue_max_all;i++)
        {
            op_cue[i]=false;
            for(int j=0;j<r_size;j++)
            {
                //cue[i][j]=(byte)Math.abs((Math.IEEEremainder((i+j),2.0))); //0-1
                cue[i][j]=false;
            }
        }
        //System.out.println("Sdm:Recog:initSdm: end initSdm");
    } // end initSdm

    //-------------------------------------------------------------------
    /*
	public static String prToHash(String perceptionRegisters) throws IOException
    {
        int i=0;
        String cnctntd="";

	  while (i < perceptionRegisters.length)
	  {
 	   if (perceptionRegisters != null)
	   {  // add keys for all PRs

	    if(Htbl.myHtbl.contains(perceptionRegisters[i]))
		{ // if value found in HTBL
                    Enumeration hte = Htbl.myHtbl.elements();
                    Enumeration htk = Htbl.myHtbl.keys();

                    System.out.println("Sdm:Recog:prToHash: hashtable size is: " +
                                        Htbl.myHtbl.size());
                    System.out.println("Sdm:Recog:prToHash: hashtable elements " +
                                       "are: " + Htbl.myHtbl.toString());

            boolean loopFlag=false;

		    while(htk.hasMoreElements())
		    {  // loop for key-value pair match
		      //System.out.println("Sdm:Recog:prToHash: searching for "+ perceptionRegisters[i]);
		      Object key=htk.nextElement();
		      Object value=hte.nextElement();
		      //System.out.println("Sdm:Recog:prToHash:"+(String)value);
		      if(perceptionRegisters[i].equals((String)value))
			  {
			    cnctntd=cnctntd+(String)key;
			    loopFlag=true;
			    break; // exit while hash key not found
			  }
		    } // end while
		    if(! loopFlag) // if not found in
		    {
		        System.out.println("Sdm:Recog:prToHash: Should NOT REACH HERE!!! check CODE");
		        cnctntd=cnctntd+"NOKY"; // avoid NullPointerExcep
		    }
		}
		else  // htbl does not contain value
		{
		    cnctntd=cnctntd+"NOKY";
		}
	  } // end if
	  else // null perceptionRegister
	  {
	    cnctntd=cnctntd+"NOKY";
	  }
	  //System.out.println("Sdm:Recog:prToHash: i is: " + i+" String = "+cnctntd);
	  i++; // NEXT PR
	 } // end while
        return(cnctntd);
    } // end prToHash
	*/
    //-------------------------------------------------------------------
    public static MemoryReadReturn read(String[] perceptionRegisters) //throws IOException, InterruptedException
    // was String[]
    {
		String sss=new String();
		sss="";

		// write perception registers to logFile
		logFile.print("Read with");
		logFile.print("\t");
		for (int i = 0; i < perceptionRegisters.length; i++)
		{
			logFile.print(perceptionRegisters[i]);
			logFile.print("\t");
		}
		logFile.println();
		logFile.flush();

        mrt.setConvergence(false);
        mrt.setOutput(dummy);

      //System.out.println("Sdm:Recog:read: with " + perceptionRegisters.length + " perception registers: ");

	  //System.out.print("Sdm:Recog:read: "+perceptionRegisters[testCounter] + " ");
      //System.out.println();
		boolean contFlag = true;
		for (int j = 0; j < perceptionRegisters.length; j++)
		{
			if (perceptionRegisters[j] == null)
				contFlag = false;
				break;
		}

		//Arpad

		if (contFlag) {
	    try {
	        try{
				mrt.setConvergence(readSdm(perceptionRegisters));
	    } catch (IOException e) {
	      System.out.println("Sdm:Recog:read: IOException!");}
	    } catch(NullPointerException npe){
	      System.out.println("Sdm:Recog:read: NullPointerException!");}

	    System.out.println("Sdm:Recog:read: memory read return's convergence to " +
			       mrt.getConvergence());
		}
		else
		{
			System.err.println("SDM sees a null!!!!!");
		}
        return(mrt);
    } //end read
    //-------------------------------------------------------------------
    public static boolean readSdm(String[] perceptionRegisters) throws IOException
    {
        boolean cnvrgd=false;
        String sss=new String();
        int indx=0;
        String cnctntd=new String();

		cnctntd = "";
		sss="";

    	try
    	{
            //System.out.println("Sdm:Recog:readSdm: before convertCueForward, "+perceptionRegisters[0]);
            //cnctntd=prToHash(perceptionRegisters);

			for (int testCounter = 0; testCounter<perceptionRegisters.length;testCounter++)
			{
				if (perceptionRegisters[testCounter] != null)
				{
					cnctntd=cnctntd+perceptionRegisters[testCounter];
					int calcLen =0;
					if(testCounter<(perceptionRegisters.length -1))
						calcLen=(fieldStart[testCounter+1]-fieldStart[testCounter]);
					else
						calcLen=lengthOfLastField;

				}
				else
				{ // if PR was null , we fill it with Spaces
					if(testCounter<(perceptionRegisters.length -1))
						cnctntd=cnctntd+spacesString.substring(0,(fieldStart[testCounter+1]));
					else
					{
						cnctntd=cnctntd+spacesString.substring(0,lengthOfLastField);
					}
				}
			}

            // convert PR to the corresponding hash keys using myHtbl
            // then convert forward
            //System.out.println("Sdm:Recog:readSdm: before convertCueForward, "+cnctntd );
            if(cnctntd.length()>0)  // or  .equals
            { // convert concat of hashkeys to binary
                convertCueForward(cnctntd,cue[0]); // String to Boolean
                    // if cnctntd has only percept w/o emotion or BN, don't care at end of cue[0]

        	    //System.out.println("Sdm:Recog:readSdm: Reading from SDM with "+perceptionRegisters[0]);
        	    //System.out.println("Sdm:Recog:readSdm: Reading from SDM with "+cnctntd);

		        for(int boo=0;boo<r_size;boo++)
        	        op_cue[boo]=true;

                cnvrgd=s.read_sdm(cue[0],op_cue);
        	    System.out.println("\nSdm:Recog:readSdm: read result is: "+cnvrgd);
                if(cnvrgd)
	            {
	                //System.out.println("SDM:Recog:readSdm: bfr convertCueBackward");
                    sss=convertCueBackward(op_cue); // Boolean to String
                    //System.out.println("SDM:Recog:readSdm: ftr convertCueBackward");
                    System.out.println("Sdm:Recog:readSdm: o/p:"+ sss); // sss is concat of keys only
                    // Now, convert the concatenated keys string to corresponding values
					while(indx<numberOfFields)  //pos<sss.length())
                    {
                        String keyString=new String("");
						if (indx<numberOfFields-1)
							keyString=sss.substring(fieldStart[indx], fieldStart[indx+1]);
						else
							keyString=sss.substring(fieldStart[indx], (fieldStart[indx]+lengthOfLastField));

                        /*
						String valueString=keyString; //(String)Htbl.myHtbl.get(keyString);
                        if(valueString==null) // if key not FOUND
                        {
                            valueString=keyString;
                            // explicitly put the key in writing
                        }*/

                        //int indx=(int)((float)pos/(float)keyLength);
                        //System.out.println("Sdm:Recog:readSdm: "+indx+" "+keyString);
                        mrt.output[indx]=keyString;
						indx++;
                        //pos=pos+keyLength;
					} // edn while

					// print returned data to logFile
					logFile.print("Read return");
					logFile.print("\t");
					for (int i = 0; i < mrt.output.length; i++)
					{
						logFile.print(mrt.output[i]);
						logFile.print("\t");
					}
					logFile.println();

	            }
	            else // if not cnvrgd
	            {
					logFile.println("Read diverged - no return value");
	                System.out.println("Sdm:Recog:readSdm: Diverge");
	                //mrt.output[0]="DIVERGENT";
	            }
	        }
            else // if cnctntd
            {
                cnvrgd=false;
            }
            mrt.setConvergence(cnvrgd);
        }catch (NullPointerException readException)
	    {System.out.println("Sdm:Recog:readSdm: readSdm() had null pointer exception.");
	    }

        return(cnvrgd);
    } // end readSdm
    //-------------------------------------------------------------------
    public static String[] getLatestSdmData() // throws IOException, InterruptedException
    {
            return(mrt.output);
    } // end getLatestSdmData
    //-------------------------------------------------------------------

    public static void write(String[] perceptionRegisters) // throws IOException, InterruptedException
    {
	    // add new Prs to Htbl
        int i=0;
        int startIndex=0;
        String sss=new String("");
		/*
	    while (i<perceptionRegisters.length)
	    { // add all new values one by one to Htbl
	      if(perceptionRegisters[i]!= null)
          { // if there is something to add

			if(! Htbl.myHtbl.contains(perceptionRegisters[i]))
            { // if value not already there ==> add new value
                while(Htbl.myHtbl.put(perceptionRegisters[i].substring(startIndex,startIndex+4),perceptionRegisters[i])!= null)
                {  // loop for a new key substring while already exist
                    startIndex++;
                }
                // when we reach here. put has taken place and new value added
                //System.out.println("Sdm:Recog:write: "+perceptionRegisters[i].substring(startIndex,startIndex+4));

            } // end if
          } // end if
          i++; // NEXT PR
	    } // end while
		*/
	    // CALL the WRITE method

		// print written data to logFile
		boolean contFlag = true;
		logFile.print("Write to");
		logFile.print("\t");
		for (int j = 0; j < perceptionRegisters.length; j++)
		{
			if (perceptionRegisters[j] == null) contFlag = false;
			logFile.print(perceptionRegisters[j]);
			logFile.print("\t");
		}
		logFile.println();
		logFile.flush();

		if(contFlag)
		{
			try{
			written_count=writeSdm(perceptionRegisters);
			}
			catch (IOException f){}
			catch(NullPointerException npe){System.out.println("Sdm:Recog:write: NULL POINTER");};
		}  // end if
		else
		{
			System.err.println("SDM sees a null!!!!!");
		}


            //return(written_count);
    } // end write
    //-------------------------------------------------------------------

    public static int writeSdm(String[] perceptionRegisters) throws IOException
    {
        int loc_written=0;
        int i=0;
        String cnctntd=new String("");
        //cnctntd=prToHash(perceptionRegisters);
		for (int testCounter = 0; testCounter < perceptionRegisters.length;testCounter++)
				cnctntd=cnctntd+perceptionRegisters[testCounter];
		//cnctntd=perceptionRegisters;
        convertCueForward(cnctntd,cue[0]);
        // convert PR to the corresponding hash keys using myHtbl
        // then convert forward
        System.out.println("Sdm:Recog:writeSdm: Please wait while writing in SDM "+cnctntd);
        for (int write_count=0;write_count<write_max;write_count++)
        {
                try{
                    loc_written=s.write_locations(cue[0]);
	                System.out.println("Sdm:Recog:writeSdm: "+loc_written+"  locations were written");
	            }catch(IOException ioe){System.out.println("Sdm:Recog:writeSdm: Cannot write to SDM");}
        } // end for
        return(loc_written);
    } // end writeSdm
    //-------------------------------------------------------------------
    public static boolean localReadSdmSCue2Levels() throws IOException
    {
        String sss=new String("");
        boolean rslt=true;
        int i,j;

        /* 65% = 60 out of the 92 chars */
        cue_stringsSCue[0]="1001234310123456789JOAN SMITH      ASMC01012001SMITH@NAVY.MIL      11JOAN SMITH      ASMC072001123423453456456756781123451ABCDEFGHIJKLMNOPQRSTUVWXYZAF010120021AVIATION TECHNIC1ABC01012001010120011001AVIATION TECHNIG456784ABC123456781234L100112345678FIND ME A JOB       0.2310.240.250.260110.270.28100000.500901AL10NORFOLK         AVIATION TECHNIJ1234010120021101200201012002010520010108200130011120010115200101182001010200101212001012420010127200102020120010204200102072001011013110112112342345345645675678112340301200103042001100020NORFOLK         AVIATION TECH   1111JOB ACCEPTANCE  1111111";
        cue_stringsSCue[1]="0105678311234567891MIKEY KELMORE   AS1 01022001KELMO@NAVY.MIL      00MIKEY KELMORE   AS1 082001135724683579468057912234562BCDEFGHIJKLMNOPQRSTUVWXYZABM010220022AVIATION TECHNID0BCD01022001010220012011AVIATION TECHNIH567895BCD234567892345A201123456789JOB DENIAL          0.3410.350.360.371000.380.39200000.601010LL11NORFOLK         AVIATION TECHNIK2345010220020102200201012003010620010109200131011320010116200101192001111200101222001012520010128200103020220010205200102082001101114211113012352346345745685679112350302200103052001101021JACKSONVILLE    BVIATION TECH   2120JOB DENIAL      1010102";
        cue_stringsSCue[2]="0019031312345678912DAVID DEMORE    AS2 01032001DEMOR@NAVY.MIL      00DAVID DEMORE    AS2 092001147025813692470358143345670CDEFGHIJKLMNOPQRSTUVWXYZABCM010320023AVIATION TECHNIE0CDE01032001010320013021AVIATION TECHNIG678906CDE345678903456B302134567890JOB ACCEPTANCE      0.4510.460.470.480010.490.50300000.701120BL12NORFOLK         AVIATION TECHNIL3456010320020023200201012004010720010110200132011420010117200101202001212200101232001012620010129200104020320010206200102092001201215312014112362347345845695670012360303200103062001102022SAN DIEGO       CVIATION TECH   4130JOB OFFER       0000003";
        cue_stringsSCue[3]="1001234310123456789                ASMC01012001SMITH@NAVY.MIL      11JOAN SMITH      ASMC072001123423453456456756781123451ABCDEFGHIJKLMNOPQRSTUVWXYZAF010120021AVIATION TECHNIC1ABC01012001010120011001AVIATION TECHNIG456784ABC123456781234L100112345678FIND ME A JOB       0.2310.240.250.260110.270.28100000.500901AL10NORFOLK         AVIATION TECHNIJ1234010120021101200201012002010520010108200130011120010115200101182001010200101212001012420010127200102020120010204200102072001011013110112112342345345645675678112340301200103042001100020NORFOLK         AVIATION TECH   1111JOB ACCEPTANCE  1111111";
		cue_stringsSCue[4]="0105678311234567891                AS1 01022001KELMO@NAVY.MIL      00MIKEY KELMORE   AS1 082001135724683579468057912234562BCDEFGHIJKLMNOPQRSTUVWXYZABM010220022AVIATION TECHNID0BCD01022001010220012011AVIATION TECHNIH567895BCD234567892345A201123456789JOB DENIAL          0.3410.350.360.371000.380.39200000.601010LL11NORFOLK         AVIATION TECHNIK2345010220020102200201012003010620010109200131011320010116200101192001111200101222001012520010128200103020220010205200102082001101114211113012352346345745685679112350302200103052001101021JACKSONVILLE    BVIATION TECH   2120JOB DENIAL      1010102";
        cue_stringsSCue[5]="0019031312345678912                AS2 01032001DEMOR@NAVY.MIL      00DAVID DEMORE    AS2 092001147025813692470358143345670CDEFGHIJKLMNOPQRSTUVWXYZABCM010320023AVIATION TECHNIE0CDE01032001010320013021AVIATION TECHNIG678906CDE345678903456B302134567890JOB ACCEPTANCE      0.4510.460.470.480010.490.50300000.701120BL12NORFOLK         AVIATION TECHNIL3456010320020023200201012004010720010110200132011420010117200101202001212200101232001012620010129200104020320010206200102092001201215312014112362347345845695670012360303200103062001102022SAN DIEGO       CVIATION TECH   4130JOB OFFER       0000003";

        System.out.println("\nTest 2 Levels");
        System.out.println("=============\n");

        for(int ii=0;ii<cue_max;ii++)
        {
         convertCueForward(cue_stringsSCue[ii],cue[ii]);
        }

        for(i=0;i<cue_max;i++)
        {
            try{
        	    System.out.println("Sdm:Recog:localReadSdmSCue: Reading from SDM with "+cue_stringsSCue[i]);
        	    if (i==0)
        	    {
                    rslt=s.read_sdmSCue(cue[i],op_cueSCue,0,480, 0,600); // cue[] now contains SCue
                    if (rslt) // 2nd level read
                    {
                        System.out.println("First Level Read Successful, getting into 2nd Level\n");
                        rslt=s.read_sdmSCue(op_cueSCue,op_cue,0,600, 0,736); // cue[] now contains SCue
                    }

                }
                else
                if (i==1)
                {
                    rslt=s.read_sdmSCue(cue[i],op_cue,256,736, 136,736);
                    if (rslt) // 2nd level read
                    {
                        System.out.println("First Level Read Successful, getting into 2nd Level\n");
                        rslt=s.read_sdmSCue(op_cueSCue,op_cue,136,736, 0,736); // cue[] now contains SCue
                    }
                }
	        }catch(IOException ioe){;}
            //sss=convertCueBackward(cue[i]);
            System.out.println("Sdm:Recog:localReadSdmSCue: i/p:"+cue_stringsSCue[i]);
        	if(rslt)
	        {
              sss=convertCueBackward(op_cue);
              System.out.println("Sdm:Recog:localReadSdmSCue: o/p:"+ sss+"\n");
              for(j=0; j<r_sizeInBytes; j=j+4)
              {
//                System.out.print(Htbl.myHtbl.get(cue_stringsSCue[i].substring(j,j+4))+" => ");
//                System.out.print(Htbl.myHtbl.get(sss.substring(j,j+4))+" \n");
              }
	        }
	        else
	        	  System.out.println("Sdm:Recog:localReadSdmSCue: Diverge on last level");
        } // end for
        return(rslt);
    } // end localReadSdmSCue2Levels
    //-------------------------------------------------------------------
    public static boolean localReadSdmSCue3Levels() throws IOException
    {
        String sss=new String("");
        boolean rslt=true;
        int i,j;

        /* 65% = 60 out of the 92 chars  480 bits
           65% = 39         312 bits */

        System.out.println("\nTest 3 Levels");
        System.out.println("=============\n");

        for(int ii=0;ii<cue_max;ii++)
        {
         convertCueForward(cue_stringsSCue[ii],cue[ii]);
        }

        for(i=0;i<cue_max;i++)
        {
            try{
        	    System.out.println("Sdm:Recog:localReadSdmSCue: Reading from SDM with "+cue_stringsSCue[i]);
        	    if (i==0)
        	    {
        	        System.out.println("First Cue \n");
                    rslt=s.read_sdmSCue(cue[i],op_cueSCue,0,312, 0,480); // cue[] now contains SCue
                    if(rslt)
                    {
                      System.out.println("First Level Read Successful, getting into 2nd Level\n");
                      rslt=s.read_sdmSCue(op_cueSCue,op_cue,0, 480, 0,600); // cue[] now contains SCue

                      if (rslt) // 3rd level read
                      {
                        System.out.println("Second Level Read Successful, getting into 3rd Level\n");
                        rslt=s.read_sdmSCue(op_cueSCue,op_cue,0,600, 0,736); // cue[] now contains SCue
                      }
                    }
                }
                else
                if (i==1)
                {
                    System.out.println("Second Cue \n");
                    rslt=s.read_sdmSCue(cue[i],op_cue,424,736,256,736);
                    if (rslt) // 3rd level read
                    {
                      System.out.println("First Level Read Successful, getting into 2nd Level\n");
                      rslt=s.read_sdmSCue(op_cueSCue,op_cue,256,736, 136,736); // cue[] now contains SCue
                      if (rslt) // 3rd level read
                      {
                        System.out.println("Second Level Read Successful, getting into 3rd Level\n");
                        rslt=s.read_sdmSCue(op_cueSCue,op_cue,136,736, 0,736); // cue[] now contains SCue
                      }
                    }
                }
	        }catch(IOException ioe){;}
            //sss=convertCueBackward(cue[i]);
            System.out.println("Sdm:Recog:localReadSdmSCue: i/p:"+cue_stringsSCue[i]);
        	if(rslt)
	        {
              sss=convertCueBackward(op_cue);
              System.out.println("Sdm:Recog:localReadSdmSCue: o/p:"+ sss+"\n");
              for(j=0; j<r_sizeInBytes; j=j+4)
              {
//                System.out.print(Htbl.myHtbl.get(cue_stringsSCue[i].substring(j,j+4))+" => ");
//                System.out.print(Htbl.myHtbl.get(sss.substring(j,j+4))+" \n");
              }
	        }
	        else
	        	  System.out.println("Sdm:Recog:localReadSdmSCue: Diverge on last level");
        } // end for
        return(rslt);
    } // end localReadSdmSCue3Levels
    //-------------------------------------------------------------------
    public static boolean localReadSdmSCue4Levels() throws IOException
    {
        String sss=new String("");
        boolean rslt=true;
        int i,j;

        /* 65% = 60 out of the 92 chars  480 bits
           65% = 39         312 bits
           65% = 25         200 bits*/


        System.out.println("\nTest 4 Levels");
        System.out.println("=============\n");

        for(int ii=0;ii<cue_max;ii++)
        {
         convertCueForward(cue_stringsSCue[ii],cue[ii]);
        }

        for(i=0;i<cue_max;i++)
        {
            try{
        	    System.out.println("Sdm:Recog:localReadSdmSCue: Reading from SDM with "+cue_stringsSCue[i]);
        	    if (i==0)
        	    {
        	       System.out.println("First Cue \n");
        	       rslt=s.read_sdmSCue(cue[i],op_cueSCue,0,200, 0,312); // cue[] now contains SCue
        	       if(rslt)
        	       {
        	        System.out.println("First Level Read Successful, getting into 2nd Level\n");
                    rslt=s.read_sdmSCue(cue[i],op_cueSCue,0,312, 0,480); // cue[] now contains SCue
                    if(rslt)
                    {
                      System.out.println("Second Level Read Successful, getting into 3rd Level\n");
                      rslt=s.read_sdmSCue(op_cueSCue,op_cue,0, 480, 0,600); // cue[] now contains SCue

                      if (rslt) // 3rd level read
                      {
                        System.out.println("Third Level Read Successful, getting into 4th Level\n");
                        rslt=s.read_sdmSCue(op_cueSCue,op_cue,0,600, 0,736); // cue[] now contains SCue
                      }
                    }
                   }
                }
                else
                if (i==1)
                {
                   System.out.println("Second Cue \n");
                   rslt=s.read_sdmSCue(cue[i],op_cue,536,736,424,736);
                   if(rslt)
                   {
                    System.out.println("First Level Read Successful, getting into 2nd Level\n");
                    rslt=s.read_sdmSCue(cue[i],op_cue,424,736,256,736);
                    if (rslt) // 3rd level read
                    {
                      System.out.println("Second Level Read Successful, getting into 3rd Level\n");
                      rslt=s.read_sdmSCue(op_cueSCue,op_cue,256,736, 136,736); // cue[] now contains SCue
                      if (rslt) // 3rd level read
                      {
                        System.out.println("Third Level Read Successful, getting into 4th Level\n");
                        rslt=s.read_sdmSCue(op_cueSCue,op_cue,136,736, 0,736); // cue[] now contains SCue
                      }
                    }
                   }
                }
	        }catch(IOException ioe){;}
            //sss=convertCueBackward(cue[i]);
            System.out.println("Sdm:Recog:localReadSdmSCue: i/p:"+cue_stringsSCue[i]);
        	if(rslt)
	        {
              sss=convertCueBackward(op_cue);
              System.out.println("Sdm:Recog:localReadSdmSCue: o/p:"+ sss+"\n");

              for(j=0; j<r_sizeInBytes; j=j+4)
              {
//                System.out.print(Htbl.myHtbl.get(cue_stringsSCue[i].substring(j,j+4))+" => ");
//                System.out.print(Htbl.myHtbl.get(sss.substring(j,j+4))+" \n");
              }

	        }
	        else
	        	  System.out.println("Sdm:Recog:localReadSdmSCue: Diverge on last level");
        } // end for
        return(rslt);
    } // end localReadSdmSCue4Levels
    //-------------------------------------------------------------------
    public static boolean localReadSdmSCue5Levels() throws IOException
    {
        String sss=new String("");
        boolean rslt=true;
        int i,j;

        /* 65% = 60 out of the 92 chars  480 bits
           65% = 39         312 bits
           65% = 25         200 bits
           65% = 16         128 bits */


        System.out.println("\nTest 5 Levels");
        System.out.println("=============\n");

        for(i=0;i<cue_max;i++)
        {
         convertCueForward(cue_stringsSCue[i],cue[i]);
        }

        for(i=0;i<cue_max;i++)
        {
            try{
        	    System.out.println("Sdm:Recog:localReadSdmSCue: Reading from SDM with "+cue_stringsSCue[i]);
        	    if (i==0)
        	    {
        	      System.out.println("First Cue \n");
        	      rslt=s.read_sdmSCue(cue[i],op_cueSCue,0,128, 0,200); // cue[] now contains SCue
        	      if(rslt)
        	      {
        	       System.out.println("First Level Read Successful, getting into 2nd Level\n");
        	       rslt=s.read_sdmSCue(cue[i],op_cueSCue,0,200, 0,312); // cue[] now contains SCue
        	       if(rslt)
        	       {
        	        System.out.println("Second Level Read Successful, getting into 3rd Level\n");
                    rslt=s.read_sdmSCue(cue[i],op_cueSCue,0,312, 0,480); // cue[] now contains SCue
                    if(rslt)
                    {
                      System.out.println("Third Level Read Successful, getting into 4th Level\n");
                      rslt=s.read_sdmSCue(op_cueSCue,op_cue,0, 480, 0,600); // cue[] now contains SCue

                      if (rslt) // 3rd level read
                      {
                        System.out.println("Fourth Level Read Successful, getting into 5th Level\n");
                        rslt=s.read_sdmSCue(op_cueSCue,op_cue,0,600, 0,736); // cue[] now contains SCue
                      }
                    }
                   }
                  }
                }
                else
                if (i==1)
                {
                  System.out.println("Second Cue \n");
                  rslt=s.read_sdmSCue(cue[i],op_cue,608,736,536,736);
                  if(rslt)
                  {
                   System.out.println("First Level Read Successful, getting into 2nd Level\n");
                   rslt=s.read_sdmSCue(cue[i],op_cue,536,736,424,736);
                   if(rslt)
                   {
                    System.out.println("Second Level Read Successful, getting into 3rd Level\n");
                    rslt=s.read_sdmSCue(cue[i],op_cue,424,736,256,736);
                    if (rslt) // 3rd level read
                    {
                      System.out.println("Third Level Read Successful, getting into 4th Level\n");
                      rslt=s.read_sdmSCue(op_cueSCue,op_cue,256,736, 136,736); // cue[] now contains SCue
                      if (rslt) // 3rd level read
                      {
                        System.out.println("Fourth Level Read Successful, getting into 5th Level\n");
                        rslt=s.read_sdmSCue(op_cueSCue,op_cue,136,736, 0,736); // cue[] now contains SCue
                      }
                    }
                   }
                  }
                }
	        }catch(IOException ioe){;}
            //sss=convertCueBackward(cue[i]);
            System.out.println("Sdm:Recog:localReadSdmSCue: i/p:"+cue_stringsSCue[i]);
        	if(rslt)
	        {
              sss=convertCueBackward(op_cue);
              System.out.println("Sdm:Recog:localReadSdmSCue: o/p:"+ sss+"\n");
              for(j=0; j<r_sizeInBytes; j=j+4)
              {
//                System.out.print(Htbl.myHtbl.get(cue_stringsSCue[i].substring(j,j+4))+" => ");
//                System.out.print(Htbl.myHtbl.get(sss.substring(j,j+4))+" \n");
              }
	        }
	        else
	        	  System.out.println("Sdm:Recog:localReadSdmSCue: Diverge on last level");
        } // end for
        return(rslt);
    } // end localReadSdmSCue5Levels
    //-------------------------------------------------------------------
    //-------------------------------------------------------------------
    public static boolean localReadSdmSCueNLevels(int N) throws IOException
    {
        String sss=new String("");
        boolean rslt=true;
        int i,j;

        /* 65% = 60 out of the 92 chars  480 bits
           65% = 39         312 bits
           65% = 25         200 bits
           65% = 16         128 bits */


        System.out.println("\nTest "+ N+" Levels");
        System.out.println("=============\n");

        for(i=0;i<cue_max;i++)
        {
         convertCueForward(cue_stringsSCue[i],cue[i]);
        }

        for(i=0;i<cue_max;i++)
        {
            try{
        	    System.out.println("Sdm:Recog:localReadSdmSCue: Reading from SDM with "+cue_stringsSCue[i]);
        	    if (i==0)
        	    {
        	      System.out.println("First Cue \n");
        	      rslt=s.read_sdmSCue(cue[i],op_cueSCue,0,128, 0,200); // cue[] now contains SCue
        	      int loop_i=1;
        	      while (rslt && loop_i <= N)
        	      {
        	       System.out.println(loop_i+" Level Read Successful, getting into "+loop_i+1+" Level\n");
        	       rslt=s.read_sdmSCue(cue[i],op_cueSCue,0,(int)(r_size*intExponentiate(0.8 ,(N-loop_i))),0,(int)(r_size*intExponentiate(0.8,(N-loop_i-1)))
        	                          );
        	       // cue[] now contains SCue

                  } // end while
                }
                else
                if (i==1)
                {
                  System.out.println("Second Cue \n");
                  rslt=s.read_sdmSCue(cue[i],op_cue,608,736,536,736);
                  if(rslt)
                  {
                   System.out.println("First Level Read Successful, getting into 2nd Level\n");
                   rslt=s.read_sdmSCue(cue[i],op_cue,536,736,424,736);
                   if(rslt)
                   {
                    System.out.println("Second Level Read Successful, getting into 3rd Level\n");
                    rslt=s.read_sdmSCue(cue[i],op_cue,424,736,256,736);
                    if (rslt) // 3rd level read
                    {
                      System.out.println("Third Level Read Successful, getting into 4th Level\n");
                      rslt=s.read_sdmSCue(op_cueSCue,op_cue,256,736, 136,736); // cue[] now contains SCue
                      if (rslt) // 3rd level read
                      {
                        System.out.println("Fourth Level Read Successful, getting into 5th Level\n");
                        rslt=s.read_sdmSCue(op_cueSCue,op_cue,136,736, 0,736); // cue[] now contains SCue
                      }
                    }
                   }
                  }
                }
	        }catch(IOException ioe){;}
            //sss=convertCueBackward(cue[i]);
            System.out.println("Sdm:Recog:localReadSdmSCue: i/p:"+cue_stringsSCue[i]);
        	if(rslt)
	        {
              sss=convertCueBackward(op_cue);
              System.out.println("Sdm:Recog:localReadSdmSCue: o/p:"+ sss+"\n");
              for(j=0; j<r_sizeInBytes; j=j+4)
              {
//                System.out.print(Htbl.myHtbl.get(cue_stringsSCue[i].substring(j,j+4))+" => ");
//                System.out.print(Htbl.myHtbl.get(sss.substring(j,j+4))+" \n");
              }
	        }
	        else
	        	  System.out.println("Sdm:Recog:localReadSdmSCue: Diverge on last level");
        } // end for
        return(rslt);
    } // end localReadSdmSCueNLevels
    //-------------------------------------------------------------------
    public static boolean localReadSdm() throws IOException
    {
        String sss=new String("");
        boolean rslt=true;
        int i,j;

        for(i=0;i<cue_max_all;i++)
        {
            try{
        	    System.out.println("Sdm:Recog:localReadSdm: Reading from SDM");
                rslt=s.read_sdm(cue[i],op_cue);
	        }catch(IOException ioe){System.out.println("IOE @ read_sdm\n");}
            //sss=convertCueBackward(cue[i]);
              System.out.println("Sdm:Recog:localReadSdm: i/p cue #"+(i+1)+" out of "+(cue_max_all)+":"+cue_strings[i]);
        	if(rslt)
	        {
              sss=convertCueBackward(op_cue);
              System.out.println("Sdm:Recog:localReadSdm: o/p cue #"+(i+1)+" out of "+(cue_max_all)+":"+sss+"\n");
			  /*
              for(j=0; j<r_sizeInBytes; j=j+4)
              {
                System.out.print(Htbl.myHtbl.get(cue_strings[i].substring(j,j+4))+" => ");
                System.out.print(Htbl.myHtbl.get(sss.substring(j,j+4))+" \n");
              } // end for
			  */
	        }
	        else
	        {
	        	  System.out.println("Sdm:Recog:localReadSdm: Diverge");
	        }

	        //System.out.println("end of for loop w cntr = "+i);
        } // end for i

        return(rslt);
    } // end localReadSdm
    //-------------------------------------------------------------------
    public static int localWriteSdm() throws IOException
    {
        int jojo=0;

		cue_strings[0]="1001234310123456789JOAN SMITH      ASMC01012001SMITH@NAVY.MIL      11JOAN SMITH      ASMC072001123423453456456756781123451ABCDEFGHIJKLMNOPQRSTUVWXYZAF010120021AVIATION TECHNIC1ABC01012001010120011001AVIATION TECHNIG456784ABC123456781234L100112345678FIND ME A JOB       0.2310.240.250.260110.270.28100000.500901AL10NORFOLK         AVIATION TECHNIJ1234010120021101200201012002010520010108200130011120010115200101182001010200101212001012420010127200102020120010204200102072001011013110112112342345345645675678112340301200103042001100020NORFOLK         AVIATION TECH   1111JOB ACCEPTANCE  1111111";        cue_stringsSCue[1]="0105678311234567891MIKEY KELMORE   AS1 01022001KELMO@NAVY.MIL      00MIKEY KELMORE   AS1 082001135724683579468057912234562BCDEFGHIJKLMNOPQRSTUVWXYZABM010220022AVIATION TECHNID0BCD01022001010220012011AVIATION TECHNIH567895BCD234567892345A201123456789JOB DENIAL          0.3410.350.360.371000.380.39200000.601010LL11NORFOLK         AVIATION TECHNIK2345010220020102200201012003010620010109200131011320010116200101192001111200101222001012520010128200103020220010205200102082001101114211113012352346345745685679112350302200103052001101021JACKSONVILLE    BVIATION TECH   2120JOB DENIAL      1010102";
        cue_strings[1]="0105678311234567891MIKEY KELMORE   AS1 01022001KELMO@NAVY.MIL      00MIKEY KELMORE   AS1 082001135724683579468057912234562BCDEFGHIJKLMNOPQRSTUVWXYZABM010220022AVIATION TECHNID0BCD01022001010220012011AVIATION TECHNIH567895BCD234567892345A201123456789JOB DENIAL          0.3410.350.360.371000.380.39200000.601010LL11NORFOLK         AVIATION TECHNIK2345010220020102200201012003010620010109200131011320010116200101192001111200101222001012520010128200103020220010205200102082001101114211113012352346345745685679112350302200103052001101021JACKSONVILLE    BVIATION TECH   2120JOB DENIAL      1010102";
        cue_strings[2]="0019031312345678912DAVID DEMORE    AS2 01032001DEMOR@NAVY.MIL      00DAVID DEMORE    AS2 092001147025813692470358143345670CDEFGHIJKLMNOPQRSTUVWXYZABCM010320023AVIATION TECHNIE0CDE01032001010320013021AVIATION TECHNIG678906CDE345678903456B302134567890JOB ACCEPTANCE      0.4510.460.470.480010.490.50300000.701120BL12NORFOLK         AVIATION TECHNIL3456010320020023200201012004010720010110200132011420010117200101202001212200101232001012620010129200104020320010206200102092001201215312014112362347345845695670012360303200103062001102022SAN DIEGO       CVIATION TECH   4130JOB OFFER       0000003";
        cue_strings[3]="1001234310123456789                ASMC01012001SMITH@NAVY.MIL      11JOAN SMITH      ASMC072001123423453456456756781123451ABCDEFGHIJKLMNOPQRSTUVWXYZAF010120021AVIATION TECHNIC1ABC01012001010120011001AVIATION TECHNIG456784ABC123456781234L100112345678FIND ME A JOB       0.2310.240.250.260110.270.28100000.500901AL10NORFOLK         AVIATION TECHNIJ1234010120021101200201012002010520010108200130011120010115200101182001010200101212001012420010127200102020120010204200102072001011013110112112342345345645675678112340301200103042001100020NORFOLK         AVIATION TECH   1111JOB ACCEPTANCE  1111111";
		cue_strings[4]="0105678311234567891                AS1 01022001KELMO@NAVY.MIL      00MIKEY KELMORE   AS1 082001135724683579468057912234562BCDEFGHIJKLMNOPQRSTUVWXYZABM010220022AVIATION TECHNID0BCD01022001010220012011AVIATION TECHNIH567895BCD234567892345A201123456789JOB DENIAL          0.3410.350.360.371000.380.39200000.601010LL11NORFOLK         AVIATION TECHNIK2345010220020102200201012003010620010109200131011320010116200101192001111200101222001012520010128200103020220010205200102082001101114211113012352346345745685679112350302200103052001101021JACKSONVILLE    BVIATION TECH   2120JOB DENIAL      1010102";
        cue_strings[5]="0019031312345678912                AS2 01032001DEMOR@NAVY.MIL      00DAVID DEMORE    AS2 092001147025813692470358143345670CDEFGHIJKLMNOPQRSTUVWXYZABCM010320023AVIATION TECHNIE0CDE01032001010320013021AVIATION TECHNIG678906CDE345678903456B302134567890JOB ACCEPTANCE      0.4510.460.470.480010.490.50300000.701120BL12NORFOLK         AVIATION TECHNIL3456010320020023200201012004010720010110200132011420010117200101202001212200101232001012620010129200104020320010206200102092001201215312014112362347345845695670012360303200103062001102022SAN DIEGO       CVIATION TECH   4130JOB OFFER       0000003";

        for(int ii=0;ii<cue_max_all;ii++)
        {	// just convert all cues, no writing yet
         //String ttos=new String("");
         //ttos=cue_strings[ii]+cue_strings[ii]+cue_strings[ii]; // 80 x 3 = 240
         //cue_strings[ii]=ttos;
         //System.out.println("Sdm:Recog:localWriteSdm: Converting forward cue : "+ii+"\n"+cue_strings[ii]);
         convertCueForward(cue_strings[ii],cue[ii]);
        }

        System.out.println("Sdm:Recog:localWriteSdm: Please wait while write cues into SDM");
        for(int i=cue_start;i<cue_max;i++)
        {

            System.out.println("Sdm:Recog:localWriteSdm: Please wait while writing in SDM cue No "+i);
            for (int write_count=0;write_count<write_max;write_count++)
            {
                try{
                    jojo=s.write_locations(cue[i]);
	            System.out.println("Sdm:Recog:localWriteSdm: for cue no. "+i+"  "+jojo+"  locations were written");
	            }catch(IOException ioe){System.out.println("Sdm:Recog:localWriteSdm: Cannot write to SDM");}
            }
        }
        return(jojo);
    } // end localWriteSdm




    //-------------------------------------------------------------------
    public static void convertCueForward(String input_cue, boolean[] output_boolean)
    {
        byte byte_size=8;
        int i,j,k;
        int wbt;
        byte output_bytes[]=new byte[r_size];

	System.out.println("\nSdm:Recog:convertCueForward: input_cue: "+input_cue);

    try{
		System.out.println("In convertCueForward : length of passed i/p cue is: "+ input_cue.length());
        for (i=0;i<input_cue.length();i++)
        {
            wbt=(int)((byte)input_cue.charAt(i));
            for (j=0;j<byte_size;j++)
            {
				try {
					output_bytes[i*byte_size+j]=(byte)(wbt>>>(byte_size -1 - j));
				}
				catch(ArrayIndexOutOfBoundsException aiobe) {
					System.err.println("ArrayIndexOutOfBoundsException");
					System.err.println("    byte_size = " + byte_size);
					System.err.println("    i = " + i);
					System.err.println("    j = " + j);
					aiobe.printStackTrace();
				}
                if((byte)(wbt>>>(byte_size -1 - j))==1) //unsigned R shift
                  output_boolean[i*byte_size+j]=true;
                else
                  output_boolean[i*byte_size+j]=false;
                wbt=(wbt - (output_bytes[i*byte_size+j] << (byte_size -1 - j)));
            } // end for
        } // end for
    }catch(NullPointerException ioe){System.out.println("Sdm:Recog:convertCueForward: Done with excep");}
	}

	// end convertCueForward
    //-------------------------------------------------------------------
    public static String convertCueBackward(boolean[] output_boolean)
    {
        byte byte_size=8;
        byte output_bytes[]=new byte[r_size];
        int i,j,k;
        int wbt;
        int byte_acc=0;
        int count_byt=0;
        //String input_cue=new String("");

        int op_arr_siz=r_size/byte_size;
        byte op_byte_arr[]=new byte[op_arr_siz];

        for (j=0;j<r_size;j++)
           if(output_boolean[j])
             output_bytes[j]=1;
           else
             output_bytes[j]=0;

        for (i=0;i<(op_arr_siz);i++)
        {
            byte_acc=0;
            for (j=0;j<byte_size;j++)
            {
                byte_acc=byte_acc+ (output_bytes[i*byte_size+j] << (byte_size -1 - j));
            } // end for
                if (byte_acc >0)
                {
                            op_byte_arr[i]=(byte)byte_acc;
                            count_byt++;
                }
            } // end for

        String input_cue=new String(op_byte_arr,0,0,count_byt);
        return(input_cue);

    } // end convertCueBackward
    //-------------------------------------------------------------------
    public static double intExponentiate(double base , int power)
    {
        double result=base;
        int loopndx=1;
        for (loopndx=1; loopndx<power ; loopndx++)
        {
            result=result*base;
        }
        return(result);
    }
    //-------------------------------------------------------------------

	//{{DECLARE_CONTROLS
	MemoryReadReturn memoryReadReturn1 = new MemoryReadReturn();
	//}}
} // end recog
//-----------------------------------------------------------------------------
