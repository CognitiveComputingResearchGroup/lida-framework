package edu.memphis.ccrg.lida.framework.shared;
//TODO:remove
public class LinkFactory {
	
	private static LinkFactory instance;
	private String LinkDefaultName;
	//private Map<String, String> linkClass = new HashMap<String, String>();

	public static LinkFactory getInstance() {
		if (instance == null) {
			instance = new LinkFactory();
		}
		return instance;
	}

	private LinkFactory() {
		LinkDefaultName = "edu.memphis.ccrg.lida.shared.LinkImpl";
	}
	
	public Link getLink() {
		Link l = null;
		try {
			l = (Link)Class.forName(LinkDefaultName).newInstance();


		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (ClassNotFoundException e) {
		}
		return l;
	}

}
