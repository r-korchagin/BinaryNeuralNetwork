package info.binarynetwork.impls;

import info.binarynetwork.interfaces.Config;
import info.binarynetwork.interfaces.LoadNeuralConfig;
import info.binarynetwork.objects.NeuralConfig;

public class GetConfig implements Config {

	private NeuralConfig config;

	public GetConfig(LoadNeuralConfig config) {
		this.config = config.loadConfig();
	}

	public void setConfig(LoadNeuralConfig config) {
		this.config = config.loadConfig();
	}

	public NeuralConfig getConfig() {
		return this.config;
	}

}
