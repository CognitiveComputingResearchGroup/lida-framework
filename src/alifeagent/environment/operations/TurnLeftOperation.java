package alifeagent.environment.operations;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.alife.elements.ALifeObject;
import edu.memphis.ccrg.alife.opreations.WorldOperation;
import edu.memphis.ccrg.alife.world.ALifeWorld;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

public class TurnLeftOperation implements WorldOperation {
	
	private static final Logger logger = Logger.getLogger(TurnLeftOperation.class.getCanonicalName());
	private final String attributeName = "direction";
	
	@Override
	public Object performOperation(ALifeWorld world, ALifeObject subject,
			ALifeObject[] objects, Object... params) {
		char currentDirection = (Character)subject.getAttribute(attributeName);
		switch(currentDirection){
			case 'N':
				subject.setAttribute(attributeName, 'W');
				break;
			case 'W':
				subject.setAttribute(attributeName, 'S');
				break;
			case 'S':
				subject.setAttribute(attributeName, 'E');
				break;
			case 'E':
				subject.setAttribute(attributeName, 'N');
				break;
		}		
		logger.log(Level.FINE, "agent turns left",TaskManager.getCurrentTick());
		return subject;
	}

}
