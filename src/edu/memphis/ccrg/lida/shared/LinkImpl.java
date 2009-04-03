/**
 * 
 */
package edu.memphis.ccrg.lida.shared;

/**
 * @author Javier Snaider
 *
 */
public class LinkImpl implements Link {
	
	private Linkable source;
	private Linkable sink;
	private LinkType type;
	private String label;

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.shared.Link#copy(edu.memphis.ccrg.lida.shared.Link)
	 */
	public Link copy(Link l) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.shared.Link#getSink()
	 */
	public Linkable getSink() {
		return sink;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.shared.Link#getSource()
	 */
	public Linkable getSource() {
		return source;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.shared.Link#getType()
	 */
	public LinkType getType() {
		// TODO Auto-generated method stub
		return type;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.shared.Linkable#getLabel()
	 */
	public String getLabel() {
		// TODO Auto-generated method stub
		return label;
	}

	/**
	 * @param label
	 * @param sink
	 * @param source
	 * @param type
	 */
	public LinkImpl(String label, Linkable sink, Linkable source, LinkType type) {
		super();
		this.label = label;
		this.sink = sink;
		this.source = source;
		this.type = type;
	}

	/**
	 * @param sink
	 * @param source
	 * @param type
	 */
	public LinkImpl(Linkable sink, Linkable source, LinkType type) {
		super();
		this.sink = sink;
		this.source = source;
		this.type = type;
	}

	/**
	 * 
	 */
	public LinkImpl() {
	}

	public void setSink(Linkable sink) {
		this.sink=sink;
		
	}

	public void setSource(Linkable source) {
		this.source=source;
		
	}

	public void setType(LinkType type) {
		this.type=type;
		
	}
	
	

}
