package edu.memphis.ccrg.lida.framework.gui.utils;

import edu.memphis.ccrg.lida.framework.shared.Link;

/**
 * @author Javier Snaider
 *
 */
public class GuiLink {
	private Link link;
	private char type;

	/**
	 * @param link
	 * @param type
	 */
	public GuiLink(Link link, char type) {
		this.link = link;
		this.type = type;
	}
	

	
	/**
	 * @return the link
	 */
	public Link getLink() {
		return link;
	}

	/**
	 * @param link the link to set
	 */
	public void setLink(Link link) {
		this.link = link;
	}

	/**
	 * @return the type
	 */
	public char getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(char type) {
		this.type = type;
	}

	public boolean equals (Object o){
		if (!(o instanceof GuiLink)){
			return false;
		}
		GuiLink gl= (GuiLink)o;
		return (link.equals(gl.link)&& type == gl.type);
	}
	
	
	public int hashCode(){
		return link.hashCode() + type;
	}
	public String toString(){
		return type+"";
	}
}
