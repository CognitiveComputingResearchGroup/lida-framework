package edu.memphis.ccrg.lida.framework.shared.test;

/**
 * Implementation of {@link IntegerElement}.
 * @author Ryan J. McCall
 */
public class IntegerElementImpl extends NameableImpl implements IntegerElement {

	private int id;

	@Override
	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public int getId() {
		return id;
	}

}
