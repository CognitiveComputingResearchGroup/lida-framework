package alifeagent.featuredetectors;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;

public class FairHealthDetector extends BasicDetectionAlgorithm {

    private final String modality = "";
    private Map<String, Object> detectorParams = new HashMap<String, Object>();

    @Override
    public void init() {
        super.init();
        detectorParams.put("mode", "health");
    }

    @Override
    public double detect() {
        double healthValue = (Double) sensoryMemory.getSensoryContent(modality, detectorParams);
        double activation = 0.0;
        if (healthValue < 0.66 && healthValue > 0.33) {
            activation = 1.0;
        }
        return activation;
    }
}
