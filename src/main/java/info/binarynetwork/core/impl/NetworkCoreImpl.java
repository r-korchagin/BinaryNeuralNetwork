package info.binarynetwork.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import info.binarynetwork.core.interfaces.NetworkCore;
import info.binarynetwork.core.objects.CRC64;
import info.binarynetwork.objects.Binary32Data;
import info.binarynetwork.objects.Binary64Data;
import info.binarynetwork.objects.CompareData;
import info.binarynetwork.objects.neuralElement;
import info.binarynetwork.objects.neuralElement32;

public class NetworkCoreImpl implements NetworkCore {

    // calculate count of Bits in int
    private static int NumberOfSetBits(int i) {
	i = i - ((i >>> 1) & 0x55555555);
	i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
	return (((i + (i >>> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;
    }

    // convert String data to long array
    private static long[] CRC64toStringData(String inStr) {
	StringTokenizer st = new StringTokenizer(inStr);
	List<String> newData = new ArrayList<String>();

	while (st.hasMoreElements()) {
	    String el = (String) st.nextElement();
	    if (el.length() > 1) {
		String formatterEl = el.replaceAll("[^\\p{L}\\p{Nd}]+", "");
		newData.add(formatterEl);
	    }
	}
	long[] resultD = new long[newData.size()];
	for (int i = 0; i < newData.size(); i++) {
	    byte bytes[] = newData.get(i).getBytes();
	    resultD[i] = CRC64.checksum(bytes);
	}
	return resultD;
    }

    private static int max_value_in__array(int[] currentData) {

	int highest = currentData[0];
	for (int index = 1; index < currentData.length; index++) {
	    if (currentData[index] > highest) {
		highest = currentData[index];
	    }
	}
	return highest;
    }

    private static long resultN(int[] currentData, int level) {
	long resultD = 0;
	int lev = (int) max_value_in__array(currentData) / 2;
	for (int i = 0; i < 64; i++) {
	    if (currentData[i] >= lev) {
		resultD = resultD ^ (1L << i);
	    }
	}
	return resultD;
    }

    private static int resultN32(int[] currentData, int level) {
	int resultD = 0;
	int lev = (int) max_value_in__array(currentData) / 2;
	for (int i = 0; i < 32; i++) {
	    if (currentData[i] >= lev) {
		resultD = resultD ^ (1 << i);
	    }
	}
	return resultD;
    }

    private static int[] setN(int[] currentData, long addData) {
	for (int i = 0; i < 64; i++) {
	    currentData[i] = currentData[i] + (int) ((addData >> i) & 1L);
	}
	return currentData;
    }

    private static int[] setN32(int[] currentData, int addData) {
	for (int i = 0; i < 32; i++) {
	    currentData[i] = currentData[i] + (int) ((addData >> i) & 1L);
	}
	return currentData;
    }

    // calculate by neural mask & source data
    private static long runLevEl(neuralElement el, long[] dat) {
	long result = 0;
	long[] lev_2 = new long[el.getLevel2()];
	long[] lev_3 = new long[el.getLevel3()];
	long[] lev_4 = new long[el.getLevel4()];
	// level_1 \/ \/ \/ \/ source arr
	// level_2 | | | | net
	if (dat.length > -1) {
	    // ************** LEVEL 1 - 2 ************************ //
	    int m = 0; // link 1 - 2 num
	    for (int k = 0; k < el.getLevel2(); k++) {
		int i = 0; // source num
		int[] currlev = new int[64]; // counter for each element
		while (i < dat.length) {
		    // i - index of source
		    // lev_2[k] = lev_2[k] ^ dat[i]&el.getLink1_2()[m];
		    currlev = setN(currlev, dat[i] & el.getLink1_2()[m]);
		    m++;
		    i++;
		}
		lev_2[k] = resultN(currlev, (int) (dat.length / 2));
		// end k
	    }
	    // ************** LEVEL 2 - 3 ************************ //
	    m = 0; // link 2 - 3 num
	    for (int k = 0; k < el.getLevel3(); k++) {
		int i = 0;
		int[] currlev = new int[64]; // counter for each element
		while (i < el.getLevel2()) {
		    // lev_3[k] = lev_3[k] ^ lev_2[i]&el.getLink2_3()[m];
		    currlev = setN(currlev, lev_2[i] & el.getLink2_3()[m]);
		    m++;
		    i++;
		}
		lev_3[k] = resultN(currlev, (int) (el.getLevel2() / 2.5));
	    }
	    // ************** LEVEL 3 - 4 ************************ //
	    m = 0; // link 2 - 3 num
	    for (int k = 0; k < el.getLevel4(); k++) {
		int i = 0;
		int[] currlev = new int[64]; // counter for each element
		while (i < el.getLevel3()) {
		    // lev_4[k] = lev_4[k] ^ lev_3[i]&el.getLink3_4()[m];
		    currlev = setN(currlev, lev_3[i] & el.getLink3_4()[m]);
		    m++;
		    i++;
		}
		lev_4[k] = resultN(currlev, (int) (el.getLevel3() / 2.5));
	    }
	    result = lev_4[0];
	}
	return result;
    }

    // calculate by neural mask & source data
    private static long runLevEl32(neuralElement32 el, int[] dat) {
	int result = 0;
	int[] lev_2 = new int[el.getLevel2()];
	int[] lev_3 = new int[el.getLevel3()];
	int[] lev_4 = new int[el.getLevel4()];
	// level_1 \/ \/ \/ \/ source arr
	// level_2 | | | | net
	if (dat.length > -1) {
	    // ************** LEVEL 1 - 2 ************************ //
	    int m = 0; // link 1 - 2 num
	    for (int k = 0; k < el.getLevel2(); k++) {
		int i = 0; // source num
		int[] currlev = new int[32]; // counter for each element
		while (i < dat.length) {
		    // i - index of source
		    // lev_2[k] = lev_2[k] ^ dat[i]&el.getLink1_2()[m];
		    currlev = setN32(currlev, dat[i] & el.getLink1_2()[m]);
		    m++;
		    i++;
		}
		lev_2[k] = resultN32(currlev, (int) (dat.length / 2));
		// end k
	    }
	    // ************** LEVEL 2 - 3 ************************ //
	    m = 0; // link 2 - 3 num
	    for (int k = 0; k < el.getLevel3(); k++) {
		int i = 0;
		int[] currlev = new int[32]; // counter for each element
		while (i < el.getLevel2()) {
		    // lev_3[k] = lev_3[k] ^ lev_2[i]&el.getLink2_3()[m];
		    currlev = setN(currlev, lev_2[i] & el.getLink2_3()[m]);
		    m++;
		    i++;
		}
		lev_3[k] = resultN32(currlev, (int) (el.getLevel2() / 2.5));
	    }
	    // ************** LEVEL 3 - 4 ************************ //
	    m = 0; // link 2 - 3 num
	    for (int k = 0; k < el.getLevel4(); k++) {
		int i = 0;
		int[] currlev = new int[32]; // counter for each element
		while (i < el.getLevel3()) {
		    // lev_4[k] = lev_4[k] ^ lev_3[i]&el.getLink3_4()[m];
		    currlev = setN(currlev, lev_3[i] & el.getLink3_4()[m]);
		    m++;
		    i++;
		}
		lev_4[k] = resultN32(currlev, (int) (el.getLevel3() / 2.5));
	    }
	    result = lev_4[0];
	}
	return result;
    }

    public float compareEl(neuralElement el, CompareData data) {
	float different = 0;
	for (int i = 0; i < data.getSize(); i++) {
	    // long[] dat = CRC64toString(cData.getTextData(i));

	    long[] dat = CRC64toStringData(data.getTextData(i));
	    // long[] dat = data.getBinaryData(i);

	    // System.out.println("dat lenght "+ String.valueOf(dat.length));
	    // startTime = System.nanoTime();
	    // long resume = runEl(el, dat);// binary neural
	    long resume = runLevEl(el, dat); // standart neural // run & return
					     // data
	    // System.out.println("-------2 Run time is =
	    // "+String.valueOf(System.nanoTime()-startTime));
	    int numLeftBit = NumberOfSetBits((int) (resume >> 32));
	    int numRightBit = NumberOfSetBits((int) (resume));
	    float compareRes = 0;
	    if (resume != 0) {
		compareRes = (float) numLeftBit / (numLeftBit + numRightBit);
	    }
	    // System.out.println("TEXT: " + cData.getTextData(i) + " REX = " +
	    // compareRes);
	    different = different + Math.abs(data.getIndexData(i) - compareRes);
	}
	return different;
    }

    public float compareEl(neuralElement32 el, Binary32Data data) {
	float different = 0;
	for (int i = 0; i < data.getSize(); i++) {
	    long resume = runLevEl32(el, data.getBinaryByID(i)); // standart
								 // neural //
								 // run
								 // & return
	    int numLeftBit = NumberOfSetBits((int) (resume >> 32));
	    int numRightBit = NumberOfSetBits((int) (resume));
	    float compareRes = 0;
	    if (resume != 0) {
		compareRes = (float) numLeftBit / (numLeftBit + numRightBit);
	    }
	    different = different + Math.abs(data.getIndexByID(i) - compareRes);
	}
	return different;
    }

    public float compareEl(neuralElement el, Binary64Data data) {
	float different = 0;
	for (int i = 0; i < data.getSize(); i++) {
	    long resume = runLevEl(el, data.getBinaryByID(i)); // standart
							       // neural // run
							       // & return
	    int numLeftBit = NumberOfSetBits((int) (resume >> 32));
	    int numRightBit = NumberOfSetBits((int) (resume));
	    float compareRes = 0;
	    if (resume != 0) {
		compareRes = (float) numLeftBit / (numLeftBit + numRightBit);
	    }
	    different = different + Math.abs(data.getIndexByID(i) - compareRes);
	}
	return different;
    }

}
