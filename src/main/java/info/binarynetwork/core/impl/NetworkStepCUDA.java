package info.binarynetwork.core.impl;

import info.binarynetwork.core.interfaces.NetworkCUDACore;
import info.binarynetwork.core.interfaces.NetworkStepExecutor;
import info.binarynetwork.objects.Binary32Data;
import info.binarynetwork.objects.Binary64Data;
import info.binarynetwork.objects.CompareData;
import info.binarynetwork.objects.neuralElement32;
import info.binarynetwork.objects.neuralElement64;

public class NetworkStepCUDA implements NetworkStepExecutor {

    private int CUDA_Grid_Size_x;
    private int CUDA_Block_Size_x;
    private NetworkCUDACore CUDACore;

    public NetworkCUDACore getCUDACore() {
	return CUDACore;
    }

    public void setCUDACore(NetworkCUDACore cUDACore) {
	CUDACore = cUDACore;
    }

    public int getCUDA_Grid_Size_x() {
	return CUDA_Grid_Size_x;
    }

    public void setCUDA_Grid_Size_x(int cUDA_Grid_Size_x) {
	CUDA_Grid_Size_x = cUDA_Grid_Size_x;
    }

    public int getCUDA_Block_Size_x() {
	return CUDA_Block_Size_x;
    }

    public void setCUDA_Block_Size_x(int cUDA_Block_Size_x) {
	CUDA_Block_Size_x = cUDA_Block_Size_x;
    }

    public float[] runStep(neuralElement64[] famiy, int familySize, CompareData data) {
	// TODO Auto-generated method stub
	return null;
    }

    public float[] runStep(neuralElement32[] family, int familySize, Binary32Data data) {
	int[][] resultArr = new int[familySize][data.getSize()];
	for (int i = 0; i < familySize; i += CUDA_Grid_Size_x)
	    for (int j = 0; j < data.getSize(); j += CUDA_Block_Size_x) {
		// Create input data for each block calculation
		neuralElement32[] thrFamily = new neuralElement32[CUDA_Grid_Size_x];
		Binary32Data thrData = new Binary32Data();
		int i_block_size = Math.min(i + CUDA_Grid_Size_x, family.length);
		int j_block_size = Math.min(j + CUDA_Block_Size_x, data.getSize());
		for (int ii = i; ii < i_block_size; ii++) {
		    thrFamily[ii - i] = family[ii];
		    for (int jj = j; jj < j_block_size; jj++) {
			thrData.appendData(data.getIndexByID(jj), data.getBinaryByID(jj));
		    }
		}
		// End input data creation

		// CUDA execution
		int[][] trhResultArr = new int[i_block_size - i][j_block_size - j];

		CUDACore.compareEl(thrFamily, thrData, CUDA_Grid_Size_x, CUDA_Block_Size_x);
		// End Calculation

		// Mapping thread result to general array
		for (int grid_x = 0; grid_x < i_block_size - i; grid_x++) {
		    try {
			System.arraycopy(trhResultArr[grid_x], 0, resultArr[i + grid_x], j, j_block_size - j);
		    } catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			System.out.println(" j " + j + " grid_x " + grid_x + " i+grid_x " + (i + grid_x)
				+ " j_block_size " + j_block_size + " comparD.getSize() " + data.getSize());
		    }
		}
		// End mapping
	    }
	float[] tchkRez = new float[family.length];
	// long[] intRez = new long[];
	for (int i = 0; i < data.getSize(); i++) { // i - data index
	    for (int j = 0; j < family.length; j++) { // j - family index
		int resume = resultArr[j][i];
		int numLeftBit = NumberBits((int) (resume >> 16));
		int numRightBit = NumberBits((int) (resume));
		float compareRes = 0;
		if (resume != 0) {
		    compareRes = (float) numLeftBit / (numLeftBit + numRightBit);
		}
		// comparD
		// System.out.println("TEXT: " + comparD.getTextData(i) + " REX
		// = " + compareRes);
		tchkRez[j] = tchkRez[j] + Math.abs(data.getIndexByID(i) - compareRes);
	    }
	}
	return tchkRez;
    }

    private static int NumberBits(int i) {
	i = i - ((i >>> 1) & 0x55555555);
	i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
	return (((i + (i >>> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;
    }

    public float[] runStep(neuralElement64[] famiy, int familySize, Binary64Data data) {
	long[][] resultArr = new long[familySize][data.getSize()];
	for (int i = 0; i < familySize; i += CUDA_Grid_Size_x)
	    for (int j = 0; j < data.getSize(); j += CUDA_Block_Size_x) {
		// Create input data for each block calculation
		neuralElement64[] thrFamily = new neuralElement64[CUDA_Grid_Size_x];
		Binary64Data thrData = new Binary64Data();
		int i_block_size = Math.min(i + CUDA_Grid_Size_x, famiy.length);
		int j_block_size = Math.min(j + CUDA_Block_Size_x, data.getSize());
		for (int ii = i; ii < i_block_size; ii++) {
		    thrFamily[ii - i] = famiy[ii];
		    for (int jj = j; jj < j_block_size; jj++) {
			thrData.appendData(data.getIndexByID(jj), data.getBinaryByID(jj));
		    }
		}
		// End input data creation

		// CUDA execution
		long[][] trhResultArr = new long[i_block_size - i][j_block_size - j];

		CUDACore.compareEl(thrFamily, thrData, CUDA_Grid_Size_x, CUDA_Block_Size_x);
		// End Calculation

		// Mapping thread result to general array
		for (int grid_x = 0; grid_x < i_block_size - i; grid_x++) {
		    try {
			System.arraycopy(trhResultArr[grid_x], 0, resultArr[i + grid_x], j, j_block_size - j);
		    } catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			System.out.println(" j " + j + " grid_x " + grid_x + " i+grid_x " + (i + grid_x)
				+ " j_block_size " + j_block_size + " comparD.getSize() " + data.getSize());
		    }
		}
		// End mapping
	    }
	float[] tchkRez = new float[famiy.length];
	// long[] intRez = new long[];
	for (int i = 0; i < data.getSize(); i++) { // i - data index
	    for (int j = 0; j < famiy.length; j++) { // j - family index
		long resume = resultArr[j][i];
		int numLeftBit = NumberBits((int) (resume >> 32));
		int numRightBit = NumberBits((int) (resume));
		float compareRes = 0;
		if (resume != 0) {
		    compareRes = (float) numLeftBit / (numLeftBit + numRightBit);
		}
		// comparD
		// System.out.println("TEXT: " + comparD.getTextData(i) + " REX
		// = " + compareRes);
		tchkRez[j] = tchkRez[j] + Math.abs(data.getIndexByID(i) - compareRes);
	    }
	}
	return tchkRez;
    }

}
