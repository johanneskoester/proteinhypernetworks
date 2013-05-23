/* Copyright (c) 2010, Johannes Köster <johannes.koester@tu-dortmund.de>
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Formatter;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import logicProteinHypernetwork.LogicProteinHypernetwork;
import logicProteinHypernetwork.analysis.complexes.Complex;
import logicProteinHypernetwork.analysis.functionalSimilarity.FunctionalSimilarityOutputStream;
import logicProteinHypernetwork.analysis.perturbationEffects.PerturbationEffect;
import logicProteinHypernetwork.analysis.pis.PIS;
import modalLogic.formula.Formula;
import modalLogic.formula.io.InvalidFormulaException;
import modalLogic.formula.io.FormulaWriter;
import modalLogic.formula.io.MathMLWriter;
import proteinHypernetwork.NetworkEntity;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.decoder.HypernetworkMLDecoder;
import proteinHypernetwork.exceptions.UnknownEntityException;
import reconstructionOfInteractionDependencies.Reconstructor;
import reconstructionOfInteractionDependencies.TruthTablePrediction;

/**
*
* @author Johannes Köster <johannes.koester@tu-dortmund.de>
*/
public class Controller {
	private static Controller instance = new Controller();

	private int threads = 1;
	private ProteinHypernetwork hypernetwork;
	private LogicProteinHypernetwork logicHypernetwork;

	public static Controller getInstance() {
		return instance;
	}

	public int getThreads() {
		return threads;
	}

	public void setThreads(int threads) {
		this.threads = threads;
	}

	public void loadHypernetwork(File file) throws FileNotFoundException,
			XMLStreamException, InvalidFormulaException {
		HypernetworkMLDecoder decoder = new HypernetworkMLDecoder();
		hypernetwork = new ProteinHypernetwork();
		decoder.decode(file, hypernetwork);
		logicHypernetwork = new LogicProteinHypernetwork(hypernetwork, threads);
	}

	public void predictComplexes(BufferedWriter writer) throws IOException {
		try {
			logicHypernetwork.predictComplexes();
			for (Complex c : logicHypernetwork.getComplexes()) {
				Collections.sort(c);
				writer.append(c.toString());
				writer.newLine();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		writer.close();
	}
	
	public void predictPIS(BufferedWriter writer) throws IOException {
		logicHypernetwork.predictPIS();
		for (PIS pis : logicHypernetwork.getPIS()) {
			writer.append(pis.toString());
			writer.newLine();
		}
		writer.close();
	}
	
	public void predictFunctionalSimilarities(BufferedWriter writer) throws IOException {
		FunctionalSimilarityOutputStream os = new FunctionalSimilarityOutputStream(writer);
		logicHypernetwork.predictFunctionalSimilarities(os);
		writer.close();
	}
	
	public void predictPerturbationEffects(Collection<String> perturbationIds, BufferedWriter writer) throws IOException, UnknownEntityException {
		logicHypernetwork.predictPerturbation(hypernetwork.getNetworkEntities(perturbationIds));
		PerturbationEffect pe = logicHypernetwork.getPerturbationEffect();
		writer.append("entity\tpossible\tmns\tactivators\tcompetitors");
		writer.newLine();
		for(NetworkEntity e : hypernetwork.getNetworkEntities()) {
			Formatter line = new Formatter(new StringBuilder());
			line.format("%1$s\t%2$d\t%3$d\t%4$f\t%5$f", e, pe.getPossibility().get(e) ? 1 : 0, pe.getMNSCount().get(e), pe.getDependencies().get(e), pe.getCompetitors().get(e));
			
			writer.append(line.toString());
			writer.newLine();
		}
		writer.close();
	}
	
	public void predictTruthTables(String network, String complexes, File destination, 
			int minComplexes, int minObservations, boolean learnThreshold, boolean filter) {
		if(!destination.exists()) {
			destination.mkdir();
		} 
		TruthTablePrediction ttp = new TruthTablePrediction(complexes, network, 10);
		// ttp.printProteins();
		ttp.predictTruthTablesWith2InteractionsFor3Proteins(destination.getPath(), minComplexes, minObservations, learnThreshold, filter);
		ttp.predictTruthTablesWith3InteractionsFor3Proteins(destination.getPath(), minComplexes, minObservations, learnThreshold, filter);
	}
	
	public void reconstructInteractionDependencies(String truthTable, BufferedWriter writer, boolean mathML) throws IOException, XMLStreamException{
		Reconstructor reconstructor = new Reconstructor(-1);
		Formula<String> formula = reconstructor.zeroLinesMethod(truthTable);
		if (mathML) {
			XMLOutputFactory output = XMLOutputFactory.newInstance();
			XMLStreamWriter xmlWriter = output.createXMLStreamWriter(writer);
			FormulaWriter<String > fwriter = new MathMLWriter<String>(xmlWriter);
			try {
				fwriter.write(formula);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			xmlWriter.close();
		}else{
			writer.write(formula.toString());
			writer.newLine();
		}		
		writer.close();
	}
}
