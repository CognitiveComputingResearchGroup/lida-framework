/*
 * BasicDirectedGraph.java
 *
 * Created on March 5, 2007, 8:50 PM
 *
 * Copyright March 5, 2007 Grotto Networking
 *
 */

package Samples.Basic;

import edu.uci.ics.jung.algorithms.flows.EdmondsKarpMaxFlow;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;

import edu.uci.ics.jung.graph.util.EdgeType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;




/**
 *
 * @author Dr. Greg M. Bernstein
 */
public class BasicDirectedGraph {
    static int edgeCount = 0;   // This works with the inner MyEdge class
                                // since inner classes cannot have static members
    DirectedGraph<MyNode, MyLink> g;
    MyNode n1, n2, n3, n4, n5;
    
    
    /** Creates a new instance of BasicDirectedGraph */
    public BasicDirectedGraph() {       
    }
    /** Constructs an example directed graph with our vertex and edge classes */
    public void constructGraph() {
        g = new DirectedSparseMultigraph<MyNode, MyLink>();
        // Create some MyNode objects to use as vertices
        n1 = new MyNode(1); n2 = new MyNode(2); n3 = new MyNode(3);  
        n4 = new MyNode(4); n5 = new MyNode(5); // note n1-n5 declared elsewhere.
        // Add some directed edges along with the vertices to the graph
        g.addEdge(new MyLink(2.0, 48),n1, n2, EdgeType.DIRECTED); // This method
        g.addEdge(new MyLink(2.0, 48),n2, n3, EdgeType.DIRECTED);
        g.addEdge(new MyLink(3.0, 192), n3, n5, EdgeType.DIRECTED); 
        g.addEdge(new MyLink(2.0, 48), n5, n4, EdgeType.DIRECTED); // or we can use
        g.addEdge(new MyLink(2.0, 48), n4, n2); // In a directed graph the
        g.addEdge(new MyLink(2.0, 48), n3, n1); // first node is the source 
        g.addEdge(new MyLink(10.0, 48), n2, n5);// and the second the destination
    }
    
    public void calcUnweightedShortestPath() {
        DijkstraShortestPath<MyNode,MyLink> alg = new DijkstraShortestPath(g);
        List<MyLink> l = alg.getPath(n1, n4);
        System.out.println("The shortest unweighted path from " + n1 + " to " + n4 + " is:");
        System.out.println(l.toString());
    }
    
    public void calcWeightedShortestPath() {
        Transformer<MyLink, Double> wtTransformer = new Transformer<MyLink,Double>() {
            public Double transform(MyLink link) {
                return link.weight;
            }
        };
        DijkstraShortestPath<MyNode,MyLink> alg = new DijkstraShortestPath(g, wtTransformer);
        List<MyLink> l = alg.getPath(n1, n4);
        Number dist = alg.getDistance(n1, n4);
        System.out.println("The shortest weighted path from " + n1 + " to " + n4 + " is:");
        System.out.println(l.toString());
        System.out.println("and the length of the path is: " + dist);
    }
    
    
    public void calcMaxFlow() {
        // For the Edmonds-Karp Max Flow algorithm we have the following
        // parameters: the graph, source vertex, sink vertex, transformer of edge capacities,
        // map of edge flow values.
        
        Transformer<MyLink, Double> capTransformer = new Transformer<MyLink, Double>(){
          public Double transform(MyLink link)  {
              return link.capacity;
          }
        };
        Map<MyLink, Double> edgeFlowMap = new HashMap<MyLink, Double>();
        // This Factory produces new edges for use by the algorithm
        Factory<MyLink> edgeFactory = new Factory<MyLink>() {
            public MyLink create() {
                return new MyLink(1.0, 1.0);
            }
        };
        
        EdmondsKarpMaxFlow<MyNode, MyLink> alg = new EdmondsKarpMaxFlow(g,n2, n5, capTransformer, edgeFlowMap,
                edgeFactory);
        alg.evaluate(); // If you forget this you won't get an error but you will get a wrong answer!!!
        System.out.println("The max flow is: " + alg.getMaxFlow());
        System.out.println("The edge set is: " + alg.getMinCutEdges().toString());
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BasicDirectedGraph myApp = new BasicDirectedGraph();
        myApp.constructGraph();
        System.out.println(myApp.g.toString());
        myApp.calcUnweightedShortestPath();
        myApp.calcWeightedShortestPath();
        myApp.calcMaxFlow();    
    }
    
    class MyNode {
        int id;
        public MyNode(int id) {
            this.id = id;
        }
        public String toString() {
            return "V"+id;
        }        
    }
    
    class MyLink {
        double capacity;
        double weight;
        int id;
        
        public MyLink(double weight, double capacity) {
            this.id = edgeCount++;
            this.weight = weight;
            this.capacity = capacity;
        } 

        public String toString() {
            return "E"+id;
        }
        
    }
    
}
