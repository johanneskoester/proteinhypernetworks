/* Copyright (c) 2012, Bianca Patro
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see "license.txt"
 * for a description.
 */

package util;

public class PairDoubleString {

	private double d;
	private String s;
	
	
	public PairDoubleString(double d, String s){
		this.d = d;
		this.s = s;
	}
	
	public double getDouble() {
		return d;
	}
	
	public String getString() {
		return s;
	}
	
	@Override
	public String toString(){
		return "(" + d + ", " + s + ")"; 
	}
	
	
}
