package edu.memphis.ccrg.lida.framework.shared;


public interface Link extends Linkable, Activatible{
	
	public static final Link NULL_LINK=new LinkImpl(null,null,LinkCategory.None);

	public abstract Linkable getSource();

	public abstract Linkable getSink();
		
	public abstract LinkCategory getCategory();
	
	public abstract void setSource(Linkable source);

	public abstract void setSink(Linkable sink);

	public abstract void setCategory(LinkCategory type);

	public abstract void setReferencedLink(Link l);

	public abstract Link getReferencedLink();

}