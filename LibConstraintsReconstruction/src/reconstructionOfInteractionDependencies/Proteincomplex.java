/* Copyright (c) 2012, Bianca Patro
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package reconstructionOfInteractionDependencies;

import java.util.ArrayList;

/**
 * Class for storing the informations of a proteincomplex. Contains the id and a list of proteins for each complex.
 * @author Bianca Patro
 *
 */
public class Proteincomplex {
	
	/** Each complex has a unique id. */
	private int id;
	/** List of proteins in this complex. */
	private ArrayList<Protein> proteins;
	
	/**
	 * Constructor which sets the id and initializes the list of proteins.
	 * @param pId - id of the complex
	 */
	public Proteincomplex(int pId) {
		id = pId;
		proteins = new ArrayList<Protein>();
	}
	
	/**
	 * Returns the list of proteins in this complex.
	 * @return the list of proteins in this complex
	 */
	public ArrayList<Protein> getProteins() {
		return proteins;
	}

	/**
	 * Returns the id of this complex.
	 * @return the id of this complex
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Adds a protein to this complex.
	 * @param p - the protein to be added
	 */
	public void addProtein(Protein p){
		proteins.add(p);
	}
	
	/**
	 * Tests if the complex contains the given protein.
	 * @param p - the protein whose presence is to be tested
	 * @return true iff the protein p is in this complex
	 */
	public boolean containsProtein(Protein p){
		return proteins.contains(p);
	}
	
	/**
	 * Tests if the complex contains all three given proteins.
	 * @param protein1 - a protein
	 * @param protein2 - a protein
	 * @param protein3 - a protein
	 * @return true iff all three proteins are in this complex
	 */
	public boolean contains3Proteins(Protein protein1, Protein protein2, Protein protein3){
		return (proteins.contains(protein1) &&
				proteins.contains(protein2) &&
				proteins.contains(protein3));
	}
	
	/**
	 * Tests if the complex contains both given proteins.
	 * @param protein1 - a protein
	 * @param protein2 - a protein
	 * @return true iff the complex contains both proteins
	 */
	public boolean contains2Proteins(Protein protein1, Protein protein2){
		return (proteins.contains(protein1) &&
				proteins.contains(protein2));
	}
	
	/**
	 * Tests if the complex contains at least two of the three given proteins.
	 * @param protein1 - a protein
	 * @param protein2 - a protein
	 * @param protein3 - a protein
	 * @return true iff the complex contains at least two of the three given proteins
	 */
	public boolean containsMin2Of3Proteins(Protein protein1, Protein protein2, Protein protein3){
		return ((proteins.contains(protein1) && proteins.contains(protein2)) ||
				(proteins.contains(protein2) && proteins.contains(protein3)) ||
				(proteins.contains(protein3) && proteins.contains(protein1)));
	}
	
	/** 
	 * Returns the number of proteins in this complex.
	 * @return the number of proteins in this complex
	 */
	public int getNumberOfProteins(){
		return proteins.size();
	}
	
	@Override
	public String toString(){
		String result = "" + id + ": {";
		for (int i = 0; i < proteins.size()-1; i++){
			result = result + proteins.get(i).getName() + ", ";
		}
		result = result + proteins.get(proteins.size()-1).getName() + "}";
		return result;
	}
	
	
}
