package info.binarynetwork.interfaces;

import info.binarynetwork.objects.neuralElement;

public interface NetworkFamily {

	public neuralElement[] loadFamily(String fileName, int familySize);

	public void saveFamily(String fileName, neuralElement[] family);

	public neuralElement[] createFamily(int familySize, int lev1, int lev2, int lev3, int lev4);

}
