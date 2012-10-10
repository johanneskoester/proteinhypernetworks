/* Copyright (c) 2012, Bianca Patro
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */


package reconstructionOfInteractionDependencies;

import java.util.ArrayList;

/**
 * Class for modelling a protein. Each protein has an unique name, a list of interacting proteins and a number of complexes it is in.
 * @author Bianca Patro
 *
 */
public class Protein {
	
	/** The name of the protein	 */
	private String name;
	/** List of interacting proteins */
	private ArrayList<Protein> interactions;
	/** Number of proteincomplexes this protein is in */
	private int numberOfComplexes;
	
	/**
	 * Constructor which only sets the name of the protein and initializes an empty list of interactions.
	 * @param pName - name of the protein
	 */
	public Protein(String pName){
		name = pName;
		interactions = new ArrayList<Protein>();
		numberOfComplexes = 0;
	}

	/**
	 * Returns the name of the protein.
	 * @return the name of the protein
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the list of interactions.
	 * @return the list of interactions
	 */
	public ArrayList<Protein> getInteractions() {
		return interactions;
	}
	
	/**
	 * Returns the number of interactions.
	 * @return the number of interactions
	 */
	public int getNumberOfInteractions() {
		return interactions.size();
	}
	
	/** 
	 * Adds an interaction with a protein p to the list of interactions.
	 * @param p - the interacting protein
	 */
	public void addInteraction(Protein p){
		interactions.add(p);
	}
	
	/**
	 * Returns true iff this protein interacts with the protein p.
	 * @param p - a protein 
	 * @return true iff this protein interacts with p
	 */
	public boolean interactsWithProtein(Protein p){
		return interactions.contains(p);
	}
	
	/**
	 * Returns the number of complexes this protein is in.
	 * @return the number of complexes this protein is in
	 */
	public int getNumberOfComplexes(){
		return numberOfComplexes;
	}
	
	/**
	 * Increases the number of complexes by one.
	 */
	public void increaseNumberOfComplexes(){
		numberOfComplexes++;
	}
	
	/**
	 * Checks if the object o is a protein with the same name as this protein.
	 */
	@Override
	public boolean equals(Object o){
		if ( o == null ){
			return false;
		}
		if ( o == this ){
			return true;
		}
	    if ( !o.getClass().equals(getClass())){
	    	return false;
	    }	      
		Protein that = (Protein) o;
		return  this.name == that.getName();
	}
	
	@Override	
	public int hashCode(){
	    return 31 * this.name.hashCode();
	}
	
}
