package edu.memphis.ccrg.lida.framework;

import java.util.Properties;

public interface LidaFactory {

	public abstract Lida getLida(Properties properties);

}