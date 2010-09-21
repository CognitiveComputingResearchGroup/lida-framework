/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.Collection;

/**
 * A generic class to manage a group of Activatibles
 * 
 * 
 * @author Javier Snaider
 *
 */
public class ActivatibleDriver {
	private Collection<? extends Activatible> activatibles;
	
	public void setActivatibles (Collection<? extends Activatible> activatibles){
		this.activatibles=activatibles;
	}

	/**
	 * @return the activatibles
	 */
	public Collection<? extends Activatible> getActivatibles() {
		return activatibles;
	}
	
	/**
	 * this method decays all the Activatible elements
	 */
	public void decay(long ticks) {
		for(Activatible a: activatibles){
			a.decay (ticks);
		}
	}
	
	/**
	 * this method excites all the Activatible elements with the same amount
	 * @param amount 
	 */
	public void excite(double amount){
		for(Activatible a: activatibles){
			a.excite(amount);
		}
	}

	public Activatible getMostActivated(){
		Activatible res=null;
		for(Activatible a: activatibles){
			if (res==null){
				res=a;
			}else if (res.getActivation()<a.getActivation()){
				res=a;
			}
		}
		return res;		
	}

	public double getAgregateActivation(){
		double agregate=0.0;
		for(Activatible a: activatibles){
			agregate += a.getActivation();			
		}
		return agregate;
	}


}
