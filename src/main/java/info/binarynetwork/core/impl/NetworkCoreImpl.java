package info.binarynetwork.core.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.StringTokenizer;

import info.binarynetwork.core.interfaces.NetworkCore;
import info.binarynetwork.core.objects.CRC64;
import info.binarynetwork.objects.Binary32Data;
import info.binarynetwork.objects.Binary64Data;
import info.binarynetwork.objects.CompareData;
import info.binarynetwork.objects.neuralElement64;
import info.binarynetwork.objects.neuralElement32;

public class NetworkCoreImpl implements NetworkCore {

    private static final int[] b32_0 = new int[] { 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
	    1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
	    1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
	    1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
	    1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
	    1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
	    1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0,
	    1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1 };
    private static final int[] b32_1 = new int[] { 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1,
	    1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1,
	    1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1,
	    1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1,
	    1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1,
	    1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1,
	    1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1,
	    1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1 };
    private static final int[] b32_2 = new int[] { 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1,
	    1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0,
	    0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1,
	    1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0,
	    0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1,
	    1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0,
	    0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1,
	    1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1 };
    private static final int[] b32_3 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
	    0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
	    1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1,
	    1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
	    0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
	    0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
	    1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1,
	    1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1 };
    private static final int[] b32_4 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1,
	    1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	    1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	    1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
    private static final int[] b32_5 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	    1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1,
	    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
    private static final int[] b32_6 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
    private static final int[] b32_7 = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
	    0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1,
	    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
	    1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };

    // calculate count of Bits in int

    private static int NumberBits(int i) {
	i = i - ((i >>> 1) & 0x55555555);
	i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
	return (((i + (i >>> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;
    }

    /*
     * private static int NumberBits(int i) { int count = 0; while (i > 0) { i
     * &= (i - 1); } return count; }
     */

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
	IntSummaryStatistics stat = Arrays.stream(currentData).summaryStatistics();
	return stat.getMax();
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

    private static int resultN32(int[] currentData) {
	int resultD = 0;
	int lev = max_value_in__array(currentData) / 2;
	// int lev = 2;
	for (int i = 0; i < 32; i++) {
	    if (currentData[i] >= lev) {
		resultD = resultD ^ (1 << i);
	    }
	}
	return resultD;
    }

    private static int[] setN(int[] currentData, long addData) {
	if (addData == 0)
	    return currentData;
	for (int i = 0; i < 64; i++) {
	    currentData[i] = currentData[i] + (int) ((addData >> i) & 1);
	}
	return currentData;
    }

    private static int[] setN32(int[] currentData, int addData) {

	if (addData == 0)
	    return currentData;

	int b0 = addData & 255;
	int b1 = (addData >> 8) & 255;
	int b2 = (addData >> 16) & 255;
	int b3 = (addData >> 24) & 255;

	currentData[0] += b32_0[b0];
	currentData[1] += b32_1[b0];
	currentData[2] += b32_2[b0];
	currentData[3] += b32_3[b0];
	currentData[4] += b32_4[b0];
	currentData[5] += b32_5[b0];
	currentData[6] += b32_6[b0];
	currentData[7] += b32_7[b0];

	currentData[8] += b32_0[b1];
	currentData[9] += b32_1[b1];
	currentData[10] += b32_2[b1];
	currentData[11] += b32_3[b1];
	currentData[12] += b32_4[b1];
	currentData[13] += b32_5[b1];
	currentData[14] += b32_6[b1];
	currentData[15] += b32_7[b1];

	currentData[16] += b32_0[b2];
	currentData[17] += b32_1[b2];
	currentData[18] += b32_2[b2];
	currentData[19] += b32_3[b2];
	currentData[20] += b32_4[b2];
	currentData[21] += b32_5[b2];
	currentData[22] += b32_6[b2];
	currentData[23] += b32_7[b2];

	currentData[24] += b32_0[b3];
	currentData[25] += b32_1[b3];
	currentData[26] += b32_2[b3];
	currentData[27] += b32_3[b3];
	currentData[28] += b32_4[b3];
	currentData[29] += b32_5[b3];
	currentData[30] += b32_6[b3];
	currentData[31] += b32_7[b3];

	return currentData;
    }

    // calculate by neural mask & source data
    private static long runLevEl(neuralElement64 el, long[] dat) {
	long result = 0;
	long[] lev_2 = new long[el.getLevel2()];
	long[] lev_3 = new long[el.getLevel3()];
	long[] lev_4 = new long[el.getLevel4()];
	// level_1 \/ \/ \/ \/ source arr
	// level_2 | | | | net
	if (dat.length > 0) {
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
		int[] currlev = new int[32]; // counter for each element
		for (int i = 0; i < dat.length; i++) {
		    // lev_2[k] = lev_2[k] ^ dat[i]&el.getLink1_2()[m];
		    currlev = setN32(currlev, dat[i] & el.getLink1_2()[m]);
		    m++;
		}
		lev_2[k] = resultN32(currlev);
		// end k
	    }
	    // ************** LEVEL 2 - 3 ************************ //
	    m = 0; // link 2 - 3 num
	    for (int k = 0; k < el.getLevel3(); k++) {
		int[] currlev = new int[32]; // counter for each element
		for (int i = 0; i < el.getLevel2(); i++) {
		    // lev_3[k] = lev_3[k] ^ lev_2[i]&el.getLink2_3()[m];
		    currlev = setN32(currlev, lev_2[i] & el.getLink2_3()[m]);
		    m++;
		}
		lev_3[k] = resultN32(currlev);
	    }
	    // ************** LEVEL 3 - 4 ************************ //
	    m = 0; // link 2 - 3 num
	    for (int k = 0; k < el.getLevel4(); k++) {
		int[] currlev = new int[32]; // counter for each element
		for (int i = 0; i < el.getLevel3(); i++) {
		    // lev_4[k] = lev_4[k] ^ lev_3[i]&el.getLink3_4()[m];
		    currlev = setN32(currlev, lev_3[i] & el.getLink3_4()[m]);
		    m++;
		}
		lev_4[k] = resultN32(currlev);
	    }
	    result = lev_4[0];
	}
	return result;
    }

    public float compareEl(neuralElement64 el, CompareData data) {
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
	    int numLeftBit = NumberBits((int) (resume >> 32));
	    int numRightBit = NumberBits((int) (resume));
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
	    int numLeftBit = NumberBits((int) (resume >> 32));
	    int numRightBit = NumberBits((int) (resume));
	    float compareRes = 0;
	    if (resume != 0) {
		compareRes = (float) numLeftBit / (numLeftBit + numRightBit);
	    }
	    different = different + Math.abs(data.getIndexByID(i) - compareRes);
	}
	return different;
    }

    public float compareEl(neuralElement64 el, Binary64Data data) {
	float different = 0;
	for (int i = 0; i < data.getSize(); i++) {
	    long resume = runLevEl(el, data.getBinaryByID(i)); // standart
							       // neural // run
							       // & return
	    int numLeftBit = NumberBits((int) (resume >> 32));
	    int numRightBit = NumberBits((int) (resume));
	    float compareRes = 0;
	    if (resume != 0) {
		compareRes = (float) numLeftBit / (numLeftBit + numRightBit);
	    }
	    different = different + Math.abs(data.getIndexByID(i) - compareRes);
	}
	return different;
    }

}
