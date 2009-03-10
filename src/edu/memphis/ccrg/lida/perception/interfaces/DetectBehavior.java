package edu.memphis.ccrg.lida.perception.interfaces;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;

public interface DetectBehavior {

	double getExcitation(String label, SensoryContent sc);
	
	void setDetectThreshold(double d);
	void setExcitationAmount(double d);

}
