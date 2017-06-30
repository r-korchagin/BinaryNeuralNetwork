package info.binarynetwork.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import info.binarynetwork.core.objects.CRC64;
import info.binarynetwork.interfaces.InputBinary64Data;
import info.binarynetwork.interfaces.LoadCompareData;
import info.binarynetwork.objects.Binary64Data;
import info.binarynetwork.objects.CompareData;

public class GetBinary64Data implements InputBinary64Data {

	private Binary64Data data;

	// convert String data to long array
	private static long[] CRC64toStringData(String inStr) {
		StringTokenizer st = new StringTokenizer(inStr);
		List<String> newData = new ArrayList<String>();

		while (st.hasMoreElements()) {
			String el = (String) st.nextElement();
			if (el.length() > 1) {
				String formatterEl = el.replaceAll("[^\\p{L}\\p{Nd}]+", "");
				newData.add(formatterEl);
			}
		}
		long[] resultD = new long[newData.size()];
		for (int i = 0; i < newData.size(); i++) {
			byte bytes[] = newData.get(i).getBytes();
			resultD[i] = CRC64.checksum(bytes);
		}
		return resultD;
	}

	public GetBinary64Data(LoadCompareData dataText) {
		Binary64Data trans = new Binary64Data();
		CompareData src = dataText.loadInputData();
		for (int i = 0; i < src.getSize(); i++) {
			trans.appendData(src.getIndexData(i), CRC64toStringData(src.getTextData(i)));
		}
		this.data = trans;
	}

	public void setInputData(LoadCompareData dataText) {
		Binary64Data trans = new Binary64Data();
		CompareData src = dataText.loadInputData();
		for (int i = 0; i < src.getSize(); i++) {
			trans.appendData(src.getIndexData(i), CRC64toStringData(src.getTextData(i)));
		}
		this.data = trans;
	}

	public Binary64Data getInputData() {
		return this.data;
	}

}
