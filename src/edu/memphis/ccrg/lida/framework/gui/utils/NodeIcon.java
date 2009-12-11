/**
 * 
 */
package edu.memphis.ccrg.lida.framework.gui.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * @author Javier Snaider
 *
 */
public class NodeIcon implements Icon {
	
	public static final Icon NODEICON=new NodeIcon(20,Color.red);
	public static final Icon LINKICON=new NodeIcon(5,Color.black);

	private int size;
	private Color color;
	
	/**
	 * @param size
	 * @param color
	 */
	public NodeIcon(int size,Color color) {
		this.color = color;
		this.size = size;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconHeight()
	 */
	public int getIconHeight() {
		return size;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#getIconWidth()
	 */
	public int getIconWidth() {
		return size;
	}

	/* (non-Javadoc)
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics, int, int)
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor(color);
		g.fillOval(x, y, size, size);

	}

}
