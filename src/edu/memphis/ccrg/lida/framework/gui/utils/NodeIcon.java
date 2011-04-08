/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.framework.gui.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

import edu.memphis.ccrg.lida.framework.gui.panels.NodeStructurePanel;

/**
 * Utility which {@link NodeStructurePanel} uses to represent Nodes.
 * @author Javier Snaider
 *
 */
public class NodeIcon implements Icon {
	
	public static final Icon NODEICON=new NodeIcon(20,Color.red);
	public static final Icon LINKICON=new NodeIcon(5,Color.black);

	private int size;
	private Color color;
	
	
	/**
	 * @param size NodeIcon size
	 * @param color NodeIcon color
	 */
	public NodeIcon(int size,Color color) {
		this.color = color;
		this.size = size;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconHeight()
	 */
	@Override
	public int getIconHeight() {
		return size;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconWidth()
	 */
	@Override
	public int getIconWidth() {
		return size;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(color);
		g.fillOval(x, y, size, size);

	}

}
