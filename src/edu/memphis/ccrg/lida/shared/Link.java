package edu.memphis.ccrg.lida.shared;


public interface Link extends Linkable{

	public abstract Link copy(Link l);

	public abstract Linkable getSource();

	public abstract Linkable getSink();

	public abstract LinkType getType();

}