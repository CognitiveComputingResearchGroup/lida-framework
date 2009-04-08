package edu.memphis.ccrg.lida.shared;

import java.util.HashMap;
import java.util.Map;

public class LinkFactory {
	
	private static LinkFactory instance;
	private String LinkDefaultName;
	private Map<String, String> linkClass = new HashMap<String, String>();

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
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return l;
	}

}
