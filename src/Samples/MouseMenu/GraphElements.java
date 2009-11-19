/*
 * GraphElements.java
 *
 * Created on March 21, 2007, 9:57 AM
 *
 * Copyright March 21, 2007 Grotto Networking
 *
 */

package Samples.MouseMenu;

import org.apache.commons.collections15.Factory;

/**
 *
 * @author Dr. Greg M. Bernstein
 */
public class GraphElements {
    
    /** Creates a new instance of GraphElements */
    public GraphElements() {
    }
    
    public static class MyVertex {
        private String name;
        private boolean packetSwitchCapable;
        private boolean tdmSwitchCapable;
        
        public MyVertex(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isPacketSwitchCapable() {
            return packetSwitchCapable;
        }

        public void setPacketSwitchCapable(boolean packetSwitchCapable) {
            this.packetSwitchCapable = packetSwitchCapable;
        }

        public boolean isTdmSwitchCapable() {
            return tdmSwitchCapable;
        }

        public void setTdmSwitchCapable(boolean tdmSwitchCapable) {
            this.tdmSwitchCapable = tdmSwitchCapable;
        }
        
        public String toString() {
            return name;
        }
    }
    
    public static class MyEdge {
        private double capacity;
        private double weight;
        private String name;

        public MyEdge(String name) {
            this.name = name;
        }
        public double getCapacity() {
            return capacity;
        }

        public void setCapacity(double capacity) {
            this.capacity = capacity;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }             
        
        public String toString() {
            return name;
        }
    }
    
    // Single factory for creating Vertices...
    public static class MyVertexFactory implements Factory<MyVertex> {
        private static int nodeCount = 0;
        private static boolean defaultPSC = false;
        private static boolean defaultTDM = true;
        private static MyVertexFactory instance = new MyVertexFactory();
        
        private MyVertexFactory() {            
        }
        
        public static MyVertexFactory getInstance() {
            return instance;
        }
        
        public GraphElements.MyVertex create() {
            String name = "Node" + nodeCount++;
            MyVertex v = new MyVertex(name);
            v.setPacketSwitchCapable(defaultPSC);
            v.setTdmSwitchCapable(defaultTDM);
            return v;
        }        

        public static boolean isDefaultPSC() {
            return defaultPSC;
        }

        public static void setDefaultPSC(boolean aDefaultPSC) {
            defaultPSC = aDefaultPSC;
        }

        public static boolean isDefaultTDM() {
            return defaultTDM;
        }

        public static void setDefaultTDM(boolean aDefaultTDM) {
            defaultTDM = aDefaultTDM;
        }
    }
    
    // Singleton factory for creating Edges...
    public static class MyEdgeFactory implements Factory<MyEdge> {
        private static int linkCount = 0;
        private static double defaultWeight;
        private static double defaultCapacity;

        private static MyEdgeFactory instance = new MyEdgeFactory();
        
        private MyEdgeFactory() {            
        }
        
        public static MyEdgeFactory getInstance() {
            return instance;
        }
        
        public GraphElements.MyEdge create() {
            String name = "Link" + linkCount++;
            MyEdge link = new MyEdge(name);
            link.setWeight(defaultWeight);
            link.setCapacity(defaultCapacity);
            return link;
        }    

        public static double getDefaultWeight() {
            return defaultWeight;
        }

        public static void setDefaultWeight(double aDefaultWeight) {
            defaultWeight = aDefaultWeight;
        }

        public static double getDefaultCapacity() {
            return defaultCapacity;
        }

        public static void setDefaultCapacity(double aDefaultCapacity) {
            defaultCapacity = aDefaultCapacity;
        }
        
    }

}
