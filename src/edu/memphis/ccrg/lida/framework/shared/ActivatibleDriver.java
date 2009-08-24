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
	public void decay() {
		for(Activatible a: activatibles){
			a.decay();
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
