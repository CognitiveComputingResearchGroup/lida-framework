package edu.memphis.ccrg.lida.framework.shared.test;

/**
 * Implementation of {@link LongElement}.
 * @author Ryan J. McCall
 */
public class LongElementImpl extends NameableImpl implements LongElement {

	private long id;
	
	@Override
	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public long getId() {
		return id;
	}

}
