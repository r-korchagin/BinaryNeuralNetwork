package info.binarynetwork.core.impl;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import info.binarynetwork.core.interfaces.NetworkFamilyModification;
import info.binarynetwork.impls.FamilyDataFile;
import info.binarynetwork.objects.neuralElement32;
import info.binarynetwork.objects.neuralElement64;

@Component("NetworkFamilyModification")
public class NetworkFamilyImpl implements NetworkFamilyModification {

    private static int getRandom(int n) {
	SecureRandom randomno = new SecureRandom();
	return randomno.nextInt(n);
    }

    // do element mutation at only 1 bit
    private static neuralElement32 smallMutationEl32(neuralElement32 el) {
	neuralElement32 res = new neuralElement32();
	res = el;
	// Random randomno = new Random();
	SecureRandom randomno = new SecureRandom();
	int sel = randomno.nextInt(3);
	if (sel == 0) {
	    int mutPos1 = randomno.nextInt(res.getLevel1() * res.getLevel2());
	    int mask1 = 1 << randomno.nextInt(32);
	    int[] lnk_1_2 = el.getLink1_2();
	    lnk_1_2[mutPos1] = lnk_1_2[mutPos1] ^ mask1;
	    res.setLink1_2(lnk_1_2);
	} else if (sel == 1) {
	    int mutPos2 = randomno.nextInt(res.getLevel2() * res.getLevel3());
	    int mask2 = 1 << randomno.nextInt(32);
	    int[] lnk_2_3 = el.getLink2_3();
	    lnk_2_3[mutPos2] = lnk_2_3[mutPos2] ^ mask2;
	    res.setLink2_3(lnk_2_3);
	} else if (sel == 2) {
	    int mutPos3 = randomno.nextInt(res.getLevel3() * res.getLevel4());
	    int mask3 = 1 << randomno.nextInt(32);
	    int[] lnk_3_4 = el.getLink3_4();
	    lnk_3_4[mutPos3] = lnk_3_4[mutPos3] ^ mask3;
	    res.setLink3_4(lnk_3_4);
	}
	return res;
    }

    // do element mutation at only 1 bit
    private static neuralElement64 smallMutationEl64(neuralElement64 el) {
	neuralElement64 res = new neuralElement64();
	res = el;
	// Random randomno = new Random();
	SecureRandom randomno = new SecureRandom();
	int sel = randomno.nextInt(3);
	if (sel == 0) {
	    int mutPos1 = randomno.nextInt(res.getLevel1() * res.getLevel2());
	    long mask1 = 1L << randomno.nextInt(64);
	    long[] lnk_1_2 = el.getLink1_2();
	    lnk_1_2[mutPos1] = lnk_1_2[mutPos1] ^ mask1;
	    res.setLink1_2(lnk_1_2);
	} else if (sel == 1) {
	    int mutPos2 = randomno.nextInt(res.getLevel2() * res.getLevel3());
	    long mask2 = 1L << randomno.nextInt(64);
	    long[] lnk_2_3 = el.getLink2_3();
	    lnk_2_3[mutPos2] = lnk_2_3[mutPos2] ^ mask2;
	    res.setLink2_3(lnk_2_3);
	} else if (sel == 2) {
	    int mutPos3 = randomno.nextInt(res.getLevel3() * res.getLevel4());
	    long mask3 = 1 << randomno.nextInt(64);
	    long[] lnk_3_4 = el.getLink3_4();
	    lnk_3_4[mutPos3] = lnk_3_4[mutPos3] ^ mask3;
	    res.setLink3_4(lnk_3_4);
	}
	return res;
    }

    private static neuralElement32 MutationEl32(neuralElement32 el, int count) {
	neuralElement32 res = new neuralElement32();
	res = el;
	// int countMutation = f(count);
	// Random randomno = new Random();
	SecureRandom randomno = new SecureRandom();
	for (int i = 0; i < count * 2; i++) {
	    int sel = randomno.nextInt(3);
	    if (sel == 0) {
		int mutPos1 = randomno.nextInt(res.getLevel1() * res.getLevel2());
		int mask1 = 1 << randomno.nextInt(32);
		int[] lnk_1_2 = el.getLink1_2();
		lnk_1_2[mutPos1] = lnk_1_2[mutPos1] ^ mask1;
		res.setLink1_2(lnk_1_2);
	    } else if (sel == 1) {
		int mutPos2 = randomno.nextInt(res.getLevel2() * res.getLevel3());
		int mask2 = 1 << randomno.nextInt(32);
		int[] lnk_2_3 = el.getLink2_3();
		lnk_2_3[mutPos2] = lnk_2_3[mutPos2] ^ mask2;
		res.setLink2_3(lnk_2_3);
	    } else if (sel == 2) {
		int mutPos3 = randomno.nextInt(res.getLevel3() * res.getLevel4());
		int mask3 = 1 << randomno.nextInt(32);
		int[] lnk_3_4 = el.getLink3_4();
		lnk_3_4[mutPos3] = lnk_3_4[mutPos3] ^ mask3;
		res.setLink3_4(lnk_3_4);
	    }
	}
	return res;
    }

    private static neuralElement64 MutationEl64(neuralElement64 el, int count) {
	neuralElement64 res = new neuralElement64();
	res = el;
	// Random randomno = new Random();
	SecureRandom randomno = new SecureRandom();
	for (int i = 0; i < count; i++) {
	    int sel = randomno.nextInt(3);
	    if (sel == 0) {
		int mutPos1 = randomno.nextInt(res.getLevel1() * res.getLevel2());
		long mask1 = 1L << randomno.nextInt(64);
		long[] lnk_1_2 = el.getLink1_2();
		lnk_1_2[mutPos1] = lnk_1_2[mutPos1] ^ mask1;
		res.setLink1_2(lnk_1_2);
	    } else if (sel == 1) {
		int mutPos2 = randomno.nextInt(res.getLevel2() * res.getLevel3());
		long mask2 = 1L << randomno.nextInt(64);
		long[] lnk_2_3 = el.getLink2_3();
		lnk_2_3[mutPos2] = lnk_2_3[mutPos2] ^ mask2;
		res.setLink2_3(lnk_2_3);
	    } else if (sel == 2) {
		int mutPos3 = randomno.nextInt(res.getLevel3() * res.getLevel4());
		long mask3 = 1L << randomno.nextInt(64);
		long[] lnk_3_4 = el.getLink3_4();
		lnk_3_4[mutPos3] = lnk_3_4[mutPos3] ^ mask3;
		res.setLink3_4(lnk_3_4);
	    }
	}
	return res;
    }

    // do crossover between 2 mask and get 2 child
    public static neuralElement32 crossEl32(neuralElement32 faa, neuralElement32 moo) {
	neuralElement32 res0 = new neuralElement32();
	res0 = faa;
	// create random cross position
	SecureRandom randomno = new SecureRandom();
	// Random randomno = new Random();
	int link_1_2_size = faa.getLevel1() * faa.getLevel2();
	int link_2_3_size = faa.getLevel2() * faa.getLevel3();
	int link_3_4_size = faa.getLevel3() * faa.getLevel4();

	int crossPos1 = randomno.nextInt(link_1_2_size);
	int crossPos2 = randomno.nextInt(link_2_3_size);
	int crossPos3 = randomno.nextInt(link_3_4_size);

	int[] lnk12 = faa.getLink1_2();
	System.arraycopy(moo.getLink1_2(), crossPos1, lnk12, crossPos1, link_1_2_size - crossPos1);
	res0.setLink1_2(lnk12);

	int[] lnk23 = faa.getLink2_3();
	System.arraycopy(moo.getLink2_3(), crossPos2, lnk23, crossPos2, link_2_3_size - crossPos2);
	res0.setLink2_3(lnk23);

	int[] lnk34 = faa.getLink3_4();
	System.arraycopy(moo.getLink3_4(), crossPos3, lnk34, crossPos3, link_3_4_size - crossPos3);
	res0.setLink3_4(lnk34);

	return res0;
    }

    // do crossover between 2 mask and get 2 child
    public static neuralElement64 crossEl64(neuralElement64 faa, neuralElement64 moo) {
	neuralElement64 res0 = new neuralElement64();
	res0 = faa;
	// create random cross position
	SecureRandom randomno = new SecureRandom();
	// Random randomno = new Random();
	int link_1_2_size = faa.getLevel1() * faa.getLevel2();
	int link_2_3_size = faa.getLevel2() * faa.getLevel3();
	int link_3_4_size = faa.getLevel3() * faa.getLevel4();

	int crossPos1 = randomno.nextInt(link_1_2_size);
	int crossPos2 = randomno.nextInt(link_2_3_size);
	int crossPos3 = randomno.nextInt(link_3_4_size);

	long[] lnk12 = faa.getLink1_2();
	System.arraycopy(moo.getLink1_2(), crossPos1, lnk12, crossPos1, link_1_2_size - crossPos1);
	res0.setLink1_2(lnk12);

	long[] lnk23 = faa.getLink2_3();
	System.arraycopy(moo.getLink2_3(), crossPos2, lnk23, crossPos2, link_2_3_size - crossPos2);
	res0.setLink2_3(lnk23);

	long[] lnk34 = faa.getLink3_4();
	System.arraycopy(moo.getLink3_4(), crossPos3, lnk34, crossPos3, link_3_4_size - crossPos3);
	res0.setLink3_4(lnk34);

	return res0;
    }

    private int GetFamilyIndex(float[] unsortResult, float val) {

	for (int i = 0; i < unsortResult.length; i++) {
	    if (unsortResult[i] == val)
		return i;
	}

	System.out.println("Search float problem " + val);
	return 0;
    }

    public neuralElement32[] FamilyMutation(neuralElement32[] family, float[] prevResult) {
	float[] sortResult = new float[family.length];
	int stopElitLev = (int) Math.round(family.length * 0.4); // elite
	int stopCrossLev = (int) Math.round(family.length * 0.8); // cross

	// Create sorted array
	sortResult = Arrays.copyOf(prevResult, prevResult.length);
	Arrays.sort(sortResult); /// less the best

	// Create new family
	List<neuralElement32> newFamiy = new ArrayList<neuralElement32>();

	for (int n = 0; n < family.length; n++) {
	    if (n < 3) {
		// family with index i is elite
		System.out.println(GetFamilyIndex(prevResult, sortResult[n]) + " SORT " + sortResult[n] + " PREV "
			+ prevResult[n]);
		newFamiy.add(family[GetFamilyIndex(prevResult, sortResult[n])]);
		// System.out.println("Fam size el = "+newFamiy.size());
	    } else if (n < stopElitLev) {
		newFamiy.add(smallMutationEl32(family[GetFamilyIndex(prevResult, sortResult[n])]));
	    } else if (n < stopCrossLev) {
		// family with index i is cross
		neuralElement32 bayb = new neuralElement32();
		int motherIndex = getRandom(n - 1);
		bayb = crossEl32(family[GetFamilyIndex(prevResult, sortResult[n])],
			family[GetFamilyIndex(prevResult, sortResult[motherIndex])]);
		newFamiy.add(MutationEl32(bayb, n));
	    } else if (n > stopCrossLev - 1) {
		newFamiy.add(FamilyDataFile.createRandomEl32(family[0].getLevel1(), family[0].getLevel2(),
			family[0].getLevel3(), family[0].getLevel4()));
	    }

	}

	neuralElement32[] resultFamily = new neuralElement32[prevResult.length];

	return newFamiy.toArray(resultFamily);
    }

    public neuralElement64[] FamilyMutation(neuralElement64[] family, float[] prevResult) {
	float[] sortResult = new float[family.length];
	int stopElitLev = (int) Math.round(family.length * 0.2); // elite
	int stopCrossLev = (int) Math.round(family.length * 0.8); // cross

	// Create sorted array
	System.arraycopy(prevResult, 0, sortResult, 0, prevResult.length);
	Arrays.sort(sortResult); /// less the best

	// Create new family
	List<neuralElement64> newFamiy = new ArrayList<neuralElement64>();

	for (int n = 0; n < family.length; n++) {
	    if (n < 2) {
		// family with index i is elite
		newFamiy.add(family[GetFamilyIndex(prevResult, sortResult[n])]);
		// System.out.println("Fam size el = "+newFamiy.size());
	    } else if (n < stopElitLev) {
		newFamiy.add(smallMutationEl64(family[GetFamilyIndex(prevResult, sortResult[n])]));
	    } else if (n < stopCrossLev) {
		// family with index i is cross
		neuralElement64 bayb = new neuralElement64();
		int motherIndex = getRandom(n - 1);
		bayb = crossEl64(family[GetFamilyIndex(prevResult, sortResult[n])],
			family[GetFamilyIndex(prevResult, sortResult[motherIndex])]);
		newFamiy.add(MutationEl64(bayb, n));
	    } else if (n > stopCrossLev - 1) {
		newFamiy.add(FamilyDataFile.createRandomEl64(family[0].getLevel1(), family[0].getLevel2(),
			family[0].getLevel3(), family[0].getLevel4()));
	    }

	}

	neuralElement64[] resultFamily = new neuralElement64[prevResult.length];

	return newFamiy.toArray(resultFamily);
    }

}
