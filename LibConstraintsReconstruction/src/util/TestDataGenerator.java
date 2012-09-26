/* Copyright (c) 2012, Bianca Patro
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */


package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

/**
 * Class for generating *.csv-files with truth-tables of boolean functions.
 * @author Bianca Patro
 *
 */
public class TestDataGenerator {

	/**
	 * Generates *.csv-files with truth-tables of boolean functions.
	 * @param args <br>
	 * args[0] - path to location to save the test-data <br>
	 * args[1] - number of variables for the boolean function <br>
	 * If only 2 parameters: for less than 5 variables all boolean functions with args[1] variables will be generated. 
	 * In case of 5 or more variables with 2 parameters 100000 functions will be randomly generated<br>
	 * opt: args[2] - number of functions to be randomly generated
	 */
	public static void main(String[] args) {
		int numberOfVariables = 0;
		int numberOfFunctions = 0;
		String[] names = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", 
				"r", "s", "t", "u", "v", "w", "x", "y", "z"};
		Random rand = new Random();

		//------------------------------------------------------------------------------
		// Error handling and initializing parameters
		if(!(args.length == 2 || args.length == 3)){
			System.out.println("Wrong number of parameters!");
			printUsageMessage();
			System.exit(-1);
		}		
		try{// Parsing the number of variables
			numberOfVariables = Integer.parseInt(args[1]);
			
			if (numberOfVariables <= 0){
				throw new NumberFormatException("Negative or zero parameter is not allowed!");
			}
		}catch(NumberFormatException ne){
			System.out.println(ne.getMessage());
			System.out.println("The number of variables has to be an integer greater 0.");
			printUsageMessage();
			System.exit(-1);
		}				
		if(args.length == 3){
			try{ // Parsing the number of functions
				numberOfFunctions = Integer.parseInt(args[2]);
				
				if (numberOfFunctions <= 0){
					throw new NumberFormatException("Negative or null parameter is not allowed!");
				}
			}catch(NumberFormatException ne){
				System.out.println(ne.getMessage());
				System.out.println("The number of functions has to be an integer greater 0.");
				printUsageMessage();
				System.exit(-1);
			}
		}else{ // With two parameters: Setting the number of functions
			if (numberOfVariables <= 4){
				numberOfFunctions = (1 << (1 << numberOfVariables));
			}else{
				System.out.println("For more than four variables all functions are too much. NumberOfFuntctions is set to 100000.");
				numberOfFunctions = 100000;				
			}
		}
		int numberOfLines = (1 << numberOfVariables);
		System.out.print("NumberOfVariables: " + numberOfVariables + ", NumberOfLines: " + numberOfLines 
				+ ", NumberOfFunctions: " + numberOfFunctions + "\n");
		
		//------------------------------------------------------------------------------
		// Generating the functions		
		BigInteger lineCounter =  BigInteger.ZERO;
		BigInteger truthValue = BigInteger.ZERO;
		for(int i = 0; i < numberOfFunctions; i++){
			lineCounter = BigInteger.ZERO;			
			String header = "";
			try{
			// Create file 
			String path = args[0] + (args[0].isEmpty() ? "" : "/");
			FileWriter fstream = new FileWriter(path + "functionWith"+numberOfVariables+"Variables"+i+".csv");
			BufferedWriter writer = new BufferedWriter(fstream);
			
			// Create and write header
			for(int j = 0; j < numberOfVariables; j++){
				if (numberOfVariables < 27){
					header = header + names[j] + " ";
				}else{
					header = header + j + " ";
				}
			}
			header = header + "observed";
			writer.write(header);
			writer.newLine();
			// Creating and Writing the lines of the truth-table
			for(int j = 0; j < numberOfLines; j++){
				String line = "";
				for (int k = 0; k < numberOfVariables; k++){
					if (k < lineCounter.bitLength()){
						line = line + (lineCounter.testBit(k) ? "1 " : "0 ");
					}else{
						line = line + "0 ";
					}
				}
				// If less than all functions: the truth-value is generated randomly
				if(args.length == 3){
					line = line + (Math.abs(rand.nextInt()) % 2);
				}else{ // Otherwise we generate the next function according to the bit representation of truthValue
					if (j < truthValue.bitLength()){
						line = line + (truthValue.testBit(j) ? "1" : "0");
					}else{
						line = line + "0";
					}
				}
				
				writer.write(line);
				if(j < numberOfLines -1){
					writer.newLine();
				}
				lineCounter = lineCounter.add(BigInteger.ONE);					  					 
			}
			truthValue = truthValue.add(BigInteger.ONE);
			
			// Close the output stream
			writer.close();
			}catch (IOException io){
				System.err.println("Error: " + io.getMessage());
			}
		}		
	}
	
	
	/**
	 * Prints a message how to use this tool.
	 */
	private static void printUsageMessage(){
		System.out.println("Use like this: TestDataGenerator <path> <numberOfVariables> [<numberOfFunctions>]");
		System.out.println("With two parameters for less than 5 variables all functions will be generated. Otherwise 100000 functions will be generated.");
	}

}
