package info.binarynetwork.impls;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;

import info.binarynetwork.interfaces.NetworkFamilyLoader;
import info.binarynetwork.objects.neuralElement64;
import info.binarynetwork.objects.neuralElement32;

public class FamilyLoaderFromJSON implements NetworkFamilyLoader {

    public neuralElement64 loadFamilyEl64(String filename) {
	neuralElement64 comparD = new neuralElement64();
	Gson filegson = new Gson();
	try {
	    FileReader reader = new FileReader(filename);
	    comparD = filegson.fromJson(reader, neuralElement64.class);
	    reader.close();
	} catch (IOException e) {
	    System.out.println("Can't open file " + filename);
	    e.printStackTrace();
	}
	return comparD;
    }

    public void saveFamilyEl64(String fileName, neuralElement64 familyEl) {
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

    public neuralElement32 loadFamilyEl32(String filename) {
	neuralElement32 comparD = new neuralElement32();
	Gson filegson = new Gson();
	try {
	    FileReader reader = new FileReader(filename);
	    comparD = filegson.fromJson(reader, neuralElement32.class);
	    reader.close();
	} catch (IOException e) {
	    System.out.println("Can't open file " + filename);
	    e.printStackTrace();
	}
	return comparD;
    }

    public void saveFamilyEl32(String fileName, neuralElement32 familyEl) {
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
