package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Properties;

import edu.memphis.ccrg.lida.framework.Lida;

public interface LidaFactory {

	public abstract Lida getLida(Properties properties);

}