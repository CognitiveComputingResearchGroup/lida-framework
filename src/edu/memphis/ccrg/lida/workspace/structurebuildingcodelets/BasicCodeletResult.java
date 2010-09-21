/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

public class BasicCodeletResult implements CodeletResult {
	
	private boolean finishedRunningNormally = false;
	private long id = 0;
	
	public void reportFinished(){
		finishedRunningNormally = true;
	}
	public boolean getCompletionStatus(){
		return finishedRunningNormally;
	}
	
	public void setId(long id){
		this.id = id;
	}
	public long getId(){
		return id;
	}
	

}
