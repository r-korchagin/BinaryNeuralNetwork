package info.binarynetwork.core.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import info.binarynetwork.core.interfaces.NetworkCUDACore;
import info.binarynetwork.objects.Binary32Data;
import info.binarynetwork.objects.Binary64Data;
import info.binarynetwork.objects.neuralElement32;
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

public class NetworkCUDACoreImpl implements NetworkCUDACore {
    /*
     * trhResultArr = NeuralCUDA.compareNeuralLong(thrFamily, thrData,
     * CUDA_Grid_Size_x, CUDA_Block_Size_x, family[0].getLevel2(),
     * family[0].getLevel3(), 0);
     */

    private int cudaDevNum = 0;

    public long[][] compareEl(neuralElement64[] family, Binary64Data data, int CUDAGridSizeX, int CUDABlockSizeX) {

	JCudaDriver.setExceptionsEnabled(false);

	int FAMILY_Size = family.length;
	int DAT_Size = data.getSize();

	// Create the PTX file by calling the NVCC
	String ptxFileName;
	try {
	    ptxFileName = preparePtxFile("newKernel64.cu", family[0].getLevel2(), family[0].getLevel3());
	} catch (IOException e) {
	    e.printStackTrace();
	    return new long[FAMILY_Size][DAT_Size];
	}

	// Initialize the driver and create a context for the first device.
	JCudaDriver.cuInit(0);
	CUdevice device = new CUdevice();
	JCudaDriver.cuDeviceGet(device, cudaDevNum);
	CUcontext context = new CUcontext();
	JCudaDriver.cuCtxCreate(context, 0, device);

	// cudaDeviceProp prop = new cudaDeviceProp();
	// JCuda.cudaGetDeviceProperties(prop, 0);
	// printDevProp(prop);

	// Load the ptx file.
	CUmodule module = new CUmodule();
	JCudaDriver.cuModuleLoad(module, ptxFileName);

	// Obtain a function pointer to the "neural" function.
	CUfunction function = new CUfunction();
	JCudaDriver.cuModuleGetFunction(function, module, "neural");

	// Create input data
	int maxSize = 0;
	long[][] dat = new long[data.getSize()][100];
	for (int i = 0; i < data.getSize(); i++) {
	    long[] currDat = data.getBinaryByID(i);
	    for (int k = 0; k < currDat.length; k++) {
		dat[i][k] = currDat[k];
	    }
	    if (currDat.length > maxSize) {
		maxSize = currDat.length;
	    }
	}

	int DAT_Lenght = maxSize;
	long[] DAT = new long[DAT_Size * DAT_Lenght + DAT_Lenght];

	for (int i = 0; i < DAT_Size; i++) {
	    for (int j = 0; j < DAT_Lenght; j++) {
		DAT[i * DAT_Lenght + j] = dat[i][j];
	    }
	}

	int level_1 = 0;
	int level_2 = 0;
	int level_3 = 0;
	int level_4 = 0;
	long[] link1_2 = new long[family.length * family[0].getLink1_2().length + family[0].getLink1_2().length];
	long[] link2_3 = new long[family.length * family[0].getLink2_3().length + family[0].getLink2_3().length];
	long[] link3_4 = new long[family.length * family[0].getLink3_4().length + family[0].getLink3_4().length];

	for (int i = 0; i < family.length; i++) {
	    // i -index family
	    level_1 = family[i].getLevel1();
	    level_2 = family[i].getLevel2();
	    level_3 = family[i].getLevel3();
	    level_4 = family[i].getLevel4();
	    for (int j = 0; j < family[i].getLink1_2().length; j++) {
		link1_2[i * family[i].getLink1_2().length + j] = family[i].getLink1_2()[j];
	    }
	    for (int j = 0; j < family[i].getLink2_3().length; j++) {
		link2_3[i * family[i].getLink2_3().length + j] = family[i].getLink2_3()[j];
	    }
	    // System.out.println(family[i].getLink2_3().length);
	    for (int j = 0; j < family[i].getLink3_4().length; j++) {
		link3_4[i * family[i].getLink3_4().length + j] = family[i].getLink3_4()[j];
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
	JCudaDriver.cuMemAlloc(deviceDATInput, DAT.length * Sizeof.LONG);
	JCudaDriver.cuMemcpyHtoD(deviceDATInput, Pointer.to(DAT), DAT.length * Sizeof.LONG);

	// link1_2
	CUdeviceptr device12Input = new CUdeviceptr();
	JCudaDriver.cuMemAlloc(device12Input, link1_2.length * Sizeof.LONG);
	JCudaDriver.cuMemcpyHtoD(device12Input, Pointer.to(link1_2), link1_2.length * Sizeof.LONG);

	// link2_3
	CUdeviceptr device23Input = new CUdeviceptr();
	JCudaDriver.cuMemAlloc(device23Input, link2_3.length * Sizeof.LONG);
	JCudaDriver.cuMemcpyHtoD(device23Input, Pointer.to(link2_3), link2_3.length * Sizeof.LONG);

	// link3_4
	CUdeviceptr device34Input = new CUdeviceptr();
	JCudaDriver.cuMemAlloc(device34Input, link3_4.length * Sizeof.LONG);
	JCudaDriver.cuMemcpyHtoD(device34Input, Pointer.to(link3_4), link3_4.length * Sizeof.LONG);

	// Allocate device output memory
	CUdeviceptr deviceOutput = new CUdeviceptr();
	JCudaDriver.cuMemAlloc(deviceOutput, FAMILY_Size * DAT_Size * Sizeof.LONG);

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
		Pointer.to(device12Input), Pointer.to(device23Input), Pointer.to(device34Input),
		Pointer.to(deviceOutput));

	// Call the kernel function.
	int errorCode = 0;

	errorCode = JCudaDriver.cuLaunchKernel(function,
		// gridSizeX, 1, 1, // Grid dimension
		CUDAGridSizeX, 1, 1, CUDABlockSizeX, 1, 1, // Block dimension
							   // blockSizeX
		0, null, // Shared memory size and stream
		kernelParameters, null // Kernel- and extra parameters
	);
	JCudaDriver.cuCtxSynchronize();
	if (errorCode != CUresult.CUDA_SUCCESS) {
	    System.out.println("Cuda error = " /* + gridSizeX */);
	}

	// Allocate host output memory and copy the device output
	// to the host.
	long hostOutput[] = new long[FAMILY_Size * DAT_Size];
	JCudaDriver.cuMemcpyDtoH(Pointer.to(hostOutput), deviceOutput, FAMILY_Size * DAT_Size * Sizeof.LONG);
	// System.out.println("CUDA RESULT :");
	// for (int i = 0; i < hostOutput.length; i++){
	// System.out.println(" VAL: " + String.valueOf(hostOutput[i]));
	// }
	// System.out.println("Size = " + hostOutput.length);

	// result float[family size];
	// System.out.println("CUDA = ");
	int m = 0;
	// float[] chkRez = new float[FAMILY_Size];
	long[][] resultArr = new long[FAMILY_Size][DAT_Size];

	// long[] intRez = new long[];
	for (int i = 0; i < DAT_Size; i++) { // i - data index
	    for (int j = 0; j < FAMILY_Size; j++) { // j - family index
		resultArr[i][j] = hostOutput[m];
		// long resume = hostOutput[m];
		// int numLeftBit = neuralElementUtil.NumberOfSetBits((int)
		// (resume >> 32));
		// int numRightBit = neuralElementUtil.NumberOfSetBits((int)
		// (resume));
		// float compareRes = 0;
		// if (resume != 0) {
		// compareRes = (float) numLeftBit / (numLeftBit + numRightBit);
		// }
		// comparD
		// System.out.println("TEXT: " + comparD.getTextData(i) + " REX
		// = " + compareRes);
		// chkRez[j] = chkRez[j] + Math.abs(comparD.getIndexData(i) -
		// compareRes);
		m++;
	    }
	}
	JCudaDriver.cuMemFree(deviceDATInput);
	JCudaDriver.cuMemFree(device12Input);
	JCudaDriver.cuMemFree(device23Input);
	JCudaDriver.cuMemFree(device34Input);
	JCudaDriver.cuMemFree(deviceOutput);
	JCuda.cudaFree(kernelParameters);
	JCudaDriver.cuCtxDetach(context);
	return resultArr;
    }

    public int[][] compareEl(neuralElement32[] family, Binary32Data data, int CUDAGridSizeX, int CUDABlockSizeX) {
	JCudaDriver.setExceptionsEnabled(false);

	int FAMILY_Size = family.length;
	int DAT_Size = data.getSize();

	// Create the PTX file by calling the NVCC
	String ptxFileName;
	try {
	    ptxFileName = preparePtxFile("newKernel32.cu", family[0].getLevel2(), family[0].getLevel3());
	} catch (IOException e) {
	    e.printStackTrace();
	    return new int[FAMILY_Size][DAT_Size];
	}

	// Initialize the driver and create a context for the first device.
	JCudaDriver.cuInit(0);
	CUdevice device = new CUdevice();
	JCudaDriver.cuDeviceGet(device, cudaDevNum);
	CUcontext context = new CUcontext();
	JCudaDriver.cuCtxCreate(context, 0, device);

	// cudaDeviceProp prop = new cudaDeviceProp();
	// JCuda.cudaGetDeviceProperties(prop, 0);
	// printDevProp(prop);

	// Load the ptx file.
	CUmodule module = new CUmodule();
	JCudaDriver.cuModuleLoad(module, ptxFileName);

	// Obtain a function pointer to the "neural" function.
	CUfunction function = new CUfunction();
	JCudaDriver.cuModuleGetFunction(function, module, "neural");

	// Create input data
	int maxSize = 0;
	int[][] dat = new int[data.getSize()][100];
	for (int i = 0; i < data.getSize(); i++) {
	    int[] currDat = data.getBinaryByID(i);
	    for (int k = 0; k < currDat.length; k++) {
		dat[i][k] = currDat[k];
	    }
	    if (currDat.length > maxSize) {
		maxSize = currDat.length;
	    }
	}

	int DAT_Lenght = maxSize;
	int[] DAT = new int[DAT_Size * DAT_Lenght + DAT_Lenght];

	for (int i = 0; i < DAT_Size; i++) {
	    for (int j = 0; j < DAT_Lenght; j++) {
		DAT[i * DAT_Lenght + j] = dat[i][j];
	    }
	}

	int level_1 = 0;
	int level_2 = 0;
	int level_3 = 0;
	int level_4 = 0;
	int[] link1_2 = new int[family.length * family[0].getLink1_2().length + family[0].getLink1_2().length];
	int[] link2_3 = new int[family.length * family[0].getLink2_3().length + family[0].getLink2_3().length];
	int[] link3_4 = new int[family.length * family[0].getLink3_4().length + family[0].getLink3_4().length];

	for (int i = 0; i < family.length; i++) {
	    // i -index family
	    level_1 = family[i].getLevel1();
	    level_2 = family[i].getLevel2();
	    level_3 = family[i].getLevel3();
	    level_4 = family[i].getLevel4();
	    for (int j = 0; j < family[i].getLink1_2().length; j++) {
		link1_2[i * family[i].getLink1_2().length + j] = family[i].getLink1_2()[j];
	    }
	    for (int j = 0; j < family[i].getLink2_3().length; j++) {
		link2_3[i * family[i].getLink2_3().length + j] = family[i].getLink2_3()[j];
	    }
	    // System.out.println(family[i].getLink2_3().length);
	    for (int j = 0; j < family[i].getLink3_4().length; j++) {
		link3_4[i * family[i].getLink3_4().length + j] = family[i].getLink3_4()[j];
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
	JCudaDriver.cuMemAlloc(deviceDATInput, DAT.length * Sizeof.INT);
	JCudaDriver.cuMemcpyHtoD(deviceDATInput, Pointer.to(DAT), DAT.length * Sizeof.INT);

	// link1_2
	CUdeviceptr device12Input = new CUdeviceptr();
	JCudaDriver.cuMemAlloc(device12Input, link1_2.length * Sizeof.INT);
	JCudaDriver.cuMemcpyHtoD(device12Input, Pointer.to(link1_2), link1_2.length * Sizeof.INT);

	// link2_3
	CUdeviceptr device23Input = new CUdeviceptr();
	JCudaDriver.cuMemAlloc(device23Input, link2_3.length * Sizeof.INT);
	JCudaDriver.cuMemcpyHtoD(device23Input, Pointer.to(link2_3), link2_3.length * Sizeof.INT);

	// link3_4
	CUdeviceptr device34Input = new CUdeviceptr();
	JCudaDriver.cuMemAlloc(device34Input, link3_4.length * Sizeof.INT);
	JCudaDriver.cuMemcpyHtoD(device34Input, Pointer.to(link3_4), link3_4.length * Sizeof.INT);

	// Allocate device output memory
	CUdeviceptr deviceOutput = new CUdeviceptr();
	JCudaDriver.cuMemAlloc(deviceOutput, FAMILY_Size * DAT_Size * Sizeof.INT);

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
		Pointer.to(device12Input), Pointer.to(device23Input), Pointer.to(device34Input),
		Pointer.to(deviceOutput));

	// Call the kernel function.
	int errorCode = 0;

	errorCode = JCudaDriver.cuLaunchKernel(function,
		// gridSizeX, 1, 1, // Grid dimension
		CUDAGridSizeX, 1, 1, CUDABlockSizeX, 1, 1, // Block dimension
							   // blockSizeX
		0, null, // Shared memory size and stream
		kernelParameters, null // Kernel- and extra parameters
	);
	JCudaDriver.cuCtxSynchronize();
	if (errorCode != CUresult.CUDA_SUCCESS) {
	    System.out.println("Cuda error = " /* + gridSizeX */);
	}

	// Allocate host output memory and copy the device output
	// to the host.
	int hostOutput[] = new int[FAMILY_Size * DAT_Size];
	JCudaDriver.cuMemcpyDtoH(Pointer.to(hostOutput), deviceOutput, FAMILY_Size * DAT_Size * Sizeof.INT);
	// System.out.println("CUDA RESULT :");
	// for (int i = 0; i < hostOutput.length; i++){
	// System.out.println(" VAL: " + String.valueOf(hostOutput[i]));
	// }
	// System.out.println("Size = " + hostOutput.length);

	// result float[family size];
	// System.out.println("CUDA = ");
	int m = 0;
	// float[] chkRez = new float[FAMILY_Size];
	int[][] resultArr = new int[FAMILY_Size][DAT_Size];

	// long[] intRez = new long[];
	for (int i = 0; i < DAT_Size; i++) { // i - data index
	    for (int j = 0; j < FAMILY_Size; j++) { // j - family index
		resultArr[i][j] = hostOutput[m];
		// long resume = hostOutput[m];
		// int numLeftBit = neuralElementUtil.NumberOfSetBits((int)
		// (resume >> 32));
		// int numRightBit = neuralElementUtil.NumberOfSetBits((int)
		// (resume));
		// float compareRes = 0;
		// if (resume != 0) {
		// compareRes = (float) numLeftBit / (numLeftBit + numRightBit);
		// }
		// comparD
		// System.out.println("TEXT: " + comparD.getTextData(i) + " REX
		// = " + compareRes);
		// chkRez[j] = chkRez[j] + Math.abs(comparD.getIndexData(i) -
		// compareRes);
		m++;
	    }
	}
	JCudaDriver.cuMemFree(deviceDATInput);
	JCudaDriver.cuMemFree(device12Input);
	JCudaDriver.cuMemFree(device23Input);
	JCudaDriver.cuMemFree(device34Input);
	JCudaDriver.cuMemFree(deviceOutput);
	JCuda.cudaFree(kernelParameters);
	JCudaDriver.cuCtxDetach(context);
	return resultArr;
    }

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
	String ptxFileName = String.valueOf(second_lev) + "_" + String.valueOf(tride_lev) + "_"
		+ cuFileName.substring(0, endIndex + 1) + "ptx";
	String newcuFileName = String.valueOf(second_lev) + "_" + String.valueOf(tride_lev) + "_"
		+ cuFileName.substring(0, endIndex + 1) + "cu";

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

    public int getCudaDevNum() {
	return cudaDevNum;
    }

    public void setCudaDevNum(int cudaDevNum) {
	this.cudaDevNum = cudaDevNum;
    }

}
