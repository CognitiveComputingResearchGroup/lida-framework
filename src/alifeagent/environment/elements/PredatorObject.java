package alifeagent.environment.elements;

import edu.memphis.ccrg.alife.elements.ALifeObjectImpl;
import edu.memphis.ccrg.alife.world.ALifeWorld;

public class PredatorObject extends ALifeObjectImpl {

	@Override
	public void updateState(ALifeWorld world) {
		world.performOperation("move", this, null);	
		world.performOperation("attack", this, null,"agent");		
	}

}
