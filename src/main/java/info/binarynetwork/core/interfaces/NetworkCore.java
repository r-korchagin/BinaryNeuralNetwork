package info.binarynetwork.core.interfaces;

import info.binarynetwork.objects.Binary32Data;
import info.binarynetwork.objects.Binary64Data;
import info.binarynetwork.objects.CompareData;
import info.binarynetwork.objects.neuralElement64;
import info.binarynetwork.objects.neuralElement32;

public interface NetworkCore {
    public float compareEl(neuralElement64 famiyEl, CompareData data);

    public float compareEl(neuralElement32 famiyEl, Binary32Data data);

    public float compareEl(neuralElement64 famiyEl, Binary64Data data);

}
