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
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class CLI {
	public static String usage = "PHAse <input> <output>";

	@Parameter(description = "Output")
	private List<String> parameters = new ArrayList<String>();

	@Parameter(names = { "-h", "--help" }, description = "Print this message.")
	private boolean help;

	@Parameter(names = { "-c", "--complexes" }, description = "Predict protein complexes.")
	private boolean predictComplexes;

	@Parameter(names = { "-p", "--pis" }, description = "Predict perturbation impact.")
	private boolean predictPIS;

	@Parameter(names = { "-s", "--similarity" }, description = "Predict functional similarities.")
	private boolean predictSimilarity;

	@Parameter(names = { "-c", "--truthtables" }, description = "Predict interaction truth tables.")
	private boolean predictTruthTables;

	@Parameter(names = { "-t", "--threads" }, description = "Number of threads to use (1 per default).")
	private int threads = 1;

	@Parameter(names = { "-lh", "--load-hypernetwork" }, description = "Load a protein hypernetwork as a HypernetworkML (.hml) file.")
	private String hypernetwork;

	@Parameter(names = { "-ln", "--load-network" }, description = "Load a protein network as a tab separated file (1st column: protein name, 2nd column: interacting protein name).")
	private String network;

	@Parameter(names = { "-lc", "--load-complexes" }, description = "Load protein complexes (1st column: complex id, 2nd column: protein name).")
	private String complexes;

	public static void main(String[] args) {
		CLI cli = new CLI();
		JCommander jc = new JCommander(cli, args);

		if (cli.help) {
			jc.usage();
			return;
		}
		if (!(cli.predictComplexes || cli.predictTruthTables || cli.predictPIS || cli.predictSimilarity)) {
			jc.usage();
			return;
		}
		if ((cli.predictComplexes || cli.predictPIS || cli.predictSimilarity)
				&& cli.hypernetwork == null) {
			jc.usage();
			return;
		}
		if (cli.predictTruthTables
				&& (cli.network == null || cli.complexes == null)) {
			jc.usage();
			return;
		}

		String output = cli.parameters.get(0);

		BufferedWriter outstream = null;
		if (output.equals("-")) {
			outstream = new BufferedWriter(new OutputStreamWriter(System.out));
		}

		Controller.getInstance().setThreads(cli.threads);

		if (cli.predictComplexes || cli.predictPIS || cli.predictSimilarity) {
			File inputFile = new File(cli.hypernetwork);
			try {
				Controller.getInstance().loadHypernetwork(inputFile);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XMLStreamException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				if (cli.predictComplexes) {
					Controller.getInstance().predictComplexes(outstream);
				} else if (cli.predictPIS) {
					Controller.getInstance().predictPIS(outstream);
				} else if (cli.predictSimilarity) {
					Controller.getInstance().predictFunctionalSimilarities(
							outstream);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if (cli.predictTruthTables) {
			if(output.equals("-")) {
				jc.usage();
				return;
			}
			Controller.getInstance().predictTruthTables(cli.network, cli.complexes, output);
		}
	}
}
