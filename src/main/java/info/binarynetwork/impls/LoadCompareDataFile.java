package info.binarynetwork.impls;

import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;

import info.binarynetwork.interfaces.LoadCompareData;
import info.binarynetwork.objects.CompareData;

public class LoadCompareDataFile implements LoadCompareData {

	private String filename;

	public LoadCompareDataFile(String file) {
		this.filename = file;
	}

	public CompareData loadInputData() {
		CompareData comparD = new CompareData();
		Gson filegson = new Gson();
		try {
			FileReader reader = new FileReader(filename);
			comparD = filegson.fromJson(reader, CompareData.class);
			reader.close();
		} catch (IOException e) {
			System.out.println("Can't open file " + filename);
			e.printStackTrace();
		}
		return comparD;
	}

}
