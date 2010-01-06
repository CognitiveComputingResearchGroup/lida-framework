package samples.Basic;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.converters.collections.MapConverter;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.LinkType;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeFactory;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;

public class XstreamTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		XStream xstream = new XStream();
		xstream.alias("node" , NodeImpl.class);
		xstream.alias("pamNode" , PamNodeImpl.class);
		xstream.alias("map" , ConcurrentHashMap.class);
		xstream.alias("link" , LinkImpl.class);
		
		xstream.registerLocalConverter(NodeImpl.class , "refNode" , new AbstractSingleValueConverter(){
			@SuppressWarnings("unchecked")
			@Override
			public boolean canConvert(Class arg0) {
				return (PamNode.class).isAssignableFrom(arg0);
			}
			@Override
			public Object fromString(String arg0) {
				// TODO Auto-generated method stub
				return null;
			}			
			@Override
			public String toString(Object o) {
				return ((PamNode)o).getId()+"";
			}			
		});
		xstream.registerConverter(new MapConverter(xstream.getMapper()){
			@SuppressWarnings("unchecked")
			public boolean canConvert(Class type){
				return type.isAssignableFrom(ConcurrentHashMap.class);
			}
		});
		PamNode pn = new PamNodeImpl();
		Node n = new NodeImpl();
		Node n2 = new NodeImpl();
		NodeStructure ns = new NodeStructureImpl();
		
		pn.setId(55L);
		pn.setActivation(0.5);
		
		n.setId(100L);		
		n.setActivation(0.7);
		n.setReferencedNode(pn);

		n2.setId(200L);		
		n2.setActivation(0.3);
		n2.setReferencedNode(pn);

		Link link = NodeFactory.getInstance().getLink(n, n2, LinkType.SAME_LOCATION);
		ns.addNode(n);
		ns.addNode(n2);
		ns.addLink(link);
		
		Map<Long,Node> l = new ConcurrentHashMap<Long,Node>();
		l.put(n.getId(),n);
		l.put(n2.getId(),n2);
		
		
	       System.out.println(xstream.toXML(l));
	       System.out.println(xstream.toXML(link));
	       System.out.println(xstream.toXML(ns));
		
	}

}
