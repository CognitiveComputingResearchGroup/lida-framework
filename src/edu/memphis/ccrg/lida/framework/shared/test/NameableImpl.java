package edu.memphis.ccrg.lida.framework.shared.test;

/**
 * Implementation of {@link Nameable}.
 * @author Ryan J. McCall
 */
public class NameableImpl implements Nameable {

	private String name;
	
	@Override
	public void setName(String n) {
		name = n;
	}

	@Override
	public String getName() {
		return name;
	}

}
