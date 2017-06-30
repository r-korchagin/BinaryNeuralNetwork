package info.binarynetwork.objects;

import java.util.ArrayList;
import java.util.List;

public class Binary32Data {
	private List<Float> indexData = new ArrayList<Float>();
	private List<int[]> binaryData = new ArrayList<int[]>();

	public List<Float> getIndexData(int i) {
		return indexData;
	}

	public void setIndexData(List<Float> indexData) {
		this.indexData = indexData;
	}

	public List<int[]> getBinaryData() {
		return binaryData;
	}

	public void setBinaryData(List<int[]> binaryData) {
		this.binaryData = binaryData;
	}

	public void appendData(double indexData, int[] binaryData) {
		this.indexData.add(new Float(indexData));
		this.binaryData.add(binaryData);
	}

	public void removeData(int i) {
		this.indexData.remove(i);
		this.binaryData.remove(i);
	}

	public Float getIndexByID(int i) {
		return this.indexData.get(i);
	}

	public int[] getBinaryByID(int i) {
		return this.binaryData.get(i);
	}
}
