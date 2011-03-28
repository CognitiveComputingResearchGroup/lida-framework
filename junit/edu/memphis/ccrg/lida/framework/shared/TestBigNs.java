package edu.memphis.ccrg.lida.framework.shared;

import java.util.Random;

public class TestBigNs {

	public static void main(String[] args) {
		new TestBigNs().test1();
	}
	
	private NodeStructure ns;
	private static LidaElementFactory factory = LidaElementFactory.getInstance();
	private int idCounter = 0;
	private int categoryIdCounter = 0;
	private Random random;
	
	public void test1(){
		int seed = 23434535;
		random = new Random(seed);
		ns = new NodeStructureImpl();
		
		int nodes = 10000;	
		double nodeLinkRatio = 5.0;
		
		int links = (int)(nodes / nodeLinkRatio);
		System.out.println("Creating NS with " + nodes + " nodes, " + links + " links");
		
		addSomeNodes(nodes);
		addSomeLinks(links);
		System.out.println("Copying NS\n");
		
		long start = System.currentTimeMillis();
		ns.copy();
		long finish = System.currentTimeMillis();
		System.out.println("time: " + (finish - start) + " ms");
		System.out.println("ms per linkable : " + (finish - start) / (1.0* ns.getLinkableCount()));
		System.out.println("total linkables " + ns.getLinkableCount());
	}
	
	public void addSomeNodes(int num){
		for(int i = 0; i < num; i++){
			Node foo = factory.getNode();
			foo.setId(idCounter++);
			ns.addDefaultNode(foo);
		}
	}
	
	public void addSomeLinks(int num){
		for(int i = 0; i < num; i++){
			int randomSource = random.nextInt(idCounter);
			int randomSink = random.nextInt(idCounter);
			while(randomSink == randomSource){
				randomSink = random.nextInt(idCounter);
			}
			Node source = ns.getNode(randomSource);
			Node sink = ns.getNode(randomSink);
			LinkCategoryNode lcn = new LinkCategoryNode();
			lcn.setId(categoryIdCounter++);
			ns.addDefaultLink(source, sink, lcn, 1.0, -1.0);
		}
	}

}
