package edu.domain.wumpusWorld;
/*
 * Class that defines agent actions.
 * 
 * Written by James P. Biagioni (jbiagi1@uic.edu)
 * for CS511 Artificial Intelligence II
 * at The University of Illinois at Chicago
 * 
 * Last modified 1/31/07 
 * 
 * DISCLAIMER:
 * Elements of this application were borrowed from
 * the client-server implementation of the Wumpus
 * World Simulator written by Kruti Mehta at
 * The University of Texas at Arlington.
 * 
 */ 

class Action {
	
	public static int START_TRIAL = 0;
	public static int GO_FORWARD = 1;
	public static int TURN_RIGHT = 2;
	public static int TURN_LEFT = 3;
	public static int GRAB = 4;
	public static int SHOOT = 5;
	public static int NO_OP = 6;
	public static int END_TRIAL = 7;
	
	public Action() {
		
		// nothing to construct...
		
	}
	
	public static String printAction(int action) {
		
		if (action == 0) return "START_TRIAL";
		else if (action == 1) return "GO_FORWARD";
		else if (action == 2) return "TURN_RIGHT";
		else if (action == 3) return "TURN_LEFT";
		else if (action == 4) return "GRAB";
		else if (action == 5) return "SHOOT";
		else if (action == 6) return "NO_OP";
		else return "END_TRIAL";
		
	}
	
}