package info.binarynetwork.neural;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import info.binarynetwork.core.interfaces.NetworkStep;
import info.binarynetwork.impls.GetBinary32Data;
import info.binarynetwork.interfaces.Config;
import info.binarynetwork.interfaces.NetworkFamily;
import info.binarynetwork.objects.NeuralConfig;
import info.binarynetwork.objects.neuralElement32;

/**
 * Binary Neural Network
 *
 */
public class App {
    public static void main(String[] args) {
	ApplicationContext ctx = new ClassPathXmlApplicationContext("NeuralContext.xml");

	Config config = (Config) ctx.getBean("config");
	NeuralConfig currConfig = config.getConfig();

	NetworkFamily family = (NetworkFamily) ctx.getBean("familyData");
	// LOAD FROM FILE
	// neuralElement[] familyData = family.loadFamily(".\\neuralfamily\\",
	// currConfig.getFAMILY_SIZE());

	// CREATE NEW64BIT FAMILY
	/*
	 * neuralElement[] familyData =
	 * family.createFamily(currConfig.getFAMILY_SIZE(),
	 * currConfig.getFIRST_LEV(), currConfig.getSECOND_LEV(),
	 * currConfig.getTRIDE_LEV(), 1);
	 * family.saveFamily(".\\neuralfamily\\test", familyData);
	 */

	// CREATE NEW32BIT FAMILY
	neuralElement32[] familyData = family.createFamily32(currConfig.getFAMILY_SIZE(), currConfig.getFIRST_LEV(),
		currConfig.getSECOND_LEV(), currConfig.getTRIDE_LEV(), 1);
	family.saveFamily32(".\\neuralfamily\\test", familyData);

	float currentResult = new Float(1000000);
	GetBinary32Data Cdata = (GetBinary32Data) ctx.getBean("inputData32");
	float stopResult = currConfig.STOP_RESULT * Cdata.getInputData().getSize() / 100;
	System.out.println("Stopresult = " + String.valueOf(stopResult));
	int stopElitLev = (int) Math.round(currConfig.getFAMILY_SIZE() * 0.2); // elite
	int stopCrossLev = (int) Math.round(currConfig.getFAMILY_SIZE() * 0.8); // cross
	int countIterrations = 0;
	int cycleCount = 0;

	NetworkStep step = (NetworkStep) ctx.getBean("network_step");
	// NetworkStep stepAlt = (NetworkStep)
	// ctx.getBean("network_step_check");

	float[] chkRez = new float[currConfig.getFAMILY_SIZE()];
	float[] sortRez = new float[currConfig.getFAMILY_SIZE()];

	while (currentResult > stopResult) {
	    long startTime = System.currentTimeMillis();
	    float[] result = step.execStep32(familyData, currConfig.getFAMILY_SIZE());
	    long endTime = System.currentTimeMillis();
	    long duration = (endTime - startTime);
	    // System.out.println("time execution ms " + duration);
	    /*
	     * for (float r : result) { System.out.println("Result1 " + r); }
	     */

	    System.arraycopy(result, 0, chkRez, 0, result.length);
	    System.arraycopy(result, 0, sortRez, 0, result.length);

	    Arrays.sort(sortRez); /// less its the best
	    // Arrays.hashCode()

	    // SEPARATE BY RESULT
	    if (currentResult == sortRez[0]) {
		countIterrations++;
	    } else {
		countIterrations = 0;
	    }
	    currentResult = sortRez[0];
	    System.out.println(cycleCount + "---- | -----" + countIterrations + " time:" + duration + "ms");
	    cycleCount++;
	}

	/*
	 * startTime = System.currentTimeMillis(); float[] result2 =
	 * stepAlt.execStep32(familyData, currConfig.getFAMILY_SIZE()); endTime
	 * = System.currentTimeMillis(); duration = (endTime - startTime);
	 * System.out.println("time execution ms  " + duration); for (float r :
	 * result2) { System.out.println("Result2 " + r); }
	 */

	((ConfigurableApplicationContext) ctx).close();

    }
}
