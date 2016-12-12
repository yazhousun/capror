package core;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExtractGenomeByNameEntry {

	public static void main(String[] args) {

		// Check if there are enough input parameters
		if (args == null || args.length < 3) {
			PrintUsage.print();
			System.exit(0);
		}

		String genomeListFile = args[0];
		String groupSeqList = args[1];
		String outputDirectory = args[2];

		// Check if the file exists
		File tempFile = new File(genomeListFile);
		if (!(tempFile.exists() && tempFile.isFile())) {
			PrintUsage.print();
			System.exit(0);
		}

		String outputFileNameOriginalFile = null;
		outputFileNameOriginalFile = tempFile.getName();
		outputFileNameOriginalFile = removeExtension(outputFileNameOriginalFile);

		// Check if the file exists
		tempFile = new File(groupSeqList);
		if (!(tempFile.exists() && tempFile.isFile())) {
			PrintUsage.print();
			System.exit(0);
		}

		String outputFileNamePrefix = null;
		outputFileNamePrefix = tempFile.getName();
		outputFileNamePrefix = removeExtension(outputFileNamePrefix);

		// Check if the output directory exists
		tempFile = new File(outputDirectory);
		if (!(tempFile.exists() && tempFile.isDirectory())) {
			PrintUsage.print();
			System.exit(0);
		}

		// Create output filename
		String outputFileName = null;

		tempFile = new File(
				outputDirectory + File.separator + outputFileNamePrefix + "."
						+ outputFileNameOriginalFile + ".fasta");
		if (!(tempFile.exists())) {
			outputFileName = tempFile.toString();
		} else {
			int fileCount = 1;
			while (tempFile.exists()) {
				tempFile = new File(outputDirectory + File.separator + outputFileNamePrefix + "."
						+ outputFileNameOriginalFile + "." + fileCount + ".fasta");
				if (tempFile.exists()) {
					fileCount++;
				}
			}
			outputFileName = tempFile.toString();
		}
		
		Date dStart = new Date();
		SimpleDateFormat currentTime = new SimpleDateFormat("hh:mm:ss a");

		System.out
				.println(currentTime.format(dStart) + " Currently collecting genomes of organisms in " + groupSeqList);

		// Add names to Hash Set
		Scanner seqNameInput = getInput(groupSeqList);
		String temp = null;
		HashSet<String> seqNameList = new HashSet<String>();
		temp = seqNameInput.nextLine().trim();
		while (seqNameInput.hasNext()) {
			seqNameList.add(temp);
			temp = seqNameInput.nextLine().trim();
		}
		seqNameList.add(temp);

		// Collect corresponding sequences
		String seqName = null;

		Scanner genomeInput = getInput(genomeListFile);

		int totalGenomes = 0;

		try {
			PrintWriter output = new PrintWriter(outputFileName);

			temp = genomeInput.nextLine().trim();

			while (genomeInput.hasNext()) {

				if (temp.startsWith(">")) {

					seqName = temp.substring(1);

					if (seqNameList.contains(">" + seqName)) {

						output.println(temp);
						temp = genomeInput.nextLine().trim();

						while ((!temp.startsWith(">")) && genomeInput.hasNext()) {
							output.println(temp);
							temp = genomeInput.nextLine().trim();
						}

						if (temp.startsWith(">")) {
							totalGenomes++;
							continue;
						}

						if (!genomeInput.hasNext()) {
							output.println(temp);
							break;
						}

					}

				}
				temp = genomeInput.nextLine().trim();

			}
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		genomeInput.close();

		Date dEnd = new Date();
		System.out.println("Genome collection completed at " + currentTime.format(dEnd) + " for " + totalGenomes
				+ " genomes. Results stored in " + outputFileName);

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
