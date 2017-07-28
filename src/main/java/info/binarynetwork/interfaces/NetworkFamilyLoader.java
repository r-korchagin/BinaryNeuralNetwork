package info.binarynetwork.interfaces;

import info.binarynetwork.objects.neuralElement64;
import info.binarynetwork.objects.neuralElement32;

public interface NetworkFamilyLoader {

    public neuralElement64 loadFamilyEl64(String filename);

    public void saveFamilyEl64(String fileName, neuralElement64 familyEl);

    public neuralElement32 loadFamilyEl32(String filename);

    public void saveFamilyEl32(String fileName, neuralElement32 familyEl);

}
