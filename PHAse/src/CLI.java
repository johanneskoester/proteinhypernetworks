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

import javax.xml.stream.XMLStreamException;

import logicProteinHypernetwork.analysis.complexes.Complex;
import logicProteinHypernetwork.analysis.pis.PIS;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * 
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class CLI {
	public static String usage = "PHAse <input> <output>";

	public static void main(String[] args) {
		CommandLineParser parser = new PosixParser();
		Options options = new Options();
		options.addOption("h", "help", false, "Print this message.");
		options.addOption("t", "threads", false,
				"Number of threads to use (1 per default).");
		options.addOption("c", "complexes", false, "Predict protein complexes.");
		options.addOption("p", "pis", false, "Predict perturbation impact.");
		// automatically generate the help statement
		HelpFormatter help = new HelpFormatter();

		String input = null;
		String output = null;
		CommandLine line = null;
		try {
			line = parser.parse(options, args);
			if (line.hasOption("help")) {
				help.printHelp(usage, options);
				return;
			}

			String[] positional = line.getArgs();
			input = positional[0];
			output = positional[1];
			System.out.println(input);
		} catch (ParseException e) {
			help.printHelp(usage, options);
		} catch (ArrayIndexOutOfBoundsException e) {
			help.printHelp(usage, options);
		}
		if (input == null || output == null || line == null) {
			return;
		}

		File infile = new File(input);
		BufferedWriter outstream = null;
		if (output.equals("-")) {
			outstream = new BufferedWriter(new OutputStreamWriter(System.out));
		}

		Controller.getInstance().setThreads(
				Integer.valueOf(line.getOptionValue("threads", "1")));

		try {
			Controller.getInstance().loadHypernetwork(infile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (line.hasOption("complexes")) {
			Controller.getInstance().predictComplexes();
			try {
				for (Complex c : Controller.getInstance().getComplexes()) {
					outstream.append(c.toString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (line.hasOption("pis")) {
			Controller.getInstance().predictPIS();
			try {
				for(PIS pis : Controller.getInstance().getPIS()) {
					outstream.append(pis.toString());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
