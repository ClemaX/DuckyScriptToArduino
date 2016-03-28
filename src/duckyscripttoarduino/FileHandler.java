/*
* Name:            DuckyScriptToArduino
* Author:          Marcus Orwen
* Copyright:       Copyright (c) 2013, Marcus Orwen
* License:         GNU GPL v3
* License file:    LICENSE.md
* You are NOT authorized to change or remove this comment box.
*/

package duckyscripttoarduino;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 *
 * @author Marcus & ClemaX
 */
public class FileHandler {

	private static Set<String> propertyNames;
	private static Properties prop = new Properties();

	/**
	 * load
	 * 
	 * @param file
	 *            input filename with extensions
	 * @return String array with line contents
	 */
	public String[] loadScript(String file) {
		List<String> lines = new ArrayList<>();// create an array list to store
												// the lines we get from
												// BufferReader
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {// read lines untill there
													// is none left
				lines.add(line);
			}
			br.close();
		} catch (Exception e) {
			System.out.println("Failed to load file!\n Error: " + e.getMessage());
		}
		String[] ret = new String[lines.size()];
		lines.toArray(ret);
		return ret;
	}

	/**
	 * save
	 * 
	 * @param data
	 *            The data that should be saved to a file
	 * @param dest
	 *            The destination filename that is going to be saved.
	 */
	public String save(String data, String dest) {
		String ret;
		// try and write ino file
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(dest), true));
			bw.write(data);
			bw.newLine();
			bw.close();
			ret = "Save complete!";
		} catch (Exception e) {
			ret = "Failed to write ino file!\n Error: " + e.getMessage();
		}
		return ret;
	}

	public static void readLocalization(File localization) {
		InputStream input = null;
		
		try {
			input = new FileInputStream(localization);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			prop.load(input);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error in readLocalization: " + e +"\n" + input + localization);
		}
		System.out.println("Property File Loaded Succesfully");
		propertyNames = prop.stringPropertyNames();

	}

	public Set<String> getPropertyNames() {
		return propertyNames;
	}

	public Properties getProperties() {
		return prop;
	}
}
