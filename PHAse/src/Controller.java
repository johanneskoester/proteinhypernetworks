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
import java.util.Collections;

import javax.xml.stream.XMLStreamException;

import logicProteinHypernetwork.LogicProteinHypernetwork;
import logicProteinHypernetwork.analysis.complexes.Complex;
import logicProteinHypernetwork.analysis.functionalSimilarity.FunctionalSimilarityOutputStream;
import logicProteinHypernetwork.analysis.pis.PIS;
import proteinHypernetwork.ProteinHypernetwork;
import proteinHypernetwork.decoder.HypernetworkMLDecoder;

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
			XMLStreamException {
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
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
	}
	
	public void predictPIS(BufferedWriter writer) throws IOException {
		logicHypernetwork.predictPIS();
		for (PIS pis : logicHypernetwork.getPIS()) {
			writer.append(pis.toString());
		}
	}
	
	public void predictFunctionalSimilarities(BufferedWriter writer) throws IOException {
		FunctionalSimilarityOutputStream os = new FunctionalSimilarityOutputStream(writer);
		logicHypernetwork.predictFunctionalSimilarities(os);
	}
}
