package alifeagent.environment.operations;

import edu.memphis.ccrg.alife.elements.ALifeObject;
import edu.memphis.ccrg.alife.opreations.WorldOperation;
import edu.memphis.ccrg.alife.world.ALifeWorld;

public class SeeObjectsInCell implements WorldOperation {

	@Override
	public Object performOperation(ALifeWorld world, ALifeObject subject,
			ALifeObject[] object, Object... params) {
		int x = (Integer) params[0];
		int y = (Integer) params[1];
		return world.getCell(x, y).getObjects();
	}
}
