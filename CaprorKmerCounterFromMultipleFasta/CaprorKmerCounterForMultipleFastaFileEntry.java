package core;

import java.io.File;

public class CaprorKmerCounterForMultipleFastaFileEntry
{

	public static void main(String[] args)
	{ 
		if (args == null || args.length < 3) {
			PrintUsage.print();
			System.exit(0);
		}

		String genomeListFile = args[0];
		String outputDirectory = args[1];
		int kmerLength = Integer.parseInt(args[2]);

		File tempFile = new File(genomeListFile);
		if (!(tempFile.exists() && tempFile.isFile())) {
			PrintUsage.print();
			System.exit(0);
		}
		
		String outputFileNamePrefix = null;
		outputFileNamePrefix = tempFile.getName();
		outputFileNamePrefix = IoHelper.removeExtension(outputFileNamePrefix);
		
		tempFile = new File(outputDirectory);
		if (!(tempFile.exists() && tempFile.isDirectory())) {
			PrintUsage.print();
			System.exit(0);
		}
		
		KmerCounterForMultipleFastaFile.compute(genomeListFile, outputDirectory, outputFileNamePrefix, kmerLength);
		
	}

}
