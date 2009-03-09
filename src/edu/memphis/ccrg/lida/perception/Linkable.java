package edu.memphis.ccrg.lida.perception;

public interface Linkable {

	void excite(double amount);
	void setSelectionThreshold(double threshold);
	
	double getMinActivation();
	double getMaxActivation();
	void setMaxActivation(double amount);
	void setMinActivation(double amount);
	
	double getDefaultMinActivation();
	double getDefaultMaxActivation();
	double getCurrentActivation();
	String getLabel();
	
	
	long getID();
	
	
}
