package info.binarynetwork.core.interfaces;

import info.binarynetwork.objects.neuralElement32;
import info.binarynetwork.objects.neuralElement64;

public interface NetworkFamilyModification {
    public neuralElement32[] FamilyMutation(neuralElement32[] family, float[] prevResult);

    public neuralElement64[] FamilyMutation(neuralElement64[] family, float[] prevResult);
}
