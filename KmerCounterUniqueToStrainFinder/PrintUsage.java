package core;

public class PrintUsage {
	public static void print()
	{
		System.err.println("Usage: java -jar KmerCounterUniqueToStrainFinder.java kmerListFile outputDirectory kmerLength\n"
				+ "==================\n"
				+ "kmerListFile     : file path to a file containing all kmers to study.\n"
				+ "outputDirectory  : file path to a folder that will contain the kmer results files.\n"
				+ "kmerLength       : must be an integer between 1 and 15. Must match the kmerListFile kmer value.\n"
				+ "==================\n"
				+ "Author: Camille Menendez, Yazhou Sun, last update 2016-04-07\n");
		
	}
}
