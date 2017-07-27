package info.binarynetwork.core.objects;

import static jcuda.driver.JCudaDriver.cuCtxCreate;
import static jcuda.driver.JCudaDriver.cuCtxSynchronize;
import static jcuda.driver.JCudaDriver.cuDeviceGet;
import static jcuda.driver.JCudaDriver.cuInit;
import static jcuda.driver.JCudaDriver.cuLaunchKernel;
import static jcuda.driver.JCudaDriver.cuMemAlloc;
import static jcuda.driver.JCudaDriver.cuMemFree;
import static jcuda.driver.JCudaDriver.cuMemcpyDtoH;
import static jcuda.driver.JCudaDriver.cuMemcpyHtoD;
import static jcuda.driver.JCudaDriver.cuModuleGetFunction;
import static jcuda.driver.JCudaDriver.cuModuleLoad;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import info.binarynetwork.objects.CompareData;
import info.binarynetwork.objects.neuralElement64;
import jcuda.Pointer;
import jcuda.Sizeof;
import jcuda.driver.CUcontext;
import jcuda.driver.CUdevice;
import jcuda.driver.CUdeviceptr;
import jcuda.driver.CUfunction;
import jcuda.driver.CUmodule;
import jcuda.driver.CUresult;
import jcuda.driver.JCudaDriver;
import jcuda.runtime.JCuda;
import jcuda.runtime.cudaDeviceProp;

public class NeuralCUDA {

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

	// calculate count of Bits in int
	private static int NumberOfSetBits(int i) {
		i = i - ((i >>> 1) & 0x55555555);
		i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
		return (((i + (i >>> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;
	}

	// @SuppressWarnings("null")
	public static float[] compareNeural(neuralElement64[] Family, CompareData comparD, int gridSizeX, int blockSizeX, int second_lev, int tride_lev, int devNum) throws IOException {
		// Enable exceptions and omit all subsequent error checks
		// JCudaDeviceQuery.getCUDAdev();
		// System.out.println("Family size = " + Family.length);
		// System.out.println("Data size = " + comparD.getSize());
		JCudaDriver.setExceptionsEnabled(false);

		// Create the PTX file by calling the NVCC
		String ptxFileName = preparePtxFile("newKernel.cu", second_lev, tride_lev);

		// Initialize the driver and create a context for the first device.
		cuInit(0);
		CUdevice device = new CUdevice();
		cuDeviceGet(device, devNum);
		CUcontext context = new CUcontext();
		cuCtxCreate(context, 0, device);

		// cudaDeviceProp prop = new cudaDeviceProp();
		// JCuda.cudaGetDeviceProperties(prop, 0);
		// printDevProp(prop);

		// Load the ptx file.
		CUmodule module = new CUmodule();
		cuModuleLoad(module, ptxFileName);

		// Obtain a function pointer to the "neural" function.
		CUfunction function = new CUfunction();
		cuModuleGetFunction(function, module, "neural");

		// Create input data
		int maxSize = 0;
		long[][] dat = new long[comparD.getSize()][100];
		for (int i = 0; i < comparD.getSize(); i++) {
			long[] currDat = CRC64toStringData(comparD.getTextData(i));
			// System.out.println(comparD.getTextData(i));
			for (int k = 0; k < currDat.length; k++) {
				dat[i][k] = currDat[k];
			}
			if (currDat.length > maxSize) {
				maxSize = currDat.length;
			}
		}
		int DAT_Size = comparD.getSize();
		int DAT_Lenght = maxSize;
		long[] DAT = new long[DAT_Size * DAT_Lenght + DAT_Lenght];

		for (int i = 0; i < DAT_Size; i++) {
			for (int j = 0; j < DAT_Lenght; j++) {
				DAT[i * DAT_Lenght + j] = dat[i][j];
			}
		}

		int FAMILY_Size = Family.length;
		int level_1 = 0;
		int level_2 = 0;
		int level_3 = 0;
		int level_4 = 0;
		long[] link1_2 = new long[Family.length * Family[0].getLink1_2().length + Family[0].getLink1_2().length];
		long[] link2_3 = new long[Family.length * Family[0].getLink2_3().length + Family[0].getLink2_3().length];
		long[] link3_4 = new long[Family.length * Family[0].getLink3_4().length + Family[0].getLink3_4().length];

		for (int i = 0; i < Family.length; i++) {
			// i -index family
			level_1 = Family[i].getLevel1();
			level_2 = Family[i].getLevel2();
			level_3 = Family[i].getLevel3();
			level_4 = Family[i].getLevel4();
			for (int j = 0; j < Family[i].getLink1_2().length; j++) {
				link1_2[i * Family[i].getLink1_2().length + j] = Family[i].getLink1_2()[j];
			}
			for (int j = 0; j < Family[i].getLink2_3().length; j++) {
				link2_3[i * Family[i].getLink2_3().length + j] = Family[i].getLink2_3()[j];
			}
			// System.out.println(Family[i].getLink2_3().length);
			for (int j = 0; j < Family[i].getLink3_4().length; j++) {
				link3_4[i * Family[i].getLink3_4().length + j] = Family[i].getLink3_4()[j];
			}
		}
		// Allocate the device input data, and copy the
		// host input data to the device
		/*
		 * int DAT_Size, int DAT_Lenght, int FAMILY_Size, long long *DAT, int
		 * level_1, int level_2, int level_3, int level_4, long long *link1_2,
		 * long long *link2_3, long long *link3_4 long long *result
		 */

		// DAT
		CUdeviceptr deviceDATInput = new CUdeviceptr();
		cuMemAlloc(deviceDATInput, DAT.length * Sizeof.LONG);
		cuMemcpyHtoD(deviceDATInput, Pointer.to(DAT), DAT.length * Sizeof.LONG);

		// link1_2
		CUdeviceptr device12Input = new CUdeviceptr();
		cuMemAlloc(device12Input, link1_2.length * Sizeof.LONG);
		cuMemcpyHtoD(device12Input, Pointer.to(link1_2), link1_2.length * Sizeof.LONG);

		// link2_3
		CUdeviceptr device23Input = new CUdeviceptr();
		cuMemAlloc(device23Input, link2_3.length * Sizeof.LONG);
		cuMemcpyHtoD(device23Input, Pointer.to(link2_3), link2_3.length * Sizeof.LONG);

		// link3_4
		CUdeviceptr device34Input = new CUdeviceptr();
		cuMemAlloc(device34Input, link3_4.length * Sizeof.LONG);
		cuMemcpyHtoD(device34Input, Pointer.to(link3_4), link3_4.length * Sizeof.LONG);

		// Allocate device output memory
		CUdeviceptr deviceOutput = new CUdeviceptr();
		cuMemAlloc(deviceOutput, FAMILY_Size * DAT_Size * Sizeof.LONG);

		// Set up the kernel parameters: A pointer to an array
		// of pointers which point to the actual values.

		/*
		 * int DAT_Size, int DAT_Lenght, int FAMILY_Size, long long *DAT, int
		 * level_1, int level_2, int level_3, int level_4, long long *link1_2,
		 * long long *link2_3, long long *link3_4 long long *result
		 */

		Pointer kernelParameters = Pointer.to(Pointer.to(new int[] { DAT_Size }), // new
																					// int[]{DAT_Size}
				Pointer.to(new int[] { DAT_Lenght }), // deviceDATLenghtInput
														// new int[]{DAT_Lenght}
				Pointer.to(new int[] { FAMILY_Size }), // deviceFAMILY_SizeInput
				Pointer.to(deviceDATInput), Pointer.to(new int[] { level_1 }), // devicelevel_1Input
				Pointer.to(new int[] { level_2 }), // new int[]{level_2}
				Pointer.to(new int[] { level_3 }), // new int[]{level_3}
				Pointer.to(new int[] { level_4 }), // new int[]{level_4}
				Pointer.to(device12Input), Pointer.to(device23Input), Pointer.to(device34Input), Pointer.to(deviceOutput));

		// Call the kernel function.
		int errorCode = 0;

		errorCode = cuLaunchKernel(function,
				// gridSizeX, 1, 1, // Grid dimension
				gridSizeX, 1, 1, blockSizeX, 1, 1, // Block dimension blockSizeX
				0, null, // Shared memory size and stream
				kernelParameters, null // Kernel- and extra parameters
		);
		cuCtxSynchronize();
		if (errorCode != CUresult.CUDA_SUCCESS) {
			System.out.println("Cuda error = " /* + gridSizeX */);
		}

		// Allocate host output memory and copy the device output
		// to the host.
		long hostOutput[] = new long[FAMILY_Size * DAT_Size];
		cuMemcpyDtoH(Pointer.to(hostOutput), deviceOutput, FAMILY_Size * DAT_Size * Sizeof.LONG);
		// System.out.println("CUDA RESULT :");
		// for (int i = 0; i < hostOutput.length; i++){
		// System.out.println(" VAL: " + String.valueOf(hostOutput[i]));
		// }
		// System.out.println("Size = " + hostOutput.length);

		// result float[family size];
		// System.out.println("CUDA = ");
		int m = 0;
		float[] chkRez = new float[FAMILY_Size];
		// long[] intRez = new long[];
		for (int i = 0; i < DAT_Size; i++) { // i - data index
			for (int j = 0; j < FAMILY_Size; j++) { // j - family index
				long resume = hostOutput[m];
				int numLeftBit = NumberOfSetBits((int) (resume >> 32));
				int numRightBit = NumberOfSetBits((int) (resume));
				float compareRes = 0;
				if (resume != 0) {
					compareRes = (float) numLeftBit / (numLeftBit + numRightBit);
				}
				// comparD
				// System.out.println("TEXT: " + comparD.getTextData(i) + " REX
				// = " + compareRes);
				chkRez[j] = chkRez[j] + Math.abs(comparD.getIndexData(i) - compareRes);
				m++;
			}
		}
		cuMemFree(deviceDATInput);
		cuMemFree(device12Input);
		cuMemFree(device23Input);
		cuMemFree(device34Input);
		cuMemFree(deviceOutput);
		JCuda.cudaFree(kernelParameters);
		JCudaDriver.cuCtxDetach(context);
		return chkRez;
	}

	/**
	 * return long double array
	 * 
	 */
	public static long[][] compareNeuralLong(neuralElement64[] Family, CompareData comparD, int gridSizeX, int blockSizeX, int second_lev, int tride_lev, int devNum) throws IOException {
		// Enable exceptions and omit all subsequent error checks
		// JCudaDeviceQuery.getCUDAdev();
		// System.out.println("Family size = " + Family.length);
		// System.out.println("Data size = " + comparD.getSize());
		JCudaDriver.setExceptionsEnabled(true);

		// Create the PTX file by calling the NVCC
		String ptxFileName = preparePtxFile("newKernel.cu", second_lev, tride_lev);

		// Initialize the driver and create a context for the first device.
		cuInit(0);
		CUdevice device = new CUdevice();
		cuDeviceGet(device, devNum);
		CUcontext context = new CUcontext();
		cuCtxCreate(context, 0, device);

		// cudaDeviceProp prop = new cudaDeviceProp();
		// JCuda.cudaGetDeviceProperties(prop, 0);
		// printDevProp(prop);

		// Load the ptx file.
		CUmodule module = new CUmodule();
		cuModuleLoad(module, ptxFileName);

		// Obtain a function pointer to the "neural" function.
		CUfunction function = new CUfunction();
		cuModuleGetFunction(function, module, "neural");

		// Create input data
		int maxSize = 0;
		long[][] dat = new long[comparD.getSize()][100];
		for (int i = 0; i < comparD.getSize(); i++) {
			long[] currDat = CRC64toStringData(comparD.getTextData(i));
			// System.out.println(comparD.getTextData(i));
			for (int k = 0; k < currDat.length; k++) {
				dat[i][k] = currDat[k];
			}
			if (currDat.length > maxSize) {
				maxSize = currDat.length;
			}
		}
		int DAT_Size = comparD.getSize();
		int DAT_Lenght = maxSize;
		long[] DAT = new long[DAT_Size * DAT_Lenght + DAT_Lenght];

		for (int i = 0; i < DAT_Size; i++) {
			for (int j = 0; j < DAT_Lenght; j++) {
				DAT[i * DAT_Lenght + j] = dat[i][j];
			}
		}

		int FAMILY_Size = Family.length;
		int level_1 = 0;
		int level_2 = 0;
		int level_3 = 0;
		int level_4 = 0;
		long[] link1_2 = new long[Family.length * Family[0].getLink1_2().length + Family[0].getLink1_2().length];
		long[] link2_3 = new long[Family.length * Family[0].getLink2_3().length + Family[0].getLink2_3().length];
		long[] link3_4 = new long[Family.length * Family[0].getLink3_4().length + Family[0].getLink3_4().length];

		for (int i = 0; i < Family.length; i++) {
			// i -index family
			level_1 = Family[i].getLevel1();
			level_2 = Family[i].getLevel2();
			level_3 = Family[i].getLevel3();
			level_4 = Family[i].getLevel4();
			for (int j = 0; j < Family[i].getLink1_2().length; j++) {
				link1_2[i * Family[i].getLink1_2().length + j] = Family[i].getLink1_2()[j];
			}
			for (int j = 0; j < Family[i].getLink2_3().length; j++) {
				link2_3[i * Family[i].getLink2_3().length + j] = Family[i].getLink2_3()[j];
			}
			// System.out.println(Family[i].getLink2_3().length);
			for (int j = 0; j < Family[i].getLink3_4().length; j++) {
				link3_4[i * Family[i].getLink3_4().length + j] = Family[i].getLink3_4()[j];
			}
		}
		// Allocate the device input data, and copy the
		// host input data to the device
		/*
		 * int DAT_Size, int DAT_Lenght, int FAMILY_Size, long long *DAT, int
		 * level_1, int level_2, int level_3, int level_4, long long *link1_2,
		 * long long *link2_3, long long *link3_4 long long *result
		 */
		/*
		 * // DAT_Size CUdeviceptr deviceDATSizeInput = new CUdeviceptr();
		 * cuMemAlloc(deviceDATSizeInput, Sizeof.INT);
		 * cuMemcpyHtoD(deviceDATSizeInput, Pointer.to(new int[]{DAT_Size}),
		 * Sizeof.INT);
		 * 
		 * // DAT_Lenght CUdeviceptr deviceDATLenghtInput = new CUdeviceptr();
		 * cuMemAlloc(deviceDATLenghtInput, Sizeof.INT);
		 * cuMemcpyHtoD(deviceDATLenghtInput, Pointer.to(new int[]{DAT_Lenght}),
		 * Sizeof.INT);
		 * 
		 * // FAMILY_Size CUdeviceptr deviceFAMILY_SizeInput = new
		 * CUdeviceptr(); cuMemAlloc(deviceFAMILY_SizeInput, Sizeof.INT);
		 * cuMemcpyHtoD(deviceFAMILY_SizeInput, Pointer.to(new
		 * int[]{FAMILY_Size}), Sizeof.INT);
		 */
		/*
		 * //level_1 CUdeviceptr devicelevel_1Input = new CUdeviceptr();
		 * cuMemAlloc(devicelevel_1Input, Sizeof.INT);
		 * cuMemcpyHtoD(devicelevel_1Input, Pointer.to(new int[]{level_1}),
		 * Sizeof.INT);
		 * 
		 * //level_2 CUdeviceptr devicelevel_2Input = new CUdeviceptr();
		 * cuMemAlloc(devicelevel_2Input, Sizeof.INT);
		 * cuMemcpyHtoD(devicelevel_2Input, Pointer.to(new int[]{level_2}),
		 * Sizeof.INT);
		 * 
		 * //level_3 CUdeviceptr devicelevel_3Input = new CUdeviceptr();
		 * cuMemAlloc(devicelevel_3Input, Sizeof.INT);
		 * cuMemcpyHtoD(devicelevel_3Input, Pointer.to(new int[]{level_3}),
		 * Sizeof.INT);
		 * 
		 * //level_4 CUdeviceptr devicelevel_4Input = new CUdeviceptr();
		 * cuMemAlloc(devicelevel_4Input, Sizeof.INT);
		 * cuMemcpyHtoD(devicelevel_4Input, Pointer.to(new int[]{level_4}),
		 * Sizeof.INT);
		 * 
		 */
		// DAT
		CUdeviceptr deviceDATInput = new CUdeviceptr();
		cuMemAlloc(deviceDATInput, DAT.length * Sizeof.LONG);
		cuMemcpyHtoD(deviceDATInput, Pointer.to(DAT), DAT.length * Sizeof.LONG);

		// link1_2
		CUdeviceptr device12Input = new CUdeviceptr();
		cuMemAlloc(device12Input, link1_2.length * Sizeof.LONG);
		cuMemcpyHtoD(device12Input, Pointer.to(link1_2), link1_2.length * Sizeof.LONG);

		// link2_3
		CUdeviceptr device23Input = new CUdeviceptr();
		cuMemAlloc(device23Input, link2_3.length * Sizeof.LONG);
		cuMemcpyHtoD(device23Input, Pointer.to(link2_3), link2_3.length * Sizeof.LONG);

		// link3_4
		CUdeviceptr device34Input = new CUdeviceptr();
		cuMemAlloc(device34Input, link3_4.length * Sizeof.LONG);
		cuMemcpyHtoD(device34Input, Pointer.to(link3_4), link3_4.length * Sizeof.LONG);

		// Allocate device output memory
		CUdeviceptr deviceOutput = new CUdeviceptr();
		cuMemAlloc(deviceOutput, FAMILY_Size * DAT_Size * Sizeof.LONG);

		// Set up the kernel parameters: A pointer to an array
		// of pointers which point to the actual values.

		/*
		 * int DAT_Size, int DAT_Lenght, int FAMILY_Size, long long *DAT, int
		 * level_1, int level_2, int level_3, int level_4, long long *link1_2,
		 * long long *link2_3, long long *link3_4 long long *result
		 */

		Pointer kernelParameters = Pointer.to(Pointer.to(new int[] { DAT_Size }), // new
																					// int[]{DAT_Size}
				Pointer.to(new int[] { DAT_Lenght }), // deviceDATLenghtInput
														// new int[]{DAT_Lenght}
				Pointer.to(new int[] { FAMILY_Size }), // deviceFAMILY_SizeInput
				Pointer.to(deviceDATInput), Pointer.to(new int[] { level_1 }), // devicelevel_1Input
				Pointer.to(new int[] { level_2 }), // new int[]{level_2}
				Pointer.to(new int[] { level_3 }), // new int[]{level_3}
				Pointer.to(new int[] { level_4 }), // new int[]{level_4}
				Pointer.to(device12Input), Pointer.to(device23Input), Pointer.to(device34Input), Pointer.to(deviceOutput));

		// Call the kernel function.
		int errorCode = 0;

		errorCode = cuLaunchKernel(function,
				// gridSizeX, 1, 1, // Grid dimension
				gridSizeX, 1, 1, blockSizeX, 1, 1, // Block dimension blockSizeX
				0, null, // Shared memory size and stream
				kernelParameters, null // Kernel- and extra parameters
		);
		cuCtxSynchronize();
		if (errorCode != CUresult.CUDA_SUCCESS) {
			System.out.println("Cuda error = " /* + gridSizeX */);
		}

		// Allocate host output memory and copy the device output
		// to the host.
		long hostOutput[] = new long[FAMILY_Size * DAT_Size];
		cuMemcpyDtoH(Pointer.to(hostOutput), deviceOutput, FAMILY_Size * DAT_Size * Sizeof.LONG);
		// System.out.println("CUDA RESULT :");
		// for (int i = 0; i < hostOutput.length; i++){
		// System.out.println(" VAL: " + String.valueOf(hostOutput[i]));
		// }
		// System.out.println("Size = " + hostOutput.length);

		// result float[family size];
		// System.out.println("CUDA = ");
		int m = 0;
		long[][] resultArr = new long[FAMILY_Size][DAT_Size];

		float[] chkRez = new float[FAMILY_Size];
		// long[] intRez = new long[];
		for (int i = 0; i < DAT_Size; i++) { // i - data index
			for (int j = 0; j < FAMILY_Size; j++) { // j - family index
				//
				resultArr[j][i] = hostOutput[m];
				//
				long resume = hostOutput[m];
				int numLeftBit = NumberOfSetBits((int) (resume >> 32));
				int numRightBit = NumberOfSetBits((int) (resume));
				float compareRes = 0;
				if (resume != 0) {
					compareRes = (float) numLeftBit / (numLeftBit + numRightBit);
				}
				// comparD
				// System.out.println("TEXT: " + comparD.getTextData(i) + " REX
				// = " + compareRes);
				chkRez[j] = chkRez[j] + Math.abs(comparD.getIndexData(i) - compareRes);
				m++;
			}
		}
		cuMemFree(deviceDATInput);
		cuMemFree(device12Input);
		cuMemFree(device23Input);
		cuMemFree(device34Input);
		cuMemFree(deviceOutput);
		JCuda.cudaFree(kernelParameters);
		JCudaDriver.cuCtxDetach(context);
		return resultArr;
	}

	/*
	 * public static void test () throws IOException { // Enable exceptions and
	 * omit all subsequent error checks JCudaDriver.setExceptionsEnabled(true);
	 * 
	 * // Create the PTX file by calling the NVCC String ptxFileName =
	 * preparePtxFile("test.cu");
	 * 
	 * // Initialize the driver and create a context for the first device.
	 * cuInit(0); CUdevice device = new CUdevice(); cuDeviceGet(device, 0);
	 * CUcontext context = new CUcontext(); cuCtxCreate(context, 0, device);
	 * 
	 * // Load the ptx file. CUmodule module = new CUmodule();
	 * cuModuleLoad(module, ptxFileName);
	 * 
	 * // Obtain a function pointer to the "add" function. CUfunction function =
	 * new CUfunction(); cuModuleGetFunction(function, module, "VecAdd");
	 * 
	 * int numElements = 100000;
	 * 
	 * // Allocate and fill the host input data long hostInputA[] = new
	 * long[numElements]; long hostInputB[] = new long[numElements]; for(int i =
	 * 0; i < numElements; i++) { hostInputA[i] = (long)i; hostInputB[i] =
	 * (long)i; }
	 * 
	 * // Allocate the device input data, and copy the // host input data to the
	 * device CUdeviceptr deviceInputA = new CUdeviceptr();
	 * cuMemAlloc(deviceInputA, numElements * Sizeof.LONG);
	 * cuMemcpyHtoD(deviceInputA, Pointer.to(hostInputA), numElements *
	 * Sizeof.LONG); CUdeviceptr deviceInputB = new CUdeviceptr();
	 * cuMemAlloc(deviceInputB, numElements * Sizeof.LONG);
	 * cuMemcpyHtoD(deviceInputB, Pointer.to(hostInputB), numElements *
	 * Sizeof.LONG);
	 * 
	 * // Allocate device output memory CUdeviceptr deviceOutput = new
	 * CUdeviceptr(); cuMemAlloc(deviceOutput, numElements * Sizeof.LONG);
	 * 
	 * // Set up the kernel parameters: A pointer to an array // of pointers
	 * which point to the actual values. Pointer kernelParameters = Pointer.to(
	 * Pointer.to(new int[]{numElements}), Pointer.to(deviceInputA),
	 * Pointer.to(deviceInputB), Pointer.to(deviceOutput) );
	 * 
	 * // Call the kernel function. int blockSizeX = 256; int gridSizeX =
	 * (int)Math.ceil((double)numElements / blockSizeX);
	 * cuLaunchKernel(function, gridSizeX, 1, 1, // Grid dimension blockSizeX,
	 * 1, 1, // Block dimension 0, null, // Shared memory size and stream
	 * kernelParameters, null // Kernel- and extra parameters );
	 * cuCtxSynchronize();
	 * 
	 * // Allocate host output memory and copy the device output // to the host.
	 * float hostOutput[] = new float[numElements];
	 * cuMemcpyDtoH(Pointer.to(hostOutput), deviceOutput, numElements *
	 * Sizeof.LONG);
	 * 
	 * // Verify the result boolean passed = true; for(int i = 0; i <
	 * numElements; i++) { long expected = i+i; if (hostOutput[i] - expected >
	 * 0) { System.out.println( "At index "+i+ " found "+hostOutput[i]+
	 * " but expected "+expected); passed = false; break; } }
	 * System.out.println("Test "+(passed?"PASSED":"FAILED"));
	 * 
	 * // Clean up. cuMemFree(deviceInputA); cuMemFree(deviceInputB);
	 * cuMemFree(deviceOutput); }
	 * 
	 */

	/**
	 * The extension of the given file name is replaced with "ptx". If the file
	 * with the resulting name does not exist, it is compiled from the given
	 * file using NVCC. The name of the PTX file is returned.
	 *
	 * @param cuFileName
	 *            The name of the .CU file
	 * @return The name of the PTX file
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	private static String preparePtxFile(String cuFileName, int second_lev, int tride_lev) throws IOException {
		int endIndex = cuFileName.lastIndexOf('.');
		if (endIndex == -1) {
			endIndex = cuFileName.length() - 1;
		}
		String ptxFileName = String.valueOf(second_lev) + "_" + String.valueOf(tride_lev) + "_" + cuFileName.substring(0, endIndex + 1) + "ptx";
		String newcuFileName = String.valueOf(second_lev) + "_" + String.valueOf(tride_lev) + "_" + cuFileName.substring(0, endIndex + 1) + "cu";

		File ptxFile = new File(ptxFileName);
		if (ptxFile.exists()) {
			return ptxFileName;
		}

		Path inpath = Paths.get(cuFileName);
		Charset charset = StandardCharsets.UTF_8;
		String content = new String(Files.readAllBytes(inpath), charset);
		content = content.replaceAll("@second_lev@", String.valueOf(second_lev));
		content = content.replaceAll("@tride_lev@", String.valueOf(tride_lev));
		Path outpath = Paths.get(newcuFileName);
		Files.write(outpath, content.getBytes(charset));

		File cuFile = new File(newcuFileName);
		if (!cuFile.exists()) {
			throw new IOException("Input file not found: " + newcuFileName);
		}
		String modelString = "-m" + System.getProperty("sun.arch.data.model");
		String command = "nvcc " + modelString + " -ptx " + cuFile.getPath() + " -o " + ptxFileName;

		System.out.println("Executing\n" + command);
		Process process = Runtime.getRuntime().exec(command);

		String errorMessage = new String(toByteArray(process.getErrorStream()));
		String outputMessage = new String(toByteArray(process.getInputStream()));
		int exitValue = 0;
		try {
			exitValue = process.waitFor();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IOException("Interrupted while waiting for nvcc output", e);
		}

		if (exitValue != 0) {
			System.out.println("nvcc process exitValue " + exitValue);
			System.out.println("errorMessage:\n" + errorMessage);
			System.out.println("outputMessage:\n" + outputMessage);
			throw new IOException("Could not create .ptx file: " + errorMessage);
		}

		System.out.println("Finished creating PTX file");
		return ptxFileName;
	}

	/**
	 * Fully reads the given InputStream and returns it as a byte array
	 *
	 * @param inputStream
	 *            The input stream to read
	 * @return The byte array containing the data from the input stream
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	private static byte[] toByteArray(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte buffer[] = new byte[8192];
		while (true) {
			int read = inputStream.read(buffer);
			if (read == -1) {
				break;
			}
			baos.write(buffer, 0, read);
		}
		return baos.toByteArray();
	}

	@SuppressWarnings("unused")
	private static void printDevProp(cudaDeviceProp prop) {
		System.out.println("totalGlobalMem = " + prop.totalGlobalMem + " bytes");
		System.out.println("sharedMemPerBlock = " + prop.sharedMemPerBlock + " bytes");
		System.out.println("warpSize = " + prop.warpSize + " ");
		System.out.println("maxThreadsPerBlock = " + prop.maxThreadsPerBlock + " ");
		System.out.println("maxThreadsDimX = " + prop.maxThreadsDim[0] + " maxThreadsDimY = " + prop.maxThreadsDim[1] + " maxThreadsDimZ = " + prop.maxThreadsDim[2]);
		System.out.println("maxGridSizeX = " + prop.maxGridSize[0] + " maxGridSizeY = " + prop.maxGridSize[1] + " maxGridSizeZ = " + prop.maxGridSize[2]);
		System.out.println("multiProcessorCount = " + prop.multiProcessorCount + " ");
		System.out.println("concurrentKernels = " + prop.concurrentKernels + " ");
		System.out.println("computeMode = " + prop.computeMode + " 0 - Default mode ");
		/**
		 * name[256] is an ASCII string identifying the device; totalGlobalMem
		 * is the total amount of global memory available on the device in
		 * bytes; sharedMemPerBlock is the maximum amount of shared memory
		 * available to a thread block in bytes; this amount is shared by all
		 * thread blocks simultaneously resident on a multiprocessor;
		 * regsPerBlock is the maximum number of 32-bit registers available to a
		 * thread block; this number is shared by all thread blocks
		 * simultaneously resident on a multiprocessor; warpSize is the warp
		 * size in threads; memPitch is the maximum pitch in bytes allowed by
		 * the memory copy functions that involve memory regions allocated
		 * through cudaMallocPitch(); maxThreadsPerBlock is the maximum number
		 * of threads per block; maxThreadsDim[3] contains the maximum size of
		 * each dimension of a block; maxGridSize[3] contains the maximum size
		 * of each dimension of a grid; clockRate is the clock frequency in
		 * kilohertz; totalConstMem is the total amount of constant memory
		 * available on the device in bytes; major, minor are the major and
		 * minor revision numbers defining the device's compute capability;
		 * textureAlignment is the alignment requirement; texture base addresses
		 * that are aligned to textureAlignment bytes do not need an offset
		 * applied to texture fetches; deviceOverlap is 1 if the device can
		 * concurrently copy memory between host and device while executing a
		 * kernel, or 0 if not; multiProcessorCount is the number of
		 * multiprocessors on the device; kernelExecTimeoutEnabled is 1 if there
		 * is a run time limit for kernels executed on the device, or 0 if not.
		 * integrated is 1 if the device is an integrated (motherboard) GPU and
		 * 0 if it is a discrete (card) component. canMapHostMemory is 1 if the
		 * device can map host memory into the CUDA address space for use with
		 * cudaHostAlloc()/cudaHostGetDevicePointer(), or 0 if not; computeMode
		 * is the compute mode that the device is currently in. Available modes
		 * are as follows:
		 * 
		 * cudaComputeModeDefault: Default mode - Device is not restricted and
		 * multiple threads can use cudaSetDevice() with this device.
		 * cudaComputeModeExclusive: Compute-exclusive mode - Only one thread
		 * will be able to use cudaSetDevice() with this device.
		 * cudaComputeModeProhibited: Compute-prohibited mode - No threads can
		 * use cudaSetDevice() with this device. Any errors from calling
		 * cudaSetDevice() with an exclusive (and occupied) or prohibited device
		 * will only show up after a non-device management runtime function is
		 * called. At that time, cudaErrorNoDevice will be returned.
		 * 
		 * concurrentKernels is 1 if the device supports executing multiple
		 * kernels within the same context simultaneously, or 0 if not. It is
		 * not guaranteed that multiple kernels will be resident on the device
		 * concurrently so this feature should not be relied upon for
		 * correctness; ECCEnabled is 1 if the device has ECC support turned on,
		 * or 0 if not. pciBusID is the PCI bus identifier of the device.
		 * pciDeviceID is the PCI device (sometimes called slot) identifier of
		 * the device. tccDriver is 1 if the device is using a TCC driver or 0
		 * if not.
		 */
	}

}
