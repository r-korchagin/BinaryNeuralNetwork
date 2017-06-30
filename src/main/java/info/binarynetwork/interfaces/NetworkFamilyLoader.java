package info.binarynetwork.interfaces;

import info.binarynetwork.objects.neuralElement;

public interface NetworkFamilyLoader {

	public neuralElement loadFamilyEl(String filename);

	public void saveFamilyEl(String fileName, neuralElement familyEl);

}
