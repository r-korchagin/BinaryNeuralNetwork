package info.binarynetwork.core.interfaces;

import info.binarynetwork.objects.neuralElement;

public interface NetworkStep {

    public float[] execStep(neuralElement[] famiy, int familySize);

}
