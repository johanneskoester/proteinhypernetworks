/* Copyright (c) 2012, Bianca Patro
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package reconstructionOfInteractionDependencies;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import util.Counter;

/**
 * Class for constructing truth-tables with the information about
 * proteincomplexes and the interactions between the proteins.
 * 
 * @author Bianca Patro
 * 
 */
public class TruthTablePrediction {
	private ArrayList<Proteincomplex> complexes;
	private HashMap<String, Protein> proteins;

	int tableCounter = 0;

	/** Truth-values are the absolute numbers of observations */
	final static int absoluteModus = 1;
	/**
	 * Truth-values are the quotient of the number of observations of one line
	 * and the complete number of observations
	 */
	final static int relativeModus = 2;

	/**
	 * Constructor which reads the proteincomplexes, reduces the set of proteins
	 * according to the threshold and reads the interactions of the remaining
	 * proteins
	 * 
	 * @param pathComplexes
	 *            - path to a PINdb-file containing the proteincomplexes
	 * @param pathInteractions
	 *            - path to a BioGRID-file containing the interactions
	 * @param thresholdProteins
	 *            - threshold for the number of complexes a protein has to be in
	 */
	public TruthTablePrediction(String pathComplexes, String pathInteractions,
			int thresholdProteins) {
		readProteinComplexes(pathComplexes);
		reduceProteinSet(thresholdProteins);
		readInteractions(pathInteractions);
	}

	/**
	 * Returns an ArrayList containing all proteincomplexes
	 * 
	 * @return an ArrayList containing all proteincomplexes
	 */
	public ArrayList<Proteincomplex> getComplexes() {
		return complexes;
	}

	/**
	 * Returns a HashMap containing all proteins with their names as keys.
	 * 
	 * @return a HashMap containing all proteins with their names as keys
	 */
	public HashMap<String, Protein> getProteinsAsHashMap() {
		return proteins;
	}

	/**
	 * Returns a Collection containing all proteins.
	 * 
	 * @return a Collection containing all proteins
	 */
	public Collection<Protein> getProteinsAsCollection() {
		return proteins.values();
	}

	/**
	 * Reads a PINdb-file containing proteincomplexes. Stores each complex in a
	 * list, and stores also each occurring protein in a HashMap with its name
	 * as key.
	 * 
	 * @param path
	 *            - path to a PINdb-file containing the proteincomplexes
	 */
	private void readProteinComplexes(String path) {
		complexes = new ArrayList<Proteincomplex>();
		proteins = new HashMap<String, Protein>();
		String id = null;
		String proteinName = "";
		Proteincomplex tempComplex = null;
		Protein tempProtein = null;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;

			// first line contains a header
			line = reader.readLine();

			// line by line processing of the file
			while ((line = reader.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(line, "\t");
				// 1st column contains the complex-id
				String j = tokenizer.nextToken();
				if (!j.equals(id)) {
					if (tempComplex != null) {
						complexes.add(tempComplex);
					}
					id = j;
					tempComplex = new Proteincomplex(id);
				}
				// 2nd token is the proteinname
				proteinName = tokenizer.nextToken();
				proteinName = proteinName.toUpperCase();
				if (proteins.get(proteinName) != null) {
					tempProtein = proteins.get(proteinName);
					tempProtein.increaseNumberOfComplexes();
				} else {
					tempProtein = new Protein(proteinName);
					tempProtein.increaseNumberOfComplexes();
					proteins.put(proteinName, tempProtein);
				}
				tempComplex.addProtein(tempProtein);

			}
			// Printing of all complexes
			/*
			 * for (int i = 0; i < complexes.size(); i++){
			 * System.out.println(complexes.get(i).toString()); }
			 */

			reader.close();
		} catch (FileNotFoundException e) {
			System.out
					.println("File not found! Please check spelling and path of the file.");
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Error while reading the file.");
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Reduces the protein-set to a subset which contains only proteins that are
	 * in at least threshold complexes.
	 * 
	 * @param threshold
	 *            - number of complexes a protein has to be in
	 */
	public void reduceProteinSet(int threshold) {
		Collection<Protein> p = proteins.values();
		Iterator<Protein> iterator = p.iterator();
		while (iterator.hasNext()) {
			Protein temp = (Protein) iterator.next();
			if (temp.getNumberOfComplexes() < threshold) {
				iterator.remove();
			}
		}
	}

	/**
	 * Reads a BIOGRID-file and stores for each protein we have read before each
	 * interaction in an adjacency-list. Only physical interactions will be
	 * stored.
	 * 
	 * @param path
	 *            - path to a BIOGRID-file containing the protein-network in
	 *            form of pairwise interactions
	 */
	private void readInteractions(String path) {
		String proteinNameA = "";
		String proteinNameB = "";
		Protein tempProteinA = null;
		Protein tempProteinB = null;

		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;

			// first line contains a header
			line = reader.readLine();

			// line by line processing of the file
			while ((line = reader.readLine()) != null) {
				int i = 0;
				StringTokenizer tokenizer = new StringTokenizer(line, "\t");
				// Columns 1 and 2 contain the names
				proteinNameA = tokenizer.nextToken().toUpperCase(); // Column 1
				if (proteins.get(proteinNameA) != null) {
					tempProteinA = proteins.get(proteinNameA);
					proteinNameB = tokenizer.nextToken().toUpperCase(); // column
																		// 2
					tempProteinB = proteins.get(proteinNameB);
					if (tempProteinB != null
							&& !tempProteinA.equals(tempProteinB)) { // ignore
																		// self-interactions
						tempProteinA.addInteraction(tempProteinB);
						tempProteinB.addInteraction(tempProteinA);
					}
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out
					.println("File not found! Please check spelling and path of the file.");
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("Error while reading the file.");
			e.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Predicts all possible truth-tables for three proteins with two
	 * interactions and saves them in the folder specified through path.
	 * 
	 * @param path
	 *            - path to folder in which the truth-tables are saved
	 * @param threshold
	 *            - threshold in how much complexes at least two of the three
	 *            proteins for each table have to be
	 * @param modus
	 *            - modus for calculating the truth-values
	 */
	public void predictTruthTablesWith2InteractionsFor3Proteins(
			String pathDestination, int threshold, int modus) {
		Collection<Protein> p = proteins.values();
		Iterator<Protein> iterator = p.iterator();
		while (iterator.hasNext()) {
			Protein p1 = (Protein) iterator.next();
			Protein p2 = null;
			Protein p3 = null;
			// Searching for three interacting proteins with three interactions
			ArrayList<Protein> adjacencyListP1 = p1.getInteractions();
			for (int i = 0; i < p1.getNumberOfInteractions(); i++) {
				p2 = adjacencyListP1.get(i);
				p3 = null;
				for (int j = i + 1; j < p1.getNumberOfInteractions(); j++) {
					p3 = adjacencyListP1.get(j);
					// We take only triple where p2 and p3 are not interacting
					// to get only tables with two interactions
					if (!p2.interactsWithProtein(p3)) {
						// System.out.println(p1.getName() + ", " + p2.getName()
						// + ", " + p3.getName());
						// Filling the truth-table for the three proteins
						if (fillTruthTableFor3Proteins(p1, p2, p3, new File(
								pathDestination, "table" + tableCounter
										+ ".csv").getPath(), threshold, modus)) {
							tableCounter++;
						}
					}
				}
			}
		}
	}

	/**
	 * Predicts all possible truth-tables for three proteins with three
	 * interactions and saves them in the folder specified through path.
	 * 
	 * @param path
	 *            - path to folder in which the truth-tables are saved
	 * @param threshold
	 *            - threshold in how much complexes at least two of the three
	 *            proteins for each table have to be
	 * @param modus
	 *            - modus for calculating the truth-values
	 */
	public void predictTruthTablesWith3InteractionsFor3Proteins(
			String pathDestination, int threshold, int modus) {
		Collection<Protein> p = proteins.values();
		Iterator<Protein> iterator = p.iterator();
		while (iterator.hasNext()) {
			Protein p1 = (Protein) iterator.next();
			Protein p2 = null;
			Protein p3 = null;
			// Searching for three interacting proteins with two interactions
			ArrayList<Protein> adjacencyListP1 = p1.getInteractions();
			for (int i = 0; i < p1.getNumberOfInteractions(); i++) {
				// we take only proteins for p2 with a lexicographical greater
				// name than p1 to avoid duplicate triples
				if (p1.getName().compareTo(adjacencyListP1.get(i).getName()) <= 0) {
					p2 = adjacencyListP1.get(i);
					p3 = null;
					for (int j = i + 1; j < p1.getNumberOfInteractions(); j++) {
						// we take only proteins for p3 with a lexicographical
						// greater name than p1 to avoid duplicate triples
						if (p1.getName().compareTo(
								adjacencyListP1.get(j).getName()) <= 0) {
							p3 = adjacencyListP1.get(j);
							// System.out.println(p1.getName() + ", " +
							// p2.getName() + ", " + p3.getName());
							// We take only triple where p2 interacts with p3 to
							// get only tables with three interactions
							if (p2.interactsWithProtein(p3)) {
								// Filling the truth-table for the three
								// proteins
								if (fillTruthTableFor3Proteins(p1, p2, p3,
										new File(pathDestination, "table"
												+ tableCounter + ".csv")
												.getPath(), threshold, modus)) {
									tableCounter++;
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Fills a truth-table for the three given proteins according to the
	 * existing interactions and the complexes the proteins are in.
	 * 
	 * @param p1
	 *            - a protein
	 * @param p2
	 *            - protein
	 * @param p3
	 *            - a protein
	 * @param path
	 *            - path including a filename to save the filled truth-table
	 * @param minNumberOfComplexes
	 *            - minimal number of complexes which contain at least two of
	 *            the three proteins
	 * @param modus
	 *            - modus for calculating the truth-values
	 * @return true iff the truth-table was filled successfully and false
	 *         otherwise
	 */
	public boolean fillTruthTableFor3Proteins(Protein p1, Protein p2,
			Protein p3, String path, int minNumberOfComplexes, int modus) {
		ArrayList<Proteincomplex> complexList = getComplexesWithMin2Of3SpecifiedProteins(
				p1, p2, p3);
		String line = "";
		int numberOfObservations = complexList.size();
		if (numberOfObservations < minNumberOfComplexes) {
			System.out.println("Less than " + minNumberOfComplexes
					+ " complexes. No reasonable statement possible for "
					+ p1.getName() + ", " + p2.getName() + " and "
					+ p3.getName() + ".");
			return false;
		}
		// the booleans indicate which interactions exist
		boolean p1p2 = p1.interactsWithProtein(p2);
		boolean p2p3 = p2.interactsWithProtein(p3);
		boolean p3p1 = p3.interactsWithProtein(p1);
		int numberOfVariables = 0;
		// Building the header of the truth-table and counting the number of
		// variables
		String header = "";
		if (p1p2) {
			header = header + p1.getName() + "pp" + p2.getName() + " ";
			numberOfVariables++;
		}
		if (p2p3) {
			header = header + p2.getName() + "pp" + p3.getName() + " ";
			numberOfVariables++;
		}
		if (p3p1) {
			header = header + p3.getName() + "pp" + p1.getName() + " ";
			numberOfVariables++;
		}

		// Map<String, Integer> table = new HashMap<String, Integer>();
		// for (int i = 0; i < complexList.size(); i++) {
		// line = ""; // Each complex provides a line in the table depending on
		// // which proteins it contains
		// Proteincomplex actualComplex = complexList.get(i);
		// if (p1p2) {
		// if (actualComplex.contains2Proteins(p1, p2)) {
		// line = line + "1 ";
		// } else {
		// line = line + "0 ";
		// }
		// }
		// if (p2p3) {
		// if (actualComplex.contains2Proteins(p2, p3)) {
		// line = line + "1 ";
		// } else {
		// line = line + "0 ";
		// }
		// }
		// if (p3p1) {
		// if (actualComplex.contains2Proteins(p3, p1)) {
		// line = line + "1 ";
		// } else {
		// line = line + "0 ";
		// }
		// }// here we count the occurrences of each line
		// if (line.equals("0 ") || line.equals("0 0 ")
		// || line.equals("0 0 0 ")) {
		// numberOfObservations--;
		// } else if (table.get(line) == null) {
		// table.put(line, 1);
		// } else {
		// table.put(line, (table.get(line) + 1));
		// }
		// }

		// Alternative implementation that does not produce artifacts for
		// 3-interaction cases
		Counter<String> counts = new Counter<String>();
		if (numberOfVariables < 3) {
			for (Proteincomplex complex : complexList) {
				if (complex.contains3Proteins(p1, p2, p3)) {
					counts.plus1("1 1");
				} else if (p1p2 && complex.contains2Proteins(p1, p2)) {
					counts.plus1("1 0");
				} else if (p2p3 && complex.contains2Proteins(p2, p3)) {
					counts.plus1("0 1");
				} else if (p3p1 && complex.contains2Proteins(p3, p1)) {
					counts.plus1("0 1");
				}

			}
		} else {
			for (Proteincomplex complex : complexList) {
				if (complex.contains3Proteins(p1, p2, p3)) {
					counts.plus1("1 1 0");
					counts.plus1("1 0 1");
					counts.plus1("0 1 1");
					counts.plus1("1 1 1");
				} else if (complex.contains2Proteins(p1, p2)) {
					counts.plus1("1 0 0");
				} else if (complex.contains2Proteins(p2, p3)) {
					counts.plus1("0 1 0");
				} else if (complex.contains2Proteins(p3, p1)) {
					counts.plus1("0 0 1");
				}

			}
		}
		Map<String, Integer> table = counts.getMap();

		if (table.size() < 2) {
			if(numberOfVariables > 2 || table.containsKey("1 0") || table.containsKey("0 1") ) {
				System.out.println("Observed only one interaction.");
				return false;
			}
		}else if(table.size() == (1 << numberOfVariables) - 1) {
			// complete table 
			// TODO skip this once we have a sophisticated threshold method
		}

		// end

		header = header + "observed-" + numberOfObservations;
		try { // Writing the table into a file
			FileWriter fstream = new FileWriter(path);
			BufferedWriter writer = new BufferedWriter(fstream);
			writer.write(header);
			writer.newLine();
			Set<String> lines = table.keySet();
			Iterator<String> iterator = lines.iterator();
			while (iterator.hasNext()) {
				String temp = (String) iterator.next();
				int absoluteValue = table.get(temp);
				if (modus == relativeModus) {
					float relativeValue = (float) absoluteValue
							/ (float) numberOfObservations;
					// Writing the truth-value as quotient of the occurences and
					// the number of complexes
					writer.write(temp + " " + relativeValue);
				} else if (modus == absoluteModus) {
					writer.write(temp + " " + absoluteValue);
				}
				writer.newLine();
			}
			// Filling in the missing lines with truth-value 0
			BigInteger lineCounter = BigInteger.ZERO;
			int numberOfLines = 1 << numberOfVariables;
			for (int j = 0; j < numberOfLines; j++) {
				line = "";
				for (int k = 0; k < numberOfVariables; k++) {
					if (k < lineCounter.bitLength()) {
						if (lineCounter.testBit(k)) {
							line = line + "1 ";
						} else {
							line = line + "0 ";
						}
					} else {
						line = line + "0 ";
					}
				}
				line = line.trim();
				if (table.get(line) == null) {
					if (j == 0) {
						line = line + " 1";
					} else {
						line = line + " 0";
					}
					writer.write(line);
					if (j < numberOfLines - 1) {
						writer.newLine();
					}
				}
				lineCounter = lineCounter.add(BigInteger.ONE);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Returns from the complete list of complexes all complexes which contain
	 * at least two of the three given proteins.
	 * 
	 * @param p1
	 *            - a protein
	 * @param p2
	 *            - a protein
	 * @param p3
	 *            - a protein
	 * @return a list with all complexes which contain at least two of the three
	 *         given proteins
	 */
	public ArrayList<Proteincomplex> getComplexesWithMin2Of3SpecifiedProteins(
			Protein p1, Protein p2, Protein p3) {
		ArrayList<Proteincomplex> reducedComplexList = new ArrayList<Proteincomplex>();
		for (int i = 0; i < complexes.size(); i++) {
			Proteincomplex tempComplex = complexes.get(i);
			if (tempComplex.containsMin2Of3Proteins(p1, p2, p3)) {
				reducedComplexList.add(tempComplex);
			}
		}
		// System.out.println("Number of Complexes: " +
		// reducedComplexList.size());
		return reducedComplexList;
	}

	/**
	 * Returns from the complete list of complexes all complexes which contain
	 * at least one of the three given proteins.
	 * 
	 * @param p1
	 *            - a protein
	 * @param p2
	 *            - a protein
	 * @param p3
	 *            - a protein
	 * @return a list with all complexes which contain at least one of the three
	 *         given proteins
	 */
	public ArrayList<Proteincomplex> getComplexesWithMin1Of3SpecifiedProteins(
			Protein p1, Protein p2, Protein p3) {
		ArrayList<Proteincomplex> reducedComplexList = new ArrayList<Proteincomplex>();
		for (int i = 0; i < complexes.size(); i++) {
			Proteincomplex tempComplex = complexes.get(i);
			if (tempComplex.containsProtein(p1)
					|| tempComplex.containsProtein(p2)
					|| tempComplex.containsProtein(p3)) {
				reducedComplexList.add(tempComplex);
			}
		}
		// System.out.println("Number of Complexes: " +
		// reducedComplexList.size());
		return reducedComplexList;
	}

	/**
	 * Prints the Proteins: names, number of complexes they are in, number of
	 * interactions they have.
	 */
	public void printProteins() {
		Collection<Protein> p = proteins.values();
		Iterator<Protein> iterator = p.iterator();
		while (iterator.hasNext()) {
			Protein temp = (Protein) iterator.next();
			System.out.print(temp.getName() + ": "
					+ temp.getNumberOfComplexes() + " complexes, "
					+ temp.getNumberOfInteractions() + " interactions ");
			// Output of the interacting proteins
			// ArrayList<Protein> adjacenceList = temp.getInteractions();
			// for(int i = 0; i< temp.getNumberOfInteractions(); i++){
			// System.out.print(adjacenceList.get(i).getName() + " ");
			// }
			System.out.print("\n");
		}
	}

}
