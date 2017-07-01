package info.binarynetwork.core.objects;

import java.util.concurrent.ConcurrentHashMap;

import info.binarynetwork.core.interfaces.NetworkCore;
import info.binarynetwork.objects.Binary32Data;
import info.binarynetwork.objects.neuralElement32;

public class FamilyThreadBin32 implements Runnable {
    private neuralElement32[] family;
    private Integer startFrom;
    private Binary32Data cData;
    private NetworkCore core;
    private ConcurrentHashMap<Integer, Float> map;

    public FamilyThreadBin32(neuralElement32[] family, Integer startFrom, Binary32Data cData, NetworkCore core,
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
