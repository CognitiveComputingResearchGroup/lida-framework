package edu.memphis.ccrg.lida.framework.shared;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;

public class TestBigNs {

	public static void main(String[] args) {
		new TestBigNs().test1();
	}
	
	private NodeStructure ns;
	private static ElementFactory factory = ElementFactory.getInstance();
	private int idCounter = 0;
	private int categoryIdCounter = 0;
	private Random random;
	private List<LinkCategory> linkCategoryPool = new ArrayList<LinkCategory>();
	
	public void test1(){
		int seed = 23434535;
		random = new Random(seed);
		ns = new NodeStructureImpl();
		
		int nodes = 10000;	
		int linkCategories = 1000;
		double nodeLinkRatio = 5.0;
		
		int links = (int)(nodes / nodeLinkRatio);
		System.out.println("Creating NS with " + nodes + " nodes, " + links + " links");
		
		createLinkCategoryPool(linkCategories);
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
	
	private void createLinkCategoryPool(int linkCategories) {
		for(int i = 0; i < linkCategories; i++){
			PamNodeImpl temp = new PamNodeImpl();
			temp.setId(categoryIdCounter++);
			linkCategoryPool.add(temp);
		}
		
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
			int randomCategory = random.nextInt(linkCategoryPool.size());
			PamNode lcn = (PamNode) linkCategoryPool.get(randomCategory);
			lcn.setId(categoryIdCounter++);
			ns.addDefaultLink(source, sink, lcn, 1.0, -1.0);
		}
	}

}
