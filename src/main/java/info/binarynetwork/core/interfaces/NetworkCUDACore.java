package info.binarynetwork.core.interfaces;

import info.binarynetwork.objects.Binary32Data;
import info.binarynetwork.objects.Binary64Data;
import info.binarynetwork.objects.neuralElement32;
import info.binarynetwork.objects.neuralElement64;

public interface NetworkCUDACore {

    public long[][] compareEl(neuralElement64[] family, Binary64Data data, int CUDAGridSizeX, int CUDABlockSizeX);

    public int[][] compareEl(neuralElement32[] family, Binary32Data data, int CUDAGridSizeX, int CUDABlockSizeX);

}
