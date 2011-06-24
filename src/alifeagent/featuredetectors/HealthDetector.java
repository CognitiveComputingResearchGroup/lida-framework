package alifeagent.featuredetectors;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.tasks.DetectionAlgorithm;
import edu.memphis.ccrg.lida.pam.tasks.MultipleDetectionAlgorithm;

public class HealthDetector extends MultipleDetectionAlgorithm implements DetectionAlgorithm {

	private PamLinkable goodHealth;
	private PamLinkable fairHealth;
	private PamLinkable badHealth;
	private final String modality = "";
	private Map<String, Object> detectorParams = new HashMap<String, Object>();
	
	@Override
	public void init(){
		super.init();
		goodHealth = pamNodeMap.get("goodHealth");
		fairHealth = pamNodeMap.get("fairHealth");
		badHealth = pamNodeMap.get("badHealth");
		detectorParams.put("mode","health");
	}
	
	@Override
	public void detectLinkables() {
		double healthValue = (Double)sensoryMemory.getSensoryContent(modality, detectorParams);

		if(healthValue > 0.66){
			pam.receiveExcitation(goodHealth, 1.0);
		}else if(healthValue > 0.33){
			pam.receiveExcitation(fairHealth, 1.0);
		}else{
			pam.receiveExcitation(badHealth, 1.0);
		}
		
	}

}
