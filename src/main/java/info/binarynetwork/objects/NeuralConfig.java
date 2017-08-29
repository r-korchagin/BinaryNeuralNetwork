package info.binarynetwork.objects;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * Class for reflect configuration file
 *
 */
public class NeuralConfig {

    @SerializedName("cp_or_cuda")
    private boolean CP_OR_CUDA = false;
    @SerializedName("cuda_cp_compare_state")
    public boolean CUDA_CP_COMPARE_STATE = false;
    @SerializedName("cuda_grid_size_x")
    public int CUDA_GRID_SIZE_X = 0;
    @SerializedName("cuda_block_size_x")
    public int CUDA_BLOCK_SIZE_X = 0;
    @SerializedName("thread_pool_size")
    public int THREAD_POOL_SIZE = 3;
    @SerializedName("family_size")
    public int FAMILY_SIZE = 0;
    @SerializedName("first_lev")
    public int FIRST_LEV = 0;
    @SerializedName("second_lev")
    public int SECOND_LEV = 0;
    @SerializedName("tride_lev")
    public int TRIDE_LEV = 0;
    @SerializedName("stop_deviation_result")
    public float STOP_RESULT = 0;
    @SerializedName("maximum_count_iteration")
    public int MAXIUM_COUNT_ITERATION = 0;

    public boolean isCP_OR_CUDA() {
	return CP_OR_CUDA;
    }

    public void setCP_OR_CUDA(boolean cP_OR_CUDA) {
	CP_OR_CUDA = cP_OR_CUDA;
    }

    public boolean isCUDA_CP_COMPARE_STATE() {
	return CUDA_CP_COMPARE_STATE;
    }

    public void setCUDA_CP_COMPARE_STATE(boolean cUDA_CP_COMPARE_STATE) {
	CUDA_CP_COMPARE_STATE = cUDA_CP_COMPARE_STATE;
    }

    public int getCUDA_GRID_SIZE_X() {
	return CUDA_GRID_SIZE_X;
    }

    public void setCUDA_GRID_SIZE_X(int cUDA_GRID_SIZE_X) {
	CUDA_GRID_SIZE_X = cUDA_GRID_SIZE_X;
    }

    public int getCUDA_BLOCK_SIZE_X() {
	return CUDA_BLOCK_SIZE_X;
    }

    public void setCUDA_BLOCK_SIZE_X(int cUDA_BLOCK_SIZE_X) {
	CUDA_BLOCK_SIZE_X = cUDA_BLOCK_SIZE_X;
    }

    public int getTHREAD_POOL_SIZE() {
	return THREAD_POOL_SIZE;
    }

    public void setTHREAD_POOL_SIZE(int tHREAD_POOL_SIZE) {
	THREAD_POOL_SIZE = tHREAD_POOL_SIZE;
    }

    public int getFAMILY_SIZE() {
	return FAMILY_SIZE;
    }

    public void setFAMILY_SIZE(int fAMILY_SIZE) {
	FAMILY_SIZE = fAMILY_SIZE;
    }

    public int getFIRST_LEV() {
	return FIRST_LEV;
    }

    public void setFIRST_LEV(int fIRST_LEV) {
	FIRST_LEV = fIRST_LEV;
    }

    public int getSECOND_LEV() {
	return SECOND_LEV;
    }

    public void setSECOND_LEV(int sECOND_LEV) {
	SECOND_LEV = sECOND_LEV;
    }

    public int getTRIDE_LEV() {
	return TRIDE_LEV;
    }

    public void setTRIDE_LEV(int tRIDE_LEV) {
	TRIDE_LEV = tRIDE_LEV;
    }

    public float getSTOP_RESULT() {
	return STOP_RESULT;
    }

    public void setSTOP_RESULT(float sTOP_RESULT) {
	STOP_RESULT = sTOP_RESULT;
    }

    public int getMAXIUM_COUNT_ITERATION() {
	return MAXIUM_COUNT_ITERATION;
    }

    public void setMAXIUM_COUNT_ITERATION(int mAXIUM_COUNT_ITERATION) {
	MAXIUM_COUNT_ITERATION = mAXIUM_COUNT_ITERATION;
    }
}
