package edu.memphis.ccrg.lida.framework.shared;

import java.util.Map;

public interface Linkable {

	public abstract String getLabel();
	public abstract String getIds();
	public abstract void init(Map<String,Object> params);
}
