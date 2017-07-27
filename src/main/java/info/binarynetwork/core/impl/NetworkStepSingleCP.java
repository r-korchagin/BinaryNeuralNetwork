package info.binarynetwork.core.impl;

import info.binarynetwork.core.interfaces.NetworkCore;
import info.binarynetwork.core.interfaces.NetworkStepExecutor;
import info.binarynetwork.objects.Binary32Data;
import info.binarynetwork.objects.Binary64Data;
import info.binarynetwork.objects.CompareData;
import info.binarynetwork.objects.neuralElement64;
import info.binarynetwork.objects.neuralElement32;

public class NetworkStepSingleCP implements NetworkStepExecutor {

    private NetworkCore core;

    public NetworkCore getCore() {
	return core;
    }

    public void setCore(NetworkCore core) {
	this.core = core;
    }

    public float[] runStep(neuralElement64[] famiy, int familySize, CompareData data) {
	float[] stepResult = new float[familySize];
	for (int i = 0; i < familySize; i++) {
	    stepResult[i] = core.compareEl(famiy[i], data);
	    System.out.println("CompareEl " + i + " Result " + stepResult[i]);
	}
	return stepResult;
    }

    public float[] runStep(neuralElement32[] famiy, int familySize, Binary32Data data) {
	float[] stepResult = new float[familySize];
	for (int i = 0; i < familySize; i++) {
	    stepResult[i] = core.compareEl(famiy[i], data);
	    System.out.println("CompareEl " + i + " Result " + stepResult[i]);
	}
	return stepResult;
    }

    public float[] runStep(neuralElement64[] famiy, int familySize, Binary64Data data) {
	float[] stepResult = new float[familySize];
	for (int i = 0; i < familySize; i++) {
	    stepResult[i] = core.compareEl(famiy[i], data);
	    System.out.println("CompareEl " + i + " Result " + stepResult[i]);
	}
	return stepResult;
    }

}
