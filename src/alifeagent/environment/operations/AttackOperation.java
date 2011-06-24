package alifeagent.environment.operations;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.alife.elements.ALifeObject;
import edu.memphis.ccrg.alife.elements.Cell;
import edu.memphis.ccrg.alife.opreations.WorldOperation;
import edu.memphis.ccrg.alife.world.ALifeWorld;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

public class AttackOperation implements WorldOperation {

	private static final Logger logger = Logger.getLogger(AttackOperation.class.getCanonicalName());
	private static final double DEFAULT_ATTACK_HEALTH_DECREASE = -.3;
	String soughtObjectName = "agent";
	
	@Override
	public Object performOperation(ALifeWorld world, ALifeObject subject,
								   ALifeObject[] objects, Object... params) {
		
		String soughtObjectName = (String) params[0];
		Cell currentCell = (Cell) subject.getContainer();
		Set<ALifeObject> cellObjects = currentCell.getObjects();
		for(ALifeObject obj: cellObjects){
			if(soughtObjectName.equals(obj.getName())){				
				obj.increaseHealth(DEFAULT_ATTACK_HEALTH_DECREASE);
				logger.log(Level.INFO, "agent attacked!",TaskManager.getCurrentTick());
				return obj;
			}
		}
		return null;
	}

}
