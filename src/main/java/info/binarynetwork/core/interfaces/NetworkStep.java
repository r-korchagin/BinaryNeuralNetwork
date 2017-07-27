package info.binarynetwork.core.interfaces;

import info.binarynetwork.objects.neuralElement64;
import info.binarynetwork.objects.neuralElement32;

public interface NetworkStep {

    public float[] execStep(neuralElement64[] famiy, int familySize);

    public float[] execStep32(neuralElement32[] famiy, int familySize);

    public float[] execStep64(neuralElement64[] famiy, int familySize);
}
