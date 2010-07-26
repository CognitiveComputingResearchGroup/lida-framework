package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Properties;

import edu.memphis.ccrg.lida.framework.Lida;

/**
 * Factory for Lida objects
 * @author Javier Snaider
 *
 */
public interface LidaFactory {

	public abstract Lida getLida(Properties properties);

}