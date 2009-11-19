/*
 * EditingGraphViewer.java
 *
 * Created on March 8, 2007, 7:49 PM; Updated May 29, 2007
 *
 * Copyright March 8, 2007 Grotto Networking
 */

package Samples.CheckingMouse;

import Samples.CheckingMouse.EditingCheckingGraphMousePlugin.EdgeChecker;
import Samples.CheckingMouse.EditingCheckingGraphMousePlugin.VertexChecker;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.GraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import org.apache.commons.collections15.Factory;


/**
 *
 * @author Dr. Greg M. Bernstein
 */
public class EditingCheckingGraphViewer {
    Graph<Integer, String> g;
    int nodeCount, edgeCount;
    Factory <Integer> vertexFactory;
    Factory<String> edgeFactory;
    MyVertexChecker vCheck;
    MyEdgeChecker eCheck;
    
    /** Creates a new instance of SimpleGraphView */
    public EditingCheckingGraphViewer() {
        // Graph<V, E> where V is the type of the vertices and E is the type of the edges
        g = new SparseGraph<Integer, String>();
        nodeCount = 0; edgeCount = 0;
        vertexFactory = new Factory<Integer>() { // My vertex factory
            public Integer create() {
                return nodeCount++;
            }
        };
        edgeFactory = new Factory<String>() { // My edge factory
            public String create() {
                return "E"+edgeCount++;
            }
        };
        vCheck = new MyVertexChecker();
        eCheck = new MyEdgeChecker();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EditingCheckingGraphViewer sgv = new EditingCheckingGraphViewer();
        // Layout<V, E>, VisualizationViewer<V,E>
        Layout<Integer, String> layout = new StaticLayout(sgv.g);
        layout.setSize(new Dimension(300,300));
        VisualizationViewer<Integer,String> vv = new VisualizationViewer<Integer,String>(layout);
        vv.setPreferredSize(new Dimension(350,350));
        // Show vertex and edge labels
        vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
        vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());
        // Create a graph mouse and add it to the visualization viewer
        // Our Vertices are going to be Integer objects so we need an Integer factory
        EditingModalGraphMouse2 gm = new EditingModalGraphMouse2(vv.getRenderContext(), 
                 sgv.vertexFactory, sgv.edgeFactory); 
        EditingCheckingGraphMousePlugin plugin = new EditingCheckingGraphMousePlugin(sgv.vertexFactory,
                sgv.edgeFactory);
        GraphMousePlugin oldPlugin = gm.getEditingPlugin(); //Remove current plugin
        gm.remove(oldPlugin);
        plugin.setVertexChecker(sgv.vCheck);
        plugin.setEdgeChecker(sgv.eCheck);
        gm.setEditingPlugin(plugin);
        vv.setGraphMouse(gm);

        
        JFrame frame = new JFrame("Editing and Checking Graph Viewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(vv);
        
        // Let's add a menu for changing mouse modes
        JMenuBar menuBar = new JMenuBar();
        JMenu modeMenu = gm.getModeMenu();
        modeMenu.setText("Mouse Mode");
        modeMenu.setIcon(null); // I'm using this in a main menu
        modeMenu.setPreferredSize(new Dimension(80,20)); // Change the size so I can see the text
        
        menuBar.add(modeMenu);
        frame.setJMenuBar(menuBar);
        gm.setMode(ModalGraphMouse.Mode.EDITING); // Start off in editing mode
        frame.pack();
        frame.setVisible(true);  
        
    }
    
    public class MyVertexChecker implements VertexChecker<Integer,String> {

        public boolean checkVertex(Graph<Integer,String> g, VisualizationViewer<Integer,String> vv, Integer v) {
            // Will test to see if the graph has more that 5 vertices
            if (g.getVertexCount() < 5) {
                return true;
            } else {
                JOptionPane.showMessageDialog(vv, "This version only supports 5 vertices!",
                        "Vertex Check", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }
    
    public class MyEdgeChecker implements EdgeChecker<Integer, String> {

        public boolean checkEdge(Graph<Integer, String> g, VisualizationViewer<Integer, String> vv, String edge, Integer start, Integer end, EdgeType dir) {
            if (dir == EdgeType.DIRECTED) {
                JOptionPane.showMessageDialog(vv, "No Directed edges allowed Today!",
                        "Edge Check", JOptionPane.ERROR_MESSAGE);
                return false;
            } else if (g.findEdge(start, end) != null) {
                JOptionPane.showMessageDialog(vv, "No parallel edges allowed in this graph!",
                        "Edge Check", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;

        }
        
    }
    
}
