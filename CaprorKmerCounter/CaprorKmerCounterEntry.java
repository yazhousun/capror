package core;

import java.io.File;

public class CaprorKmerCounterEntry {

	public static void main(String[] args) {
		if (args == null || args.length < 5) {
			PrintUsage.print();
			System.exit(0);
		}

		String genomeListDirectory = args[0];
		String mostRecentVersionsFile = args[1];
		String outputDirectory = args[2];
		int kmerLength = Integer.parseInt(args[3]);
		String outputFileNamePrefix = args[4];
		String filename = null;
		
		int lastSeparatorIndex = outputFileNamePrefix.lastIndexOf(File.separator);
		if (lastSeparatorIndex == -1)
		{
			filename = outputFileNamePrefix;
		} else
		{
			filename = outputFileNamePrefix.substring(lastSeparatorIndex + 1);
		}

		int extensionIndex = filename.lastIndexOf('.');
		if (extensionIndex == filename.length()-1){
			outputFileNamePrefix = filename;
		}
		else {
			outputFileNamePrefix = filename + ".";
		}
		
		File tempFile = new File(genomeListDirectory);
		if (!(tempFile.exists() && tempFile.isDirectory())) {
			PrintUsage.print();
			System.exit(0);
		}

		tempFile = new File(mostRecentVersionsFile);
		if (!(tempFile.exists() && tempFile.isFile())) {
			PrintUsage.print();
			System.exit(0);
		}

		tempFile = new File(outputDirectory);
		if (!(tempFile.exists() && tempFile.isDirectory())) {
			PrintUsage.print();
			System.exit(0);
		}

		KmerCounter.compute(genomeListDirectory, mostRecentVersionsFile, outputDirectory, outputFileNamePrefix, kmerLength);
	}

}
