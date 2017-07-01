package info.binarynetwork.objects;

import java.util.ArrayList;
import java.util.List;

public class Binary64Data {
    private List<Float> indexData = new ArrayList<Float>();
    private List<long[]> binaryData = new ArrayList<long[]>();

    public List<Float> getIndexData(int i) {
	return indexData;
    }

    public void setIndexData(List<Float> indexData) {
	this.indexData = indexData;
    }

    public List<long[]> getBinaryData() {
	return binaryData;
    }

    public void setBinaryData(List<long[]> binaryData) {
	this.binaryData = binaryData;
    }

    public void appendData(double indexData, long[] binaryData) {
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

    public long[] getBinaryByID(int i) {
	return this.binaryData.get(i);
    }

    public int getSize() {
	return this.indexData.size();
    }
}
