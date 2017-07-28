package info.binarynetwork.impls;

import org.springframework.stereotype.Component;

import info.binarynetwork.interfaces.OutputRuntimeResult;

@Component("OutputRuntimeResult")
public class OutputRuntime implements OutputRuntimeResult {

    public void Printout(float[] result, long startTime, int cycleCount, int countIteration) {
	int countShowResult = 7;
	if (result.length < 7)
	    countShowResult = result.length;

	long duration = (System.currentTimeMillis() - startTime);
	System.out.println("");
	System.out.println(cycleCount + " ---- | ----- " + countIteration + " time:" + duration + "ms");

	// Printout result
	for (int i = 0; i < countShowResult; i++) {
	    String termOut = i + ": Max result = ";
	    termOut += result[i];
	    System.out.println(termOut);
	}

    }

}
