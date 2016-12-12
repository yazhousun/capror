package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class KmerCounterUniqueToSpeciesFinderEntry {

	public static void main(String[] args) {
		if (args == null || args.length < 4) {
			PrintUsage.print();
			System.exit(0);
		}

		String kmerListFileAll = args[0];
		String kmerListFileGroup = args[1];
		String outputDirectory = args[2];
		int kmerLength = Integer.parseInt(args[3]);

		File tempFile = new File(kmerListFileAll);
		if (!(tempFile.exists() && tempFile.isFile())) {
			PrintUsage.print();
			System.exit(0);
		}
		
		tempFile = new File(kmerListFileGroup);
		if (!(tempFile.exists() && tempFile.isFile())) {
			PrintUsage.print();
			System.exit(0);
		}

		String outputFileNamePrefix = null;
		outputFileNamePrefix = tempFile.getName();
		outputFileNamePrefix = removeExtension(outputFileNamePrefix);

		tempFile = new File(outputDirectory);
		if (!(tempFile.exists() && tempFile.isDirectory())) {
			PrintUsage.print();
			System.exit(0);
		}

		Scanner inputAllKmers = getInput(kmerListFileAll);
		Scanner inputGroupKmers = getInput(kmerListFileGroup);

		String outputFileName = null;
		tempFile = new File(outputDirectory + File.separator + outputFileNamePrefix + ".speciesspecific.txt");
		if (!(tempFile.exists())) {
			outputFileName = tempFile.toString();
		} else {
			int fileCount = 1;
			while (tempFile.exists()) {
				tempFile = new File(outputDirectory + File.separator + outputFileNamePrefix + ".speciesspecific" + "."
						+ fileCount + ".txt");
				if (tempFile.exists()) {
					fileCount++;
				}
			}
			outputFileName = tempFile.toString();
		}

		String temp1 = null;
		String temp2 = null;
		int intValueOfInputAll;
		int intValueOfInputGroup;
		double percentDifference = 0;

		System.out.println("Currently searching for species-specific kmers of length " + kmerLength);
			
		try {
			PrintWriter output = new PrintWriter(outputFileName);
			output.println("Kmer ID for length of " + kmerLength + "\tPresent in x total genomes\tPresent in y genomes from group " + kmerListFileGroup);

			int lineIndexNumber = 0;
			while (inputAllKmers.hasNext() && inputGroupKmers.hasNext()) {
				temp1 = inputAllKmers.nextLine().trim();
				temp2 = inputGroupKmers.nextLine().trim();
				intValueOfInputAll = Integer.parseInt(temp1);
				intValueOfInputGroup = Integer.parseInt(temp2);
				
				if (intValueOfInputGroup > 0) {
					percentDifference = (double) intValueOfInputGroup/ (double) intValueOfInputAll;
					if (percentDifference >= 0.9) {
						output.println(lineIndexNumber + "\t" + intValueOfInputAll + "\t" + intValueOfInputGroup);
					}

					if (lineIndexNumber % 1000000 == 0) {
						System.out.println(lineIndexNumber + " lines analyzed.");
					}
					lineIndexNumber++;
				}

			}
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("Species specific kmer finder is completed and output file has been created.");

	}

	public static Scanner getInput(String inputFileName) {
		Scanner input = null;

		try {
			input = new Scanner(new File(inputFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		return (input);
	}

	public static String removeExtension(String s) {

		String separator = System.getProperty("file.separator");
		String filename;

		// Remove the path up to the filename.
		int lastSeparatorIndex = s.lastIndexOf(separator);
		if (lastSeparatorIndex == -1) {
			filename = s;
		} else {
			filename = s.substring(lastSeparatorIndex + 1);
		}

		// Remove the extension.
		int extensionIndex = filename.lastIndexOf(".");
		if (extensionIndex == -1)
			return filename;

		return filename.substring(0, extensionIndex);
	}

}
