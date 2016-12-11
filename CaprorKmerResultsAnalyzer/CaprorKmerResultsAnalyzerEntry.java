package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class CaprorKmerResultsAnalyzerEntry {

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

		int lessThan10Count = 0;
		int lessThan5Count = 0;
		int lessThan3Count = 0;
		int lessThan2Count = 0;
		int greaterThan1kCount = 0;
		int greaterThan2kCount = 0;
		int greaterThan3kCount = 0;
		int greaterThan4kCount = 0;

		Scanner input = getInput(kmerListFile);

		String temp = null;
		int intValueOfIndex;
		int intValueOfInput;

		System.out.println(
				"Currently generating analysis results of kmers with length " + kmerLength + " from " + kmerListFile);
		
		try {
						
			PrintWriter summaryOutput = new PrintWriter(getOutputFileName(outputDirectory + File.separator + outputFileNamePrefix, ".summary"));
			PrintWriter L10 = new PrintWriter(getOutputFileName(outputDirectory + File.separator + outputFileNamePrefix, ".L10"));
			PrintWriter L5 = new PrintWriter(getOutputFileName(outputDirectory + File.separator + outputFileNamePrefix, ".L5"));
			PrintWriter L3 = new PrintWriter(getOutputFileName(outputDirectory + File.separator + outputFileNamePrefix, ".L3"));
			PrintWriter L2 = new PrintWriter(getOutputFileName(outputDirectory + File.separator + outputFileNamePrefix, ".L2"));
			PrintWriter G1k = new PrintWriter(getOutputFileName(outputDirectory + File.separator + outputFileNamePrefix, ".G1k"));
			PrintWriter G2k = new PrintWriter(getOutputFileName(outputDirectory + File.separator + outputFileNamePrefix, ".G2k"));
			PrintWriter G3k = new PrintWriter(getOutputFileName(outputDirectory + File.separator + outputFileNamePrefix, ".G3k"));
			PrintWriter G4k = new PrintWriter(getOutputFileName(outputDirectory + File.separator + outputFileNamePrefix, ".G4k"));
			
			int linesAnalyzed = 0;
			while (input.hasNext()) {
				temp = input.nextLine().trim();
				intValueOfIndex = linesAnalyzed;
				intValueOfInput = Integer.parseInt(temp);

				if (intValueOfInput > 0) {
					if (intValueOfInput < 10) {
						lessThan10Count++;
						L10.println(intValueOfIndex + "\t" + intValueOfInput);
					}
					if (intValueOfInput < 5) {
						lessThan5Count++;
						L5.println(intValueOfIndex + "\t" + intValueOfInput);
					}
					if (intValueOfInput < 3) {
						lessThan3Count++;
						L3.println(intValueOfIndex + "\t" + intValueOfInput);
					}
					if (intValueOfInput < 2) {
						lessThan2Count++;
						L2.println(intValueOfIndex + "\t" + intValueOfInput);
					}
					if (intValueOfInput > 1000) {
						greaterThan1kCount++;
						G1k.println(intValueOfIndex + "\t" + intValueOfInput);
					}
					if (intValueOfInput > 2000) {
						greaterThan2kCount++;
						G2k.println(intValueOfIndex + "\t" + intValueOfInput);
					}
					if (intValueOfInput > 3000) {
						greaterThan3kCount++;
						G3k.println(intValueOfIndex + "\t" + intValueOfInput);
					}
					if (intValueOfInput > 4000) {
						greaterThan4kCount++;
						G4k.println(intValueOfIndex + "\t" + intValueOfInput);
					}

					if (linesAnalyzed % 500000 == 0) {
						System.out.println(linesAnalyzed + " lines analyzed.");
					}
				}
				linesAnalyzed++;
			}

			summaryOutput.println(
					"These are the results for " + kmerListFile + "\nKmer length is set at: " + kmerLength + "\n");
			summaryOutput
					.println("The number of kmers that appear in greater than 4,000 genomes is: " + greaterThan4kCount);
			summaryOutput
					.println("The number of kmers that appear in greater than 3,000 genomes is: " + greaterThan3kCount);
			summaryOutput
					.println("The number of kmers that appear in greater than 2,000 genomes is: " + greaterThan2kCount);
			summaryOutput
					.println("The number of kmers that appear in greater than 1,000 genomes is: " + greaterThan1kCount);
			summaryOutput.println("The number of kmers that appear in less than 10 genomes is: " + lessThan10Count);
			summaryOutput.println("The number of kmers that appear in less than 5 genomes is: " + lessThan5Count);
			summaryOutput.println("The number of kmers that appear in less than 3 genomes is: " + lessThan3Count);
			summaryOutput.println("The number of kmers that appear in less than 2 genomes is: " + lessThan2Count);

			summaryOutput.close();
			L10.close();
			L5.close();
			L3.close();
			L2.close();
			G1k.close();
			G2k.close();
			G3k.close();
			G4k.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("Analysis completed and output files have been created.");

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
	
	public static String getOutputFileName(String filenamePrefix, String outputFileSuffix){
		
		String outputFileName = null;
		File tempFile = new File(filenamePrefix + outputFileSuffix + ".txt");
		if (!(tempFile.exists())) {
			outputFileName = tempFile.toString();
		} else {
			int fileCount = 1;
			while (tempFile.exists()) {
				tempFile = new File(filenamePrefix + outputFileSuffix + "." + fileCount + ".txt");
				if (tempFile.exists()) {
					fileCount++;
				}
			}
			outputFileName = tempFile.toString();
		}
		
		return outputFileName;
	}
}
