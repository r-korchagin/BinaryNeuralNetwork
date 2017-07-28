package info.binarynetwork.neural;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import info.binarynetwork.core.interfaces.NetworkFamilyModification;
import info.binarynetwork.core.interfaces.NetworkStep;
import info.binarynetwork.impls.GetBinary32Data;
import info.binarynetwork.interfaces.Config;
import info.binarynetwork.interfaces.NetworkFamily;
import info.binarynetwork.interfaces.OutputRuntimeResult;
import info.binarynetwork.objects.NeuralConfig;
import info.binarynetwork.objects.neuralElement32;

/**
 * Binary Neural Network
 *
 */
public class App {
    public static void main(String[] args) {
	BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	ApplicationContext ctx = new ClassPathXmlApplicationContext("NeuralContext.xml");

	Config config = (Config) ctx.getBean("config");
	NeuralConfig currConfig = config.getConfig();

	NetworkFamily family = (NetworkFamily) ctx.getBean("familyData");
	NetworkFamilyModification FamilyGen = (NetworkFamilyModification) ctx.getBean("NetworkFamilyModification");
	OutputRuntimeResult printout = (OutputRuntimeResult) ctx.getBean("OutputRuntimeResult");

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

	neuralElement32[] familyData;

	if (args.length > 0 && args[0].equalsIgnoreCase("continue")) {
	    // LOAD NEW32BIT FAMILY
	    System.out.println("Load 32bit family from \\neuralfamily\\test");
	    familyData = family.loadFamily32(".\\neuralfamily\\test", currConfig.getFAMILY_SIZE());
	} else {
	    // CREATE NEW32BIT FAMILY
	    System.out.println("Create New Family");
	    familyData = family.createFamily32(currConfig.getFAMILY_SIZE(), currConfig.getFIRST_LEV(),
		    currConfig.getSECOND_LEV(), currConfig.getTRIDE_LEV(), 1);
	    family.saveFamily32(".\\neuralfamily\\test", familyData);
	}

	float currentResult = new Float(1000000);
	GetBinary32Data Cdata = (GetBinary32Data) ctx.getBean("inputData32");
	float stopResult = currConfig.STOP_RESULT * Cdata.getInputData().getSize() / 100;
	System.out.println("Stopresult = " + String.valueOf(stopResult));

	int countIterrations = 0;
	int cycleCount = 0;

	NetworkStep step = (NetworkStep) ctx.getBean("network_step");
	// NetworkStep stepAlt = (NetworkStep)
	// ctx.getBean("network_step_check");

	while (currentResult > stopResult) {
	    long startTime = System.currentTimeMillis();

	    float[] result = step.execStep32(familyData, currConfig.getFAMILY_SIZE());

	    familyData = FamilyGen.FamilyMutation(familyData, result);

	    // SEPARATE BY RESULT
	    Arrays.sort(result);
	    if (currentResult == result[0]) {
		countIterrations++; // increase counter same result
	    } else {
		countIterrations = 0;
	    }

	    printout.Printout(result, startTime, cycleCount, countIterrations);

	    currentResult = result[0];
	    cycleCount++;

	    String input;

	    try {
		if (System.in.available() > 0) {
		    input = reader.readLine();
		    if ("q".equals(input)) {
			System.out.println("Exit! Save family...");
			family.saveFamily32(".\\neuralfamily\\test", familyData);
			currentResult = 0;
		    }
		}
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	    // Stop learning if get maximum same iteration
	    if (countIterrations >= currConfig.MAXIUM_COUNT_ITERATION) {
		currentResult = 0;
		family.saveFamily32(".\\neuralfamily\\test", familyData);
	    }
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

    public static long getChecksumEl(neuralElement32 el) {
	long chk = 0;
	for (int i = 0; i < el.getLink1_2().length; i++) {
	    chk = chk ^ el.getLink1_2()[i];
	}
	for (int i = 0; i < el.getLink2_3().length; i++) {
	    chk = chk ^ el.getLink2_3()[i];
	}
	for (int i = 0; i < el.getLink3_4().length; i++) {
	    chk = chk ^ el.getLink3_4()[i];
	}
	return chk;
    }
}
