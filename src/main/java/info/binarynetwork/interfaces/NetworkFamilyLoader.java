package info.binarynetwork.interfaces;

import info.binarynetwork.objects.neuralElement32;
import info.binarynetwork.objects.neuralElement64;

public interface NetworkFamilyLoader {

    /**
     * Function load 64-bit neural element from file
     * 
     * @param mask
     *            - JSON file name with element data
     * @return 64-bit neural element
     */
    public neuralElement64 loadFamilyEl64(String filename);

    /**
     * Function save 64-bit neural element into file
     * 
     * @param fileName
     *            - File name (excluding extension)
     * @param familyEl
     *            - 64-bit element for save in file
     */
    public void saveFamilyEl64(String fileName, neuralElement64 familyEl);

    /**
     * Function load 32-bit neural element from file
     * 
     * @param filename
     *            - File name (excluding extension)
     * @return 32-bit neural element
     */
    public neuralElement32 loadFamilyEl32(String filename);

    /**
     * Function save 32-bit neural element into file
     * 
     * @param fileName
     *            - File name (excluding extension)
     * @param familyEl
     *            - 32-bit element for save in file
     */
    public void saveFamilyEl32(String fileName, neuralElement32 familyEl);

}
