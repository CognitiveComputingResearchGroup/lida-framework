/*
    File:   Timer.java
    Author: Sidney D'Mello
    Date:   Feb 28, 2003 
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.util;

import java.util.Date;
import java.io.Serializable;

/*
 *  Timer is a very simple class designed to provide consistency in time calculations
 *  over the sdm package.
 *
 *  This is a simple way to calculate tim:
 *  Timer t = new Timer();
 *  t.start();
 *  t.stop();
 *  t.getTimeInSecs();
 */

public class SidneyTimer implements Serializable
{
	private long start;
	private long stop;

/**
 *  Create a new timer object.
 *
 *  It just performs some very basic initializations. It does not start the timer.
 */        
	public SidneyTimer()     
	{
		start = 0;
		stop = 0;
	}

/**
 *  Start the timer.
 *
 *  @return return the time the timer started as the number of milliseconds simce Jan 1, 1970.
 */        
	public long start()
	{
		start = new Date().getTime();
	
		return start;
	}

/**
 *  Stop the timer.
 *
 *  @return return the time the timer stopped as the number of milliseconds simce Jan 1, 1970.
 */        
	public long stop()
	{
		stop = new Date().getTime();
		
		return stop;
	}

        
/*
 *  Get the time between start and stop in milliseconds.
 *
 *  @return elapsed time in milliseconds.
 */        
	public long getTimeInMillis()
	{
		return (stop - start);
	}

/*
 *  Get the time between start and stop in seconds.
 *
 *  @return elapsed time in seconds.
 */        
	public double getTimeInSecs()
	{
		return ((stop - start)/1000);
	}

/**
 *  Reset this timer.
 *
 */        
	public void reset()
	{
		start = 0;
		stop = 0;
	}

/**
 *  Get the time in millis when this timer was last started.
 *
 *  @return time (in millis) when this timer was last started.
 */        
        public long getStartTime()
        {
            return start;
        }

/**
 *  Get the time in millis when this timer was last stopped.
 *
 *  @return time (in millis) when this timer was last stopped.
 */        
        public long getStopTime()
        {
            return stop;
        }
        
/**
 *  String in the form of start_time : stop_time representing this timer.
 *
 *  @return String representing this timer.
 */        
	public String toString()
	{
            return(start + " : " + stop);
	}
}
