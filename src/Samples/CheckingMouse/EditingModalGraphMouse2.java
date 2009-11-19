/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Samples.CheckingMouse;

import edu.uci.ics.jung.visualization.RenderContext;
import edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import org.apache.commons.collections15.Factory;

/**
 * I had trouble adding my new EditingPlugin to the existing 
 * EditingModalGraphMouse so I came up with this as a work around.
 * 
 * @author Dr. Greg M. Bernstein
 */
public class EditingModalGraphMouse2<V,E> extends EditingModalGraphMouse<V,E> {
    
    	public EditingModalGraphMouse2(RenderContext rc,
			Factory<V> vertexFactory, Factory<E> edgeFactory) {
		super(rc, vertexFactory, edgeFactory, 1.1f, 1/1.1f);
	}
        	public EditingModalGraphMouse2(RenderContext rc,
			Factory<V> vertexFactory, Factory<E> edgeFactory, float in, float out) {
		super(rc, vertexFactory, edgeFactory, in, out);

	}
                
        public void setEditingPlugin(EditingGraphMousePlugin plug) {
            editingPlugin = plug;
        }

}
