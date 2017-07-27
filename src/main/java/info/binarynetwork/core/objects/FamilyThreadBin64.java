package info.binarynetwork.core.objects;

import java.util.concurrent.ConcurrentHashMap;

import info.binarynetwork.core.interfaces.NetworkCore;
import info.binarynetwork.objects.Binary64Data;
import info.binarynetwork.objects.neuralElement64;

public class FamilyThreadBin64 implements Runnable {
    private neuralElement64[] family;
    private Integer startFrom;
    private Binary64Data cData;
    private NetworkCore core;
    private ConcurrentHashMap<Integer, Float> map;

    public FamilyThreadBin64(neuralElement64[] family, Integer startFrom, Binary64Data cData, NetworkCore core,
	    ConcurrentHashMap<Integer, Float> map) {
	this.family = family;
	this.startFrom = startFrom;
	this.cData = cData;
	this.core = core;
	this.map = map;
    }

    public void run() {
	System.out.println("Start thread " + String.valueOf(startFrom));
	for (int i = 0; i < family.length; i++) {
	    float chkRez = core.compareEl(family[i], cData);
	    map.put(startFrom, chkRez);
	    startFrom++;
	}
    }

}
