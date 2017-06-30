package info.binarynetwork.core.impl;

import info.binarynetwork.core.interfaces.NetworkCore;
import info.binarynetwork.core.interfaces.NetworkStepExecutor;
import info.binarynetwork.objects.CompareData;
import info.binarynetwork.objects.neuralElement;

public class NetworkStepSingleCP implements NetworkStepExecutor {

	private NetworkCore core;

	public NetworkCore getCore() {
		return core;
	}

	public void setCore(NetworkCore core) {
		this.core = core;
	}

	public float[] runStep(neuralElement[] famiy, int familySize, CompareData data) {
		float[] stepResult = new float[familySize];
		for (int i = 0; i < familySize; i++) {
			stepResult[i] = core.compareEl(famiy[i], data);
			System.out.println("CompareEl " + i + " Result " + stepResult[i]);
		}
		return stepResult;
	}

}
