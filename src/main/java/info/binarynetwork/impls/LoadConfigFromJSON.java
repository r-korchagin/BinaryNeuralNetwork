package info.binarynetwork.impls;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

import info.binarynetwork.interfaces.LoadNeuralConfig;
import info.binarynetwork.objects.NeuralConfig;

public class LoadConfigFromJSON implements LoadNeuralConfig {

	private String fileName;

	public LoadConfigFromJSON(String filename) {
		this.fileName = filename;
	}

	public NeuralConfig loadConfig() {
		Gson filegson = new Gson();
		NeuralConfig ResultConfig = new NeuralConfig();
		try {
			FileReader reader = new FileReader(fileName);
			ResultConfig = filegson.fromJson(reader, NeuralConfig.class);
			reader.close();
		} catch (IOException e) {
			System.out.println("Can't open file " + fileName);
			e.printStackTrace();
		}
		return ResultConfig;
	}

}
