/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
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
	 * @param link Lida Framework link
	 * @param type type
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

	@Override
	public boolean equals (Object o){
		if (!(o instanceof GuiLink)){
			return false;
		}
		GuiLink gl= (GuiLink)o;
		return (link.equals(gl.link)&& type == gl.type);
	}
	
	
	@Override
	public int hashCode(){
		return link.hashCode() + type;
	}
	@Override
	public String toString(){
		return type+"";
	}
}
