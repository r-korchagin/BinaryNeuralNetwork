package info.binarynetwork.impls;

import java.security.SecureRandom;

import info.binarynetwork.interfaces.NetworkFamily;
import info.binarynetwork.interfaces.NetworkFamilyLoader;
import info.binarynetwork.objects.neuralElement32;
import info.binarynetwork.objects.neuralElement64;

public class FamilyDataFile implements NetworkFamily {

    private NetworkFamilyLoader loader;

    public static neuralElement64 createRandomEl64(int lev1, int lev2, int lev3, int lev4) {
	neuralElement64 el = new neuralElement64();
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

    public static neuralElement32 createRandomEl32(int lev1, int lev2, int lev3, int lev4) {
	neuralElement32 el = new neuralElement32();
	el.setLevel1(lev1);
	el.setLevel2(lev2);
	el.setLevel3(lev3);
	el.setLevel4(lev4);
	int[] lv1_2 = new int[lev1 * lev2];
	int[] lv2_3 = new int[lev2 * lev3];
	int[] lv3_4 = new int[lev3 * lev4];
	SecureRandom randomno = new SecureRandom();
	// Random randomno = new Random(); // For emulated random
	for (int i = 0; i < lev1 * lev2; i++) {
	    lv1_2[i] = randomno.nextInt();
	}
	for (int i = 0; i < lev2 * lev3; i++) {
	    lv2_3[i] = randomno.nextInt();
	}
	for (int i = 0; i < lev3 * lev4; i++) {
	    lv3_4[i] = randomno.nextInt();
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

    public neuralElement64[] loadFamily(String fileName, int familySize) {
	neuralElement64[] famiy = new neuralElement64[familySize];
	// load family from file
	for (int i = 0; i < familySize; i++) {
	    String FileName = fileName + String.valueOf(i) + ".json";
	    famiy[i] = loader.loadFamilyEl64(FileName);
	}
	return famiy;
    }

    public void saveFamily(String fileName, neuralElement64[] family) {
	for (int i = 0; i < family.length; i++) {
	    String FileName = fileName + String.valueOf(i) + ".json";
	    loader.saveFamilyEl64(FileName, family[i]);
	}

    }

    public neuralElement64[] createFamily(int familySize, int lev1, int lev2, int lev3, int lev4) {
	neuralElement64[] family = new neuralElement64[familySize];
	for (int i = 0; i < familySize; i++) {
	    family[i] = createRandomEl64(lev1, lev2, lev3, lev4);
	    // createRandomEl(100, 4000, 2000, 1,fileName);
	}
	return family;
    }

    public neuralElement32[] createFamily32(int familySize, int lev1, int lev2, int lev3, int lev4) {
	neuralElement32[] family = new neuralElement32[familySize];
	for (int i = 0; i < familySize; i++) {
	    family[i] = createRandomEl32(lev1, lev2, lev3, lev4);
	    // createRandomEl(100, 4000, 2000, 1,fileName);
	}
	return family;
    }

    public neuralElement32[] loadFamily32(String fileName, int familySize) {
	neuralElement32[] famiy = new neuralElement32[familySize];
	// load family from file
	for (int i = 0; i < familySize; i++) {
	    String FileName = fileName + String.valueOf(i) + ".json";
	    famiy[i] = loader.loadFamilyEl32(FileName);
	}
	return famiy;
    }

    public void saveFamily32(String fileName, neuralElement32[] family) {
	for (int i = 0; i < family.length; i++) {
	    String FileName = fileName + String.valueOf(i) + ".json";
	    loader.saveFamilyEl32(FileName, family[i]);
	}

    }

}
