package core;

import java.io.*;
import java.util.*;

public class CheckGenomeLengthsEntry {

	public static void main(String[] args) throws Exception {

		if (args == null || args.length < 2) {
			PrintUsage.print();
			System.exit(0);
		}

		String genomeListDirectory = args[0];
		String outputDirectory = args[1];

		File tempFile = new File(genomeListDirectory);
		if (!(tempFile.exists() && tempFile.isDirectory())) {
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

		int totalGenomes = 0;

		String temp = null;
		String seqName = null;
		StringBuilder seq = null;
		String sequence = null;

		String outputFileName = null;

		tempFile = new File(outputDirectory + File.separator + outputFileNamePrefix + ".seqNameAndLength.txt");
		if (!(tempFile.exists())) {
			outputFileName = tempFile.toString();
		} else {
			int fileCount = 1;
			while (tempFile.exists()) {
				tempFile = new File(outputDirectory + File.separator + outputFileNamePrefix + ".seqNameAndLength."
						+ fileCount + ".txt");
				if (tempFile.exists()) {
					fileCount++;
				}
			}
			outputFileName = tempFile.toString();
		}

		try {
			PrintWriter output = new PrintWriter(outputFileName);

			Scanner input = null;
			File baseDir = new File(genomeListDirectory);
			String[] files = baseDir.list();

			for (int i = 0; i < files.length; i++) {

				input = getInput(baseDir + File.separator + files[i]);
				temp = input.nextLine().trim();

				while (input.hasNext()) {

					if (temp.startsWith(">")) {
						seq = new StringBuilder();

						seqName = temp.substring(1);

						temp = input.nextLine().trim();
						while ((!temp.startsWith(">")) && input.hasNext()) {
							seq.append(temp);
							temp = input.nextLine().trim();
						}

						output.print(seqName + "\t" + seq.length() + "\n");

						if (temp.startsWith(">")) {
							sequence = seq.toString();
							totalGenomes++;
							continue;
						}

						if (!input.hasNext()) {
							seq.append(temp);
							sequence = seq.toString();
							totalGenomes++;
							break;
						}
					}

				}

			}
			input.close();

			output.close();
		} catch (FileNotFoundException e)

		{
			e.printStackTrace();
		}

		System.out.println("Genome length collected for " + totalGenomes + " genomes.");

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
