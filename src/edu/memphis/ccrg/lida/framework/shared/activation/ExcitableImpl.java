package edu.memphis.ccrg.lida.framework.shared.activation;

import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;

/**
 * The default (abstract) implementation of {@link Excitable}.
 * @author Ryan J. McCall
 *
 */
public abstract class ExcitableImpl extends DecayableImpl implements Excitable {
    
	/**
	 * The {@link ExciteStrategy} used by this {@link Excitable}.
	 */
	protected ExciteStrategy exciteStrategy;
	
	@Override
	public void setExciteStrategy(ExciteStrategy s){
		exciteStrategy = s;
	}
	
	@Override
	public ExciteStrategy getExciteStrategy(){
		return exciteStrategy;
	}
}