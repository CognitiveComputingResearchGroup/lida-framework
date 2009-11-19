package Samples.CheckingMouse;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;


import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.util.ArrowFactory;
import edu.uci.ics.jung.visualization.VisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingGraphMousePlugin;

/**
 * A plugin that can create vertices, undirected edges, and directed edges
 * using mouse gestures. This plugin allows for user defined behavior to
 * determine whether a vertex or edge should be added to the graph. In addition,
 * parameters for a vertex or edge can be interactively set here prior to being
 * added to the graph.  Note: all "significant" code reused from Tom Nelson's
 * EditingGraphMousePlugin, I just added checking hooks.
 * 
 * @author Dr. Greg Bernstein
 *
 */
public class EditingCheckingGraphMousePlugin<V,E> extends EditingGraphMousePlugin<V,E> {
    

    protected EdgeChecker<V,E> edgeChecker;
    protected VertexChecker<V,E> vertexChecker;
    
    public EditingCheckingGraphMousePlugin(Factory<V> vertexFactory, Factory<E> edgeFactory) {
        this(MouseEvent.BUTTON1_MASK, vertexFactory, edgeFactory);
    }

    /**
     * create instance and prepare shapes for visual effects
     * @param modifiers
     */
    public EditingCheckingGraphMousePlugin(int modifiers, Factory<V> vertexFactory, Factory<E> edgeFactory) {
        super(modifiers, vertexFactory, edgeFactory);
        this.vertexFactory = vertexFactory;
        this.edgeFactory = edgeFactory;
        rawEdge.setCurve(0.0f, 0.0f, 0.33f, 100, .66f, -50,
                1.0f, 0.0f);
        rawArrowShape = ArrowFactory.getNotchedArrow(20, 16, 8);
        edgePaintable = new EdgePaintable();
        arrowPaintable = new ArrowPaintable();
		this.cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
    }
    
    /**
     * overrided to be more flexible, and pass events with
     * key combinations. The default responds to both ButtonOne
     * and ButtonOne+Shift
     */
    public boolean checkModifiers(MouseEvent e) {
        return (e.getModifiers() & modifiers) != 0;
    }

    /**
     * If the mouse is pressed in an empty area, create a new vertex there.
     * If the mouse is pressed on an existing vertex, prepare to create
     * an edge from that vertex to another
     */
    @SuppressWarnings("unchecked")
	public void mousePressed(MouseEvent e) {
        if(checkModifiers(e)) {
            final VisualizationViewer<V,E> vv =
                (VisualizationViewer<V,E>)e.getSource();
            final Point2D p = e.getPoint();
            GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
            if(pickSupport != null) {
            	Graph<V,E> graph = vv.getModel().getGraphLayout().getGraph();
            	// set default edge type
            	if(graph instanceof DirectedGraph) {
            		edgeIsDirected = EdgeType.DIRECTED;
            	} else {
            		edgeIsDirected = EdgeType.UNDIRECTED;
            	}
            	
                final V vertex = pickSupport.getVertex(vv.getModel().getGraphLayout(), p.getX(), p.getY());
                if(vertex != null) { // get ready to make an edge
                    startVertex = vertex;
                    down = e.getPoint();
                    transformEdgeShape(down, down);
                    vv.addPostRenderPaintable(edgePaintable);
                    if((e.getModifiers() & MouseEvent.SHIFT_MASK) != 0
                    		&& vv.getModel().getGraphLayout().getGraph() instanceof UndirectedGraph == false) {
                        edgeIsDirected = EdgeType.DIRECTED;
                    }
                    if(edgeIsDirected == EdgeType.DIRECTED) {
                        transformArrowShape(down, e.getPoint());
                        vv.addPostRenderPaintable(arrowPaintable);
                    }
                } else { // make a new vertex

                    V newVertex = vertexFactory.create();
                    Layout<V,E> layout = vv.getModel().getGraphLayout();
                    boolean okToAdd = true;
                    if (vertexChecker != null) {
                        okToAdd = vertexChecker.checkVertex(graph, vv, newVertex);
                    }
                    if (okToAdd) {
                        graph.addVertex(newVertex);
                        layout.setLocation(newVertex, vv.getRenderContext().getMultiLayerTransformer().inverseTransform(e.getPoint()));
                        System.out.println("Added vertex: " + newVertex);
                    }
                }
            }
            vv.repaint();
        }
    }
    
    /**
     * If startVertex is non-null, and the mouse is released over an
     * existing vertex, create an undirected edge from startVertex to
     * the vertex under the mouse pointer. If shift was also pressed,
     * create a directed edge instead.
     */
    @SuppressWarnings("unchecked")
	public void mouseReleased(MouseEvent e) {
        if(checkModifiers(e)) {
            final VisualizationViewer<V,E> vv =
                (VisualizationViewer<V,E>)e.getSource();
            final Point2D p = e.getPoint();
            Layout<V,E> layout = vv.getModel().getGraphLayout();
            GraphElementAccessor<V,E> pickSupport = vv.getPickSupport();
            if(pickSupport != null) {
                final V vertex = pickSupport.getVertex(layout, p.getX(), p.getY());
                if (vertex != null && startVertex != null) {
                    Graph<V, E> graph =
                            (Graph<V, E>) vv.getGraphLayout().getGraph();
                    boolean okToAdd = true;
                    E edge = edgeFactory.create();
                    if (edgeChecker != null) {
                        okToAdd = edgeChecker.checkEdge(graph, vv, edge,
                                startVertex, vertex, edgeIsDirected);
                    }
                    if (okToAdd) {
                        graph.addEdge(edge, startVertex, vertex, edgeIsDirected);
                    }
                    vv.repaint();
                }
            }
            startVertex = null;
            down = null;
            edgeIsDirected = EdgeType.UNDIRECTED;
            vv.removePostRenderPaintable(edgePaintable);
            vv.removePostRenderPaintable(arrowPaintable);
        }
    }

    /**
     * If startVertex is non-null, stretch an edge shape between
     * startVertex and the mouse pointer to simulate edge creation
     */
    public void mouseDragged(MouseEvent e) {
        if(checkModifiers(e)) {
            if(startVertex != null) {
                transformEdgeShape(down, e.getPoint());
                if(edgeIsDirected == EdgeType.DIRECTED) {
                    transformArrowShape(down, e.getPoint());
                }
            }
            VisualizationViewer<V,E> vv =
                (VisualizationViewer<V,E>)e.getSource();
            vv.repaint();
        }
    }
    
    /**
     * code lifted from PluggableRenderer to move an edge shape into an
     * arbitrary position
     */
    private void transformEdgeShape(Point2D down, Point2D out) {
        float x1 = (float) down.getX();
        float y1 = (float) down.getY();
        float x2 = (float) out.getX();
        float y2 = (float) out.getY();

        AffineTransform xform = AffineTransform.getTranslateInstance(x1, y1);
        
        float dx = x2-x1;
        float dy = y2-y1;
        float thetaRadians = (float) Math.atan2(dy, dx);
        xform.rotate(thetaRadians);
        float dist = (float) Math.sqrt(dx*dx + dy*dy);
        xform.scale(dist / rawEdge.getBounds().getWidth(), 1.0);
        edgeShape = xform.createTransformedShape(rawEdge);
    }
    
    private void transformArrowShape(Point2D down, Point2D out) {
        float x1 = (float) down.getX();
        float y1 = (float) down.getY();
        float x2 = (float) out.getX();
        float y2 = (float) out.getY();

        AffineTransform xform = AffineTransform.getTranslateInstance(x2, y2);
        
        float dx = x2-x1;
        float dy = y2-y1;
        float thetaRadians = (float) Math.atan2(dy, dx);
        xform.rotate(thetaRadians);
        arrowShape = xform.createTransformedShape(rawArrowShape);
    }

    public void setEdgeChecker(EdgeChecker<V, E> edgeChecker) {
        this.edgeChecker = edgeChecker;
    }

    public void setVertexChecker(VertexChecker<V, E> vertexChecker) {
        this.vertexChecker = vertexChecker;
    }
    
    /**
     * Used for the edge creation visual effect during mouse drag
     */
    class EdgePaintable implements VisualizationServer.Paintable {
        
        public void paint(Graphics g) {
            if(edgeShape != null) {
                Color oldColor = g.getColor();
                g.setColor(Color.black);
                ((Graphics2D)g).draw(edgeShape);
                g.setColor(oldColor);
            }
        }
        
        public boolean useTransform() {
            return false;
        }
    }
    
    /**
     * Used for the directed edge creation visual effect during mouse drag
     */
    class ArrowPaintable implements VisualizationServer.Paintable {
        
        public void paint(Graphics g) {
            if(arrowShape != null) {
                Color oldColor = g.getColor();
                g.setColor(Color.black);
                ((Graphics2D)g).fill(arrowShape);
                g.setColor(oldColor);
            }
        }
        
        public boolean useTransform() {
            return false;
        }
    }



    
    /**
     * Use with EditingCheckingGraphMousePlugin to allow checking of an vertex prior
     * to graph addition. Note that the VisualizationViewer parameter is furnished
     * to allow for interactivity with the user.
     * @author Dr. Greg Bernstein
     */
    public interface VertexChecker<V, E> {

        boolean checkVertex(Graph<V, E> g, VisualizationViewer<V, E> vv, V vertex);
    }

    /**
     * Use with EditingCheckingGraphMousePlugin to allow checking of an edge prior
     * to graph addition. Note that the VisualizationViewer parameter is furnished
     * to allow for interactivity with the user.
     * @author Dr. Greg M. Bernstein
     */
    public interface EdgeChecker<V, E> {

        boolean checkEdge(Graph<V, E> g, VisualizationViewer<V, E> vv, E edge, V start, V end, EdgeType dir);
    }
}
