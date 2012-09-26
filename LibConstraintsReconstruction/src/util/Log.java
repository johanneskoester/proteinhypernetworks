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

/**
 * A class for logging debug-infos and printing only the infos that are logged with a lower equal loglevel. 
 * @author Bianca Patro
 *
 */
public class Log {

	private int logLevel;	
	private FileWriter fstream; 
	private BufferedWriter writer;
	boolean saveInFile;
	
	/**
	 * Constructor which initially sets the logLevel to a default-value and prints the output on std-out.
	 */
	public Log(){
		logLevel = 3;
		saveInFile = false;
	}
	
	/**
	 * Constructor which initially sets the logLevel according to the parameter and prints the output on std-out.
	 * @param logLevel - states the level of the Log
	 */
	public Log(int logLevel){
		this.logLevel = logLevel;
		saveInFile = false;
	}
	
	/**
	 * Constructor which initially sets the logLevel according to the parameter and prints the output in a specified file.
	 * @param logLevel - states the level of the Log
	 * @param path - path  path to a file in which the output is written
	 */
	public Log(int logLevel, String path){
		this.logLevel = logLevel;
		saveInFile = true;
		try {
			this.fstream = new FileWriter(path);
			this.writer = new BufferedWriter(fstream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Prints a message, when the level is lower than the logLevel of Log.
	 * @param level - determines when the message is printed
	 * @param msg - the message to be printed (its a print, not print-line)
	 */
	public void printMessage(int level, String msg){
		if (logLevel >= level){
			if (saveInFile){
				try {			
					writer.write(msg);
					writer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				System.out.print(msg);
			}
		}		
	}
	
	
}
