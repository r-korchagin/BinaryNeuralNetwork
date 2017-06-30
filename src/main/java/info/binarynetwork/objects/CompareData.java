package info.binarynetwork.objects;

import java.util.ArrayList;
import java.util.List;

public class CompareData {

	private List<String> textData = new ArrayList<String>();
	private List<Float> indexData = new ArrayList<Float>();

	public void setData(String data, double ind) {
		this.textData.add(data);
		this.indexData.add(new Float(ind));
	}

	public void removeData(int i) {
		this.textData.remove(i);
		this.indexData.remove(i);
	}

	// getter
	public String getTextData(int i) {
		return this.textData.get(i);
	}

	public float getIndexData(int i) {
		return this.indexData.get(i);
	}

	public int getSize() {
		return this.textData.size();
	}

}
