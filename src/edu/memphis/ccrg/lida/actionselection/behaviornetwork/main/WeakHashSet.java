package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class WeakHashSet<E> extends AbstractSet<E>{
	
	private Map<E, Boolean> map = new WeakHashMap<E, Boolean>();
	
	public boolean add(E o){
		return map.put(o, true);
	}
	
	public boolean addAll(Collection<? extends E> c){
		boolean modified = false;
		Iterator<? extends E> e = c.iterator();
		while (e.hasNext()) {
		    if (add(e.next()))
		    	modified = true;
		}
		return modified;
	}

	public boolean contains(Object o){
		return map.containsKey(o);
	}
	
	public boolean isEmpty(){
		return map.isEmpty();
	}
	
	public boolean remove(Object o){
		return map.remove(o);
	}
	
	public void clear(){
		map.clear();
	}
	
	public int size(){
		return map.size();
	}

	public Iterator<E> iterator() {
		return map.keySet().iterator();
	}

}
