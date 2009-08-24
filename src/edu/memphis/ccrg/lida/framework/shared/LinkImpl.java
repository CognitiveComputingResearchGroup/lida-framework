package edu.memphis.ccrg.lida.framework.shared;

/**
 * 
 * @author Ryan McCall
 */
public class LinkImpl implements Link {

	private Linkable sink;
	private Linkable source;
	private String ids;
	private LinkType type;

	public LinkImpl(Linkable source, Linkable sink, LinkType type, String ids) {
		this.source = source;
		this.sink = sink;
		this.type = type;
		this.ids = ids;
		updateIds();
	}

	public LinkImpl(Link l) {
		sink = l.getSink();
		source = l.getSource();
		type = l.getType();
		ids = l.getIds();
		updateIds();
	}

	public LinkImpl() {
	}

	public LinkImpl copy(LinkImpl l) {
		return new LinkImpl(l);
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

		if (ids.equals(other)) {
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

	public void setIds(String id) {
		this.ids = id;
	}
	
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
		ids = "L( " + ((source!=null)?source.getIds():"") + " : " + ((sink!=null)?sink.getIds():"") + " : " + type + " )";
	}
}
