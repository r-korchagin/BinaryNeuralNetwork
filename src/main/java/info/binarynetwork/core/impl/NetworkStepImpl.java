package info.binarynetwork.core.impl;

import info.binarynetwork.core.interfaces.NetworkStep;
import info.binarynetwork.core.interfaces.NetworkStepExecutor;
import info.binarynetwork.interfaces.InputBinary32Data;
import info.binarynetwork.interfaces.InputBinary64Data;
import info.binarynetwork.interfaces.InputCompareData;
import info.binarynetwork.objects.neuralElement64;
import info.binarynetwork.objects.neuralElement32;

public class NetworkStepImpl implements NetworkStep {

    private NetworkStepExecutor executor;
    private InputCompareData data;
    private InputBinary32Data data32;
    private InputBinary64Data data64;

    public InputBinary32Data getData32() {
	return data32;
    }

    public void setData32(InputBinary32Data data32) {
	this.data32 = data32;
    }

    public InputBinary64Data getData64() {
	return data64;
    }

    public void setData64(InputBinary64Data data64) {
	this.data64 = data64;
    }

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

    public float[] execStep(neuralElement64[] famiy, int familySize) {
	float[] result = executor.runStep(famiy, familySize, data.getInputData());
	return result;
    }

    public float[] execStep32(neuralElement32[] famiy, int familySize) {
	float[] result = executor.runStep(famiy, familySize, data32.getInputData());
	return result;
    }

    public float[] execStep64(neuralElement64[] famiy, int familySize) {
	float[] result = executor.runStep(famiy, familySize, data64.getInputData());
	return result;
    }

}
