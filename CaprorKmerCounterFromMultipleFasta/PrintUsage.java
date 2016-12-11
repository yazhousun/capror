package core;

public class PrintUsage {
	public static void print()
	{
		System.err.println("Usage: java -jar CaprorKmerCounterFromMultipleFasta.java genomeListFile outputDirectory kmerLength\n"
				+ "==================\n"
				+ "genomeListFile   : file path to a file containing all genomes to study.\n"
				+ "outputDirectory  : file path to a folder that will contain the kmer results files.\n"
				+ "kmerLength       : must be an integer between 1 and 15.\n"
				+ "==================\n"
				+ "Author: Camille Menendez, Yazhou Sun, last update 2016-04-12\n");
		
	}
}
