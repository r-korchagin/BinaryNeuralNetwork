/**
 *  Load Network Configuration From File
 */
package info.binarynetwork.impls;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.ini4j.Ini;

import info.binarynetwork.interfaces.LoadNeuralConfig;
import info.binarynetwork.objects.NeuralConfig;

/**
 * @author Roman.Korchagin
 * 
 */

public class LoadConfigFromFile implements LoadNeuralConfig {

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.binarynetwork.interfaces.LoadNeuralConfig#loadConfig(java.lang.
	 * String)
	 */
	private String fileName;

	public LoadConfigFromFile(String filename) {
		this.fileName = filename;
	}

	public NeuralConfig loadConfig() {
		NeuralConfig ResultConfig = new NeuralConfig();
		try {

			Ini ini = new Ini();
			ini.load(new FileReader(fileName));

			Ini.Section driverSection = ini.get("driver");
			ResultConfig.setCP_OR_CUDA(Boolean.parseBoolean(driverSection.get("cp_or_cuda")));

			Ini.Section poolSection = ini.get("pool");
			ResultConfig.setTHREAD_POOL_SIZE(Integer.parseInt(poolSection.get("thread_pool_size")));

			Ini.Section cudaSection = ini.get("cuda");
			ResultConfig.setCUDA_CP_COMPARE_STATE(Boolean.parseBoolean(cudaSection.get("cuda_cp_compare_state")));
			ResultConfig.setCUDA_GRID_SIZE_X(Integer.parseInt(cudaSection.get("cuda_grid_size_x")));
			ResultConfig.setCUDA_BLOCK_SIZE_X(Integer.parseInt(cudaSection.get("cuda_block_size_x")));

			Ini.Section neuralSection = ini.get("neural");
			ResultConfig.setFAMILY_SIZE(Integer.parseInt(neuralSection.get("family_size")));
			ResultConfig.setFIRST_LEV(Integer.parseInt(neuralSection.get("first_lev")));
			ResultConfig.setSECOND_LEV(Integer.parseInt(neuralSection.get("second_lev")));
			ResultConfig.setTRIDE_LEV(Integer.parseInt(neuralSection.get("tride_lev")));

			Ini.Section conditionSection = ini.get("condition");
			ResultConfig.setSTOP_RESULT(Float.parseFloat(conditionSection.get("stop_deviation_result")));
			ResultConfig.setMAXIUM_COUNT_ITERATION(Integer.parseInt(conditionSection.get("maximum_count_iteration")));

		} catch (FileNotFoundException e1) {
			System.out.println("File config.ini not found");
			e1.printStackTrace();
		} catch (IOException e) {
			System.out.println("Can't read file config.ini");
			e.printStackTrace();
		}
		return ResultConfig;
	}

}
