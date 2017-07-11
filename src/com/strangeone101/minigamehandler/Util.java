package com.strangeone101.minigamehandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.Files;

public class Util {
	
	public static boolean isInteger(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
	/**
	 * Sets a value in an ini file
	 * @param file The ini file
	 * @param key The key
	 * @param value The value you want to set it to
	 * @return True if the value was set successfully 
	 */
	public static boolean updateINIFile(File file, String key, String value) {
		try {
			List<String> lines = Files.readLines(file, Charset.defaultCharset());
			List<String> newLines = new ArrayList<String>();
			boolean written = false;
			for (String line : lines) {
				if (line.startsWith("#") || !line.contains("=")) {
					newLines.add(line);
					continue;
				}
				String lineKey = line.split("=", 2)[0];
				
				if (lineKey.equalsIgnoreCase(key)) {
					newLines.add(key + "=" + value);
					written = true;
				}
				else newLines.add(line);
			}
			
			if (!written) newLines.add(key + "=" + value);
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			for (String s : newLines) {
				writer.write(s);
				writer.newLine();
			}
			
			writer.close();
			
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/**
	 * Read an INI file and store all the properties in a map.
	 * @param file The ini file
	 * @return A map filled with the ini properties
	 */
	public static Map<String, String> readINI(File file) {
		Map<String, String> map = new HashMap<String, String>();
		
		try {
			List<String> lines = Files.readLines(file, Charset.defaultCharset());
			for (String line : lines) {
				if (line.startsWith("#") || line.contains("=")) {
					continue;
				}
				String lineKey = line.split("=", 2)[0];
				String lineValue = line.split("=", 2)[1];
				
				map.put(lineKey, lineValue);
			}
			
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
