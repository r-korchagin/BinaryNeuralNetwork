package info.binarynetwork.interfaces;

import info.binarynetwork.objects.neuralElement64;
import info.binarynetwork.objects.neuralElement32;

public interface NetworkFamily {

    public neuralElement64[] loadFamily(String fileName, int familySize);

    public void saveFamily(String fileName, neuralElement64[] family);

    public neuralElement32[] loadFamily32(String fileName, int familySize);

    public void saveFamily32(String fileName, neuralElement32[] family);

    public neuralElement64[] createFamily(int familySize, int lev1, int lev2, int lev3, int lev4);

    public neuralElement32[] createFamily32(int familySize, int lev1, int lev2, int lev3, int lev4);

}
