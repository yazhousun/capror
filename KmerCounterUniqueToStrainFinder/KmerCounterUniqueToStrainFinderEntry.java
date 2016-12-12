package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class KmerCounterUniqueToStrainFinderEntry {

	public static void main(String[] args) {
		if (args == null || args.length < 3) {
			PrintUsage.print();
			System.exit(0);
		}

		String kmerListFile = args[0];
		String outputDirectory = args[1];
		int kmerLength = Integer.parseInt(args[2]);

		File tempFile = new File(kmerListFile);
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

		Scanner input = getInput(kmerListFile);

		String outputFileName = null;
		tempFile = new File(outputDirectory + File.separator + outputFileNamePrefix + ".strainspecific.txt");
		if (!(tempFile.exists())) {
			outputFileName = tempFile.toString();
		} else {
			int fileCount = 1;
			while (tempFile.exists()) {
				tempFile = new File(outputDirectory + File.separator + outputFileNamePrefix + ".strainspecific" + "."
						+ fileCount + ".txt");
				if (tempFile.exists()) {
					fileCount++;
				}
			}
			outputFileName = tempFile.toString();
		}

		String temp = null;
		int intValueOfInput;

		System.out.println("Currently searching for strain-specific kmers of length " + kmerLength);

		try {
			PrintWriter output = new PrintWriter(outputFileName);
			output.println("Kmer ID for length of " + kmerLength + "\tPresent in total x genomes");

			int lineIndexNumber = 0;
			while (input.hasNext()) {
				temp = input.nextLine().trim();
				intValueOfInput = Integer.parseInt(temp);

				if (intValueOfInput > 0) {
					if (intValueOfInput < 5) {
						output.println(lineIndexNumber + "\t" + intValueOfInput);
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

		System.out.println("Strain specific kmer finder is completed and output file has been created.");

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
