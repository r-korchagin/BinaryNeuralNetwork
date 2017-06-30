/**
 * 
 */

package info.binarynetwork.impls;

import info.binarynetwork.interfaces.InputCompareData;
import info.binarynetwork.interfaces.LoadCompareData;
import info.binarynetwork.objects.CompareData;

/**
 * @author Roman.Korchagin
 *
 */
public class GetInputData implements InputCompareData {

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.binarynetwork.interfaces.InputCompareData#setInputData(info.
	 * binarynetwork.interfaces.LoadCompareData)
	 */
	private CompareData data;

	public GetInputData(LoadCompareData data) {
		this.data = data.loadInputData();
	}

	public void setInputData(LoadCompareData data) {
		this.data = data.loadInputData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see info.binarynetwork.interfaces.InputCompareData#getInputData()
	 */
	public CompareData getInputData() {
		return this.data;
	}

}
