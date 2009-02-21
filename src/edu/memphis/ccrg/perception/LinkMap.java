package edu.memphis.ccrg.perception;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LinkMap {
	private Map<Linkable, Set<Link>> linkMap;
	
	public LinkMap(){
		linkMap = new HashMap<Linkable, Set<Link>>();
	}//public LinkMap()
	
	public void addLinkSet(Set<Link> links){
		for(Link l: links)
			addLink(l);
	}//public void addLinkSet(Set<Link> links)

	public boolean addLink(Link l){
		boolean result1 = false;
		boolean result2 = false;
		Linkable end = l.getSource();
		Set<Link> tempLinks = linkMap.get(end);
		if(tempLinks == null){
			tempLinks=new HashSet<Link>();
			linkMap.put(end, tempLinks);
		}
		result1 = tempLinks.add(l);
		
		end = l.getSink();
		tempLinks = linkMap.get(end);
		if(tempLinks == null){
			tempLinks = new HashSet<Link>();
			linkMap.put(end, tempLinks);
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
	
	public Set<Link> getLinks(Linkable l){
		return linkMap.get(l);	
	}//public Set<Link> getLinks(Node n)
	
	public Set<Link> getLinks(Linkable NorL, LinkType type){
		Set<Link> result = linkMap.get(NorL);
		if(result != null){
			for(Link l: result){
				if(l.getType() != type)//remove links that don't match specified type
					result.remove(l);
			}//for each link
		}//result != null
		return result;
	}//public Set<Link> getLinks(Node n, LinkType type)
 	
	public void deleteLinkable(Linkable n){
		Set<Link> tempLinks = linkMap.get(n);
		Set<Link> otherLinks;
		Linkable other;
		
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
		linkMap.remove(n);//finally remove the linkable and its links		
	}//public void deleteNode(Linkable n)
	
}//public class LinkMap
