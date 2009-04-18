package edu.memphis.ccrg.lida.shared;


public interface Link extends Linkable{

	public abstract Linkable getSource();

	public abstract Linkable getSink();
		
	public abstract void setIds(String id);

	public abstract LinkType getType();
	
	public abstract void setSource(Linkable source);

	public abstract void setSink(Linkable sink);

	public abstract void setType(LinkType type);

}