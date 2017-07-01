package info.binarynetwork.core.interfaces;

import info.binarynetwork.objects.Binary32Data;
import info.binarynetwork.objects.Binary64Data;
import info.binarynetwork.objects.CompareData;
import info.binarynetwork.objects.neuralElement;
import info.binarynetwork.objects.neuralElement32;

public interface NetworkCore {
    public float compareEl(neuralElement famiyEl, CompareData data);

    public float compareEl(neuralElement32 famiyEl, Binary32Data data);

    public float compareEl(neuralElement famiyEl, Binary64Data data);

}
