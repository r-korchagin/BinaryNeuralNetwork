package info.binarynetwork.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.CRC32;

import info.binarynetwork.interfaces.InputBinary32Data;
import info.binarynetwork.interfaces.LoadCompareData;
import info.binarynetwork.objects.Binary32Data;
import info.binarynetwork.objects.CompareData;

public class GetBinary32Data implements InputBinary32Data {

    private Binary32Data data32;

    // convert String data to long array
    private static int[] CRC32toStringData(String inStr) {
	StringTokenizer st = new StringTokenizer(inStr);
	List<String> newData = new ArrayList<String>();

	while (st.hasMoreElements()) {
	    String el = (String) st.nextElement();
	    if (el.length() > 1) {
		String formatterEl = el.replaceAll("[^\\p{L}\\p{Nd}]+", "");
		newData.add(formatterEl);
	    }
	}
	int[] resultD = new int[newData.size()];
	for (int i = 0; i < newData.size(); i++) {
	    CRC32 crc = new CRC32();
	    crc.update(newData.get(i).getBytes());
	    resultD[i] = (int) crc.getValue();
	}
	return resultD;
    }

    public GetBinary32Data(LoadCompareData dataText) {
	Binary32Data trans = new Binary32Data();
	CompareData src = dataText.loadInputData();
	for (int i = 0; i < src.getSize(); i++) {
	    trans.appendData(src.getIndexData(i), CRC32toStringData(src.getTextData(i)));
	}
	this.data32 = trans;
    }

    public void setInputData(LoadCompareData dataText) {
	Binary32Data trans = new Binary32Data();
	CompareData src = dataText.loadInputData();
	for (int i = 0; i < src.getSize(); i++) {
	    trans.appendData(src.getIndexData(i), CRC32toStringData(src.getTextData(i)));
	}
	this.data32 = trans;
    }

    public Binary32Data getInputData() {
	return this.data32;
    }

}
