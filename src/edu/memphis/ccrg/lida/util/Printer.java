package edu.memphis.ccrg.lida.util;

import java.util.Collection;
import java.util.Set;

import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Linkable;
import edu.memphis.ccrg.lida.shared.Node;

public class Printer{
	
	public static double rnd(double d){    //rounds a double to the nearest 100th
    	return Math.round(d*10.0)/10.0;
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
	//Linkables
	public static void printIds(Collection<Linkable> linkables) {
		if(linkables != null){
			for(Linkable l: linkables)
				System.out.println(l.getIds());
			System.out.println();
		}
	}
	public static void printLabels(Collection<Linkable> linkables) {
		if(linkables != null){
			for(Linkable l: linkables)
				System.out.println(l.getIds());
			System.out.println();
		}
	}
	//Links
	public static void printTypes(Collection<Link> links) {
		if(links != null){
			for(Link l: links){
				System.out.println(l.getType());
			}
			System.out.println();
		}
	}
	public static void printLinks(Collection<Link> links) {
		if(links != null){
			for(Link l: links){
				System.out.println(l.toString());
			}
			System.out.println();
		}
	}
	//Nodes
	public static void printNodes(Collection<Node> nodes) {
		if(nodes != null){
			for(Node n: nodes){
				System.out.println(n.toString());
			}
			System.out.println();
		}
	}//
	
}//class
