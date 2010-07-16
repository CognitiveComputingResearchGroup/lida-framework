package edu.memphis.ccrg.lida.framework.shared;

import java.util.Map;

/**
 * 
 * @author Ryan McCall
 */
public class LinkImpl extends LearnableActivatibleImpl implements Link {

	private Linkable sink;
	private Linkable source;
	private String ids;
	private LinkType type;
	private Link referencedLink = null;

	public LinkImpl(Linkable source, Linkable sink, LinkType type) {
		this.source = source;
		this.sink = sink;
		this.type = type;
		updateIds();
	}

	public LinkImpl(Link l) {
		sink = l.getSink();
		source = l.getSource();
		type = l.getType();
		ids = l.getIds();
		referencedLink = l.getReferencedLink();
		updateIds();
	}

	public LinkImpl() {
	}

	public LinkImpl copy() {
		return new LinkImpl(this);
	}

	/**
	 * This method compares this LinkImp with any kind of Link. Two Links are
	 * equals if and only if their sources and sinks are equals and are Links of
	 * the same type.
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Link)) {
			return false;
		}
		Link other = (Link) obj;

		if (other.getType() != type) {
			return false;
		}

		if (ids.equals(other.getIds())) {
			return true;
		}
		return false;
	}

	public String getIds() {
		return ids;
	}

	public String getLabel() {
		return "Link: " + ids;
	}

	public Linkable getSink() {
		return sink;
	}

	public Linkable getSource() {
		return source;
	}

	public LinkType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + ((source == null) ? 0 : source.hashCode())
				+ ((sink == null) ? 0 : sink.hashCode());
		hash = hash * 31 + (type == null ? 0 : type.hashCode());
		return hash;
	}

//	public void setIds(String id) {
//		//The ids can not be set
//	}
	
	public String getId(){
		return ids;
	}

	public void setSink(Linkable sink) {
		this.sink = sink;
		updateIds();
	}

	public void setSource(Linkable source) {
		this.source = source;
		updateIds();
	}

	public void setType(LinkType type) {
		this.type = type;
		updateIds();
	}

	@Override
	public String toString() {
		return getLabel();
	}

	private void updateIds() {
		ids = "L(" + ((source!=null)?source.getIds():"") + ":" + ((sink!=null)?sink.getIds():"") + ":" + type + ")";
	}

	public Link getReferencedLink() {
		return referencedLink;
	}

	public void setReferencedLink(Link l) {
		referencedLink = l;
	}
	
	public void init(Map<String, Object> params) {
		// TODO Auto-generated method stub
	}
	

}
