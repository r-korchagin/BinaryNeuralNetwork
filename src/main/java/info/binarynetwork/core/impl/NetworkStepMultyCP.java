package info.binarynetwork.core.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import info.binarynetwork.core.interfaces.NetworkCore;
import info.binarynetwork.core.interfaces.NetworkStepExecutor;
import info.binarynetwork.core.objects.FamilyThread;
import info.binarynetwork.core.objects.FamilyThreadBin32;
import info.binarynetwork.core.objects.FamilyThreadBin64;
import info.binarynetwork.objects.Binary32Data;
import info.binarynetwork.objects.Binary64Data;
import info.binarynetwork.objects.CompareData;
import info.binarynetwork.objects.neuralElement;
import info.binarynetwork.objects.neuralElement32;

public class NetworkStepMultyCP implements NetworkStepExecutor {

    private NetworkCore core;
    private int THREAD_POOL_SIZE;

    public int getTHREAD_POOL_SIZE() {
	return THREAD_POOL_SIZE;
    }

    public void setTHREAD_POOL_SIZE(int tHREAD_POOL_SIZE) {
	THREAD_POOL_SIZE = tHREAD_POOL_SIZE;
    }

    public NetworkCore getCore() {
	return core;
    }

    public void setCore(NetworkCore core) {
	this.core = core;
    }

    public float[] runStep(neuralElement[] famiy, int familySize, CompareData data) {

	float[] stepResult = new float[familySize];
	ConcurrentHashMap<Integer, Float> rezConcurrentHashMap = new ConcurrentHashMap<Integer, Float>();
	ExecutorService ExServer = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

	int z = 0;
	for (int k = 1; k <= THREAD_POOL_SIZE; k++) {
	    int stopline = (int) (familySize / THREAD_POOL_SIZE + 2) * k;
	    if (stopline > familySize) {
		stopline = familySize;
	    }
	    neuralElement[] thrFamily = new neuralElement[stopline - z];
	    int i = 0;
	    int threadStartIndex = z;
	    while (z < stopline) {
		thrFamily[i] = famiy[z];
		i++;
		z++;
	    }
	    // create thread VAR thrFamily, threadStartIndex
	    Runnable thread = new FamilyThread(thrFamily, threadStartIndex, data, core, rezConcurrentHashMap);
	    ExServer.execute(thread);
	}
	// wait thread complete
	ExServer.shutdown();
	try {
	    ExServer.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
	} catch (InterruptedException e) {

	    e.printStackTrace();
	}

	for (Map.Entry<Integer, Float> e : rezConcurrentHashMap.entrySet()) {
	    stepResult[e.getKey()] = e.getValue();
	}

	return stepResult;
    }

    public float[] runStep(neuralElement32[] famiy, int familySize, Binary32Data data) {
	float[] stepResult = new float[familySize];
	ConcurrentHashMap<Integer, Float> rezConcurrentHashMap = new ConcurrentHashMap<Integer, Float>();
	ExecutorService ExServer = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

	int z = 0;
	for (int k = 1; k <= THREAD_POOL_SIZE; k++) {
	    int stopline = (int) (familySize / THREAD_POOL_SIZE + 2) * k;
	    if (stopline > familySize) {
		stopline = familySize;
	    }
	    neuralElement32[] thrFamily = new neuralElement32[stopline - z];
	    int i = 0;
	    int threadStartIndex = z;
	    while (z < stopline) {
		thrFamily[i] = famiy[z];
		i++;
		z++;
	    }
	    // create thread VAR thrFamily, threadStartIndex
	    Runnable thread = new FamilyThreadBin32(thrFamily, threadStartIndex, data, core, rezConcurrentHashMap);
	    ExServer.execute(thread);
	}
	// wait thread complete
	ExServer.shutdown();
	try {
	    ExServer.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
	} catch (InterruptedException e) {

	    e.printStackTrace();
	}

	for (Map.Entry<Integer, Float> e : rezConcurrentHashMap.entrySet()) {
	    stepResult[e.getKey()] = e.getValue();
	}

	return stepResult;
    }

    public float[] runStep(neuralElement[] famiy, int familySize, Binary64Data data) {
	float[] stepResult = new float[familySize];
	ConcurrentHashMap<Integer, Float> rezConcurrentHashMap = new ConcurrentHashMap<Integer, Float>();
	ExecutorService ExServer = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

	int z = 0;
	for (int k = 1; k <= THREAD_POOL_SIZE; k++) {
	    int stopline = (int) (familySize / THREAD_POOL_SIZE + 2) * k;
	    if (stopline > familySize) {
		stopline = familySize;
	    }
	    neuralElement[] thrFamily = new neuralElement[stopline - z];
	    int i = 0;
	    int threadStartIndex = z;
	    while (z < stopline) {
		thrFamily[i] = famiy[z];
		i++;
		z++;
	    }
	    // create thread VAR thrFamily, threadStartIndex
	    Runnable thread = new FamilyThreadBin64(thrFamily, threadStartIndex, data, core, rezConcurrentHashMap);
	    ExServer.execute(thread);
	}
	// wait thread complete
	ExServer.shutdown();
	try {
	    ExServer.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
	} catch (InterruptedException e) {

	    e.printStackTrace();
	}

	for (Map.Entry<Integer, Float> e : rezConcurrentHashMap.entrySet()) {
	    stepResult[e.getKey()] = e.getValue();
	}

	return stepResult;
    }

}
