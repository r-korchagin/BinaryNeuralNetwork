package info.binarynetwork.core.interfaces;

import info.binarynetwork.objects.CompareData;
import info.binarynetwork.objects.neuralElement;

public interface NetworkStepExecutor {
	public float[] runStep(neuralElement[] famiy, int familySize, CompareData data);
}
