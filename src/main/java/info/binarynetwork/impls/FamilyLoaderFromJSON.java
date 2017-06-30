package info.binarynetwork.impls;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import info.binarynetwork.interfaces.NetworkFamilyLoader;
import info.binarynetwork.objects.neuralElement;

public class FamilyLoaderFromJSON implements NetworkFamilyLoader {

	public neuralElement loadFamilyEl(String filename) {
		neuralElement comparD = new neuralElement();
		Gson filegson = new Gson();
		try {
			FileReader reader = new FileReader(filename);
			comparD = filegson.fromJson(reader, neuralElement.class);
			reader.close();
		} catch (IOException e) {
			System.out.println("Can't open file " + filename);
			e.printStackTrace();
		}
		return comparD;
	}

	public void saveFamilyEl(String fileName, neuralElement familyEl) {
		try {
			Gson gson = new Gson();
			// write converted json data to a file named "file.json"
			FileWriter writer = new FileWriter(fileName);
			gson.toJson(familyEl, writer);

			writer.flush();
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
