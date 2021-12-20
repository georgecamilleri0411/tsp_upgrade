import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.System.gc;
import java.util.Scanner;

public class FileReader {

	// Reads the file passed as argument and stores it in an array of int with 3 dimensions
	public static void readFile(String filePath) {
		String nextLine = "";

		try {
			// Clear ArrayLists Utilities.cities and Utilities.distances, and array Utilities.permutations
			Utilities.cities.clear();
			Utilities.distances.clear();
			Utilities.distancesHash.clear();
			Utilities.permutations = null;
			gc();

			File textFile = new File(filePath);
			if (textFile.exists()) {
				Scanner fileReader = new Scanner(textFile);
				boolean dupl = false;
				while (fileReader.hasNextLine()) {
					nextLine = fileReader.nextLine().replaceAll("\t", " ").trim();
					if (String.valueOf(nextLine) != null) {
						int[] lineData = parseLine(nextLine);

						// Check for duplicate cities
						for (City c : Utilities.cities) {
							dupl = false;
							if ((c.getX() == lineData[1]) && (c.getY() == lineData[2])) {
								dupl = true;
								break;
							}
						}
						if (!dupl) {
							Utilities.cities.add(new City(lineData[0], lineData[1], lineData[2]));
						}
					}
				}
				fileReader.close();
			} else {
				System.out.println ("FileReader.readFile - File not found (" + textFile.getAbsolutePath() + ")");
			}

			// Set the distance from all cities to all other cities
			Utilities.setDistances();

		} catch (FileNotFoundException e) {
			System.out.println("FileReader.readFile - An error has occurred - " + e.getMessage());
			System.out.println("Line data: " + nextLine);
			e.printStackTrace();
		} finally {
			gc();
		}
	}

	/*
	 Parses the data line read and returns an array of Integers
	 */
	private static int[] parseLine(String dataLine) {
		try {
			// Replace multiple spaces by a single space using Regex
			dataLine = dataLine.replaceAll("^ +| +$|( )+", "$1");

			// Splits the string into a String array, then loops through it to populate the Integer array
			String[] line = dataLine.split(" ");
			int[] output = new int[line.length];

			for (int i = 0; i < line.length; i++) {
				if (isNumeric(line[i])) {
					output[i] = Integer.parseInt(line[i]);
				} else {
					output[i] = Integer.parseInt(null);
				}
			}

			return output;
		} catch (Exception e) {
			System.out.println("FileReader.parseLine - An error has occurred - " + e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			gc();
		}
	}

	/*
	Returns True if the value passed is numeric
	 */
	public static boolean isNumeric(String input) {

		if (input == null || input.trim().length() == 0) {
			return false;
		}
		try {
			int i = Integer.parseInt(input);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
}
