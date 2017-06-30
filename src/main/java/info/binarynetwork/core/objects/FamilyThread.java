package info.binarynetwork.core.objects;

import java.util.concurrent.ConcurrentHashMap;

import info.binarynetwork.core.interfaces.NetworkCore;
import info.binarynetwork.objects.CompareData;
import info.binarynetwork.objects.neuralElement;

public class FamilyThread implements Runnable {
	private final neuralElement[] family;
	private Integer startFrom;
	private final CompareData cData;
	private NetworkCore core;
	private ConcurrentHashMap<Integer, Float> map;

	public FamilyThread(neuralElement[] family, Integer startFrom, CompareData cData, NetworkCore core, ConcurrentHashMap<Integer, Float> map) {
		this.family = family;
		this.startFrom = startFrom;
		this.cData = cData;
		this.core = core;
		this.map = map;
		// System.out.println("Create thread");
	}

	public void run() {
		System.out.println("Start thread " + String.valueOf(startFrom));
		for (int i = 0; i < family.length; i++) {
			float chkRez = core.compareEl(family[i], cData);
			// put data into hash table
			// System.out.println("THREAD RESULT " + (startFrom + i) + " VAL " +
			// String.valueOf(chkRez));
			map.put(startFrom, chkRez);
			startFrom++;
		}
		// System.out.println("Stop thread " + String.valueOf(startFrom));
	}

}
