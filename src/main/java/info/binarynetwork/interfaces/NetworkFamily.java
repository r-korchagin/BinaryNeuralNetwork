package info.binarynetwork.interfaces;

import info.binarynetwork.objects.neuralElement32;
import info.binarynetwork.objects.neuralElement64;

public interface NetworkFamily {

    /**
     * Load neural 64bit element list from file list by mask
     * 
     * @param fileName
     *            - Mask file name
     * @param familySize
     *            - Size of family
     * @return - 32bit family array
     */
    public neuralElement64[] loadFamily(String fileName, int familySize);

    /**
     * Save neural 64bit element list to file by file name mask
     * 
     * @param fileName
     *            - Mask file name
     * @param family
     *            - Array neural 64bit elements
     */
    public void saveFamily(String fileName, neuralElement64[] family);

    /**
     * Load neural 32bit element list from file list by mask
     * 
     * @param fileName
     *            - Mask file name
     * @param familySize
     *            - Size of family
     * @return - 32bit family array
     */
    public neuralElement32[] loadFamily32(String fileName, int familySize);

    /**
     * Save neural 32bit element list to file by file name mask
     * 
     * @param fileName
     *            - Mask file name
     * @param family
     *            - Array neural 32bit elements
     */
    public void saveFamily32(String fileName, neuralElement32[] family);

    /**
     * Create random 64bit neural element
     * 
     * @param familySize
     *            Count elements in family
     * @param lev1
     *            Count neurons in first level
     * @param lev2
     *            Count neurons in second level
     * @param lev3
     *            Count neurons in third level
     * @param lev4
     *            Count neurons in forth level
     * @return Array of 64bit elements
     */
    public neuralElement64[] createFamily(int familySize, int lev1, int lev2, int lev3, int lev4);

    /**
     * Create random 32bit neural element
     * 
     * @param familySize
     *            Count elements in family
     * @param lev1
     *            Count neurons in first level
     * @param lev2
     *            Count neurons in second level
     * @param lev3
     *            Count neurons in third level
     * @param lev4
     *            Count neurons in forth level
     * @return Array of 32bit elements
     */
    public neuralElement32[] createFamily32(int familySize, int lev1, int lev2, int lev3, int lev4);

}
