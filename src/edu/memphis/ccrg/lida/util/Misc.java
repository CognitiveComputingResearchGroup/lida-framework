package edu.memphis.ccrg.lida.util;

public class Misc{
	
	public static double rnd(double d){    //rounds a double to the nearest 100th
    	return Math.round(d*10000.0)/10000.0;
    }
	
	public static void p(double d){
		System.out.println(d + "");
	}
	
	public static void p(String s){
		System.out.println(s);
	}

}
