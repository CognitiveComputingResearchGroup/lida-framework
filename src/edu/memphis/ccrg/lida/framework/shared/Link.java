package edu.memphis.ccrg.lida.framework.shared;


public interface Link extends Linkable, Activatible{
	
	public static final Link NULL_LINK=new LinkImpl(null,null,LinkType.NONE);

	public abstract Linkable getSource();

	public abstract Linkable getSink();
		
	public abstract LinkType getType();
	
	public abstract void setSource(Linkable source);

	public abstract void setSink(Linkable sink);

	public abstract void setType(LinkType type);

	public abstract void setReferencedLink(Link l);

	public abstract Link getReferencedLink();

}