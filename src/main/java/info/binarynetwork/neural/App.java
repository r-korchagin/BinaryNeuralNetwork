package info.binarynetwork.neural;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import info.binarynetwork.core.interfaces.NetworkStep;
import info.binarynetwork.interfaces.Config;
import info.binarynetwork.interfaces.InputCompareData;
import info.binarynetwork.interfaces.NetworkFamily;
import info.binarynetwork.objects.CompareData;
import info.binarynetwork.objects.NeuralConfig;
import info.binarynetwork.objects.neuralElement;

/**
 * Binary Neural Network
 *
 */
public class App {
	public static void main(String[] args) {
		System.out.println("Hello World!");
		ApplicationContext ctx = new ClassPathXmlApplicationContext("NeuralContext.xml");

		Config config = (Config) ctx.getBean("config");
		NeuralConfig currConfig = config.getConfig();

		InputCompareData cData = (InputCompareData) ctx.getBean("inputData");
		CompareData Data = cData.getInputData();

		NetworkFamily family = (NetworkFamily) ctx.getBean("familyData");
		// neuralElement[] familyData = family.loadFamily(".\\neuralfamily\\",
		// currConfig.getFAMILY_SIZE());
		neuralElement[] familyData = family.createFamily(currConfig.getFAMILY_SIZE(), currConfig.getFIRST_LEV(), currConfig.getSECOND_LEV(), currConfig.getTRIDE_LEV(), 1);
		family.saveFamily(".\\neuralfamily\\test", familyData);

		NetworkStep step = (NetworkStep) ctx.getBean("network_step");
		NetworkStep stepAlt = (NetworkStep) ctx.getBean("network_step_check");

		long startTime = System.nanoTime();
		float[] result = step.execStep(familyData, currConfig.getFAMILY_SIZE(), Data);
		long endTime = System.nanoTime();
		long duration = (endTime - startTime);
		System.out.println("time execution ns  " + duration);
		for (float r : result) {
			System.out.println("Result1 " + r);
		}

		float[] result2 = stepAlt.execStep(familyData, currConfig.getFAMILY_SIZE(), Data);
		for (float r : result2) {
			System.out.println("Result2 " + r);
		}

		((ConfigurableApplicationContext) ctx).close();
	}
}
