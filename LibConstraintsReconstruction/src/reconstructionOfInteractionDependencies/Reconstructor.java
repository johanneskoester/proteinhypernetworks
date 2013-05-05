/* Copyright (c) 2012, Bianca Patro
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package reconstructionOfInteractionDependencies;

import java.io.*;
import java.math.BigInteger;
import java.util.StringTokenizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Set;

import util.Log;
import util.PairDoubleString;

import modalLogic.formula.Formula;
import modalLogic.formula.Constant;
import modalLogic.formula.Literal;
import modalLogic.formula.factory.FormulaFactory;

//import quineMcCluskey.Formula;
import quineMcCluskey.Term;


/**
 * Class for reconstructing interaction dependencies between proteininteractions. For a given truth-table the represented formula in SINF is calculated. 
 * Furthermore there are additional methods for manipulation of propositional logic formulas. 
 * @author Bianca Patro
 *
 */
public class Reconstructor {
	

	/**	A log for different output-levels depending on a parameter. */
	public Log log;
	/** Names of the observed interactions. */
	private String[] literalNames;


	/** Threshold to get all lines with a truth-value truly greater than zero. */
	public static final double thresholdAllGreaterZero = Double.MIN_NORMAL;
	
	
	/**
	 * Constructor which initially sets the log-level to a default-value 
	 * and prints the output on std-out.
	 */
	public Reconstructor(){
		 log = new Log();
	}
	

	/**
	 * Constructor which initially sets the log-level according to the parameter and prints the output on std-out.
	 * @param outputLevel - Defines how much (debug-)output is displayed. 
	 * Choose a negative number for none output, 0 for little output, 10 (or greater) for the most detailed output. 
	 * Output is printed on std-out.
	 */
	public Reconstructor(int outputLevel){
		 log = new Log(outputLevel);
	}
	
	
	/**
	 * Constructor which initially sets the log-level according to the parameter and prints the output in a specified file.
	 * @param outputLevel - Defines how much (debug-)output is displayed. 
	 * Choose a negative number for none output, 0 for little output, 10 (or greater) for the most detailed output. 
	 * @param outputPath - path to a file in which the output is written
	 */
	public Reconstructor(int outputLevel, String outputPath){
		 log = new Log(outputLevel, outputPath);
	}
	
	
	
	/**
	 * Checks the input if it leads to a valid truth-table, then executes the 1-lines-method and after that the 0-lines-method
	 * and gives output about the formula-sizes for comparing the methods. 
	 * In addition the reduction methods are also executed and evaluated. <br>
	 * This method should be called with log-level 0, because all relevant output is written at this level. 
	 * For default all entries with a truth-value truly greater than zero are counted as one.
	 * @param path - path to a file with a truth-table	 
	 */	
	public void compareBothMethods(String path){
		compareBothMethods(path, thresholdAllGreaterZero); 
	}
	
	
	/**
	 * Checks the input if it leads to a valid truth-table, then executes the 1-lines-method and after that the 0-lines-method
	 * and gives output about the formula-sizes for comparing the methods. 
	 * In addition the reduction methods are also executed and evaluated. <br>
	 * This method should be called with log-level 0, because all relevant output is written at this level.	
	 * @param path - path to a file with a truth-table
	 * @param threshold - limit to check whether a entry is counted as 1 or 0
	 */	
	@SuppressWarnings("unchecked")
	public void compareBothMethods(String path, double threshold){
		if (!checkInputTruthTable(path)){
			throw new IllegalArgumentException("Invalid Thruth-Table!");
		}
		log.printMessage(0,"> " + path + "\n");
		log.printMessage(0, "1\t");
		quineMcCluskey.Formula quineFormula1 = readOnly1LinesFromTruthtableToFormula(path, threshold);
		long start = System.currentTimeMillis();
		Formula<String> formula1;
		if (quineFormula1 != null){
			log.printMessage(10, "Formula represented through truth-table: \n" + quineFormula1.toString() + "\n");
			quineFormula1 = executeQuineMcCluskey(quineFormula1);
			formula1  = from1LinesQuineMcCluskeyFormulaToLibModallogicFormulaInDNF(quineFormula1, literalNames);
		}else{
			formula1 = new Constant(false);			
		}
		log.printMessage(3, "Formula in DNF represented in LibModalLogic:\n" + formula1.toString() + "\n");
		log.printMessage(0, "DNF: " + getNumberOfClauses(formula1) + "\t");
		
		formula1 = fromDNFtoCNF(formula1);
		/* CNF*/	log.printMessage(0, "CNF: " + getNumberOfClauses(formula1) + "\t");
					Formula<String> formulaInINF1 = fromCNFtoSINF(formula1);
					long end = System.currentTimeMillis();	
					long timeSINF = end - start;
		/* SINF*/	log.printMessage(0, "SINF: " + getNumberOfImplications(formulaInINF1) + "\t");
					Formula<String> cloneINF1 = formulaInINF1.clone();
					start = System.currentTimeMillis();			
					Formula<String> reducedformulaInINF1 = reduceSINF1(formulaInINF1);
					end = System.currentTimeMillis();
					long timeRed1 = end - start;
		/* red1*/	log.printMessage(0, "Red1: " + getNumberOfImplications(reducedformulaInINF1) + "\t");
					start = System.currentTimeMillis();
					reducedformulaInINF1 = reduceSINF2(reducedformulaInINF1);
					end = System.currentTimeMillis();
					long timeRed12 = timeRed1 + end - start;
		/* red1+2*/	log.printMessage(0, "Red1+2: " + getNumberOfImplications(reducedformulaInINF1) + "\t");
					start = System.currentTimeMillis();
					Formula<String> reducedCloneformulaInINF1 = reduceSINF2(cloneINF1);
					end = System.currentTimeMillis();
					long timeRed2 = end - start;
		/* red2*/	log.printMessage(0, "Red2: " + getNumberOfImplications(reducedCloneformulaInINF1) + "\t");
					start = System.currentTimeMillis();
					reducedCloneformulaInINF1 = reduceSINF1(reducedCloneformulaInINF1);
					end = System.currentTimeMillis();
					long timeRed21 = timeRed2 + end - start;
		/* red2+1*/	log.printMessage(0, "Red2+1: " + getNumberOfImplications(reducedCloneformulaInINF1) + "\n");
		
		/* times */ log.printMessage(0, "1\t" + "times:\tSINF: " + timeSINF + "\tRed1: " + timeRed1 + "\tRed1+2: " + timeRed12
				+ "\tRed2: " + timeRed2 + "\tRed2+1: " + timeRed21 + "\n");
		/* SINF1*/   log.printMessage(7, "< before reducing: " + formulaInINF1.toString() + "\n");	
		/* SINF2*/   log.printMessage(0, "< after reducing1+2: " + reducedformulaInINF1.toString() + "\n");	
		/* SINF3*/   log.printMessage(0, "< after reducing2+1: " + reducedCloneformulaInINF1.toString() + "\n");	
		log.printMessage(0, "equivalence test for oneLines Red1+2<->Red2+1: " + areEquivalent(reducedformulaInINF1, reducedCloneformulaInINF1) + "\n");
		log.printMessage(0, "Red1+2 == Red2+1? "+reducedformulaInINF1.toString().equals(reducedCloneformulaInINF1.toString()) + "\n");
		
		
		log.printMessage(0, "0\t");
		quineMcCluskey.Formula quineFormula0 = readOnly0LinesFromTruthtableToFormula(path, threshold);
		start = System.currentTimeMillis();
		Formula<String> formula0;
		if (quineFormula0 != null){
			log.printMessage(10, "Formula represented through truth-table: \n" + quineFormula0.toString() + "\n");
			quineFormula0 = executeQuineMcCluskey(quineFormula0);
			log.printMessage(8, "Formula after executing complete Quine-McCluskey: \n" + quineFormula0.toString() +"\n");
			formula0  = from0LinesQuineMcCluskeyFormulaToLibModallogicFormulaInCNF(quineFormula0, literalNames);
		}else{
			formula0 = new Constant(true);			
		}
		/* CNF*/	log.printMessage(0, "CNF: " + getNumberOfClauses(formula0) + "\t");
					Formula<String> formulaInINF0 = fromCNFtoSINF(formula0);
					end = System.currentTimeMillis();	
					timeSINF = end - start;			
		/* SINF*/	log.printMessage(0, "SINF: " + getNumberOfImplications(formulaInINF0) + "\t");
					Formula<String> cloneINF = formulaInINF0.clone();
					start = System.currentTimeMillis();	
					Formula<String> reducedformulaInINF0 = reduceSINF1(formulaInINF0);
					end = System.currentTimeMillis();
					timeRed1 = end - start;
		/* red1*/	log.printMessage(0, "Red1: " + getNumberOfImplications(reducedformulaInINF0) + "\t");
					start = System.currentTimeMillis();	
					reducedformulaInINF0 = reduceSINF2(reducedformulaInINF0);
					end = System.currentTimeMillis();
					timeRed12 = timeRed1 + end - start;
		/* red1+2*/	log.printMessage(0, "Red1+2: " + getNumberOfImplications(reducedformulaInINF0) + "\t");
					start = System.currentTimeMillis();	
					Formula<String> reducedCloneformulaInINF0 = reduceSINF2(cloneINF);
					end = System.currentTimeMillis();
					timeRed2 = end - start;
		/* red2*/	log.printMessage(0, "Red2: " + getNumberOfImplications( reducedCloneformulaInINF0) + "\t");
					start = System.currentTimeMillis();	
					reducedCloneformulaInINF0 = reduceSINF1(reducedCloneformulaInINF0);
					end = System.currentTimeMillis();
					timeRed21 = timeRed2 + end - start;
		/* red2+1*/	log.printMessage(0, "Red2+1: " + getNumberOfImplications(reducedCloneformulaInINF0) + "\n");
		
		/* times */ log.printMessage(0, "0\t" + "times:\tSINF: " + timeSINF + "\tRed1: " + timeRed1 + "\tRed1+2: " + timeRed12
				+ "\tRed2: " + timeRed2 + "\tRed2+1: " + timeRed21 + "\n");
		/* SINF1*/   log.printMessage(7, "< before reducing: " + formulaInINF0.toString() + "\n");	
		/* SINF2*/   log.printMessage(0, "< after reducing1+2: " + reducedformulaInINF0.toString() + "\n");	
		/* SINF3*/   log.printMessage(0, "< after reducing2+1: " + reducedCloneformulaInINF0.toString() + "\n");	
		
		log.printMessage(0, "equivalence test for zeroLines Red1+2<->Red2+1: " + areEquivalent(reducedformulaInINF0, reducedCloneformulaInINF0) + "\n");
		log.printMessage(0, "Red1+2 == Red2+1? "+reducedformulaInINF0.toString().equals(reducedCloneformulaInINF0.toString()) + "\n");
		log.printMessage(0, "equivalence test for zeroLines and oneLines Red2+1<->Red2+1: " + areEquivalent(reducedCloneformulaInINF1, reducedCloneformulaInINF0) + "\n\n");
	}
	
		
//------------------------------------------------------------------------------
// Method 1: from 1-lines in truth-table to with Quine-McCluskey minimised DNF
//	to CNF to SINF
	
	/**
	 * Gets a path to a truth-table and executes all steps of the 1-lines-method without reducing the formula:
	 * Reading the 1-lines of the truth-table and executing the Quine-McCluskey on it. Transforming the resulting DNF
	 * into CNF and after that into SINF. <br>
	 * For default all entries with a truth-value truly greater than zero are counted as one.
	 * @param path - path to a file with a truth-table
	 * @return the formula represented through the truth-table in SINF
	 */
	public Formula<String> oneLinesMethod(String path){
		return oneLinesMethod(path, thresholdAllGreaterZero);
	}
	
	
	/**
	 * Gets a path to a truth-table and executes all steps of the 1-lines-method without reducing the formula:
	 * Reading the 1-lines of the truth-table and executing the Quine-McCluskey on it. Transforming the resulting DNF
	 * into CNF and after that into SINF.
	 * @param path - path to a file with a truth-table
	 * @param threshold - limit to check whether a entry is counted as 1 or 0
	 * @return the formula represented through the truth-table in SINF
	 */
	@SuppressWarnings("unchecked")
	public Formula<String> oneLinesMethod(String path, double threshold){
		if (!checkInputTruthTable(path)){
			throw new IllegalArgumentException("Invalid Thruth-Table!");
		}
		log.printMessage(0,"> " + path + "\n");
		log.printMessage(0, "1-lines-method\n");
		if (threshold == thresholdAllGreaterZero){
			log.printMessage(0, "Default-Threshold, all values greater zero are counted as one.\n");
		}else{
			log.printMessage(0, "Threshold: " + threshold + "\n"); 
		}
		quineMcCluskey.Formula quineFormula = readOnly1LinesFromTruthtableToFormula(path, threshold);
		Formula<String> formula;
		if (quineFormula != null){
			log.printMessage(10, "Formula represented through truth-table: \n" + quineFormula.toString() + "\n");
			quineFormula = executeQuineMcCluskey(quineFormula);
			
			formula  = from1LinesQuineMcCluskeyFormulaToLibModallogicFormulaInDNF(quineFormula, literalNames);
		}else{
			formula = new Constant(false);			
		}
		formula = fromDNFtoCNF(formula);		
		Formula<String> formulaInSINF = fromCNFtoSINF(formula);
		log.printMessage(1, getNumberOfImplications(formulaInSINF) + "\n");
		return formulaInSINF;
	}
	
	/**
	 * Reads the truth-table, stores only the lines with last entry '1' 
	 * and parses the boolean formula represented through the truth-table.
	 * @param  path - path to a .csv-file containing a truth-table
	 * @param threshold - threshold whether an entry is counted as 1 or 0
	 * @return the formula represented through the truth-table (in the Quine-McCluskey-Formula-Format)
	 */
	public quineMcCluskey.Formula readOnly1LinesFromTruthtableToFormula(String path, double threshold){
		quineMcCluskey.Formula formula = null;
		try {			
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;
			ArrayList<Term> terms = new ArrayList<Term>();
			// first line contains names of the interactions, which later become the literalNames
			String interactions = reader.readLine();			
			int i = 0;
			StringTokenizer tokenizer = new StringTokenizer(interactions, " \t");
			literalNames = new String[tokenizer.countTokens()];
			while(tokenizer.hasMoreTokens()){
				literalNames[i] = tokenizer.nextToken();
				i++;
			}				
			reader.mark(0);
			int lineCounter = 0;
			// counting the number of lines in the table 
			while((line = reader.readLine()) != null){
				lineCounter++;
			}
			reader.reset();
			double numberOfVariables = (Math.log(lineCounter)/Math.log(2));
						
			// line by line processing of the table
			while((line = reader.readLine()) != null){
				String lastToken = lastToken(line);
				double last = Double.parseDouble(lastToken);
				String lineWithoutLast = line.substring(0, line.lastIndexOf(lastToken));
				// if the last token is counted as 0, we discard the line 
				if (last >= 0 && last < threshold){
					continue;
				} else if (last >= threshold && last <= 1){
					Term term = parseTerm(lineWithoutLast);
					if (term != null){
						terms.add(term);
					}
				}else{
					System.out.println("Invalid truth-table");
					System.exit(-1);
				}								
			}
			if (terms.size() != 0){
				formula = new quineMcCluskey.Formula(terms);
			}else{
//				System.out.println("Error because of Nullfunction. Only lines with last entry '1' are stored.");
//				System.exit(-1);
				return null;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found! Please check spelling and path of the file.");
//			e.printStackTrace();
			System.exit(-1); 
		} catch (IOException e) {
			System.out.println("Error while reading the file.");
			e.printStackTrace();
			System.exit(-1);
		}		
		return formula;
	}

	
	/**
	 * Executes the complete QuineMcCluskey-Algorithm on a formula.
	 * @param formula - the formula to be reduced (has to be in the Quine-McCluskey-Formula-Format)
	 * @return the reduced formula (in the Quine-McCluskey-Formula-Format)
	 */
	public quineMcCluskey.Formula executeQuineMcCluskey(quineMcCluskey.Formula formula){
		formula.reduceToPrimeImplicants();
		log.printMessage(10, "PrimeImplicants of the formula: \n" + formula.toString() + "\n");
		formula.reducePrimeImplicantsToSubset();
		log.printMessage(7, "Formula after executing complete Quine-McCluskey: \n" + formula.toString() + "\n");
		return formula;
	}
	
	/**
	 * Returns the names of the literals after reading a truth-table.
	 * @return the names of the literals after reading a truth-table
	 */
	public String[] getLiteralNames() {
		return literalNames;
	}
	
	/**
	 * Transforms a formula in the Quine-McCluskey-Formula-Format 
	 * into a representation in the libModallogic-Formula-Format (formula is represented in DNF).
	 * @param quineFormula - the formula in the Quine-McCluskey-Formula-Format to be transformed
	 * @param literalNames - the names of the literals 
	 * @return the transformed formula in the libModallogic-Formula-Format
	 */
	@SuppressWarnings("unchecked")
	public Formula<String> from1LinesQuineMcCluskeyFormulaToLibModallogicFormulaInDNF(quineMcCluskey.Formula quineFormula, String[] literalNames){
		FormulaFactory<String> factory = new FormulaFactory<String>();
		List<byte[]> termList = quineFormula.getTermList();
		
		// indicates whether at least one term contains at least one literal that isn't don't-care (tautologies have only don't-cares)
		boolean min1Term = false; 
		if (termList.size() > 1){
			factory.openDisjunction(); // Disjunction only when more than one clause
		}		
		// each term is one clause in the disjunction
		for (int i = 0; i < termList.size(); i++){
			byte[] term = termList.get(i);
			int literalsInTerm = countZerosAndOnesInArray(term);
			if(literalsInTerm > 1){
				factory.openConjunction();;	// Conjunction only when more than one literal
			}			
			for (int j = 0; j < term.length; j++){
				// 0 stands for a negative literal
				if (term[j] == 0){
					min1Term = true;
					if (factory.getCurrent() == null){ // only one literal without disjunction or conjunction
						Formula<String> temp = new Literal<String>(literalNames[j]);
						temp.negate();
						return temp;
					}else{
						factory.negation();
						factory.literal(literalNames[j]);
					}
				}// 1 stands for a positive literal
				else if (term[j] == 1){
					min1Term = true;
					if (factory.getCurrent() == null){ // only one literal without disjunction or conjunction
						return new Literal<String>(literalNames[j]);
					}else{
						factory.literal(literalNames[j]);
					}
				}// in other cases the literal isn't in this term
			}
			if(literalsInTerm > 1){
				factory.close();	// Conjunction only when more than one literal
			}
		}
		if (termList.size() > 1){
			factory.close(); // Disjunction only when more than one clause
		}
		Formula<String> newFormula = factory.create();	
		
		// in case of a tautology
		if (!min1Term){	
			newFormula = new modalLogic.formula.Constant(true);
		}			
		log.printMessage(5, "Formula in DNF represented in LibModalLogic:\n" + newFormula.toString() + "\n");
		return newFormula;
	}
	

	/**
	 * Transforms a formula from DNF to CNF 
	 * @param formula - a formula in DNF to be transformed in CNF
	 * @return the transformed formula in minimised CNF
	 */
	public Formula<String> fromDNFtoCNF(Formula<String> formula){ 
		if (!isInDNF(formula)){
			throw new IllegalArgumentException("Formula has to be in DNF.");
		}
		// if formula is already in CNF (Constant, Literal, only one disjunctive or conjunctive clause) we have nothing to do here
		if(isInCNF(formula)){
			return formula;
		}
		// there are at least two conjunctive clauses in the formula
		Formula<String> temp = formula.getChild(0);	
		for (int i = 1; i < formula.getChildCount(); i++){
			temp = distributiveLaw1(temp, formula.getChild(i));
		}// now temp contains the CNF of formula		
		log.printMessage(2, "Number of clauses in the CNF: " + temp.getChildCount() + "\n");
		log.printMessage(6, "Formula in CNF: \n" + temp.toString() + "\n");
		
		return temp;
	}


	/**
	 * Applies the distributive law1: (A ∨ (B∧C)) = ((A∨B) ∧ (A∨C))
	 * @param clauses - a conjunction of disjunctive terms (e.g. (a∨b∨c)∧(¬c∨d) )
	 * @param conjunctiveTerm - a conjunctive term (e.g. (a∧b∧c) )
	 * @return a formula containing the result of the distributive law1
	 */
	@SuppressWarnings("unchecked")
	public Formula<String> distributiveLaw1 (Formula<String> clauses, Formula<String> conjunctiveTerm){
		FormulaFactory<String> factory = new FormulaFactory<String>();
		// Case1: clauses is only one literal
		if(clauses.getType() == Formula.LITERAL){
			// subcase: conjunctiveTerm is also only one literal
			if (conjunctiveTerm.getType() == Formula.LITERAL){
				factory.openDisjunction();
				placeLiterals(factory, clauses, conjunctiveTerm);	
				factory.close();
				Formula<String> temp = factory.create();
				return temp;
			}else{ // subcase: conjunctiveTerm is a conjunction with more than one literal
				factory.openConjunction();
				for (int j = 0; j < conjunctiveTerm.getChildCount(); j++){	
					factory.openDisjunction();
					placeLiterals(factory, clauses, conjunctiveTerm.getChild(j));
					factory.close();
				}
				factory.close();
			}// Case2: clauses is only one disjunctive clause
		}else if(clauses.getType() == Formula.DISJUNCTION){
			if (conjunctiveTerm.getType() == Formula.LITERAL){ // subcase: conjunctiveTerm is only one literal
				factory.openDisjunction();
				for (int i = 0; i < clauses.getChildCount(); i++){	
					placeLiterals(factory, clauses.getChild(i), null);
				}
				placeLiterals(factory, conjunctiveTerm, null);
				factory.close();
			}else{ // subcase: conjunctiveTerm is a conjunction with more than one literal
				factory.openConjunction();
				for (int j = 0; j < conjunctiveTerm.getChildCount(); j++){
					factory.openDisjunction();
					for (int i = 0; i < clauses.getChildCount(); i++){	
						placeLiterals(factory, clauses.getChild(i), null);
					}
					placeLiterals(factory, conjunctiveTerm.getChild(j), null);
					factory.close();
				}
				factory.close();
			}						
		}else{// Case3: clauses is a conjunction of disjunctive terms
			factory.openConjunction();			
			// for each clause from clauses we take each literal from the conjunctiveTerm
			for (int i = 0; i < getNumberOfClauses(clauses); i++){	
				Formula<String> actualClause = clauses.getChild(i);
				for (int j = 0; j < getNumberOfClauses(conjunctiveTerm); j++){	
					factory.openDisjunction();
						// if actualClause is a literal, we copy it as it is
						if (actualClause.getType() == Formula.LITERAL){
							placeLiterals(factory, actualClause, null);
						// if actualClause is a constant 	
						}else if (actualClause.getType() == Formula.CONSTANT){
							// if it is bottom we do nothing, because (⊥∨(B∧C)) = (B∧C)
							if (!actualClause.isNegation()){
								// if it is top, we jump out of this loop an go to the next clause, because ⊤∨(B∧C)) = ⊤
								break;
							}
						}else {
							// if actualClause is a disjunctive term, we take each child (=each literal) and copy it
							for (int k = 0; k < actualClause.getChildCount() ; k++){
								placeLiterals(factory, actualClause.getChild(k), null);
								log.printMessage(12, "DistributiveLaw1, loop-indices: i:" + i + ", j:" + j+ ", k:" + k + "\n");
							}
						}
						// subcase: conjunctiveTerm is only one literal
						if (conjunctiveTerm.getType() == Formula.LITERAL){
							placeLiterals(factory, conjunctiveTerm, null);
						}else{ // subcase: conjunctiveTerm is a conjunction with more than one literal
							// in this part the actual literal (index j of conjunctiveTerm) is put to the rest
							placeLiterals(factory, conjunctiveTerm.getChild(j), null);
						}
					factory.close();				
				}
			}
			factory.close();
		}
		Formula<String> temp = factory.create();	
		log.printMessage(7, "in distributiveLaw1: temp before reduction " + temp + "\n");
		// Reducing-part of the algorithm
		boolean clauseRemoved = false;
		// for each clause in temp we check if its a tautology or contains duplicate literals
		for (int i = 0; i < temp.getChildCount(); i++){	
			clauseRemoved = false;
			Formula<String> clause = temp.getChild(i);
			// for each literal in a clause we check each other literal in the same clause
			for (int j = 0; (j < clause.getChildCount() && !clauseRemoved); j++){	
				for (int k = j+1; (k < clause.getChildCount() && !clauseRemoved); k++){	
					// Check if the clause contains 'A' and '¬A' for a literal 'A' -> tautologies can be removed
					if (areContraryLiterals(clause.getChild(j), clause.getChild(k))){
						clauseRemoved = true;
						temp.removeChild(clause);
						i--;							
					}	
					// Check if there are duplicate literals
					else if (clause.getChild(j).getProposition().equals(clause.getChild(k).getProposition())) {
						clause.removeChild(clause.getChild(j));			
						j--;
					}
				}
			}
		}
		// If all clauses are tautologies (and because of this have been removed) temp has to be 'top'
		if (temp.getChildCount() == 0 && temp.getType() != Formula.LITERAL){ // != LITERAL because literals have childCount=0
			temp =  new modalLogic.formula.Constant(true);
		}
		return temp;
	}
	
	
	/**
	 * Transforms a formula from CNF to SINF
	 * @param formula - a formula in CNF to be transformed in SINF
	 * @return the transformed formula in SINF
	 */
	public Formula<String> fromCNFtoSINF(Formula<String> formula){
		if(!isInCNF(formula)){
			throw new IllegalArgumentException("Formula has to be in CNF.");
		}
		short type = formula.getType(); 
		FormulaFactory<String> factory = new FormulaFactory<String>();
		// Case1: formula is only a constant: 
		if (type == Formula.CONSTANT){
			if(formula.isNegation()){
			// '⊥' results in (⊤ ⇒ ⊥)	
			factory.openImplication();
				factory.constant(true);
				factory.constant(false);
			factory.close();
			}else{
			// '⊤' results in (⊤ ⇒ ⊤)	
			factory.openImplication();
				factory.constant(true);
				factory.constant(true);
			factory.close();
			}		
			Formula<String> formulaInINF = factory.create();	
			return formulaInINF;
		}		
		// Case2: formula is only one literal
		if (type == Formula.LITERAL){
			// '¬a' results in (a ⇒ ⊥)
			if (formula.isNegation()){
				factory.openImplication();
					factory.literal(formula.getProposition());
					factory.constant(false);
				factory.close();
			}else{
				// 'a' results in (⊤ ⇒ a)	
				factory.openImplication();
					factory.constant(true);
					factory.literal(formula.getProposition());
				factory.close();
			}
			Formula<String> formulaInINF = factory.create();	
			return formulaInINF;
		}
		// Case3: there is one clause
		boolean min1Implication = false; // indicates if there has been at least one implication
		if (type == Formula.DISJUNCTION){
			int numberOfNegativeLiterals = getNumberOfNegativeLiterals(formula);
			if (formula.getChildCount() > 1 && numberOfNegativeLiterals > 1){
				factory.openConjunction();	// Conjunction only when more than one negative literal
			}
			// for each negative literal in this clause we generate one implication
			for (int j = 0; j < formula.getChildCount(); j++){	
				if (formula.getChild(j).isNegation()){
					min1Implication = true;							
					factory.openImplication();
						factory.literal(formula.getChild(j).getProposition());
						Formula<String> clone = formula.clone();
						clone.removeChild(formula.getChild(j));
						if (clone.getChildCount() == 1){ // after removing the premise there is one literal left
							placeLiterals(factory, clone.getChild(), null);
						}else{ // there is more than one literal left
							factory.subformula(clone);	
						}
					factory.close();								
				}
			}
			// if all literals are positive, we build an implication with top 
			if (!min1Implication){	//(a∨b∨c) = (⊤ ⇒ a∨b∨c)				
				factory.openImplication();
					factory.constant(true);
					factory.subformula(formula);						
				factory.close();					
			}
			if (formula.getChildCount() > 1 && numberOfNegativeLiterals > 1){
				factory.close();	// Conjunction only when more than one negative literal
			}
			formula = factory.create();
			return formula;
		}
		// Case4: there are at least two clauses in the formula
		// generation of all possible implications from the clauses in the CNF	
		if (formula.getChildCount() > 1){
			factory.openConjunction();		// Conjunction only when more than one clause
		}
		for (int i = 0; i < formula.getChildCount(); i++){
			min1Implication = false;
			Formula<String> clause = formula.getChild(i);
			// clause has only one literal
			if (clause.getType() == Formula.LITERAL){
				min1Implication = true;	
				// '¬a' results in (a ⇒ ⊥)
				if (clause.isNegation()){
					factory.openImplication();
						factory.literal(clause.getProposition());
						factory.constant(false);
					factory.close();
				}else{
					// 'a' results in (⊤ ⇒ a)	
					factory.openImplication();
						factory.constant(true);
						factory.literal(clause.getProposition());
					factory.close();
				}
				continue; // finish this loop-cycle and go to the next clause
			}
			// clause is a disjunction of literals
			// for each negative literal in each clause we generate one implication
			for (int j = 0; j < clause.getChildCount(); j++){	
				if (clause.getChild(j).isNegation()){
					min1Implication = true;							
					factory.openImplication();
						factory.literal(clause.getChild(j).getProposition());
						Formula<String> clone = clause.clone();
						clone.removeChild(clause.getChild(j));
						if (clone.getChildCount() == 1){ // after removing the premise there is one literal left
							placeLiterals(factory, clone.getChild(), null);
						}else if (clone.getChildCount() == 0){ // one literal wrapped in disjunction after minimising CNF leaves an empty clone
							factory.constant(false);
						}else{ // there is more than one literal left
							factory.subformula(clone);	
						}
					factory.close();								
				}
			}
			// if all literals are positive, we build an implication with top 
			if (!min1Implication){	//(a∨b∨c) = (⊤ ⇒ a∨b∨c)				
				factory.openImplication();
					factory.constant(true);
					factory.subformula(clause);						
				factory.close();					
			}
		}	
		if (formula.getChildCount() > 1){
			factory.close();	// Conjunction only when more than one clause
		}
		formula = factory.create();
		// now temp contains the formula in SINF and has to be minimised
		
		log.printMessage(2, "Number of implications (without minimising): " + getNumberOfImplications(formula) + "\n");
		log.printMessage(6, "Formula in SINF (without minimising): " + formula + "\n");
		return formula;
	}
	
	
	/**
	 * Reduces a propositional logic formula in SINF by removing implications, 
	 * which conclusions are a superset of an other implications conclusion, when both implications have the same premise
	 * @param formula - the formula to be reduced
	 * @return the reduced formula in SINF
	 */
	public Formula<String> reduceSINF1(Formula<String> formula){
		Formula<String> clone = formula.clone();
		if (clone.getType() == Formula.IMPLICATION){
			return clone;
		}
		// for each implication we check if there is another implication that contains all literals of it
		for (int i = 0; i < clone.getChildCount(); i++){
			Formula<String> implication = clone.getChild(i);
			Formula<String> actualProposition = implication.getChild(0);
			for (int j = i+1; j < clone.getChildCount(); j++){	
				// if two implications have the same proposition at the premise, we compare the conclusions
				// in 'a -> b' a is the premise and b is the conclusion 
				if ((i != j) && clone.getChild(j).getChild(0).equals(actualProposition)){
					Formula<String> conclusion1 = implication.getChild(1);
					Formula<String> conclusion2 = clone.getChild(j).getChild(1);					
					int compareResult = haveSameLiterals(conclusion1, conclusion2);
					log.printMessage(10, "in MinimizeINF:\t Implication1: "+ conclusion1 + "Implication2: "+ conclusion2 + "CompareResult: " + compareResult + "\n");
					// if conclusion1 contains all literals of conclusion2, we delete the complete implication of conclusion1
					if (compareResult == 1){
						clone.removeChild(implication);
						i--;
						break;
					// if conclusion2 contains all literals of conclusion1, we delete the complete implication of conclusion2
					}else if (compareResult == -1){
						clone.removeChild(clone.getChild(j));
						j--;
					}
				}
			}
		}
		return clone;
	}
	
	
	/**
	 * Reduces a propositional logic formula in SINF by removing equivalent implications.
	 * @param formula - the formula to be reduced
	 * @return the reduced formula in SINF
	 */
	public Formula<String> reduceSINF2(Formula<String> formula){
		Formula<String> clone = formula.clone();
		if (clone.getType() == Formula.IMPLICATION){
			return clone;
		}
		// for each implication we check if there is another implication that is equivalent
		for (int i = 0; i < clone.getChildCount(); i++){
			Formula<String> temp = clone.getChild(i);
			for (int j = i+1; j < clone.getChildCount(); j++){
				if (areEquivalent(temp, clone.getChild(j))){
					// if two implications are equivalent, we remove the first
					clone.removeChild(temp);
					i--;
					break;
				}
			}
		}
		return clone;
	}
	
	
	
	
//------------------------------------------------------------------------------
// Method 0: from 0-lines in truth-table to with Quine-McCluskey minimised CNF to SINF
//
		
	/**
	 * Gets a path to a truth-table and executes all steps of the 0-lines-method without reducing the formula:
	 * Reading the 0-lines of the truth-table and executing the Quine-McCluskey on it. Transforming the resulting CNF
	 * into SINF. For default all entries with a truth-value truly greater than zero are counted as one.
	 * @param path - path to a file with a truth-table
	 * @return the formula represented through the truth-table in SINF
	 */	
	public Formula<String> zeroLinesMethod(String path){
		return zeroLinesMethod(path, thresholdAllGreaterZero);
	}
	
	
	/**
	 * Gets a path to a truth-table and executes all steps of the 0-lines-method without reducing the formula:
	 * Reading the 0-lines of the truth-table and executing the Quine-McCluskey on it. Transforming the resulting CNF
	 * into SINF.
	 * @param path - path to a file with a truth-table
	 * @param threshold - limit to check whether a entry is counted as 1 or 0
	 * @return the formula represented through the truth-table in SINF
	 */	
	@SuppressWarnings("unchecked")
	public Formula<String> zeroLinesMethod(String path, double threshold){
		if (!checkInputTruthTable(path)){
			throw new IllegalArgumentException("Invalid Thruth-Table!");
		}
		log.printMessage(0,"> " + path + "\n");
		log.printMessage(0, "0-lines-method\n");
		if (threshold == thresholdAllGreaterZero){
			log.printMessage(0, "Default-Threshold, all values greater zero are counted as one.\n");
		}else{
			log.printMessage(0, "Threshold: " + threshold + "\n");
		}
		quineMcCluskey.Formula quineFormula = readOnly0LinesFromTruthtableToFormula(path, threshold);
		Formula<String> formula;
		if (quineFormula != null){
			log.printMessage(10, "Formula represented through truth-table: \n" + quineFormula.toString());
			quineFormula = executeQuineMcCluskey(quineFormula);
			formula  = from0LinesQuineMcCluskeyFormulaToLibModallogicFormulaInCNF(quineFormula, literalNames);
		}else{
			formula = new Constant(true);			
		}
		Formula<String> formulaInSINF = fromCNFtoSINF(formula);
		log.printMessage(1, getNumberOfImplications(formulaInSINF) + "\n");
		return formulaInSINF;

	}
	

	/**
	 * Reads the truth-table, stores only the lines with last entry '0' 
	 * and parses the boolean formula represented through the truth-table.
	 * @param path - path to a .csv-file containing a truth-table
	 * @return the formula represented through the truth-table (in the Quine-McCluskey-Formula-Format)
	 */
	public quineMcCluskey.Formula readOnly0LinesFromTruthtableToFormula(String path, double threshold){
		quineMcCluskey.Formula formula = null;
		try {			
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;
			ArrayList<Term> terms = new ArrayList<Term>();
			
			// first line contains names of the interactions, which later become the literalNames
			String interactions = reader.readLine();			
			int i = 0;
			StringTokenizer tokenizer = new StringTokenizer(interactions, " \t");
			literalNames = new String[tokenizer.countTokens()];
			while(tokenizer.hasMoreTokens()){
				literalNames[i] = tokenizer.nextToken();
				i++;
			}	
						
			// line by line processing of the table
			while((line = reader.readLine()) != null){
				String lastToken = lastToken(line);
				double last = Double.parseDouble(lastToken(line));
				String lineWithoutLast = line.substring(0, line.lastIndexOf(lastToken));
				// if the last token is counted as 1, we discard the line 
				if (last >= threshold && last <= 1){
					continue;
				} else if (last >= 0 && last < threshold){
					Term term = parseTerm(lineWithoutLast);
					if (term != null){
						terms.add(term);
					}
				}else{
					System.out.println("Invalid truth-table");
					System.exit(-1);
				}								
			}							
			if (terms.size() != 0){
				formula = new quineMcCluskey.Formula(terms);
			}else{
//					System.out.println("Error because of Onefunction. Only lines with last entry '0' are stored.");
//					System.exit(-1);
				return null;
			}	
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found! Please check spelling and path of the file.");
//				e.printStackTrace();
			System.exit(-1); 
		} catch (IOException e) {
			System.out.println("Error while reading the file.");
//				e.printStackTrace();
			System.exit(-1);
		}		
		return formula;
	}

	
	/**
	 * Transforms a formula in the Quine-McCluskey-Formula-Format into a representation in the libModallogic-Formula-Format (formula is represented in CNF).
	 * @param quineFormula - the formula in the Quine-McCluskey-Formula-Format to be transformed
	 * @param literalNames - the names of the literals 
	 * @return the transformed formula in the libModallogic-Formula-Format
	 */
	@SuppressWarnings("unchecked")
	public Formula<String> from0LinesQuineMcCluskeyFormulaToLibModallogicFormulaInCNF(quineMcCluskey.Formula quineFormula, String[] literalNames){
		FormulaFactory<String> factory = new FormulaFactory<String>();
		List<byte[]> termList = quineFormula.getTermList();
		// indicates whether at least one term contains at least one literal that isn't don't-care (tautologies have only don't-cares)
		boolean min1Term = false; 
		if (termList.size() > 1){
			factory.openConjunction(); // Conjunction only when more than one clause
		}
		// each term is one clause in the conjunction			
		for (int i = 0; i < termList.size(); i++){
			byte[] term = termList.get(i);		
			int literalsInTerm = countZerosAndOnesInArray(term);
			if(literalsInTerm > 1){
				factory.openDisjunction();	// Disjunction only when more than one literal
			}
			for (int j = 0; j < term.length; j++){
				// 0 stands for a positive literal in the maxterm
				if (term[j] == 0){
					min1Term = true;
					if (factory.getCurrent() == null){ // only one literal
						return new Literal<String>(literalNames[j]);
					}else{
						factory.literal(literalNames[j]);
					}
				}// 1 stands for a negative literal in the maxterm
				else if (term[j] == 1){
					min1Term = true;
					if (factory.getCurrent() == null){ // only one literal
						Formula<String> temp = new Literal<String>(literalNames[j]);
						temp.negate();
						return temp;
					}else{
						factory.negation();
						factory.literal(literalNames[j]);
					}	
				}// in other cases the literal isn't in this term
			}
			if(literalsInTerm > 1){
				factory.close();	// Disjunction only when more than one literal
			}
		}
		if (termList.size() > 1){
			factory.close(); // Conjunction only when more than one clause
		}
		Formula<String> newFormula = factory.create();	
		
		// in case of a tautology
		if (!min1Term){	
			newFormula = new modalLogic.formula.Constant(false);
		}	
		log.printMessage(3, "Number of clauses in the CNF: " + newFormula.getChildCount() + "\n");
		log.printMessage(6, "Formula in CNF: \n" + newFormula.toString() + "\n");
		return newFormula;
	}
				

//------------------------------------------------------------------------------
// equivalence-test, functionEvaluation, areNegationOfEachOther, isInDNF, isInCNF....			
	
	
	/**
	 * Calculates up to two different thresholds for the given truth-table.
	 * @param path - path to a file with a truth-table
	 * @return a ArrayList containing the suggested thresholds and a description of the thresholds
	 */
 	public ArrayList<PairDoubleString>suggestThresholdsForTruthTable(String path){
 		// Reading the truth-values into a double-array
 		double [] truthValues = null;
 		int numberOfLines = 0;
 		try {			
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;
			// first line contains names of the interactions -> can be ignored
			StringTokenizer tokenizer = new StringTokenizer(reader.readLine(), " \t");	
			numberOfLines = 1 << (tokenizer.countTokens() - 1);
			truthValues = new double[numberOfLines];			
			int i = 0;
			// line by line processing of the table
			while((line = reader.readLine()) != null){
				double last = Double.parseDouble(lastToken(line));
				truthValues[i] = last;
				i++;
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found! Please check spelling and path of the file.");
//				e.printStackTrace();
			System.exit(-1); 
		} catch (IOException e) {
			System.out.println("Error while reading the file.");
//				e.printStackTrace();
			System.exit(-1);
		} 
		// Analyzing the truth-values to determine reasonable thresholds
		Arrays.sort(truthValues);
		ArrayList<PairDoubleString> thresholds = new ArrayList<PairDoubleString>();		
		double[] distances = new double[numberOfLines-1];
		double maxDistance = 0;
		int marker = 0;
		for (int i = 0; i < truthValues.length-1; i++){
			// not comparing with 0 or 1 for the distance
			if ((truthValues[i] < 1 && truthValues[i+1] < 1
					&& truthValues[i] > thresholdAllGreaterZero && truthValues[i+1] > thresholdAllGreaterZero)){
				distances[i] = truthValues[i]-truthValues[i+1];
				if (maxDistance > distances[i]){
					maxDistance = distances[i];
					marker = i;
				}	
			}
		}
		double distanceThreshold = (truthValues[marker]+truthValues[marker+1])/2;
		
		// Counting how much lines are counted as one with the different thresholds
		int countDefault = 0;
		int countDistance = 0;
		for (int i = 0; i < truthValues.length; i++){
			if (truthValues[i] > 0){
				countDefault++;
			}
			if (truthValues[i] >= distanceThreshold){
				countDistance++;
			}

		}		
		thresholds.add(new PairDoubleString(thresholdAllGreaterZero, "Default-Threshold counts all values truly greater zero as one. " +
				countDefault + " lines counted as one and " + (numberOfLines-countDefault) + " lines counted as zero."));
		if (distanceThreshold > thresholdAllGreaterZero && countDistance != countDefault){ // distanceThreshold is only reasonably if distanceThreshold != 0 
			thresholds.add(new PairDoubleString(distanceThreshold, "Threshold as arithmetic mean between the truth-values with the greatest distance. " +
				countDistance + " lines counted as one and " + (numberOfLines-countDistance) + " lines counted as zero."));
		}

		// Printing the thresholds at log-level 1
		log.printMessage(1, "Overview over suggested thresholds: \n");
		for(int i = 0; i < thresholds.size(); i++){
			log.printMessage(1, thresholds.get(i).toString() + "\n");
		}
		log.printMessage(1,  "\n");
		
//		for (int i = 0; i < truthValues.length; i++){
//			System.out.print(truthValues[i]+ "\t");
//		}
//		System.out.print("\n");
		return thresholds;
 	}
	
	
	/**
 	 * Checks if the path leads to a valid truth-table. A truth-table is valid, 
 	 * when there are no duplicate lines, each line has the same number of tokens, 
 	 * and the table is complete, i.e. the number of lines is 2^(numberOfVariables+1)
 	 * (The content of the table (only 0 and 1) is not tested in this method, but when the table is parsed.)
 	 * @param path - path to a truth-table in a .csv-file
 	 * @return true iff the truth-table is valid, otherwise a IllegalArgumentException is thrown
 	 */
 	public boolean checkInputTruthTable(String path){
 		try {			
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String line;
			HashMap<String,String> testMap = new HashMap<String, String>();
			
			// first line contains names of the interactions, which later become the literalNames
			// -> each line has to have the same number of tokens as the first line
			String interactions = reader.readLine();				
			StringTokenizer tokenizer = new StringTokenizer(interactions, " \t");
			int numberOfColumns = tokenizer.countTokens();
			BigInteger lineCounter = new BigInteger("1");		
			// line by line processing of the table
			while((line = reader.readLine()) != null){
				tokenizer = new StringTokenizer(line);
				// test if the line has the correct number of tokens
				if(tokenizer.countTokens() != numberOfColumns){
					throw new IllegalArgumentException("Invalid truth-table - there is a line with the wrong number of tokens.");
				}
				String lastToken = lastToken(line);
				String lineWithoutLast = line.substring(0, line.lastIndexOf(lastToken));
				// test if a line has a duplicate 
				if (testMap.get(lineWithoutLast) != null){
					throw new IllegalArgumentException("Invalid truth-table - there are duplicate lines in the table.");
				}else{
					testMap.put(lineWithoutLast, lastToken);
				}
				lineCounter = lineCounter.add(BigInteger.ONE);		
			}
			// test if the table has the correct number of lines
			if(lineCounter.intValue() != ((1 << (numberOfColumns -1))+1)){
				throw new IllegalArgumentException("Invalid truth-table - the table has the wrong numer of lines.");
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("in checkInputTruthTable: File not found! Please check spelling and path of the file.");
//			e.printStackTrace();
			System.exit(-1); 
		} catch (IOException e) {
			System.out.println("in checkInputTruthTable: Error while reading the file.");
//			e.printStackTrace();
			System.exit(-1);
		}
 		return true;
 	}
 	
	
	/**
	 * Tests if a formula is in DNF.
	 * @param formula - the formula to be tested
	 * @return true iff the formula is in DNF
	 */
	public boolean isInDNF(Formula<String> formula){
		short type = formula.getType();
		// a literal or a constant are in DNF, otherwise it has to be a disjunction of conjunctions or only one conjunction
		if (!((type ==  Formula.DISJUNCTION) 
				|| (type ==  Formula.CONJUNCTION)
				|| (type ==  Formula.LITERAL)
				|| (type ==  Formula.CONSTANT))){
			return false;
		}
		// if there is only one conjunction, all children have to be literal or constant
		if (type == Formula.CONJUNCTION){
			for (int j = 0; j < formula.getChildCount(); j++){				
				short childType = formula.getChild(j).getType();
				if (!((childType ==  Formula.LITERAL)
						|| (childType ==  Formula.CONSTANT))){
					return false; 
				}
			}
		}
		// in the disjunction each child has to be constant, literal, or conjunction
		for (int i = 0; i < formula.getChildCount(); i++){
			Formula<String> child = formula.getChild(i);
			short childType = child.getType();
			if (!((childType==  Formula.CONJUNCTION) 
					|| (childType ==  Formula.LITERAL)
					|| (childType ==  Formula.CONSTANT))){
				return false;
			}
			// in the inner conjunctions each child has to be a literal or a constant
			for (int j = 0; j < child.getChildCount(); j++){
				short childChildType = child.getChild(j).getType();
				if (!((childChildType ==  Formula.LITERAL)
						|| (childChildType ==  Formula.CONSTANT))){
					return false; 
				}
			}
		}
		return true;
	}
	
	
	/**
	 * Tests if a formula is in CNF.
	 * @param formula - the formula to be tested
	 * @return true iff the formula is in CNF
	 */
	public boolean isInCNF(Formula<String> formula){
		short type = formula.getType();
		// a literal or a constant are in CNF, otherwise it has to be a conjunction of disjunctions or only one disjunction
		if (!((type ==  Formula.CONJUNCTION) 
				|| (type ==  Formula.DISJUNCTION)
				|| (type ==  Formula.LITERAL)
				|| (type ==  Formula.CONSTANT))){
			return false;
		}
		// if there is only one disjunction, all children have to be literal or constant
		if (type == Formula.DISJUNCTION){
			for (int j = 0; j < formula.getChildCount(); j++){				
				short childType = formula.getChild(j).getType();
				if (!((childType ==  Formula.LITERAL)
						|| (childType ==  Formula.CONSTANT))){
					return false; 
				}
			}
		}
		// in the conjunction each child has to be constant, literal, or disjunction
		for (int i = 0; i < formula.getChildCount(); i++){
			Formula<String> child = formula.getChild(i);
			short childType = child.getType();			
			if (!((childType==  Formula.DISJUNCTION) 
					|| (childType ==  Formula.LITERAL)
					|| (childType ==  Formula.CONSTANT))){
				return false;
			}
			// in the inner disjunctions each child has to be a literal or a constant
			for (int j = 0; j < child.getChildCount(); j++){				
				short childChildType = child.getChild(j).getType();
				if (!((childChildType ==  Formula.LITERAL)
						|| (childChildType ==  Formula.CONSTANT))){
					return false; 
				}
			}
		}
		return true;
	}
	
	
	/**
	 * Tests if two propositional logic formulas are equivalent.
	 * @param f - a formula
	 * @param g - a formula
	 * @return true iff f and g are equivalent
	 */
	public boolean areEquivalent(Formula<String> f, Formula<String> g){
		// we need all variables and the number of variables to test all configurations of logical values
		Set<String> variableSet = f.getPropositions();
		variableSet.addAll(g.getPropositions());
		int numberOfVariables = variableSet.size();
		String[] variables = new String[numberOfVariables];
		variables = variableSet.toArray(variables);
		
		log.printMessage(9, "areEquivalent: Set of variables" + variableSet + " Number of Variables: " + numberOfVariables + "\n");
		log.printMessage(9, "f = " + f.toString() + " g = " + g.toString() + "\n");
		HashMap<String, Boolean> logicalValues = new HashMap<String, Boolean>();
		// 2^b = 1 << b
		// because of BigEndian it works in this order: 000-100-010-110-001-101-011-111 
		BigInteger lineCounter = new BigInteger("0"); // counts tested lines of the truth-table
		for (int i = 0; i < (1 << numberOfVariables); i++){
			// for each line of the truth-table we put the configurations of logical values in a HashMap
			logicalValues.clear();
			for (int j = 0; j < numberOfVariables; j++){
				if (j < lineCounter.bitLength()){
					if (lineCounter.testBit(j)){
						logicalValues.put(variables[j], true);
					}else{
						logicalValues.put(variables[j], false);
					}
				}else{
					logicalValues.put(variables[j], false);
				}
			}
			log.printMessage(10, "areEquivalent: actual vector with logicalValues: " + logicalValues.values().toString() + "\n");
			boolean resultF = functionEvaluation(f, logicalValues);
			boolean resultG = functionEvaluation(g, logicalValues);
			log.printMessage(10, "areEquivalent: in line " + lineCounter + " resultF = " + resultF + ", resultG = " + resultG + "\n");
			// if there is one different result, the functions are not equivalent and we return false
			if (resultF != resultG){
				return false;
			}
			lineCounter = lineCounter.add(BigInteger.ONE);
		}		
		return true;
	}
	
	
	/**
	 * Tests if two propositional logic formulas are the negations of each other
	 * @param f - a formula
	 * @param g - a formula
	 * @return true iff f is the negation of g
	 */
	public boolean areNegationOfEachOther(Formula<String> f, Formula<String> g){
		// we need all variables and the number of variables to test all configurations of logical values
		Set<String> variableSet = f.getPropositions();
		variableSet.addAll(g.getPropositions());
		int numberOfVariables = variableSet.size();
		String[] variables = new String[numberOfVariables];
		variables = variableSet.toArray(variables);
		
		log.printMessage(9, "areNegationOfEachOther: Set of variables" + variableSet + " Number of Variables: " + numberOfVariables + "\n");
		log.printMessage(9, "f = " + f.toString() + " g = " + g.toString() + "\n");
		HashMap<String, Boolean> logicalValues = new HashMap<String, Boolean>();
		// 2^b = 1 << b
		// because of BigEndian it works in this order: 000-100-010-110-001-101-011-111 
		BigInteger lineCounter = new BigInteger("0"); // counts tested lines of the truth-table
		for (int i = 0; i < (1 << numberOfVariables); i++){
			// for each line of the truth-table we put the configurations of logical values in a HashMap
			logicalValues.clear();
			for (int j = 0; j < numberOfVariables; j++){
				if (j < lineCounter.bitLength()){
					if (lineCounter.testBit(j)){
						logicalValues.put(variables[j], true);
					}else{
						logicalValues.put(variables[j], false);
					}
				}else{
					logicalValues.put(variables[j], false);
				}
			}
			log.printMessage(10, "areNegationOfEachOther: actual vector with logicalValues: " + logicalValues.values().toString() + "\n");
			boolean resultF = functionEvaluation(f, logicalValues);
			boolean resultG = functionEvaluation(g, logicalValues);
			log.printMessage(10, "areNegationOfEachOther: in line " + lineCounter + " resultF = " + resultF + ", resultG = " + resultG + "\n");
			// if there is one equal result, the functions are not negations of each other and we return false
			if (resultF == resultG){
				return false;
			}
			lineCounter = lineCounter.add(BigInteger.ONE);
		}		
		return true;
	}
	
	
	/**
	 * Determines the truth-value of a propositional logic formula under a given assignment of logical-values for each variable.
	 * @param f - the formula to be evaluated
	 * @param logicalValues - the logical-values for each in variable in a HashMap
	 * @return the truth-value of the formula under the given logical-values, its
	 * 			true iff the assignment of logical-values fulfills the formula
	 */
	public boolean functionEvaluation(Formula<String> f, HashMap<String, Boolean> logicalValues ){	
		switch (f.getType()) {
	      case Formula.CONJUNCTION:
	    	  for (int i = 0; i < f.getChildCount(); i++){
	    		  // ¬(a∧b) = (¬a∨¬b)
	    		  if (f.isNegation()){
	    			  // a disjunction is true when at least one term is true, with all terms negated we return true if at least one term is false
	    			  if (!functionEvaluation(f.getChild(i), logicalValues)){
		    			  return true;
		    		  }
	    		  }else{// (a∧b) 
	    			  // a conjunction is true if all terms are true, we return false if at least one term is false
	    			  if (!functionEvaluation(f.getChild(i), logicalValues)){
		    			  return false;
		    		  }
	    		  }
	    	  }
	    	  if (f.isNegation()){
	    		  return false;
	    	  }else{
	    		  return true;
	    	  }
	      case Formula.DISJUNCTION:
	    	  for (int i = 0; i < f.getChildCount(); i++){
	    		  // ¬(a∨b) = (¬a∧¬b)
	    		  if (f.isNegation()){
	    			  // a conjunction is true if all terms are true, with all terms negated we return false if at least one term is true
	    			  if (functionEvaluation(f.getChild(i), logicalValues)){
		    			  return false;
		    		  }
	    		  }else{// (a∨b)
	    			// a disjunction is true when at least one term is true, we return true if at least one term is true
	    			  if(functionEvaluation(f.getChild(i), logicalValues)){
		    			  return true;
		    		  }
	    		  }
	    	  }
	    	  if (f.isNegation()){
	    		  return true;
	    	  }else{
	    		  return false;
	    	  }
	      case Formula.IMPLICATION:
	    	  // ¬(a⇒b) = (a∧¬b)
	    	  if (f.isNegation()){
	    		// in case of ¬(⊤⇒A) = (⊤∧¬A) the result is the functionAnalysis of ¬A
		    	  if (f.getChild(0).getType() == Formula.CONSTANT && !f.getChild(0).isNegation()){
		    		  return !functionEvaluation(f.getChild(1), logicalValues);	    		  
		    	  }else if (!functionEvaluation(f.getChild(0), logicalValues)){
		    		  return false;
		    	  }else if (functionEvaluation(f.getChild(1), logicalValues)){
		    		  return false;
		    	  }
		    	  return true;
	    	  }else{// (a⇒b) = (¬a∨b) 
	    		  // in case of (⊤⇒A) = (⊥∨A) the result is the functionAnalysis of A
	    		  if (f.getChild(0).getType() == Formula.CONSTANT && !f.getChild(0).isNegation()){
		    		  return functionEvaluation(f.getChild(1), logicalValues);	   
		    	  }else if (!functionEvaluation(f.getChild(0), logicalValues)){
		    		  return true;
		    	  }else if (functionEvaluation(f.getChild(1), logicalValues)){
		    		  return true;
		    	  }
		    	  return false; 
	    	  }
	      case Formula.LITERAL:
	    	  	if (f.isNegation()){
	    	  		return !logicalValues.get(f.getProposition());
	    	  	}else{
	    	  		return logicalValues.get(f.getProposition());
	    	  	}
	      case Formula.CONSTANT:
	    	  	if (f.isNegation()){
	    	  		return false;
	    	  	}else{
	    	  		return true;
	    	  	}	
	      default: throw new UnsupportedOperationException("Only propositional logic is supported.");
		}		
	}
	
	
	/**
	 * Tests if a formula contains the literals of another formula. Method works in both directions.
	 * @param f - a formula
	 * @param g - a formula
	 * @return -1 iff g contains all literals of f
	 * 			<br> 0 iff neither f contains all literals of g, nor g contains all literals of f
	 * 			<br> 1 iff f contains all literals of g, or f and g are both the same literal
	 */
	public int haveSameLiterals(Formula<String> f, Formula<String> g){
		if (f.getType() == Formula.LITERAL){
			// f and g are both literals
			if (g.getType() == Formula.LITERAL){
				// if they are the same literal, we say f contains g and return 1, otherwise neither f contains g nor g contains f and we return 0
				if (areSameLiterals(f, g)){
					return 1;
				}else{
					return 0;
				}
			// f is a literal but g is a formula, so we check if g contains	f
			}else{
				for (int i = 0; i < g.getChildCount(); i++){
					if (areSameLiterals(f, g.getChild(i))){
						// in this case g contains f
						return -1;
					}
				}// otherwise neither f contains g nor g contains f and we return 0
				return 0;
			}
		// f is'nt a literal	
		}else{
			if (g.getType() == Formula.LITERAL){
				// g is a literal but f is a formula, so we check if f contains	g							
				for (int i = 0; i < f.getChildCount(); i++){
					if (areSameLiterals(g, f.getChild(i))){
						// in this case f contains g
						return 1;
					}
				}// otherwise neither f contains g nor g contains f and we return 0
				return 0;
			// f and g are both formulas
			}else{
				// if f has  less or equal literals than g, we check if g contains all literals of f
				if(f.getChildCount() <= g.getChildCount()){
					for (int i = 0; i < f.getChildCount(); i++){
						int compareResult = haveSameLiterals(g, f.getChild(i));
						if (compareResult == 0){
							return 0;							
						}												
					}
					return -1;
				// g has less literals than f, so we check if f contains all literals of g
				}else{
					for (int i = 0; i < g.getChildCount(); i++){
						int compareResult = haveSameLiterals(f, g.getChild(i));
						if (compareResult == 0){
							return 0;
						}
					}
					return 1;
				}
			}	
		}
	}
	
	
	/**
	 * Tests if the first parameter is the same literal as the other.
	 * @param f - a formula of type LITERAL
	 * @param g - a formula of type LITERAL
	 * @return true iff f is the same literal as g
	 */	
	public boolean areSameLiterals(Formula<String> f, Formula<String> g){
		if (f.getType() == Formula.LITERAL && g.getType() == Formula.LITERAL){	
			return ((f.isNegation() == g.isNegation()) && f.getProposition().equals(g.getProposition()));
		}
		else{
			throw new IllegalArgumentException("Only literals are supported.");
		}
	}	
	
	
	/**
	 * Tests if the first parameter is the negation of the second parameter.
	 * @param f - a formula of type LITERAL
	 * @param g - a formula of type LITERAL
	 * @return true iff f is the negation of g
	 */	
	public boolean areContraryLiterals(Formula<String> f, Formula<String> g){
		if (f.getType() == Formula.LITERAL && g.getType() == Formula.LITERAL){
			return ((f.isNegation() && !g.isNegation()) || (!f.isNegation() && g.isNegation())) &&
		              f.getProposition().equals(g.getProposition());
		}
		else{
			throw new IllegalArgumentException("Only literals are supported.");
		}	
	}	
	
	
//------------------------------------------------------------------------------
// helper-methods	
	
	/**
	 * Retrieves the last token of the text.
	 * @param text - text from which you want the last token
	 * @return the last token of the text
	 */
 	private String lastToken(String text){
		String temp = text;
		StringTokenizer tokenizer = new StringTokenizer(temp);
		while(tokenizer.hasMoreTokens()){
			temp = tokenizer.nextToken();
		}			
		return temp;
	}
 	
	 	
	/**
	 * Parses a term from a line of the truth-table.
	 * @param text - text to be parsed
	 * @return term which represents the line of the truth-table (in the Quine-McCluskey-Term-Format)
	 */
 	private Term parseTerm(String text){
		StringTokenizer tokenizer = new StringTokenizer(text);
		ArrayList<Byte> t = new ArrayList<Byte>();
		while(tokenizer.hasMoreTokens()){
			String temp = tokenizer.nextToken();
			if (temp.equals("0")) {
                t.add((byte)0);
            } else if (temp.equals("1")) {
                t.add((byte)1);
            }else{
            	throw new IllegalArgumentException("Invalid truth-table - only entrys '0' and '1' are allowed.");
			}
        }
        if (t.size() > 0) {
            byte[] resultBytes = new byte[t.size()];
            for(int i = 0; i < t.size(); i++) {
                resultBytes[i] = (byte)t.get(i);
            }
            return new Term(resultBytes);
        }else {
            return null;
        }
	}
 	
 	
 	/**
 	 * Places literals in an existing formula inclusive setting the right negation
 	 * @param factory - a formula factory, which has opened the existing formula context
 	 * @param f - a literal
 	 * @param g - a literal or null, when only one literal should be placed
 	 */
 	private void placeLiterals(FormulaFactory<String> factory, Formula<String> f, Formula<String> g){
 		if (f.isNegation()){ // setting the first literal
			factory.negation();
			factory.literal(f.getProposition());
		}else{
			factory.literal(f.getProposition());
		}
		if (g != null){	 // setting the second literal if it is'nt null
 			if (g.isNegation()){
				factory.negation();
				factory.literal(g.getProposition());
			}else{
				factory.literal(g.getProposition());
			}
		}
 	}
 	
 	
	/**
	 * Counts how much Zeros and Ones are in the term.	
	 * @param array - byte-Array to count in
	 * @return the number of 0 and 1 in the array
	 */
 	private int countZerosAndOnesInArray(byte[] array){
 		int count = 0;
 		for(int i = 0; i < array.length; i++){
 			if (array[i] == 1 || array[i] == 0){
 				count++;
 			}
 		}
 		return count;
 	}


 	/**
 	 * Counts the number of negative literals in a given formula.
 	 * @param formula - a formula
 	 * @return the number of negative literals in the given formula
 	 */
 	public int getNumberOfNegativeLiterals(Formula<String> formula){
 		int count = 0;
 		if(formula.getType() == Formula.LITERAL && formula.isNegation()){
 			return 1;
 		}
 		for (int i = 0; i < formula.getChildCount(); i++){
 			count = count + getNumberOfNegativeLiterals(formula.getChild(i));
 		}
 		return count;
 	}

 	
 	/**
 	 * Counts the number of implications in a given formula.
 	 * @param formula - a formula
 	 * @return the number of implications in the given formula
 	 */
 	public int getNumberOfImplications(Formula<String> formula){
 		int count = 0;
 		if(formula.getType() == Formula.IMPLICATION){
 			return 1;
 		}
 		for (int i = 0; i < formula.getChildCount(); i++){
 			count = count + getNumberOfImplications(formula.getChild(i));
 		}
 		return count;
 	}
 	
 
 	/**
 	 * Counts the number of clauses in a given formula.
 	 * @param formula - a formula
 	 * @return the number of clauses in the given formula
 	 */
 	public int getNumberOfClauses(Formula<String> formula){
 		if(formula.getType() == Formula.LITERAL ||formula.getType() == Formula.CONSTANT){
 			return 1;
 		} 	
 		return formula.getChildCount();
 	}
 	
}