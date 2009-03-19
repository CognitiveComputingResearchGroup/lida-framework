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

	public static void print(char[][][] a) {
		for(int i = 0; i < a.length; i++){
			for(int j = 0; j < a[0].length; j++){
				for(int k = 0; k < a[0][0].length; k++){
					System.out.print(a[i][j][k] + " ");
				}
				System.out.print(" ");
			}
			System.out.println("\n");
		}
		
	}

}
