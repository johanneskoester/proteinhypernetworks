/* Copyright (c) 2012, Bianca Patro
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */


package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Class with static methods for constructing the truth-tables for known interaction dependencies.
 * @author Bianca Patro
 *
 */
public class TablesForKnownDependencies {
	
	/**
	 * This Method constructs the truth-tables for files containing known allosteric effects.
	 * @param pathSource - path to file with known allosteric effect with the following columns: host - interactor - activator - inhibitor
	 * @param pathDestination - path to a folder for saving the constructed tables
	 */
	public static void constructTruthTablesForKnownAllostericEffects(String pathSource, String pathDestination){
		//TODO Anpassen auf gleichzeitig Activatoren und Inhibitoren
		try {			
			BufferedReader reader = new BufferedReader(new FileReader(pathSource));
			String line;
			
			// first line contains a header
			line = reader.readLine();
			int i = 0; // counting the printed tables			
			// line by line processing of the file
			while((line = reader.readLine()) != null){
				if (line.contains("phosphorylation")){
					continue;
				}
				StringTokenizer tokenizer = new StringTokenizer(line, "\t", true);
				boolean inhibitor = false;
				// 1st column contains the host
				String host = tokenizer.nextToken().replace("\"","");
				tokenizer.nextToken();
				int numberOfVariables = 0;
				// 2nd column contains the interactors
				String interactorNames = tokenizer.nextToken().replace("\"","");
				StringTokenizer tokenizerInteractors = new StringTokenizer(interactorNames, ",");
				ArrayList<String> interactors = new ArrayList<String>();
				while (tokenizerInteractors.hasMoreElements()){
					interactors.add(tokenizerInteractors.nextToken().replace(" ",""));
					numberOfVariables++;
				}
				tokenizer.nextToken();
				// 3rd column contains the activators
				String activatorNames = tokenizer.nextToken().replace("\"","");
				// or the 4th column contains the inhibitors
				if (activatorNames.equals("\t")){
					inhibitor = true;
					activatorNames = tokenizer.nextToken().replace("\"","");
				}
				StringTokenizer tokenizerActivators = new StringTokenizer(activatorNames, ",");
				ArrayList<String> activators = new ArrayList<String>();
				while (tokenizerActivators.hasMoreElements()){
					activators.add(tokenizerActivators.nextToken().replace(" ",""));
					numberOfVariables++;
				}
				
				try { // Writing the table into a file
					FileWriter fstream = new FileWriter(pathDestination+"tableAllostericEffect"+i+".csv");;
					BufferedWriter writer = new BufferedWriter(fstream);
					// Writing the header
					String header = "";
					for (int j = 0; j < interactors.size(); j++){
						header += host+"pp"+interactors.get(j) + " ";
					}
					for (int j = 0; j < activators.size(); j++){
						header += host+"pp"+activators.get(j) + " ";
					}
					writer.write(header + "observed");
					writer.newLine();
					
					int numberOfLines = 1 << numberOfVariables;
					// Writing the table
					BigInteger lineCounter =  BigInteger.ZERO;
					String actualLine = "";
					// Building the lines
					for(int j = 0; j < numberOfLines; j++){
						actualLine = "";
						for (int k = 0; k < numberOfVariables; k++){
							if (k < lineCounter.bitLength()){
								actualLine = actualLine + (lineCounter.testBit(k) ? "1 " : "0 ");
							}else{
								actualLine = actualLine + "0 ";
							}
						}
						// Determining the truth-values
						boolean value = true;
						BigInteger temp = lineCounter;
						for (int k = 0; k < interactors.size(); k++){
							temp = temp.clearBit(k);
							if(!lineCounter.testBit(k)){
								value &= true;
							}else if (inhibitor){							
								if(temp.compareTo(BigInteger.ZERO) == 0){
									value &= true; 
								}else{
									value &= false;
								}
							}else{
								if(temp.compareTo(BigInteger.ZERO) == 0){
									value &= false;
								}else{ 
									value &= true;
								}
							}
						}
						actualLine = actualLine + (value ? "1 " : "0 ");
						
						writer.write(actualLine);
						if(j < numberOfLines -1){
							writer.newLine();
						}						
						lineCounter = lineCounter.add(BigInteger.ONE);		
					}
					writer.close();
		
				} catch (IOException e) {
					System.out.println("Error while writing the file.");
					e.printStackTrace();
					System.exit(-1);
				}
				i++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found! Please check spelling and path of the file.");
			e.printStackTrace();
			System.exit(-1); 
		} catch (IOException e) {
			System.out.println("Error while reading the file.");
			e.printStackTrace();
			System.exit(-1);
		}		
	}

	/**
	 * This Method constructs the truth-tables for files containing known competitions.
	 * @param pathSource - path to file with known competitions with the following columns: host - cometitors
	 * @param pathDestination - path to a folder for saving the constructed tables
	 */
	public static void constructTruthTablesForKnownCompetitionsBetween2Interactions(String pathSource, String pathDestination){
		try {			
			BufferedReader reader = new BufferedReader(new FileReader(pathSource));
			String line;
			
			// first line contains a header
			line = reader.readLine();
			int i = 0; // counting the printed tables			
			// line by line processing of the file
			while((line = reader.readLine()) != null){
				if (line.contains("phosphorylation")){
					continue;
				}
				StringTokenizer tokenizer = new StringTokenizer(line, "\t");
				// 1st column contains the host
				String host = tokenizer.nextToken().replace("\"","");
				int numberOfVariables = 0;
				// 2nd column contains the competitors
				String competitorNames = tokenizer.nextToken().replace("\"","");
				StringTokenizer tokenizerCompetitors = new StringTokenizer(competitorNames, ",");
				ArrayList<String> competitors = new ArrayList<String>();
				while (tokenizerCompetitors.hasMoreElements()){
					competitors.add(tokenizerCompetitors.nextToken().replace(" ",""));
					numberOfVariables++;
				}
				int numberOfLines = 1 << numberOfVariables;
					
				try { // Writing the table into a file
					FileWriter fstream = new FileWriter(pathDestination+"tableCompetitions"+i+".csv");;
					BufferedWriter writer = new BufferedWriter(fstream);
					// Writing the header
					String header = "";
					for (int j = 0; j < competitors.size(); j++){
						header += host+"pp"+competitors.get(j) + " ";
					}
					writer.write(header + "observed");
					writer.newLine();
					// Writing the table
					BigInteger lineCounter =  BigInteger.ZERO;
					String actualLine = "";
					for(int j = 0; j < numberOfLines; j++){
						actualLine = "";
						for (int k = 0; k < numberOfVariables; k++){
							if (k < lineCounter.bitLength()){
								actualLine = actualLine + (lineCounter.testBit(k) ? "1 " : "0 ");
							}else{
								actualLine = actualLine + "0 ";
							}
						}
						if(!lineCounter.testBit(0)){
							actualLine = actualLine + "1";
						}else{							
							if(lineCounter.clearBit(0).compareTo(BigInteger.ZERO) == 0){
								actualLine = actualLine + "1";
							}else{
								actualLine = actualLine + "0";
							}
						}
						
						writer.write(actualLine);
						if(j < numberOfLines -1){
							writer.newLine();
						}						
						lineCounter = lineCounter.add(BigInteger.ONE);		
					}
				
					writer.close();
		
				} catch (IOException e) {
					System.out.println("Error while writing the file.");
					e.printStackTrace();
					System.exit(-1);
				}
				i++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found! Please check spelling and path of the file.");
			e.printStackTrace();
			System.exit(-1); 
		} catch (IOException e) {
			System.out.println("Error while reading the file.");
			e.printStackTrace();
			System.exit(-1);
		}		
	}
	
}
