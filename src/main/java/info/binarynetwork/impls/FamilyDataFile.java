package info.binarynetwork.impls;

import java.security.SecureRandom;

import info.binarynetwork.interfaces.NetworkFamily;
import info.binarynetwork.interfaces.NetworkFamilyLoader;
import info.binarynetwork.objects.neuralElement;

public class FamilyDataFile implements NetworkFamily {

	private NetworkFamilyLoader loader;

	private neuralElement createRandomEl(int lev1, int lev2, int lev3, int lev4) {
		neuralElement el = new neuralElement();
		el.setLevel1(lev1);
		el.setLevel2(lev2);
		el.setLevel3(lev3);
		el.setLevel4(lev4);
		long[] lv1_2 = new long[lev1 * lev2];
		long[] lv2_3 = new long[lev2 * lev3];
		long[] lv3_4 = new long[lev3 * lev4];
		SecureRandom randomno = new SecureRandom();
		// Random randomno = new Random(); // For emulated random
		for (int i = 0; i < lev1 * lev2; i++) {
			lv1_2[i] = randomno.nextLong();
		}
		for (int i = 0; i < lev2 * lev3; i++) {
			lv2_3[i] = randomno.nextLong();
		}
		for (int i = 0; i < lev3 * lev4; i++) {
			lv3_4[i] = randomno.nextLong();
		}
		el.setLink1_2(lv1_2);
		el.setLink2_3(lv2_3);
		el.setLink3_4(lv3_4);

		return el;
	}

	public NetworkFamilyLoader getLoader() {
		return loader;
	}

	public void setLoader(NetworkFamilyLoader loader) {
		this.loader = loader;
	}

	public neuralElement[] loadFamily(String fileName, int familySize) {
		neuralElement[] famiy = new neuralElement[familySize];
		// load family from file
		for (int i = 0; i < familySize; i++) {
			String FileName = fileName + String.valueOf(i) + ".json";
			famiy[i] = loader.loadFamilyEl(FileName);
		}
		return famiy;
	}

	public void saveFamily(String fileName, neuralElement[] family) {
		for (int i = 0; i < family.length; i++) {
			String FileName = fileName + String.valueOf(i) + ".json";
			loader.saveFamilyEl(FileName, family[i]);
		}

	}

	public neuralElement[] createFamily(int familySize, int lev1, int lev2, int lev3, int lev4) {
		neuralElement[] family = new neuralElement[familySize];
		for (int i = 0; i < familySize; i++) {
			family[i] = createRandomEl(lev1, lev2, lev3, lev4);
			// createRandomEl(100, 4000, 2000, 1,fileName);
		}
		return family;
	}

}
