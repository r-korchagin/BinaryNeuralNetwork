package info.binarynetwork.core.interfaces;

import info.binarynetwork.objects.Binary32Data;
import info.binarynetwork.objects.Binary64Data;
import info.binarynetwork.objects.CompareData;
import info.binarynetwork.objects.neuralElement64;
import info.binarynetwork.objects.neuralElement32;

public interface NetworkStepExecutor {

    public float[] runStep(neuralElement64[] famiy, int familySize, CompareData data);

    public float[] runStep(neuralElement32[] famiy, int familySize, Binary32Data data);

    public float[] runStep(neuralElement64[] famiy, int familySize, Binary64Data data);
}
