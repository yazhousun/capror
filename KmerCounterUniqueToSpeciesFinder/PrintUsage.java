package core;

public class PrintUsage {
	public static void print()
	{
		System.err.println("Usage: java -jar KmerCounterUniqueToSpeciesFinder.java kmerListFile outputDirectory kmerLength\n"
				+ "==================\n"
				+ "kmerListFileAll  : file path to a file containing all kmers to study.\n"
				+ "kmerListFileGroup: file path to a file containing kmers in a specific group.\n"
				+ "outputDirectory  : file path to a folder that will contain the kmer results files.\n"
				+ "kmerLength       : must be an integer between 1 and 15. Must match the kmerListFile kmer value.\n"
				+ "==================\n"
				+ "Author: Camille Menendez, Yazhou Sun, last update 2016-04-07\n");
		
	}
	
	public static void groupValueLarger()
	{
		System.err.println("Kmer Value for group is larger than Kmer Value for all. Check usage.\n");
	}
}
