package edu.memphis.ccrg.perception;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LinkMap {
	
	private Map<Node, Set<Link>> linkMap;
	
	public LinkMap(){
		linkMap = new HashMap<Node, Set<Link>>();
	}//public LinkMap()
	
	public void addLinkSet(Set<Link> links){
		for(Link l: links)
			addLink(l);
	}//public void addLinkSet(Set<Link> links)

	public boolean addLink(Link l){
		boolean result1 = false;
		boolean result2 = false;
		Node n = l.getSource();
		Set<Link> tempLinks = linkMap.get(n);
		if(tempLinks == null){
			tempLinks=new HashSet<Link>();
			linkMap.put(n, tempLinks);
		}
		result1 = tempLinks.add(l);
		
		n = l.getSink();
		tempLinks = linkMap.get(n);
		if(tempLinks == null){
			tempLinks = new HashSet<Link>();
			linkMap.put(n, tempLinks);
		}
		result2 = tempLinks.add(l);
		return result1 || result2;
	}//public boolean addLink(Link l)
	
	public void deleteLink(Link l){
		Set<Link> sourceLinks = linkMap.get(l.getSource());
		Set<Link> sinkLinks = linkMap.get(l.getSink());
		if(sourceLinks != null)
			sourceLinks.remove(l);
		if(sinkLinks != null)
			sinkLinks.remove(l);		
	}//public void deleteLink(Link l)
	
	public Set<Link> getLinks(Node n){
		return linkMap.get(n);	
	}//public Set<Link> getLinks(Node n)
	
	public Set<Link> getLinks(Node n, LinkType type){
		Set<Link> result = linkMap.get(n);
		for(Link l: result){//TODO: ask Javier is this would cause a null pointer.
			if(l.getType() != type)//remove links that don't match specified type
				result.remove(l);
		}			
		return result;
	}//public Set<Link> getLinks(Node n, LinkType type)
 	
	public void deleteNode(Node n){
		Set<Link> tempLinks = linkMap.get(n);
		Set<Link> otherLinks;
		Node other;
		
		for(Link l: tempLinks){
			other = l.getSink();
			if(!other.equals(n)){		
				otherLinks = linkMap.get(other);
				if(otherLinks != null)
					otherLinks.remove(l);
			}
			
			other = l.getSource();
			if(!other.equals(n)){
				otherLinks = linkMap.get(other);
				if(otherLinks != null)
					otherLinks.remove(l);
			}						
		}//for all of the links connected to n		
		linkMap.remove(n);
	}//public void deleteNode(Node n)
	
}//public class LinkMap
