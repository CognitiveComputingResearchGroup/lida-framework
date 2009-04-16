package edu.memphis.ccrg.lida.shared;

public interface Link extends Linkable{
	
	void setType(LinkType type);
	LinkType getType();
	
	void setSource(Linkable source);
	void setSink(Linkable sink);
	
	Linkable getSource();	
	Linkable getSink();

	Link copy(Link l);
	
	String toString();	

}//interface