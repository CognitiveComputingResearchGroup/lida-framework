package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class WeakHashSet<E> extends AbstractSet<E> implements Set<E>, java.io.Serializable{
	
	private static final long serialVersionUID = -4148697472689300159L;
	
	private Map<E, Object> map = new WeakHashMap<E, Object>();
	
	private static final Object PRESENT = new Object();
	
	public boolean add(E o){
		return map.put(o, PRESENT)==null;
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
		return map.remove(o)==PRESENT;
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
