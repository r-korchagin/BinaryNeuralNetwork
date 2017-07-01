package info.binarynetwork.core.impl;

import info.binarynetwork.core.interfaces.NetworkStep;
import info.binarynetwork.core.interfaces.NetworkStepExecutor;
import info.binarynetwork.interfaces.InputCompareData;
import info.binarynetwork.objects.neuralElement;

public class NetworkStepImpl implements NetworkStep {

    private NetworkStepExecutor executor;
    private InputCompareData data;

    public InputCompareData getData() {
	return data;
    }

    public void setData(InputCompareData data) {
	this.data = data;
    }

    public NetworkStepExecutor getExecutor() {
	return executor;
    }

    public void setExecutor(NetworkStepExecutor executor) {
	this.executor = executor;
    }

    public float[] execStep(neuralElement[] famiy, int familySize) {
	float[] result = executor.runStep(famiy, familySize, data.getInputData());
	return result;
    }

}
