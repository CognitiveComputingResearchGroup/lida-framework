/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.framework.gui.utils;

import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;

/**
 * A GraphMousePlugin that brings up distinct popup menus when an edge or vertex
 * is appropriately clicked in a graph. If these menus contain components that
 * implement either the EdgeMenuListener or VertexMenuListener then the
 * corresponding interface methods will be called prior to the display of the
 * menus (so that they can display context sensitive information for the edge or
 * vertex).
 * 
 * @author Dr. Greg M. Bernstein
 */
public class PopupVertexFormMousePlugin<V, E> extends
		AbstractPopupGraphMousePlugin {

	/** Creates a new instance of PopupVertexEdgeMenuMousePlugin */
	public PopupVertexFormMousePlugin() {
		this(InputEvent.BUTTON3_MASK);
	}

	/**
	 * Creates a new instance of PopupVertexEdgeMenuMousePlugin
	 * 
	 * @param modifiers
	 *            mouse event modifiers see the jung visualization Event class.
	 */
	public PopupVertexFormMousePlugin(int modifiers) {
		super(modifiers);
	}

	/**
	 * Implementation of the AbstractPopupGraphMousePlugin method. This is where
	 * the work gets done. You shouldn't have to modify unless you really want
	 * to...
	 * 
	 * @param e
	 */
	@Override
	@SuppressWarnings("unchecked")
	protected void handlePopup(MouseEvent e) {
		final VisualizationViewer<V, E> vv = (VisualizationViewer<V, E>) e
				.getSource();
		Point2D p = e.getPoint();

		GraphElementAccessor<V, E> pickSupport = vv.getPickSupport();
		if (pickSupport != null) {
			final V v = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p
					.getY());
			if (v != null) {
				// System.out.println("Vertex " + v + " was right clicked");
				if (v instanceof Node) {
					//vertexPopup.show(vv, e.getX(), e.getY());
				} 
			}
		}
	}

}
