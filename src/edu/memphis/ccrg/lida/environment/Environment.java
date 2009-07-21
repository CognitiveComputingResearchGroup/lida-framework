package edu.memphis.ccrg.lida.environment;

import edu.memphis.ccrg.lida.actionselection.ActionContent;

public interface Environment {

	void receiveBehaviorContent(ActionContent content);

	void resetEnvironment();

}
