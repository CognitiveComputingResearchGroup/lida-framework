package alifeagent.environment.operations;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.alife.elements.ALifeObject;
import edu.memphis.ccrg.alife.world.ALifeWorld;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

public class FleeOperation extends MoveAgentOperation {

	private static final Logger logger = Logger.getLogger(FleeOperation.class.getCanonicalName());
	private static final char[] directions = {'N', 'E', 'S', 'W'};
	
	@Override
	public Object performOperation(ALifeWorld world, ALifeObject subject,
			ALifeObject[] objects, Object... params) {
		char subjectDirection = (Character) subject.getAttribute("direction");
		
		int newDirection = 0;
		int randomTurn = (int) (Math.random() * 3) + 1;
		switch(subjectDirection){
			case 'N':
				newDirection = 0;
				break;
			case 'E':
				newDirection = 1;
				break;
			case 'S':
				newDirection = 2;
				break;
			case 'W':
				newDirection = 3;
				break;		
		}
		newDirection = (newDirection + randomTurn) % 4;
		subject.setAttribute("direction", directions[newDirection]);
		logger.log(Level.FINE, "agent flees",TaskManager.getCurrentTick());
		return super.performOperation(world, subject, objects, params);
	}

}
