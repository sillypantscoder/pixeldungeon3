package com.sillypantscoder.pixeldungeon3.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class contains various random utilities used by the rest of the code.
 */
public class Utils {
	public static String readFile(File file) {
		try {
			Scanner fileReader = new Scanner(file);
			String allData = "";
			while (fileReader.hasNextLine()) {
				String data = fileReader.nextLine();
				allData += data + "\n";
			}
			fileReader.close();
			// Parse the data
			return allData;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "Error finding file...";
		}
	}
	public static String getResource(String filename) {
		return readFile(AssetLoader.getResource(filename));
	}
	public static String decodeURIComponent(String in) {
		try {
			return URLDecoder.decode(in, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return in;
		}
	}
	public static double map(double x, double fromStart, double fromEnd, double toStart, double toEnd) {
		return (x - fromStart) / (fromEnd - fromStart) * (toEnd - toStart) + toStart;
	}
	public static double[] normalize(double x, double y) {
		double dist = Math.sqrt((x * x) + (y * y));
		double newX = x / dist;
		double newY = y / dist;
		return new double[] { newX, newY };
	}
	public static<T> ArrayList<T> arrayToArrayList(T[] items) {
		ArrayList<T> arr = new ArrayList<T>();
		for (T item : items) {
			arr.add(item);
		}
		return arr;
	}
}
