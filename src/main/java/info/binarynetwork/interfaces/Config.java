package info.binarynetwork.interfaces;

import info.binarynetwork.objects.NeuralConfig;

public interface Config {

	void setConfig(LoadNeuralConfig config);

	NeuralConfig getConfig();
}
