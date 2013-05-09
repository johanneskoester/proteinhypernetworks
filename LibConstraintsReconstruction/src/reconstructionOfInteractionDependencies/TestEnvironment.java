/* Copyright (c) 2012, Bianca Patro
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */


package reconstructionOfInteractionDependencies;

import java.util.Arrays;

import modalLogic.formula.Formula;
import util.TestDataGenerator;


/**
 * Class for testing the different functionalities of the reconstructionOfInteractionDependencies-library.
 * @author Bianca Patro
 *
 */

public class TestEnvironment {

	/**
	 *java -jar TestEnvironment.jar -h | --help | help  Prints the helpmessage. <br>
	 *java -jar TestEnvironment.jar command command-specific-options<br>
	 *commands: (case-insensitive)<br>
	 *<pre>TDG|dateGeneration|TestDataGeneration path numberOfVariables [numberOfFunctions]
	 *recon|reconstruction                  0|0lines|0linesMethod|1|1lines|1linesMethod|2|compare|compareBothMethods file [logLevel = 3]
	 *TTP|prediction|TruthTablePrediction   filePINdb fileBOGrid thresholdProteins thresholdComplexes pathDestination numberOfInteractions minNumberOfObservations learnThreshold</pre>
	 */
	public static void main(String[] args) {
		if(args.length < 1){
			System.out.println("Too few parameters!");
			printUsageMessage();
			System.exit(1);
		}
		// Helpmessage for the program
		if(args[0].equalsIgnoreCase("-h") || args[0].equalsIgnoreCase("--help") ||  args[0].equalsIgnoreCase("help")){
			printUsageMessage();
			System.exit(0);
		// TestDateGeneration	
		}else if (args[0].equalsIgnoreCase("dataGeneration") || args[0].equalsIgnoreCase("tdg") || args[0].equalsIgnoreCase("testDataGeneration")){
			if (!(args.length == 3 || args.length == 4)){
				System.out.println("Too few parameters!");
				printUsageMessage();
				System.exit(1);
			}
			String[] param = Arrays.copyOfRange(args, 1, args.length);
			TestDataGenerator.main(param);						
		// Reconstruction of interaction dependencies	
		}else if(args[0].equalsIgnoreCase("reconstruction") || args[0].equalsIgnoreCase("recon")){
			if (!(args.length == 3 || args.length == 4 || args.length == 5)){
				System.out.println("Too few parameters!");
				printUsageMessage();
				System.exit(1);
			}
			String method = args[1];
			String path = args[2];
			int logLevel = 0;
			if (args.length >=4){
				try {
					logLevel = Integer.parseInt(args[4]);
				}catch (NumberFormatException ne) {
					System.out.println("The loglevel has to be an int.");
					printUsageMessage();
					System.exit(1);
				}
			}else {
				logLevel = 3;
			}
			
			Reconstructor recon = new Reconstructor(logLevel);
			if (method.equalsIgnoreCase("0") || method.equalsIgnoreCase("0lines") || method.equalsIgnoreCase("0linesMethod")){
				Formula<String> formula = recon.zeroLinesMethod(path);
				recon.log.printMessage(0, "Formula in SINF:\n" + formula + "\n");
				formula = recon.reduceSINF2(formula);
				recon.log.printMessage(0, "Formula in SINF after reducingSINF2:\n" + formula + "\n\n");
			}else if  (method.equalsIgnoreCase("1") || method.equalsIgnoreCase("1lines") || method.equalsIgnoreCase("1linesMethod")){
				Formula<String> formula = recon.oneLinesMethod(path);
				recon.log.printMessage(0, "Formula in SINF:\n" + formula + "\n");
				formula = recon.reduceSINF2(formula);
				recon.log.printMessage(0, "Formula in SINF after reducingSINF2:\n" + formula + "\n\n");
			}else if  (method.equalsIgnoreCase("2") || method.equalsIgnoreCase("compare") || method.equalsIgnoreCase("compareBothMethods")){
				recon.compareBothMethods(path);
			}
		// TruthTablePrediction	
		}else if (args[0].equalsIgnoreCase("truthTablePrediction") || args[0].equalsIgnoreCase("ttp")){
			if (args.length != 8){
				System.out.println("Too few parameters!");
				printUsageMessage();
				System.exit(1);
			}
			String pathPINdb = args[1];
			String pathBioGrid = args[2];
			int thresholdProteins = 0;
			int thresholdComplexes = 0;
			String pathDestination = args[5] + (args[5].isEmpty() ? "" : "/");
			int interactions = 0;
			int minNumberOfObservations  = 0;
			boolean learnThreshold = false;
			try{
				thresholdProteins = Integer.parseInt(args[3]);
				thresholdComplexes = Integer.parseInt(args[4]);
				interactions = Integer.parseInt(args[6]); 
				minNumberOfObservations = Integer.parseInt(args[7]);
				learnThreshold = Boolean.getBoolean(args[8]);
				
			}catch(NumberFormatException ne){
				System.out.println("The tresholds, the number of interactions and the minNumberOfObservations have to be ints.");
				printUsageMessage();
				System.exit(1);
			}
			TruthTablePrediction ttp = new TruthTablePrediction(pathPINdb, pathBioGrid, thresholdProteins);
			if (interactions == 2){
				ttp.predictTruthTablesWith2InteractionsFor3Proteins(pathDestination, thresholdComplexes, minNumberOfObservations, learnThreshold);
			}else if (interactions == 3){
				ttp.predictTruthTablesWith3InteractionsFor3Proteins(pathDestination, thresholdComplexes, minNumberOfObservations, learnThreshold);
			}else {
				ttp.predictTruthTablesWith2InteractionsFor3Proteins(pathDestination + "2Interactions", thresholdComplexes, minNumberOfObservations, learnThreshold);
				ttp.predictTruthTablesWith3InteractionsFor3Proteins(pathDestination + "3Interactions", thresholdComplexes, minNumberOfObservations, learnThreshold);
			}
		}else{
			System.out.println("Wrong parameters.");
			printUsageMessage();
			System.exit(1);
		}
	}
	
	private static void printUsageMessage(){
		System.out.println("Use the program like this:");
		System.out.println("java -jar TestEnvironment.jar -h | --help | help \t Prints the helpmessage.");
		System.out.println("java -jar TestEnvironment.jar command command-specific-options");
		System.out.println("commands: (case-insensitive)");
		System.out.println("TDG|dateGeneration|TestDataGeneration\t <path> <numberOfVariables> [<numberOfFunctions>]");
		System.out.println("recon|reconstruction                 \t 0|0lines|0linesMethod|1|1lines|1linesMethod|2|compare|compareBothMethods <file> [<logLevel> = 3]");
		System.out.println("TTP|prediction|TruthTablePrediction  \t <filePINdb> <fileBOGrid> <thresholdProteins> <thresholdComplexes> <pathDestination> <numberOfInteractions> <minNumberOfObservations> <learnThreshold>");
	}
}
