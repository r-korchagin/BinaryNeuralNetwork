package info.binarynetwork.core.impl;

import info.binarynetwork.core.interfaces.NetworkStep;
import info.binarynetwork.core.interfaces.NetworkStepExecutor;
import info.binarynetwork.objects.CompareData;
import info.binarynetwork.objects.neuralElement;

public class NetworkStepImpl implements NetworkStep {

	private NetworkStepExecutor executor;

	public NetworkStepExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(NetworkStepExecutor executor) {
		this.executor = executor;
	}

	public float[] execStep(neuralElement[] famiy, int familySize, CompareData data) {
		float[] result = executor.runStep(famiy, familySize, data);
		return result;
	}

}
