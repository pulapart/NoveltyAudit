package com.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class readFile {
	
public static String[] readCol(String inputfile, int col) throws Exception {
	
	 File f = new File(inputfile);
		
	String[] results = new String[10000] ;
	 if (f.exists() && !f.isDirectory()) {

		FileInputStream fis;
		fis = new FileInputStream(f);
		BufferedReader rd = new BufferedReader (new InputStreamReader(fis));
		String line = ""; String[] fields = null;
		int count = 0;
		
		while ((line = rd.readLine()) != null ) {
	 		fields = null;
	 		fields = line.split("\t");
	 		results[count] = fields[col];
	 		count++;
	 	}	 
	 	
		rd.close();
	 	return results;
	 } 
	 return null;
	}	
}	
